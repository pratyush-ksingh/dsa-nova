import java.util.*;

/**
 * Problem: Commutable Islands
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given A islands and B bridges (weighted edges), find minimum cost to connect all islands.
 * Classic Minimum Spanning Tree problem.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// Kruskal's MST with Union-Find (sort edges, greedily pick cheapest non-cycle edge)
// Time: O(E log E)  |  Space: O(V)
// ============================================================
class BruteForce {
    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;
        if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
        return true;
    }

    static int solve(int A, int[][] B) {
        // B[i] = {u, v, weight}
        Arrays.sort(B, (a, b) -> a[2] - b[2]);
        parent = new int[A + 1];
        rank = new int[A + 1];
        for (int i = 0; i <= A; i++) parent[i] = i;

        int totalCost = 0, edgesUsed = 0;
        for (int[] edge : B) {
            if (union(edge[0], edge[1])) {
                totalCost += edge[2];
                edgesUsed++;
                if (edgesUsed == A - 1) break;
            }
        }
        return totalCost;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Prim's MST using min-heap (adjacency list representation)
// Time: O(E log V)  |  Space: O(V + E)
// ============================================================
class Optimal {
    static int solve(int A, int[][] B) {
        // Build adjacency list
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= A; i++) adj.add(new ArrayList<>());
        for (int[] edge : B) {
            adj.get(edge[0]).add(new int[]{edge[1], edge[2]});
            adj.get(edge[1]).add(new int[]{edge[0], edge[2]});
        }

        // Min-heap: {cost, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        boolean[] visited = new boolean[A + 1];
        pq.offer(new int[]{0, 1}); // start from island 1
        int totalCost = 0;

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cost = curr[0], node = curr[1];
            if (visited[node]) continue;
            visited[node] = true;
            totalCost += cost;
            for (int[] neighbor : adj.get(node)) {
                if (!visited[neighbor[0]]) {
                    pq.offer(new int[]{neighbor[1], neighbor[0]});
                }
            }
        }
        return totalCost;
    }
}

// ============================================================
// APPROACH 3: BEST
// Kruskal's with path-compression and union by rank — most cache-friendly
// Same complexity but cleaner implementation
// Time: O(E log E)  |  Space: O(V)
// ============================================================
class Best {
    static int[] par;

    static int find(int x) {
        while (par[x] != x) {
            par[x] = par[par[x]]; // path halving
            x = par[x];
        }
        return x;
    }

    static int solve(int A, int[][] B) {
        int[][] edges = B.clone();
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
        par = new int[A + 1];
        for (int i = 0; i <= A; i++) par[i] = i;

        int cost = 0;
        for (int[] e : edges) {
            int pu = find(e[0]), pv = find(e[1]);
            if (pu != pv) {
                par[pu] = pv;
                cost += e[2];
            }
        }
        return cost;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Commutable Islands ===");

        // 4 islands, bridges: (1,2,1),(1,3,4),(1,4,3),(2,4,2),(3,4,5)
        // MST = 1+2+3 = 6
        int A = 4;
        int[][] B = {{1, 2, 1}, {1, 3, 4}, {1, 4, 3}, {2, 4, 2}, {3, 4, 5}};

        System.out.println("BruteForce (Kruskal): " + BruteForce.solve(A, B));  // 6
        System.out.println("Optimal    (Prim's):  " + Optimal.solve(A, B));     // 6
        System.out.println("Best       (Kruskal): " + Best.solve(A, B));        // 6

        // 3 islands, bridges: (1,2,6),(1,3,3),(2,3,2)
        // MST = 2+3 = 5
        int A2 = 3;
        int[][] B2 = {{1, 2, 6}, {1, 3, 3}, {2, 3, 2}};
        System.out.println("Test2 BruteForce: " + BruteForce.solve(A2, B2));  // 5
        System.out.println("Test2 Optimal:    " + Optimal.solve(A2, B2));     // 5
    }
}
