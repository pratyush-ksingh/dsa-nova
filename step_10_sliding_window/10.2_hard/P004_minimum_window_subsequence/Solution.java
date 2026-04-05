/**
 * Problem: Minimum Window Subsequence
 * Difficulty: HARD | XP: 50
 *
 * Find the smallest window in S such that T appears as a SUBSEQUENCE
 * (characters must appear in order, not necessarily contiguous).
 * Return "" if no such window exists.
 * Real-life use: Sequence alignment, DNA pattern search, diff tools.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Check every substring of S; verify if T is a subsequence of it.
    // Time: O(N^2 * |T|)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        public static String minWindowSubseq(String s, String t) {
            String result = "";
            for (int i = 0; i < s.length(); i++) {
                for (int j = i + t.length(); j <= s.length(); j++) {
                    String window = s.substring(i, j);
                    if (isSubseq(window, t)) {
                        if (result.isEmpty() || window.length() < result.length()) {
                            result = window;
                        }
                        break; // can't make it shorter by extending j
                    }
                }
            }
            return result;
        }

        private static boolean isSubseq(String window, String t) {
            int j = 0;
            for (int i = 0; i < window.length() && j < t.length(); i++) {
                if (window.charAt(i) == t.charAt(j)) j++;
            }
            return j == t.length();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Two-pointer scan:
    // 1. Move right pointer forward matching T left-to-right.
    // 2. Once T is fully matched, move left pointer backward matching T right-to-left.
    //    This shrinks the window to the tightest fit.
    // Time: O(|S| * |T|) — actually O(|S|) amortized  |  Space: O(1)
    // ============================================================
    static class Optimal {
        public static String minWindowSubseq(String s, String t) {
            int minLen = Integer.MAX_VALUE;
            String result = "";
            int i = 0;
            while (i < s.length()) {
                // Forward pass: find end of window where T is a subsequence
                int j = 0; // pointer into T
                while (i < s.length() && j < t.length()) {
                    if (s.charAt(i) == t.charAt(j)) j++;
                    i++;
                }
                if (j < t.length()) break; // T not found
                // Backward pass: shrink window from right
                int end = i - 1;
                j = t.length() - 1;
                while (j >= 0) {
                    if (s.charAt(end) == t.charAt(j)) j--;
                    end--;
                }
                int start = end + 1;
                if (i - start < minLen) {
                    minLen = i - start;
                    result = s.substring(start, i);
                }
                i = start + 1; // restart search from next position
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // DP table: dp[i][j] = starting index in S from which T[0..j-1] is a
    // subsequence ending at S[i-1]. Then scan dp[*][|T|] for shortest window.
    // Time: O(|S| * |T|)  |  Space: O(|S| * |T|)
    // ============================================================
    static class Best {
        public static String minWindowSubseq(String s, String t) {
            int m = s.length(), n = t.length();
            // dp[i][j]: starting index in S if we match T[0..j-1] ending at S[i-1]
            int[][] dp = new int[m + 1][n + 1];
            for (int[] row : dp) Arrays.fill(row, -1);
            // Base case: matching T[0...-1] (empty T) ends at position i starting at i
            for (int i = 0; i <= m; i++) dp[i][0] = i;

            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (s.charAt(i - 1) == t.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = dp[i - 1][j];
                    }
                }
            }
            int minLen = Integer.MAX_VALUE, start = -1;
            for (int i = n; i <= m; i++) {
                if (dp[i][n] != -1 && i - dp[i][n] < minLen) {
                    minLen = i - dp[i][n];
                    start = dp[i][n];
                }
            }
            return start == -1 ? "" : s.substring(start, start + minLen);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Minimum Window Subsequence ===");

        String[][] tests = {
            {"abcdebdde", "bde"},  // "bcde" or "bdde"
            {"jmeqksfrsdtmsplmvwiqjexpsgwtdlgslb", "lim"},
            {"cnhczmccqouqadqtmjjzl", "cm"},
            {"a", "a"},
        };
        for (String[] t : tests) {
            System.out.printf("%nS=\"%s\"  T=\"%s\"%n", t[0], t[1]);
            System.out.println("  Brute  : \"" + BruteForce.minWindowSubseq(t[0], t[1]) + "\"");
            System.out.println("  Optimal: \"" + Optimal.minWindowSubseq(t[0], t[1]) + "\"");
            System.out.println("  Best   : \"" + Best.minWindowSubseq(t[0], t[1]) + "\"");
        }
    }
}
