/**
 * Problem: Jump Game (LeetCode #55)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursive Backtracking
    // Time: O(2^n)  |  Space: O(n) recursion
    // Try all possible jumps from each position.
    // ============================================================
    public static boolean bruteForce(int[] nums) {
        return canReach(nums, 0);
    }

    private static boolean canReach(int[] nums, int pos) {
        if (pos >= nums.length - 1) return true;

        int maxJump = Math.min(nums[pos], nums.length - 1 - pos);
        for (int j = 1; j <= maxJump; j++) {
            if (canReach(nums, pos + j)) return true;
        }
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Bottom-Up DP
    // Time: O(n^2)  |  Space: O(n)
    // dp[i] = can we reach the last index from position i?
    // ============================================================
    public static boolean optimal(int[] nums) {
        int n = nums.length;
        boolean[] dp = new boolean[n];
        dp[n - 1] = true;

        for (int i = n - 2; i >= 0; i--) {
            int maxJump = Math.min(nums[i], n - 1 - i);
            for (int j = 1; j <= maxJump; j++) {
                if (dp[i + j]) {
                    dp[i] = true;
                    break;  // Found a reachable position, no need to check more
                }
            }
        }
        return dp[0];
    }

    // ============================================================
    // APPROACH 3: BEST -- Greedy Max-Reach
    // Time: O(n)  |  Space: O(1)
    // Track farthest reachable index. Single pass.
    // ============================================================
    public static boolean best(int[] nums) {
        int maxReach = 0;
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            if (i > maxReach) return false;  // Can't reach this index
            maxReach = Math.max(maxReach, i + nums[i]);
            if (maxReach >= n - 1) return true;  // Can already reach the end
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Jump Game ===");

        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("Brute [2,3,1,1,4]:   " + bruteForce(nums1));   // true
        System.out.println("Optimal [2,3,1,1,4]: " + optimal(nums1));      // true
        System.out.println("Best [2,3,1,1,4]:    " + best(nums1));          // true

        int[] nums2 = {3, 2, 1, 0, 4};
        System.out.println("Brute [3,2,1,0,4]:   " + bruteForce(nums2));   // false
        System.out.println("Optimal [3,2,1,0,4]: " + optimal(nums2));      // false
        System.out.println("Best [3,2,1,0,4]:    " + best(nums2));          // false

        // Edge cases
        System.out.println("Best [0]:            " + best(new int[]{0}));           // true
        System.out.println("Best [2,0,0]:        " + best(new int[]{2, 0, 0}));    // true
        System.out.println("Best [1,0,1]:        " + best(new int[]{1, 0, 1}));    // false
    }
}
