/**
 * Problem: Count Subarrays with Given Sum
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: 560 (Subarray Sum Equals K)
 *
 * Given an integer array nums and an integer k, return the total number of
 * contiguous subarrays whose sum equals k.
 *
 * @author DSA_Nova
 */
import java.util.HashMap;
import java.util.Map;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    /**
     * Fix each start index i. Extend the end index j to the right, summing
     * incrementally. Count every subarray where the cumulative sum equals k.
     */
    static class BruteForce {
        public int subarraySum(int[] nums, int k) {
            int n = nums.length, count = 0;
            for (int i = 0; i < n; i++) {
                int currentSum = 0;
                for (int j = i; j < n; j++) {
                    currentSum += nums[j];
                    if (currentSum == k) count++;
                }
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Prefix Sum + HashMap
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * For each index j, prefix_sum[j] is the sum of nums[0..j].
     * A subarray nums[i+1..j] sums to k iff prefix_sum[j] - prefix_sum[i] == k,
     * i.e., prefix_sum[i] == prefix_sum[j] - k.
     *
     * Use a HashMap to count how many times each prefix sum has occurred.
     * Initialize with {0: 1} to count subarrays starting at index 0.
     */
    static class Optimal {
        public int subarraySum(int[] nums, int k) {
            Map<Integer, Integer> freq = new HashMap<>();
            freq.put(0, 1);
            int prefixSum = 0, count = 0;
            for (int num : nums) {
                prefixSum += num;
                count += freq.getOrDefault(prefixSum - k, 0);
                freq.put(prefixSum, freq.getOrDefault(prefixSum, 0) + 1);
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Prefix Sum + HashMap (explicit style)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Identical algorithm to Optimal, written with more explicit variable names
     * to make the "needed complement" pattern crystal clear.
     * For arrays with only non-negative numbers, a sliding window would give O(1)
     * space, but since negatives are allowed, the prefix-sum HashMap is optimal.
     */
    static class Best {
        public int subarraySum(int[] nums, int k) {
            Map<Integer, Integer> prefixCounts = new HashMap<>();
            prefixCounts.put(0, 1);
            int runningSum = 0, result = 0;
            for (int x : nums) {
                runningSum += x;
                int needed = runningSum - k;
                result += prefixCounts.getOrDefault(needed, 0);
                prefixCounts.put(runningSum, prefixCounts.getOrDefault(runningSum, 0) + 1);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Count Subarrays with Given Sum ===");

        int[] nums1 = {1, 1, 1}; int k1 = 2;
        System.out.println("Input: [1,1,1], k=2");
        System.out.println("Brute:   " + new BruteForce().subarraySum(nums1, k1));   // 2
        System.out.println("Optimal: " + new Optimal().subarraySum(nums1, k1));       // 2
        System.out.println("Best:    " + new Best().subarraySum(nums1, k1));          // 2

        int[] nums2 = {1, 2, 3}; int k2 = 3;
        System.out.println("\nInput: [1,2,3], k=3");
        System.out.println("Brute:   " + new BruteForce().subarraySum(nums2, k2));   // 2
        System.out.println("Optimal: " + new Optimal().subarraySum(nums2, k2));       // 2
        System.out.println("Best:    " + new Best().subarraySum(nums2, k2));          // 2

        int[] nums3 = {-1, -1, 1}; int k3 = 0;
        System.out.println("\nInput: [-1,-1,1], k=0");
        System.out.println("Brute:   " + new BruteForce().subarraySum(nums3, k3));   // 1
        System.out.println("Optimal: " + new Optimal().subarraySum(nums3, k3));       // 1
        System.out.println("Best:    " + new Best().subarraySum(nums3, k3));          // 1
    }
}
