import java.util.*;

/**
 * Problem: Kth Element of Two Sorted Arrays
 * Difficulty: HARD | XP: 50
 *
 * Given two sorted arrays A and B of sizes m and n, find the k-th smallest
 * element in the merged sorted array (1-indexed).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(m + n)  |  Space: O(m + n)
    // Merge both arrays fully, then return the k-th element.
    // ============================================================
    public static int bruteForce(int[] A, int[] B, int k) {
        int m = A.length, n = B.length;
        int[] merged = new int[m + n];
        int i = 0, j = 0, idx = 0;
        while (i < m && j < n) {
            if (A[i] <= B[j]) merged[idx++] = A[i++];
            else merged[idx++] = B[j++];
        }
        while (i < m) merged[idx++] = A[i++];
        while (j < n) merged[idx++] = B[j++];
        return merged[k - 1];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Recursive Binary Elimination
    // Time: O(log(m + n))  |  Space: O(log(m + n)) stack
    // At each step, compare the k/2-th elements of both arrays.
    // The smaller side's first k/2 elements can't contain the answer,
    // so eliminate them and reduce k by k/2.
    // ============================================================
    private static int kthHelper(int[] A, int aStart, int[] B, int bStart, int k) {
        if (aStart >= A.length) return B[bStart + k - 1];
        if (bStart >= B.length) return A[aStart + k - 1];
        if (k == 1) return Math.min(A[aStart], B[bStart]);

        int half = k / 2;
        int aVal = (aStart + half - 1 < A.length) ? A[aStart + half - 1] : Integer.MAX_VALUE;
        int bVal = (bStart + half - 1 < B.length) ? B[bStart + half - 1] : Integer.MAX_VALUE;

        if (aVal < bVal) {
            return kthHelper(A, aStart + half, B, bStart, k - half);
        } else {
            return kthHelper(A, aStart, B, bStart + half, k - half);
        }
    }

    public static int optimal(int[] A, int[] B, int k) {
        return kthHelper(A, 0, B, 0, k);
    }

    // ============================================================
    // APPROACH 3: BEST — Binary Search on partition
    // Time: O(log(min(m, n)))  |  Space: O(1)
    // Binary search on how many elements to take from the smaller
    // array A. For a given partition (i from A, k-i from B),
    // valid if A[i-1] <= B[k-i] and B[k-i-1] <= A[i].
    // This is the cleanest O(log(min(m,n))) approach.
    // ============================================================
    public static int best(int[] A, int[] B, int k) {
        // Ensure A is the smaller array
        if (A.length > B.length) return best(B, A, k);
        int m = A.length, n = B.length;

        // i elements from A (0..min(k,m)), rest from B
        int lo = Math.max(0, k - n), hi = Math.min(k, m);

        while (lo <= hi) {
            int i = (lo + hi) / 2;
            int j = k - i;

            int aLeft  = (i == 0) ? Integer.MIN_VALUE : A[i - 1];
            int aRight = (i == m) ? Integer.MAX_VALUE : A[i];
            int bLeft  = (j == 0) ? Integer.MIN_VALUE : B[j - 1];
            int bRight = (j == n) ? Integer.MAX_VALUE : B[j];

            if (aLeft <= bRight && bLeft <= aRight) {
                return Math.max(aLeft, bLeft);
            } else if (aLeft > bRight) {
                hi = i - 1;
            } else {
                lo = i + 1;
            }
        }
        return -1; // shouldn't reach
    }

    public static void main(String[] args) {
        System.out.println("=== Kth Element of Two Sorted Arrays ===");
        int[][] As = {{2, 3, 6, 7, 9}, {1, 2, 3}, {1},     {1, 3}};
        int[][] Bs = {{1, 4, 8, 10},   {4, 5, 6}, {2, 3},  {2}};
        int[] ks  = {5,                 4,         2,        3};
        // Expected: sorted merges are:
        // [1,2,3,4,6,7,8,9,10] k=5 -> 6
        // [1,2,3,4,5,6] k=4 -> 4
        // [1,2,3] k=2 -> 2
        // [1,2,3] k=3 -> 3

        for (int t = 0; t < As.length; t++) {
            int bf = bruteForce(As[t], Bs[t], ks[t]);
            int op = optimal(As[t], Bs[t], ks[t]);
            int be = best(As[t], Bs[t], ks[t]);
            System.out.printf("A=%s B=%s k=%d -> Brute=%d, Optimal=%d, Best=%d%n",
                    Arrays.toString(As[t]), Arrays.toString(Bs[t]), ks[t], bf, op, be);
        }
    }
}
