import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Try all subarrays, track max sum and its indices.
// ============================================================
class BruteForce {
    // Returns [maxSum, startIdx, endIdx]
    public static long[] solve(int[] arr) {
        int n = arr.length;
        long maxSum = Long.MIN_VALUE;
        int bestL = 0, bestR = 0;
        for (int i = 0; i < n; i++) {
            long sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum > maxSum) {
                    maxSum = sum;
                    bestL = i; bestR = j;
                }
            }
        }
        return new long[]{maxSum, bestL, bestR};
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Kadane's Algorithm with index tracking)
// Time: O(n)  |  Space: O(1)
// Track current sum start; when sum resets, update start index.
// ============================================================
class Optimal {
    public static long[] solve(int[] arr) {
        int n = arr.length;
        long maxSum = Long.MIN_VALUE, curSum = 0;
        int bestL = 0, bestR = 0, tempStart = 0;

        for (int i = 0; i < n; i++) {
            curSum += arr[i];
            if (curSum > maxSum) {
                maxSum = curSum;
                bestL = tempStart;
                bestR = i;
            }
            if (curSum < 0) {
                curSum = 0;
                tempStart = i + 1;
            }
        }
        return new long[]{maxSum, bestL, bestR};
    }
}

// ============================================================
// APPROACH 3: BEST (Kadane's — handles all-negative arrays)
// Time: O(n)  |  Space: O(1)
// Initialise maxSum as arr[0] to handle all-negative input.
// ============================================================
class Best {
    public static long[] solve(int[] arr) {
        int n = arr.length;
        long maxSum = arr[0], curSum = arr[0];
        int bestL = 0, bestR = 0, tempStart = 0;

        for (int i = 1; i < n; i++) {
            if (curSum < 0) {
                curSum = arr[i];
                tempStart = i;
            } else {
                curSum += arr[i];
            }
            if (curSum > maxSum) {
                maxSum = curSum;
                bestL = tempStart;
                bestR = i;
            }
        }
        return new long[]{maxSum, bestL, bestR};
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Print Max Subarray Sum ===");
        int[][] tests = {
            {-2, 1, -3, 4, -1, 2, 1, -5, 4},
            {1, 2, 3, 4, 5},
            {-1, -2, -3, -4},
            {-2, -3, 4, -1, -2, 1, 5, -3},
        };
        for (int[] arr : tests) {
            long[] bf  = BruteForce.solve(arr.clone());
            long[] op  = Optimal.solve(arr.clone());
            long[] bst = Best.solve(arr.clone());
            System.out.println("arr=" + Arrays.toString(arr));
            System.out.printf("  BF:   sum=%d  subarray=[%d..%d] %s%n",
                bf[0], (int)bf[1], (int)bf[2],
                Arrays.toString(Arrays.copyOfRange(arr, (int)bf[1], (int)bf[2]+1)));
            System.out.printf("  OPT:  sum=%d  subarray=[%d..%d] %s%n",
                op[0], (int)op[1], (int)op[2],
                Arrays.toString(Arrays.copyOfRange(arr, (int)op[1], (int)op[2]+1)));
            System.out.printf("  BEST: sum=%d  subarray=[%d..%d] %s%n",
                bst[0], (int)bst[1], (int)bst[2],
                Arrays.toString(Arrays.copyOfRange(arr, (int)bst[1], (int)bst[2]+1)));
        }
    }
}
