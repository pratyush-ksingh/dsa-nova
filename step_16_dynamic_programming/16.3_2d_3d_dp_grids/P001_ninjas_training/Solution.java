/**
 * Problem: Ninja's Training (2D DP)
 * Difficulty: MEDIUM | XP: 25
 *
 * N days, 3 activities per day. Can't repeat same activity on consecutive days.
 * Maximize total merit points.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^N) | Space: O(N) recursion stack
// ============================================================
class RecursiveNinja {
    public int ninjaTraining(int[][] points) {
        int n = points.length;
        return solve(n - 1, 3, points);
    }

    // last = 3 means "no restriction" (first call)
    private int solve(int day, int last, int[][] points) {
        if (day == 0) {
            int maxVal = 0;
            for (int j = 0; j < 3; j++) {
                if (j != last) {
                    maxVal = Math.max(maxVal, points[0][j]);
                }
            }
            return maxVal;
        }

        int maxVal = 0;
        for (int j = 0; j < 3; j++) {
            if (j != last) {
                int val = points[day][j] + solve(day - 1, j, points);
                maxVal = Math.max(maxVal, val);
            }
        }
        return maxVal;
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(N * 4 * 3) = O(N) | Space: O(N * 4) = O(N)
// ============================================================
class MemoNinja {
    public int ninjaTraining(int[][] points) {
        int n = points.length;
        int[][] dp = new int[n][4];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(n - 1, 3, points, dp);
    }

    private int solve(int day, int last, int[][] points, int[][] dp) {
        if (day == 0) {
            int maxVal = 0;
            for (int j = 0; j < 3; j++) {
                if (j != last) {
                    maxVal = Math.max(maxVal, points[0][j]);
                }
            }
            return dp[0][last] = maxVal;
        }

        if (dp[day][last] != -1) return dp[day][last];

        int maxVal = 0;
        for (int j = 0; j < 3; j++) {
            if (j != last) {
                int val = points[day][j] + solve(day - 1, j, points, dp);
                maxVal = Math.max(maxVal, val);
            }
        }
        return dp[day][last] = maxVal;
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(N * 4 * 3) = O(N) | Space: O(N * 4) = O(N)
// ============================================================
class TabNinja {
    public int ninjaTraining(int[][] points) {
        int n = points.length;
        int[][] dp = new int[n][4];

        // Base case: day 0
        dp[0][0] = Math.max(points[0][1], points[0][2]);  // last=0, can't do 0
        dp[0][1] = Math.max(points[0][0], points[0][2]);  // last=1, can't do 1
        dp[0][2] = Math.max(points[0][0], points[0][1]);  // last=2, can't do 2
        dp[0][3] = Math.max(points[0][0], Math.max(points[0][1], points[0][2]));

        // Fill day 1 to n-1
        for (int day = 1; day < n; day++) {
            for (int last = 0; last < 4; last++) {
                dp[day][last] = 0;
                for (int j = 0; j < 3; j++) {
                    if (j != last) {
                        dp[day][last] = Math.max(
                            dp[day][last],
                            points[day][j] + dp[day - 1][j]
                        );
                    }
                }
            }
        }

        return dp[n - 1][3];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(N * 4 * 3) = O(N) | Space: O(1)
// ============================================================
class SpaceNinja {
    public int ninjaTraining(int[][] points) {
        int n = points.length;
        int[] prev = new int[4];

        // Base case: day 0
        prev[0] = Math.max(points[0][1], points[0][2]);
        prev[1] = Math.max(points[0][0], points[0][2]);
        prev[2] = Math.max(points[0][0], points[0][1]);
        prev[3] = Math.max(points[0][0], Math.max(points[0][1], points[0][2]));

        for (int day = 1; day < n; day++) {
            int[] curr = new int[4];
            for (int last = 0; last < 4; last++) {
                curr[last] = 0;
                for (int j = 0; j < 3; j++) {
                    if (j != last) {
                        curr[last] = Math.max(curr[last], points[day][j] + prev[j]);
                    }
                }
            }
            prev = curr;
        }

        return prev[3];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Ninja's Training (2D DP) ===\n");

        RecursiveNinja rec = new RecursiveNinja();
        MemoNinja memo = new MemoNinja();
        TabNinja tab = new TabNinja();
        SpaceNinja space = new SpaceNinja();

        int[][][] testCases = {
            {{10, 40, 70}, {20, 50, 80}, {30, 60, 90}},
            {{10, 50, 1}, {5, 100, 11}},
            {{18, 11, 28}, {72, 13, 91}, {55, 12, 99}, {64, 80, 78}, {22, 77, 44}},
            {{10, 20, 30}},
            {{100, 1, 1}, {1, 1, 100}},
        };
        int[] expected = {210, 110, 345, 30, 200};

        for (int t = 0; t < testCases.length; t++) {
            int r = rec.ninjaTraining(testCases[t]);
            int m = memo.ninjaTraining(testCases[t]);
            int tb = tab.ninjaTraining(testCases[t]);
            int s = space.ninjaTraining(testCases[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("Test " + (t + 1) + ": Expected=" + expected[t]
                    + " | Rec=" + r + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s
                    + " | Pass=" + pass);
        }
    }
}
