/**
 * Problem: Bubble Sort
 * Difficulty: EASY | XP: 10
 *
 * Sort an array using bubble sort. Implement both basic and optimized versions.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Basic Bubble Sort (No Optimization)
// Time: O(n^2) always | Space: O(1)
// ============================================================
class BruteForce {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}

// ============================================================
// Approach 2: Optimized Bubble Sort (Early Termination)
// Time: O(n^2) worst, O(n) best | Space: O(1)
// ============================================================
class Optimal {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break; // Array is already sorted
        }
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Bubble Sort ===\n");

        int[][] testCases = {
            {64, 34, 25, 12, 22, 11, 90},
            {5, 1, 4, 2, 8},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {2, 1},
            {42},
            {3, 3, 3},
            {-2, 3, -1, 5},
            {4, 2, 4, 1, 2}
        };

        int[][] expected = {
            {11, 12, 22, 25, 34, 64, 90},
            {1, 2, 4, 5, 8},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {1, 2},
            {42},
            {3, 3, 3},
            {-2, -1, 3, 5},
            {1, 2, 2, 4, 4}
        };

        for (int t = 0; t < testCases.length; t++) {
            int[] arr1 = testCases[t].clone();
            int[] arr2 = testCases[t].clone();

            BruteForce.bubbleSort(arr1);
            Optimal.bubbleSort(arr2);

            boolean pass = Arrays.equals(arr1, expected[t]) && Arrays.equals(arr2, expected[t]);

            System.out.println("Input:    " + Arrays.toString(testCases[t]));
            System.out.println("  Basic:    " + Arrays.toString(arr1));
            System.out.println("  Optimal:  " + Arrays.toString(arr2));
            System.out.println("  Expected: " + Arrays.toString(expected[t]));
            System.out.println("  Pass:     " + pass);
            System.out.println();
        }
    }
}
