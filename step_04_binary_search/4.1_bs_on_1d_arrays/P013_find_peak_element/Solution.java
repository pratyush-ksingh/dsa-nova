/**
 * Problem: Find Peak Element (LeetCode 162)
 * Difficulty: MEDIUM | XP: 25
 *
 * A peak element is strictly greater than its neighbors.
 * Return any peak's index. Boundaries are treated as -infinity.
 * Must run in O(log n) time.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Linear scan
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Scan for the first element greater than both its neighbors.
         * Boundaries count as -infinity so index 0 and n-1 can be peaks.
         */
        public int findPeakElement(int[] nums) {
            int n = nums.length;
            for (int i = 0; i < n; i++) {
                boolean leftOk  = (i == 0)     || nums[i] > nums[i - 1];
                boolean rightOk = (i == n - 1) || nums[i] > nums[i + 1];
                if (leftOk && rightOk) return i;
            }
            return -1; // unreachable for valid input
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Binary search toward the rising side
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * If nums[mid] < nums[mid+1], the slope rises to the right.
         * A peak must exist in [mid+1, hi] because we cannot fall off
         * a cliff without passing a local maximum (boundaries are -inf).
         * Otherwise, a peak exists in [lo, mid].
         */
        public int findPeakElement(int[] nums) {
            int lo = 0, hi = nums.length - 1;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] < nums[mid + 1]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            return lo;
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Same binary search with explicit direction choice
    // Time: O(log n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Always move toward the greater neighbor. The invariant is that
         * a peak always exists within [lo, hi]. When lo == hi, we've found it.
         */
        public int findPeakElement(int[] nums) {
            int lo = 0, hi = nums.length - 1;

            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (nums[mid] > nums[mid + 1]) {
                    hi = mid;       // peak could be mid itself or to the left
                } else {
                    lo = mid + 1;   // peak must be to the right
                }
            }
            return lo; // lo == hi is the peak index
        }
    }

    // ============================================================
    // DRIVER
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Find Peak Element ===");

        int[][] inputs    = {{1,2,3,1}, {1,2,1,3,5,6,4}, {1}, {2,1}, {1,2}};
        int[][] validSets = {{2}, {1,5}, {0}, {0}, {1}};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        for (int i = 0; i < inputs.length; i++) {
            int b  = bf.findPeakElement(inputs[i]);
            int o  = opt.findPeakElement(inputs[i]);
            int be = bst.findPeakElement(inputs[i]);

            boolean bOk  = contains(validSets[i], b);
            boolean oOk  = contains(validSets[i], o);
            boolean beOk = contains(validSets[i], be);
            String status = (bOk && oOk && beOk) ? "OK" : "FAIL";
            System.out.printf("[%s] Brute=%d, Optimal=%d, Best=%d%n", status, b, o, be);
        }
    }

    static boolean contains(int[] arr, int val) {
        for (int v : arr) if (v == val) return true;
        return false;
    }
}
