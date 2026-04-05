/**
 * Problem: All Possible Combinations (Power Set)
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Generate all subsets of a given set of distinct integers.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative Cascading
    // Time: O(N * 2^N)  |  Space: O(N * 2^N)
    // Start with [[]], for each num duplicate all existing subsets
    // and append num to the copies.
    // ============================================================
    static class BruteForce {
        public static List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(new ArrayList<>());

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
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Backtracking (Include / Exclude)
    // Time: O(N * 2^N)  |  Space: O(N) stack + O(N * 2^N) output
    // At each index, choose to include or exclude the element.
    // ============================================================
    static class Optimal {
        public static List<List<Integer>> subsets(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            backtrack(nums, 0, new ArrayList<>(), result);
            return result;
        }

        private static void backtrack(int[] nums, int index,
                                       List<Integer> current,
                                       List<List<Integer>> result) {
            if (index == nums.length) {
                result.add(new ArrayList<>(current)); // copy!
                return;
            }
            // Exclude nums[index]
            backtrack(nums, index + 1, current, result);
            // Include nums[index]
            current.add(nums[index]);
            backtrack(nums, index + 1, current, result);
            current.remove(current.size() - 1); // backtrack
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Bitmask Enumeration
    // Time: O(N * 2^N)  |  Space: O(N * 2^N) output
    // Each integer 0..2^N-1 encodes a subset via its bits.
    // ============================================================
    static class Best {
        public static List<List<Integer>> subsets(int[] nums) {
            int n = nums.length;
            int total = 1 << n; // 2^N
            List<List<Integer>> result = new ArrayList<>();

            for (int mask = 0; mask < total; mask++) {
                List<Integer> subset = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if ((mask & (1 << j)) != 0) {
                        subset.add(nums[j]);
                    }
                }
                result.add(subset);
            }
            return result;
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        int[][] testCases = {
            {},
            {1},
            {1, 2},
            {1, 2, 3},
        };
        int[] expectedSizes = {1, 2, 4, 8};

        System.out.println("=== All Possible Combinations (Power Set) ===\n");

        for (int t = 0; t < testCases.length; t++) {
            int[] nums = testCases[t];
            List<List<Integer>> b = BruteForce.subsets(nums);
            List<List<Integer>> o = Optimal.subsets(nums);
            List<List<Integer>> bt = Best.subsets(nums);

            boolean sizeOk = b.size() == expectedSizes[t]
                          && o.size() == expectedSizes[t]
                          && bt.size() == expectedSizes[t];

            // Verify all produce the same subsets (order-independent)
            Set<List<Integer>> bSet = normalize(b);
            Set<List<Integer>> oSet = normalize(o);
            Set<List<Integer>> btSet = normalize(bt);
            boolean contentOk = bSet.equals(oSet) && oSet.equals(btSet);

            String status = (sizeOk && contentOk) ? "PASS" : "FAIL";
            System.out.printf("[%s] nums=%-12s | Count: Brute=%d, Backtrack=%d, Bitmask=%d | Expected=%d%n",
                    status, Arrays.toString(nums), b.size(), o.size(), bt.size(), expectedSizes[t]);
            System.out.println("  Subsets (Bitmask): " + bt);
            System.out.println();
        }
    }

    private static Set<List<Integer>> normalize(List<List<Integer>> subsets) {
        Set<List<Integer>> set = new HashSet<>();
        for (List<Integer> s : subsets) {
            List<Integer> sorted = new ArrayList<>(s);
            Collections.sort(sorted);
            set.add(sorted);
        }
        return set;
    }
}
