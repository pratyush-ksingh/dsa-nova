/**
 * Problem: Left Rotate Array by One
 * Difficulty: EASY | XP: 10
 *
 * Given an array, rotate it to the left by one position.
 * [1,2,3,4,5] -> [2,3,4,5,1]
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Extra Array)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    public static void rotateLeft(int[] arr) {
        int n = arr.length;
        if (n <= 1) return;

        int[] temp = new int[n];
        for (int i = 1; i < n; i++) {
            temp[i - 1] = arr[i];
        }
        temp[n - 1] = arr[0];

        System.arraycopy(temp, 0, arr, 0, n);
    }
}

// ============================================================
// Approach 2: Optimal (In-Place Shift)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static void rotateLeft(int[] arr) {
        int n = arr.length;
        if (n <= 1) return;

        int first = arr[0];
        for (int i = 0; i < n - 1; i++) {
            arr[i] = arr[i + 1];
        }
        arr[n - 1] = first;
    }
}

// ============================================================
// Approach 3: Best (System.arraycopy -- same idea, cleaner)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static void rotateLeft(int[] arr) {
        int n = arr.length;
        if (n <= 1) return;

        int first = arr[0];
        System.arraycopy(arr, 1, arr, 0, n - 1);
        arr[n - 1] = first;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Left Rotate Array by One ===\n");

        int[][] testCases = {
            {1, 2, 3, 4, 5},
            {7},
            {3, 9},
            {5, 5, 5, 5},
            {10, 20, 30}
        };
        int[][] expected = {
            {2, 3, 4, 5, 1},
            {7},
            {9, 3},
            {5, 5, 5, 5},
            {20, 30, 10}
        };

        for (int t = 0; t < testCases.length; t++) {
            int[] a1 = testCases[t].clone();
            int[] a2 = testCases[t].clone();
            int[] a3 = testCases[t].clone();

            BruteForce.rotateLeft(a1);
            Optimal.rotateLeft(a2);
            Best.rotateLeft(a3);

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
