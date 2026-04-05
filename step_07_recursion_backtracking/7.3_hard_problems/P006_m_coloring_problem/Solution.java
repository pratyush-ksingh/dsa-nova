/**
 * Problem: M Coloring Problem
 * Difficulty: HARD | XP: 50
 *
 * Given an undirected graph and M colors, determine if the graph can be colored
 * using at most M colors such that no two adjacent vertices share the same color.
 *
 * Classic graph backtracking / constraint satisfaction problem.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Backtracking, check all color assignments
    // Time: O(M^V)  |  Space: O(V) recursion + color array
    // Try assigning each color to each vertex; backtrack on conflict
    // ============================================================
    public static boolean bruteForce(int[][] graph, int m, int v) {
        int[] colors = new int[v];
        return solveBrute(graph, m, colors, 0);
    }

    private static boolean solveBrute(int[][] graph, int m, int[] colors, int node) {
        if (node == colors.length) return true;
        for (int color = 1; color <= m; color++) {
            if (isSafe(graph, colors, node, color)) {
                colors[node] = color;
                if (solveBrute(graph, m, colors, node + 1)) return true;
                colors[node] = 0;
            }
        }
        return false;
    }

    private static boolean isSafe(int[][] graph, int[] colors, int node, int color) {
        for (int neighbor = 0; neighbor < graph.length; neighbor++) {
            if (graph[node][neighbor] == 1 && colors[neighbor] == color) return false;
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Backtracking with adjacency list representation
    // Time: O(M^V)  |  Space: O(V + E)
    // Same logic, but adjacency list avoids O(V) neighbor scan per step
    // ============================================================
    public static boolean optimal(List<List<Integer>> adj, int m, int v) {
        int[] colors = new int[v];
        return solveOptimal(adj, m, colors, 0);
    }

    private static boolean solveOptimal(List<List<Integer>> adj, int m,
                                         int[] colors, int node) {
        if (node == colors.length) return true;
        for (int color = 1; color <= m; color++) {
            if (isSafeAdj(adj, colors, node, color)) {
                colors[node] = color;
                if (solveOptimal(adj, m, colors, node + 1)) return true;
                colors[node] = 0;
            }
        }
        return false;
    }

    private static boolean isSafeAdj(List<List<Integer>> adj, int[] colors,
                                      int node, int color) {
        for (int neighbor : adj.get(node)) {
            if (colors[neighbor] == color) return false;
        }
        return true;
    }

    // ============================================================
    // APPROACH 3: BEST - Backtracking + bitmask to track used colors per node
    // Time: O(M^V)  |  Space: O(V + E)
    // Use an int bitmask per node to track which colors are forbidden (O(1) check)
    // ============================================================
    public static boolean best(List<List<Integer>> adj, int m, int v) {
        int[] colors = new int[v];
        int[] usedColors = new int[v]; // bitmask of colors used by neighbors
        return solveBest(adj, m, colors, usedColors, 0);
    }

    private static boolean solveBest(List<List<Integer>> adj, int m,
                                      int[] colors, int[] usedColors, int node) {
        if (node == colors.length) return true;
        for (int color = 1; color <= m; color++) {
            if ((usedColors[node] & (1 << color)) == 0) {
                colors[node] = color;
                // Mark this color as forbidden for all neighbors
                for (int nb : adj.get(node)) usedColors[nb] |= (1 << color);
                if (solveBest(adj, m, colors, usedColors, node + 1)) return true;
                // Unmark
                for (int nb : adj.get(node)) usedColors[nb] &= ~(1 << color);
                colors[node] = 0;
            }
        }
        return false;
    }

    // Helper: build adjacency list from adjacency matrix
    private static List<List<Integer>> toAdjList(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (graph[i][j] == 1) neighbors.add(j);
            }
            adj.add(neighbors);
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== M Coloring Problem ===");

        // Graph: 4-cycle  0-1-2-3-0  (needs >= 2 colors; complete would need 4)
        int[][] graph = {
            {0, 1, 1, 1},
            {1, 0, 1, 0},
            {1, 1, 0, 1},
            {1, 0, 1, 0}
        };
        int v = 4;
        List<List<Integer>> adj = toAdjList(graph);

        System.out.println("Graph: 4-node (see adjacency matrix)");

        System.out.println("M=3:");
        System.out.println("  Brute:   " + bruteForce(graph, 3, v));
        System.out.println("  Optimal: " + optimal(adj, 3, v));
        System.out.println("  Best:    " + best(adj, 3, v));

        System.out.println("M=2:");
        System.out.println("  Brute:   " + bruteForce(graph, 2, v));
        System.out.println("  Optimal: " + optimal(adj, 2, v));
        System.out.println("  Best:    " + best(adj, 2, v));

        // Complete graph K4 — needs exactly 4 colors
        int[][] k4 = {
            {0,1,1,1},{1,0,1,1},{1,1,0,1},{1,1,1,0}
        };
        List<List<Integer>> adjK4 = toAdjList(k4);
        System.out.println("\nK4 (complete graph, needs 4 colors):");
        System.out.println("M=3: " + bruteForce(k4, 3, 4) + "  M=4: " + bruteForce(k4, 4, 4));
    }
}
