import java.util.*;

/**
 * Problem: Cheapest Flights Within K Stops (LeetCode #787)
 * Difficulty: MEDIUM | XP: 25
 *
 * There are n cities connected by directed flights. Given flights as
 * [from, to, price], find the cheapest price from src to dst with at
 * most k stops. Return -1 if no such route exists.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- BFS exploring all paths with <= k stops
    // Time: O(n^k)  |  Space: O(n*k)
    // BFS state: (node, cost, stops_used). No sophisticated pruning.
    // ============================================================
    public static int bruteForce(int n, int[][] flights, int src, int dst, int k) {
        // Build adjacency list
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] f : flights) {
            adj.computeIfAbsent(f[0], x -> new ArrayList<>()).add(new int[]{f[1], f[2]});
        }

        // BFS queue: [node, cost, stops_used]
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{src, 0, 0});

        // Track min cost to reach each (node, stops) pair
        int[][] minCost = new int[n][k + 2];
        for (int[] row : minCost) Arrays.fill(row, Integer.MAX_VALUE);
        minCost[src][0] = 0;

        int best = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int node = curr[0], cost = curr[1], stops = curr[2];

            if (node == dst) {
                best = Math.min(best, cost);
                continue;
            }
            if (stops > k) continue;

            List<int[]> neighbors = adj.getOrDefault(node, Collections.emptyList());
            for (int[] nb : neighbors) {
                int neighbor = nb[0], price = nb[1];
                int newCost = cost + price;
                int newStops = stops + 1;
                if (newCost < minCost[neighbor][newStops] && newCost < best) {
                    minCost[neighbor][newStops] = newCost;
                    queue.offer(new int[]{neighbor, newCost, newStops});
                }
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Bellman-Ford with k+1 relaxation rounds
    // Time: O(k * E)  |  Space: O(n)
    // Each round allows one more edge (= one more stop). After k+1 rounds,
    // dist[dst] holds the cheapest k-stop route. A temp copy prevents
    // chaining multiple relaxations within the same round.
    // ============================================================
    public static int optimal(int n, int[][] flights, int src, int dst, int k) {
        final int INF = Integer.MAX_VALUE / 2;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int i = 0; i <= k; i++) {          // k+1 rounds = at most k intermediate stops
            int[] temp = dist.clone();           // snapshot: prevents multi-hop in one round
            for (int[] f : flights) {
                int u = f[0], v = f[1], w = f[2];
                if (dist[u] != INF && dist[u] + w < temp[v]) {
                    temp[v] = dist[u] + w;
                }
            }
            dist = temp;
        }
        return dist[dst] >= INF ? -1 : dist[dst];
    }

    // ============================================================
    // APPROACH 3: BEST -- Dijkstra with (cost, node, stops) state
    // Time: O(k * E * log(k*n))  |  Space: O(k*n)
    // Extend Dijkstra's state with stops_used. A node can be
    // revisited at different stop counts. Prune (node, stops) pairs
    // where we've already found a cheaper route.
    // ============================================================
    public static int best(int n, int[][] flights, int src, int dst, int k) {
        final int INF = Integer.MAX_VALUE / 2;

        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] f : flights) {
            adj.computeIfAbsent(f[0], x -> new ArrayList<>()).add(new int[]{f[1], f[2]});
        }

        // minCost[node][stops] = cheapest cost reaching node with exactly 'stops' stops used
        int[][] minCost = new int[n][k + 2];
        for (int[] row : minCost) Arrays.fill(row, INF);
        minCost[src][0] = 0;

        // PriorityQueue: [cost, node, stops_used]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cost = curr[0], node = curr[1], stops = curr[2];

            if (node == dst) return cost;
            if (stops > k) continue;
            if (cost > minCost[node][stops]) continue; // stale entry

            List<int[]> neighbors = adj.getOrDefault(node, Collections.emptyList());
            for (int[] nb : neighbors) {
                int neighbor = nb[0], price = nb[1];
                int newCost = cost + price;
                int newStops = stops + 1;
                if (newCost < minCost[neighbor][newStops]) {
                    minCost[neighbor][newStops] = newCost;
                    pq.offer(new int[]{newCost, neighbor, newStops});
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== Cheapest Flights Within K Stops ===\n");

        // Example 1
        int[][] flights1 = {{0,1,100},{1,2,100},{2,0,100},{1,3,600},{2,3,200}};
        System.out.println("Brute:   " + bruteForce(4, flights1, 0, 3, 1));  // 700
        System.out.println("Optimal: " + optimal(4, flights1, 0, 3, 1));     // 700
        System.out.println("Best:    " + best(4, flights1, 0, 3, 1));        // 700

        System.out.println();
        // Example 2
        int[][] flights2 = {{0,1,100},{1,2,100},{0,2,500}};
        System.out.println("Brute:   " + bruteForce(3, flights2, 0, 2, 1));  // 200
        System.out.println("Optimal: " + optimal(3, flights2, 0, 2, 1));     // 200
        System.out.println("Best:    " + best(3, flights2, 0, 2, 1));        // 200

        System.out.println();
        System.out.println("k=0:     " + optimal(3, flights2, 0, 2, 0));     // 500

        System.out.println();
        int[][] flights3 = {{0,1,100}};
        System.out.println("No path: " + optimal(3, flights3, 0, 2, 1));     // -1
    }
}
