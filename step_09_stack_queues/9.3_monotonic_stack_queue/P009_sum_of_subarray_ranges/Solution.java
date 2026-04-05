/**
 * Problem: Sum of Subarray Ranges
 * LeetCode 2104 | Difficulty: MEDIUM | XP: 25
 *
 * Key Insight: Sum of ranges = sum_of_subarray_maximums - sum_of_subarray_minimums.
 * Each part is solved with a monotonic stack contribution technique in O(n).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Enumerate every subarray [i..j], maintain running min and max.
         * Add (max - min) to the answer.
         */
        public long subArrayRanges(int[] nums) {
            int n = nums.length;
            long total = 0;
            for (int i = 0; i < n; i++) {
                int curMin = nums[i], curMax = nums[i];
                for (int j = i; j < n; j++) {
                    curMin = Math.min(curMin, nums[j]);
                    curMax = Math.max(curMax, nums[j]);
                    total += curMax - curMin;
                }
            }
            return total;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Monotonic Stack)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /** Sum of subarray minimums using PLE / NLE contribution. */
        private long sumSubarrayMins(int[] nums) {
            int n = nums.length;
            int[] stack = new int[n + 1];
            int top = -1;
            long total = 0;
            for (int i = 0; i <= n; i++) {
                int val = (i < n) ? nums[i] : Integer.MIN_VALUE;
                while (top >= 0 && nums[stack[top]] > val) {
                    int mid = stack[top--];
                    int left = (top >= 0) ? stack[top] : -1;
                    total += (long) nums[mid] * (mid - left) * (i - mid);
                }
                stack[++top] = i;
            }
            return total;
        }

        /** Sum of subarray maximums using PGE / NGE contribution. */
        private long sumSubarrayMaxs(int[] nums) {
            int n = nums.length;
            int[] stack = new int[n + 1];
            int top = -1;
            long total = 0;
            for (int i = 0; i <= n; i++) {
                int val = (i < n) ? nums[i] : Integer.MAX_VALUE;
                while (top >= 0 && nums[stack[top]] < val) {
                    int mid = stack[top--];
                    int left = (top >= 0) ? stack[top] : -1;
                    total += (long) nums[mid] * (mid - left) * (i - mid);
                }
                stack[++top] = i;
            }
            return total;
        }

        public long subArrayRanges(int[] nums) {
            return sumSubarrayMaxs(nums) - sumSubarrayMins(nums);
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Single-pass with two stacks)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Combine min-stack and max-stack processing in one pass.
         * Reduces the number of loop iterations by half (same asymptotic).
         */
        public long subArrayRanges(int[] nums) {
            int n = nums.length;
            int[] minStk = new int[n + 1];
            int[] maxStk = new int[n + 1];
            int minTop = -1, maxTop = -1;
            long sumMin = 0, sumMax = 0;

            for (int i = 0; i <= n; i++) {
                // Pop from min-stack (monotone increasing)
                while (minTop >= 0 && (i == n || nums[minStk[minTop]] >= nums[i])) {
                    int mid = minStk[minTop--];
                    int left = (minTop >= 0) ? minStk[minTop] : -1;
                    sumMin += (long) nums[mid] * (mid - left) * (i - mid);
                }
                // Pop from max-stack (monotone decreasing)
                while (maxTop >= 0 && (i == n || nums[maxStk[maxTop]] <= nums[i])) {
                    int mid = maxStk[maxTop--];
                    int left = (maxTop >= 0) ? maxStk[maxTop] : -1;
                    sumMax += (long) nums[mid] * (mid - left) * (i - mid);
                }
                if (i < n) {
                    minStk[++minTop] = i;
                    maxStk[++maxTop] = i;
                }
            }
            return sumMax - sumMin;
        }
    }

    public static void main(String[] args) {
        int[][] tests    = {{1, 2, 3}, {1, 3, 3}, {4, -2, -3, 4, 1}};
        long[]  expected = {4, 4, 59};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== Sum of Subarray Ranges ===");
        for (int t = 0; t < tests.length; t++) {
            long b = bf.subArrayRanges(tests[t]);
            long o = opt.subArrayRanges(tests[t]);
            long be = bst.subArrayRanges(tests[t]);
            String status = (b == o && o == be && be == expected[t]) ? "OK" : "FAIL";
            System.out.printf("  brute=%d  optimal=%d  best=%d  (expected %d) [%s]%n",
                              b, o, be, expected[t], status);
        }
    }
}
