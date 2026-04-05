/**
 * Problem: Selection Sort
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Standard Selection Sort
    // Time: O(n^2)  |  Space: O(1)
    //
    // For each position i, find the minimum in arr[i..n-1]
    // and swap it into position i.
    // ============================================================
    static class BruteForce {
        public void selectionSort(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] < arr[minIdx]) {
                        minIdx = j;
                    }
                }
                // Swap arr[i] and arr[minIdx]
                if (minIdx != i) {
                    int temp = arr[i];
                    arr[i] = arr[minIdx];
                    arr[minIdx] = temp;
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Stable Selection Sort (shift-based)
    // Time: O(n^2)  |  Space: O(1)
    //
    // Instead of swapping (which breaks stability), shift elements
    // right and insert the minimum at position i. This preserves
    // the relative order of equal elements.
    // ============================================================
    static class Optimal {
        public void selectionSort(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                // Find minimum in arr[i..n-1]
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] < arr[minIdx]) {
                        minIdx = j;
                    }
                }
                // Save the minimum value
                int minVal = arr[minIdx];
                // Shift arr[i..minIdx-1] one position to the right
                for (int k = minIdx; k > i; k--) {
                    arr[k] = arr[k - 1];
                }
                // Place minimum at position i
                arr[i] = minVal;
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Double-Ended Selection Sort
    // Time: O(n^2)  |  Space: O(1)
    //
    // Each pass finds BOTH the min and max, placing the min at
    // the front and the max at the back. Halves the number of
    // passes (same asymptotic complexity, better constant).
    // ============================================================
    static class Best {
        public void selectionSort(int[] arr) {
            int left = 0;
            int right = arr.length - 1;

            while (left < right) {
                int minIdx = left;
                int maxIdx = left;

                // Find min and max in arr[left..right]
                for (int i = left; i <= right; i++) {
                    if (arr[i] < arr[minIdx]) minIdx = i;
                    if (arr[i] > arr[maxIdx]) maxIdx = i;
                }

                // Place minimum at left
                swap(arr, left, minIdx);

                // If the maximum was at 'left', it got swapped to minIdx
                if (maxIdx == left) {
                    maxIdx = minIdx;
                }

                // Place maximum at right
                swap(arr, right, maxIdx);

                left++;
                right--;
            }
        }

        private void swap(int[] arr, int i, int j) {
            if (i != j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }

    // Helper to print an array
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Selection Sort ===\n");

        int[][] tests = {
            {64, 25, 12, 22, 11},
            {5, 1, 4, 2, 8},
            {1, 2, 3},
            {3, 2, 1},
            {1},
            {}
        };

        for (int[] test : tests) {
            System.out.println("Input: " + arrayToString(test));

            // Brute Force
            int[] arr1 = test.clone();
            new BruteForce().selectionSort(arr1);
            System.out.println("Brute:   " + arrayToString(arr1));

            // Optimal (stable)
            int[] arr2 = test.clone();
            new Optimal().selectionSort(arr2);
            System.out.println("Optimal: " + arrayToString(arr2));

            // Best (double-ended)
            int[] arr3 = test.clone();
            new Best().selectionSort(arr3);
            System.out.println("Best:    " + arrayToString(arr3));

            System.out.println();
        }
    }
}
