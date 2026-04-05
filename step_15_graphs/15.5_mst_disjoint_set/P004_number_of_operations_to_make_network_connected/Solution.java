import java.util.*;

/**
 * Problem: Number of Operations to Make Network Connected (LeetCode #1319)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- DFS to Count Components
    // Time: O(V + E)  |  Space: O(V + E)
    // Build adjacency list, DFS to count connected components.
    // ============================================================
    public static int bruteForce(int n, int[][] connections) {
        if (connections.length < n - 1) return -1;

        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] conn : connections) {
            adj.get(conn[0]).add(conn[1]);
            adj.get(conn[1]).add(conn[0]);
        }

        // Count connected components via DFS
        boolean[] visited = new boolean[n];
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                components++;
                dfs(i, adj, visited);
            }
        }

        return components - 1;
    }

    private static void dfs(int node, List<List<Integer>> adj, boolean[] visited) {
        visited[node] = true;
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adj, visited);
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Union-Find
    // Time: O(E * alpha(V)) ~ O(E)  |  Space: O(V)
    // Process edges with Union-Find, count remaining components.
    // ============================================================
    static int[] parent, rank;

    static void initDSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;
        if (rank[px] < rank[py]) { int tmp = px; px = py; py = tmp; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
        return true;
    }

    public static int optimal(int n, int[][] connections) {
        if (connections.length < n - 1) return -1;

        initDSU(n);
        int components = n;

        for (int[] conn : connections) {
            if (union(conn[0], conn[1])) {
                components--;
            }
        }

        return components - 1;
    }

    // ============================================================
    // APPROACH 3: BEST -- Union-Find with Redundant Cable Tracking
    // Time: O(E * alpha(V)) ~ O(E)  |  Space: O(V)
    // Same Union-Find, also counts redundant cables for verification.
    // ============================================================
    public static int best(int n, int[][] connections) {
        if (connections.length < n - 1) return -1;

        initDSU(n);
        int components = n;
        int redundant = 0;

        for (int[] conn : connections) {
            if (union(conn[0], conn[1])) {
                components--;
            } else {
                redundant++; // failed union = redundant cable
            }
        }

        // redundant >= components - 1 is guaranteed when connections.length >= n - 1
        // because: connections.length = (n - components) + redundant >= n - 1
        // => redundant >= n - 1 - n + components = components - 1
        return components - 1;
    }

    public static void main(String[] args) {
        System.out.println("=== Number of Operations to Make Network Connected ===\n");

        // Example 1
        int[][] conn1 = {{0,1},{0,2},{1,2}};
        System.out.println("Brute:   " + bruteForce(4, conn1));   // 1
        System.out.println("Optimal: " + optimal(4, conn1));       // 1
        System.out.println("Best:    " + best(4, conn1));          // 1

        // Example 2
        int[][] conn2 = {{0,1},{0,2},{0,3},{1,2},{1,3}};
        System.out.println("\nOptimal: " + optimal(6, conn2));     // 2

        // Impossible case
        int[][] conn3 = {{0,1},{0,2},{0,3},{1,2}};
        System.out.println("Impossible: " + optimal(6, conn3));    // -1

        // Already connected
        int[][] conn4 = {{0,1},{1,2},{2,3}};
        System.out.println("Connected: " + optimal(4, conn4));     // 0

        // Single node
        System.out.println("Single: " + optimal(1, new int[][]{}));// 0
    }
}
