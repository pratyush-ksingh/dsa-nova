/**
 * Problem: Find Eventual Safe States (LeetCode #802)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a directed graph where graph[i] lists nodes reachable from i,
 * find all safe nodes (every path leads to a terminal node).
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (DFS from each node with memoization)
    // Time: O(V * (V + E))  |  Space: O(V)
    // ============================================================
    static class BruteForce {
        // 0=unvisited, 1=in-progress, 2=safe, 3=unsafe
        int[] state;

        public List<Integer> eventualSafeNodes(int[][] graph) {
            int n = graph.length;
            state = new int[n];
            List<Integer> result = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (isSafe(i, graph)) {
                    result.add(i);
                }
            }
            return result;
        }

        private boolean isSafe(int node, int[][] graph) {
            if (state[node] == 2) return true;
            if (state[node] == 1 || state[node] == 3) return false;

            state[node] = 1; // in progress
            for (int neighbor : graph[node]) {
                if (!isSafe(neighbor, graph)) {
                    state[node] = 3;
                    return false;
                }
            }
            state[node] = 2; // safe
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (DFS with 3-state coloring)
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    static class Optimal {
        public List<Integer> eventualSafeNodes(int[][] graph) {
            int n = graph.length;
            int[] color = new int[n]; // 0=white, 1=gray, 2=black
            List<Integer> result = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (dfs(i, graph, color)) {
                    result.add(i);
                }
            }
            return result;
        }

        private boolean dfs(int node, int[][] graph, int[] color) {
            if (color[node] != 0) return color[node] == 2;
            color[node] = 1; // gray

            for (int neighbor : graph[node]) {
                if (color[neighbor] == 1) return false;
                if (color[neighbor] == 0 && !dfs(neighbor, graph, color)) {
                    return false;
                }
            }

            color[node] = 2; // black = safe
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Reverse graph + Kahn's algorithm)
    // Time: O(V + E)  |  Space: O(V + E)
    // ============================================================
    static class Best {
        public List<Integer> eventualSafeNodes(int[][] graph) {
            int n = graph.length;

            // Build reverse graph and compute outdegree
            List<List<Integer>> reverseAdj = new ArrayList<>();
            int[] outdegree = new int[n];

            for (int i = 0; i < n; i++) {
                reverseAdj.add(new ArrayList<>());
            }

            for (int u = 0; u < n; u++) {
                for (int v : graph[u]) {
                    reverseAdj.get(v).add(u);
                }
                outdegree[u] = graph[u].length;
            }

            // Start with terminal nodes (outdegree 0)
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                if (outdegree[i] == 0) {
                    queue.offer(i);
                }
            }

            boolean[] safe = new boolean[n];
            while (!queue.isEmpty()) {
                int node = queue.poll();
                safe[node] = true;
                for (int neighbor : reverseAdj.get(node)) {
                    outdegree[neighbor]--;
                    if (outdegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (safe[i]) result.add(i);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Find Eventual Safe States ===\n");

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        // Test 1
        int[][] g1 = {{1,2},{2,3},{5},{0},{5},{},{}};
        System.out.println("Test 1: Expected [2, 4, 5, 6]");
        System.out.println("  Brute: " + bf.eventualSafeNodes(g1));
        System.out.println("  DFS:   " + opt.eventualSafeNodes(g1));
        System.out.println("  Topo:  " + best.eventualSafeNodes(g1));

        // Test 2: All terminal
        int[][] g2 = {{},{},{}};
        System.out.println("\nTest 2: Expected [0, 1, 2]");
        System.out.println("  Brute: " + new BruteForce().eventualSafeNodes(g2));
        System.out.println("  DFS:   " + new Optimal().eventualSafeNodes(g2));
        System.out.println("  Topo:  " + new Best().eventualSafeNodes(g2));

        // Test 3: Full cycle
        int[][] g3 = {{1},{2},{0}};
        System.out.println("\nTest 3 (cycle): Expected []");
        System.out.println("  Brute: " + new BruteForce().eventualSafeNodes(g3));
        System.out.println("  DFS:   " + new Optimal().eventualSafeNodes(g3));
        System.out.println("  Topo:  " + new Best().eventualSafeNodes(g3));
    }
}
