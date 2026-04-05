/**
 * Problem: Largest Element in Array
 * Difficulty: EASY | XP: 10
 *
 * Given an array of integers, find and return the largest element.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Sort)
// Time: O(n log n) | Space: O(1) for in-place sort
// ============================================================
class BruteForce {
    public static int largestElement(int[] arr) {
        Arrays.sort(arr);
        return arr[arr.length - 1];
    }
}

// ============================================================
// Approach 2: Optimal (Single Pass Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int largestElement(int[] arr) {
        int maxVal = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }
        return maxVal;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Largest Element in Array ===\n");

        int[][] testCases = {
            {3, 5, 7, 2, 8},
            {10},
            {5, 5, 5, 5},
            {1, 2, 3, 4, 5},
            {9, 7, 5, 3, 1}
        };
        int[] expected = {8, 10, 5, 5, 9};

        for (int t = 0; t < testCases.length; t++) {
            int[] arr = testCases[t].clone();
            int bruteResult = BruteForce.largestElement(testCases[t].clone());
            int optimalResult = Optimal.largestElement(arr);

            System.out.println("Input:    " + Arrays.toString(arr));
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + (bruteResult == expected[t] && optimalResult == expected[t]));
            System.out.println();
        }
    }
}
