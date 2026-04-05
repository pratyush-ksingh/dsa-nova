/**
 * Problem: Sum of Subarray Minimums
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int MOD = 1_000_000_007;

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Check all subarrays
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    public static int bruteForce(int[] arr) {
        long sum = 0;
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int min = arr[i];
            for (int j = i; j < n; j++) {
                min = Math.min(min, arr[j]);
                sum = (sum + min) % MOD;
            }
        }
        return (int) sum;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Monotonic stack (PLE + NLE)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static int optimal(int[] arr) {
        int n = arr.length;
        int[] left = new int[n];  // distance to Previous Less Element
        int[] right = new int[n]; // distance to Next Less-or-Equal Element
        Stack<Integer> stack = new Stack<>();

        // Find PLE distances (strictly less on left)
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) stack.pop();
            left[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
            stack.push(i);
        }

        stack.clear();
        // Find NLE distances (less or equal on right, to handle duplicates)
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) stack.pop();
            right[i] = stack.isEmpty() ? n - i : stack.peek() - i;
            stack.push(i);
        }

        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum = (sum + (long) arr[i] * left[i] % MOD * right[i]) % MOD;
        }
        return (int) sum;
    }

    // ============================================================
    // APPROACH 3: BEST - Single-pass monotonic stack with contribution
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static int best(int[] arr) {
        int n = arr.length;
        long sum = 0;
        long[] dp = new long[n]; // dp[i] = sum of minimums of all subarrays ending at i
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }
            if (stack.isEmpty()) {
                // arr[i] is the minimum of all subarrays ending at i
                dp[i] = (long) arr[i] * (i + 1);
            } else {
                // arr[i] is min for subarrays starting after stack.peek()
                // For subarrays that include stack.peek(), use dp[stack.peek()]
                dp[i] = dp[stack.peek()] + (long) arr[i] * (i - stack.peek());
            }
            dp[i] %= MOD;
            sum = (sum + dp[i]) % MOD;
            stack.push(i);
        }
        return (int) sum;
    }

    public static void main(String[] args) {
        System.out.println("=== Sum of Subarray Minimums ===");
        int[] arr = {3, 1, 2, 4};  // Expected: 17
        System.out.println("Brute Force: " + bruteForce(arr));
        System.out.println("Optimal:     " + optimal(arr));
        System.out.println("Best:        " + best(arr));
    }
}
