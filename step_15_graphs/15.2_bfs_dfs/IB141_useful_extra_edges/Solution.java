/**
 * Problem: Useful Extra Edges
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given a weighted undirected graph with N nodes and M edges, a source S,
 * destination T, and a list of extra edges (u, v, w), find the shortest
 * path from S to T, optionally using at most ONE extra edge.
 *
 * Approach: Run Dijkstra from S and from T to get dist_from_s[] and
 * dist_from_t[]. Then for each extra edge (u,v,w), try:
 *   dist_from_s[u] + w + dist_from_t[v]
 *   dist_from_s[v] + w + dist_from_t[u]
 * Return minimum of original shortest path and all extra-edge candidates.
 *
 * Real-life use: Road network augmentation, telecom relay planning.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(E * (V + E) log V)  |  Space: O(V + E)
    // For each extra edge, add it to the graph and run Dijkstra.
    // Then take the minimum over all runs.
    // ============================================================
    public static int bruteForce(int n, int[][] edges, int src, int dst,
                                  int[][] extraEdges) {
        // Run Dijkstra on original graph
        int baseResult = dijkstra(n, buildAdj(n, edges), src, dst);

        int best = baseResult;
        for (int[] extra : extraEdges) {
            // Add extra edge temporarily
            int[][] newEdges = Arrays.copyOf(edges, edges.length + 1);
            newEdges[edges.length] = new int[]{extra[0], extra[1], extra[2]};
            int res = dijkstra(n, buildAdj(n, newEdges), src, dst);
            best = Math.min(best, res);
        }
        return best == INF ? -1 : best;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Two Dijkstra runs: from S and from T. Then check each extra edge.
    // This avoids running Dijkstra for each extra edge separately.
    // ============================================================
    public static int optimal(int n, int[][] edges, int src, int dst,
                               int[][] extraEdges) {
        List<int[]>[] adj = buildAdj(n, edges);
        int[] distS = dijkstraFull(n, adj, src);
        int[] distT = dijkstraFull(n, adj, dst);

        // Original shortest path
        int result = distS[dst];

        // Try using exactly one extra edge
        for (int[] e : extraEdges) {
            int u = e[0], v = e[1], w = e[2];
            // Path: S -> u -> v -> T  (using extra edge u-v)
            if (distS[u] != INF && distT[v] != INF)
                result = Math.min(result, distS[u] + w + distT[v]);
            // Path: S -> v -> u -> T  (undirected extra edge)
            if (distS[v] != INF && distT[u] != INF)
                result = Math.min(result, distS[v] + w + distT[u]);
        }
        return result == INF ? -1 : result;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Same two-Dijkstra approach, cleaner code with modular helpers.
    // This is the standard interview solution.
    // ============================================================
    public static int best(int n, int[][] edges, int src, int dst,
                            int[][] extraEdges) {
        List<int[]>[] adj = buildAdj(n, edges);
        int[] ds = dijkstraFull(n, adj, src);
        int[] dt = dijkstraFull(n, adj, dst);

        int ans = ds[dst];
        for (int[] e : extraEdges) {
            int u = e[0], v = e[1], w = e[2];
            if (ds[u] < INF && dt[v] < INF)
                ans = Math.min(ans, ds[u] + w + dt[v]);
            if (ds[v] < INF && dt[u] < INF)
                ans = Math.min(ans, ds[v] + w + dt[u]);
        }
        return ans >= INF ? -1 : ans;
    }

    // --- Helpers ---
    @SuppressWarnings("unchecked")
    private static List<int[]>[] buildAdj(int n, int[][] edges) {
        List<int[]>[] adj = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(new int[]{e[1], e[2]});
            adj[e[1]].add(new int[]{e[0], e[2]});
        }
        return adj;
    }

    private static int dijkstra(int n, List<int[]>[] adj, int src, int dst) {
        int[] dist = dijkstraFull(n, adj, src);
        return dist[dst];
    }

    private static int[] dijkstraFull(int n, List<int[]>[] adj, int src) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int d = curr[0], u = curr[1];
            if (d > dist[u]) continue;
            for (int[] nb : adj[u]) {
                int v = nb[0], w = nb[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    public static void main(String[] args) {
        System.out.println("=== Useful Extra Edges ===\n");

        // Graph: 1-2-3-4 line, extra edge 1->4 with weight 2
        int n = 4;
        int[][] edges = {{1, 2, 5}, {2, 3, 5}, {3, 4, 5}};
        int[][] extra = {{1, 4, 2}};
        // Without extra: 1->2->3->4 = 15. With extra: 1->4 = 2.
        System.out.println("Test 1:");
        System.out.println("  Brute:   " + bruteForce(n, edges, 1, 4, extra)); // 2
        System.out.println("  Optimal: " + optimal(n, edges, 1, 4, extra));    // 2
        System.out.println("  Best:    " + best(n, edges, 1, 4, extra));       // 2

        // No extra edge helps
        int[][] extra2 = {{2, 3, 100}};
        System.out.println("\nTest 2 (extra edge useless):");
        System.out.println("  Best: " + best(n, edges, 1, 4, extra2)); // 15

        // Disconnected graph
        int[][] edges3 = {{1, 2, 5}};
        int[][] extra3 = {{2, 3, 3}};
        System.out.println("\nTest 3 (extra makes path possible):");
        System.out.println("  Best: " + best(3, edges3, 1, 3, extra3)); // 8
    }
}
