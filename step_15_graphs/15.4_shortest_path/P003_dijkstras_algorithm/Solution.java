import java.util.*;

/**
 * Problem: Dijkstra's Algorithm
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Bellman-Ford Style
    // Time: O(V * E)  |  Space: O(V)
    // Relax all edges V-1 times. Correct but slow for non-negative weights.
    // ============================================================
    public static int[] bruteForce(int V, List<int[]>[] adj, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // Collect all edges from adjacency list
        List<int[]> edges = new ArrayList<>();
        for (int u = 0; u < V; u++) {
            if (adj[u] == null) continue;
            for (int[] edge : adj[u]) {
                edges.add(new int[]{u, edge[0], edge[1]});
            }
        }

        // Relax all edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            boolean updated = false;
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        return dist;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Dijkstra with Min-Heap
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Greedy: always expand nearest unfinalized vertex via priority queue.
    // Uses lazy deletion for stale heap entries.
    // ============================================================
    public static int[] optimal(int V, List<int[]>[] adj, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // Min-heap: {distance, vertex}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int d = top[0], u = top[1];

            // Lazy deletion: skip stale entries
            if (d > dist[u]) continue;

            // Relax all neighbors
            if (adj[u] != null) {
                for (int[] edge : adj[u]) {
                    int v = edge[0], w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        pq.offer(new int[]{dist[v], v});
                    }
                }
            }
        }

        return dist;
    }

    // ============================================================
    // APPROACH 3: BEST -- Dijkstra with TreeSet (Decrease-Key)
    // Time: O((V + E) log V)  |  Space: O(V)
    // TreeSet acts as indexed PQ with true decrease-key.
    // Heap size stays at most V (no stale entries).
    // ============================================================
    public static int[] best(int V, List<int[]>[] adj, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // TreeSet of {distance, vertex} -- supports O(log n) removal
        TreeSet<int[]> set = new TreeSet<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });
        set.add(new int[]{0, src});

        while (!set.isEmpty()) {
            int[] top = set.pollFirst();
            int d = top[0], u = top[1];

            if (adj[u] != null) {
                for (int[] edge : adj[u]) {
                    int v = edge[0], w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        // Decrease-key: remove old, insert new
                        if (dist[v] != Integer.MAX_VALUE) {
                            set.remove(new int[]{dist[v], v});
                        }
                        dist[v] = dist[u] + w;
                        set.add(new int[]{dist[v], v});
                    }
                }
            }
        }

        return dist;
    }

    // ============================================================
    // HELPER: Build undirected adjacency list
    // ============================================================
    @SuppressWarnings("unchecked")
    public static List<int[]>[] buildGraph(int V, int[][] edges) {
        List<int[]>[] adj = new ArrayList[V];
        for (int i = 0; i < V; i++) adj[i] = new ArrayList<>();
        for (int[] e : edges) {
            adj[e[0]].add(new int[]{e[1], e[2]});
            adj[e[1]].add(new int[]{e[0], e[2]});
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Dijkstra's Algorithm ===");

        int V = 5;
        int[][] edges = {{0,1,2},{0,3,6},{1,2,3},{1,3,8},{1,4,5},{2,4,7}};
        List<int[]>[] adj = buildGraph(V, edges);

        System.out.println("Brute:   " + Arrays.toString(bruteForce(V, adj, 0)));
        System.out.println("Optimal: " + Arrays.toString(optimal(V, adj, 0)));
        System.out.println("Best:    " + Arrays.toString(best(V, adj, 0)));
        // Expected: [0, 2, 5, 6, 7]

        // Edge case: disconnected graph
        int[][] edges2 = {{0,1,3}};
        List<int[]>[] adj2 = buildGraph(3, edges2);
        System.out.println("\nDisconnected: " + Arrays.toString(optimal(3, adj2, 0)));
        // Expected: [0, 3, 2147483647]
    }
}
