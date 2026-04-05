/**
 * Problem: Maximum Absolute Difference
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an array A of N integers, find:
 *   max over all (i,j) of |A[i] - A[j]| + |i - j|
 *
 * Key insight: expand absolute values into 4 cases:
 *   |A[i]-A[j]| + |i-j| = max of:
 *     (A[i]+i) - (A[j]+j)
 *     (A[j]+j) - (A[i]+i)
 *     (A[i]-i) - (A[j]-j)
 *     (A[j]-j) - (A[i]-i)
 *
 * So answer = max(max(A[i]+i) - min(A[j]+j),
 *                 max(A[i]-i) - min(A[j]-j))
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Try all pairs (i, j) and compute |A[i]-A[j]| + |i-j|, tracking max.
         */
        public static int solve(int[] A) {
            int n = A.length;
            int ans = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int val = Math.abs(A[i] - A[j]) + Math.abs(i - j);
                    ans = Math.max(ans, val);
                }
            }
            return ans;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Track 4 extremes in one pass
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Track max and min of (A[i]+i) and (A[i]-i) simultaneously.
         * Answer = max(maxPlus - minPlus, maxMinus - minMinus).
         */
        public static int solve(int[] A) {
            int maxPlus  = Integer.MIN_VALUE, minPlus  = Integer.MAX_VALUE;
            int maxMinus = Integer.MIN_VALUE, minMinus = Integer.MAX_VALUE;

            for (int i = 0; i < A.length; i++) {
                maxPlus  = Math.max(maxPlus,  A[i] + i);
                minPlus  = Math.min(minPlus,  A[i] + i);
                maxMinus = Math.max(maxMinus, A[i] - i);
                minMinus = Math.min(minMinus, A[i] - i);
            }
            return Math.max(maxPlus - minPlus, maxMinus - minMinus);
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Two separate passes (same O(n))
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Equivalent to Optimal; written as two independent range computations
         * for the (A[i]+i) family and (A[i]-i) family.
         * The range (max - min) of each family is computed and we take max.
         */
        public static int solve(int[] A) {
            int n = A.length;
            int max1 = Integer.MIN_VALUE, min1 = Integer.MAX_VALUE;
            int max2 = Integer.MIN_VALUE, min2 = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                max1 = Math.max(max1, A[i] + i);
                min1 = Math.min(min1, A[i] + i);
                max2 = Math.max(max2, A[i] - i);
                min2 = Math.min(min2, A[i] - i);
            }
            return Math.max(max1 - min1, max2 - min2);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Absolute Difference ===");
        int[][] tests = {
            {1, 3, -1},
            {55, -8, 43, 52, 8, 59, -91, -79, -18, 27},
            {1, 2, 3, 4, 5}
        };
        for (int[] A : tests) {
            System.out.printf("A=%s: Brute=%d, Optimal=%d, Best=%d%n",
                Arrays.toString(A),
                BruteForce.solve(A),
                Optimal.solve(A),
                Best.solve(A));
        }
    }
}
