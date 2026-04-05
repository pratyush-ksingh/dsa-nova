/**
 * Problem: Scramble String
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given strings s1 and s2, determine if s2 is a scrambled version of s1.
 * A scrambled string is obtained by recursively swapping children in the
 * binary tree representation of the string.
 *
 * DP recurrence:
 * isScramble(s1, s2) = true if:
 *   For some split i:
 *     (isScramble(s1[0..i], s2[0..i]) && isScramble(s1[i+1..], s2[i+1..]))
 *     OR
 *     (isScramble(s1[0..i], s2[n-i..n]) && isScramble(s1[i+1..], s2[0..n-i-1]))
 *
 * Real-life use: String obfuscation, tree-based encoding, pattern matching.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(N^4 * 5^N) without memo  |  Space: O(N^2 * memo depth)
    // Pure recursion without memoization.
    // ============================================================
    public static boolean bruteForce(String s1, String s2) {
        if (s1.equals(s2)) return true;
        if (s1.length() != s2.length()) return false;
        int n = s1.length();

        // Pruning: character counts must match
        int[] count = new int[26];
        for (int i = 0; i < n; i++) {
            count[s1.charAt(i) - 'a']++;
            count[s2.charAt(i) - 'a']--;
        }
        for (int c : count) if (c != 0) return false;

        for (int i = 1; i < n; i++) {
            // No swap
            if (bruteForce(s1.substring(0, i), s2.substring(0, i)) &&
                bruteForce(s1.substring(i), s2.substring(i)))
                return true;
            // Swap
            if (bruteForce(s1.substring(0, i), s2.substring(n - i)) &&
                bruteForce(s1.substring(i), s2.substring(0, n - i)))
                return true;
        }
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(N^4)  |  Space: O(N^3) for memoization
    // Memoized recursion. Key = (s1, s2) or (i, j, len).
    // ============================================================
    public static boolean optimal(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        Map<String, Boolean> memo = new HashMap<>();
        return isScramble(s1, s2, memo);
    }

    private static boolean isScramble(String s1, String s2, Map<String, Boolean> memo) {
        if (s1.equals(s2)) return true;
        String key = s1 + "#" + s2;
        if (memo.containsKey(key)) return memo.get(key);

        int n = s1.length();
        int[] cnt = new int[26];
        for (int i = 0; i < n; i++) {
            cnt[s1.charAt(i) - 'a']++;
            cnt[s2.charAt(i) - 'a']--;
        }
        for (int c : cnt) {
            if (c != 0) {
                memo.put(key, false);
                return false;
            }
        }

        for (int i = 1; i < n; i++) {
            if (isScramble(s1.substring(0, i), s2.substring(0, i), memo) &&
                isScramble(s1.substring(i), s2.substring(i), memo)) {
                memo.put(key, true);
                return true;
            }
            if (isScramble(s1.substring(0, i), s2.substring(n - i), memo) &&
                isScramble(s1.substring(i), s2.substring(0, n - i), memo)) {
                memo.put(key, true);
                return true;
            }
        }
        memo.put(key, false);
        return false;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(N^4)  |  Space: O(N^3)
    // Bottom-up 3D DP. dp[len][i][j] = is s1[i..i+len-1] a scramble
    // of s2[j..j+len-1]?
    // ============================================================
    public static boolean best(String s1, String s2) {
        int n = s1.length();
        if (n != s2.length()) return false;
        if (s1.equals(s2)) return true;

        // dp[len][i][j]: is s1[i..i+len-1] scramble of s2[j..j+len-1]
        boolean[][][] dp = new boolean[n + 1][n][n];

        // Base case: length 1
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                dp[1][i][j] = (s1.charAt(i) == s2.charAt(j));

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                for (int j = 0; j <= n - len; j++) {
                    for (int k = 1; k < len; k++) {
                        // No swap: s1[i..i+k-1] <-> s2[j..j+k-1]
                        //          s1[i+k..i+len-1] <-> s2[j+k..j+len-1]
                        if (dp[k][i][j] && dp[len - k][i + k][j + k]) {
                            dp[len][i][j] = true;
                            break;
                        }
                        // Swap: s1[i..i+k-1] <-> s2[j+len-k..j+len-1]
                        //       s1[i+k..i+len-1] <-> s2[j..j+len-k-1]
                        if (dp[k][i][j + len - k] && dp[len - k][i + k][j]) {
                            dp[len][i][j] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[n][0][0];
    }

    public static void main(String[] args) {
        System.out.println("=== Scramble String ===\n");

        // "rgeat" is a scramble of "great"
        System.out.println("Test 1: s1=\"great\", s2=\"rgeat\" (expected true)");
        System.out.println("  Brute:   " + bruteForce("great", "rgeat"));
        System.out.println("  Optimal: " + optimal("great", "rgeat"));
        System.out.println("  Best:    " + best("great", "rgeat"));

        // "abcde" is not a scramble of "caebd"
        System.out.println("\nTest 2: s1=\"abcde\", s2=\"caebd\" (expected false)");
        System.out.println("  Best: " + best("abcde", "caebd"));

        // Same string
        System.out.println("\nTest 3: s1=\"abc\", s2=\"abc\" (expected true)");
        System.out.println("  Best: " + best("abc", "abc"));

        // "a" not scramble of "b"
        System.out.println("\nTest 4: s1=\"a\", s2=\"b\" (expected false)");
        System.out.println("  Best: " + best("a", "b"));

        System.out.println("\nTest 5: s1=\"ab\", s2=\"ba\" (expected true)");
        System.out.println("  Best: " + best("ab", "ba"));
    }
}
