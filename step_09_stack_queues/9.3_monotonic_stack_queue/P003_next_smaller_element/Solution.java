/**
 * Problem: Next Smaller Element
 * Difficulty: MEDIUM | XP: 25
 *
 * For each element, find the next smaller element to its right.
 * Return -1 if no smaller element exists.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Scan Right for Each Element)
// Time: O(n^2) | Space: O(1) extra
// ============================================================
class BruteForce {
    public static int[] nextSmallerElement(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[i]) {
                    result[i] = arr[j];
                    break;
                }
            }
        }
        return result;
    }
}

// ============================================================
// Approach 2: Optimal (Monotonic Stack -- Right to Left)
// Time: O(n) | Space: O(n)
// ============================================================
class Optimal {
    public static int[] nextSmallerElement(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>(); // stores values

        for (int i = n - 1; i >= 0; i--) {
            // Pop elements >= current
            while (!stack.isEmpty() && stack.peek() >= arr[i]) {
                stack.pop();
            }
            // Top of stack is next smaller
            if (!stack.isEmpty()) {
                result[i] = stack.peek();
            }
            stack.push(arr[i]);
        }
        return result;
    }
}

// ============================================================
// Approach 3: Best (Monotonic Stack -- Left to Right Variant)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    public static int[] nextSmallerElement(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            // Current element is NSE for all stack elements > arr[i]
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int idx = stack.pop();
                result[idx] = arr[i];
            }
            stack.push(i);
        }
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Next Smaller Element ===\n");

        int[][] inputs = {
            {4, 5, 2, 10, 8},
            {1, 3, 2, 4},
            {5, 4, 3, 2, 1},
            {1, 2, 3, 4, 5},
            {2, 1, 4, 3}
        };
        int[][] expected = {
            {2, 2, -1, 8, -1},
            {-1, 2, -1, -1},
            {4, 3, 2, 1, -1},
            {-1, -1, -1, -1, -1},
            {1, -1, 3, -1}
        };

        for (int t = 0; t < inputs.length; t++) {
            int[] b = BruteForce.nextSmallerElement(inputs[t]);
            int[] o = Optimal.nextSmallerElement(inputs[t]);
            int[] r = Best.nextSmallerElement(inputs[t]);
            boolean pass = Arrays.equals(o, expected[t])
                        && Arrays.equals(r, expected[t])
                        && Arrays.equals(b, expected[t]);

            System.out.println("Input:    " + Arrays.toString(inputs[t]));
            System.out.println("  Brute:    " + Arrays.toString(b));
            System.out.println("  Optimal:  " + Arrays.toString(o));
            System.out.println("  Best:     " + Arrays.toString(r));
            System.out.println("  Expected: " + Arrays.toString(expected[t])
                             + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
