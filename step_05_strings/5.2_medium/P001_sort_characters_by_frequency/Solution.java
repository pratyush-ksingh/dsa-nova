/**
 * Problem: Sort Characters by Frequency (LeetCode #451)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- HashMap + Sort
    // Time: O(n log n)  |  Space: O(n)
    //
    // Count frequencies, sort unique chars by freq descending,
    // rebuild string by repeating each char.
    // ============================================================
    static class BruteForce {
        public String frequencySort(String s) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : s.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }

            List<Character> chars = new ArrayList<>(freq.keySet());
            chars.sort((a, b) -> freq.get(b) - freq.get(a));

            StringBuilder sb = new StringBuilder();
            for (char c : chars) {
                int count = freq.get(c);
                for (int i = 0; i < count; i++) {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashMap + Bucket Sort
    // Time: O(n)  |  Space: O(n)
    //
    // Use bucket sort: bucket[i] holds chars with frequency i.
    // Iterate buckets from n down to 1 to build result.
    // ============================================================
    static class Optimal {
        public String frequencySort(String s) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : s.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }

            int n = s.length();
            @SuppressWarnings("unchecked")
            List<Character>[] buckets = new List[n + 1];
            for (int i = 0; i <= n; i++) {
                buckets[i] = new ArrayList<>();
            }

            for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
                buckets[entry.getValue()].add(entry.getKey());
            }

            StringBuilder sb = new StringBuilder();
            for (int i = n; i >= 1; i--) {
                for (char c : buckets[i]) {
                    for (int j = 0; j < i; j++) {
                        sb.append(c);
                    }
                }
            }
            return sb.toString();
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- int[128] array + Bucket Sort
    // Time: O(n)  |  Space: O(n)
    //
    // Use int array instead of HashMap for faster counting.
    // Same bucket sort, but avoids HashMap overhead.
    // ============================================================
    static class Best {
        public String frequencySort(String s) {
            int[] freq = new int[128];
            for (char c : s.toCharArray()) freq[c]++;

            int n = s.length();
            @SuppressWarnings("unchecked")
            List<Character>[] buckets = new List[n + 1];
            for (int i = 0; i <= n; i++) buckets[i] = new ArrayList<>();

            for (int c = 0; c < 128; c++) {
                if (freq[c] > 0) buckets[freq[c]].add((char) c);
            }

            StringBuilder sb = new StringBuilder();
            for (int i = n; i >= 1; i--) {
                for (char c : buckets[i]) {
                    for (int j = 0; j < i; j++) sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sort Characters by Frequency ===\n");

        String[] tests = {"tree", "cccaaa", "Aabb", "a"};
        String[] expected = {"eert", "aaaccc", "bbAa", "a"};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            System.out.println("Input:    \"" + s + "\"");
            System.out.println("Expected: \"" + expected[t] + "\" (or valid variant)");
            System.out.println("Brute:    \"" + new BruteForce().frequencySort(s) + "\"");
            System.out.println("Optimal:  \"" + new Optimal().frequencySort(s) + "\"");
            System.out.println("Best:     \"" + new Best().frequencySort(s) + "\"");
            System.out.println();
        }
    }
}
