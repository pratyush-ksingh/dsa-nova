/**
 * Problem: Single Element in Sorted Array (LeetCode 540)
 * Difficulty: MEDIUM | XP: 25
 *
 * Every element appears exactly twice except for one element which appears once.
 * Find that single element in O(log n) time and O(1) space.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - XOR all elements
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * XOR all elements. Duplicates cancel (a ^ a = 0),
         * leaving only the unique element (0 ^ x = x).
         */
        public int singleNonDuplicate(int[] nums) {
            int result = 0;
            for (int num : nums) {
                result ^= num;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Binary search on pair index pattern
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Key insight: Before the single element, pairs sit at (even, even+1).
         * After the single element, the parity flips.
         * Always snap mid to even, then check if nums[mid] == nums[mid+1].
         */
        public int singleNonDuplicate(int[] nums) {
            int lo = 0, hi = nums.length - 1;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (mid % 2 == 1) mid--;   // snap to even index

                if (nums[mid] == nums[mid + 1]) {
                    // Pair intact -> single element is to the right
                    lo = mid + 2;
                } else {
                    // Pair broken -> single element is at mid or left
                    hi = mid;
                }
            }
            return nums[lo];
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Binary search with XOR trick (branchless partner)
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * mid ^ 1 flips the last bit:
         *   even mid -> mid+1 (right partner)
         *   odd  mid -> mid-1 (left partner)
         * If they match, single is to the right; otherwise to the left.
         */
        public int singleNonDuplicate(int[] nums) {
            int lo = 0, hi = nums.length - 1;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] == nums[mid ^ 1]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return nums[lo];
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Single Element in Sorted Array ===");

        int[][] inputs = {
            {1, 1, 2, 3, 3, 4, 4, 8, 8},
            {3, 3, 7, 7, 10, 11, 11},
            {1},
            {1, 1, 2},
            {1, 2, 2}
        };
        int[] expected = {2, 10, 1, 2, 1};

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            int b = bf.singleNonDuplicate(inputs[i]);
            int o = opt.singleNonDuplicate(inputs[i]);
            int be = bst.singleNonDuplicate(inputs[i]);
            String status = (b == expected[i] && o == expected[i] && be == expected[i]) ? "OK" : "FAIL";
            System.out.printf("[%s] Brute=%d, Optimal=%d, Best=%d (expected %d)%n",
                status, b, o, be, expected[i]);
        }
    }
}
