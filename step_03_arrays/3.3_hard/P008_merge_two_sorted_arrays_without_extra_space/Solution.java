/**
 * Problem: Merge Two Sorted Arrays Without Extra Space
 * Difficulty: HARD | XP: 50
 *
 * Given two sorted arrays A (size m) and B (size n), merge them in-place
 * so that A contains the m smallest elements sorted, and B contains the rest sorted.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O((m+n) log(m+n))  |  Space: O(m+n)
    // ============================================================
    /**
     * Concatenate both arrays, sort the combined array, then redistribute.
     * Simple but uses extra space — violates the "without extra space" constraint.
     * Shown as reference for correctness comparison.
     * Real-life: Merging two sorted customer databases for deduplication.
     */
    public static void bruteForce(long[] A, long[] B) {
        int m = A.length, n = B.length;
        long[] combined = new long[m + n];
        System.arraycopy(A, 0, combined, 0, m);
        System.arraycopy(B, 0, combined, m, n);
        Arrays.sort(combined);
        System.arraycopy(combined, 0, A, 0, m);
        System.arraycopy(combined, m, B, 0, n);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Two-pointer swap approach)
    // Time: O(m * n)  |  Space: O(1)
    // ============================================================
    /**
     * Two pointers: i at end of A, j at start of B.
     * If A[i] > B[j], swap them and fix the order in each array.
     * B stays sorted after each swap; A needs a right-shift (insertion-sort style).
     * Real-life: In-place merge in embedded systems with no heap allocation.
     */
    public static void optimal(long[] A, long[] B) {
        int m = A.length, n = B.length;
        int i = m - 1, j = 0;
        while (i >= 0 && j < n) {
            if (A[i] > B[j]) {
                long tmp = A[i]; A[i] = B[j]; B[j] = tmp;
                // Fix sorting in A: bubble A[i] left
                for (int k = i - 1; k >= 0 && A[k] > A[k + 1]; k--) {
                    long t = A[k]; A[k] = A[k + 1]; A[k + 1] = t;
                }
                // Fix sorting in B: bubble B[j] right
                for (int k = j + 1; k < n && B[k] < B[k - 1]; k++) {
                    long t = B[k]; B[k] = B[k - 1]; B[k - 1] = t;
                }
            }
            i--;
            j++;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Shell/Gap method)
    // Time: O((m+n) log(m+n))  |  Space: O(1)
    // ============================================================
    /**
     * Gap algorithm (Shell sort inspired):
     * Start with gap = ceil((m+n)/2), compare elements gap apart across A and B.
     * Halve the gap each iteration until gap = 0.
     * Real-life: Cache-efficient in-place merge used in timsort variants.
     */
    public static void best(long[] A, long[] B) {
        int m = A.length, n = B.length;
        int total = m + n;
        int gap = (total + 1) / 2;

        while (gap > 0) {
            // Compare and swap all pairs that are 'gap' apart
            for (int left = 0; left + gap < total; left++) {
                int right = left + gap;
                long lv = left  < m ? A[left]      : B[left - m];
                long rv = right < m ? A[right]      : B[right - m];
                if (lv > rv) {
                    // Swap
                    if (left < m && right < m) { long t=A[left]; A[left]=A[right]; A[right]=t; }
                    else if (left < m)          { long t=A[left]; A[left]=B[right-m]; B[right-m]=t; }
                    else                        { long t=B[left-m]; B[left-m]=B[right-m]; B[right-m]=t; }
                }
            }
            if (gap == 1) break;
            gap = (gap + 1) / 2;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Merge Two Sorted Arrays Without Extra Space ===");

        long[][] testA = {{1, 3, 5, 7}, {1}, {10, 12}};
        long[][] testB = {{0, 2, 6, 8, 9}, {2}, {5, 18, 20}};
        String[] expA  = {"[0, 1, 2, 3]", "[1]", "[5, 10]"};
        String[] expB  = {"[5, 6, 7, 8, 9]", "[2]", "[12, 18, 20]"};

        for (int t = 0; t < testA.length; t++) {
            System.out.println("\nA=" + Arrays.toString(testA[t]) + "  B=" + Arrays.toString(testB[t]));
            System.out.println("Expected A=" + expA[t] + "  B=" + expB[t]);

            long[] a1 = testA[t].clone(), b1 = testB[t].clone();
            bruteForce(a1, b1);
            System.out.println("Brute:   A=" + Arrays.toString(a1) + "  B=" + Arrays.toString(b1));

            long[] a2 = testA[t].clone(), b2 = testB[t].clone();
            optimal(a2, b2);
            System.out.println("Optimal: A=" + Arrays.toString(a2) + "  B=" + Arrays.toString(b2));

            long[] a3 = testA[t].clone(), b3 = testB[t].clone();
            best(a3, b3);
            System.out.println("Best:    A=" + Arrays.toString(a3) + "  B=" + Arrays.toString(b3));
        }
    }
}
