/**
 * Problem: Implement StrStr (LeetCode 28 / InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Return the index of the first occurrence of needle in haystack,
 * or -1 if needle is not part of haystack.
 * If needle is empty, return 0.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Sliding window character comparison
    // Time: O(n * m)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Try every starting index i in haystack.
         * For each i, compare haystack[i..i+m-1] with needle char by char.
         * Return i on first complete match.
         */
        public int strStr(String haystack, String needle) {
            int n = haystack.length(), m = needle.length();
            if (m == 0) return 0;
            if (m > n)  return -1;

            for (int i = 0; i <= n - m; i++) {
                int j = 0;
                while (j < m && haystack.charAt(i + j) == needle.charAt(j)) {
                    j++;
                }
                if (j == m) return i;
            }
            return -1;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - KMP (Knuth-Morris-Pratt)
    // Time: O(n + m)  |  Space: O(m)
    // ============================================================
    static class Optimal {
        /**
         * Preprocess needle into LPS (Longest Proper Prefix which is also Suffix) array.
         * On mismatch during search, use lps to skip backward in needle
         * without re-checking already matched characters in haystack.
         */
        public int strStr(String haystack, String needle) {
            int n = haystack.length(), m = needle.length();
            if (m == 0) return 0;
            if (m > n)  return -1;

            int[] lps = buildLPS(needle);

            int i = 0, j = 0;
            while (i < n) {
                if (haystack.charAt(i) == needle.charAt(j)) {
                    i++; j++;
                }
                if (j == m) {
                    return i - j; // found
                } else if (i < n && haystack.charAt(i) != needle.charAt(j)) {
                    if (j != 0) {
                        j = lps[j - 1]; // skip using lps, don't increment i
                    } else {
                        i++;
                    }
                }
            }
            return -1;
        }

        private int[] buildLPS(String pattern) {
            int m = pattern.length();
            int[] lps = new int[m];
            int len = 0, i = 1;
            while (i < m) {
                if (pattern.charAt(i) == pattern.charAt(len)) {
                    lps[i++] = ++len;
                } else {
                    if (len != 0) {
                        len = lps[len - 1]; // don't increment i
                    } else {
                        lps[i++] = 0;
                    }
                }
            }
            return lps;
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Rabin-Karp Rolling Hash
    // Time: O(n + m) average, O(n*m) worst  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Rolling hash: compute hash of needle and first window of haystack.
         * Slide the window by subtracting leftmost char and adding new rightmost char.
         * On hash match, verify with equals() to avoid hash collisions.
         */
        public int strStr(String haystack, String needle) {
            int n = haystack.length(), m = needle.length();
            if (m == 0) return 0;
            if (m > n)  return -1;

            final long BASE = 31L;
            final long MOD  = 1_000_000_007L;

            // Precompute BASE^(m-1) % MOD
            long power = 1;
            for (int k = 0; k < m - 1; k++) {
                power = (power * BASE) % MOD;
            }

            long needleHash = 0, windowHash = 0;
            for (int k = 0; k < m; k++) {
                needleHash = (needleHash * BASE + (needle.charAt(k) - 'a' + 1)) % MOD;
                windowHash = (windowHash * BASE + (haystack.charAt(k) - 'a' + 1)) % MOD;
            }

            if (windowHash == needleHash && haystack.substring(0, m).equals(needle)) return 0;

            for (int i = 1; i <= n - m; i++) {
                windowHash = (windowHash - (haystack.charAt(i - 1) - 'a' + 1) * power % MOD + MOD) % MOD;
                windowHash = (windowHash * BASE + (haystack.charAt(i + m - 1) - 'a' + 1)) % MOD;
                if (windowHash == needleHash && haystack.substring(i, i + m).equals(needle)) {
                    return i;
                }
            }
            return -1;
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Implement StrStr ===");

        String[][] inputs   = {
            {"sadbutsad", "sad"},
            {"leetcode", "leeto"},
            {"hello", "ll"},
            {"aaa", "aaaa"},
            {"a", "a"},
            {"", ""},
            {"mississippi", "issip"}
        };
        int[] expected = {0, -1, 2, -1, 0, 0, 4};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            int b  = bf.strStr(inputs[i][0], inputs[i][1]);
            int o  = opt.strStr(inputs[i][0], inputs[i][1]);
            int be = bst.strStr(inputs[i][0], inputs[i][1]);
            String status = (b == expected[i] && o == expected[i] && be == expected[i]) ? "OK" : "FAIL";
            System.out.printf("[%s] haystack=%s, needle=%s | Brute=%d, KMP=%d, RK=%d (expected %d)%n",
                status, inputs[i][0], inputs[i][1], b, o, be, expected[i]);
        }
    }
}
