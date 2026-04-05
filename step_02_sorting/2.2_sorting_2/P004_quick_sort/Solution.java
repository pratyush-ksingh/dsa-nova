/**
 * Problem: Quick Sort
 * Difficulty: MEDIUM | XP: 25
 * Source: Striver A2Z
 *
 * Sort an array in ascending order using Quick Sort.
 * Three variants: naive first-element pivot, Lomuto + random pivot, Hoare + random pivot.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;
import java.util.Random;

public class Solution {

    private static final Random RNG = new Random();

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Lomuto, first element as pivot
    // Time: O(n²) worst case  |  Space: O(n) stack worst case
    // ============================================================
    static class BruteForce {
        /**
         * Lomuto partition with pivot = first element (moved to end before partitioning).
         * Degrades to O(n^2) on sorted or reverse-sorted input because the pivot
         * is always the minimum (or maximum), producing maximally unbalanced splits.
         */
        public static void sort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                sort(arr, low, pi - 1);
                sort(arr, pi + 1, high);
            }
        }

        private static int partition(int[] arr, int low, int high) {
            // Move first element to end to use as pivot
            int tmp = arr[low]; arr[low] = arr[high]; arr[high] = tmp;
            int pivot = arr[high];
            int store = low;
            for (int i = low; i < high; i++) {
                if (arr[i] <= pivot) {
                    tmp = arr[store]; arr[store] = arr[i]; arr[i] = tmp;
                    store++;
                }
            }
            tmp = arr[store]; arr[store] = arr[high]; arr[high] = tmp;
            return store;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Lomuto + random pivot
    // Time: O(n log n) average  |  Space: O(log n) average stack
    // ============================================================
    static class Optimal {
        /**
         * Lomuto partition with a randomly selected pivot.
         * Randomisation breaks adversarial inputs (already sorted, all-equal, etc.)
         * and gives expected O(n log n) performance on any input.
         * Pivot is swapped to the high position before partitioning.
         */
        public static void sort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                sort(arr, low, pi - 1);
                sort(arr, pi + 1, high);
            }
        }

        private static int partition(int[] arr, int low, int high) {
            // Random pivot: swap a random element with high
            int randIdx = low + RNG.nextInt(high - low + 1);
            int tmp = arr[randIdx]; arr[randIdx] = arr[high]; arr[high] = tmp;
            int pivot = arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (arr[j] <= pivot) {
                    i++;
                    tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                }
            }
            tmp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = tmp;
            return i + 1;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Hoare partition + random pivot
    // Time: O(n log n) average  |  Space: O(log n) average stack
    // On average ~3x fewer swaps than Lomuto.
    // ============================================================
    static class Best {
        /**
         * Hoare partition scheme: two pointers start at the ends and move inward.
         * More efficient than Lomuto in practice (~3x fewer swaps on average).
         * IMPORTANT: partition() returns index j where split is [low..j] and [j+1..high].
         * The pivot is NOT guaranteed to be in its final position after partition.
         */
        public static void sort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                sort(arr, low, pi);
                sort(arr, pi + 1, high);
            }
        }

        private static int partition(int[] arr, int low, int high) {
            // Place random pivot at low
            int randIdx = low + RNG.nextInt(high - low + 1);
            int tmp = arr[randIdx]; arr[randIdx] = arr[low]; arr[low] = tmp;
            int pivot = arr[low];
            int i = low - 1;
            int j = high + 1;
            while (true) {
                do { i++; } while (arr[i] < pivot);
                do { j--; } while (arr[j] > pivot);
                if (i >= j) return j;
                tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Quick Sort ===");
        int[][] inputs = {
            {64, 34, 25, 12, 22, 11, 90},
            {5, 4, 3, 2, 1},
            {1, 2, 3, 4, 5},
            {1},
            {3, 3, 3, 1, 2}
        };

        for (int[] original : inputs) {
            int[] b  = Arrays.copyOf(original, original.length);
            int[] o  = Arrays.copyOf(original, original.length);
            int[] be = Arrays.copyOf(original, original.length);

            BruteForce.sort(b,  0, b.length  - 1);
            Optimal.sort(o,     0, o.length  - 1);
            Best.sort(be,       0, be.length - 1);

            boolean allMatch = Arrays.equals(b, o) && Arrays.equals(o, be);
            System.out.printf("Input: %-30s | Brute: %-30s | Match: %s%n",
                    Arrays.toString(original), Arrays.toString(b), allMatch ? "OK" : "FAIL");
        }
    }
}
