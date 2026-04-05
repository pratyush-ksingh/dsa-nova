/**
 * Problem: Longest Substring Without Repeating Characters (LeetCode #3)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find the length of the longest substring without repeating characters.
 *
 * @author DSA_Nova
 */
import java.util.HashMap;
import java.util.HashSet;

// ============================================================
// Approach 1: Brute Force (Check All Substrings)
// Time: O(n^3) | Space: O(min(n, 128))
// ============================================================
class BruteForce {
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            HashSet<Character> seen = new HashSet<>();
            for (int j = i; j < n; j++) {
                if (seen.contains(s.charAt(j))) break;
                seen.add(s.charAt(j));
                maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }
}

// ============================================================
// Approach 2: Sliding Window with Hash Set
// Time: O(2n) = O(n) | Space: O(min(n, 128))
// ============================================================
class SlidingWindowSet {
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int left = 0, maxLen = 0;
        HashSet<Character> window = new HashSet<>();

        for (int right = 0; right < n; right++) {
            while (window.contains(s.charAt(right))) {
                window.remove(s.charAt(left));
                left++;
            }
            window.add(s.charAt(right));
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}

// ============================================================
// Approach 3: Best (Sliding Window with Hash Map -- Jump)
// Time: O(n) | Space: O(min(n, 128))
// ============================================================
class Optimal {
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int left = 0, maxLen = 0;
        HashMap<Character, Integer> lastSeen = new HashMap<>();

        for (int right = 0; right < n; right++) {
            char c = s.charAt(right);
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                left = lastSeen.get(c) + 1;
            }
            lastSeen.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Substring Without Repeating Characters ===\n");

        String[] inputs = {"abcabcbb", "bbbbb", "pwwkew", "", "a", "abcdef", "dvdf"};
        int[] expected =  {3,          1,       3,       0,  1,   6,        3};

        boolean allPass = true;
        for (int t = 0; t < inputs.length; t++) {
            int b = BruteForce.lengthOfLongestSubstring(inputs[t]);
            int s = SlidingWindowSet.lengthOfLongestSubstring(inputs[t]);
            int o = Optimal.lengthOfLongestSubstring(inputs[t]);

            boolean pass = b == expected[t] && s == expected[t] && o == expected[t];
            allPass &= pass;

            System.out.printf("Input: %-12s | Brute=%d Set=%d Map=%d | Expected=%d [%s]%n",
                "\"" + inputs[t] + "\"", b, s, o, expected[t], pass ? "PASS" : "FAIL");
        }
        System.out.println("\nAll pass: " + allPass);
    }
}
