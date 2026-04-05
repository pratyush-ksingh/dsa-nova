/**
 * Problem: Count Number of Nice Subarrays
 * Difficulty: HARD | XP: 50
 *
 * Given an array of integers nums and an integer k, return the number of
 * contiguous subarrays that contain exactly k odd numbers.
 *
 * Key insight: exactly(k) = atMost(k) - atMost(k-1)
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Check every subarray
    // Time: O(n^2)  |  Space: O(1)
    // Count odd numbers in every [i,j] subarray; increment if count == k
    // ============================================================
    public static int bruteForce(int[] nums, int k) {
        int n = nums.length, count = 0;
        for (int i = 0; i < n; i++) {
            int odds = 0;
            for (int j = i; j < n; j++) {
                if (nums[j] % 2 != 0) odds++;
                if (odds == k) count++;
                if (odds > k) break;
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Prefix sum of odd counts
    // Time: O(n)  |  Space: O(n)
    // prefix[i] = number of odd numbers in nums[0..i-1]
    // Subarrays [l,r] with exactly k odds: prefix[r+1] - prefix[l] == k
    // Use a frequency map of prefix counts
    // ============================================================
    public static int optimal(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int odds = 0, count = 0;
        for (int num : nums) {
            if (num % 2 != 0) odds++;
            count += freq.getOrDefault(odds - k, 0);
            freq.merge(odds, 1, Integer::sum);
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST - Sliding window: exactly(k) = atMost(k) - atMost(k-1)
    // Time: O(n)  |  Space: O(1)
    // atMost(k) counts subarrays with at most k odds using two pointers
    // ============================================================
    public static int best(int[] nums, int k) {
        return atMost(nums, k) - atMost(nums, k - 1);
    }

    private static int atMost(int[] nums, int k) {
        if (k < 0) return 0;
        int left = 0, odds = 0, count = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] % 2 != 0) odds++;
            while (odds > k) {
                if (nums[left] % 2 != 0) odds--;
                left++;
            }
            count += right - left + 1; // all subarrays ending at right with left..right start
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Count Number of Nice Subarrays ===");

        int[] nums = {1, 1, 2, 1, 1}; int k = 3;
        System.out.println("nums=" + Arrays.toString(nums) + ", k=" + k);
        System.out.println("Brute:   " + bruteForce(nums, k));   // 2
        System.out.println("Optimal: " + optimal(nums, k));       // 2
        System.out.println("Best:    " + best(nums, k));          // 2

        nums = new int[]{2, 4, 6}; k = 1;
        System.out.println("\nnums=" + Arrays.toString(nums) + ", k=" + k);
        System.out.println("Brute:   " + bruteForce(nums, k));   // 0
        System.out.println("Optimal: " + optimal(nums, k));
        System.out.println("Best:    " + best(nums, k));

        nums = new int[]{2, 2, 2, 1, 2, 2, 1, 2, 2, 2}; k = 2;
        System.out.println("\nnums=" + Arrays.toString(nums) + ", k=" + k);
        System.out.println("Brute:   " + bruteForce(nums, k));   // 16
        System.out.println("Optimal: " + optimal(nums, k));
        System.out.println("Best:    " + best(nums, k));
    }
}
