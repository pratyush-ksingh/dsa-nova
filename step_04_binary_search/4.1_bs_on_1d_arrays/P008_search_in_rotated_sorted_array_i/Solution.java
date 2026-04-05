/**
 * Problem: Search in Rotated Sorted Array I
 * LeetCode 33 | Difficulty: MEDIUM | XP: 25
 *
 * A sorted array (no duplicates) is rotated at some unknown pivot.
 * Given the array and a target, return the index of target or -1.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  -  Linear scan
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        public int search(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == target) return i;
            }
            return -1;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  -  Modified binary search
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * At each mid point, at least one half is always fully sorted.
         * Identify the sorted half by comparing nums[lo] with nums[mid].
         * Check if target falls in the sorted half; narrow accordingly.
         */
        public int search(int[] nums, int target) {
            int lo = 0, hi = nums.length - 1;

            while (lo <= hi) {
                int mid = (lo + hi) / 2;

                if (nums[mid] == target) return mid;

                // Left half sorted
                if (nums[lo] <= nums[mid]) {
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
            return -1;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  -  Same modified binary search, clean form
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Best {
        public int search(int[] nums, int target) {
            int lo = 0, hi = nums.length - 1;

            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;

                if (nums[mid] == target) return mid;

                if (nums[lo] <= nums[mid]) {              // left half sorted
                    if (nums[lo] <= target && target < nums[mid])
                        hi = mid - 1;
                    else
                        lo = mid + 1;
                } else {                                   // right half sorted
                    if (nums[mid] < target && target <= nums[hi])
                        lo = mid + 1;
                    else
                        hi = mid - 1;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Search in Rotated Sorted Array I ===");
        int[] arr = {4, 5, 6, 7, 0, 1, 2};

        System.out.println("Brute  (target=0): " + new BruteForce().search(arr, 0));  // 4
        System.out.println("Optimal(target=3): " + new Optimal().search(arr, 3));     // -1
        System.out.println("Best   (target=4): " + new Best().search(arr, 4));        // 0
    }
}
