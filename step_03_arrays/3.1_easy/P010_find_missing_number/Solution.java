import java.util.*;

/**
 * Problem: Find Missing Number (LeetCode #268)
 * Difficulty: EASY | XP: 10
 *
 * Array of n distinct numbers from [0, n]. Find the missing one.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- HashSet
    // Time: O(n)  |  Space: O(n)
    // Insert all into set, check which 0..n is missing.
    // ============================================================
    static class BruteForce {
        public static int missingNumber(int[] nums) {
            Set<Integer> set = new HashSet<>();
            for (int num : nums) {
                set.add(num);
            }
            int n = nums.length;
            for (int i = 0; i <= n; i++) {
                if (!set.contains(i)) {
                    return i;
                }
            }
            return -1; // unreachable
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sum Formula (Gauss's Trick)
    // Time: O(n)  |  Space: O(1)
    // expected_sum - actual_sum = missing number.
    // ============================================================
    static class Optimal {
        public static int missingNumber(int[] nums) {
            int n = nums.length;
            long expectedSum = (long) n * (n + 1) / 2;
            long actualSum = 0;
            for (int num : nums) {
                actualSum += num;
            }
            return (int) (expectedSum - actualSum);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- XOR Cancellation
    // Time: O(n)  |  Space: O(1)
    // XOR all indices [0..n] with all array values. Pairs cancel.
    // ============================================================
    static class Best {
        public static int missingNumber(int[] nums) {
            int n = nums.length;
            int xor = 0;

            // XOR with indices 0 to n
            for (int i = 0; i <= n; i++) {
                xor ^= i;
            }
            // XOR with array elements
            for (int num : nums) {
                xor ^= num;
            }

            return xor;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Find Missing Number ===");

        int[] test1 = {3, 0, 1};
        int[] test2 = {0, 1};
        int[] test3 = {9, 6, 4, 2, 3, 5, 7, 0, 1};
        int[] test4 = {0};
        int[] test5 = {1};

        System.out.println("--- Brute Force ---");
        System.out.println(BruteForce.missingNumber(test1));  // 2
        System.out.println(BruteForce.missingNumber(test2));  // 2
        System.out.println(BruteForce.missingNumber(test3));  // 8

        System.out.println("--- Optimal (Sum) ---");
        System.out.println(Optimal.missingNumber(test1));     // 2
        System.out.println(Optimal.missingNumber(test2));     // 2
        System.out.println(Optimal.missingNumber(test3));     // 8

        System.out.println("--- Best (XOR) ---");
        System.out.println(Best.missingNumber(test1));        // 2
        System.out.println(Best.missingNumber(test4));        // 1
        System.out.println(Best.missingNumber(test5));        // 0
    }
}
