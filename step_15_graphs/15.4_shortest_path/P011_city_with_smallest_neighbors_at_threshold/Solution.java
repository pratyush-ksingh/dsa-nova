/**
 * Problem: City with Smallest Neighbors at Threshold (LeetCode 1334)
 * Difficulty: MEDIUM | XP: 25
 *
 * There are n cities numbered 0 to n-1. Given bidirectional weighted edges and
 * an integer distanceThreshold, return the city with the smallest number of
 * cities reachable within distance <= distanceThreshold.
 * On tie, return the city with the greatest number (largest index).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int INF = (int) 1e9;

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Dijkstra from every source city
    // Time: O(V * (V + E) * log V)  |  Space: O(V + E)
    // ============================================================
    static class BruteForce {
        /**
         * Build an undirected adjacency list. Run Dijkstra from every city.
         * Count cities reachable within threshold. Track minimum-count city,
         * using the larger index on ties.
         */
        public static int solve(int n, int[][] edges, int distanceThreshold) {
            List<List<int[]>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
            for (int[] e : edges) {
                adj.get(e[0]).add(new int[]{e[1], e[2]});
                adj.get(e[1]).add(new int[]{e[0], e[2]});
            }

            int resultCity = -1;
            int minCount = Integer.MAX_VALUE;

            for (int city = 0; city < n; city++) {
                int[] dist = dijkstra(adj, n, city);
                int count = 0;
                for (int other = 0; other < n; other++) {
                    if (other != city && dist[other] <= distanceThreshold) count++;
                }
                // Tie-break: prefer larger city index (use <=)
                if (count <= minCount) {
                    minCount = count;
                    resultCity = city;
                }
            }
            return resultCity;
        }

        private static int[] dijkstra(List<List<int[]>> adj, int n, int src) {
            int[] dist = new int[n];
            Arrays.fill(dist, INF);
            dist[src] = 0;
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
    // APPROACH 2: OPTIMAL — Floyd-Warshall + count reachable cities
    // Time: O(V^3)  |  Space: O(V^2)
    // ============================================================
    static class Optimal {
        /**
         * Build all-pairs shortest paths with Floyd-Warshall.
         * For each city, count how many others are reachable within threshold.
         * Return the city with the fewest reachable (largest index on tie).
         *
         * Preferred approach for n <= 100 (LeetCode constraint): simple, clean,
         * handles all edge cases, and O(n^3) is fast enough.
         */
        public static int solve(int n, int[][] edges, int distanceThreshold) {
            int[][] dist = new int[n][n];
            for (int[] row : dist) Arrays.fill(row, INF);
            for (int i = 0; i < n; i++) dist[i][i] = 0;
            for (int[] e : edges) {
                dist[e[0]][e[1]] = Math.min(dist[e[0]][e[1]], e[2]);
                dist[e[1]][e[0]] = Math.min(dist[e[1]][e[0]], e[2]);
            }

            // Floyd-Warshall
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dist[i][k] != INF && dist[k][j] != INF) {
                            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                        }
                    }
                }
            }

            int resultCity = -1;
            int minCount = Integer.MAX_VALUE;

            for (int city = 0; city < n; city++) {
                int count = 0;
                for (int other = 0; other < n; other++) {
                    if (other != city && dist[city][other] <= distanceThreshold) count++;
                }
                if (count <= minCount) {
                    minCount = count;
                    resultCity = city;
                }
            }
            return resultCity;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Floyd-Warshall, reverse iteration for clean tie-break
    // Time: O(V^3)  |  Space: O(V^2)
    // ============================================================
    static class Best {
        /**
         * Same Floyd-Warshall as Optimal, but the counting pass iterates
         * cities from n-1 down to 0 and uses strict < for count comparison.
         * Because we see larger indices first, the first city that achieves
         * the minimum count is automatically the correct tie-break winner —
         * no need for <= logic. We can also stop early once we find it.
         *
         * This is a minor code-quality improvement that interviewers appreciate
         * as it makes the tie-break rule explicit and self-documenting.
         */
        public static int solve(int n, int[][] edges, int distanceThreshold) {
            int[][] dist = new int[n][n];
            for (int[] row : dist) Arrays.fill(row, INF);
            for (int i = 0; i < n; i++) dist[i][i] = 0;
            for (int[] e : edges) {
                dist[e[0]][e[1]] = Math.min(dist[e[0]][e[1]], e[2]);
                dist[e[1]][e[0]] = Math.min(dist[e[1]][e[0]], e[2]);
            }

            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dist[i][k] != INF && dist[k][j] != INF) {
                            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                        }
                    }
                }
            }

            int resultCity = -1;
            int minCount = Integer.MAX_VALUE;

            // Iterate from largest city downward; strict < means first-found large city wins ties
            for (int city = n - 1; city >= 0; city--) {
                int count = 0;
                for (int other = 0; other < n; other++) {
                    if (other != city && dist[city][other] <= distanceThreshold) count++;
                }
                if (count < minCount) {
                    minCount = count;
                    resultCity = city;
                }
            }
            return resultCity;
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== City with Smallest Neighbors at Threshold ===\n");

        // LeetCode Example 1 — expected output: 3
        int n1 = 4;
        int[][] edges1 = {{0, 1, 3}, {1, 2, 1}, {1, 3, 4}, {2, 3, 1}};
        int thresh1 = 4;
        System.out.println("Example 1 (expected 3):");
        System.out.println("  Brute:   " + BruteForce.solve(n1, edges1, thresh1));
        System.out.println("  Optimal: " + Optimal.solve(n1, edges1, thresh1));
        System.out.println("  Best:    " + Best.solve(n1, edges1, thresh1));

        // LeetCode Example 2 — expected output: 0
        int n2 = 5;
        int[][] edges2 = {{0, 1, 2}, {0, 4, 8}, {1, 2, 3}, {1, 4, 2}, {2, 3, 1}, {3, 4, 1}};
        int thresh2 = 2;
        System.out.println("\nExample 2 (expected 0):");
        System.out.println("  Brute:   " + BruteForce.solve(n2, edges2, thresh2));
        System.out.println("  Optimal: " + Optimal.solve(n2, edges2, thresh2));
        System.out.println("  Best:    " + Best.solve(n2, edges2, thresh2));
    }
}
