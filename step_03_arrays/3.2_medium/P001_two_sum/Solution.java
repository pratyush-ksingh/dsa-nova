/**
 * Problem: Two Sum (LeetCode #1)
 * Difficulty: EASY | XP: 10
 *
 * Given an array of integers nums and an integer target, return indices
 * of the two numbers such that they add up to target.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// ============================================================
// Approach 1: Brute Force (Nested Loops)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{}; // Should never reach here per problem guarantee
    }
}

// ============================================================
// Approach 2: Optimal (Two-Pass Hash Map)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        // Pass 1: Build the map {value -> index}
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }

        // Pass 2: Look up complements
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[]{i, map.get(complement)};
            }
        }
        return new int[]{};
    }
}

// ============================================================
// Approach 3: Best (Single-Pass Hash Map)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{};
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Two Sum ===\n");

        int[][] numsArr = {
            {2, 7, 11, 15},
            {3, 2, 4},
            {3, 3},
            {-1, -2, -3, -4, -5},
            {0, 4, 3, 0}
        };
        int[] targets = {9, 6, 6, -8, 0};
        int[][] expected = {
            {0, 1},
            {1, 2},
            {0, 1},
            {2, 4},
            {0, 3}
        };

        for (int t = 0; t < numsArr.length; t++) {
            int[] nums = numsArr[t];
            int target = targets[t];

            int[] bruteResult = BruteForce.twoSum(nums.clone(), target);
            int[] optimalResult = Optimal.twoSum(nums.clone(), target);
            int[] bestResult = Best.twoSum(nums.clone(), target);

            System.out.println("Input:    " + Arrays.toString(nums) + ", target = " + target);
            System.out.println("Brute:    " + Arrays.toString(bruteResult));
            System.out.println("Optimal:  " + Arrays.toString(optimalResult));
            System.out.println("Best:     " + Arrays.toString(bestResult));
            System.out.println("Expected: " + Arrays.toString(expected[t]));

            // Verify: check that the values at returned indices sum to target
            boolean bruteValid = nums[bruteResult[0]] + nums[bruteResult[1]] == target;
            boolean optimalValid = nums[optimalResult[0]] + nums[optimalResult[1]] == target;
            boolean bestValid = nums[bestResult[0]] + nums[bestResult[1]] == target;
            System.out.println("Pass:     " + (bruteValid && optimalValid && bestValid));
            System.out.println();
        }
    }
}
