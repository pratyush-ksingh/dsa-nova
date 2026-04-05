/**
 * Problem: Floyd Warshall Algorithm
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a weighted directed graph with V vertices as an adjacency matrix,
 * find the shortest path between every pair of vertices.
 * Use INF = 1e8 (not Integer.MAX_VALUE) to safely perform intermediate additions.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int INF = (int) 1e8;

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Dijkstra from every source vertex
    // Time: O(V * (V + E) * log V)  |  Space: O(V^2)
    // ============================================================
    static class BruteForce {
        /**
         * Build an adjacency list from the matrix, then run Dijkstra
         * once per source vertex.  Works only on non-negative weight graphs.
         */
        public static int[][] solve(int[][] matrix) {
            int V = matrix.length;
            // Build adjacency list
            List<List<int[]>> adj = new ArrayList<>();
            for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
            for (int u = 0; u < V; u++) {
                for (int v = 0; v < V; v++) {
                    if (u != v && matrix[u][v] != INF) {
                        adj.get(u).add(new int[]{v, matrix[u][v]});
                    }
                }
            }

            int[][] result = new int[V][V];
            for (int src = 0; src < V; src++) {
                result[src] = dijkstra(adj, V, src);
            }
            return result;
        }

        private static int[] dijkstra(List<List<int[]>> adj, int V, int src) {
            int[] dist = new int[V];
            Arrays.fill(dist, INF);
            dist[src] = 0;
            // Min-heap: {distance, vertex}
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            pq.offer(new int[]{0, src});

            while (!pq.isEmpty()) {
                int[] curr = pq.poll();
                int d = curr[0], u = curr[1];
                if (d > dist[u]) continue;
                for (int[] edge : adj.get(u)) {
                    int v = edge[0], w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        pq.offer(new int[]{dist[v], v});
                    }
                }
            }
            return dist;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Classic Floyd-Warshall triple loop
    // Time: O(V^3)  |  Space: O(V^2)
    // ============================================================
    static class Optimal {
        /**
         * dp[i][j] = shortest distance from i to j.
         * For each intermediate k: dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j]).
         * Process all k before moving to k+1 — order of outer loop matters!
         */
        public static int[][] solve(int[][] matrix) {
            int V = matrix.length;
            int[][] dp = new int[V][V];

            // Copy matrix and ensure diagonal is 0
            for (int i = 0; i < V; i++) {
                dp[i] = Arrays.copyOf(matrix[i], V);
                dp[i][i] = 0;
            }

            // Relax through each intermediate vertex k
            for (int k = 0; k < V; k++) {
                for (int i = 0; i < V; i++) {
                    for (int j = 0; j < V; j++) {
                        if (dp[i][k] != INF && dp[k][j] != INF) {
                            dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j]);
                        }
                    }
                }
            }
            return dp;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Floyd-Warshall + Negative Cycle Detection
    // Time: O(V^3)  |  Space: O(V^2)
    // ============================================================
    static class Best {
        /**
         * Same as Optimal. After completion, check if any dp[i][i] < 0.
         * A negative diagonal entry means a negative-weight cycle passes through i.
         * Returns the distance matrix, or throws if a negative cycle is found.
         *
         * This is the answer interviewers want for "production-safe" Floyd-Warshall.
         */
        public static int[][] solve(int[][] matrix) {
            int V = matrix.length;
            int[][] dp = new int[V][V];

            for (int i = 0; i < V; i++) {
                dp[i] = Arrays.copyOf(matrix[i], V);
                dp[i][i] = 0;
            }

            for (int k = 0; k < V; k++) {
                for (int i = 0; i < V; i++) {
                    for (int j = 0; j < V; j++) {
                        if (dp[i][k] != INF && dp[k][j] != INF) {
                            dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j]);
                        }
                    }
                }
            }

            // Negative cycle detection
            for (int i = 0; i < V; i++) {
                if (dp[i][i] < 0) {
                    throw new RuntimeException("Negative cycle detected at vertex " + i);
                }
            }

            return dp;
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Floyd Warshall Algorithm ===\n");

        // Example: V=4
        // Edges: 0->1(3), 0->3(7), 1->0(8), 1->2(2), 2->0(5), 2->3(1), 3->0(2)
        int[][] graph = {
            {0,   3,   INF, 7  },
            {8,   0,   2,   INF},
            {5,   INF, 0,   1  },
            {2,   INF, INF, 0  }
        };

        System.out.println("Input adjacency matrix:");
        printMatrix(graph);

        System.out.println("\nBrute Force (Dijkstra from each vertex):");
        printMatrix(BruteForce.solve(graph));

        System.out.println("\nOptimal (Floyd-Warshall):");
        printMatrix(Optimal.solve(graph));

        System.out.println("\nBest (Floyd-Warshall + Negative Cycle Detection):");
        printMatrix(Best.solve(graph));

        // Test negative cycle detection
        System.out.println("\nTesting negative cycle detection...");
        int[][] negGraph = {
            {0,   1,   INF},
            {INF, 0,   -2 },
            {-1,  INF, 0  }
        };
        try {
            Best.solve(negGraph);
            System.out.println("No negative cycle (unexpected)");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    private static void printMatrix(int[][] mat) {
        for (int[] row : mat) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i] == INF ? "INF" : row[i]);
                if (i < row.length - 1) sb.append(", ");
            }
            sb.append("]");
            System.out.println(sb);
        }
    }
}
