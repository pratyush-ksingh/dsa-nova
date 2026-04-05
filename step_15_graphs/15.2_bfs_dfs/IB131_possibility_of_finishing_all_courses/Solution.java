import java.util.*;

/**
 * Problem: Possibility of Finishing All Courses
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given N courses and prerequisites as edges [a, b] meaning "must take b before a",
 * determine if all courses can be finished (i.e., no cycle in directed graph).
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// DFS-based cycle detection using 3-color marking (WHITE=0, GRAY=1, BLACK=2)
// Time: O(V + E)  |  Space: O(V + E)
// ============================================================
class BruteForce {
    static int[] color;
    static List<List<Integer>> adj;

    static boolean hasCycle(int node) {
        color[node] = 1; // GRAY = being processed
        for (int neighbor : adj.get(node)) {
            if (color[neighbor] == 1) return true; // back edge => cycle
            if (color[neighbor] == 0 && hasCycle(neighbor)) return true;
        }
        color[node] = 2; // BLACK = done
        return false;
    }

    static boolean canFinish(int numCourses, int[][] prerequisites) {
        adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) adj.get(pre[1]).add(pre[0]);

        color = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0 && hasCycle(i)) return false;
        }
        return true;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Kahn's Algorithm (BFS Topological Sort) — count processed nodes
// If all nodes processed, no cycle exists.
// Time: O(V + E)  |  Space: O(V + E)
// ============================================================
class Optimal {
    static boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            processed++;
            for (int neighbor : adj.get(node)) {
                if (--inDegree[neighbor] == 0) queue.offer(neighbor);
            }
        }
        return processed == numCourses;
    }
}

// ============================================================
// APPROACH 3: BEST
// Iterative DFS using explicit stack — avoids recursion limit issues
// Time: O(V + E)  |  Space: O(V + E)
// ============================================================
class Best {
    static boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) adj.get(pre[1]).add(pre[0]);

        // 0=unvisited, 1=in-stack, 2=done
        int[] state = new int[numCourses];

        for (int start = 0; start < numCourses; start++) {
            if (state[start] != 0) continue;
            Deque<int[]> stack = new ArrayDeque<>(); // [node, neighborIndex]
            stack.push(new int[]{start, 0});
            state[start] = 1;

            while (!stack.isEmpty()) {
                int[] top = stack.peek();
                int node = top[0];
                List<Integer> neighbors = adj.get(node);

                if (top[1] < neighbors.size()) {
                    int next = neighbors.get(top[1]++);
                    if (state[next] == 1) return false; // cycle
                    if (state[next] == 0) {
                        state[next] = 1;
                        stack.push(new int[]{next, 0});
                    }
                } else {
                    state[node] = 2;
                    stack.pop();
                }
            }
        }
        return true;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Possibility of Finishing All Courses ===");

        // 2 courses, no cycle: can finish
        System.out.println("BruteForce [2, [[1,0]]]:     " + BruteForce.canFinish(2, new int[][]{{1, 0}}));   // true
        System.out.println("Optimal    [2, [[1,0]]]:     " + Optimal.canFinish(2, new int[][]{{1, 0}}));      // true
        System.out.println("Best       [2, [[1,0]]]:     " + Best.canFinish(2, new int[][]{{1, 0}}));         // true

        // 2 courses, cycle: cannot finish
        System.out.println("BruteForce cycle [2,0],[0,1]: " + BruteForce.canFinish(2, new int[][]{{1, 0}, {0, 1}})); // false
        System.out.println("Optimal    cycle:             " + Optimal.canFinish(2, new int[][]{{1, 0}, {0, 1}}));    // false

        // 4 courses with complex deps
        System.out.println("4 courses no cycle: " + Optimal.canFinish(4, new int[][]{{1,0},{2,0},{3,1},{3,2}})); // true
    }
}
