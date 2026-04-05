/**
 * Problem: Palindrome Partitioning II
 * Difficulty: HARD | XP: 50
 * LeetCode: 132
 *
 * Given a string s, partition it so every substring is a palindrome.
 * Return the MINIMUM number of cuts needed.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Recursive Exhaustive Search
    // Time: O(2^n * n)  |  Space: O(n) recursion stack
    // ============================================================
    static class BruteForce {
        private String s;
        private int n;

        /**
         * At each position 'start', try every valid end point.
         * If s[start..end] is a palindrome, recurse on s[end+1..n-1] and add 1 cut.
         * Take the minimum across all valid end points.
         */
        public int minCut(String s) {
            this.s = s;
            this.n = s.length();
            return recurse(0);
        }

        private int recurse(int start) {
            if (start == n) return 0;
            if (isPalindrome(start, n - 1)) return 0; // whole suffix is palindrome

            int minCuts = Integer.MAX_VALUE;
            for (int end = start; end < n - 1; end++) {
                if (isPalindrome(start, end)) {
                    int cuts = 1 + recurse(end + 1);
                    minCuts = Math.min(minCuts, cuts);
                }
            }
            return minCuts;
        }

        private boolean isPalindrome(int l, int r) {
            while (l < r) {
                if (s.charAt(l) != s.charAt(r)) return false;
                l++; r--;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Palindrome Table + 1D DP
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    static class Optimal {
        /**
         * Phase 1 — Precompute palindrome table:
         *   pal[i][j] = true if s[i..j] is a palindrome.
         *   Fill from shorter to longer substrings:
         *     pal[i][i]     = true  (single char)
         *     pal[i][i+1]   = s[i] == s[i+1]
         *     pal[i][j]     = s[i] == s[j] && pal[i+1][j-1]
         *
         * Phase 2 — 1D DP for min cuts:
         *   dp[i] = min cuts for s[0..i]
         *   dp[i] = 0 if s[0..i] is a palindrome
         *   dp[i] = min(dp[j-1] + 1) for all j in [1..i] where s[j..i] is palindrome
         *
         * Answer: dp[n-1]
         */
        public int minCut(String s) {
            int n = s.length();
            if (n <= 1) return 0;

            // Phase 1: palindrome table
            boolean[][] pal = new boolean[n][n];
            for (int i = 0; i < n; i++) pal[i][i] = true;
            for (int i = 0; i < n - 1; i++) pal[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
            for (int len = 3; len <= n; len++) {
                for (int i = 0; i <= n - len; i++) {
                    int j = i + len - 1;
                    pal[i][j] = (s.charAt(i) == s.charAt(j)) && pal[i + 1][j - 1];
                }
            }

            // Phase 2: DP
            int[] dp = new int[n];
            for (int i = 0; i < n; i++) {
                if (pal[0][i]) {
                    dp[i] = 0;
                } else {
                    dp[i] = i; // worst case
                    for (int j = 1; j <= i; j++) {
                        if (pal[j][i]) {
                            dp[i] = Math.min(dp[i], dp[j - 1] + 1);
                        }
                    }
                }
            }
            return dp[n - 1];
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Expand Around Center + DP (O(n) Space)
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Instead of a full O(n^2) palindrome table, expand each center outward.
         * For every character/gap as center, expand while characters match.
         * Each expansion reveals a palindrome s[left..right]; use it to update dp[right]:
         *
         *   dp[right] = min(dp[right], left == 0 ? 0 : dp[left-1] + 1)
         *
         * dp[i] = minimum cuts to partition s[0..i].
         * Initialize dp[i] = i (cut before every character = i cuts for i+1 chars).
         *
         * Same O(n^2) time as Approach 2 but O(n) space (no pal[][] table).
         * Constant factor is also better because we avoid random-access of a 2D table.
         */
        public int minCut(String s) {
            int n = s.length();
            if (n <= 1) return 0;

            int[] dp = new int[n];
            for (int i = 0; i < n; i++) dp[i] = i; // worst-case init

            for (int center = 0; center < n; center++) {
                // Odd-length palindromes
                expand(s, dp, center, center, n);
                // Even-length palindromes
                expand(s, dp, center, center + 1, n);
            }
            return dp[n - 1];
        }

        private void expand(String s, int[] dp, int left, int right, int n) {
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                // s[left..right] is a palindrome
                int newCuts = (left == 0) ? 0 : dp[left - 1] + 1;
                dp[right] = Math.min(dp[right], newCuts);
                left--;
                right++;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Palindrome Partitioning II ===\n");

        String[] inputs   = {"aab", "a", "ab", "aa", "abcba", "aabb"};
        int[]    expected = {1,      0,   1,    0,    0,       1};

        BruteForce bf = new BruteForce();
        Optimal    op = new Optimal();
        Best       be = new Best();

        for (int i = 0; i < inputs.length; i++) {
            String input = inputs[i];
            int bfRes = bf.minCut(input);
            int opRes = op.minCut(input);
            int beRes = be.minCut(input);
            String status = (bfRes == opRes && opRes == beRes && beRes == expected[i]) ? "OK" : "MISMATCH";
            System.out.printf("s=%-12s => Brute=%d, Optimal=%d, Best=%d | Expected=%d [%s]%n",
                "\"" + input + "\"", bfRes, opRes, beRes, expected[i], status);
        }
    }
}
