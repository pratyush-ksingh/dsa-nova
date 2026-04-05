import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(r*c log(r*c))  |  Space: O(r*c)
// Collect all elements into an array, sort, return middle element.
// ============================================================
class BruteForce {
    public static int solve(int[][] matrix) {
        int r = matrix.length, c = matrix[0].length;
        int[] flat = new int[r * c];
        int idx = 0;
        for (int[] row : matrix) for (int x : row) flat[idx++] = x;
        Arrays.sort(flat);
        return flat[(r * c) / 2];
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Binary Search on Value
// Time: O(32 * r * log c)  |  Space: O(1)
// Binary search on the value range [min, max].
// For each mid value, count elements <= mid across all rows
// (using upper_bound on each sorted row). Median is the smallest
// value where count > (r*c)/2.
// ============================================================
class Optimal {
    // Count elements <= val in the entire matrix using binary search on each row
    private static int countLessEqual(int[][] matrix, int val) {
        int count = 0;
        for (int[] row : matrix) {
            // upperBound: first index > val
            int lo = 0, hi = row.length;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (row[mid] <= val) lo = mid + 1;
                else hi = mid;
            }
            count += lo;
        }
        return count;
    }

    public static int solve(int[][] matrix) {
        int r = matrix.length, c = matrix[0].length;
        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE;
        for (int[] row : matrix) {
            lo = Math.min(lo, row[0]);
            hi = Math.max(hi, row[c - 1]);
        }
        int desired = (r * c + 1) / 2; // position of median (1-indexed)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (countLessEqual(matrix, mid) < desired) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

// ============================================================
// APPROACH 3: BEST - Same binary search with explicit median position
// Time: O(32 * r * log c)  |  Space: O(1)
// Functionally identical, but written to show the intuition clearly:
// find the smallest x such that at least (r*c/2 + 1) elements are <= x.
// ============================================================
class Best {
    private static int upperBound(int[] row, int val) {
        int lo = 0, hi = row.length;
        while (lo < hi) {
            int m = (lo + hi) / 2;
            if (row[m] <= val) lo = m + 1; else hi = m;
        }
        return lo;
    }

    private static int countLE(int[][] mat, int val) {
        int total = 0;
        for (int[] row : mat) total += upperBound(row, val);
        return total;
    }

    public static int solve(int[][] matrix) {
        int r = matrix.length, c = matrix[0].length;
        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE;
        for (int[] row : matrix) {
            lo = Math.min(lo, row[0]);
            hi = Math.max(hi, row[c - 1]);
        }
        int medianPos = (r * c + 1) / 2;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (countLE(matrix, mid) < medianPos) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Matrix Median ===");

        int[][][] tests = {
            {{1,3,5},{2,6,9},{3,6,9}},        // expected 5
            {{1,2,3,4,5}},                     // expected 3
            {{1,1,1},{1,1,1},{1,1,2}},         // expected 1
        };
        int[] expected = {5, 3, 1};

        for (int t = 0; t < tests.length; t++) {
            int[][] mat = tests[t];
            int bf = BruteForce.solve(mat);
            int op = Optimal.solve(mat);
            int be = Best.solve(mat);
            System.out.printf("Matrix=%s => Brute=%d, Optimal=%d, Best=%d (exp=%d)%n",
                Arrays.deepToString(mat), bf, op, be, expected[t]);
        }
    }
}
