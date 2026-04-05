/**
 * Problem: Course Schedule II (LeetCode #210)
 * Difficulty: MEDIUM | XP: 25
 *
 * There are numCourses courses labeled 0 to numCourses-1. Given prerequisites
 * where prerequisites[i] = [a, b] means you must take b before a, return an
 * ordering of courses. Return empty array if impossible (cycle exists).
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (DFS-based topological sort)
    // Time: O(V + E)  |  Space: O(V + E)
    // ============================================================
    static class BruteForce {
        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < numCourses; i++) {
                adj.add(new ArrayList<>());
            }
            for (int[] pre : prerequisites) {
                adj.get(pre[1]).add(pre[0]);
            }

            int[] color = new int[numCourses]; // 0=white, 1=gray, 2=black
            List<Integer> order = new ArrayList<>();

            for (int i = 0; i < numCourses; i++) {
                if (color[i] == 0) {
                    if (!dfs(i, adj, color, order)) {
                        return new int[0];
                    }
                }
            }

            // Reverse to get topological order
            int[] result = new int[numCourses];
            for (int i = 0; i < numCourses; i++) {
                result[i] = order.get(numCourses - 1 - i);
            }
            return result;
        }

        private boolean dfs(int node, List<List<Integer>> adj, int[] color, List<Integer> order) {
            color[node] = 1; // gray
            for (int neighbor : adj.get(node)) {
                if (color[neighbor] == 1) return false; // cycle
                if (color[neighbor] == 0) {
                    if (!dfs(neighbor, adj, color, order)) return false;
                }
            }
            color[node] = 2; // black
            order.add(node);
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Kahn's Algorithm - BFS with indegree)
    // Time: O(V + E)  |  Space: O(V + E)
    // ============================================================
    static class Optimal {
        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List<List<Integer>> adj = new ArrayList<>();
            int[] indegree = new int[numCourses];

            for (int i = 0; i < numCourses; i++) {
                adj.add(new ArrayList<>());
            }
            for (int[] pre : prerequisites) {
                adj.get(pre[1]).add(pre[0]);
                indegree[pre[0]]++;
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < numCourses; i++) {
                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }

            int[] order = new int[numCourses];
            int idx = 0;

            while (!queue.isEmpty()) {
                int node = queue.poll();
                order[idx++] = node;

                for (int neighbor : adj.get(node)) {
                    indegree[neighbor]--;
                    if (indegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return idx == numCourses ? order : new int[0];
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Kahn's Algorithm - clean version)
    // Time: O(V + E)  |  Space: O(V + E)
    // ============================================================
    static class Best {
        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List<List<Integer>> adj = new ArrayList<>();
            int[] indegree = new int[numCourses];

            for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

            for (int[] p : prerequisites) {
                adj.get(p[1]).add(p[0]);
                indegree[p[0]]++;
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < numCourses; i++) {
                if (indegree[i] == 0) queue.offer(i);
            }

            int[] result = new int[numCourses];
            int count = 0;

            while (!queue.isEmpty()) {
                int course = queue.poll();
                result[count++] = course;
                for (int next : adj.get(course)) {
                    if (--indegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }

            return count == numCourses ? result : new int[0];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Course Schedule II ===\n");

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        // Test 1: 4 courses
        int[][] p1 = {{1,0},{2,0},{3,1},{3,2}};
        System.out.println("Test 1: numCourses=4");
        System.out.println("  DFS:    " + Arrays.toString(bf.findOrder(4, p1)));
        System.out.println("  Kahn's: " + Arrays.toString(opt.findOrder(4, p1)));
        System.out.println("  Best:   " + Arrays.toString(best.findOrder(4, p1)));

        // Test 2: 2 courses
        int[][] p2 = {{1,0}};
        System.out.println("\nTest 2: numCourses=2");
        System.out.println("  DFS:    " + Arrays.toString(bf.findOrder(2, p2)));
        System.out.println("  Kahn's: " + Arrays.toString(opt.findOrder(2, p2)));
        System.out.println("  Best:   " + Arrays.toString(best.findOrder(2, p2)));

        // Test 3: Cycle
        int[][] p3 = {{1,0},{0,1}};
        System.out.println("\nTest 3 (cycle): numCourses=2");
        System.out.println("  DFS:    " + Arrays.toString(bf.findOrder(2, p3)));
        System.out.println("  Kahn's: " + Arrays.toString(opt.findOrder(2, p3)));
        System.out.println("  Best:   " + Arrays.toString(best.findOrder(2, p3)));

        // Test 4: No prerequisites
        int[][] p4 = {};
        System.out.println("\nTest 4 (no prereqs): numCourses=3");
        System.out.println("  DFS:    " + Arrays.toString(bf.findOrder(3, p4)));
        System.out.println("  Kahn's: " + Arrays.toString(opt.findOrder(3, p4)));
        System.out.println("  Best:   " + Arrays.toString(best.findOrder(3, p4)));
    }
}
