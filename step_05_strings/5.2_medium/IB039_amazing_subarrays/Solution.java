/**
 * Problem: Amazing Subarrays
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Key Insight: A vowel at index i starts exactly (n - i) substrings.
 *
 * @author DSA_Nova
 */
public class Solution {

    private static final int MOD = 10007;

    private static boolean isVowel(char c) {
        c = Character.toLowerCase(c);
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Enumerate All Substrings
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    public static int bruteForce(String s) {
        int n = s.length();
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (isVowel(s.charAt(i))) {
                // Every substring starting at i (length 1 to n-i) is amazing
                for (int j = i; j < n; j++) {
                    count = (count + 1) % MOD;
                }
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Single Pass Math
    // Time: O(n)  |  Space: O(1)
    //
    // If s[i] is a vowel, it contributes (n - i) amazing substrings.
    // ============================================================
    public static int optimal(String s) {
        int n = s.length();
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (isVowel(s.charAt(i))) {
                count = (count + (n - i)) % MOD;
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (already O(n) / O(1))
    // Time: O(n)  |  Space: O(1)
    //
    // Uses a boolean lookup array for slightly faster vowel checks.
    // ============================================================
    public static int best(String s) {
        boolean[] vowelMap = new boolean[128];
        for (char c : "aeiouAEIOU".toCharArray()) {
            vowelMap[c] = true;
        }

        int n = s.length();
        int count = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < 128 && vowelMap[c]) {
                count = (count + (n - i)) % MOD;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Amazing Subarrays ===");

        String[] tests = {"ABEC", "AAA", "BCD", ""};
        for (String t : tests) {
            System.out.printf("Input: \"%s\"%n", t);
            System.out.printf("  Brute:   %d%n", bruteForce(t));
            System.out.printf("  Optimal: %d%n", optimal(t));
            System.out.printf("  Best:    %d%n", best(t));
            System.out.println();
        }
    }
}
