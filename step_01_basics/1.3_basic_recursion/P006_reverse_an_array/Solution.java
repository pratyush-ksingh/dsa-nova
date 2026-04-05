/**
 * Problem: Reverse an Array
 * Difficulty: EASY | XP: 10
 *
 * Reverse an array in-place. Show iterative and recursive two-pointer approaches.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (New Array -- Extra Space)
// Time: O(n) | Space: O(n)
// ============================================================
class BruteForce {
    /**
     * Create a new array, fill it backwards, then copy back.
     */
    public static int[] reverse(int[] arr) {
        int n = arr.length;
        int[] reversed = new int[n];
        for (int i = 0; i < n; i++) {
            reversed[i] = arr[n - 1 - i];
        }
        return reversed;
    }
}

// ============================================================
// Approach 2: Optimal (Iterative Two-Pointer Swap)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    /**
     * Two pointers from both ends, swap and converge.
     * Modifies array in-place.
     */
    public static void reverse(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            // Swap
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}

// ============================================================
// Approach 3: Best (Recursive Two-Pointer Swap)
// Time: O(n) | Space: O(n/2) = O(n) recursion stack
// ============================================================
class Best {
    /**
     * Recursive version of two-pointer swap.
     * Base case: left >= right (pointers crossed or met).
     */
    public static void reverse(int[] arr) {
        helper(arr, 0, arr.length - 1);
    }

    private static void helper(int[] arr, int left, int right) {
        if (left >= right) return; // base case

        // Swap
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;

        // Recurse on inner subarray
        helper(arr, left + 1, right - 1);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Reverse an Array ===\n");

        int[][] inputs = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4},
            {1},
            {},
            {1, 2},
            {-3, 0, 5, -1, 7},
        };
        int[][] expected = {
            {5, 4, 3, 2, 1},
            {4, 3, 2, 1},
            {1},
            {},
            {2, 1},
            {7, -1, 5, 0, -3},
        };

        for (int t = 0; t < inputs.length; t++) {
            // Brute force (returns new array)
            int[] b = BruteForce.reverse(inputs[t].clone());

            // Optimal (in-place)
            int[] oArr = inputs[t].clone();
            Optimal.reverse(oArr);

            // Best (recursive in-place)
            int[] rArr = inputs[t].clone();
            Best.reverse(rArr);

            boolean pass = Arrays.equals(b, expected[t])
                        && Arrays.equals(oArr, expected[t])
                        && Arrays.equals(rArr, expected[t]);

            System.out.println("Input:     " + Arrays.toString(inputs[t]));
            System.out.println("Brute:     " + Arrays.toString(b));
            System.out.println("Iterative: " + Arrays.toString(oArr));
            System.out.println("Recursive: " + Arrays.toString(rArr));
            System.out.println("Expected:  " + Arrays.toString(expected[t]));
            System.out.println("Pass:      " + pass);
            System.out.println();
        }
    }
}
