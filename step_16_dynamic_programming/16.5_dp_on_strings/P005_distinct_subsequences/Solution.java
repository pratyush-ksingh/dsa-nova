/**
 * Problem: Distinct Subsequences (LeetCode 115)
 * Difficulty: HARD | XP: 50
 *
 * Given two strings s and t, return the number of distinct subsequences
 * of s which equal t. A subsequence is formed by deleting some (or no)
 * characters from s without changing the relative order of the rest.
 *
 * Example: s="rabbbit", t="rabbit" => 3
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Pure Recursion
    // Time: O(2^m)  |  Space: O(m) stack  (m = s.length)
    // At each s position: skip it or use it (if matches t[j]).
    // ============================================================
    static class BruteForce {

        private static int rec(String s, String t, int i, int j) {
            if (j == t.length()) return 1;  // all of t matched
            if (i == s.length()) return 0;  // s exhausted
            int result = rec(s, t, i + 1, j);  // always skip s[i]
            if (s.charAt(i) == t.charAt(j)) {
                result += rec(s, t, i + 1, j + 1);  // use s[i]
            }
            return result;
        }

        public static int solve(String s, String t) {
            return rec(s, t, 0, 0);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — 2D DP (Bottom-Up)
    // Time: O(m * n)  |  Space: O(m * n)
    // dp[i][j] = # distinct subsequences of s[0..i-1] equal to t[0..j-1]
    // ============================================================
    static class Optimal {

        public static int solve(String s, String t) {
            int m = s.length(), n = t.length();
            if (n > m) return 0;

            long[][] dp = new long[m + 1][n + 1];
            // Empty t is matched by every prefix of s
            for (int i = 0; i <= m; i++) dp[i][0] = 1;

            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    dp[i][j] = dp[i - 1][j];  // skip s[i-1]
                    if (s.charAt(i - 1) == t.charAt(j - 1)) {
                        dp[i][j] += dp[i - 1][j - 1];  // use s[i-1]
                    }
                }
            }
            return (int) dp[m][n];
        }
    }

    // ============================================================
    // APPROACH 3: BEST — 1D DP (Space-Optimized)
    // Time: O(m * n)  |  Space: O(n)
    // Roll the 2D table into a single 1D array. Traverse j right-to-left
    // so dp[j-1] still represents the previous row's dp[i-1][j-1].
    // ============================================================
    static class Best {

        public static int solve(String s, String t) {
            int m = s.length(), n = t.length();
            if (n > m) return 0;

            long[] dp = new long[n + 1];
            dp[0] = 1;  // empty t always matchable

            for (int i = 0; i < m; i++) {
                // Right to left: dp[j-1] still holds dp[i-1][j-1]
                for (int j = n; j >= 1; j--) {
                    if (s.charAt(i) == t.charAt(j - 1)) {
                        dp[j] += dp[j - 1];
                    }
                }
            }
            return (int) dp[n];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Distinct Subsequences ===");

        String[][] tests = {
            {"rabbbit", "rabbit", "3"},
            {"babgbag", "bag",    "5"},
            {"b",       "b",      "1"},
            {"b",       "a",      "0"},
            {"aaa",     "a",      "3"},
            {"aaa",     "aa",     "3"},
            {"",        "a",      "0"},
            {"a",       "",       "1"},
        };

        for (String[] t : tests) {
            String s = t[0], pat = t[1];
            int exp = Integer.parseInt(t[2]);
            int b = BruteForce.solve(s, pat);
            int o = Optimal.solve(s, pat);
            int bt = Best.solve(s, pat);
            String ok = (b == exp && o == exp && bt == exp) ? "OK" : "FAIL";
            System.out.printf("  s=%s, t=%s => Brute=%d, Optimal=%d, Best=%d, "
                + "Expected=%d [%s]%n", s, pat, b, o, bt, exp, ok);
        }
    }
}
