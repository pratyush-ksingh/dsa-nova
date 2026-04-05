/**
 * Problem: Merge Sort
 * Difficulty: MEDIUM | XP: 25
 *
 * Sort an array using the Merge Sort algorithm.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Built-in Sort (baseline)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        public static int[] sort(int[] arr) {
            int[] result = arr.clone();
            Arrays.sort(result);
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Classic Top-Down Merge Sort
    // Time: O(n log n)  |  Space: O(n)
    // Recursively divide and merge sorted halves.
    // ============================================================
    static class Optimal {
        public static int[] sort(int[] arr) {
            int[] result = arr.clone();
            if (result.length <= 1) return result;
            mergeSort(result, 0, result.length - 1);
            return result;
        }

        private static void mergeSort(int[] arr, int low, int high) {
            if (low >= high) return;

            int mid = low + (high - low) / 2;
            mergeSort(arr, low, mid);
            mergeSort(arr, mid + 1, high);
            merge(arr, low, mid, high);
        }

        private static void merge(int[] arr, int low, int mid, int high) {
            int[] temp = new int[high - low + 1];
            int i = low, j = mid + 1, k = 0;

            while (i <= mid && j <= high) {
                if (arr[i] <= arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }

            while (i <= mid) temp[k++] = arr[i++];
            while (j <= high) temp[k++] = arr[j++];

            System.arraycopy(temp, 0, arr, low, temp.length);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Bottom-Up Iterative Merge Sort
    // Time: O(n log n)  |  Space: O(n) but no recursion stack
    // Merge sub-arrays of increasing width: 1, 2, 4, 8, ...
    // ============================================================
    static class Best {
        public static int[] sort(int[] arr) {
            int[] result = arr.clone();
            int n = result.length;
            if (n <= 1) return result;

            int[] temp = new int[n];

            for (int width = 1; width < n; width *= 2) {
                for (int left = 0; left < n; left += 2 * width) {
                    int mid = Math.min(left + width - 1, n - 1);
                    int right = Math.min(left + 2 * width - 1, n - 1);
                    merge(result, temp, left, mid, right);
                }
            }
            return result;
        }

        private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
            int i = left, j = mid + 1, k = left;

            while (i <= mid && j <= right) {
                if (arr[i] <= arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }
            while (i <= mid) temp[k++] = arr[i++];
            while (j <= right) temp[k++] = arr[j++];

            System.arraycopy(temp, left, arr, left, right - left + 1);
        }
    }

    public static void main(String[] args) {
        int[][] tests = {
            {5, 3, 8, 4, 2},
            {38, 27, 43, 3, 9, 82, 10},
            {1},
            {2, 1},
            {1, 2, 3, 4, 5}
        };

        System.out.println("=== Merge Sort ===");
        for (int[] test : tests) {
            System.out.println("Input:  " + Arrays.toString(test));
            System.out.println("  Brute:   " + Arrays.toString(BruteForce.sort(test)));
            System.out.println("  Optimal: " + Arrays.toString(Optimal.sort(test)));
            System.out.println("  Best:    " + Arrays.toString(Best.sort(test)));
        }
    }
}
