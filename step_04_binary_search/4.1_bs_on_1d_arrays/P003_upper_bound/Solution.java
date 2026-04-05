/**
 * Problem: Upper Bound
 * Difficulty: EASY | XP: 10
 *
 * Find the smallest index i such that arr[i] > target in a sorted array.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int upperBound(int[] arr, int x) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > x) return i;
        }
        return arr.length;
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static int upperBound(int[] arr, int x) {
        int low = 0, high = arr.length - 1;
        int ans = arr.length; // default: no element > x

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] > x) {
                ans = mid;       // candidate found, search left for smaller index
                high = mid - 1;
            } else {
                low = mid + 1;   // arr[mid] <= x, search right
            }
        }
        return ans;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Upper Bound ===\n");

        int[] arr = {1, 2, 3, 5, 5, 5, 7};

        int[][] tests = {
            // {target, expected}
            {5, 6},   // first > 5 is 7 at index 6
            {4, 3},   // first > 4 is 5 at index 3
            {7, 7},   // no element > 7, return n=7
            {0, 0},   // all elements > 0, return 0
            {1, 1},   // first > 1 is 2 at index 1
            {6, 6},   // first > 6 is 7 at index 6
            {10, 7},  // no element > 10, return 7
        };

        boolean allPass = true;
        for (int[] t : tests) {
            int x = t[0], expected = t[1];
            int b = BruteForce.upperBound(arr, x);
            int o = Optimal.upperBound(arr, x);

            boolean pass = b == expected && o == expected;
            allPass &= pass;

            System.out.printf("arr=%s, x=%d | Brute=%d Optimal=%d | Expected=%d [%s]%n",
                Arrays.toString(arr), x, b, o, expected, pass ? "PASS" : "FAIL");
        }

        // Additional test: single element
        int[] single = {5};
        int b1 = BruteForce.upperBound(single, 5);
        int o1 = Optimal.upperBound(single, 5);
        boolean p1 = b1 == 1 && o1 == 1;
        allPass &= p1;
        System.out.printf("arr=[5], x=5 | Brute=%d Optimal=%d | Expected=1 [%s]%n", b1, o1, p1 ? "PASS" : "FAIL");

        int b2 = BruteForce.upperBound(single, 3);
        int o2 = Optimal.upperBound(single, 3);
        boolean p2 = b2 == 0 && o2 == 0;
        allPass &= p2;
        System.out.printf("arr=[5], x=3 | Brute=%d Optimal=%d | Expected=0 [%s]%n", b2, o2, p2 ? "PASS" : "FAIL");

        System.out.println("\nAll pass: " + allPass);
    }
}
