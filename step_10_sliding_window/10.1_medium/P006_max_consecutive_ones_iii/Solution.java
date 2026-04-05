/**
 * Problem: Max Consecutive Ones III
 * Difficulty: MEDIUM | XP: 25
 * LeetCode #1004
 *
 * Key Insight: "Flip at most k zeros" = "longest subarray with at most k zeros."
 * Classic sliding window.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Try All Subarrays
    // Time: O(n^2)  |  Space: O(1)
    //
    // For each start index, expand right counting zeros.
    // ============================================================
    public static int bruteForce(int[] nums, int k) {
        int n = nums.length;
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            int zeroCount = 0;
            for (int j = i; j < n; j++) {
                if (nums[j] == 0) zeroCount++;
                if (zeroCount > k) break;
                maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sliding Window (Shrinking)
    // Time: O(n)  |  Space: O(1)
    //
    // Expand right, shrink left when zeroCount > k.
    // Track max window size.
    // ============================================================
    public static int optimal(int[] nums, int k) {
        int left = 0, zeroCount = 0, maxLen = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) zeroCount++;

            while (zeroCount > k) {
                if (nums[left] == 0) zeroCount--;
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }

    // ============================================================
    // APPROACH 3: BEST -- Non-Shrinking Sliding Window
    // Time: O(n)  |  Space: O(1)
    //
    // Window only grows or slides. Use 'if' instead of 'while'.
    // Final answer is n - left.
    // ============================================================
    public static int best(int[] nums, int k) {
        int left = 0, zeroCount = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) zeroCount++;

            if (zeroCount > k) {
                // Slide window: move left by 1 (not while -- just if)
                if (nums[left] == 0) zeroCount--;
                left++;
            }
        }

        // The window never shrinks, so its final size is the max
        return nums.length - left;
    }

    public static void main(String[] args) {
        System.out.println("=== Max Consecutive Ones III ===");

        int[][] arrays = {
            {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1},
            {1, 1, 1, 1},
            {0, 0, 0}
        };
        int[] ks = {2, 3, 0, 0};

        for (int t = 0; t < arrays.length; t++) {
            System.out.printf("nums=%s, k=%d%n", java.util.Arrays.toString(arrays[t]), ks[t]);
            System.out.printf("  Brute:   %d%n", bruteForce(arrays[t], ks[t]));
            System.out.printf("  Optimal: %d%n", optimal(arrays[t], ks[t]));
            System.out.printf("  Best:    %d%n", best(arrays[t], ks[t]));
            System.out.println();
        }
    }
}
