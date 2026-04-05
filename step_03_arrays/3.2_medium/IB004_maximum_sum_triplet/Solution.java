/**
 * Problem: Maximum Sum Triplet
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an array A of N integers, find max A[i] + A[j] + A[k] such that
 * i < j < k and A[i] < A[j] < A[k]. Return 0 if no such triplet exists.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^3)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Try all triplets (i, j, k) with i < j < k.
         * Check strict increase condition; track max sum.
         */
        public static int solve(int[] A) {
            int n = A.length;
            int ans = 0;
            for (int i = 0; i < n - 2; i++) {
                for (int j = i + 1; j < n - 1; j++) {
                    if (A[j] <= A[i]) continue;
                    for (int k = j + 1; k < n; k++) {
                        if (A[k] > A[j]) {
                            ans = Math.max(ans, A[i] + A[j] + A[k]);
                        }
                    }
                }
            }
            return ans;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Suffix max + sorted prefix set
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /**
         * For each j (middle element):
         *   - Best i: largest value < A[j] to the left (TreeMap for O(log n))
         *   - Best k: suffix max > A[j] to the right (precomputed suffix max array)
         *
         * Suffix max array: suffixMax[j] = max(A[j+1..n-1]).
         * If suffixMax[j] > A[j], we have a valid k.
         * Then find the floor entry < A[j] in the sorted left set.
         */
        public static int solve(int[] A) {
            int n = A.length;
            if (n < 3) return 0;

            // Precompute suffix max
            int[] suffixMax = new int[n];
            suffixMax[n - 1] = 0;
            for (int i = n - 2; i >= 0; i--) {
                suffixMax[i] = Math.max(A[i + 1], suffixMax[i + 1]);
            }

            // TreeMap to track elements to the left, get largest < A[j]
            TreeMap<Integer, Integer> leftMap = new TreeMap<>();
            int ans = 0;

            for (int j = 0; j < n; j++) {
                int rightMax = suffixMax[j];
                if (rightMax > A[j]) {
                    Map.Entry<Integer, Integer> entry = leftMap.lowerEntry(A[j]);
                    if (entry != null) {
                        ans = Math.max(ans, entry.getKey() + A[j] + rightMax);
                    }
                }
                leftMap.merge(A[j], 1, Integer::sum);
            }
            return ans;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Same approach, TreeSet variant
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Identical to Optimal in complexity, using TreeSet (handles duplicates
         * by tracking just the value set — for finding "largest strictly less"
         * we only need the key, so TreeSet suffices if we add all seen values).
         *
         * Note: TreeSet loses count of duplicates; we still get the correct
         * maximum value smaller than A[j].
         */
        public static int solve(int[] A) {
            int n = A.length;
            if (n < 3) return 0;

            int[] suffixMax = new int[n];
            for (int i = n - 2; i >= 0; i--) {
                suffixMax[i] = Math.max(A[i + 1], suffixMax[i + 1]);
            }

            TreeSet<Integer> leftSet = new TreeSet<>();
            int ans = 0;

            for (int j = 0; j < n; j++) {
                int rightMax = suffixMax[j];
                if (rightMax > A[j]) {
                    Integer bestI = leftSet.lower(A[j]); // largest element < A[j]
                    if (bestI != null) {
                        ans = Math.max(ans, bestI + A[j] + rightMax);
                    }
                }
                leftSet.add(A[j]);
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Sum Triplet ===");
        int[][] tests = {
            {2, 5, 3, 1, 4, 9},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {1, 5, 3, 6, 7}
        };
        for (int[] A : tests) {
            System.out.printf("A=%s: Brute=%d, Optimal=%d, Best=%d%n",
                Arrays.toString(A),
                BruteForce.solve(A),
                Optimal.solve(A),
                Best.solve(A));
        }
    }
}
