/**
 * Problem: Convert Min Heap to Max Heap
 * Difficulty: EASY | XP: 10
 *
 * Given an array representing a min-heap, convert it to a max-heap in-place.
 * Uses bottom-up build max-heap (Floyd's algorithm).
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // Max-Heapify: sift down node at index i
    // ============================================================
    private static void maxHeapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            maxHeapify(arr, n, largest);
        }
    }

    // ============================================================
    // Approach 1: Bottom-Up Build Max-Heap (Optimal)
    // Time: O(n) | Space: O(log n) recursion stack
    // ============================================================
    public static void convertToMaxHeap(int[] arr) {
        int n = arr.length;
        // Start from last internal node and heapify down
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapify(arr, n, i);
        }
    }

    // ============================================================
    // Approach 2: Iterative Max-Heapify (no recursion stack)
    // Time: O(n) | Space: O(1)
    // ============================================================
    private static void maxHeapifyIterative(int[] arr, int n, int i) {
        while (true) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n && arr[left] > arr[largest]) largest = left;
            if (right < n && arr[right] > arr[largest]) largest = right;

            if (largest == i) break;

            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            i = largest;
        }
    }

    public static void convertToMaxHeapIterative(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapifyIterative(arr, n, i);
        }
    }

    // ============================================================
    // Helper: verify max-heap property
    // ============================================================
    private static boolean isMaxHeap(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n / 2; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < n && arr[i] < arr[left]) return false;
            if (right < n && arr[i] < arr[right]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Convert Min Heap to Max Heap ===\n");

        int[][] testCases = {
            {1, 2, 3, 4, 5},
            {1, 5, 6, 9, 10, 8},
            {1, 2, 3},
            {7},
            {1, 1, 1, 1, 1},
        };

        for (int[] tc : testCases) {
            // Test recursive approach
            int[] arr1 = tc.clone();
            System.out.println("Min-Heap:  " + Arrays.toString(arr1));
            convertToMaxHeap(arr1);
            System.out.println("Max-Heap:  " + Arrays.toString(arr1));
            System.out.println("Valid Max: " + isMaxHeap(arr1));

            // Test iterative approach
            int[] arr2 = tc.clone();
            convertToMaxHeapIterative(arr2);
            System.out.println("Iterative: " + Arrays.toString(arr2));
            System.out.println("Valid Max: " + isMaxHeap(arr2) + "\n");
        }
    }
}
