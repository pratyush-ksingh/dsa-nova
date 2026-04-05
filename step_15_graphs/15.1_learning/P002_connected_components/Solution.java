/**
 * Problem: Connected Components (LeetCode #323 equivalent)
 * Difficulty: EASY | XP: 10
 *
 * Given n nodes and undirected edges, find the number of connected components.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: DFS
// Time: O(V + E) | Space: O(V + E)
// ============================================================
class DFSSolution {
    public int countComponents(int n, int[][] edges) {
        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        boolean[] visited = new boolean[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(i, adj, visited);
            }
        }
        return count;
    }

    private void dfs(int node, List<List<Integer>> adj, boolean[] visited) {
        visited[node] = true;
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adj, visited);
            }
        }
    }
}

// ============================================================
// Approach 2: BFS
// Time: O(V + E) | Space: O(V + E)
// ============================================================
class BFSSolution {
    public int countComponents(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        boolean[] visited = new boolean[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i);
                visited[i] = true;
                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int neighbor : adj.get(node)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        return count;
    }
}

// ============================================================
// Approach 3: Union-Find (Disjoint Set Union)
// Time: O(V + E * alpha(V)) ~ O(V + E) | Space: O(V)
// ============================================================
class UnionFindSolution {
    private int[] parent, rank;

    public int countComponents(int n, int[][] edges) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int components = n;
        for (int[] e : edges) {
            if (union(e[0], e[1])) {
                components--;
            }
        }
        return components;
    }

    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    private boolean union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return false;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Connected Components ===\n");

        int[][][] edgeSets = {
            {{0, 1}, {1, 2}, {3, 4}},
            {{0, 1}, {1, 2}, {2, 3}, {3, 4}},
            {},
            {{0, 1}},
        };
        int[] ns = {5, 5, 4, 3};
        int[] expected = {2, 1, 4, 2};

        DFSSolution dfs = new DFSSolution();
        BFSSolution bfs = new BFSSolution();
        UnionFindSolution uf = new UnionFindSolution();

        for (int t = 0; t < edgeSets.length; t++) {
            int dfsResult = dfs.countComponents(ns[t], edgeSets[t]);
            int bfsResult = bfs.countComponents(ns[t], edgeSets[t]);
            int ufResult = uf.countComponents(ns[t], edgeSets[t]);

            System.out.println("n=" + ns[t] + ", edges=" + Arrays.deepToString(edgeSets[t]));
            System.out.println("  DFS:        " + dfsResult);
            System.out.println("  BFS:        " + bfsResult);
            System.out.println("  Union-Find: " + ufResult);
            System.out.println("  Expected:   " + expected[t]);
            boolean pass = dfsResult == expected[t] && bfsResult == expected[t] && ufResult == expected[t];
            System.out.println("  Pass:       " + pass + "\n");
        }
    }
}
