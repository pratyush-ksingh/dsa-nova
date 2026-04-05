/**
 * Problem: Rotate String (LeetCode #796)
 * Difficulty: EASY | XP: 10
 *
 * Given two strings s and goal, return true if s can become
 * goal after some number of left rotations.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Try All Rotations)
// Time: O(n^2) | Space: O(n)
// ============================================================
class BruteForce {
    public static boolean rotateString(String s, String goal) {
        if (s.length() != goal.length()) return false;
        int n = s.length();

        for (int i = 0; i < n; i++) {
            // Build rotation: s[i..n-1] + s[0..i-1]
            String rotated = s.substring(i) + s.substring(0, i);
            if (rotated.equals(goal)) {
                return true;
            }
        }
        return false;
    }
}

// ============================================================
// Approach 2: Optimal (Concatenation Trick: s + s)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static boolean rotateString(String s, String goal) {
        if (s.length() != goal.length()) return false;
        String doubled = s + s;
        return doubled.contains(goal);
    }
}

// ============================================================
// Approach 3: Best (KMP-style on circular text)
// Time: O(n) | Space: O(n) for failure array
// ============================================================
class Best {
    public static boolean rotateString(String s, String goal) {
        if (s.length() != goal.length()) return false;
        int n = s.length();
        if (n == 0) return true;

        // Build KMP failure function for goal
        int[] fail = new int[n];
        fail[0] = 0;
        int k = 0;
        for (int i = 1; i < n; i++) {
            while (k > 0 && goal.charAt(k) != goal.charAt(i)) {
                k = fail[k - 1];
            }
            if (goal.charAt(k) == goal.charAt(i)) {
                k++;
            }
            fail[i] = k;
        }

        // Run KMP on s treated as circular (2n characters)
        int j = 0;
        for (int i = 0; i < 2 * n; i++) {
            char c = s.charAt(i % n);
            while (j > 0 && goal.charAt(j) != c) {
                j = fail[j - 1];
            }
            if (goal.charAt(j) == c) {
                j++;
            }
            if (j == n) {
                return true;
            }
        }
        return false;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Rotate String ===\n");

        String[][] testCases = {
            {"abcde", "cdeab"},
            {"abcde", "abced"},
            {"a", "a"},
            {"aa", "aa"},
            {"abc", "abc"},
            {"abc", "bac"},
            {"ab", "ba"}
        };
        boolean[] expected = {true, false, true, true, true, false, true};

        for (int t = 0; t < testCases.length; t++) {
            String s = testCases[t][0];
            String goal = testCases[t][1];

            boolean bruteResult = BruteForce.rotateString(s, goal);
            boolean optimalResult = Optimal.rotateString(s, goal);
            boolean bestResult = Best.rotateString(s, goal);

            boolean pass = bruteResult == expected[t]
                        && optimalResult == expected[t]
                        && bestResult == expected[t];

            System.out.println("Input:    s=\"" + s + "\", goal=\"" + goal + "\"");
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Best:     " + bestResult);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
