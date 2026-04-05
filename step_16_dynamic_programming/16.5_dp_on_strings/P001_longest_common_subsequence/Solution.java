/**
 * Problem: Longest Common Subsequence (LeetCode #1143)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find length of LCS of two strings.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^(m+n)) | Space: O(m+n)
// ============================================================
class RecursiveLCS {
    public int longestCommonSubsequence(String text1, String text2) {
        return solve(text1.length() - 1, text2.length() - 1, text1, text2);
    }

    private int solve(int i, int j, String s1, String s2) {
        if (i < 0 || j < 0) return 0;

        // Characters match -- both part of LCS
        if (s1.charAt(i) == s2.charAt(j)) {
            return 1 + solve(i - 1, j - 1, s1, s2);
        }

        // Mismatch -- try skipping one character from each string
        return Math.max(solve(i - 1, j, s1, s2), solve(i, j - 1, s1, s2));
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(m * n) | Space: O(m * n)
// Uses 1-based indexing to simplify base cases
// ============================================================
class MemoLCS {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(m, n, text1, text2, dp);
    }

    // 1-based: solve(i,j) considers text1[0..i-1] and text2[0..j-1]
    private int solve(int i, int j, String s1, String s2, int[][] dp) {
        if (i == 0 || j == 0) return 0;
        if (dp[i][j] != -1) return dp[i][j];

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            dp[i][j] = 1 + solve(i - 1, j - 1, s1, s2, dp);
        } else {
            dp[i][j] = Math.max(solve(i - 1, j, s1, s2, dp),
                                solve(i, j - 1, s1, s2, dp));
        }
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class TabLCS {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        // dp[0][*] = dp[*][0] = 0 (already initialized)

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];  // match: diagonal + 1
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);  // max(up, left)
                }
            }
        }

        return dp[m][n];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(m * n) | Space: O(min(m, n))
// ============================================================
class SpaceLCS {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();

        // Optimize: make the shorter string the column dimension
        if (m < n) {
            return longestCommonSubsequence(text2, text1);
        }

        int[] prev = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            int[] curr = new int[n + 1];
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[j] = 1 + prev[j - 1];  // diagonal
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);  // max(up, left)
                }
            }
            prev = curr;
        }

        return prev[n];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Common Subsequence ===\n");

        RecursiveLCS rec = new RecursiveLCS();
        MemoLCS memo = new MemoLCS();
        TabLCS tab = new TabLCS();
        SpaceLCS space = new SpaceLCS();

        String[][] texts = {
            {"abcde", "ace"},
            {"abc", "abc"},
            {"abc", "def"},
            {"aab", "azb"},
            {"a", "a"},
            {"a", "b"},
            {"bsbininm", "jmjkbkjkv"},
        };
        int[] expected = {3, 3, 0, 2, 1, 0, 1};

        for (int t = 0; t < texts.length; t++) {
            int r = rec.longestCommonSubsequence(texts[t][0], texts[t][1]);
            int m = memo.longestCommonSubsequence(texts[t][0], texts[t][1]);
            int tb = tab.longestCommonSubsequence(texts[t][0], texts[t][1]);
            int s = space.longestCommonSubsequence(texts[t][0], texts[t][1]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("text1=\"" + texts[t][0] + "\", text2=\"" + texts[t][1] + "\"");
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
