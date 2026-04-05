/**
 * Problem: Largest Rectangle in Histogram
 * Difficulty: HARD | XP: 50
 *
 * Given an array of non-negative integers representing bar heights in a histogram,
 * find the area of the largest rectangle that fits entirely within the histogram.
 *
 * @author DSA_Nova
 */
import java.util.Stack;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Try all pairs of bars as left/right boundary
    // Time: O(N^2)  |  Space: O(1)
    // For each pair (i,j), min height * width is the rectangle area.
    // ============================================================
    public static int bruteForce(int[] heights) {
        int n = heights.length;
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int minH = heights[i];
            for (int j = i; j < n; j++) {
                minH = Math.min(minH, heights[j]);
                maxArea = Math.max(maxArea, minH * (j - i + 1));
            }
        }
        return maxArea;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Monotonic Stack (single pass)
    // Time: O(N)  |  Space: O(N)
    // Maintain increasing stack. When we find a bar shorter than stack top,
    // pop and compute area using that height with width = current - stack_below - 1.
    // ============================================================
    public static int optimal(int[] heights) {
        int n = heights.length;
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && heights[stack.peek()] > h) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }

    // ============================================================
    // APPROACH 3: BEST - Precompute NSL/NSR (Next Smaller Left/Right)
    // Time: O(N)  |  Space: O(N)
    // For each bar i: width = nsr[i] - nsl[i] - 1. Two-pass stack approach.
    // Equivalent O(N) but clearer and avoids sentinel in loop.
    // ============================================================
    public static int best(int[] heights) {
        int n = heights.length;
        int[] nsl = new int[n]; // index of next smaller element to the left
        int[] nsr = new int[n]; // index of next smaller element to the right
        Stack<Integer> stack = new Stack<>();

        // NSL
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) stack.pop();
            nsl[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        stack.clear();
        // NSR
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) stack.pop();
            nsr[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int width = nsr[i] - nsl[i] - 1;
            maxArea = Math.max(maxArea, heights[i] * width);
        }
        return maxArea;
    }

    public static void main(String[] args) {
        System.out.println("=== Largest Rectangle in Histogram ===");

        int[][] tests = {
            {2, 1, 5, 6, 2, 3},   // expected 10
            {2, 4},               // expected 4
            {1},                  // expected 1
            {6, 7, 5, 2, 4, 5, 9, 3}, // expected 16
            {0},                  // expected 0
        };
        int[] expected = {10, 4, 1, 16, 0};

        for (int i = 0; i < tests.length; i++) {
            int b = bruteForce(tests[i]);
            int o = optimal(tests[i]);
            int be = best(tests[i]);
            System.out.printf("Test %d: brute=%d opt=%d best=%d (exp=%d) %s%n",
                i+1, b, o, be, expected[i], (b==expected[i] && o==expected[i] && be==expected[i]) ? "OK" : "FAIL");
        }
    }
}
