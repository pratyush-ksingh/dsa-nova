/**
 * Problem: Triangle Minimum Path Sum (LeetCode #120)
 * Difficulty: MEDIUM | XP: 25
 *
 * Min path sum from top to bottom of triangle.
 * Bottom-up approach makes space optimization elegant.
 * All 4 DP approaches.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion (Top-Down from peak)
// Time: O(2^n) | Space: O(n) stack
// ============================================================
class Recursive {
    public int minimumTotal(List<List<Integer>> triangle) {
        return solve(triangle, 0, 0);
    }

    private int solve(List<List<Integer>> tri, int i, int j) {
        if (i == tri.size() - 1) return tri.get(i).get(j);

        int goDown = solve(tri, i + 1, j);
        int goRight = solve(tri, i + 1, j + 1);

        return tri.get(i).get(j) + Math.min(goDown, goRight);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n^2) | Space: O(n^2)
// ============================================================
class Memoization {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] dp = new int[n][n];
        for (int[] row : dp) Arrays.fill(row, Integer.MIN_VALUE);
        return solve(triangle, 0, 0, dp);
    }

    private int solve(List<List<Integer>> tri, int i, int j, int[][] dp) {
        if (i == tri.size() - 1) return tri.get(i).get(j);
        if (dp[i][j] != Integer.MIN_VALUE) return dp[i][j];

        int goDown = solve(tri, i + 1, j, dp);
        int goRight = solve(tri, i + 1, j + 1, dp);

        dp[i][j] = tri.get(i).get(j) + Math.min(goDown, goRight);
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n^2) | Space: O(n^2)
// ============================================================
class Tabulation {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] dp = new int[n][n];

        // Initialize bottom row
        for (int j = 0; j < n; j++) {
            dp[n - 1][j] = triangle.get(n - 1).get(j);
        }

        // Fill from bottom to top
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                dp[i][j] = triangle.get(i).get(j) +
                           Math.min(dp[i + 1][j], dp[i + 1][j + 1]);
            }
        }

        return dp[0][0];
    }
}

// ============================================================
// Approach 4: Space Optimized (1D array, bottom-up)
// Time: O(n^2) | Space: O(n)
// ============================================================
class SpaceOptimized {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();

        // Start with the bottom row
        int[] dp = new int[n];
        for (int j = 0; j < n; j++) {
            dp[j] = triangle.get(n - 1).get(j);
        }

        // Fold upward, row by row
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                // dp[j] = from below (same index), dp[j+1] = from below-right
                // Process left-to-right: dp[j+1] hasn't been overwritten yet
                dp[j] = triangle.get(i).get(j) + Math.min(dp[j], dp[j + 1]);
            }
        }

        return dp[0];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Triangle Minimum Path Sum (LeetCode #120) ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        // Test case 1: Classic example
        List<List<Integer>> tri1 = Arrays.asList(
            Arrays.asList(2),
            Arrays.asList(3, 4),
            Arrays.asList(6, 5, 7),
            Arrays.asList(4, 1, 8, 3)
        );

        // Test case 2: Single element
        List<List<Integer>> tri2 = Arrays.asList(
            Arrays.asList(-10)
        );

        // Test case 3: Two rows
        List<List<Integer>> tri3 = Arrays.asList(
            Arrays.asList(1),
            Arrays.asList(2, 3)
        );

        // Test case 4: Negative numbers
        List<List<Integer>> tri4 = Arrays.asList(
            Arrays.asList(-1),
            Arrays.asList(2, 3),
            Arrays.asList(1, -1, -3)
        );

        @SuppressWarnings("unchecked")
        List<List<Integer>>[] tests = new List[]{tri1, tri2, tri3, tri4};
        int[] expected = {11, -10, 3, -1};

        System.out.printf("%-12s %-8s %-8s %-8s %-8s %-8s %-6s%n",
                "Triangle", "Rec", "Memo", "Tab", "Space", "Expect", "Pass");
        System.out.println("-".repeat(65));

        for (int t = 0; t < tests.length; t++) {
            int r = rec.minimumTotal(tests[t]);
            int m = memo.minimumTotal(tests[t]);
            int tb = tab.minimumTotal(tests[t]);
            int s = space.minimumTotal(tests[t]);

            boolean pass = (r == expected[t]) && (m == expected[t]) &&
                           (tb == expected[t]) && (s == expected[t]);

            System.out.printf("%-12s %-8d %-8d %-8d %-8d %-8d %-6s%n",
                    tests[t].size() + " rows", r, m, tb, s, expected[t],
                    pass ? "PASS" : "FAIL");
        }

        // Visualize the triangle and dp process
        System.out.println("\n--- Bottom-Up DP Visualization ---");
        System.out.println("Triangle:");
        System.out.println("    2");
        System.out.println("   3 4");
        System.out.println("  6 5 7");
        System.out.println(" 4 1 8 3");

        System.out.println("\nDP (bottom to top):");
        System.out.println(" dp = [4, 1, 8, 3]   <- bottom row");
        System.out.println(" i=2: dp[0]=6+min(4,1)=7  dp[1]=5+min(1,8)=6  dp[2]=7+min(8,3)=10");
        System.out.println("      dp = [7, 6, 10, 3]");
        System.out.println(" i=1: dp[0]=3+min(7,6)=9  dp[1]=4+min(6,10)=10");
        System.out.println("      dp = [9, 10, 10, 3]");
        System.out.println(" i=0: dp[0]=2+min(9,10)=11");
        System.out.println("      dp = [11, 10, 10, 3]");
        System.out.println("\n Answer: dp[0] = 11");

        // Path reconstruction
        System.out.println("\n--- Path Reconstruction ---");
        int n = tri1.size();
        int[][] dpFull = new int[n][n];
        for (int j = 0; j < n; j++) dpFull[n - 1][j] = tri1.get(n - 1).get(j);
        for (int i = n - 2; i >= 0; i--)
            for (int j = 0; j <= i; j++)
                dpFull[i][j] = tri1.get(i).get(j) +
                        Math.min(dpFull[i + 1][j], dpFull[i + 1][j + 1]);

        StringBuilder path = new StringBuilder();
        int j = 0;
        for (int i = 0; i < n; i++) {
            path.append(tri1.get(i).get(j));
            if (i < n - 1) {
                path.append(" -> ");
                if (dpFull[i + 1][j + 1] < dpFull[i + 1][j]) j++;
            }
        }
        System.out.println("Best path: " + path + " = " + dpFull[0][0]);
    }
}
