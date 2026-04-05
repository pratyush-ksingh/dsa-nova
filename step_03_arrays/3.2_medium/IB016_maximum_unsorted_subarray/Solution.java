/**
 * Problem: Maximum Unsorted Subarray
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Find the shortest subarray [start, end] such that sorting A[start..end]
 * makes the entire array sorted. Return [-1] if already sorted.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n³)  |  Space: O(n)
    // Try every (i, j) pair; sort the subarray and compare with sorted(A)
    // ============================================================
    static class BruteForce {
        public static int[] findUnsortedSubarray(int[] A) {
            int n = A.length;
            int[] sorted = A.clone();
            Arrays.sort(sorted);

            if (Arrays.equals(A, sorted)) return new int[]{-1};

            int bestL = -1, bestR = -1, bestLen = n + 1;

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    // Sort subarray A[i..j] in a copy
                    int[] candidate = A.clone();
                    Arrays.sort(candidate, i, j + 1);
                    if (Arrays.equals(candidate, sorted)) {
                        if ((j - i + 1) < bestLen) {
                            bestLen = j - i + 1;
                            bestL = i;
                            bestR = j;
                        }
                    }
                }
            }
            return new int[]{bestL, bestR};
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (two-pass expand)
    // Time: O(n)  |  Space: O(1)
    // Find initial boundaries, compute subarray min/max, expand outward
    // ============================================================
    static class Optimal {
        /**
         * Pass 1: left = first index where A[i] > A[i+1]
         *         right = last index where A[j] < A[j-1]
         * Pass 2: compute min/max of A[left..right]
         * Pass 3: expand left while A[left-1] > subMin
         *         expand right while A[right+1] < subMax
         */
        public static int[] findUnsortedSubarray(int[] A) {
            int n = A.length;

            int left = -1;
            for (int i = 0; i < n - 1; i++) {
                if (A[i] > A[i + 1]) { left = i; break; }
            }
            if (left == -1) return new int[]{-1};

            int right = -1;
            for (int j = n - 1; j > 0; j--) {
                if (A[j] < A[j - 1]) { right = j; break; }
            }

            int subMin = Integer.MAX_VALUE, subMax = Integer.MIN_VALUE;
            for (int k = left; k <= right; k++) {
                subMin = Math.min(subMin, A[k]);
                subMax = Math.max(subMax, A[k]);
            }

            while (left > 0 && A[left - 1] > subMin) left--;
            while (right < n - 1 && A[right + 1] < subMax) right++;

            return new int[]{left, right};
        }
    }

    // ============================================================
    // APPROACH 3: BEST (single conceptual pass from both ends)
    // Time: O(n)  |  Space: O(1)
    // Running max from left gives right boundary;
    // running min from right gives left boundary — no separate expand needed.
    // ============================================================
    static class Best {
        /**
         * Right boundary: scan left to right tracking running max.
         *   Any position where A[i] < runningMax must be inside the unsorted region.
         *   The LAST such position is the right boundary.
         * Left boundary: scan right to left tracking running min.
         *   Any position where A[i] > runningMin must be inside the unsorted region.
         *   The LAST such position (from the right) is the left boundary.
         */
        public static int[] findUnsortedSubarray(int[] A) {
            int n = A.length;
            int runMax = A[0], right = -1;
            for (int i = 1; i < n; i++) {
                if (A[i] < runMax) right = i;
                else runMax = A[i];
            }
            if (right == -1) return new int[]{-1};

            int runMin = A[n - 1], left = -1;
            for (int i = n - 2; i >= 0; i--) {
                if (A[i] > runMin) left = i;
                else runMin = A[i];
            }
            return new int[]{left, right};
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Unsorted Subarray ===");
        int[][] inputs = {
            {1, 3, 2, 4, 5},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {2, 6, 4, 8, 10, 9, 15},
            {2, 1},
            {1, 3, 5, 2, 6, 4, 8},
        };
        int[][] expected = {
            {1, 2},
            {-1},
            {0, 4},
            {1, 5},
            {0, 1},
            {1, 5},
        };

        for (int t = 0; t < inputs.length; t++) {
            int[] b  = BruteForce.findUnsortedSubarray(inputs[t]);
            int[] o  = Optimal.findUnsortedSubarray(inputs[t]);
            int[] be = Best.findUnsortedSubarray(inputs[t]);
            boolean ok = Arrays.equals(b, o) && Arrays.equals(o, be) && Arrays.equals(be, expected[t]);
            System.out.printf("A=%-30s | Brute: %-8s | Optimal: %-8s | Best: %-8s | %s%n",
                    Arrays.toString(inputs[t]),
                    Arrays.toString(b), Arrays.toString(o), Arrays.toString(be),
                    ok ? "OK" : "FAIL");
        }
    }
}
