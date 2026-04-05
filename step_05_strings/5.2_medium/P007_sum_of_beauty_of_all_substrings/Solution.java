/**
 * Problem: Sum of Beauty of All Substrings (LeetCode #1781)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Generate all substrings + HashMap
    // Time: O(n³)  |  Space: O(26)
    //
    // For every substring s[i..j], build a frequency map, find
    // max_freq and min_freq of present chars, add their difference.
    // ============================================================
    static class BruteForce {
        public int beautySum(String s) {
            int n = s.length(), total = 0;
            for (int i = 0; i < n; i++) {
                Map<Character, Integer> freq = new HashMap<>();
                for (int j = i; j < n; j++) {
                    char c = s.charAt(j);
                    freq.put(c, freq.getOrDefault(c, 0) + 1);
                    int maxF = Collections.max(freq.values());
                    int minF = Collections.min(freq.values());
                    total += maxF - minF;
                }
            }
            return total;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- O(n²) with int[26] freq array
    // Time: O(n² * 26)  |  Space: O(26)
    //
    // Fix start i, extend j rightward. Maintain int[26] freq array.
    // Recompute max/min over the 26 slots each step.
    // ============================================================
    static class Optimal {
        public int beautySum(String s) {
            int n = s.length(), total = 0;
            for (int i = 0; i < n; i++) {
                int[] freq = new int[26];
                for (int j = i; j < n; j++) {
                    freq[s.charAt(j) - 'a']++;
                    int maxF = 0, minF = Integer.MAX_VALUE;
                    for (int f : freq) {
                        if (f > 0) {
                            maxF = Math.max(maxF, f);
                            minF = Math.min(minF, f);
                        }
                    }
                    total += maxF - minF;
                }
            }
            return total;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- O(n²) with explicit max tracking
    // Time: O(n² * 26)  |  Space: O(26)
    //
    // Maintain a running max as we extend j. Only scan for min,
    // capping the min-search early once we know minF < maxF.
    // Avoids one full pass by updating max incrementally.
    // ============================================================
    static class Best {
        public int beautySum(String s) {
            int n = s.length(), total = 0;
            for (int i = 0; i < n; i++) {
                int[] freq = new int[26];
                int maxF = 0;
                for (int j = i; j < n; j++) {
                    int idx = s.charAt(j) - 'a';
                    freq[idx]++;
                    if (freq[idx] > maxF) maxF = freq[idx];
                    int minF = maxF;
                    for (int f : freq) {
                        if (f > 0 && f < minF) minF = f;
                    }
                    total += maxF - minF;
                }
            }
            return total;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sum of Beauty of All Substrings ===\n");

        String[] tests = {"aabcb", "aabcbaa", "a", "aa", "abcd"};
        int[]    exp   = {5,       17,         0,   0,    0    };

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            int b  = new BruteForce().beautySum(s);
            int o  = new Optimal().beautySum(s);
            int bt = new Best().beautySum(s);
            String st = (b == exp[t] && o == exp[t] && bt == exp[t]) ? "OK" : "MISMATCH";
            System.out.printf("s=\"%s\"  Expected=%d  Brute=%d  Optimal=%d  Best=%d  [%s]%n",
                    s, exp[t], b, o, bt, st);
        }
    }
}
