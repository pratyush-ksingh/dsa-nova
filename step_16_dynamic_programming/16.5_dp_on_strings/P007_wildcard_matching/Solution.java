/**
 * Problem: Wildcard Matching (LeetCode 44)
 * Difficulty: HARD | XP: 50
 *
 * Given input string s and pattern p, implement wildcard matching where:
 *   '?' matches any single character.
 *   '*' matches any sequence of characters (including empty).
 * Return true if s matches p entirely.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Pure Recursion
    // Time: O(2^(m+n)) worst case  |  Space: O(m+n) stack
    // '*' branches into: match empty (advance j) OR match one char (advance i).
    // ============================================================
    static class BruteForce {

        private static boolean rec(String s, String p, int i, int j) {
            if (i == s.length() && j == p.length()) return true;
            if (j == p.length()) return false;
            if (i == s.length()) {
                // Remaining pattern must all be '*'
                for (int k = j; k < p.length(); k++) {
                    if (p.charAt(k) != '*') return false;
                }
                return true;
            }
            if (p.charAt(j) == '*') {
                return rec(s, p, i, j + 1)       // '*' matches empty
                    || rec(s, p, i + 1, j);       // '*' matches s[i]
            } else if (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i)) {
                return rec(s, p, i + 1, j + 1);
            }
            return false;
        }

        public static boolean solve(String s, String p) {
            return rec(s, p, 0, 0);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — 2D DP (Bottom-Up)
    // Time: O(m * n)  |  Space: O(m * n)
    // dp[i][j] = true if s[0..i-1] matches p[0..j-1]
    // ============================================================
    static class Optimal {

        public static boolean solve(String s, String p) {
            int m = s.length(), n = p.length();
            boolean[][] dp = new boolean[m + 1][n + 1];
            dp[0][0] = true;

            // Leading '*' in pattern can match empty string
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 1];
            }

            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (p.charAt(j - 1) == '*') {
                        // '*' matches empty (skip '*'): dp[i][j-1]
                        // '*' matches s[i-1] (keep '*'): dp[i-1][j]
                        dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                    } else if (p.charAt(j - 1) == '?' || p.charAt(j - 1) == s.charAt(i - 1)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                    // else remains false
                }
            }
            return dp[m][n];
        }
    }

    // ============================================================
    // APPROACH 3: BEST — 1D Space-Optimized DP
    // Time: O(m * n)  |  Space: O(n)
    // Same recurrence as Optimal, but rolled into a single 1D array.
    // Track `prev` (diagonal value dp[i-1][j-1]) before overwriting.
    // ============================================================
    static class Best {

        public static boolean solve(String s, String p) {
            int m = s.length(), n = p.length();
            boolean[] dp = new boolean[n + 1];
            dp[0] = true;

            // Initialize for leading stars
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j - 1) == '*') dp[j] = dp[j - 1];
                else break;
            }

            for (int i = 1; i <= m; i++) {
                boolean prev = dp[0];  // dp[i-1][j-1] before overwrite
                dp[0] = false;         // dp[i][0] = false for i >= 1
                for (int j = 1; j <= n; j++) {
                    boolean temp = dp[j];  // save dp[i-1][j]
                    if (p.charAt(j - 1) == '*') {
                        // dp[j] (old) = dp[i-1][j], dp[j-1] = dp[i][j-1]
                        dp[j] = dp[j] || dp[j - 1];
                    } else if (p.charAt(j - 1) == '?' || p.charAt(j - 1) == s.charAt(i - 1)) {
                        dp[j] = prev;
                    } else {
                        dp[j] = false;
                    }
                    prev = temp;
                }
            }
            return dp[n];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Wildcard Matching ===");

        String[][] tests = {
            {"aa",    "a",     "false"},
            {"aa",    "*",     "true"},
            {"cb",    "?a",    "false"},
            {"adceb", "*a*b",  "true"},
            {"acdcb", "a*c?b", "false"},
            {"",      "",      "true"},
            {"",      "*",     "true"},
            {"",      "?",     "false"},
            {"abc",   "a?c",   "true"},
            {"abc",   "a*c",   "true"},
        };

        for (String[] t : tests) {
            String s = t[0], p = t[1];
            boolean exp = Boolean.parseBoolean(t[2]);
            boolean b = BruteForce.solve(s, p);
            boolean o = Optimal.solve(s, p);
            boolean bt = Best.solve(s, p);
            String ok = (b == exp && o == exp && bt == exp) ? "OK" : "FAIL";
            System.out.printf("  s=%s, p=%s => Brute=%b, Optimal=%b, Best=%b, "
                + "Expected=%b [%s]%n", s, p, b, o, bt, exp, ok);
        }
    }
}
