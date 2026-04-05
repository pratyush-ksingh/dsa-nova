/**
 * Problem: Balance Array
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Find the number of "special" indices i such that if A[i] is removed,
 * the sum of elements at even positions == sum of elements at odd positions
 * (0-indexed, after the removal re-indexes remaining elements).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    /**
     * For each index i, simulate removal by summing even/odd positions of remaining array.
     * Real-life: Checking balance conditions when removing an element from a dataset.
     */
    public static int bruteForce(int[] A) {
        int n = A.length;
        int count = 0;
        for (int skip = 0; skip < n; skip++) {
            long evenSum = 0, oddSum = 0;
            int pos = 0;
            for (int i = 0; i < n; i++) {
                if (i == skip) continue;
                if (pos % 2 == 0) evenSum += A[i];
                else oddSum += A[i];
                pos++;
            }
            if (evenSum == oddSum) count++;
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Precompute prefix even/odd sums. For removing index i:
     *   - Elements before i keep their parity.
     *   - Elements after i flip their parity.
     * New evenSum = prefixEven[i] + suffixOdd[i+1]
     * New oddSum  = prefixOdd[i]  + suffixEven[i+1]
     * Real-life: Fast dataset rebalancing checks in distributed systems.
     */
    public static int optimal(int[] A) {
        int n = A.length;
        // Prefix sums: index in original array
        long[] prefixEven = new long[n + 1]; // sum of A[j] where j even and j < i
        long[] prefixOdd  = new long[n + 1]; // sum of A[j] where j odd  and j < i
        for (int i = 0; i < n; i++) {
            prefixEven[i + 1] = prefixEven[i] + (i % 2 == 0 ? A[i] : 0);
            prefixOdd[i + 1]  = prefixOdd[i]  + (i % 2 == 1 ? A[i] : 0);
        }
        long totalEven = prefixEven[n];
        long totalOdd  = prefixOdd[n];

        int count = 0;
        for (int i = 0; i < n; i++) {
            // Suffix sums (in original indices)
            long suffixEven = totalEven - prefixEven[i + 1];
            long suffixOdd  = totalOdd  - prefixOdd[i + 1];

            // After removing i:
            // - Elements left of i (0..i-1) keep parity
            // - Elements right of i (i+1..n-1) flip parity
            long newEven = prefixEven[i] + suffixOdd;  // left even + right (was odd -> now even)
            long newOdd  = prefixOdd[i]  + suffixEven; // left odd  + right (was even -> now odd)

            if (newEven == newOdd) count++;
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    /**
     * Instead of prefix arrays, maintain running sums while iterating.
     * leftEven, leftOdd track sums to the left; rightEven, rightOdd to the right.
     * Real-life: In-place streaming analytics for balance checking.
     */
    public static int best(int[] A) {
        int n = A.length;
        long rightEven = 0, rightOdd = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) rightEven += A[i];
            else rightOdd += A[i];
        }

        long leftEven = 0, leftOdd = 0;
        int count = 0;
        for (int i = 0; i < n; i++) {
            // Remove A[i] from right sums
            if (i % 2 == 0) rightEven -= A[i];
            else rightOdd -= A[i];

            // After removal of i: elements at i+1..n-1 shift parity
            long newEven = leftEven + rightOdd;
            long newOdd  = leftOdd  + rightEven;
            if (newEven == newOdd) count++;

            // Add A[i] to left sums (before moving to i+1)
            if (i % 2 == 0) leftEven += A[i];
            else leftOdd += A[i];
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Balance Array ===");

        int[][] tests = {
            {2, 1, 6, 4},
            {1, 2, 3},
            {1},
            {1, 1, 1},
        };
        int[] expected = {1, 1, 1, 3};

        for (int t = 0; t < tests.length; t++) {
            System.out.println("\nInput: " + Arrays.toString(tests[t]));
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:   " + bruteForce(tests[t]));
            System.out.println("Optimal: " + optimal(tests[t]));
            System.out.println("Best:    " + best(tests[t]));
        }
    }
}
