/**
 * Problem: Recursive Insertion Sort
 * Difficulty: EASY | XP: 10
 *
 * Sort an array using recursive insertion sort.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative Insertion Sort
    // Time: O(n^2) worst, O(n) best  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        public static int[] sort(int[] arr) {
            int[] result = arr.clone();
            int n = result.length;
            for (int i = 1; i < n; i++) {
                int key = result[i];
                int j = i - 1;
                while (j >= 0 && result[j] > key) {
                    result[j + 1] = result[j];
                    j--;
                }
                result[j + 1] = key;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Insertion Sort
    // Time: O(n^2) worst, O(n) best  |  Space: O(n) stack
    // Recursively sort first n-1 elements, then insert n-th.
    // ============================================================
    static class Optimal {
        public static int[] sort(int[] arr) {
            int[] result = arr.clone();
            recursiveInsertionSort(result, result.length);
            return result;
        }

        private static void recursiveInsertionSort(int[] arr, int n) {
            if (n <= 1) return;

            // Sort first n-1 elements
            recursiveInsertionSort(arr, n - 1);

            // Insert arr[n-1] into sorted arr[0..n-2]
            int key = arr[n - 1];
            int j = n - 2;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Fully Recursive (insert step also recursive)
    // Time: O(n^2)  |  Space: O(n) for sort + O(n) for insert
    // No loops at all -- pure recursion.
    // ============================================================
    static class Best {
        public static int[] sort(int[] arr) {
            int[] result = arr.clone();
            recursiveInsertionSort(result, result.length);
            return result;
        }

        private static void recursiveInsertionSort(int[] arr, int n) {
            if (n <= 1) return;

            recursiveInsertionSort(arr, n - 1);
            recursiveInsert(arr, n - 1);
        }

        // Insert arr[pos] into sorted arr[0..pos-1]
        private static void recursiveInsert(int[] arr, int pos) {
            if (pos == 0 || arr[pos - 1] <= arr[pos]) return;

            // Swap and recurse
            int temp = arr[pos];
            arr[pos] = arr[pos - 1];
            arr[pos - 1] = temp;
            recursiveInsert(arr, pos - 1);
        }
    }

    public static void main(String[] args) {
        int[][] tests = {
            {12, 11, 13, 5, 6},
            {5, 4, 3, 2, 1},
            {1},
            {2, 1},
            {1, 2, 3, 4, 5},
            {-3, 4, -1, 0, 2}
        };

        System.out.println("=== Recursive Insertion Sort ===");
        for (int[] test : tests) {
            System.out.println("Input:  " + Arrays.toString(test));
            System.out.println("  Brute:   " + Arrays.toString(BruteForce.sort(test)));
            System.out.println("  Optimal: " + Arrays.toString(Optimal.sort(test)));
            System.out.println("  Best:    " + Arrays.toString(Best.sort(test)));
        }
    }
}
