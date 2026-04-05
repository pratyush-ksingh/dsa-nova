/**
 * Problem: Sliding Window Maximum
 * Difficulty: HARD | XP: 50
 *
 * Given an array and window size k, find the maximum in each sliding window.
 * Real-life use: Network traffic analysis, stock price tracking, image processing.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // For each window of size k, scan all k elements for max.
    // Time: O(N * k)  |  Space: O(1) extra
    // ============================================================
    static class BruteForce {
        public static int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            if (n == 0 || k == 0) return new int[0];
            int[] result = new int[n - k + 1];
            for (int i = 0; i <= n - k; i++) {
                int max = nums[i];
                for (int j = i + 1; j < i + k; j++) {
                    max = Math.max(max, nums[j]);
                }
                result[i] = max;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Monotonic deque (decreasing): front always holds index of current window max.
    // - Remove indices out of window from front.
    // - Remove smaller elements from back (they'll never be max).
    // Time: O(N)  |  Space: O(k)
    // ============================================================
    static class Optimal {
        public static int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            if (n == 0 || k == 0) return new int[0];
            int[] result = new int[n - k + 1];
            // Deque stores indices; front = index of max in current window
            Deque<Integer> dq = new ArrayDeque<>();
            for (int i = 0; i < n; i++) {
                // Remove elements outside window
                while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
                // Maintain decreasing order: remove elements smaller than nums[i]
                while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
                dq.offerLast(i);
                // Start recording once first window is complete
                if (i >= k - 1) result[i - k + 1] = nums[dq.peekFirst()];
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Block decomposition (sparse table alternative for interviews):
    // Split array into blocks of size k. For each position i,
    // left[i] = max from start of block to i (left scan),
    // right[i] = max from i to end of block (right scan).
    // Window max = max(right[i], left[i+k-1]).
    // Time: O(N)  |  Space: O(N) — no deque needed, easier to reason about
    // ============================================================
    static class Best {
        public static int[] maxSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            if (n == 0 || k == 0) return new int[0];
            int[] left  = new int[n];
            int[] right = new int[n];
            for (int i = 0; i < n; i++) {
                if (i % k == 0) left[i] = nums[i];                         // block start
                else left[i] = Math.max(left[i - 1], nums[i]);
            }
            for (int i = n - 1; i >= 0; i--) {
                if (i == n - 1 || (i + 1) % k == 0) right[i] = nums[i];   // block end
                else right[i] = Math.max(right[i + 1], nums[i]);
            }
            int[] result = new int[n - k + 1];
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.max(right[i], left[i + k - 1]);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sliding Window Maximum ===");

        int[][] arrs = {{1,3,-1,-3,5,3,6,7}, {1}, {9,8,7,6,5}, {1,3,1,2,0,5}};
        int[] ks    = {3, 1, 3, 3};

        for (int i = 0; i < arrs.length; i++) {
            System.out.printf("%nnums=%s  k=%d%n", Arrays.toString(arrs[i]), ks[i]);
            System.out.println("  Brute  : " + Arrays.toString(BruteForce.maxSlidingWindow(arrs[i], ks[i])));
            System.out.println("  Optimal: " + Arrays.toString(Optimal.maxSlidingWindow(arrs[i], ks[i])));
            System.out.println("  Best   : " + Arrays.toString(Best.maxSlidingWindow(arrs[i], ks[i])));
        }
    }
}
