/**
 * Problem: Next Greater Element I (LeetCode #496)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given nums1 (subset of nums2), for each element in nums1 find
 * the next greater element to its right in nums2.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Nested Linear Scan
    // Time: O(m * n)  |  Space: O(m)
    //
    // For each element in nums1, find it in nums2 and scan right
    // for the first greater element.
    // ============================================================
    public static int[] bruteForce(int[] nums1, int[] nums2) {
        int[] result = new int[nums1.length];

        for (int i = 0; i < nums1.length; i++) {
            int target = nums1[i];
            boolean found = false;
            result[i] = -1;

            // Find target in nums2, then scan right
            for (int j = 0; j < nums2.length; j++) {
                if (nums2[j] == target) {
                    found = true;
                }
                if (found && nums2[j] > target) {
                    result[i] = nums2[j];
                    break;
                }
            }
        }

        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Monotonic Stack Left-to-Right + HashMap
    // Time: O(n + m)  |  Space: O(n)
    //
    // Traverse nums2 left-to-right with a decreasing stack.
    // When a larger element arrives, pop smaller ones and record
    // their NGE. Store in HashMap for O(1) lookup.
    // ============================================================
    public static int[] optimal(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nge = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int val : nums2) {
            // Pop elements smaller than current -- they found their NGE
            while (!stack.isEmpty() && stack.peek() < val) {
                nge.put(stack.pop(), val);
            }
            stack.push(val);
        }

        // Remaining elements have no NGE
        while (!stack.isEmpty()) {
            nge.put(stack.pop(), -1);
        }

        // Build result for nums1
        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = nge.get(nums1[i]);
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Monotonic Stack Right-to-Left + HashMap
    // Time: O(n + m)  |  Space: O(n)
    //
    // Traverse nums2 right-to-left. Stack holds "candidates"
    // for NGE. Pop those <= current (they are blocked). Stack
    // top is the NGE. More intuitive: at each step, the stack
    // is exactly the set of elements to my right that matter.
    // ============================================================
    public static int[] best(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nge = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = nums2.length - 1; i >= 0; i--) {
            int val = nums2[i];
            // Pop elements that can never be NGE for anything at or left of i
            while (!stack.isEmpty() && stack.peek() <= val) {
                stack.pop();
            }
            nge.put(val, stack.isEmpty() ? -1 : stack.peek());
            stack.push(val);
        }

        // Build result for nums1
        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = nge.get(nums1[i]);
        }
        return result;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Next Greater Element I ===\n");

        int[][][] tests = {
            {{4, 1, 2}, {1, 3, 4, 2}},    // expected: [-1, 3, -1]
            {{2, 4}, {1, 2, 3, 4}},         // expected: [3, -1]
            {{1}, {1}},                      // expected: [-1]
            {{1, 3, 5}, {6, 5, 4, 3, 2, 1}} // expected: [-1, -1, -1]
        };

        for (int[][] test : tests) {
            int[] nums1 = test[0], nums2 = test[1];
            System.out.println("nums1=" + Arrays.toString(nums1) +
                             ", nums2=" + Arrays.toString(nums2));
            System.out.println("  Brute:   " + Arrays.toString(bruteForce(nums1, nums2)));
            System.out.println("  Optimal: " + Arrays.toString(optimal(nums1, nums2)));
            System.out.println("  Best:    " + Arrays.toString(best(nums1, nums2)));
            System.out.println();
        }
    }
}
