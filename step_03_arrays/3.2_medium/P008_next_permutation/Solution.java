/**
 * Problem: Next Permutation
 * Difficulty: MEDIUM | XP: 25
 *
 * Find the next lexicographically greater permutation of an array in-place.
 * If no such permutation exists (array is descending), rearrange to smallest (ascending).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n! * n)  |  Space: O(n!)
    // ============================================================
    /**
     * Generate all permutations in sorted order, find current, return next.
     * Only feasible for very small n (<=8). Demonstrates correctness.
     * Real-life: Exhaustive schedule generation (all orderings of tasks).
     */
    public static int[] bruteForce(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        // Generate all permutations
        List<int[]> perms = new ArrayList<>();
        generatePerms(sorted, 0, perms);
        perms.sort((a, b) -> {
            for (int i = 0; i < a.length; i++) if (a[i] != b[i]) return a[i] - b[i];
            return 0;
        });
        for (int i = 0; i < perms.size(); i++) {
            if (Arrays.equals(perms.get(i), nums)) {
                return perms.get((i + 1) % perms.size());
            }
        }
        return nums;
    }

    private static void generatePerms(int[] arr, int idx, List<int[]> result) {
        if (idx == arr.length) { result.add(arr.clone()); return; }
        for (int i = idx; i < arr.length; i++) {
            swap(arr, idx, i);
            generatePerms(arr, idx + 1, result);
            swap(arr, idx, i);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Three-step algorithm:
     * 1. Find rightmost index i where nums[i] < nums[i+1]  (pivot)
     * 2. Find rightmost index j where nums[j] > nums[i], swap them
     * 3. Reverse the suffix starting at i+1
     * Real-life: Generating next test case in combinatorial testing, iterating game states.
     */
    public static int[] optimal(int[] nums) {
        int n = nums.length;
        int[] result = nums.clone();
        // Step 1: find pivot
        int i = n - 2;
        while (i >= 0 && result[i] >= result[i + 1]) i--;

        if (i >= 0) {
            // Step 2: find element just greater than pivot
            int j = n - 1;
            while (result[j] <= result[i]) j--;
            swap(result, i, j);
        }
        // Step 3: reverse suffix
        int left = i + 1, right = n - 1;
        while (left < right) swap(result, left++, right--);
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Same as Optimal — this is the canonical algorithm with no further improvement possible.
     * This version modifies in-place (no clone) as typically expected in interviews.
     * Real-life: In-place permutation generation in memory-constrained environments.
     */
    public static void best(int[] nums) {
        int n = nums.length;
        int i = n - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) i--;
        if (i >= 0) {
            int j = n - 1;
            while (nums[j] <= nums[i]) j--;
            swap(nums, i, j);
        }
        int left = i + 1, right = n - 1;
        while (left < right) swap(nums, left++, right--);
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }

    public static void main(String[] args) {
        System.out.println("=== Next Permutation ===");

        int[][] tests = {
            {1, 2, 3},
            {3, 2, 1},
            {1, 1, 5},
            {1, 3, 2},
        };
        String[] expected = {
            "[1, 3, 2]",
            "[1, 2, 3]",
            "[1, 5, 1]",
            "[2, 1, 3]",
        };

        for (int t = 0; t < tests.length; t++) {
            System.out.println("\nInput: " + Arrays.toString(tests[t]) + "  =>  expected: " + expected[t]);
            System.out.println("Brute:   " + Arrays.toString(bruteForce(tests[t].clone())));
            System.out.println("Optimal: " + Arrays.toString(optimal(tests[t].clone())));
            int[] inplace = tests[t].clone();
            best(inplace);
            System.out.println("Best:    " + Arrays.toString(inplace));
        }
    }
}
