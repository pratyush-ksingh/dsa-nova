/**
 * Problem: Majority Element
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Count each element
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    public static int bruteForce(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] == nums[i]) count++;
            }
            if (count > n / 2) return nums[i];
        }
        return -1;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - HashMap frequency count
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static int optimal(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        int n = nums.length;
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
            if (freq.get(num) > n / 2) return num;
        }
        return -1;
    }

    // ============================================================
    // APPROACH 3: BEST - Boyer-Moore Voting Algorithm
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static int best(int[] nums) {
        int candidate = nums[0], count = 0;
        for (int num : nums) {
            if (count == 0) candidate = num;
            count += (num == candidate) ? 1 : -1;
        }
        // Optional verification step (if majority element guaranteed, skip)
        int verify = 0;
        for (int num : nums) {
            if (num == candidate) verify++;
        }
        return verify > nums.length / 2 ? candidate : -1;
    }

    public static void main(String[] args) {
        System.out.println("=== Majority Element ===");
        int[] nums = {2, 2, 1, 1, 1, 2, 2};
        System.out.println("Brute Force: " + bruteForce(nums));
        System.out.println("Optimal:     " + optimal(nums));
        System.out.println("Best:        " + best(nums));
    }
}
