/**
 * Problem: Insertion Sort
 * Difficulty: EASY | XP: 10
 *
 * Key Insight: Save the key, shift larger elements right, place key in gap.
 * Best case O(n) for nearly sorted arrays.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Swap-Based Insertion
    // Time: O(n^2) worst/avg, O(n) best  |  Space: O(1)
    //
    // Repeatedly swap element with its left neighbor until positioned.
    // ============================================================
    public static void bruteForce(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int j = i;
            while (j > 0 && arr[j] < arr[j - 1]) {
                // Swap
                int temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
                j--;
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Key-and-Shift
    // Time: O(n^2) worst/avg, O(n) best  |  Space: O(1)
    //
    // Save key, shift larger elements right, place key once.
    // Fewer writes than swap-based approach.
    // ============================================================
    public static void optimal(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            // Shift elements > key to the right
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            // Place key in the gap
            arr[j + 1] = key;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Binary Search + Shift
    // Time: O(n^2) shifts, O(n log n) comparisons  |  Space: O(1)
    //
    // Use binary search to find insertion position, then shift.
    // Saves comparisons when comparison is expensive.
    // ============================================================
    public static void best(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];

            // Binary search for correct position in arr[0..i-1]
            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (arr[mid] <= key) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }
            // lo = insertion position

            // Shift elements arr[lo..i-1] one position right
            for (int j = i; j > lo; j--) {
                arr[j] = arr[j - 1];
            }

            arr[lo] = key;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Insertion Sort ===");

        int[][] tests = {
            {12, 11, 13, 5, 6},
            {4, 3, 2, 1},
            {1, 2, 3, 4},
            {5}
        };

        for (int[] t : tests) {
            int[] a = t.clone(), b = t.clone(), c = t.clone();
            System.out.printf("Input: %s%n", Arrays.toString(t));

            bruteForce(a);
            System.out.printf("  Brute:   %s%n", Arrays.toString(a));

            optimal(b);
            System.out.printf("  Optimal: %s%n", Arrays.toString(b));

            best(c);
            System.out.printf("  Best:    %s%n", Arrays.toString(c));
            System.out.println();
        }
    }
}
