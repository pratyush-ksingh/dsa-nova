/**
 * Problem: Check Anagram (LeetCode #242)
 * Difficulty: EASY | XP: 10
 *
 * Given two strings s and t, return true if t is an anagram of s.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Sort Both Strings)
// Time: O(n log n) | Space: O(n)
// ============================================================
class BruteForce {
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        Arrays.sort(sArr);
        Arrays.sort(tArr);

        return Arrays.equals(sArr, tArr);
    }
}

// ============================================================
// Approach 2: Optimal (Two Frequency Arrays)
// Time: O(n) | Space: O(1) -- fixed 26-element arrays
// ============================================================
class Optimal {
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] freqS = new int[26];
        int[] freqT = new int[26];

        for (int i = 0; i < s.length(); i++) {
            freqS[s.charAt(i) - 'a']++;
            freqT[t.charAt(i) - 'a']++;
        }

        return Arrays.equals(freqS, freqT);
    }
}

// ============================================================
// Approach 3: Best (Single Frequency Array -- increment/decrement)
// Time: O(n) | Space: O(1) -- single 26-element array
// ============================================================
class Best {
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] freq = new int[26];

        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
            freq[t.charAt(i) - 'a']--;
        }

        for (int count : freq) {
            if (count != 0) return false;
        }
        return true;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Check Anagram ===\n");

        String[][] testCases = {
            {"anagram", "nagaram"},
            {"rat", "car"},
            {"a", "a"},
            {"ab", "ba"},
            {"abc", "abcd"},
            {"aab", "abb"},
            {"aaa", "aaa"}
        };
        boolean[] expected = {true, false, true, true, false, false, true};

        for (int t = 0; t < testCases.length; t++) {
            String s = testCases[t][0];
            String goal = testCases[t][1];

            boolean bruteResult = BruteForce.isAnagram(s, goal);
            boolean optimalResult = Optimal.isAnagram(s, goal);
            boolean bestResult = Best.isAnagram(s, goal);

            boolean pass = bruteResult == expected[t]
                        && optimalResult == expected[t]
                        && bestResult == expected[t];

            System.out.println("Input:    s=\"" + s + "\", t=\"" + goal + "\"");
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Best:     " + bestResult);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
