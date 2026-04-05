/**
 * Problem: Second Largest Element
 * Difficulty: EASY | XP: 10
 *
 * Given an array of integers, find the second largest distinct element.
 * Return -1 if it does not exist.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Sort)
// Time: O(n log n) | Space: O(1) for in-place sort
// ============================================================
class BruteForce {
    public static int secondLargest(int[] arr) {
        int n = arr.length;
        if (n < 2) return -1;

        Arrays.sort(arr);
        int largest = arr[n - 1];

        // Walk backwards to find first element strictly less than largest
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] != largest) {
                return arr[i];
            }
        }
        return -1; // All elements are the same
    }
}

// ============================================================
// Approach 2: Optimal (Two-Pass Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int secondLargest(int[] arr) {
        int n = arr.length;
        if (n < 2) return -1;

        // Pass 1: Find the largest
        int largest = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] > largest) {
                largest = arr[i];
            }
        }

        // Pass 2: Find the largest element strictly less than 'largest'
        int second = -1;
        for (int i = 0; i < n; i++) {
            if (arr[i] != largest && arr[i] > second) {
                second = arr[i];
            }
        }
        return second;
    }
}

// ============================================================
// Approach 3: Best (Single-Pass with Two Variables)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int secondLargest(int[] arr) {
        int n = arr.length;
        if (n < 2) return -1;

        int first = -1, second = -1;

        for (int i = 0; i < n; i++) {
            if (arr[i] > first) {
                second = first;   // Old champion becomes silver
                first = arr[i];   // New champion
            } else if (arr[i] > second && arr[i] != first) {
                second = arr[i];  // New silver medalist
            }
        }
        return second;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Second Largest Element ===\n");

        int[][] testCases = {
            {12, 35, 1, 10, 34, 1},
            {10, 10, 10},
            {5, 10},
            {7},
            {1, 2, 3, 4, 5},
            {5, 5, 5, 3}
        };
        int[] expected = {34, -1, 5, -1, 4, 3};

        for (int t = 0; t < testCases.length; t++) {
            int[] arr = testCases[t];
            int bruteResult = BruteForce.secondLargest(arr.clone());
            int optimalResult = Optimal.secondLargest(arr.clone());
            int bestResult = Best.secondLargest(arr.clone());

            System.out.println("Input:    " + Arrays.toString(arr));
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Best:     " + bestResult);
            System.out.println("Expected: " + expected[t]);
            boolean pass = (bruteResult == expected[t])
                        && (optimalResult == expected[t])
                        && (bestResult == expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
