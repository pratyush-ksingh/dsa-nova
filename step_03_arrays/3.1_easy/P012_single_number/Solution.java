import java.util.*;

/**
 * Problem: Single Number (LeetCode #136)
 * Difficulty: EASY | XP: 10
 *
 * Every element appears twice except one. Find the single number.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- HashMap Counting
    // Time: O(n)  |  Space: O(n)
    // Count occurrences, return element with count 1.
    // ============================================================
    static class BruteForce {
        public static int singleNumber(int[] nums) {
            Map<Integer, Integer> freq = new HashMap<>();
            for (int num : nums) {
                freq.put(num, freq.getOrDefault(num, 0) + 1);
            }
            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                if (entry.getValue() == 1) {
                    return entry.getKey();
                }
            }
            return -1; // unreachable
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sorting + Adjacent Scan
    // Time: O(n log n)  |  Space: O(1) with in-place sort
    // Sort, then check pairs. Unpaired element is the answer.
    // ============================================================
    static class Optimal {
        public static int singleNumber(int[] nums) {
            Arrays.sort(nums);
            int n = nums.length;

            // Check pairs (i, i+1)
            for (int i = 0; i < n - 1; i += 2) {
                if (nums[i] != nums[i + 1]) {
                    return nums[i];
                }
            }
            // If no mismatch, last element is the single number
            return nums[n - 1];
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- XOR Cancellation
    // Time: O(n)  |  Space: O(1)
    // XOR all elements. Pairs cancel (a^a=0), single remains.
    // ============================================================
    static class Best {
        public static int singleNumber(int[] nums) {
            int result = 0;
            for (int num : nums) {
                result ^= num;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Single Number ===");

        int[] test1 = {2, 2, 1};
        int[] test2 = {4, 1, 2, 1, 2};
        int[] test3 = {1};
        int[] test4 = {-1, -1, -2};
        int[] test5 = {0, 1, 0};

        System.out.println("--- Brute Force ---");
        System.out.println(BruteForce.singleNumber(test1));  // 1
        System.out.println(BruteForce.singleNumber(test2));  // 4

        System.out.println("--- Optimal (Sort) ---");
        System.out.println(Optimal.singleNumber(new int[]{2, 2, 1}));       // 1
        System.out.println(Optimal.singleNumber(new int[]{4, 1, 2, 1, 2})); // 4

        System.out.println("--- Best (XOR) ---");
        System.out.println(Best.singleNumber(test3));  // 1
        System.out.println(Best.singleNumber(test4));  // -2
        System.out.println(Best.singleNumber(test5));  // 1
    }
}
