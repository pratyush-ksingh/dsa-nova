/**
 * Problem: Two Teams
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given N people and their friendships, determine if they can be divided into
 * 2 teams such that no two people in the same team are friends.
 * This is equivalent to checking if the graph is BIPARTITE.
 *
 * Return 1 if possible (bipartite), 0 if not possible.
 *
 * Real-life use: Conflict scheduling, team formation, 2-coloring problems.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(V + E)  |  Space: O(V)
    // BFS coloring: assign color 0/1 alternately. If two adjacent
    // nodes get same color, graph is not bipartite.
    // ============================================================
    public static int bruteForce(int n, int[][] friendships) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] f : friendships) {
            adj.get(f[0]).add(f[1]);
            adj.get(f[1]).add(f[0]);
        }

        int[] color = new int[n + 1];
        Arrays.fill(color, -1);

        for (int start = 1; start <= n; start++) {
            if (color[start] != -1) continue;
            // BFS from this component
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start);
            color[start] = 0;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int neighbor : adj.get(node)) {
                    if (color[neighbor] == -1) {
                        color[neighbor] = 1 - color[node];
                        queue.offer(neighbor);
                    } else if (color[neighbor] == color[node]) {
                        return 0; // Not bipartite
                    }
                }
            }
        }
        return 1;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(V + E)  |  Space: O(V)
    // DFS-based bipartite check. Same complexity but uses call stack.
    // Often simpler to implement recursively.
    // ============================================================
    public static int optimal(int n, int[][] friendships) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] f : friendships) {
            adj.get(f[0]).add(f[1]);
            adj.get(f[1]).add(f[0]);
        }

        int[] color = new int[n + 1];
        Arrays.fill(color, -1);

        for (int i = 1; i <= n; i++) {
            if (color[i] == -1) {
                if (!dfsBipartite(adj, color, i, 0)) return 0;
            }
        }
        return 1;
    }

    private static boolean dfsBipartite(List<List<Integer>> adj, int[] color,
                                         int node, int c) {
        color[node] = c;
        for (int nb : adj.get(node)) {
            if (color[nb] == -1) {
                if (!dfsBipartite(adj, color, nb, 1 - c)) return false;
            } else if (color[nb] == color[node]) {
                return false;
            }
        }
        return true;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(V + E)  |  Space: O(V)
    // Union-Find approach: for each node u with neighbors v1, v2...
    // all neighbors must be in the same "opposing" set as u.
    // We use bipartite property: union(v, first_neighbor_of_u).
    // Simpler: BFS is canonical. This approach uses DSU to verify.
    // Actually, we use the standard BFS with adjacency matrix input
    // (InterviewBit format often gives adjacency matrix).
    // ============================================================
    public static int best(int n, int[][] adj) {
        // adj[i][j] = 1 means i and j are friends (1-indexed)
        int[] color = new int[n + 1];
        Arrays.fill(color, -1);

        for (int start = 1; start <= n; start++) {
            if (color[start] != -1) continue;
            Deque<Integer> queue = new ArrayDeque<>();
            queue.offer(start);
            color[start] = 0;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int nb = 1; nb <= n; nb++) {
                    if (adj[node - 1][nb - 1] == 1) {
                        if (color[nb] == -1) {
                            color[nb] = 1 - color[node];
                            queue.offer(nb);
                        } else if (color[nb] == color[node]) {
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        System.out.println("=== Two Teams (Bipartite Check) ===\n");

        // Test 1: Simple bipartite (1-2, 2-3, 3-4 - even cycle)
        int[][] f1 = {{1, 2}, {2, 3}, {3, 4}, {4, 1}};
        System.out.println("Test 1 (even cycle, bipartite):");
        System.out.println("  Brute:   " + bruteForce(4, f1)); // 1
        System.out.println("  Optimal: " + optimal(4, f1));    // 1

        // Test 2: Triangle (odd cycle, not bipartite)
        int[][] f2 = {{1, 2}, {2, 3}, {3, 1}};
        System.out.println("\nTest 2 (triangle, NOT bipartite):");
        System.out.println("  Brute:   " + bruteForce(3, f2)); // 0
        System.out.println("  Optimal: " + optimal(3, f2));    // 0

        // Test 3: Disconnected bipartite graph
        int[][] f3 = {{1, 2}, {3, 4}};
        System.out.println("\nTest 3 (disconnected, bipartite):");
        System.out.println("  Brute:   " + bruteForce(4, f3)); // 1
        System.out.println("  Optimal: " + optimal(4, f3));    // 1

        // Test 4 for best() with adjacency matrix
        int[][] adjMatrix = {
            {0, 1, 0, 1},
            {1, 0, 1, 0},
            {0, 1, 0, 1},
            {1, 0, 1, 0}
        };
        System.out.println("\nTest 4 (adj matrix, bipartite):");
        System.out.println("  Best: " + best(4, adjMatrix)); // 1
    }
}
