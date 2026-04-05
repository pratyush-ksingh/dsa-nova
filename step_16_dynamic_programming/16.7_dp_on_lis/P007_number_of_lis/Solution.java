import java.util.*;

/**
 * Problem: Number of Longest Increasing Subsequences (LeetCode 673)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Generate all subsequences
    // Time: O(2^n * n)  |  Space: O(n)
    // ============================================================
    public static int bruteForce(int[] nums) {
        int n = nums.length;
        int[] maxLen = {0};
        int[] count = {0};

        backtrack(nums, n, 0, Integer.MIN_VALUE, 0, maxLen, count);
        return count[0];
    }

    private static void backtrack(int[] nums, int n, int idx,
                                   int last, int length,
                                   int[] maxLen, int[] count) {
        if (length > maxLen[0]) {
            maxLen[0] = length;
            count[0] = 1;
        } else if (length == maxLen[0] && length > 0) {
            count[0]++;
        }
        for (int i = idx; i < n; i++) {
            if (nums[i] > last) {
                backtrack(nums, n, i + 1, nums[i], length + 1, maxLen, count);
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP with length[] and count[] arrays
    // Time: O(n^2)  |  Space: O(n)
    // dpLen[i] = LIS length ending at i.
    // dpCnt[i] = number of such LIS.
    // ============================================================
    public static int optimal(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] dpLen = new int[n];
        int[] dpCnt = new int[n];
        Arrays.fill(dpLen, 1);
        Arrays.fill(dpCnt, 1);

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (dpLen[j] + 1 > dpLen[i]) {
                        dpLen[i] = dpLen[j] + 1;
                        dpCnt[i] = dpCnt[j];
                    } else if (dpLen[j] + 1 == dpLen[i]) {
                        dpCnt[i] += dpCnt[j];
                    }
                }
            }
        }

        int maxLen = 0;
        for (int l : dpLen) maxLen = Math.max(maxLen, l);

        int result = 0;
        for (int i = 0; i < n; i++) {
            if (dpLen[i] == maxLen) result += dpCnt[i];
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same DP (segment tree gives O(n log n)
    // but is overkill for interviews; O(n^2) is accepted on LeetCode)
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    public static int best(int[] nums) {
        return optimal(nums);
    }

    public static void main(String[] args) {
        System.out.println("=== Number of Longest Increasing Subsequences ===\n");

        int[] nums1 = {1, 3, 5, 4, 7};
        System.out.println("Brute:   " + bruteForce(nums1));   // 2
        System.out.println("Optimal: " + optimal(nums1));       // 2
        System.out.println("Best:    " + best(nums1));          // 2

        int[] nums2 = {2, 2, 2, 2, 2};
        System.out.println("\nBrute:   " + bruteForce(nums2));  // 5
        System.out.println("Optimal: " + optimal(nums2));       // 5
        System.out.println("Best:    " + best(nums2));          // 5

        int[] nums3 = {1, 2, 4, 3, 5, 4, 7, 2};
        System.out.println("\nBrute:   " + bruteForce(nums3));  // 3
        System.out.println("Optimal: " + optimal(nums3));       // 3
        System.out.println("Best:    " + best(nums3));          // 3
    }
}
