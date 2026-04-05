import java.util.*;

/**
 * Problem: Kruskal's Algorithm
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // UNION-FIND (Disjoint Set Union) helper
    // ============================================================
    static int[] parent, rank;

    static void initDSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]); // path compression
        return parent[x];
    }

    static boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false; // same component -- would create cycle
        if (rank[px] < rank[py]) { int tmp = px; px = py; py = tmp; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
        return true;
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check all spanning tree combinations
    // Time: O(C(E,V-1) * V) -- exponential  |  Space: O(V + E)
    // Enumerate all subsets of V-1 edges, check connectivity.
    // Only feasible for tiny graphs.
    // ============================================================
    public static int bruteForce(int V, int[][] edges) {
        int E = edges.length;
        int minWeight = Integer.MAX_VALUE;

        // Generate all combinations of V-1 edges from E edges
        List<List<Integer>> combos = new ArrayList<>();
        generateCombos(combos, new ArrayList<>(), 0, E, V - 1);

        for (List<Integer> combo : combos) {
            // Check if these V-1 edges form a spanning tree
            initDSU(V);
            int weight = 0;
            boolean valid = true;
            for (int idx : combo) {
                int u = edges[idx][0], v = edges[idx][1], w = edges[idx][2];
                if (!union(u, v)) { valid = false; break; } // cycle detected
                weight += w;
            }
            // Check all vertices connected
            if (valid) {
                Set<Integer> roots = new HashSet<>();
                for (int i = 0; i < V; i++) roots.add(find(i));
                if (roots.size() == 1) minWeight = Math.min(minWeight, weight);
            }
        }

        return minWeight == Integer.MAX_VALUE ? -1 : minWeight;
    }

    private static void generateCombos(List<List<Integer>> result, List<Integer> current,
                                         int start, int n, int k) {
        if (current.size() == k) { result.add(new ArrayList<>(current)); return; }
        for (int i = start; i < n; i++) {
            current.add(i);
            generateCombos(result, current, i + 1, n, k);
            current.remove(current.size() - 1);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Kruskal's with Union-Find
    // Time: O(E log E)  |  Space: O(V + E)
    // Sort edges by weight, greedily add lightest non-cycle-forming edge.
    // ============================================================
    public static int optimal(int V, int[][] edges) {
        // Sort edges by weight
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);

        initDSU(V);
        int mstWeight = 0;
        int edgesAdded = 0;

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (union(u, v)) {
                mstWeight += w;
                edgesAdded++;
                if (edgesAdded == V - 1) break; // MST complete
            }
        }

        return edgesAdded == V - 1 ? mstWeight : -1; // -1 if disconnected
    }

    // ============================================================
    // APPROACH 3: BEST -- Kruskal's with Optimized DSU + Edge List
    // Time: O(E log E + E * alpha(V))  |  Space: O(V + E)
    // Same algorithm with full optimizations and MST edge tracking.
    // ============================================================
    public static int best(int V, int[][] edges) {
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);

        initDSU(V);
        int mstWeight = 0;
        int edgesAdded = 0;
        List<int[]> mstEdges = new ArrayList<>();

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (union(u, v)) {
                mstWeight += w;
                mstEdges.add(edge);
                edgesAdded++;
                if (edgesAdded == V - 1) break;
            }
        }

        if (edgesAdded != V - 1) return -1;

        // Print MST edges for verification
        System.out.print("  MST edges: ");
        for (int[] e : mstEdges) {
            System.out.print("(" + e[0] + "-" + e[1] + ":" + e[2] + ") ");
        }
        System.out.println();

        return mstWeight;
    }

    public static void main(String[] args) {
        System.out.println("=== Kruskal's Algorithm ===\n");

        // Example 1
        int[][] edges1 = {{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}};
        System.out.println("Brute:   " + bruteForce(4, edges1));

        int[][] edges1b = {{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}};
        System.out.println("Optimal: " + optimal(4, edges1b));

        int[][] edges1c = {{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}};
        System.out.print("Best:    " + best(4, edges1c));
        // Expected: 19

        // Example 2: triangle
        System.out.println();
        int[][] edges2 = {{0,1,1},{1,2,2},{0,2,3}};
        System.out.println("Triangle: " + optimal(3, edges2));
        // Expected: 3
    }
}
