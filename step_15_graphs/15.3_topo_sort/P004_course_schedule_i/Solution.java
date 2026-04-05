import java.util.*;

/**
 * Problem: Course Schedule I (LeetCode #207)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- DFS with visited + recStack
    // Time: O(V + E)  |  Space: O(V + E)
    // Detect cycle using recursion stack tracking.
    // ============================================================
    public static boolean bruteForce(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = buildGraph(numCourses, prerequisites);
        boolean[] visited = new boolean[numCourses];
        boolean[] recStack = new boolean[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (!visited[i]) {
                if (hasCycleDFS(i, adj, visited, recStack)) return false;
            }
        }
        return true;
    }

    private static boolean hasCycleDFS(int node, List<List<Integer>> adj,
                                        boolean[] visited, boolean[] recStack) {
        visited[node] = true;
        recStack[node] = true;

        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                if (hasCycleDFS(neighbor, adj, visited, recStack)) return true;
            } else if (recStack[neighbor]) {
                return true; // back edge = cycle
            }
        }

        recStack[node] = false; // backtrack
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Kahn's Algorithm (BFS Topological Sort)
    // Time: O(V + E)  |  Space: O(V + E)
    // Process nodes with in-degree 0 layer by layer.
    // If all processed, no cycle; otherwise cycle exists.
    // ============================================================
    public static boolean optimal(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = buildGraph(numCourses, prerequisites);
        int[] inDegree = new int[numCourses];

        // Calculate in-degrees
        for (int[] prereq : prerequisites) {
            inDegree[prereq[0]]++;
        }

        // Enqueue all nodes with in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int processedCount = 0;

        while (!queue.isEmpty()) {
            int course = queue.poll();
            processedCount++;

            for (int dependent : adj.get(course)) {
                inDegree[dependent]--;
                if (inDegree[dependent] == 0) {
                    queue.offer(dependent);
                }
            }
        }

        return processedCount == numCourses;
    }

    // ============================================================
    // APPROACH 3: BEST -- DFS with 3-State Coloring
    // Time: O(V + E)  |  Space: O(V + E)
    // WHITE(0)=unvisited, GRAY(1)=in-progress, BLACK(2)=done.
    // GRAY->GRAY edge = back edge = cycle.
    // ============================================================
    public static boolean best(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = buildGraph(numCourses, prerequisites);
        int[] color = new int[numCourses]; // 0=WHITE, 1=GRAY, 2=BLACK

        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0) { // WHITE
                if (hasCycleColored(i, adj, color)) return false;
            }
        }
        return true;
    }

    private static boolean hasCycleColored(int node, List<List<Integer>> adj, int[] color) {
        color[node] = 1; // GRAY -- exploring

        for (int neighbor : adj.get(node)) {
            if (color[neighbor] == 1) return true;  // back edge = cycle
            if (color[neighbor] == 0) {              // WHITE -- unvisited
                if (hasCycleColored(neighbor, adj, color)) return true;
            }
            // BLACK (2) -- already fully processed, safe to skip
        }

        color[node] = 2; // BLACK -- fully processed
        return false;
    }

    // ============================================================
    // HELPER: Build directed adjacency list
    // prereq [a, b] means b -> a (take b before a)
    // ============================================================
    private static List<List<Integer>> buildGraph(int n, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] prereq : prerequisites) {
            adj.get(prereq[1]).add(prereq[0]); // b -> a
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Course Schedule I ===\n");

        // Example 1: no cycle
        int[][] prereqs1 = {{1, 0}};
        System.out.println("Brute:   " + bruteForce(2, prereqs1));  // true
        System.out.println("Optimal: " + optimal(2, prereqs1));     // true
        System.out.println("Best:    " + best(2, prereqs1));        // true

        // Example 2: cycle
        int[][] prereqs2 = {{1, 0}, {0, 1}};
        System.out.println("\nCycle:");
        System.out.println("Brute:   " + bruteForce(2, prereqs2));  // false
        System.out.println("Optimal: " + optimal(2, prereqs2));     // false
        System.out.println("Best:    " + best(2, prereqs2));        // false

        // Example 3: diamond
        int[][] prereqs3 = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        System.out.println("\nDiamond:");
        System.out.println("Optimal: " + optimal(4, prereqs3));     // true

        // Edge case: no prerequisites
        System.out.println("\nNo prereqs: " + optimal(5, new int[][]{})); // true
    }
}
