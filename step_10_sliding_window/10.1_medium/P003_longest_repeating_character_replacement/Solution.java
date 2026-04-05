/**
 * Problem: Longest Repeating Character Replacement (LeetCode #424)
 * Difficulty: MEDIUM | XP: 25
 *
 * Replace at most k characters to get the longest repeating substring.
 * Key insight: window is valid when (windowLen - maxFreq) <= k.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Check All Substrings)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int characterReplacement(String s, int k) {
        int n = s.length();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int[] freq = new int[26];
            int maxFreq = 0;
            for (int j = i; j < n; j++) {
                freq[s.charAt(j) - 'A']++;
                maxFreq = Math.max(maxFreq, freq[s.charAt(j) - 'A']);
                int windowLen = j - i + 1;
                if (windowLen - maxFreq <= k) {
                    ans = Math.max(ans, windowLen);
                }
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 2: Optimal (Sliding Window -- Shrinking)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int characterReplacement(String s, int k) {
        int n = s.length();
        int[] freq = new int[26];
        int maxFreq = 0, left = 0, ans = 0;

        for (int right = 0; right < n; right++) {
            freq[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, freq[s.charAt(right) - 'A']);

            // Shrink window until valid
            while ((right - left + 1) - maxFreq > k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Sliding Window -- Non-Shrinking)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int characterReplacement(String s, int k) {
        int n = s.length();
        int[] freq = new int[26];
        int maxFreq = 0, left = 0;

        for (int right = 0; right < n; right++) {
            freq[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, freq[s.charAt(right) - 'A']);

            // If invalid, slide window (don't shrink -- just move left by 1)
            if ((right - left + 1) - maxFreq > k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            }
        }
        // Window size never shrinks, so final size = n - left
        return n - left;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Repeating Character Replacement ===\n");

        String[] strings = {"ABAB", "AABABBA", "AAAA", "ABCDE", "A", "ABBB"};
        int[] ks =         {2,      1,         2,      0,       1,   2};
        int[] expected =   {4,      4,         4,      1,       1,   4};

        for (int t = 0; t < strings.length; t++) {
            String s = strings[t];
            int k = ks[t];
            int b = BruteForce.characterReplacement(s, k);
            int o = Optimal.characterReplacement(s, k);
            int r = Best.characterReplacement(s, k);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    s=\"" + s + "\", k=" + k);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + r);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
