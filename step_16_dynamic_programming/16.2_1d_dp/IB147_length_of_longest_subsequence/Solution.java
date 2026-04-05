/**
 * Problem: Length of Longest Subsequence
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an array of integers, find the length of the longest subsequence
 * such that adjacent elements in the subsequence differ by exactly 1.
 * This is an LIS variant where the "increase" rule is replaced by
 * "differ by exactly +1 between consecutive chosen elements".
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^n * n)  |  Space: O(n)
    // Generate all 2^n subsequences; check each for validity.
    // ============================================================
    static class BruteForce {

        private static int maxLen;

        private static void generate(int[] arr, int index, List<Integer> current) {
            if (!current.isEmpty()) {
                boolean valid = true;
                for (int i = 1; i < current.size(); i++) {
                    if (current.get(i) - current.get(i - 1) != 1) {
                        valid = false;
                        break;
                    }
                }
                if (valid) maxLen = Math.max(maxLen, current.size());
            }
            for (int i = index; i < arr.length; i++) {
                current.add(arr[i]);
                generate(arr, i + 1, current);
                current.remove(current.size() - 1);
            }
        }

        public static int solve(int[] arr) {
            if (arr.length == 0) return 0;
            maxLen = 1;
            generate(arr, 0, new ArrayList<>());
            return maxLen;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — HashMap DP
    // Time: O(n)  |  Space: O(n)
    // dp[v] = length of longest valid subsequence ending at value v.
    // For each x: dp[x] = dp.getOrDefault(x-1, 0) + 1
    // ============================================================
    static class Optimal {

        public static int solve(int[] arr) {
            if (arr.length == 0) return 0;
            Map<Integer, Integer> dp = new HashMap<>();
            int maxLen = 1;

            for (int x : arr) {
                int len = dp.getOrDefault(x - 1, 0) + 1;
                dp.put(x, len);
                maxLen = Math.max(maxLen, len);
            }
            return maxLen;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — HashMap DP with max-merge for duplicates
    // Time: O(n)  |  Space: O(n)
    // Same as Optimal but uses merge() to correctly handle duplicate values:
    // if arr=[1,2,1,2], two chains both end at 2 => keep the longer.
    // ============================================================
    static class Best {

        public static int solve(int[] arr) {
            if (arr.length == 0) return 0;
            Map<Integer, Integer> chain = new HashMap<>();
            int ans = 1;

            for (int val : arr) {
                int prevLen = chain.getOrDefault(val - 1, 0);
                int newLen = prevLen + 1;
                // Keep the maximum chain length ending at `val`
                chain.merge(val, newLen, Math::max);
                ans = Math.max(ans, chain.get(val));
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Length of Longest Subsequence ===");

        int[][] tests = {
            {3, 10, 3, 4, 2, 1, 5},   // expected 3: [3,4,5] at indices 2,3,6
            {1, 2, 3, 4, 5},            // expected 5: full array
            {5, 4, 3},                  // expected 1: no two differ by +1 in order
            {1, 9, 3, 0, 18, 2},        // expected 2: [1,2] at indices 0,5
            {1},                         // expected 1
        };
        int[] expected = {3, 5, 1, 2, 1};

        for (int i = 0; i < tests.length; i++) {
            int b = BruteForce.solve(tests[i]);
            int o = Optimal.solve(tests[i]);
            int bt = Best.solve(tests[i]);
            String status = (b == expected[i] && o == expected[i] && bt == expected[i])
                            ? "OK" : "FAIL";
            System.out.printf("  arr=%s%n    Brute=%d, Optimal=%d, Best=%d, Expected=%d [%s]%n",
                Arrays.toString(tests[i]), b, o, bt, expected[i], status);
        }
    }
}
