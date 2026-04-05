/**
 * Problem: Longest Substring with At Most K Distinct Characters
 * LeetCode 340 | Difficulty: HARD | XP: 50
 *
 * Key Insight: Sliding window — expand right freely, shrink left whenever
 *              the number of distinct characters in the window exceeds k.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(k)
    // ============================================================
    static class BruteForce {
        /**
         * Try every starting index i. Extend j rightward, track distinct
         * chars in a Set. Break once distinct > k. Update best length.
         */
        public int lengthOfLongestSubstringKDistinct(String s, int k) {
            if (k == 0 || s.isEmpty()) return 0;
            int n = s.length(), best = 0;
            for (int i = 0; i < n; i++) {
                Set<Character> seen = new HashSet<>();
                for (int j = i; j < n; j++) {
                    seen.add(s.charAt(j));
                    if (seen.size() > k) break;
                    best = Math.max(best, j - i + 1);
                }
            }
            return best;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Sliding Window + HashMap)
    // Time: O(n)  |  Space: O(k)
    // ============================================================
    static class Optimal {
        /**
         * Maintain a frequency map of characters in window [left, right].
         * When map.size() > k, shrink from the left until back to k.
         */
        public int lengthOfLongestSubstringKDistinct(String s, int k) {
            if (k == 0 || s.isEmpty()) return 0;
            Map<Character, Integer> freq = new HashMap<>();
            int left = 0, best = 0;
            for (int right = 0; right < s.length(); right++) {
                char ch = s.charAt(right);
                freq.merge(ch, 1, Integer::sum);
                while (freq.size() > k) {
                    char lc = s.charAt(left++);
                    freq.merge(lc, -1, Integer::sum);
                    if (freq.get(lc) == 0) freq.remove(lc);
                }
                best = Math.max(best, right - left + 1);
            }
            return best;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Sliding Window + LinkedHashMap for O(1) shrink)
    // Time: O(n)  |  Space: O(k)
    // ============================================================
    static class Best {
        /**
         * Use a LinkedHashMap to remember the most-recent index of each
         * character. When window has k+1 distinct chars, the LHM's first
         * entry gives the leftmost character's last occurrence — we jump
         * left directly without scanning character by character.
         * Shrink cost is O(1) amortised instead of O(window_size) worst-case.
         */
        public int lengthOfLongestSubstringKDistinct(String s, int k) {
            if (k == 0 || s.isEmpty()) return 0;
            LinkedHashMap<Character, Integer> lastSeen = new LinkedHashMap<>();
            int left = 0, best = 0;
            for (int right = 0; right < s.length(); right++) {
                char ch = s.charAt(right);
                lastSeen.remove(ch);          // refresh insertion order
                lastSeen.put(ch, right);

                if (lastSeen.size() > k) {
                    Map.Entry<Character, Integer> oldest =
                        lastSeen.entrySet().iterator().next();
                    lastSeen.remove(oldest.getKey());
                    left = oldest.getValue() + 1;
                }
                best = Math.max(best, right - left + 1);
            }
            return best;
        }
    }

    public static void main(String[] args) {
        String[] strs = {"eceba", "aa", "abcadcacacaca", "", "abc"};
        int[]    ks   = {2,       1,    3,               2,  0};
        int[]    exps = {3,       2,    11,               0,  0};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== Longest Substring with At Most K Distinct ===");
        for (int t = 0; t < strs.length; t++) {
            int b  = bf.lengthOfLongestSubstringKDistinct(strs[t], ks[t]);
            int o  = opt.lengthOfLongestSubstringKDistinct(strs[t], ks[t]);
            int be = bst.lengthOfLongestSubstringKDistinct(strs[t], ks[t]);
            String status = (b == o && o == be && be == exps[t]) ? "OK" : "FAIL";
            System.out.printf("  s=%-20s k=%d => brute=%d opt=%d best=%d (exp %d) [%s]%n",
                              strs[t], ks[t], b, o, be, exps[t], status);
        }
    }
}
