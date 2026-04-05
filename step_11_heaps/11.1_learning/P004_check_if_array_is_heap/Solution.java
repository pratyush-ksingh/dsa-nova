/**
 * Problem: Check if Array is Heap
 * Difficulty: EASY | XP: 10
 *
 * Given an array, determine if it represents a valid min-heap, max-heap, or neither.
 * For 0-based index: parent i has children at 2i+1 and 2i+2.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // Check if array is a valid Min-Heap
    // Time: O(n) | Space: O(1)
    // ============================================================
    public static boolean isMinHeap(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n / 2; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n && arr[i] > arr[left]) return false;
            if (right < n && arr[i] > arr[right]) return false;
        }
        return true;
    }

    // ============================================================
    // Check if array is a valid Max-Heap
    // Time: O(n) | Space: O(1)
    // ============================================================
    public static boolean isMaxHeap(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n / 2; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n && arr[i] < arr[left]) return false;
            if (right < n && arr[i] < arr[right]) return false;
        }
        return true;
    }

    // ============================================================
    // Combined: returns "Min-Heap", "Max-Heap", "Both", or "Neither"
    // Time: O(n) | Space: O(1)
    // ============================================================
    public static String checkHeap(int[] arr) {
        boolean min = isMinHeap(arr);
        boolean max = isMaxHeap(arr);

        if (min && max) return "Both";      // e.g., all elements equal or single element
        if (min) return "Min-Heap";
        if (max) return "Max-Heap";
        return "Neither";
    }

    public static void main(String[] args) {
        System.out.println("=== Check if Array is Heap ===\n");

        int[][] testCases = {
            {10, 20, 30, 25, 35},       // Min-Heap
            {90, 70, 60, 50, 40},       // Max-Heap
            {10, 50, 20, 55, 5},        // Neither
            {5, 5, 5, 5},              // Both
            {42},                       // Both (single element)
            {1, 2},                     // Min-Heap
            {2, 1},                     // Max-Heap
            {1, 3, 2, 7, 5, 4, 6},     // Min-Heap
        };
        String[] expected = {
            "Min-Heap", "Max-Heap", "Neither", "Both",
            "Both", "Min-Heap", "Max-Heap", "Min-Heap"
        };

        for (int t = 0; t < testCases.length; t++) {
            String result = checkHeap(testCases[t]);
            boolean pass = result.equals(expected[t]);
            System.out.println("arr = " + Arrays.toString(testCases[t]));
            System.out.println("  Result: " + result + " | Expected: " + expected[t] + " | Pass: " + pass + "\n");
        }
    }
}
