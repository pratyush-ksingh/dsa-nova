/**
 * Problem: First Non-Repeating Character in Stream
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a stream of characters, after each character arrives,
 * print the first non-repeating character seen so far.
 * Print '#' if no such character exists.
 * Real-life use: Real-time analytics, log deduplication, stream processing.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // For each new character, scan all characters seen so far to find first non-repeating.
    // Time: O(N^2)  |  Space: O(N)
    // ============================================================
    static class BruteForce {
        public static String processStream(String stream) {
            StringBuilder result = new StringBuilder();
            List<Character> seen = new ArrayList<>();
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : stream.toCharArray()) {
                seen.add(c);
                freq.merge(c, 1, Integer::sum);
                char ans = '#';
                for (char ch : seen) {
                    if (freq.get(ch) == 1) { ans = ch; break; }
                }
                result.append(ans);
            }
            return result.toString();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Queue + frequency map.
    // Queue holds order of first appearances.
    // After each char, peek front; if freq > 1, pop and repeat.
    // Time: O(N * 26) = O(N)  |  Space: O(26) = O(1)
    // ============================================================
    static class Optimal {
        public static String processStream(String stream) {
            StringBuilder result = new StringBuilder();
            int[] freq = new int[26];
            Queue<Character> queue = new LinkedList<>();
            for (char c : stream.toCharArray()) {
                freq[c - 'a']++;
                queue.offer(c);
                // Remove from front while front char is repeating
                while (!queue.isEmpty() && freq[queue.peek() - 'a'] > 1) {
                    queue.poll();
                }
                result.append(queue.isEmpty() ? '#' : queue.peek());
            }
            return result.toString();
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // LinkedHashMap preserves insertion order; removal of repeating chars is O(1).
    // After each char: if freq becomes 2+, remove from map entirely.
    // First key in map = first non-repeating.
    // Time: O(N)  |  Space: O(26) = O(1)
    // ============================================================
    static class Best {
        public static String processStream(String stream) {
            StringBuilder result = new StringBuilder();
            // LinkedHashMap: maintains insertion order
            Map<Character, Integer> map = new LinkedHashMap<>();
            for (char c : stream.toCharArray()) {
                map.put(c, map.getOrDefault(c, 0) + 1);
                if (map.get(c) > 1) map.remove(c); // no longer non-repeating
                result.append(map.isEmpty() ? '#' : map.keySet().iterator().next());
            }
            return result.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== First Non-Repeating Character in Stream ===");

        String[] streams = {"aabc", "aabcc", "geeksforgeeks", "a"};
        for (String s : streams) {
            System.out.printf("%nstream=\"%s\"%n", s);
            System.out.println("  Brute  : " + BruteForce.processStream(s));
            System.out.println("  Optimal: " + Optimal.processStream(s));
            System.out.println("  Best   : " + Best.processStream(s));
        }
    }
}
