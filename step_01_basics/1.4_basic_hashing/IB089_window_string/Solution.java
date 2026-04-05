/**
 * Problem: Window String (Minimum Window Substring)
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given strings S and T, find the minimum window in S that contains all
 * characters of T (including duplicates). Return "" if no such window exists.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2 * |T|)  |  Space: O(|T|)
    // ============================================================
    /**
     * Try all O(n^2) substrings of S; for each, check if it contains all of T's chars.
     * Real-life: Finding shortest news excerpt that mentions all required keywords.
     */
    public static String bruteForce(String S, String T) {
        if (S == null || T == null || S.length() < T.length()) return "";
        int n = S.length();
        int minLen = Integer.MAX_VALUE;
        int minStart = -1;

        Map<Character, Integer> need = new HashMap<>();
        for (char c : T.toCharArray()) need.merge(c, 1, Integer::sum);

        for (int i = 0; i < n; i++) {
            Map<Character, Integer> window = new HashMap<>();
            for (int j = i; j < n; j++) {
                window.merge(S.charAt(j), 1, Integer::sum);
                if (containsAll(window, need)) {
                    if (j - i + 1 < minLen) {
                        minLen = j - i + 1;
                        minStart = i;
                    }
                    break; // expanding further won't shrink the window starting at i
                }
            }
        }
        return minStart == -1 ? "" : S.substring(minStart, minStart + minLen);
    }

    private static boolean containsAll(Map<Character, Integer> window, Map<Character, Integer> need) {
        for (Map.Entry<Character, Integer> e : need.entrySet()) {
            if (window.getOrDefault(e.getKey(), 0) < e.getValue()) return false;
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n + |T|)  |  Space: O(|T|)
    // ============================================================
    /**
     * Two-pointer sliding window with a "formed" counter.
     * Expand right until window is valid, then shrink from left to minimize.
     * Real-life: Search engines finding the smallest document segment covering all query terms.
     */
    public static String optimal(String S, String T) {
        if (S == null || T == null || S.length() < T.length()) return "";

        Map<Character, Integer> need = new HashMap<>();
        for (char c : T.toCharArray()) need.merge(c, 1, Integer::sum);

        int required = need.size(); // distinct chars that must be satisfied
        int formed = 0;
        Map<Character, Integer> window = new HashMap<>();

        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int minLeft = 0;

        while (right < S.length()) {
            char c = S.charAt(right);
            window.merge(c, 1, Integer::sum);
            if (need.containsKey(c) && window.get(c).equals(need.get(c))) formed++;

            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minLeft = left;
                }
                char lc = S.charAt(left);
                window.merge(lc, -1, Integer::sum);
                if (need.containsKey(lc) && window.get(lc) < need.get(lc)) formed--;
                left++;
            }
            right++;
        }
        return minLen == Integer.MAX_VALUE ? "" : S.substring(minLeft, minLeft + minLen);
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n + |T|)  |  Space: O(1) [ASCII charset = 128 chars]
    // ============================================================
    /**
     * Same two-pointer approach but uses int[128] arrays instead of HashMaps
     * for O(1) lookups and lower constant overhead.
     * Real-life: High-performance grep-like tools that process gigabytes of text.
     */
    public static String best(String S, String T) {
        if (S == null || T == null || S.length() < T.length()) return "";

        int[] need   = new int[128];
        int[] window = new int[128];
        int required = 0;

        for (char c : T.toCharArray()) {
            if (need[c] == 0) required++;
            need[c]++;
        }

        int formed = 0;
        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE, minLeft = 0;

        while (right < S.length()) {
            char c = S.charAt(right);
            window[c]++;
            if (need[c] > 0 && window[c] == need[c]) formed++;

            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minLeft = left;
                }
                char lc = S.charAt(left);
                if (need[lc] > 0 && window[lc] == need[lc]) formed--;
                window[lc]--;
                left++;
            }
            right++;
        }
        return minLen == Integer.MAX_VALUE ? "" : S.substring(minLeft, minLeft + minLen);
    }

    public static void main(String[] args) {
        System.out.println("=== Window String ===");

        System.out.println("\nS=ADOBECODEBANC, T=ABC");
        System.out.println("Expected: BANC");
        System.out.println("Brute:   " + bruteForce("ADOBECODEBANC", "ABC"));
        System.out.println("Optimal: " + optimal("ADOBECODEBANC", "ABC"));
        System.out.println("Best:    " + best("ADOBECODEBANC", "ABC"));

        System.out.println("\nS=a, T=a");
        System.out.println("Expected: a");
        System.out.println("Brute:   " + bruteForce("a", "a"));
        System.out.println("Optimal: " + optimal("a", "a"));
        System.out.println("Best:    " + best("a", "a"));

        System.out.println("\nS=a, T=aa");
        System.out.println("Expected: (empty)");
        System.out.println("Brute:   '" + bruteForce("a", "aa") + "'");
        System.out.println("Optimal: '" + optimal("a", "aa") + "'");
        System.out.println("Best:    '" + best("a", "aa") + "'");
    }
}
