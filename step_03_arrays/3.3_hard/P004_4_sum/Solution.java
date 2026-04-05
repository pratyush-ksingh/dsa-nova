/**
 * Problem: 4 Sum
 * Difficulty: HARD | XP: 35
 * LeetCode: 18
 *
 * Given an array nums of n integers and a target, return all unique quadruplets
 * [nums[a], nums[b], nums[c], nums[d]] such that a, b, c, d are distinct
 * and nums[a] + nums[b] + nums[c] + nums[d] == target.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^4)  |  Space: O(k)
    // ============================================================
    /**
     * Try all combinations of 4 indices (a < b < c < d).
     * If their values sum to target, add the sorted tuple to a Set to
     * automatically deduplicate results.
     */
    static class BruteForce {
        public List<List<Integer>> fourSum(int[] nums, int target) {
            int n = nums.length;
            Set<List<Integer>> resultSet = new HashSet<>();
            for (int a = 0; a < n; a++)
                for (int b = a + 1; b < n; b++)
                    for (int c = b + 1; c < n; c++)
                        for (int d = c + 1; d < n; d++)
                            if ((long)nums[a] + nums[b] + nums[c] + nums[d] == target) {
                                List<Integer> quad = Arrays.asList(nums[a], nums[b], nums[c], nums[d]);
                                Collections.sort(quad);
                                resultSet.add(quad);
                            }
            return new ArrayList<>(resultSet);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Sort + Fix Two + Two Pointers
    // Time: O(n^3)  |  Space: O(k)
    // ============================================================
    /**
     * Sort the array. Fix first element (i) and second element (j).
     * Use two pointers lo = j+1, hi = n-1 to find pairs summing to
     * (target - nums[i] - nums[j]). Skip duplicates at each level.
     */
    static class Optimal {
        public List<List<Integer>> fourSum(int[] nums, int target) {
            Arrays.sort(nums);
            int n = nums.length;
            List<List<Integer>> result = new ArrayList<>();

            for (int i = 0; i < n - 3; i++) {
                if (i > 0 && nums[i] == nums[i - 1]) continue;

                for (int j = i + 1; j < n - 2; j++) {
                    if (j > i + 1 && nums[j] == nums[j - 1]) continue;

                    int lo = j + 1, hi = n - 1;
                    while (lo < hi) {
                        long total = (long)nums[i] + nums[j] + nums[lo] + nums[hi];
                        if (total == target) {
                            result.add(Arrays.asList(nums[i], nums[j], nums[lo], nums[hi]));
                            while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
                            while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
                            lo++; hi--;
                        } else if (total < target) {
                            lo++;
                        } else {
                            hi--;
                        }
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Sort + Fix Two + Two Pointers + Pruning
    // Time: O(n^3)  |  Space: O(k)
    // ============================================================
    /**
     * Same as Optimal plus two early-exit pruning conditions per outer loop:
     * 1. If the minimum possible quadruplet sum with current i exceeds target, break.
     * 2. If the maximum possible quadruplet sum with current i is below target, skip.
     * Same two prunings for the j loop. Dramatically faster on sorted/extreme inputs.
     *
     * Note: use long arithmetic to avoid integer overflow when elements are near
     * Integer.MAX_VALUE or MIN_VALUE.
     */
    static class Best {
        public List<List<Integer>> fourSum(int[] nums, int target) {
            Arrays.sort(nums);
            int n = nums.length;
            List<List<Integer>> result = new ArrayList<>();

            for (int i = 0; i < n - 3; i++) {
                if (i > 0 && nums[i] == nums[i - 1]) continue;
                if ((long)nums[i] + nums[i+1] + nums[i+2] + nums[i+3] > target) break;
                if ((long)nums[i] + nums[n-1] + nums[n-2] + nums[n-3] < target) continue;

                for (int j = i + 1; j < n - 2; j++) {
                    if (j > i + 1 && nums[j] == nums[j - 1]) continue;
                    if ((long)nums[i] + nums[j] + nums[j+1] + nums[j+2] > target) break;
                    if ((long)nums[i] + nums[j] + nums[n-1] + nums[n-2] < target) continue;

                    int lo = j + 1, hi = n - 1;
                    while (lo < hi) {
                        long total = (long)nums[i] + nums[j] + nums[lo] + nums[hi];
                        if (total == target) {
                            result.add(Arrays.asList(nums[i], nums[j], nums[lo], nums[hi]));
                            while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
                            while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
                            lo++; hi--;
                        } else if (total < target) {
                            lo++;
                        } else {
                            hi--;
                        }
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 4 Sum ===");

        int[] nums1 = {1, 0, -1, 0, -2, 2}; int t1 = 0;
        System.out.println("Input: [1,0,-1,0,-2,2], target=0");
        System.out.println("Brute:   " + new BruteForce().fourSum(nums1.clone(), t1));
        System.out.println("Optimal: " + new Optimal().fourSum(nums1.clone(), t1));
        System.out.println("Best:    " + new Best().fourSum(nums1.clone(), t1));

        int[] nums2 = {2, 2, 2, 2, 2}; int t2 = 8;
        System.out.println("\nInput: [2,2,2,2,2], target=8");
        System.out.println("Brute:   " + new BruteForce().fourSum(nums2.clone(), t2));
        System.out.println("Optimal: " + new Optimal().fourSum(nums2.clone(), t2));
        System.out.println("Best:    " + new Best().fourSum(nums2.clone(), t2));
    }
}
