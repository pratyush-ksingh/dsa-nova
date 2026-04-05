import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// For each pair (i,j) with i<j, check if A[i] > A[j] and count inversions
// ============================================================
class BruteForce {
    public static long solve(long[] A) {
        int n = A.length;
        long count = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (A[i] > A[j]) count++;
        return count;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Merge Sort
// Time: O(n log n)  |  Space: O(n)
// During merge step, when left[i] > right[j], all remaining left elements
// also form inversions with right[j]: count += (mid - i + 1)
// ============================================================
class Optimal {
    private static long mergeCount(long[] arr, long[] temp, int left, int right) {
        if (left >= right) return 0;
        int mid = (left + right) / 2;
        long count = 0;
        count += mergeCount(arr, temp, left, mid);
        count += mergeCount(arr, temp, mid + 1, right);
        count += merge(arr, temp, left, mid, right);
        return count;
    }

    private static long merge(long[] arr, long[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) temp[i] = arr[i];
        int i = left, j = mid + 1, k = left;
        long count = 0;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                // temp[i..mid] are all > temp[j], each is an inversion
                count += (mid - i + 1);
                arr[k++] = temp[j++];
            }
        }
        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
        return count;
    }

    public static long solve(long[] A) {
        long[] copy = A.clone();
        long[] temp = new long[A.length];
        return mergeCount(copy, temp, 0, copy.length - 1);
    }
}

// ============================================================
// APPROACH 3: BEST - Merge Sort (iterative / same idea, explicit copy)
// Time: O(n log n)  |  Space: O(n)
// Non-recursive bottom-up merge sort with inversion counting
// ============================================================
class Best {
    public static long solve(long[] A) {
        int n = A.length;
        long[] arr = A.clone();
        long[] temp = new long[n];
        long count = 0;
        for (int width = 1; width < n; width *= 2) {
            for (int left = 0; left < n; left += 2 * width) {
                int mid   = Math.min(left + width - 1, n - 1);
                int right = Math.min(left + 2 * width - 1, n - 1);
                if (mid < right) count += mergeIter(arr, temp, left, mid, right);
            }
        }
        return count;
    }

    private static long mergeIter(long[] arr, long[] temp, int left, int mid, int right) {
        for (int i = left; i <= right; i++) temp[i] = arr[i];
        int i = left, j = mid + 1, k = left;
        long count = 0;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) { arr[k++] = temp[i++]; }
            else { count += (mid - i + 1); arr[k++] = temp[j++]; }
        }
        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
        return count;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Count Inversions ===");

        long[][] tests = {
            {2, 4, 1, 3, 5},    // expected 3: (2,1),(4,1),(4,3)
            {5, 4, 3, 2, 1},    // expected 10
            {1, 2, 3, 4, 5},    // expected 0
        };

        for (long[] A : tests) {
            System.out.printf("A=%s => Brute=%d, Optimal=%d, Best=%d%n",
                Arrays.toString(A),
                BruteForce.solve(A),
                Optimal.solve(A),
                Best.solve(A));
        }
    }
}
