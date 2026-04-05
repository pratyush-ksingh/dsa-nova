import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(1)
// Linear scan through the entire array.
// ============================================================
class BruteForce {
    public static int solve(int[] A, int target) {
        for (int i = 0; i < A.length; i++)
            if (A[i] == target) return i;
        return -1;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(log n)  |  Space: O(1)
// 1. Find the peak (bitonic point) using binary search.
// 2. Binary search in ascending half [0..peak].
// 3. Binary search in descending half [peak+1..n-1].
// ============================================================
class Optimal {
    private static int findPeak(int[] A) {
        int lo = 0, hi = A.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (A[mid] < A[mid + 1]) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    private static int bsAsc(int[] A, int lo, int hi, int target) {
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (A[mid] == target) return mid;
            if (A[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    private static int bsDesc(int[] A, int lo, int hi, int target) {
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (A[mid] == target) return mid;
            if (A[mid] > target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    public static int solve(int[] A, int target) {
        int peak = findPeak(A);
        int idx = bsAsc(A, 0, peak, target);
        return idx != -1 ? idx : bsDesc(A, peak + 1, A.length - 1, target);
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(log n)  |  Space: O(1)
// Same as Optimal, written more concisely and combined in
// a single unified method with clear structure.
// ============================================================
class Best {
    public static int solve(int[] A, int target) {
        int n = A.length;
        // Find peak index
        int lo = 0, hi = n - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (A[mid] < A[mid + 1]) lo = mid + 1;
            else hi = mid;
        }
        int peak = lo;

        // Search ascending part [0..peak]
        lo = 0; hi = peak;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (A[mid] == target) return mid;
            if (A[mid] < target) lo = mid + 1; else hi = mid - 1;
        }

        // Search descending part [peak+1..n-1]
        lo = peak + 1; hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (A[mid] == target) return mid;
            if (A[mid] > target) lo = mid + 1; else hi = mid - 1;
        }

        return -1;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Search in Bitonic Array ===");
        int[][] arrays = {
            {1, 3, 8, 12, 4, 2},
            {3, 9, 10, 20, 17, 5, 1},
            {5, 6, 7, 8, 9, 10, 3, 2, 1},
            {10, 20, 30, 40, 50},
        };
        int[] targets = {4, 20, 3, 5};
        int[] expected = {4, 3, 6, -1};

        for (int t = 0; t < arrays.length; t++) {
            int bf  = BruteForce.solve(arrays[t], targets[t]);
            int op  = Optimal.solve(arrays[t], targets[t]);
            int bst = Best.solve(arrays[t], targets[t]);
            String ok = (bf == op && op == bst && bst == expected[t]) ? "OK" : "MISMATCH";
            System.out.println("arr=" + Arrays.toString(arrays[t]) + " target=" + targets[t]
                + "  BF=" + bf + "  OPT=" + op + "  BEST=" + bst + "  EXP=" + expected[t] + "  " + ok);
        }
    }
}
