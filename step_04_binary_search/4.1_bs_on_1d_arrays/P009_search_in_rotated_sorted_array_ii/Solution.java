/**
 * Problem: Search in Rotated Sorted Array II
 * LeetCode 81 | Difficulty: MEDIUM | XP: 25
 *
 * Same as LC-33 but the array may contain duplicates.
 * Return true if target exists in nums, false otherwise.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  -  Linear scan
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        public boolean search(int[] nums, int target) {
            for (int num : nums) {
                if (num == target) return true;
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  -  Binary search with duplicate handling
    // Time: O(log n) average, O(n) worst  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * When nums[lo] == nums[mid] == nums[hi] we cannot determine which
         * half is sorted, so we shrink both boundaries (lo++, hi--).
         * Otherwise the same logic as LC-33 applies.
         */
        public boolean search(int[] nums, int target) {
            int lo = 0, hi = nums.length - 1;

            while (lo <= hi) {
                int mid = (lo + hi) / 2;

                if (nums[mid] == target) return true;

                // Ambiguous: shrink both sides
                if (nums[lo] == nums[mid] && nums[mid] == nums[hi]) {
                    lo++;
                    hi--;
                }
                // Left half sorted
                else if (nums[lo] <= nums[mid]) {
                    if (nums[lo] <= target && target < nums[mid]) {
                        hi = mid - 1;
                    } else {
                        lo = mid + 1;
                    }
                }
                // Right half sorted
                else {
                    if (nums[mid] < target && target <= nums[hi]) {
                        lo = mid + 1;
                    } else {
                        hi = mid - 1;
                    }
                }
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  -  Same algorithm, with clear inline comments
    // Time: O(log n) average, O(n) worst  |  Space: O(1)
    // ============================================================
    static class Best {
        public boolean search(int[] nums, int target) {
            int lo = 0, hi = nums.length - 1;

            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;

                if (nums[mid] == target) return true;

                // Can't determine sorted half — shrink both ends
                if (nums[lo] == nums[mid] && nums[mid] == nums[hi]) {
                    lo++;
                    hi--;
                } else if (nums[lo] <= nums[mid]) {   // left half definitely sorted
                    if (nums[lo] <= target && target < nums[mid])
                        hi = mid - 1;                 // target in left half
                    else
                        lo = mid + 1;                 // target in right half
                } else {                              // right half definitely sorted
                    if (nums[mid] < target && target <= nums[hi])
                        lo = mid + 1;                 // target in right half
                    else
                        hi = mid - 1;                 // target in left half
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Search in Rotated Sorted Array II ===");
        int[] a1 = {2, 5, 6, 0, 0, 1, 2};
        int[] a2 = {1, 0, 1, 1, 1};

        System.out.println("Brute  (target=0): " + new BruteForce().search(a1, 0));   // true
        System.out.println("Optimal(target=3): " + new Optimal().search(a1, 3));      // false
        System.out.println("Best   (target=0): " + new Best().search(a2, 0));         // true
    }
}
