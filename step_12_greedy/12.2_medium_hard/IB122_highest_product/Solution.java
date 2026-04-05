/**
 * Problem: Highest Product
 * InterviewBit | Difficulty: EASY | XP: 10
 *
 * Given an array of integers, find the maximum product of any 3 numbers.
 * The array may contain negatives — two negatives * one positive can dominate.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (All triplets)
    // Time: O(n^3)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Try every combination of 3 distinct indices.
         * Return the maximum product found.
         */
        public int maxProduct(int[] nums) {
            int n = nums.length;
            long best = Long.MIN_VALUE;
            for (int i = 0; i < n; i++)
                for (int j = i + 1; j < n; j++)
                    for (int k = j + 1; k < n; k++)
                        best = Math.max(best, (long) nums[i] * nums[j] * nums[k]);
            return (int) best;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Sort, two candidates)
    // Time: O(n log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Sort the array. Candidates:
         *   1. Three largest: nums[n-1] * nums[n-2] * nums[n-3]
         *   2. Two smallest (most negative) * largest: nums[0] * nums[1] * nums[n-1]
         * Return max of the two.
         */
        public int maxProduct(int[] nums) {
            int[] a = nums.clone();
            Arrays.sort(a);
            int n = a.length;
            long cand1 = (long) a[n-1] * a[n-2] * a[n-3];
            long cand2 = (long) a[0]   * a[1]   * a[n-1];
            return (int) Math.max(cand1, cand2);
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Single pass — O(n))
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Track max1 >= max2 >= max3 (three largest) and min1 <= min2 (two smallest)
         * in one pass. Answer = max(max1*max2*max3, min1*min2*max1).
         */
        public int maxProduct(int[] nums) {
            long max1 = Long.MIN_VALUE, max2 = Long.MIN_VALUE, max3 = Long.MIN_VALUE;
            long min1 = Long.MAX_VALUE, min2 = Long.MAX_VALUE;

            for (int num : nums) {
                if (num >= max1)      { max3 = max2; max2 = max1; max1 = num; }
                else if (num >= max2) { max3 = max2; max2 = num; }
                else if (num >  max3) { max3 = num; }

                if (num <= min1)      { min2 = min1; min1 = num; }
                else if (num <  min2) { min2 = num; }
            }
            return (int) Math.max(max1 * max2 * max3, min1 * min2 * max1);
        }
    }

    public static void main(String[] args) {
        int[][] tests = {{1,2,3}, {-10,-3,5,6,-2}, {0,-1,3,100,50}, {-1,-2,-3}, {1,2,3,4}};
        int[]   exps  = {6,       180,              15000,            -6,         24};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== Highest Product ===");
        for (int t = 0; t < tests.length; t++) {
            int b  = bf.maxProduct(tests[t]);
            int o  = opt.maxProduct(tests[t]);
            int be = bst.maxProduct(tests[t]);
            String status = (b == o && o == be && be == exps[t]) ? "OK" : "FAIL";
            System.out.printf("  brute=%d opt=%d best=%d (exp=%d) [%s]%n",
                              b, o, be, exps[t], status);
        }
    }
}
