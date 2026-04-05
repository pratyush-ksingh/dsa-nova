/**
 * Problem: Ways to Decode
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * A message containing letters 'A'-'Z' is encoded as '1'-'26'.
 * Given an encoded string of digits, count the number of ways to decode it.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Recursion)
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    // At each position, try decoding one digit or two digits.
    // A single digit is valid if it is '1'-'9'.
    // Two digits are valid if they form a number between 10 and 26.
    static class BruteForce {
        public static int numDecodings(String s) {
            return solve(s, 0);
        }

        private static int solve(String s, int idx) {
            if (idx == s.length()) return 1;       // decoded completely
            if (s.charAt(idx) == '0') return 0;    // leading zero – invalid

            // Option 1: take one digit
            int ways = solve(s, idx + 1);

            // Option 2: take two digits (if valid 10-26)
            if (idx + 1 < s.length()) {
                int twoDigit = Integer.parseInt(s.substring(idx, idx + 2));
                if (twoDigit >= 10 && twoDigit <= 26) {
                    ways += solve(s, idx + 2);
                }
            }
            return ways;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    // Same recursion but cache results in a memo array to avoid
    // recomputing the same sub-problems.
    static class Optimal {
        public static int numDecodings(String s) {
            int n = s.length();
            int[] memo = new int[n + 1];
            java.util.Arrays.fill(memo, -1);
            return solve(s, 0, memo);
        }

        private static int solve(String s, int idx, int[] memo) {
            if (idx == s.length()) return 1;
            if (s.charAt(idx) == '0') return 0;
            if (memo[idx] != -1) return memo[idx];

            int ways = solve(s, idx + 1, memo);

            if (idx + 1 < s.length()) {
                int twoDigit = Integer.parseInt(s.substring(idx, idx + 2));
                if (twoDigit >= 10 && twoDigit <= 26) {
                    ways += solve(s, idx + 2, memo);
                }
            }
            memo[idx] = ways;
            return ways;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Bottom-Up DP, O(1) Space)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    // dp[i] = number of ways to decode s[0..i-1].
    // We only need dp[i-1] and dp[i-2] at any point, so use two variables.
    //
    // Transition:
    //   - If s[i-1] != '0': prev1 contributes (one-digit decode)
    //   - If s[i-2..i-1] in "10"-"26": prev2 contributes (two-digit decode)
    static class Best {
        public static int numDecodings(String s) {
            int n = s.length();
            if (n == 0 || s.charAt(0) == '0') return 0;

            int prev2 = 1;  // dp[0]: empty string = 1 way
            int prev1 = 1;  // dp[1]: first char already validated non-zero

            for (int i = 2; i <= n; i++) {
                int curr = 0;

                // Single-digit decode: s[i-1]
                if (s.charAt(i - 1) != '0') {
                    curr += prev1;
                }

                // Two-digit decode: s[i-2..i-1]
                int twoDigit = Integer.parseInt(s.substring(i - 2, i));
                if (twoDigit >= 10 && twoDigit <= 26) {
                    curr += prev2;
                }

                prev2 = prev1;
                prev1 = curr;
            }
            return prev1;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Ways to Decode ===");

        String[] tests = {"12", "226", "06", "0", "10", "27", "11106"};
        // Expected: 2, 3, 0, 0, 1, 1, 2

        for (String s : tests) {
            int bf = BruteForce.numDecodings(s);
            int op = Optimal.numDecodings(s);
            int be = Best.numDecodings(s);
            System.out.printf("Input: %-8s | Brute: %d | Optimal: %d | Best: %d%n",
                              "\"" + s + "\"", bf, op, be);
        }
    }
}
