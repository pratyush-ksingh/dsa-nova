/**
 * Problem: Convert to Palindrome (Valid Palindrome II - LeetCode 680 / InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a non-empty string s, check if it can become a palindrome
 * by deleting at most one character.
 * Return 1 if yes, 0 if no.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Try removing each character
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Check if original string is already a palindrome.
         * If not, try removing each character one by one and check.
         * Return 1 if any single removal produces a palindrome.
         */
        public int isPalindromePossible(String s) {
            if (isPalindrome(s)) return 1;
            for (int i = 0; i < s.length(); i++) {
                String candidate = s.substring(0, i) + s.substring(i + 1);
                if (isPalindrome(candidate)) return 1;
            }
            return 0;
        }

        private boolean isPalindrome(String s) {
            int lo = 0, hi = s.length() - 1;
            while (lo < hi) {
                if (s.charAt(lo++) != s.charAt(hi--)) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Two pointers, skip one on mismatch
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Use two pointers from both ends.
         * On mismatch, try skipping left or right character.
         * If either resulting substring is a palindrome, return 1.
         * We get at most one skip.
         */
        public int isPalindromePossible(String s) {
            int lo = 0, hi = s.length() - 1;
            while (lo < hi) {
                if (s.charAt(lo) != s.charAt(hi)) {
                    return (isPalindromeRange(s, lo + 1, hi) ||
                            isPalindromeRange(s, lo, hi - 1)) ? 1 : 0;
                }
                lo++;
                hi--;
            }
            return 1;
        }

        private boolean isPalindromeRange(String s, int lo, int hi) {
            while (lo < hi) {
                if (s.charAt(lo++) != s.charAt(hi--)) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Same two pointer (explicit helper, clean)
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Identical to Approach 2. Using a clear helper that checks
         * a [lo, hi] range for palindrome validity.
         * The key insight: we have exactly ONE skip budget.
         * The moment we hit a mismatch, we must decide — skip left or skip right.
         */
        public int isPalindromePossible(String s) {
            int lo = 0, hi = s.length() - 1;
            while (lo < hi) {
                if (s.charAt(lo) != s.charAt(hi)) {
                    return (check(s, lo + 1, hi) || check(s, lo, hi - 1)) ? 1 : 0;
                }
                lo++;
                hi--;
            }
            return 1;
        }

        private boolean check(String s, int lo, int hi) {
            while (lo < hi) {
                if (s.charAt(lo++) != s.charAt(hi--)) return false;
            }
            return true;
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Convert to Palindrome ===");

        String[] inputs   = {"aba", "abca", "abc", "a", "deeee", "aab"};
        int[]    expected = {1,     1,      0,     1,   1,       1   };

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            int b  = bf.isPalindromePossible(inputs[i]);
            int o  = opt.isPalindromePossible(inputs[i]);
            int be = bst.isPalindromePossible(inputs[i]);
            String status = (b == expected[i] && o == expected[i] && be == expected[i]) ? "OK" : "FAIL";
            System.out.printf("[%s] s=%s | Brute=%d, Optimal=%d, Best=%d (expected %d)%n",
                status, inputs[i], b, o, be, expected[i]);
        }
    }
}
