/**
 * Problem: Longest Palindromic Substring (LeetCode #5)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check All Substrings
    // Time: O(n³)  |  Space: O(1)
    //
    // For every pair (i, j), verify if s[i..j] is a palindrome
    // using a two-pointer check. Track the longest found.
    // ============================================================
    static class BruteForce {
        private boolean isPalindrome(String s, int l, int r) {
            while (l < r) {
                if (s.charAt(l) != s.charAt(r)) return false;
                l++; r--;
            }
            return true;
        }

        public String longestPalindrome(String s) {
            int n = s.length(), bestStart = 0, bestLen = 1;
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    if (isPalindrome(s, i, j) && j - i + 1 > bestLen) {
                        bestStart = i;
                        bestLen = j - i + 1;
                    }
                }
            }
            return s.substring(bestStart, bestStart + bestLen);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Expand Around Center
    // Time: O(n²)  |  Space: O(1)
    //
    // For each position i, expand outward for odd-length palindromes
    // (center at i) and even-length (center between i and i+1).
    // ============================================================
    static class Optimal {
        private int[] expand(String s, int l, int r) {
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
                l--; r++;
            }
            return new int[]{l + 1, r - 1};
        }

        public String longestPalindrome(String s) {
            int n = s.length(), start = 0, end = 0;
            for (int i = 0; i < n; i++) {
                int[] odd  = expand(s, i, i);
                int[] even = expand(s, i, i + 1);
                if (odd[1]  - odd[0]  > end - start) { start = odd[0];  end = odd[1];  }
                if (even[1] - even[0] > end - start) { start = even[0]; end = even[1]; }
            }
            return s.substring(start, end + 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Manacher's Algorithm
    // Time: O(n)  |  Space: O(n)
    //
    // Transform "abc" -> "^#a#b#c#$". Build radius array P[] where
    // P[i] = palindrome radius in transformed string. Use previously
    // computed palindromes to avoid re-expansion.
    // ============================================================
    static class Best {
        public String longestPalindrome(String s) {
            StringBuilder sb = new StringBuilder("^");
            for (char c : s.toCharArray()) sb.append('#').append(c);
            sb.append("#$");
            String t = sb.toString();
            int n = t.length();
            int[] p = new int[n];
            int center = 0, right = 0;

            for (int i = 1; i < n - 1; i++) {
                int mirror = 2 * center - i;
                if (i < right) p[i] = Math.min(right - i, p[mirror]);
                while (t.charAt(i + p[i] + 1) == t.charAt(i - p[i] - 1)) p[i]++;
                if (i + p[i] > right) { center = i; right = i + p[i]; }
            }

            int maxLen = 0, ci = 0;
            for (int i = 1; i < n - 1; i++) {
                if (p[i] > maxLen) { maxLen = p[i]; ci = i; }
            }
            int start = (ci - maxLen) / 2;
            return s.substring(start, start + maxLen);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Longest Palindromic Substring ===\n");

        String[] tests   = {"babad", "cbbd", "a", "racecar", "abacaba"};
        int[]    expLens = {3,       2,      1,   7,         7       };

        for (int t = 0; t < tests.length; t++) {
            String s  = tests[t];
            String b  = new BruteForce().longestPalindrome(s);
            String o  = new Optimal().longestPalindrome(s);
            String bt = new Best().longestPalindrome(s);
            String st = (b.length() == expLens[t] && o.length() == expLens[t]
                         && bt.length() == expLens[t]) ? "OK" : "MISMATCH";
            System.out.printf("Input=\"%s\"  ExpLen=%d  Brute=\"%s\"  Optimal=\"%s\"  Best=\"%s\"  [%s]%n",
                    s, expLens[t], b, o, bt, st);
        }
    }
}
