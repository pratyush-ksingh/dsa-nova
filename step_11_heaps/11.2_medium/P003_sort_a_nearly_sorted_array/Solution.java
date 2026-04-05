/**
 * Problem: Sort a Nearly Sorted Array (K-Sorted Array)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array where each element is at most k positions away from
 * its sorted position, sort the array efficiently.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Regular Sort)
// Time: O(n log n) | Space: O(1) if in-place
// ============================================================
class BruteForce {
    public static int[] solve(int[] arr, int k) {
        Arrays.sort(arr);
        return arr;
    }
}

// ============================================================
// Approach 2: Optimal (Min-Heap of size k+1)
// Time: O(n log k) | Space: O(k)
// ============================================================
class Optimal {
    public static int[] solve(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n];

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // Add first k+1 elements to heap
        int limit = Math.min(k + 1, n);
        for (int i = 0; i < limit; i++) {
            minHeap.add(arr[i]);
        }

        int idx = 0;
        for (int i = k + 1; i < n; i++) {
            result[idx++] = minHeap.poll();
            minHeap.add(arr[i]);
        }

        // Drain remaining
        while (!minHeap.isEmpty()) {
            result[idx++] = minHeap.poll();
        }

        return result;
    }
}

// ============================================================
// Approach 3: Best (Same Min-Heap, in-place write-back)
// Time: O(n log k) | Space: O(k)
// ============================================================
class Best {
    public static int[] solve(int[] arr, int k) {
        int n = arr.length;
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        int limit = Math.min(k + 1, n);
        for (int i = 0; i < limit; i++) {
            minHeap.add(arr[i]);
        }

        int idx = 0;
        for (int i = k + 1; i < n; i++) {
            arr[idx++] = minHeap.poll();
            minHeap.add(arr[i]);
        }

        while (!minHeap.isEmpty()) {
            arr[idx++] = minHeap.poll();
        }

        return arr;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Sort a Nearly Sorted Array ===\n");

        Object[][] tests = {
            {new int[]{6, 5, 3, 2, 8, 10, 9}, 3, new int[]{2, 3, 5, 6, 8, 9, 10}},
            {new int[]{10, 9, 8, 7, 4, 70, 60, 50}, 4, new int[]{4, 7, 8, 9, 10, 50, 60, 70}},
            {new int[]{1, 2, 3, 4, 5}, 0, new int[]{1, 2, 3, 4, 5}},
            {new int[]{3, 1, 2}, 2, new int[]{1, 2, 3}},
            {new int[]{2, 1}, 1, new int[]{1, 2}},
            {new int[]{1}, 0, new int[]{1}},
        };

        for (Object[] t : tests) {
            int[] arr = (int[]) t[0];
            int k = (int) t[1];
            int[] expected = (int[]) t[2];

            int[] b = BruteForce.solve(arr.clone(), k);
            int[] o = Optimal.solve(arr.clone(), k);
            int[] h = Best.solve(arr.clone(), k);
            boolean pass = Arrays.equals(b, expected)
                        && Arrays.equals(o, expected)
                        && Arrays.equals(h, expected);

            System.out.println("Input:    arr=" + Arrays.toString(arr) + ", k=" + k);
            System.out.println("Brute:    " + Arrays.toString(b));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(h));
            System.out.println("Expected: " + Arrays.toString(expected));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
