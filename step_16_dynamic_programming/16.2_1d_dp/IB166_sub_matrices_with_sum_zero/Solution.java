/**
 * Problem: Sub Matrices with Sum Zero
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given an N x N matrix, count all sub-matrices whose elements sum to 0.
 *
 * Approach: Fix two row boundaries r1 and r2. Compress the 2D problem
 * to 1D by summing columns between rows r1 and r2 (columnSum[j]).
 * Then count subarrays with sum 0 in columnSum[] using prefix sum + hashmap.
 *
 * Time: O(N^3)  -- O(N^2) row pairs * O(N) hashmap scan per pair.
 *
 * Real-life use: Financial analysis (zero-sum regions), image processing.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(N^5)  |  Space: O(1)
    // Enumerate all (r1, r2, c1, c2) combinations and sum each sub-matrix.
    // ============================================================
    public static int bruteForce(int[][] matrix) {
        int n = matrix.length;
        int count = 0;
        for (int r1 = 0; r1 < n; r1++) {
            for (int r2 = r1; r2 < n; r2++) {
                for (int c1 = 0; c1 < n; c1++) {
                    for (int c2 = c1; c2 < n; c2++) {
                        int sum = 0;
                        for (int r = r1; r <= r2; r++)
                            for (int c = c1; c <= c2; c++)
                                sum += matrix[r][c];
                        if (sum == 0) count++;
                    }
                }
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(N^3)  |  Space: O(N)
    // Fix r1 and r2, compute column prefix sums, then use hashmap
    // to count subarrays with sum 0 in O(N).
    // ============================================================
    public static int optimal(int[][] matrix) {
        int n = matrix.length;
        int count = 0;

        for (int r1 = 0; r1 < n; r1++) {
            int[] colSum = new int[n]; // colSum[j] = sum of matrix[r1..r2][j]
            for (int r2 = r1; r2 < n; r2++) {
                // Extend row range to r2
                for (int j = 0; j < n; j++) colSum[j] += matrix[r2][j];

                // Count subarrays with zero sum in colSum[]
                Map<Integer, Integer> prefixCount = new HashMap<>();
                prefixCount.put(0, 1);
                int prefixSum = 0;
                for (int j = 0; j < n; j++) {
                    prefixSum += colSum[j];
                    count += prefixCount.getOrDefault(prefixSum, 0);
                    prefixCount.merge(prefixSum, 1, Integer::sum);
                }
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(N^3)  |  Space: O(N)
    // Same as optimal but with cleaner variable naming and uses
    // 2D prefix sums to pre-compute column sums in O(N^2) setup.
    // ============================================================
    public static int best(int[][] matrix) {
        int n = matrix.length;
        // Precompute 2D prefix sums for column range sums
        int[][] prefix = new int[n + 1][n]; // prefix[i][j] = sum of col j from row 0 to i-1
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                prefix[i + 1][j] = prefix[i][j] + matrix[i][j];

        int count = 0;
        for (int r1 = 0; r1 < n; r1++) {
            for (int r2 = r1; r2 < n; r2++) {
                // colSum[j] = sum of matrix[r1..r2][j]
                Map<Integer, Integer> freq = new HashMap<>();
                freq.put(0, 1);
                int ps = 0;
                for (int j = 0; j < n; j++) {
                    int colVal = prefix[r2 + 1][j] - prefix[r1][j];
                    ps += colVal;
                    count += freq.getOrDefault(ps, 0);
                    freq.merge(ps, 1, Integer::sum);
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Sub Matrices with Sum Zero ===\n");

        int[][] m1 = {
            {1, -1},
            {-1,  1}
        };
        System.out.println("Test 1 (2x2 alternating, expected 5):");
        System.out.println("  Brute:   " + bruteForce(m1));
        System.out.println("  Optimal: " + optimal(m1));
        System.out.println("  Best:    " + best(m1));

        int[][] m2 = {
            { 1,  2, -3},
            {-1, -2,  3},
            { 0,  0,  0}
        };
        System.out.println("\nTest 2 (3x3):");
        System.out.println("  Brute:   " + bruteForce(m2));
        System.out.println("  Best:    " + best(m2));

        int[][] m3 = {{0}};
        System.out.println("\nTest 3 (single zero, expected 1):");
        System.out.println("  Best: " + best(m3));

        int[][] m4 = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        System.out.println("\nTest 4 (all zeros 3x3, expected 36):");
        System.out.println("  Brute: " + bruteForce(m4));
        System.out.println("  Best:  " + best(m4));
    }
}
