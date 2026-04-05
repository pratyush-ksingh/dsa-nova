import java.util.*;

/**
 * Problem: Number of Ways to Arrive at Destination (LeetCode #1976)
 * Difficulty: MEDIUM | XP: 25
 *
 * Count the number of ways to travel from node 0 to node n-1 via
 * the shortest path in a bidirectional weighted graph.
 * Return the count modulo 10^9 + 7.
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int MOD = 1_000_000_007;

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Dijkstra then DFS counting paths
    // Time: O(2^V) in worst case  |  Space: O(V + E)
    // Find the shortest distance first, then DFS to count all paths
    // of exactly that length.
    // ============================================================
    public static int bruteForce(int n, int[][] roads) {
        List<int[]>[] adj = buildAdj(n, roads);

        // Step 1: Dijkstra to find shortest distance
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE / 2);
        dist[0] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.offer(new long[]{0, 0});
        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            long d = curr[0]; int u = (int) curr[1];
            if (d > dist[u]) continue;
            for (int[] nb : adj[u]) {
                if (dist[u] + nb[1] < dist[nb[0]]) {
                    dist[nb[0]] = dist[u] + nb[1];
                    pq.offer(new long[]{dist[nb[0]], nb[0]});
                }
            }
        }

        long targetDist = dist[n - 1];
        if (targetDist == Long.MAX_VALUE / 2) return 0;

        // Step 2: DFS counting paths of exactly targetDist
        boolean[] visited = new boolean[n];
        int[] count = {0};
        dfs(0, 0, targetDist, n, adj, visited, count);
        return count[0];
    }

    private static void dfs(int node, long cost, long target, int n,
                             List<int[]>[] adj, boolean[] visited, int[] count) {
        if (node == n - 1 && cost == target) {
            count[0] = (count[0] + 1) % MOD;
            return;
        }
        if (cost >= target) return;
        visited[node] = true;
        for (int[] nb : adj[node]) {
            if (!visited[nb[0]] && cost + nb[1] <= target) {
                dfs(nb[0], cost + nb[1], target, n, adj, visited, count);
            }
        }
        visited[node] = false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Dijkstra tracking dist[] and ways[]
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Standard Dijkstra extended with a ways[] array.
    // - Shorter path found: reset ways[v] = ways[u]
    // - Equal path found:   ways[v] = (ways[v] + ways[u]) % MOD
    // ============================================================
    public static int optimal(int n, int[][] roads) {
        List<int[]>[] adj = buildAdj(n, roads);

        long[] dist = new long[n];
        long[] ways = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE / 2);
        dist[0] = 0;
        ways[0] = 1;

        // [cost, node] -- use long[] for large weights
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.offer(new long[]{0, 0});

        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            long d = curr[0]; int u = (int) curr[1];

            if (d > dist[u]) continue; // stale

            for (int[] nb : adj[u]) {
                int v = nb[0]; long w = nb[1];
                long newDist = dist[u] + w;

                if (newDist < dist[v]) {
                    // Strictly shorter path: reset ways
                    dist[v] = newDist;
                    ways[v] = ways[u];
                    pq.offer(new long[]{newDist, v});
                } else if (newDist == dist[v]) {
                    // Equal path: accumulate ways
                    ways[v] = (ways[v] + ways[u]) % MOD;
                }
            }
        }
        return (int) ways[n - 1];
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Dijkstra, adjacency as array for speed
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Identical algorithm to Approach 2. Uses array-of-lists adjacency
    // (pre-allocated) instead of HashMap for O(1) neighbor access.
    // ============================================================
    public static int best(int n, int[][] roads) {
        // Same as optimal -- adjacency already built as array in buildAdj
        return optimal(n, roads);
    }

    @SuppressWarnings("unchecked")
    private static List<int[]>[] buildAdj(int n, int[][] roads) {
        List<int[]>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int[] r : roads) {
            adj[r[0]].add(new int[]{r[1], r[2]});
            adj[r[1]].add(new int[]{r[0], r[2]});
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Number of Ways to Arrive at Destination ===\n");

        // Example 1
        int[][] roads1 = {{0,6,7},{0,1,2},{1,2,3},{1,3,3},{6,3,3},{3,5,1},{6,5,1},{2,5,1},{0,4,5},{4,6,2}};
        System.out.println("Brute:   " + bruteForce(7, roads1));  // 4
        System.out.println("Optimal: " + optimal(7, roads1));     // 4
        System.out.println("Best:    " + best(7, roads1));        // 4

        System.out.println();
        // Two nodes, direct road
        System.out.println("Two nodes: " + optimal(2, new int[][]{{0,1,5}})); // 1

        System.out.println();
        // Two equal paths
        int[][] roads3 = {{0,1,1},{0,2,1},{1,3,1},{2,3,1}};
        System.out.println("Two equal: " + optimal(4, roads3)); // 2
    }
}
