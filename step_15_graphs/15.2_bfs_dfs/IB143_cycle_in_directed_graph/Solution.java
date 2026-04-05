import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - DFS with visited + recursion stack
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// For directed graphs, parent-tracking from undirected DFS does
// NOT work. We need two sets: globally visited nodes and nodes
// currently in the DFS recursion stack. If we reach a node
// already in the recursion stack, there is a back edge => cycle.
// ============================================================

class BruteForce {
    boolean[] visited, inStack;
    List<List<Integer>> adj;

    private boolean dfs(int node) {
        visited[node] = true;
        inStack[node] = true;

        for (int nb : adj.get(node)) {
            if (!visited[nb]) {
                if (dfs(nb)) return true;
            } else if (inStack[nb]) {
                return true; // back edge
            }
        }
        inStack[node] = false;
        return false;
    }

    public int solve(int V, int[][] edges) {
        adj = new ArrayList<>();
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) adj.get(e[0]).add(e[1]);

        visited = new boolean[V + 1];
        inStack = new boolean[V + 1];

        for (int i = 1; i <= V; i++) {
            if (!visited[i] && dfs(i)) return 1;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - 3-color DFS (WHITE/GRAY/BLACK)
// Time: O(V + E)  |  Space: O(V)
// ============================================================
// Color each node: 0=WHITE(unvisited), 1=GRAY(in progress),
// 2=BLACK(fully processed). If during DFS we encounter a GRAY
// node, we've found a back edge => cycle exists.
// This is the canonical approach taught in algorithms courses.
// ============================================================

class Optimal {
    int[] color;
    List<List<Integer>> adj;
    static final int WHITE = 0, GRAY = 1, BLACK = 2;

    private boolean dfs(int node) {
        color[node] = GRAY;
        for (int nb : adj.get(node)) {
            if (color[nb] == GRAY) return true;
            if (color[nb] == WHITE && dfs(nb)) return true;
        }
        color[node] = BLACK;
        return false;
    }

    public int solve(int V, int[][] edges) {
        adj = new ArrayList<>();
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) adj.get(e[0]).add(e[1]);

        color = new int[V + 1]; // all WHITE
        for (int i = 1; i <= V; i++) {
            if (color[i] == WHITE && dfs(i)) return 1;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 3: BEST - Kahn's Algorithm (Topological Sort BFS)
// Time: O(V + E)  |  Space: O(V + E)
// ============================================================
// Compute in-degrees. Add all zero-in-degree nodes to queue.
// BFS removes nodes and reduces neighbor in-degrees. If all V
// nodes are processed, there is no cycle (valid topological
// order exists). If processed < V, a cycle is present.
// Real-life use: detecting circular imports in Python/Node.js,
// deadlock detection in OS scheduling, CI/CD pipeline cycles.
// ============================================================

class Best {
    public int solve(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] indegree = new int[V + 1];
        for (int i = 0; i <= V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            indegree[e[1]]++;
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= V; i++) {
            if (indegree[i] == 0) q.add(i);
        }

        int processed = 0;
        while (!q.isEmpty()) {
            int node = q.poll();
            processed++;
            for (int nb : adj.get(node)) {
                if (--indegree[nb] == 0) q.add(nb);
            }
        }
        return processed == V ? 0 : 1;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Cycle in Directed Graph ===");

        // Test 1: cycle: 1->2->3->1
        int[][] e1 = {{1,2},{2,3},{3,1}};
        System.out.println("Test1 Brute   (expect 1): " + new BruteForce().solve(3, e1));
        System.out.println("Test1 Optimal (expect 1): " + new Optimal().solve(3, e1));
        System.out.println("Test1 Best    (expect 1): " + new Best().solve(3, e1));

        // Test 2: DAG (no cycle): 1->2->3, 1->3
        int[][] e2 = {{1,2},{2,3},{1,3}};
        System.out.println("Test2 Brute   (expect 0): " + new BruteForce().solve(3, e2));
        System.out.println("Test2 Optimal (expect 0): " + new Optimal().solve(3, e2));
        System.out.println("Test2 Best    (expect 0): " + new Best().solve(3, e2));

        // Test 3: self-loop 1->1
        int[][] e3 = {{1,1}};
        System.out.println("Test3 Best    (expect 1): " + new Best().solve(1, e3));

        // Test 4: disconnected DAG
        int[][] e4 = {{1,2},{3,4}};
        System.out.println("Test4 Best    (expect 0): " + new Best().solve(4, e4));

        // Test 5: disconnected with one cycle
        int[][] e5 = {{1,2},{3,4},{4,3}};
        System.out.println("Test5 Best    (expect 1): " + new Best().solve(4, e5));
    }
}
