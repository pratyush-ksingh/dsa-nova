/**
 * Problem: Remove Consecutive Characters
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Remove all consecutive duplicate characters from a string.
 * "aabccba" -> "abcba"
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Repeated Scan -- though one pass suffices)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    public static String removeConsecutive(String s) {
        if (s.length() <= 1) return s;

        StringBuilder result = new StringBuilder();
        result.append(s.charAt(0));

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) != s.charAt(i - 1)) {
                result.append(s.charAt(i));
            }
        }
        return result.toString();
    }
}

// ============================================================
// Approach 2: Optimal (Single Pass with StringBuilder)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static String removeConsecutive(String s) {
        if (s.length() <= 1) return s;

        StringBuilder result = new StringBuilder();
        result.append(s.charAt(0));

        for (int i = 1; i < s.length(); i++) {
            // Compare with last character in result
            if (s.charAt(i) != result.charAt(result.length() - 1)) {
                result.append(s.charAt(i));
            }
        }
        return result.toString();
    }
}

// ============================================================
// Approach 3: Best (In-Place with Write Pointer on char array)
// Time: O(n) | Space: O(n) for char array
// ============================================================
class Best {
    public static String removeConsecutive(String s) {
        if (s.length() <= 1) return s;

        char[] arr = s.toCharArray();
        int write = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[write]) {
                write++;
                arr[write] = arr[i];
            }
        }

        return new String(arr, 0, write + 1);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Remove Consecutive Characters ===\n");

        String[] testCases = {
            "aabccba",
            "aab",
            "abcd",
            "aaaa",
            "aabbcc",
            "a",
            "ababab",
            "aabc",
            "abcc"
        };
        String[] expected = {
            "abcba",
            "ab",
            "abcd",
            "a",
            "abc",
            "a",
            "ababab",
            "abc",
            "abc"
        };

        for (int t = 0; t < testCases.length; t++) {
            String bruteResult = BruteForce.removeConsecutive(testCases[t]);
            String optimalResult = Optimal.removeConsecutive(testCases[t]);
            String bestResult = Best.removeConsecutive(testCases[t]);

            boolean pass = bruteResult.equals(expected[t])
                        && optimalResult.equals(expected[t])
                        && bestResult.equals(expected[t]);

            System.out.println("Input:    \"" + testCases[t] + "\"");
            System.out.println("Brute:    \"" + bruteResult + "\"");
            System.out.println("Optimal:  \"" + optimalResult + "\"");
            System.out.println("Best:     \"" + bestResult + "\"");
            System.out.println("Expected: \"" + expected[t] + "\"");
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
