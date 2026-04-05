/**
 * Problem: Number of Next Greater Elements to the Right
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of N integers, for each element find the count of
 * elements strictly greater than it that lie to its right.
 *
 * E.g., arr=[3,4,2,7,5,1] -> [3,2,2,0,0,0]
 *   arr[0]=3: {4,7,5} are greater -> count=3
 *   arr[1]=4: {7,5} -> count=2
 *   arr[2]=2: {7,5} -> count=2
 *   arr[3]=7: {} -> count=0
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1) extra
    //
    // For each element at index i, scan all elements to its right
    // and count those strictly greater than arr[i].
    // ============================================================
    public static int[] bruteForce(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] > arr[i]) count++;
            }
            result[i] = count;
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Modified Merge Sort
    // Time: O(n log n)  |  Space: O(n)
    //
    // During merge sort, when we merge left and right halves,
    // elements in the right half have higher original indices.
    // If a right-half element is greater than a left-half element,
    // it is a valid NGE for that left element. Count remaining right
    // elements at the moment a left element is placed.
    //
    // Key: All elements in right half originally appeared AFTER
    // all elements in left half, so they are valid "right" elements.
    // ============================================================
    static int[] resultGlobal;

    public static int[] optimal(int[] arr) {
        int n = arr.length;
        resultGlobal = new int[n];
        // Pair: {value, originalIndex}
        int[][] indexed = new int[n][2];
        for (int i = 0; i < n; i++) {
            indexed[i][0] = arr[i];
            indexed[i][1] = i;
        }
        mergeSort(indexed, 0, n - 1);
        return resultGlobal;
    }

    private static void mergeSort(int[][] arr, int l, int r) {
        if (l >= r) return;
        int mid = (l + r) / 2;
        mergeSort(arr, l, mid);
        mergeSort(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    private static void merge(int[][] arr, int l, int mid, int r) {
        int n1 = mid - l + 1;
        int n2 = r - mid;
        int[][] left = new int[n1][2];
        int[][] right = new int[n2][2];
        System.arraycopy(arr, l, left, 0, n1);
        System.arraycopy(arr, mid + 1, right, 0, n2);

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (left[i][0] < right[j][0]) {
                // right[j] > left[i]: all of right[j..n2-1] are >= right[j] > left[i]
                // They all appeared to the right of left[i] originally.
                resultGlobal[left[i][1]] += (n2 - j);
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }
        while (i < n1) arr[k++] = left[i++];
        while (j < n2) arr[k++] = right[j++];
    }

    // ============================================================
    // APPROACH 3: BEST -- BIT (Binary Indexed Tree / Fenwick Tree)
    // Time: O(n log n)  |  Space: O(max_val)
    //
    // Process from right to left. Maintain a frequency BIT over
    // (compressed) values. For each arr[i], query BIT for count
    // of elements in (arr[i], max] that are already inserted.
    // Then insert arr[i] into the BIT.
    // ============================================================
    static int[] bit;
    static int bitSize;

    public static int[] best(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];

        // Coordinate compression
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        // Remove duplicates and build rank map
        Map<Integer, Integer> rank = new HashMap<>();
        int r = 0;
        for (int v : sorted) {
            if (!rank.containsKey(v)) rank.put(v, ++r);
        }
        bitSize = r;
        bit = new int[bitSize + 2];

        for (int i = n - 1; i >= 0; i--) {
            int rk = rank.get(arr[i]);
            // Count of elements > arr[i] = total inserted - count of elements <= arr[i]
            int totalInserted = bitQuery(bitSize);
            int countLE = bitQuery(rk);
            result[i] = totalInserted - countLE;
            bitUpdate(rk);
        }
        return result;
    }

    private static void bitUpdate(int i) {
        for (; i <= bitSize; i += i & (-i)) bit[i]++;
    }

    private static int bitQuery(int i) {
        int s = 0;
        for (; i > 0; i -= i & (-i)) s += bit[i];
        return s;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Number of NGEs to Right ===\n");

        int[][][] tests = {
            {{3, 4, 2, 7, 5, 1}, {3, 2, 2, 0, 0, 0}},
            {{1, 2, 3, 4, 5},    {4, 3, 2, 1, 0}},
            {{5, 4, 3, 2, 1},    {0, 0, 0, 0, 0}},
            {{1},                {0}},
            {{3, 3, 3},          {0, 0, 0}},
            {{1, 3, 2, 4},       {3, 1, 1, 0}},
        };

        for (int[][] test : tests) {
            int[] arr = test[0];
            int[] expected = test[1];

            int[] b = bruteForce(arr.clone());
            int[] o = optimal(arr.clone());
            int[] r = best(arr.clone());

            boolean pass = Arrays.equals(b, expected)
                        && Arrays.equals(o, expected)
                        && Arrays.equals(r, expected);

            System.out.printf("arr=%s%n", Arrays.toString(arr));
            System.out.printf("  Expected: %s%n", Arrays.toString(expected));
            System.out.printf("  Brute:    %s%n", Arrays.toString(b));
            System.out.printf("  Optimal:  %s%n", Arrays.toString(o));
            System.out.printf("  Best:     %s  [%s]%n%n", Arrays.toString(r), pass ? "PASS" : "FAIL");
        }
    }
}
