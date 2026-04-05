import java.util.*;

/**
 * Problem: Longest String Chain (LeetCode #1048)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sort + Pairwise Check DP
    // Time: O(n^2 * L)  |  Space: O(n)
    // Sort by length, for each pair check predecessor relationship.
    // ============================================================
    public static int bruteForce(String[] words) {
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        int n = words.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int ans = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (isPredecessor(words[j], words[i])) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(ans, dp[i]);
        }

        return ans;
    }

    private static boolean isPredecessor(String s, String l) {
        if (l.length() != s.length() + 1) return false;
        int i = 0, j = 0;
        boolean skipped = false;
        while (i < s.length() && j < l.length()) {
            if (s.charAt(i) == l.charAt(j)) {
                i++; j++;
            } else if (!skipped) {
                skipped = true;
                j++;
            } else {
                return false;
            }
        }
        return true;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sort + HashMap DP
    // Time: O(n * L^2)  |  Space: O(n * L)
    // For each word, remove each char and check if predecessor exists.
    // ============================================================
    public static int optimal(String[] words) {
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        Map<String, Integer> dp = new HashMap<>();
        int ans = 1;

        for (String word : words) {
            int best = 1;
            for (int i = 0; i < word.length(); i++) {
                String pred = word.substring(0, i) + word.substring(i + 1);
                if (dp.containsKey(pred)) {
                    best = Math.max(best, dp.get(pred) + 1);
                }
            }
            dp.put(word, best);
            ans = Math.max(ans, best);
        }

        return ans;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same HashMap Approach (Optimal for this problem)
    // Time: O(n * L^2)  |  Space: O(n * L)
    // ============================================================
    public static int best(String[] words) {
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        Map<String, Integer> dp = new HashMap<>();
        int ans = 1;

        for (String word : words) {
            int bestChain = 1;
            for (int i = 0; i < word.length(); i++) {
                String pred = word.substring(0, i) + word.substring(i + 1);
                if (dp.containsKey(pred)) {
                    bestChain = Math.max(bestChain, dp.get(pred) + 1);
                }
            }
            dp.put(word, bestChain);
            ans = Math.max(ans, bestChain);
        }

        return ans;
    }

    public static void main(String[] args) {
        System.out.println("=== Longest String Chain ===\n");

        String[] w1 = {"a", "b", "ba", "bca", "bda", "bdca"};
        System.out.println("Brute:   " + bruteForce(w1));   // 4
        System.out.println("Optimal: " + optimal(w1));       // 4
        System.out.println("Best:    " + best(w1));          // 4

        String[] w2 = {"xbc", "pcxbcf", "xb", "cxbc", "pcxbc"};
        System.out.println("\nBrute:   " + bruteForce(w2));  // 5
        System.out.println("Optimal: " + optimal(w2));       // 5
        System.out.println("Best:    " + best(w2));          // 5

        String[] w3 = {"abcd", "dbqca"};
        System.out.println("\nNo chain: " + best(w3));       // 1

        String[] w4 = {"a"};
        System.out.println("Single:   " + best(w4));         // 1
    }
}
