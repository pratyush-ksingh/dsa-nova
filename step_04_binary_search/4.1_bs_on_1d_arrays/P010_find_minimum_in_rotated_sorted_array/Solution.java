/**
 * Problem: Find Minimum in Rotated Sorted Array
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: 153
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Linear scan — track the running minimum.
         * Simple but ignores the sorted-rotated structure.
         */
        public int findMin(int[] nums) {
            int minimum = nums[0];
            for (int num : nums) {
                minimum = Math.min(minimum, num);
            }
            return minimum;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Binary search comparing mid with the right boundary.
         *
         * If nums[mid] > nums[right]: the minimum is in the right half
         *   (the left half is sorted and all its values are bigger than
         *   the right portion, so the dip/pivot is to the right of mid).
         * Otherwise: mid could be the minimum; narrow to [left, mid].
         */
        public int findMin(int[] nums) {
            int left = 0, right = nums.length - 1;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] > nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            return nums[left];
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Same binary search with an early-exit optimisation:
         * if the current window is already sorted (nums[left] < nums[right])
         * the minimum is simply nums[left] — no need to continue halving.
         */
        public int findMin(int[] nums) {
            int left = 0, right = nums.length - 1;
            while (left < right) {
                if (nums[left] < nums[right]) {
                    return nums[left]; // already sorted sub-array
                }
                int mid = left + (right - left) / 2;
                if (nums[mid] > nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            return nums[left];
        }
    }

    public static void main(String[] args) {
        int[][] testCases = {
            {3, 4, 5, 1, 2},
            {4, 5, 6, 7, 0, 1, 2},
            {11, 13, 15, 17},
            {2, 1},
            {1}
        };
        int[] expected = {1, 0, 11, 1, 1};

        BruteForce bf = new BruteForce();
        Optimal op = new Optimal();
        Best be = new Best();

        System.out.println("=== Find Minimum in Rotated Sorted Array ===");
        for (int i = 0; i < testCases.length; i++) {
            int b = bf.findMin(testCases[i]);
            int o = op.findMin(testCases[i]);
            int bst = be.findMin(testCases[i]);
            String status = (b == expected[i] && o == expected[i] && bst == expected[i]) ? "PASS" : "FAIL";
            System.out.printf("[%s] Brute=%d, Optimal=%d, Best=%d (expected %d)%n",
                status, b, o, bst, expected[i]);
        }
    }
}
