/**
 * Problem: Recursive Bubble Sort
 * Difficulty: EASY | XP: 10
 *
 * Implement bubble sort using recursion instead of nested loops.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Iterative Bubble Sort -- for comparison)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
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
// Approach 2: Optimal (Recursive Bubble Sort)
// Time: O(n^2) | Space: O(n) recursion stack
// ============================================================
class Optimal {
    public static void bubbleSort(int[] arr, int n) {
        // Base case: single element or empty
        if (n <= 1) return;

        // One pass: bubble the largest to arr[n-1]
        for (int j = 0; j < n - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }

        // Recurse on the unsorted portion
        bubbleSort(arr, n - 1);
    }
}

// ============================================================
// Approach 3: Best (Recursive Bubble Sort with Early Termination)
// Time: O(n^2) worst, O(n) best | Space: O(n) recursion stack
// ============================================================
class Best {
    public static void bubbleSort(int[] arr, int n) {
        // Base case
        if (n <= 1) return;

        boolean swapped = false;

        // One pass with swap tracking
        for (int j = 0; j < n - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = true;
            }
        }

        // If no swaps, array is already sorted
        if (!swapped) return;

        // Recurse on remaining portion
        bubbleSort(arr, n - 1);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Recursive Bubble Sort ===\n");

        int[][] testCases = {
            {64, 34, 25, 12, 22, 11, 90},
            {5, 1, 4, 2, 8},
            {1, 2, 3},
            {3, 2, 1},
            {5},
            {2, 1},
            {3, 3, 3},
            {-5, -1, -3}
        };
        int[][] expected = {
            {11, 12, 22, 25, 34, 64, 90},
            {1, 2, 4, 5, 8},
            {1, 2, 3},
            {1, 2, 3},
            {5},
            {1, 2},
            {3, 3, 3},
            {-5, -3, -1}
        };

        for (int t = 0; t < testCases.length; t++) {
            int[] a1 = testCases[t].clone();
            int[] a2 = testCases[t].clone();
            int[] a3 = testCases[t].clone();

            BruteForce.bubbleSort(a1);
            Optimal.bubbleSort(a2, a2.length);
            Best.bubbleSort(a3, a3.length);

            boolean pass = Arrays.equals(a1, expected[t])
                        && Arrays.equals(a2, expected[t])
                        && Arrays.equals(a3, expected[t]);

            System.out.println("Input:    " + Arrays.toString(testCases[t]));
            System.out.println("Brute:    " + Arrays.toString(a1));
            System.out.println("Optimal:  " + Arrays.toString(a2));
            System.out.println("Best:     " + Arrays.toString(a3));
            System.out.println("Expected: " + Arrays.toString(expected[t]));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
