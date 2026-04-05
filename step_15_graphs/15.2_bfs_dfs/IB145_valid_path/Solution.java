/**
 * Problem: Valid Path
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a rectangle of dimensions X x Y and a set of circles (cx, cy, r),
 * determine if a valid path exists from (0,0) to (X,Y) that does NOT pass
 * through any circle.
 *
 * Key insight: A path is BLOCKED if circles form a "barrier" connecting the
 * LEFT or BOTTOM wall to the RIGHT or TOP wall in a way that cuts the rectangle.
 * Specifically, the path from (0,0) to (X,Y) is BLOCKED iff circles form a
 * connected chain from the left/bottom edge to the right/top edge (crossing
 * the anti-diagonal).
 *
 * Cleaner reformulation: path is impossible iff there's a chain of
 * overlapping/touching circles connecting:
 *   - The LEFT wall (cx - r <= 0) to the RIGHT wall (cx + r >= X), OR
 *   - The BOTTOM wall (cy - r <= 0) to the TOP wall (cy + r >= Y)
 * Even cleaner: path is blocked iff circles form a connected barrier from
 * (left-or-bottom) border to (right-or-top) border in the diagonal direction.
 *
 * Standard approach: BFS/DFS on circles. Two virtual nodes:
 *   - "top-left barrier": circles touching left wall OR top wall
 *   - "bottom-right barrier": circles touching right wall OR bottom wall
 * If these connect, path is blocked.
 *
 * Actually the correct formulation: Path from bottom-left (0,0) to top-right
 * (X,Y). It's blocked iff circles form a connected chain from the LEFT+BOTTOM
 * boundary to the RIGHT+TOP boundary... Let's use the standard IB version:
 * path exists = 1, doesn't exist = 0.
 * Circles block the path if they form a chain connecting LEFT wall to TOP wall
 * (blocking the diagonal). Two circles "connect" if distance between centers
 * <= sum of radii.
 *
 * Real-life use: Robot navigation, obstacle avoidance planning.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(N^2)  |  Space: O(N)
    // BFS/DFS: Check if any chain of overlapping circles connects
    // the LEFT/BOTTOM wall to the RIGHT/TOP wall, blocking the path.
    // Two circles overlap if dist(c1, c2) <= r1 + r2.
    // Path blocked if: chain from (touches_left OR touches_bottom)
    //                           to (touches_right OR touches_top)
    // ============================================================
    public static int bruteForce(int x, int y, int[][] circles) {
        // circles[i] = {cx, cy, r}
        int n = circles.length;
        if (n == 0) return 1;

        // Check if any single circle completely blocks
        // (covers origin to destination directly)
        // Build adjacency
        boolean[] visited = new boolean[n];

        // BFS from "source circles" (touching left wall x=0 or bottom y=0)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            int cx = circles[i][0], cy = circles[i][1], r = circles[i][2];
            // Touches left wall (x=0) or bottom wall (y=0)
            if (cx - r <= 0 || cy - r <= 0) {
                queue.offer(i);
                visited[i] = true;
            }
        }

        while (!queue.isEmpty()) {
            int i = queue.poll();
            int cx1 = circles[i][0], cy1 = circles[i][1], r1 = circles[i][2];
            // Check if this circle reaches right wall or top wall
            if (cx1 + r1 >= x || cy1 + r1 >= y) return 0; // Path blocked

            for (int j = 0; j < n; j++) {
                if (visited[j]) continue;
                int cx2 = circles[j][0], cy2 = circles[j][1], r2 = circles[j][2];
                long dx = cx1 - cx2, dy = cy1 - cy2;
                long dist2 = dx * dx + dy * dy;
                long radSum = (long)(r1 + r2);
                if (dist2 <= radSum * radSum) {
                    visited[j] = true;
                    queue.offer(j);
                }
            }
        }
        return 1;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(N^2)  |  Space: O(N)
    // Same logic as brute force with DFS. More elegant recursion.
    // The key insight: path (0,0)->(X,Y) is blocked iff obstacle
    // circles form a connected chain from left/bottom to right/top.
    // ============================================================
    public static int optimal(int x, int y, int[][] circles) {
        int n = circles.length;
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            int cx = circles[i][0], cy = circles[i][1], r = circles[i][2];
            if (!visited[i] && (cx - r <= 0 || cy - r <= 0)) {
                if (dfs(circles, visited, i, x, y)) return 0;
            }
        }
        return 1;
    }

    private static boolean dfs(int[][] circles, boolean[] visited, int i, int X, int Y) {
        visited[i] = true;
        int cx = circles[i][0], cy = circles[i][1], r = circles[i][2];
        if (cx + r >= X || cy + r >= Y) return true; // reaches right/top
        for (int j = 0; j < circles.length; j++) {
            if (visited[j]) continue;
            int cx2 = circles[j][0], cy2 = circles[j][1], r2 = circles[j][2];
            long dx = cx - cx2, dy = cy - cy2;
            if (dx * dx + dy * dy <= (long)(r + r2) * (r + r2)) {
                if (dfs(circles, visited, j, X, Y)) return true;
            }
        }
        return false;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(N^2)  |  Space: O(N^2) with adj list, O(N) with inline
    // Union-Find approach: union all overlapping circles.
    // Add virtual node 0 for "left/bottom" and virtual node n+1 for
    // "right/top". Check if they are connected.
    // ============================================================
    public static int best(int x, int y, int[][] circles) {
        int n = circles.length;
        int[] parent = new int[n + 2]; // 0 = left/bottom wall, n+1 = right/top wall
        for (int i = 0; i < parent.length; i++) parent[i] = i;

        int LEFT_BOT = 0, RIGHT_TOP = n + 1;

        for (int i = 0; i < n; i++) {
            int cx = circles[i][0], cy = circles[i][1], r = circles[i][2];
            if (cx - r <= 0 || cy - r <= 0) union(parent, LEFT_BOT, i + 1);
            if (cx + r >= x || cy + r >= y) union(parent, RIGHT_TOP, i + 1);
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cx1 = circles[i][0], cy1 = circles[i][1], r1 = circles[i][2];
                int cx2 = circles[j][0], cy2 = circles[j][1], r2 = circles[j][2];
                long dx = cx1 - cx2, dy = cy1 - cy2;
                if (dx * dx + dy * dy <= (long)(r1 + r2) * (r1 + r2)) {
                    union(parent, i + 1, j + 1);
                }
            }
        }

        return find(parent, LEFT_BOT) == find(parent, RIGHT_TOP) ? 0 : 1;
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private static void union(int[] parent, int a, int b) {
        parent[find(parent, a)] = find(parent, b);
    }

    public static void main(String[] args) {
        System.out.println("=== Valid Path ===\n");

        // Test 1: No circles -> path always exists
        System.out.println("Test 1 (no circles):");
        System.out.println("  Best: " + best(5, 5, new int[][]{})); // 1

        // Test 2: Circles block the path (chain from left to top)
        // Rectangle 5x5, circle at (2,2) with r=3 covers corner region
        int[][] c2 = {{2, 2, 3}};
        System.out.println("Test 2 (single big circle):");
        System.out.printf("  Brute: %d | Optimal: %d | Best: %d%n",
                bruteForce(5, 5, c2), optimal(5, 5, c2), best(5, 5, c2));

        // Test 3: Two circles forming a chain
        int[][] c3 = {{0, 3, 2}, {2, 1, 2}};
        System.out.println("Test 3 (chained circles):");
        System.out.printf("  Brute: %d | Optimal: %d | Best: %d%n",
                bruteForce(5, 5, c3), optimal(5, 5, c3), best(5, 5, c3));

        // Test 4: Circles don't block
        int[][] c4 = {{4, 4, 1}};
        System.out.println("Test 4 (circle in corner, doesn't block):");
        System.out.printf("  Brute: %d | Optimal: %d | Best: %d%n",
                bruteForce(10, 10, c4), optimal(10, 10, c4), best(10, 10, c4));
    }
}
