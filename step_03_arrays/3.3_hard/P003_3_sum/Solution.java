/**
 * Problem: 3 Sum
 * LeetCode 15 | Difficulty: HARD | XP: 30
 *
 * Given an integer array nums, return all unique triplets [a, b, c] such that
 * a + b + c == 0.  The solution set must not contain duplicates.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  -  Triple nested loops + Set dedup
    // Time: O(n^3)  |  Space: O(k)  where k = number of triplets
    // ============================================================
    static class BruteForce {
        public List<List<Integer>> threeSum(int[] nums) {
            Set<String> seen = new HashSet<>();
            List<List<Integer>> result = new ArrayList<>();
            int n = nums.length;

            for (int i = 0; i < n - 2; i++) {
                for (int j = i + 1; j < n - 1; j++) {
                    for (int k = j + 1; k < n; k++) {
                        if (nums[i] + nums[j] + nums[k] == 0) {
                            int[] triplet = {nums[i], nums[j], nums[k]};
                            Arrays.sort(triplet);
                            String key = triplet[0] + "," + triplet[1] + "," + triplet[2];
                            if (seen.add(key)) {
                                result.add(Arrays.asList(triplet[0], triplet[1], triplet[2]));
                            }
                        }
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  -  Sort + fix i + two pointers
    // Time: O(n^2)  |  Space: O(1) extra
    // ============================================================
    static class Optimal {
        /**
         * Sort the array.
         * For each index i (skipping duplicates), use two pointers
         * lo = i+1, hi = n-1 to find pairs summing to -nums[i].
         * Skip duplicates at lo and hi after each match.
         */
        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            List<List<Integer>> result = new ArrayList<>();
            int n = nums.length;

            for (int i = 0; i < n - 2; i++) {
                if (i > 0 && nums[i] == nums[i - 1]) continue;  // skip dup i
                if (nums[i] > 0) break;                          // all sums positive

                int lo = i + 1, hi = n - 1;
                while (lo < hi) {
                    int sum = nums[i] + nums[lo] + nums[hi];
                    if (sum == 0) {
                        result.add(Arrays.asList(nums[i], nums[lo], nums[hi]));
                        while (lo < hi && nums[lo] == nums[lo + 1]) lo++;
                        while (lo < hi && nums[hi] == nums[hi - 1]) hi--;
                        lo++;
                        hi--;
                    } else if (sum < 0) {
                        lo++;
                    } else {
                        hi--;
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  -  Sort + fix i + HashSet inner pass
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Sort array; fix i; for the inner loop use a HashSet to find
         * the complement without a hi pointer.
         * Demonstrates the hash-based pattern sometimes asked in interviews.
         */
        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            List<List<Integer>> result = new ArrayList<>();
            int n = nums.length;

            for (int i = 0; i < n - 2; i++) {
                if (i > 0 && nums[i] == nums[i - 1]) continue;
                if (nums[i] > 0) break;

                Set<Integer> seenInner = new HashSet<>();
                int j = i + 1;
                while (j < n) {
                    int complement = -nums[i] - nums[j];
                    if (seenInner.contains(complement)) {
                        result.add(Arrays.asList(nums[i], complement, nums[j]));
                        while (j + 1 < n && nums[j] == nums[j + 1]) j++;
                    }
                    seenInner.add(nums[j]);
                    j++;
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 3 Sum ===");
        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        int[] nums2 = {0, 1, 1};
        int[] nums3 = {0, 0, 0};

        System.out.println("Brute:   " + new BruteForce().threeSum(nums1));
        System.out.println("Optimal: " + new Optimal().threeSum(nums2));
        System.out.println("Best:    " + new Best().threeSum(nums3));
    }
}
