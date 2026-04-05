import java.util.*;

/**
 * Problem: Diffk II
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given a sorted array A and integer k, determine if there exist two
 * indices i != j such that A[i] - A[j] = k. Return 1 if yes, 0 otherwise.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Check every pair (i, j) where i != j.
    // ============================================================
    static class BruteForce {
        public static int diffk(int[] arr, int k) {
            int n = arr.length;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && arr[i] - arr[j] == k) {
                        return 1;
                    }
                }
            }
            return 0;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashSet Lookup
    // Time: O(N)  |  Space: O(N)
    // For each x, check if (x - k) or (x + k) is in the set.
    // ============================================================
    static class Optimal {
        public static int diffk(int[] arr, int k) {
            Set<Integer> seen = new HashSet<>();
            for (int x : arr) {
                if (seen.contains(x - k) || seen.contains(x + k)) {
                    return 1;
                }
                seen.add(x);
            }
            return 0;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Two Pointers (Sorted Array)
    // Time: O(N)  |  Space: O(1)
    // Two pointers i, j. If diff == k and i != j, found.
    // If diff < k, advance i. If diff > k, advance j.
    // ============================================================
    static class Best {
        public static int diffk(int[] arr, int k) {
            int n = arr.length;
            int i = 0, j = 0;
            while (i < n && j < n) {
                int diff = arr[i] - arr[j];
                if (diff == k && i != j) {
                    return 1;
                } else if (diff < k) {
                    i++;
                } else {
                    j++;
                    if (j == i) j++;
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        int[][][] tests = {
            {{1, 2, 3, 4, 5}, {2}},
            {{1, 3, 5}, {4}},
            {{1, 1}, {0}},
            {{1, 2, 3}, {0}},
            {{1, 5, 10}, {5}},
        };
        System.out.println("=== Diffk II ===");
        for (int[][] t : tests) {
            int[] arr = t[0];
            int k = t[1][0];
            System.out.printf("  arr=%s, k=%d: brute=%d, optimal=%d, best=%d%n",
                Arrays.toString(arr), k,
                BruteForce.diffk(arr, k),
                Optimal.diffk(arr, k),
                Best.diffk(arr, k));
        }
    }
}
