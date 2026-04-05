/**
 * Problem: Count Number of Substrings with Exactly K Distinct Characters
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check All Substrings
    // Time: O(n³)  |  Space: O(k)
    //
    // Generate every substring. Use a HashSet to count distinct
    // characters. Increment counter when set size equals k.
    // ============================================================
    static class BruteForce {
        public int countSubstrings(String s, int k) {
            int n = s.length(), count = 0;
            for (int i = 0; i < n; i++) {
                Set<Character> seen = new HashSet<>();
                for (int j = i; j < n; j++) {
                    seen.add(s.charAt(j));
                    if (seen.size() == k) count++;
                    else if (seen.size() > k) break;
                }
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- atMost(k) - atMost(k-1) Sliding Window
    // Time: O(n)  |  Space: O(k)
    //
    // exactly(k) = atMost(k) - atMost(k-1).
    // atMost(k) counts substrings with at most k distinct chars
    // via a two-pointer sliding window.
    // ============================================================
    static class Optimal {
        private int atMost(String s, int k) {
            Map<Character, Integer> freq = new HashMap<>();
            int left = 0, count = 0;
            for (int right = 0; right < s.length(); right++) {
                char rc = s.charAt(right);
                freq.put(rc, freq.getOrDefault(rc, 0) + 1);
                while (freq.size() > k) {
                    char lc = s.charAt(left++);
                    freq.put(lc, freq.get(lc) - 1);
                    if (freq.get(lc) == 0) freq.remove(lc);
                }
                count += right - left + 1;
            }
            return count;
        }

        public int countSubstrings(String s, int k) {
            return atMost(s, k) - atMost(s, k - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same sliding window with int[26] array
    // Time: O(n)  |  Space: O(1)
    //
    // Replace HashMap with a fixed int[26] array and an explicit
    // distinct counter to eliminate hashing overhead entirely.
    // ============================================================
    static class Best {
        private int atMost(String s, int k) {
            int[] freq = new int[26];
            int distinct = 0, left = 0, count = 0;
            for (int right = 0; right < s.length(); right++) {
                int ri = s.charAt(right) - 'a';
                if (freq[ri] == 0) distinct++;
                freq[ri]++;
                while (distinct > k) {
                    int li = s.charAt(left++) - 'a';
                    freq[li]--;
                    if (freq[li] == 0) distinct--;
                }
                count += right - left + 1;
            }
            return count;
        }

        public int countSubstrings(String s, int k) {
            return atMost(s, k) - atMost(s, k - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Count Number of Substrings with Exactly K Distinct Chars ===\n");

        String[] strings = {"abc", "aba", "aa",  "aabab"};
        int[]    ks      = {2,     2,     1,     2     };
        int[]    exp     = {2,     3,     3,     9     };

        for (int t = 0; t < strings.length; t++) {
            String s = strings[t]; int k = ks[t];
            int b  = new BruteForce().countSubstrings(s, k);
            int o  = new Optimal().countSubstrings(s, k);
            int bt = new Best().countSubstrings(s, k);
            String st = (b == exp[t] && o == exp[t] && bt == exp[t]) ? "OK" : "MISMATCH";
            System.out.printf("s=\"%s\" k=%d  Expected=%d  Brute=%d  Optimal=%d  Best=%d  [%s]%n",
                    s, k, exp[t], b, o, bt, st);
        }
    }
}
