/**
 * Problem: Power Set using Bits
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Recursive pick/not-pick
    // Time: O(2^n * n)  |  Space: O(n) recursion depth
    // ============================================================
    public static List<List<Integer>> bruteForce(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        generateSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void generateSubsets(int[] nums, int idx, List<Integer> current,
                                        List<List<Integer>> result) {
        if (idx == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }
        // Don't pick
        generateSubsets(nums, idx + 1, current, result);
        // Pick
        current.add(nums[idx]);
        generateSubsets(nums, idx + 1, current, result);
        current.remove(current.size() - 1);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Bitmask iteration
    // Time: O(2^n * n)  |  Space: O(1) extra (excluding output)
    // ============================================================
    public static List<List<Integer>> optimal(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        int total = 1 << n; // 2^n
        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            result.add(subset);
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST - Iterative build (cascading)
    // Time: O(2^n * n)  |  Space: O(1) extra (excluding output)
    // ============================================================
    public static List<List<Integer>> best(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>()); // start with empty set
        for (int num : nums) {
            int size = result.size();
            for (int i = 0; i < size; i++) {
                List<Integer> newSubset = new ArrayList<>(result.get(i));
                newSubset.add(num);
                result.add(newSubset);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Power Set using Bits ===");
        int[] nums = {1, 2, 3};
        System.out.println("Brute Force: " + bruteForce(nums));
        System.out.println("Optimal:     " + optimal(nums));
        System.out.println("Best:        " + best(nums));
    }
}
