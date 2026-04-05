import java.util.*;

/**
 * Problem: Noble Integer (InterviewBit)
 * Difficulty: EASY | XP: 10
 *
 * Find if there exists an integer x in the array such that the
 * count of numbers strictly greater than x equals x.
 * Return 1 if exists, -1 otherwise.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check Each Element
    // Time: O(n^2)  |  Space: O(1)
    // For each element, count how many are strictly greater.
    // ============================================================
    static class BruteForce {
        public static int solve(int[] A) {
            int n = A.length;
            for (int i = 0; i < n; i++) {
                int count = 0;
                for (int j = 0; j < n; j++) {
                    if (A[j] > A[i]) {
                        count++;
                    }
                }
                if (count == A[i]) {
                    return 1;
                }
            }
            return -1;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sort + Index-Based Count
    // Time: O(n log n)  |  Space: O(1) with in-place sort
    // After sorting, count_greater(A[i]) = n - 1 - i (last occurrence).
    // ============================================================
    static class Optimal {
        public static int solve(int[] A) {
            Arrays.sort(A);
            int n = A.length;

            for (int i = 0; i < n; i++) {
                // Skip duplicates: only process the last occurrence
                if (i < n - 1 && A[i] == A[i + 1]) {
                    continue;
                }
                // Count of elements strictly greater = n - 1 - i
                int countGreater = n - 1 - i;
                if (A[i] == countGreater) {
                    return 1;
                }
            }
            return -1;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Sort + Clean Single Pass
    // Time: O(n log n)  |  Space: O(1)
    // Same logic, explicitly handles edge cases.
    // ============================================================
    static class Best {
        public static int solve(int[] A) {
            Arrays.sort(A);
            int n = A.length;

            for (int i = 0; i < n; i++) {
                // Skip to last occurrence of this value
                if (i < n - 1 && A[i] == A[i + 1]) {
                    continue;
                }
                // Elements strictly greater than A[i]: everything after index i
                int countGreater = n - 1 - i;
                if (A[i] >= 0 && A[i] == countGreater) {
                    return 1;
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Noble Integer ===");

        int[] test1 = {3, 2, 1, 3};    // Noble: 2 (count > 2 = 2)
        int[] test2 = {5, 6, 2};        // Noble: 2 (count > 2 = 2)
        int[] test3 = {1, 1, 1};        // No noble integer
        int[] test4 = {0};              // Noble: 0 (count > 0 = 0)
        int[] test5 = {7, 3, 16, 10};   // Noble: 3 (count > 3 = 3)
        int[] test6 = {0, 1, 2};        // Noble: 1 (count > 1 = 1)

        System.out.println("--- Brute Force ---");
        System.out.println(BruteForce.solve(test1));  // 1
        System.out.println(BruteForce.solve(test2));  // 1
        System.out.println(BruteForce.solve(test3));  // -1

        System.out.println("--- Optimal ---");
        System.out.println(Optimal.solve(new int[]{3, 2, 1, 3}));   // 1
        System.out.println(Optimal.solve(new int[]{5, 6, 2}));      // 1
        System.out.println(Optimal.solve(new int[]{1, 1, 1}));      // -1

        System.out.println("--- Best ---");
        System.out.println(Best.solve(new int[]{0}));               // 1
        System.out.println(Best.solve(new int[]{7, 3, 16, 10}));    // 1
        System.out.println(Best.solve(new int[]{0, 1, 2}));         // 1
    }
}
