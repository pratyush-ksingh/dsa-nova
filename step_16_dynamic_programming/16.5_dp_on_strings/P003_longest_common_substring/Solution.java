/**
 * Problem: Longest Common Substring
 * Difficulty: MEDIUM | XP: 25
 *
 * Find length of longest common substring between two strings.
 * dp[i][j] resets to 0 on mismatch (unlike LCS).
 * All 4 DP approaches.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(3^(n+m)) | Space: O(n+m) stack
// ============================================================
class Recursive {
    private int maxLen;

    public int longestCommonSubstring(String s1, String s2) {
        maxLen = 0;
        // Try every (i, j) endpoint and find matching streak length
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                maxLen = Math.max(maxLen, solve(s1, s2, i, j));
            }
        }
        return maxLen;
    }

    // Returns length of common substring ending at s1[i-1] and s2[j-1]
    private int solve(String s1, String s2, int i, int j) {
        if (i == 0 || j == 0) return 0;
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return 1 + solve(s1, s2, i - 1, j - 1);
        }
        return 0; // streak breaks
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n * m) | Space: O(n * m)
// ============================================================
class Memoization {
    public int longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int[] row : dp) Arrays.fill(row, -1);

        int maxLen = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                maxLen = Math.max(maxLen, solve(s1, s2, i, j, dp));
            }
        }
        return maxLen;
    }

    private int solve(String s1, String s2, int i, int j, int[][] dp) {
        if (i == 0 || j == 0) return 0;
        if (dp[i][j] != -1) return dp[i][j];

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            dp[i][j] = 1 + solve(s1, s2, i - 1, j - 1, dp);
        } else {
            dp[i][j] = 0;
        }
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n * m) | Space: O(n * m)
// ============================================================
class Tabulation {
    public int longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        // dp[0][*] = dp[*][0] = 0 (already initialized)

        int maxLen = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    maxLen = Math.max(maxLen, dp[i][j]);
                } else {
                    dp[i][j] = 0; // streak resets
                }
            }
        }
        return maxLen;
    }
}

// ============================================================
// Approach 4: Space Optimized (1D array, right-to-left)
// Time: O(n * m) | Space: O(m)
// ============================================================
class SpaceOptimized {
    public int longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[] dp = new int[m + 1];

        int maxLen = 0;

        for (int i = 1; i <= n; i++) {
            // Traverse right-to-left to preserve dp[j-1] from previous row
            for (int j = m; j >= 1; j--) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[j] = dp[j - 1] + 1;
                    maxLen = Math.max(maxLen, dp[j]);
                } else {
                    dp[j] = 0;
                }
            }
        }
        return maxLen;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Common Substring ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        String[][] tests = {
            {"abcde", "abfce"},
            {"abc", "abc"},
            {"abc", "def"},
            {"abcdxyz", "xyzabcd"},
            {"", "abc"},
            {"a", "a"},
            {"zxabcdezy", "yzabcdezx"},
        };
        int[] expected = {2, 3, 0, 4, 0, 1, 6};

        System.out.printf("%-20s %-20s %-8s %-8s %-8s %-8s %-8s %-6s%n",
                "s1", "s2", "Recurse", "Memo", "Tab", "Space", "Expect", "Pass");
        System.out.println("-".repeat(100));

        for (int t = 0; t < tests.length; t++) {
            String s1 = tests[t][0], s2 = tests[t][1];
            int r = rec.longestCommonSubstring(s1, s2);
            int m = memo.longestCommonSubstring(s1, s2);
            int tb = tab.longestCommonSubstring(s1, s2);
            int s = space.longestCommonSubstring(s1, s2);

            boolean pass = (r == expected[t]) && (m == expected[t]) &&
                           (tb == expected[t]) && (s == expected[t]);

            System.out.printf("%-20s %-20s %-8d %-8d %-8d %-8d %-8d %-6s%n",
                    "\"" + s1 + "\"", "\"" + s2 + "\"", r, m, tb, s, expected[t],
                    pass ? "PASS" : "FAIL");
        }

        // Demonstrate the difference from LCS
        System.out.println("\n--- LCS vs Longest Common Substring ---");
        System.out.println("s1='abcde', s2='ace'");
        System.out.println("LCS = 3 (a, c, e -- not contiguous)");
        System.out.println("Longest Common Substring = " + tab.longestCommonSubstring("abcde", "ace") +
                " (only single char matches)");
    }
}
