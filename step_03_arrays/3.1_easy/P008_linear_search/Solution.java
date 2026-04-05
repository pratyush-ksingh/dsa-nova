/**
 * Problem: Linear Search
 * Difficulty: EASY | XP: 10
 *
 * Find the index of a target in an unsorted array. Return -1 if not found.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Basic Linear Search)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int search(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}

// ============================================================
// Approach 2: Optimal (Sentinel Linear Search)
// Time: O(n) | Space: O(1) -- fewer comparisons per iteration
// ============================================================
class Optimal {
    public static int search(int[] arr, int target) {
        int n = arr.length;
        if (n == 0) return -1;

        // Save last element and place sentinel
        int last = arr[n - 1];
        arr[n - 1] = target;

        int i = 0;
        while (arr[i] != target) {
            i++;
        }

        // Restore last element
        arr[n - 1] = last;

        // Check if we found a real match or just the sentinel
        if (i < n - 1 || last == target) {
            return i;
        }
        return -1;
    }
}

// ============================================================
// Approach 3: Best (Search from Both Ends)
// Time: O(n) worst, ~n/2 avg | Space: O(1)
// ============================================================
class Best {
    public static int search(int[] arr, int target) {
        int left = 0, right = arr.length - 1;

        while (left <= right) {
            if (arr[left] == target) return left;
            if (arr[right] == target) return right;
            left++;
            right--;
        }
        return -1;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Linear Search ===\n");

        int[][] arrays = {
            {4, 1, 7, 3, 9},
            {4, 1, 7, 3, 9},
            {10},
            {10},
            {2, 2, 2},
            {5, 1, 2}
        };
        int[] targets = {7, 5, 10, 3, 2, 5};
        int[] expected = {2, -1, 0, -1, 0, 0};

        for (int t = 0; t < arrays.length; t++) {
            int[] arr = arrays[t];
            int bruteResult = BruteForce.search(arr.clone(), targets[t]);
            int optimalResult = Optimal.search(arr.clone(), targets[t]);
            int bestResult = Best.search(arr.clone(), targets[t]);

            boolean pass = bruteResult == expected[t]
                        && optimalResult == expected[t]
                        && bestResult == expected[t];

            System.out.println("Input:    " + Arrays.toString(arr) + ", target=" + targets[t]);
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Best:     " + bestResult);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
