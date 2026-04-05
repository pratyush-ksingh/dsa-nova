/**
 * Problem: Next Greater Element II (LeetCode #503)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a circular array, find the next greater element for each
 * position. Wrap around the end to the beginning.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Circular Linear Scan
    // Time: O(n^2)  |  Space: O(n)
    //
    // For each element, scan the next n-1 elements circularly
    // using modulo indexing.
    // ============================================================
    public static int[] bruteForce(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n; j++) {
                int idx = (i + j) % n;
                if (nums[idx] > nums[i]) {
                    result[i] = nums[idx];
                    break;
                }
            }
        }

        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Monotonic Stack Left-to-Right, 2n
    // Time: O(n)  |  Space: O(n)
    //
    // Traverse 0..2n-1. Stack stores indices waiting for their NGE.
    // When nums[i%n] > nums[stack.top()], pop and record answer.
    // Only push indices < n to avoid duplicates.
    // ============================================================
    public static int[] optimal(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < 2 * n; i++) {
            int val = nums[i % n];
            // Pop indices whose NGE is the current element
            while (!stack.isEmpty() && nums[stack.peek()] < val) {
                result[stack.pop()] = val;
            }
            // Only push indices from first pass
            if (i < n) {
                stack.push(i);
            }
        }

        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Monotonic Stack Right-to-Left, 2n
    // Time: O(n)  |  Space: O(n)
    //
    // Traverse 2n-1 down to 0. Stack holds values in decreasing
    // order (candidates for NGE). Pop all <= current. Stack top
    // is the NGE. Record result only when i < n.
    // ============================================================
    public static int[] best(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 2 * n - 1; i >= 0; i--) {
            int val = nums[i % n];
            // Pop elements that can never be NGE (blocked by current)
            while (!stack.isEmpty() && stack.peek() <= val) {
                stack.pop();
            }
            // Record result only for original indices
            if (i < n) {
                result[i] = stack.isEmpty() ? -1 : stack.peek();
            }
            stack.push(val);
        }

        return result;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Next Greater Element II ===\n");

        int[][] tests = {
            {1, 2, 1},           // [2, -1, 2]
            {1, 2, 3, 4, 3},    // [2, 3, 4, -1, 4]
            {5, 4, 3, 2, 1},    // [-1, 5, 5, 5, 5]
            {1, 1, 1},           // [-1, -1, -1]
            {3},                 // [-1]
        };

        int[][] expected = {
            {2, -1, 2},
            {2, 3, 4, -1, 4},
            {-1, 5, 5, 5, 5},
            {-1, -1, -1},
            {-1},
        };

        for (int t = 0; t < tests.length; t++) {
            System.out.println("Input:    " + Arrays.toString(tests[t]));
            System.out.println("Expected: " + Arrays.toString(expected[t]));

            int[] b = bruteForce(tests[t]);
            int[] o = optimal(tests[t]);
            int[] be = best(tests[t]);

            System.out.println("Brute:    " + Arrays.toString(b) +
                " " + (Arrays.equals(b, expected[t]) ? "PASS" : "FAIL"));
            System.out.println("Optimal:  " + Arrays.toString(o) +
                " " + (Arrays.equals(o, expected[t]) ? "PASS" : "FAIL"));
            System.out.println("Best:     " + Arrays.toString(be) +
                " " + (Arrays.equals(be, expected[t]) ? "PASS" : "FAIL"));
            System.out.println();
        }
    }
}
