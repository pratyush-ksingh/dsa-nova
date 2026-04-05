import java.util.*;

/**
 * Problem: Edit Distance (LeetCode #72)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(3^(m+n))  |  Space: O(m+n) recursion stack
    // ============================================================
    public static int bruteForce(String word1, String word2) {
        return solveBrute(word1, word2, word1.length() - 1, word2.length() - 1);
    }

    private static int solveBrute(String w1, String w2, int i, int j) {
        if (i < 0) return j + 1;
        if (j < 0) return i + 1;
        if (w1.charAt(i) == w2.charAt(j)) {
            return solveBrute(w1, w2, i - 1, j - 1);
        }
        int insert = 1 + solveBrute(w1, w2, i, j - 1);
        int delete = 1 + solveBrute(w1, w2, i - 1, j);
        int replace = 1 + solveBrute(w1, w2, i - 1, j - 1);
        return Math.min(insert, Math.min(delete, replace));
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- 2D DP Tabulation
    // Time: O(m * n)  |  Space: O(m * n)
    // ============================================================
    public static int optimal(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        dp[i][j - 1],       // insert
                        Math.min(dp[i - 1][j],       // delete
                                 dp[i - 1][j - 1])   // replace
                    );
                }
            }
        }

        return dp[m][n];
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized 1D DP
    // Time: O(m * n)  |  Space: O(min(m, n))
    // ============================================================
    public static int best(String word1, String word2) {
        // Make word2 shorter for space optimization
        if (word1.length() < word2.length()) {
            String tmp = word1; word1 = word2; word2 = tmp;
        }

        int m = word1.length(), n = word2.length();
        int[] prev = new int[n + 1];
        for (int j = 0; j <= n; j++) prev[j] = j;

        for (int i = 1; i <= m; i++) {
            int[] curr = new int[n + 1];
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                        curr[j - 1],
                        Math.min(prev[j], prev[j - 1])
                    );
                }
            }
            prev = curr;
        }

        return prev[n];
    }

    public static void main(String[] args) {
        System.out.println("=== Edit Distance ===\n");

        System.out.println("Brute:   " + bruteForce("horse", "ros"));          // 3
        System.out.println("Optimal: " + optimal("horse", "ros"));              // 3
        System.out.println("Best:    " + best("horse", "ros"));                 // 3

        System.out.println("\nBrute:   " + bruteForce("intention", "execution")); // 5
        System.out.println("Optimal: " + optimal("intention", "execution"));      // 5
        System.out.println("Best:    " + best("intention", "execution"));         // 5

        System.out.println("\nEmpty:   " + best("", "abc"));                      // 3
        System.out.println("Same:    " + best("abc", "abc"));                     // 0
    }
}
