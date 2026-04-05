/**
 * Problem: Minimum Window Substring
 * Difficulty: HARD | XP: 50
 *
 * Find the smallest window in string S that contains all characters of T
 * (including duplicates). Return "" if impossible.
 * Real-life use: Search highlighting, DNA sequence search, substring matching.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Check every substring of S; for each one, verify if it contains all of T.
    // Time: O(N^2 * |T|)  |  Space: O(|T|)
    // ============================================================
    static class BruteForce {
        public static String minWindow(String s, String t) {
            if (s.isEmpty() || t.isEmpty()) return "";
            String result = "";
            for (int i = 0; i < s.length(); i++) {
                for (int j = i + t.length(); j <= s.length(); j++) {
                    String sub = s.substring(i, j);
                    if (containsAll(sub, t)) {
                        if (result.isEmpty() || sub.length() < result.length()) {
                            result = sub;
                        }
                    }
                }
            }
            return result;
        }

        private static boolean containsAll(String window, String t) {
            int[] freq = new int[128];
            for (char c : t.toCharArray()) freq[c]++;
            for (char c : window.toCharArray()) freq[c]--;
            for (int i = 0; i < 128; i++) if (freq[i] > 0) return false;
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Sliding window with two frequency arrays and a "formed" counter.
    // Expand right until window is valid; shrink left to minimize.
    // Time: O(|S| + |T|)  |  Space: O(|S| + |T|)
    // ============================================================
    static class Optimal {
        public static String minWindow(String s, String t) {
            if (s.isEmpty() || t.isEmpty()) return "";
            int[] need = new int[128], have = new int[128];
            int required = 0;
            for (char c : t.toCharArray()) {
                if (need[c]++ == 0) required++;
            }
            int formed = 0, left = 0, minLen = Integer.MAX_VALUE, minStart = 0;
            for (int right = 0; right < s.length(); right++) {
                char rc = s.charAt(right);
                have[rc]++;
                if (need[rc] > 0 && have[rc] == need[rc]) formed++;
                while (formed == required) {
                    if (right - left + 1 < minLen) {
                        minLen = right - left + 1;
                        minStart = left;
                    }
                    char lc = s.charAt(left++);
                    have[lc]--;
                    if (need[lc] > 0 && have[lc] < need[lc]) formed--;
                }
            }
            return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Filtered sliding window: pre-filter S to only positions with characters in T.
    // Reduces window traversal when T is much smaller than S.
    // Time: O(|S| + |T|)  |  Space: O(|T|)
    // ============================================================
    static class Best {
        public static String minWindow(String s, String t) {
            if (s.isEmpty() || t.isEmpty()) return "";
            Set<Character> tSet = new HashSet<>();
            for (char c : t.toCharArray()) tSet.add(c);
            // Filtered positions: only indices in S that have chars in T
            List<int[]> filtered = new ArrayList<>(); // [char, index]
            for (int i = 0; i < s.length(); i++) {
                if (tSet.contains(s.charAt(i))) filtered.add(new int[]{s.charAt(i), i});
            }
            int[] need = new int[128], have = new int[128];
            int required = 0;
            for (char c : t.toCharArray()) {
                if (need[c]++ == 0) required++;
            }
            int formed = 0, left = 0, minLen = Integer.MAX_VALUE, minStart = 0;
            for (int right = 0; right < filtered.size(); right++) {
                char rc = (char) filtered.get(right)[0];
                have[rc]++;
                if (need[rc] > 0 && have[rc] == need[rc]) formed++;
                while (formed == required) {
                    int start = filtered.get(left)[1];
                    int end   = filtered.get(right)[1];
                    if (end - start + 1 < minLen) {
                        minLen = end - start + 1;
                        minStart = start;
                    }
                    char lc = (char) filtered.get(left)[0];
                    have[lc]--;
                    if (need[lc] > 0 && have[lc] < need[lc]) formed--;
                    left++;
                }
            }
            return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Minimum Window Substring ===");

        String[][] tests = {{"ADOBECODEBANC", "ABC"}, {"a", "a"}, {"a", "aa"}, {"aa", "aa"}};
        for (String[] t : tests) {
            System.out.printf("%nS=\"%s\"  T=\"%s\"%n", t[0], t[1]);
            System.out.println("  Brute  : \"" + BruteForce.minWindow(t[0], t[1]) + "\"");
            System.out.println("  Optimal: \"" + Optimal.minWindow(t[0], t[1]) + "\"");
            System.out.println("  Best   : \"" + Best.minWindow(t[0], t[1]) + "\"");
        }
    }
}
