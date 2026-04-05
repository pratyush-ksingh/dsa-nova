import java.util.*;

/**
 * Problem: Network Delay Time (LeetCode #743)
 * Difficulty: MEDIUM | XP: 25
 *
 * You have a network of n nodes (labeled 1..n). Given directed weighted
 * edges times[i] = [u, v, w], a signal is sent from node k.
 * Return the minimum time for ALL nodes to receive the signal, or -1
 * if it is impossible.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Bellman-Ford (V-1 relaxations)
    // Time: O(V * E)  |  Space: O(V)
    // Relax all edges V-1 times. Works for non-negative weights.
    // Answer is the max value in the dist array.
    // ============================================================
    public static int bruteForce(int[][] times, int n, int k) {
        final int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);
        dist[k] = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean updated = false;
            for (int[] e : times) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == INF) return -1;
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Dijkstra with Min-Heap
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Greedy SSSP: always expand the nearest unfinalized node.
    // Answer = max(dist[1..n]).
    // ============================================================
    public static int optimal(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] e : times) {
            adj.computeIfAbsent(e[0], x -> new ArrayList<>()).add(new int[]{e[1], e[2]});
        }

        final int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);
        dist[k] = 0;

        // PriorityQueue: [distance, node]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, k});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int d = curr[0], u = curr[1];

            if (d > dist[u]) continue; // stale entry

            List<int[]> neighbors = adj.getOrDefault(u, Collections.emptyList());
            for (int[] nb : neighbors) {
                int v = nb[0], w = nb[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }

        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == INF) return -1;
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }

    // ============================================================
    // APPROACH 3: BEST -- Dijkstra with early termination
    // Time: O((V + E) log V)  |  Space: O(V + E)
    // Same Dijkstra but tracks "visited" count. When the n-th node
    // is popped from the heap, that is our answer (no need to scan
    // the dist array afterward).
    // ============================================================
    public static int best(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] e : times) {
            adj.computeIfAbsent(e[0], x -> new ArrayList<>()).add(new int[]{e[1], e[2]});
        }

        final int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);
        dist[k] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, k});
        int visited = 0;

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int d = curr[0], u = curr[1];

            if (d > dist[u]) continue; // stale

            visited++;
            if (visited == n) return d; // all nodes finalized

            List<int[]> neighbors = adj.getOrDefault(u, Collections.emptyList());
            for (int[] nb : neighbors) {
                int v = nb[0], w = nb[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return -1; // some nodes unreachable
    }

    public static void main(String[] args) {
        System.out.println("=== Network Delay Time ===\n");

        // Example 1
        int[][] times1 = {{2,1,1},{2,3,1},{3,4,1}};
        System.out.println("Brute:   " + bruteForce(times1, 4, 2));  // 2
        System.out.println("Optimal: " + optimal(times1, 4, 2));     // 2
        System.out.println("Best:    " + best(times1, 4, 2));        // 2

        System.out.println();
        // Single node
        System.out.println("Single:  " + optimal(new int[][]{}, 1, 1)); // 0

        System.out.println();
        // Unreachable
        int[][] times3 = {{1,3,1}};
        System.out.println("Unreachable: " + optimal(times3, 3, 1));    // -1
    }
}
