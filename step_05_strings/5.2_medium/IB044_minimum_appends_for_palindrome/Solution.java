import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(n)
// Try removing suffix chars one by one; check if remaining prefix
// is a palindrome. Count how many chars were removed.
// ============================================================
class BruteForce {
    private static boolean isPalindrome(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) return false;
            start++;
            end--;
        }
        return true;
    }

    public static int solve(String s) {
        int n = s.length();
        for (int len = n; len >= 1; len--) {
            if (isPalindrome(s, 0, len - 1)) {
                return n - len;
            }
        }
        return n - 1;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// Use KMP failure function on s + "#" + reverse(s).
// lps[last] = length of longest palindromic prefix of s.
// Answer = n - lps[last].
// ============================================================
class Optimal {
    public static int solve(String s) {
        String rev = new StringBuilder(s).reverse().toString();
        String combined = s + "#" + rev;
        int m = combined.length();
        int[] lps = new int[m];

        int len = 0, i = 1;
        while (i < m) {
            if (combined.charAt(i) == combined.charAt(len)) {
                lps[i++] = ++len;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
        }
        return s.length() - lps[m - 1];
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// Same KMP approach with a reusable helper. Asymptotically optimal
// — cannot determine palindrome prefix length faster than O(n).
// ============================================================
class Best {
    private static int[] buildLPS(char[] pattern) {
        int n = pattern.length;
        int[] lps = new int[n];
        int len = 0, i = 1;
        while (i < n) {
            if (pattern[i] == pattern[len]) {
                lps[i++] = ++len;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    public static int solve(String s) {
        int n = s.length();
        String rev = new StringBuilder(s).reverse().toString();
        char[] pattern = (s + "$" + rev).toCharArray();
        int[] lps = buildLPS(pattern);
        return n - lps[pattern.length - 1];
    }
}

public class Solution {
    public static void main(String[] args) {
        String[] tests    = {"abcd", "aba", "aacecaaa", "racecar", "a", "ab"};
        int[]    expected = {3,       0,     1,           0,         0,   1};

        System.out.println("=== Minimum Appends for Palindrome ===");
        for (int i = 0; i < tests.length; i++) {
            String s = tests[i];
            int b    = BruteForce.solve(s);
            int o    = Optimal.solve(s);
            int best = Best.solve(s);
            String status = (b == expected[i] && o == expected[i] && best == expected[i]) ? "PASS" : "FAIL";
            System.out.printf("Input: %-12s | Brute: %d | Optimal: %d | Best: %d | Expected: %d | %s%n",
                    "\"" + s + "\"", b, o, best, expected[i], status);
        }
    }
}
