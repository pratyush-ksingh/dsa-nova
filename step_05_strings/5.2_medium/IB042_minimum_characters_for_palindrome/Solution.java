/**
 * Problem: Minimum Characters to Add to Make String a Palindrome
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Find the minimum number of characters to prepend to string A
 * to make it a palindrome.
 * Answer = len(A) - (length of longest palindromic prefix of A).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Check each prefix
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Try all prefix lengths from n down to 1.
         * The first (longest) palindromic prefix s[0..k-1] found means
         * we only need to prepend n - k characters.
         */
        public int minCharacters(String s) {
            int n = s.length();
            for (int k = n; k >= 1; k--) {
                if (isPalindrome(s, 0, k - 1)) {
                    return n - k;
                }
            }
            return n;
        }

        private boolean isPalindrome(String s, int lo, int hi) {
            while (lo < hi) {
                if (s.charAt(lo++) != s.charAt(hi--)) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - KMP on s + "#" + reverse(s)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /**
         * Construct combined = s + "#" + reverse(s).
         * Compute KMP LPS array for combined.
         * lps[last] = length of longest prefix of s that equals a suffix of reverse(s).
         * This prefix is a palindrome (it reads the same forwards and backwards).
         * Answer = n - lps[last].
         *
         * The '#' separator prevents the LPS from crossing s and rev(s) boundary.
         */
        public int minCharacters(String s) {
            int n = s.length();
            if (n == 0) return 0;

            String rev = new StringBuilder(s).reverse().toString();
            String combined = s + "#" + rev;
            int m = combined.length();

            int[] lps = computeLPS(combined);
            return n - lps[m - 1];
        }

        private int[] computeLPS(String pattern) {
            int m = pattern.length();
            int[] lps = new int[m];
            int len = 0, i = 1;
            while (i < m) {
                if (pattern.charAt(i) == pattern.charAt(len)) {
                    lps[i++] = ++len;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i++] = 0;
                    }
                }
            }
            return lps;
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Same KMP (optimized loop style)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Same KMP combined-string approach. Written with a cleaner
         * single-loop LPS builder. The '#' sentinel (not in alphabet)
         * guarantees lps never incorrectly crosses the s / rev(s) boundary.
         */
        public int minCharacters(String s) {
            int n = s.length();
            if (n == 0) return 0;

            String combined = s + "#" + new StringBuilder(s).reverse();
            int[] lps = new int[combined.length()];
            int j = 0;

            for (int i = 1; i < combined.length(); i++) {
                while (j > 0 && combined.charAt(i) != combined.charAt(j)) {
                    j = lps[j - 1];
                }
                if (combined.charAt(i) == combined.charAt(j)) j++;
                lps[i] = j;
            }

            return n - lps[combined.length() - 1];
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Minimum Characters for Palindrome ===");

        String[] inputs   = {"ABC", "abcd", "a", "aa", "ab", "aba", "aab"};
        int[]    expected = {2,     3,      0,   0,    1,    0,     1   };

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            int b  = bf.minCharacters(inputs[i]);
            int o  = opt.minCharacters(inputs[i]);
            int be = bst.minCharacters(inputs[i]);
            String status = (b == expected[i] && o == expected[i] && be == expected[i]) ? "OK" : "FAIL";
            System.out.printf("[%s] s=%s | Brute=%d, KMP=%d, Best=%d (expected %d)%n",
                status, inputs[i], b, o, be, expected[i]);
        }
    }
}
