/**
 * Problem: How Many Times Array is Rotated
 * Difficulty: MEDIUM | XP: 25
 *
 * A sorted array (ascending) has been rotated k times to the right.
 * Given the rotated array, return k (number of rotations).
 *
 * Key insight: The number of rotations equals the index of the minimum element.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Linear scan to find the index of the minimum element.
     * The index of the minimum equals the number of rotations.
     */
    static class BruteForce {
        public int findKRotation(int[] nums) {
            int minVal = nums[0], minIdx = 0;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] < minVal) {
                    minVal = nums[i];
                    minIdx = i;
                }
            }
            return minIdx;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Binary Search for Minimum
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    /**
     * In a rotated sorted array, one half is always fully sorted.
     * The minimum element lies in the UNSORTED half.
     *
     * Compare nums[mid] with nums[hi]:
     * - If nums[mid] > nums[hi]: left half is sorted, min is in (mid, hi] → lo = mid + 1
     * - Else: right half is sorted, min is in [lo, mid] → hi = mid
     *
     * When lo == hi, that index holds the minimum = number of rotations.
     */
    static class Optimal {
        public int findKRotation(int[] nums) {
            int lo = 0, hi = nums.length - 1;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] > nums[hi]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return lo;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Binary Search with Explicit Comments
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    /**
     * Same algorithm as Optimal with additional inline reasoning.
     * Comparing with nums[hi] (rather than nums[lo]) is standard because:
     * - It avoids the ambiguity when lo == mid (2-element array)
     * - The "not rotated" case (nums[0] <= nums[n-1]) returns 0 naturally
     *
     * The minimum index IS the answer: an array rotated k times has its
     * original first element at index k.
     */
    static class Best {
        public int findKRotation(int[] nums) {
            int lo = 0, hi = nums.length - 1;

            // If array is not rotated (fully sorted), min is at index 0
            if (nums[lo] <= nums[hi]) return 0;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] > nums[hi]) {
                    // Left half [lo..mid] is sorted ascending
                    // Minimum must be somewhere in (mid, hi]
                    lo = mid + 1;
                } else {
                    // Right half [mid..hi] is sorted ascending
                    // Minimum is in [lo, mid] (mid could be the min)
                    hi = mid;
                }
            }

            return lo; // index of minimum = number of rotations
        }
    }

    public static void main(String[] args) {
        System.out.println("=== How Many Times Array is Rotated ===");

        int[] test1 = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("Input: [4,5,6,7,0,1,2]");
        System.out.println("Brute:   " + new BruteForce().findKRotation(test1));  // 4
        System.out.println("Optimal: " + new Optimal().findKRotation(test1));      // 4
        System.out.println("Best:    " + new Best().findKRotation(test1));         // 4

        int[] test2 = {1, 2, 3, 4, 5};
        System.out.println("\nInput: [1,2,3,4,5] (not rotated)");
        System.out.println("Brute:   " + new BruteForce().findKRotation(test2));  // 0
        System.out.println("Optimal: " + new Optimal().findKRotation(test2));      // 0
        System.out.println("Best:    " + new Best().findKRotation(test2));         // 0

        int[] test3 = {3, 4, 5, 1, 2};
        System.out.println("\nInput: [3,4,5,1,2]");
        System.out.println("Brute:   " + new BruteForce().findKRotation(test3));  // 3
        System.out.println("Optimal: " + new Optimal().findKRotation(test3));      // 3
        System.out.println("Best:    " + new Best().findKRotation(test3));         // 3
    }
}
