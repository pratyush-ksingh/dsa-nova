/**
 * Problem: Shortest Common Supersequence (LeetCode 1092)
 * Difficulty: HARD | XP: 50
 *
 * Return the shortest string that has both str1 and str2 as subsequences.
 * SCS length = len(str1) + len(str2) - LCS(str1, str2).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — plain recursion
    // Time: O(2^(m+n))  |  Space: O(m+n) recursion stack
    // At each step match characters if equal, else try both branches
    // ============================================================
    static class BruteForce {
        public String shortestCommonSupersequence(String str1, String str2) {
            return helper(str1, str2, 0, 0);
        }

        private String helper(String s1, String s2, int i, int j) {
            if (i == s1.length()) return s2.substring(j);
            if (j == s2.length()) return s1.substring(i);
            if (s1.charAt(i) == s2.charAt(j)) {
                return s1.charAt(i) + helper(s1, s2, i + 1, j + 1);
            }
            String optA = s1.charAt(i) + helper(s1, s2, i + 1, j);
            String optB = s2.charAt(j) + helper(s1, s2, i, j + 1);
            return optA.length() <= optB.length() ? optA : optB;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — LCS DP + backtrack reconstruction
    // Time: O(m*n)  |  Space: O(m*n)
    // ============================================================
    static class Optimal {
        public String shortestCommonSupersequence(String str1, String str2) {
            int m = str1.length(), n = str2.length();

            // Build LCS DP table
            int[][] dp = new int[m + 1][n + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                        dp[i][j] = 1 + dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }

            // Backtrack to reconstruct SCS
            StringBuilder sb = new StringBuilder();
            int i = m, j = n;
            while (i > 0 && j > 0) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    sb.append(str1.charAt(i - 1));
                    i--;
                    j--;
                } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                    sb.append(str1.charAt(i - 1));
                    i--;
                } else {
                    sb.append(str2.charAt(j - 1));
                    j--;
                }
            }
            while (i > 0) { sb.append(str1.charAt(i - 1)); i--; }
            while (j > 0) { sb.append(str2.charAt(j - 1)); j--; }

            return sb.reverse().toString();
        }
    }

    // ============================================================
    // APPROACH 3: BEST — same O(m*n), tie-breaking with strict >
    // Time: O(m*n)  |  Space: O(m*n)
    // Full table required for reconstruction; no further space saving
    // possible without losing the ability to rebuild the actual string.
    // ============================================================
    static class Best {
        public String shortestCommonSupersequence(String str1, String str2) {
            int m = str1.length(), n = str2.length();

            int[][] dp = new int[m + 1][n + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            int i = m, j = n;
            while (i > 0 && j > 0) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    sb.append(str1.charAt(i - 1));
                    i--; j--;
                } else if (dp[i - 1][j] > dp[i][j - 1]) {
                    sb.append(str1.charAt(i - 1));
                    i--;
                } else {
                    sb.append(str2.charAt(j - 1));
                    j--;
                }
            }
            while (i > 0) { sb.append(str1.charAt(--i)); }
            while (j > 0) { sb.append(str2.charAt(--j)); }

            return sb.reverse().toString();
        }
    }

    // Helper: check s is a subsequence of scs
    private static boolean isSubseq(String scs, String s) {
        int j = 0;
        for (int i = 0; i < scs.length() && j < s.length(); i++) {
            if (scs.charAt(i) == s.charAt(j)) j++;
        }
        return j == s.length();
    }

    public static void main(String[] args) {
        String[][] tests = {
            {"abac", "cab"},
            {"geek", "eke"},
            {"AGGTAB", "GXTXAYB"},
            {"abc", "abc"},
            {"", "abc"}
        };

        BruteForce bf = new BruteForce();
        Optimal op = new Optimal();
        Best bt = new Best();

        System.out.println("=== Shortest Common Supersequence ===");
        for (String[] t : tests) {
            String s1 = t[0], s2 = t[1];
            String b  = bf.shortestCommonSupersequence(s1, s2);
            String o  = op.shortestCommonSupersequence(s1, s2);
            String bs = bt.shortestCommonSupersequence(s1, s2);

            boolean valid = isSubseq(b, s1)  && isSubseq(b, s2)
                         && isSubseq(o, s1)  && isSubseq(o, s2)
                         && isSubseq(bs, s1) && isSubseq(bs, s2)
                         && b.length() == o.length()
                         && o.length() == bs.length();
            String status = valid ? "PASS" : "FAIL";
            System.out.printf("[%s] '%s'+'%s' -> brute='%s'(%d) optimal='%s'(%d) best='%s'(%d)%n",
                    status, s1, s2, b, b.length(), o, o.length(), bs, bs.length());
        }
    }
}
