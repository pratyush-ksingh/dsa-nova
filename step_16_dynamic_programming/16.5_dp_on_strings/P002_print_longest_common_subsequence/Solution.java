/**
 * Problem: Print Longest Common Subsequence
 * Difficulty: MEDIUM | XP: 25
 *
 * Not just find LENGTH of LCS but actually PRINT the LCS string.
 * Build DP table, then backtrack through it.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Tab + Backtrack (Print)
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Plain Recursion (Length only)
// Time: O(2^(m+n)) | Space: O(m+n)
// ============================================================
class RecursiveLCS {
    public int lcsLength(String s1, String s2) {
        return solve(s1, s2, s1.length(), s2.length());
    }

    private int solve(String s1, String s2, int i, int j) {
        if (i == 0 || j == 0) return 0;

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            return 1 + solve(s1, s2, i - 1, j - 1);
        }

        return Math.max(solve(s1, s2, i - 1, j), solve(s1, s2, i, j - 1));
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class MemoLCS {
    public int lcsLength(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        // Initialize with -1 to distinguish unvisited
        for (int[] row : dp) java.util.Arrays.fill(row, -1);
        return solve(s1, s2, m, n, dp);
    }

    private int solve(String s1, String s2, int i, int j, int[][] dp) {
        if (i == 0 || j == 0) return 0;
        if (dp[i][j] != -1) return dp[i][j];

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            dp[i][j] = 1 + solve(s1, s2, i - 1, j - 1, dp);
        } else {
            dp[i][j] = Math.max(solve(s1, s2, i - 1, j, dp),
                                solve(s1, s2, i, j - 1, dp));
        }
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class TabLCS {
    public int lcsLength(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }
}

// ============================================================
// Approach 4: Tabulation + Backtracking (PRINT the LCS)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class PrintLCS {
    public String lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // Phase 1: Build the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // Phase 2: Backtrack to reconstruct the LCS string
        int lcsLen = dp[m][n];
        char[] result = new char[lcsLen];
        int idx = lcsLen - 1;  // Fill from the end

        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                // This character is part of LCS
                result[idx] = s1.charAt(i - 1);
                idx--;
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                // Move up (skip character from s1)
                i--;
            } else {
                // Move left (skip character from s2)
                j--;
            }
        }

        return new String(result);
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Print Longest Common Subsequence ===\n");

        RecursiveLCS rec = new RecursiveLCS();
        MemoLCS memo = new MemoLCS();
        TabLCS tab = new TabLCS();
        PrintLCS printer = new PrintLCS();

        String[][] pairs = {
            {"abcde", "ace"},
            {"acd", "ced"},
            {"abc", "def"},
            {"abcba", "abcbcba"},
            {"a", "a"},
            {"a", "b"},
            {"", "abc"},
        };
        int[] expectedLen = {3, 2, 0, 5, 1, 0, 0};
        String[] expectedStr = {"ace", "cd", "", "abcba", "a", "", ""};

        for (int t = 0; t < pairs.length; t++) {
            String s1 = pairs[t][0], s2 = pairs[t][1];
            int r = rec.lcsLength(s1, s2);
            int m = memo.lcsLength(s1, s2);
            int tb = tab.lcsLength(s1, s2);
            String printed = printer.lcs(s1, s2);

            boolean pass = r == expectedLen[t] && m == expectedLen[t]
                    && tb == expectedLen[t] && printed.equals(expectedStr[t]);

            System.out.println("s1=\"" + s1 + "\", s2=\"" + s2 + "\"");
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb
                    + " | Print=\"" + printed + "\"");
            System.out.println("  Expected len=" + expectedLen[t]
                    + ", str=\"" + expectedStr[t] + "\" | Pass=" + pass + "\n");
        }
    }
}
