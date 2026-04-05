/**
 * Problem: Pick from Both Sides (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an integer array A and integer B, pick exactly B elements from either
 * the left end or the right end to maximize their sum.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Try all left/right splits)
// Time: O(B^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int pickFromBothSides(int[] A, int B) {
        int n = A.length;
        int maxSum = Integer.MIN_VALUE;

        for (int leftCount = 0; leftCount <= B; leftCount++) {
            int rightCount = B - leftCount;
            int current = 0;
            // Sum first leftCount elements
            for (int i = 0; i < leftCount; i++) {
                current += A[i];
            }
            // Sum last rightCount elements
            for (int i = n - rightCount; i < n; i++) {
                current += A[i];
            }
            maxSum = Math.max(maxSum, current);
        }
        return maxSum;
    }
}

// ============================================================
// Approach 2: Optimal (Prefix sum + sliding window)
// Time: O(B) | Space: O(1)
// ============================================================
class Optimal {
    public static int pickFromBothSides(int[] A, int B) {
        int n = A.length;

        // Initial window: all B elements from the left
        int windowSum = 0;
        for (int i = 0; i < B; i++) {
            windowSum += A[i];
        }
        int maxSum = windowSum;

        // Slide: remove A[B-i] from left portion, add A[n-i] from right
        for (int i = 1; i <= B; i++) {
            windowSum = windowSum - A[B - i] + A[n - i];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }
}

// ============================================================
// Approach 3: Best (Same sliding window, cleanest form)
// Time: O(B) | Space: O(1)
// ============================================================
class Best {
    public static int pickFromBothSides(int[] A, int B) {
        int n = A.length;

        // Start: take all B from left
        int curr = 0;
        for (int i = 0; i < B; i++) {
            curr += A[i];
        }
        int result = curr;

        // Swap one left element for one right element, B times
        for (int i = 1; i <= B; i++) {
            // Remove A[B-i] (rightmost of current left picks)
            // Add A[n-i] (i-th element from the right end)
            curr += A[n - i] - A[B - i];
            result = Math.max(result, curr);
        }
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Pick from Both Sides ===\n");

        int[][] arrays = {
            {5, -2, 3, 1, 2},
            {1, 2},
            {-1, -2, -3, -4, -5},
            {1, 2, 3, 4, 5},
            {10, 1, 1, 1, 1, 1, 10}
        };
        int[] Bs       = {3,  1,  2,  2,  2};
        int[] expected = {8,  2, -3,  9, 20};

        for (int t = 0; t < arrays.length; t++) {
            int[] A = arrays[t];
            int B   = Bs[t];

            int bruteResult   = BruteForce.pickFromBothSides(A, B);
            int optimalResult = Optimal.pickFromBothSides(A, B);
            int bestResult    = Best.pickFromBothSides(A, B);
            boolean pass = bruteResult == expected[t]
                        && optimalResult == expected[t]
                        && bestResult == expected[t];

            System.out.println("Input:    " + Arrays.toString(A) + ", B=" + B);
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Best:     " + bestResult);
            System.out.println("Expected: " + expected[t] + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
