/**
 * Problem: Kadane's Algorithm - Maximum Subarray Sum
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: 53
 *
 * Given an integer array nums, find the contiguous subarray (containing at
 * least one number) which has the largest sum and return its sum.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    /**
     * Try every possible subarray [i..j]. For each starting index i,
     * extend j rightward and accumulate the sum incrementally.
     * Track the global maximum. Avoids the O(n^3) triple-loop.
     */
    static class BruteForce {
        public int maxSubArray(int[] nums) {
            int n = nums.length;
            int maxSum = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int currentSum = 0;
                for (int j = i; j < n; j++) {
                    currentSum += nums[j];
                    maxSum = Math.max(maxSum, currentSum);
                }
            }
            return maxSum;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Kadane's Algorithm
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * At each position i, decide: extend the previous subarray or restart
     * from nums[i]? Take whichever is larger. The running maximum is the answer.
     * Key recurrence: currentSum = max(nums[i], currentSum + nums[i])
     */
    static class Optimal {
        public int maxSubArray(int[] nums) {
            int maxSum = nums[0];
            int currentSum = nums[0];
            for (int i = 1; i < nums.length; i++) {
                currentSum = Math.max(nums[i], currentSum + nums[i]);
                maxSum = Math.max(maxSum, currentSum);
            }
            return maxSum;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Kadane's with Subarray Index Tracking
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Same O(n) Kadane's algorithm, but also tracks the start and end
     * indices of the maximum subarray for follow-up questions.
     * Returns the maximum sum; start/end indices accessible internally.
     */
    static class Best {
        public int maxSubArray(int[] nums) {
            int maxSum = nums[0];
            int currentSum = nums[0];
            int start = 0, end = 0, tempStart = 0;

            for (int i = 1; i < nums.length; i++) {
                if (nums[i] > currentSum + nums[i]) {
                    currentSum = nums[i];
                    tempStart = i;
                } else {
                    currentSum += nums[i];
                }
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                    start = tempStart;
                    end = i;
                }
            }
            // Maximum subarray is nums[start..end]
            System.out.println("  Subarray indices: [" + start + ", " + end + "]");
            return maxSum;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Kadane's Algorithm - Maximum Subarray Sum ===");

        int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Input: [-2,1,-3,4,-1,2,1,-5,4]");
        System.out.println("Brute:   " + new BruteForce().maxSubArray(nums1));   // 6
        System.out.println("Optimal: " + new Optimal().maxSubArray(nums1));       // 6
        System.out.print("Best:    ");
        System.out.println(new Best().maxSubArray(nums1));                         // 6

        int[] nums2 = {-1, -2, -3, -4};
        System.out.println("\nInput: [-1,-2,-3,-4]");
        System.out.println("Brute:   " + new BruteForce().maxSubArray(nums2));   // -1
        System.out.println("Optimal: " + new Optimal().maxSubArray(nums2));       // -1
        System.out.print("Best:    ");
        System.out.println(new Best().maxSubArray(nums2));                         // -1
    }
}
