/**
 * Problem: Check if Array is Sorted
 * Difficulty: EASY | XP: 10
 *
 * Given an array, check if it is sorted in non-decreasing order.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Check All Pairs)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static boolean isSorted(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] > arr[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

// ============================================================
// Approach 2: Optimal (Single Pass Adjacent Comparison)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Check if Array is Sorted ===\n");

        int[][] testCases = {
            {1, 2, 3, 4, 5},
            {1, 3, 2, 4, 5},
            {5, 5, 5, 5},
            {42},
            {2, 1},
            {1, 2, 3, 1},
            {5, 4, 3, 2, 1},
            {-3, -1, 0, 4}
        };
        boolean[] expected = {true, false, true, true, false, false, false, true};

        for (int t = 0; t < testCases.length; t++) {
            int[] arr = testCases[t];
            boolean bruteResult = BruteForce.isSorted(arr);
            boolean optimalResult = Optimal.isSorted(arr);

            System.out.println("Input:    " + Arrays.toString(arr));
            System.out.println("  Brute:    " + bruteResult);
            System.out.println("  Optimal:  " + optimalResult);
            System.out.println("  Expected: " + expected[t]);
            System.out.println("  Pass:     " + (bruteResult == expected[t] && optimalResult == expected[t]));
            System.out.println();
        }
    }
}
