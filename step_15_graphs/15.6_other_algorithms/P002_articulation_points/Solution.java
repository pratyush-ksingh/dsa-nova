/**
 * Problem: Articulation Points
 * Difficulty: HARD | XP: 50
 *
 * An articulation point (cut vertex) is a vertex in an undirected graph whose
 * removal increases the number of connected components. Tarjan's algorithm
 * finds all APs in a single DFS pass using discovery and low-link values.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(V * (V + E))  |  Space: O(V + E)
    // Remove each vertex, check connectivity with BFS/DFS.
    // ============================================================
    static class BruteForce {

        private static boolean isConnected(int n, List<List<Integer>> adj, int skip) {
            boolean[] visited = new boolean[n];
            int start = -1;
            for (int i = 0; i < n; i++) {
                if (i != skip) { start = i; break; }
            }
            if (start == -1) return true;

            Stack<Integer> stack = new Stack<>();
            stack.push(start);
            visited[start] = true;
            int count = 1;

            while (!stack.isEmpty()) {
                int node = stack.pop();
                for (int nb : adj.get(node)) {
                    if (nb != skip && !visited[nb]) {
                        visited[nb] = true;
                        count++;
                        stack.push(nb);
                    }
                }
            }
            int total = 0;
            for (int i = 0; i < n; i++) if (i != skip) total++;
            return count == total;
        }

        public static List<Integer> articulationPoints(int n, List<List<Integer>> adj) {
            List<Integer> result = new ArrayList<>();
            for (int v = 0; v < n; v++) {
                if (!isConnected(n, adj, v)) {
                    result.add(v);
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Tarjan's Algorithm (Recursive)
    // Time: O(V + E)  |  Space: O(V)
    // disc[u]: discovery time. low[u]: lowest disc reachable from subtree of u.
    // AP conditions: root with >=2 children, OR non-root with low[child]>=disc[u].
    // ============================================================
    static class Optimal {

        private static int[] disc, low;
        private static boolean[] visited, isAP;
        private static int timer;

        private static void dfs(int u, int parent, List<List<Integer>> adj) {
            visited[u] = true;
            disc[u] = low[u] = timer++;
            int children = 0;

            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    children++;
                    dfs(v, u, adj);
                    low[u] = Math.min(low[u], low[v]);
                    // Non-root AP condition
                    if (parent != -1 && low[v] >= disc[u]) isAP[u] = true;
                    // Root AP condition
                    if (parent == -1 && children > 1) isAP[u] = true;
                } else if (v != parent) {
                    // Back edge
                    low[u] = Math.min(low[u], disc[v]);
                }
            }
        }

        public static List<Integer> articulationPoints(int n, List<List<Integer>> adj) {
            disc = new int[n];
            low = new int[n];
            visited = new boolean[n];
            isAP = new boolean[n];
            timer = 0;

            for (int i = 0; i < n; i++) {
                if (!visited[i]) dfs(i, -1, adj);
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (isAP[i]) result.add(i);
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Tarjan's Algorithm (Iterative)
    // Time: O(V + E)  |  Space: O(V)
    // Same algorithm, iterative DFS to avoid stack overflow on large graphs.
    // Stack stores [node, parent, neighborIndex, childrenCount].
    // ============================================================
    static class Best {

        public static List<Integer> articulationPoints(int n, List<List<Integer>> adj) {
            int[] disc = new int[n];
            int[] low = new int[n];
            boolean[] isAP = new boolean[n];
            Arrays.fill(disc, -1);
            int[] timer = {0};

            for (int start = 0; start < n; start++) {
                if (disc[start] != -1) continue;

                // Stack: [node, parent, neighborIndex, childrenCount]
                Deque<int[]> stack = new ArrayDeque<>();
                stack.push(new int[]{start, -1, 0, 0});
                disc[start] = low[start] = timer[0]++;

                while (!stack.isEmpty()) {
                    int[] frame = stack.peek();
                    int u = frame[0], parent = frame[1], idx = frame[2];
                    List<Integer> neighbors = adj.get(u);

                    if (idx < neighbors.size()) {
                        frame[2]++;  // advance neighbor index
                        int v = neighbors.get(idx);

                        if (disc[v] == -1) {
                            // Tree edge
                            frame[3]++;  // increment child count
                            disc[v] = low[v] = timer[0]++;
                            stack.push(new int[]{v, u, 0, 0});
                        } else if (v != parent) {
                            // Back edge
                            low[u] = Math.min(low[u], disc[v]);
                        }
                    } else {
                        // Done with u — pop and update parent
                        stack.pop();
                        if (!stack.isEmpty()) {
                            int[] pFrame = stack.peek();
                            int pu = pFrame[0], pp = pFrame[1];
                            low[pu] = Math.min(low[pu], low[u]);
                            if (pp == -1 && pFrame[3] > 1) isAP[pu] = true;
                            if (pp != -1 && low[u] >= disc[pu]) isAP[pu] = true;
                        }
                    }
                }
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (isAP[i]) result.add(i);
            }
            return result;
        }
    }

    // Helper to build adjacency list
    private static List<List<Integer>> buildAdj(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Articulation Points ===");

        // Graph 1: chain 0-1-2-3-4, APs = 1,2,3
        int n1 = 5;
        int[][] edges1 = {{0,1},{1,2},{2,3},{3,4}};
        List<List<Integer>> adj1 = buildAdj(n1, edges1);
        System.out.println("Chain 0-1-2-3-4:");
        System.out.println("  Brute:   " + BruteForce.articulationPoints(n1, adj1));
        System.out.println("  Optimal: " + Optimal.articulationPoints(n1, adj1));
        System.out.println("  Best:    " + Best.articulationPoints(n1, adj1));

        // Graph 2: triangle + chain, APs = 1,3
        int n2 = 5;
        int[][] edges2 = {{0,1},{0,2},{1,2},{1,3},{3,4}};
        List<List<Integer>> adj2 = buildAdj(n2, edges2);
        System.out.println("Triangle + chain:");
        System.out.println("  Brute:   " + BruteForce.articulationPoints(n2, adj2));
        System.out.println("  Optimal: " + Optimal.articulationPoints(n2, adj2));
        System.out.println("  Best:    " + Best.articulationPoints(n2, adj2));
    }
}
