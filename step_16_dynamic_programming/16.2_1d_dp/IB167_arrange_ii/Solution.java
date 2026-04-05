/**
 * Problem: Arrange II
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given a string S and an integer K, partition S into exactly K non-empty
 * contiguous groups. The cost of a group is the number of distinct characters
 * in it. Minimize the total cost.
 *
 * Approach: DP with bitmask.
 *   - Characters are lowercase letters (at most 26, so bitmask fits in int).
 *   - dp[i][j] = min cost to partition first i characters into j groups.
 *   - cost(l, r) = distinct chars in S[l..r] = popcount(bitmask[l..r])
 *   - Precompute cost(l, r) for all pairs using bitmasks.
 *   - Transition: dp[i][k] = min over l < i of dp[l][k-1] + cost(l, i-1)
 *
 * Real-life use: Text compression, data partitioning, batch job scheduling.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(K * N^2)  |  Space: O(K * N)
    // Standard DP with precomputed cost array.
    // dp[i][k] = min cost to partition first i chars into k groups.
    // ============================================================
    public static int bruteForce(String s, int k) {
        int n = s.length();
        if (k > n) return -1;

        // Precompute cost[i][j] = distinct chars in s[i..j]
        int[][] cost = new int[n][n];
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int j = i; j < n; j++) {
                mask |= (1 << (s.charAt(j) - 'a'));
                cost[i][j] = Integer.bitCount(mask);
            }
        }

        // dp[i][p] = min cost using first i chars in p groups
        int[][] dp = new int[n + 1][k + 1];
        for (int[] row : dp) Arrays.fill(row, Integer.MAX_VALUE / 2);
        dp[0][0] = 0;

        for (int p = 1; p <= k; p++) {
            for (int i = p; i <= n; i++) {
                for (int prev = p - 1; prev < i; prev++) {
                    if (dp[prev][p - 1] < Integer.MAX_VALUE / 2) {
                        dp[i][p] = Math.min(dp[i][p], dp[prev][p - 1] + cost[prev][i - 1]);
                    }
                }
            }
        }
        return dp[n][k] >= Integer.MAX_VALUE / 2 ? -1 : dp[n][k];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(K * N^2)  |  Space: O(N) with rolling array
    // Same DP but use rolling arrays to reduce space from O(K*N) to O(N).
    // ============================================================
    public static int optimal(String s, int k) {
        int n = s.length();
        if (k > n) return -1;

        // Precompute bitmask[i][j]
        int[][] maskArr = new int[n][n];
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int j = i; j < n; j++) {
                mask |= (1 << (s.charAt(j) - 'a'));
                maskArr[i][j] = mask;
            }
        }

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        Arrays.fill(prev, Integer.MAX_VALUE / 2);
        prev[0] = 0;

        for (int p = 1; p <= k; p++) {
            Arrays.fill(curr, Integer.MAX_VALUE / 2);
            for (int i = p; i <= n; i++) {
                for (int l = p - 1; l < i; l++) {
                    if (prev[l] < Integer.MAX_VALUE / 2) {
                        int cost = Integer.bitCount(maskArr[l][i - 1]);
                        curr[i] = Math.min(curr[i], prev[l] + cost);
                    }
                }
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[n] >= Integer.MAX_VALUE / 2 ? -1 : prev[n];
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(K * N^2)  |  Space: O(K * N)
    // Memoized recursion (top-down DP) — more intuitive.
    // memo[i][k] = min cost to partition s[i..n-1] into k groups.
    // ============================================================
    public static int best(String s, int k) {
        int n = s.length();
        if (k > n) return -1;
        int[][] memo = new int[n][k + 1];
        for (int[] row : memo) Arrays.fill(row, -1);
        int res = topDown(s, 0, k, memo, n);
        return res >= Integer.MAX_VALUE / 2 ? -1 : res;
    }

    private static int topDown(String s, int idx, int groups, int[][] memo, int n) {
        if (groups == 0 && idx == n) return 0;
        if (groups == 0 || idx == n) return Integer.MAX_VALUE / 2;
        if (memo[idx][groups] != -1) return memo[idx][groups];

        int mask = 0;
        int res = Integer.MAX_VALUE / 2;
        // Try all ending positions for current group
        for (int end = idx; end <= n - groups; end++) {
            mask |= (1 << (s.charAt(end) - 'a'));
            int cost = Integer.bitCount(mask);
            int sub = topDown(s, end + 1, groups - 1, memo, n);
            if (sub < Integer.MAX_VALUE / 2)
                res = Math.min(res, cost + sub);
        }
        return memo[idx][groups] = res;
    }

    public static void main(String[] args) {
        System.out.println("=== Arrange II ===\n");

        System.out.println("Test 1: s=\"aabab\", k=2");
        System.out.println("  Brute:   " + bruteForce("aabab", 2));  // expected: 2 (a|abab -> 1+2=3, or aa|bab -> 1+2=3, or aab|ab -> 2+2=4... min=2 with a|abab? no. aab=2, ab=2. Or a=1, abab=2. min=3?)
        System.out.println("  Optimal: " + optimal("aabab", 2));
        System.out.println("  Best:    " + best("aabab", 2));

        System.out.println("\nTest 2: s=\"abc\", k=3");
        System.out.println("  Best: " + best("abc", 3)); // each char alone = 3*1 = 3

        System.out.println("\nTest 3: s=\"abcde\", k=1");
        System.out.println("  Best: " + best("abcde", 1)); // all distinct = 5

        System.out.println("\nTest 4: s=\"aaa\", k=2");
        System.out.println("  Best: " + best("aaa", 2)); // all 'a' -> 1+1=2

        System.out.println("\nTest 5: s=\"abcabc\", k=3");
        System.out.println("  Brute:   " + bruteForce("abcabc", 3));
        System.out.println("  Best:    " + best("abcabc", 3));
    }
}
