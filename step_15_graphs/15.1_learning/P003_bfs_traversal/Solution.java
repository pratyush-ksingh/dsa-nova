/**
 * Problem: BFS Traversal
 * Difficulty: EASY | XP: 10
 *
 * Implement BFS on a graph. Return nodes in BFS order from source.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Standard BFS (Single Component)
// Time: O(V + E) | Space: O(V)
// ============================================================
class StandardBFS {
    public List<Integer> bfs(int V, List<List<Integer>> adj, int src) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[V];

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(src);
        visited[src] = true; // mark visited at ENQUEUE time

        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true; // mark before enqueue!
                    queue.offer(neighbor);
                }
            }
        }
        return result;
    }
}

// ============================================================
// Approach 2: BFS with Level Tracking
// Time: O(V + E) | Space: O(V)
// ============================================================
class LevelBFS {
    public List<List<Integer>> bfsLevels(int V, List<List<Integer>> adj, int src) {
        List<List<Integer>> levels = new ArrayList<>();
        boolean[] visited = new boolean[V];

        Queue<Integer> queue = new LinkedList<>();
        queue.offer(src);
        visited[src] = true;

        while (!queue.isEmpty()) {
            int size = queue.size(); // nodes at current level
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int node = queue.poll();
                level.add(node);
                for (int neighbor : adj.get(node)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
            levels.add(level);
        }
        return levels;
    }
}

// ============================================================
// Approach 3: BFS for Disconnected Graph (All Components)
// Time: O(V + E) | Space: O(V)
// ============================================================
class DisconnectedBFS {
    public List<Integer> bfsAll(int V, List<List<Integer>> adj) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i);
                visited[i] = true;
                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    result.add(node);
                    for (int neighbor : adj.get(node)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        return result;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {

    private static List<List<Integer>> buildAdj(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== BFS Traversal ===\n");

        StandardBFS standard = new StandardBFS();
        LevelBFS levelBfs = new LevelBFS();
        DisconnectedBFS disconnected = new DisconnectedBFS();

        // Test 1: Tree-like graph
        //   0
        //  / \
        // 1   2
        // |   |
        // 3   4
        int[][] edges1 = {{0,1},{0,2},{1,3},{2,4}};
        List<List<Integer>> adj1 = buildAdj(5, edges1);
        System.out.println("Test 1: Tree graph");
        System.out.println("  BFS from 0: " + standard.bfs(5, adj1, 0));
        System.out.println("  Levels:     " + levelBfs.bfsLevels(5, adj1, 0));
        System.out.println("  Expected:   [0, 1, 2, 3, 4]");
        System.out.println("  Pass: " + standard.bfs(5, adj1, 0).equals(Arrays.asList(0,1,2,3,4)));
        System.out.println();

        // Test 2: Graph with cycle
        // 0-1-2-0, 1-3
        int[][] edges2 = {{0,1},{1,2},{2,0},{1,3}};
        List<List<Integer>> adj2 = buildAdj(4, edges2);
        System.out.println("Test 2: Cyclic graph");
        System.out.println("  BFS from 0: " + standard.bfs(4, adj2, 0));
        System.out.println("  Levels:     " + levelBfs.bfsLevels(4, adj2, 0));
        System.out.println();

        // Test 3: Disconnected graph
        // 0-1, 2-3 (two components)
        int[][] edges3 = {{0,1},{2,3}};
        List<List<Integer>> adj3 = buildAdj(4, edges3);
        System.out.println("Test 3: Disconnected graph");
        System.out.println("  BFS from 0:  " + standard.bfs(4, adj3, 0));
        System.out.println("  BFS all:     " + disconnected.bfsAll(4, adj3));
        System.out.println("  Expected all: [0, 1, 2, 3]");
        System.out.println();

        // Test 4: Single node
        List<List<Integer>> adj4 = buildAdj(1, new int[][]{});
        System.out.println("Test 4: Single node");
        System.out.println("  BFS from 0: " + standard.bfs(1, adj4, 0));
        System.out.println("  Expected:   [0]");
        System.out.println("  Pass: " + standard.bfs(1, adj4, 0).equals(Arrays.asList(0)));
    }
}
