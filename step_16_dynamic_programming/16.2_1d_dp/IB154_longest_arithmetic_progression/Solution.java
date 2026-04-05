/**
 * Problem: Longest Arithmetic Progression
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a sorted array A, find the length of the longest subsequence
 * that forms an Arithmetic Progression (equal differences between terms).
 *
 * DP approach:
 *   dp[j][diff] = length of longest AP ending at index j with common difference diff.
 *   For each pair (i, j) where i < j:
 *     diff = A[j] - A[i]
 *     dp[j][diff] = dp[i][diff] + 1   (extend AP ending at i with diff)
 *   Base: every pair forms an AP of length 2.
 *   Answer: max over all dp[j][diff], initialized to 2 (any two elements).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (O(n^3) check all subsequences)
    // Time: O(n^3)  |  Space: O(1)
    // ============================================================
    // For every pair (i, j) as the first two elements of an AP,
    // greedily extend the AP by scanning to the right.
    static class BruteForce {
        public static int longestAP(int[] A) {
            int n = A.length;
            if (n <= 2) return n;
            int best = 2;

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int diff = A[j] - A[i];
                    int len = 2;
                    int last = A[j];
                    // Extend the AP greedily
                    for (int k = j + 1; k < n; k++) {
                        if (A[k] - last == diff) {
                            len++;
                            last = A[k];
                        }
                    }
                    best = Math.max(best, len);
                }
            }
            return best;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (DP with HashMap per index)
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    // dp[j] is a map: diff -> length of longest AP ending at j with that diff.
    // For each j, look at all i < j, compute diff = A[j]-A[i],
    // then dp[j][diff] = dp[i].getOrDefault(diff, 1) + 1.
    // The result is the max length across all dp[j][diff].
    static class Optimal {
        public static int longestAP(int[] A) {
            int n = A.length;
            if (n <= 2) return n;

            @SuppressWarnings("unchecked")
            HashMap<Integer, Integer>[] dp = new HashMap[n];
            for (int i = 0; i < n; i++) dp[i] = new HashMap<>();

            int best = 2;
            for (int j = 1; j < n; j++) {
                for (int i = 0; i < j; i++) {
                    int diff = A[j] - A[i];
                    int len = dp[i].getOrDefault(diff, 1) + 1;
                    dp[j].merge(diff, len, Math::max);
                    best = Math.max(best, len);
                }
            }
            return best;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (DP with 2D array using coordinate compression)
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    // Since A is sorted, differences range from A[n-1]-A[0] to -(A[n-1]-A[0]).
    // We use a 2D array dp[i][j] = length of the longest AP where j is the
    // index of the second-to-last element (i.e., A[i] is last, A[j] is second-last).
    //
    // For each pair (j, i) with j < i:
    //   Compute diff = A[i] - A[j]
    //   Use binary search (since array is sorted) to find index k where A[k] = A[j] - diff.
    //   If k exists: dp[j][i] = dp[k][j] + 1
    //   Else:        dp[j][i] = 2
    //
    // This avoids hash maps entirely.
    static class Best {
        public static int longestAP(int[] A) {
            int n = A.length;
            if (n <= 2) return n;

            // dp[i][j] = length of longest AP with last two elements at index j, i (j<i)
            int[][] dp = new int[n][n];
            // Initialize all pairs to length 2
            for (int[] row : dp) Arrays.fill(row, 2);

            int best = 2;

            // Build a value->index map for O(1) lookup
            HashMap<Integer, Integer> indexMap = new HashMap<>();
            for (int i = 0; i < n; i++) indexMap.put(A[i], i);

            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    int diff = A[i] - A[j];
                    int prev = A[j] - diff;
                    if (indexMap.containsKey(prev)) {
                        int k = indexMap.get(prev);
                        // k must be strictly less than j
                        if (k < j) {
                            dp[j][i] = dp[k][j] + 1;
                        }
                    }
                    best = Math.max(best, dp[j][i]);
                }
            }
            return best;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Longest Arithmetic Progression ===");

        int[][] tests = {
            {1, 7, 10, 13, 14, 19},  // AP: 1,7,13,19  len=4
            {1, 2, 3},               // AP: 1,2,3       len=3
            {3, 6, 9, 12},           // AP: full array  len=4
            {9, 4, 7, 2, 10},        // Needs sorting; but array should be sorted per problem
            {1, 2, 4, 5, 7, 8, 10}, // AP: 1,4,7,10 or 2,5,8 -> len=4
        };
        // Sort each test (problem says sorted input)
        for (int[] t : tests) Arrays.sort(t);

        int[] expected = {4, 3, 4, 3, 4};

        for (int t = 0; t < tests.length; t++) {
            int bf = BruteForce.longestAP(tests[t]);
            int op = Optimal.longestAP(tests[t]);
            int be = Best.longestAP(tests[t]);
            String status = (bf == op && op == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("[%s] A=%-30s | Brute: %d | Optimal: %d | Best: %d | Expected: %d%n",
                              status, Arrays.toString(tests[t]), bf, op, be, expected[t]);
        }
    }
}
