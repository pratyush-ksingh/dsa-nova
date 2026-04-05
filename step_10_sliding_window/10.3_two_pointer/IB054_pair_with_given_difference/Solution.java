/**
 * Problem: Pair With Given Difference (InterviewBit)
 * Difficulty: EASY | XP: 10
 *
 * Given array A and integer B, find if there exists a pair (i,j) with
 * A[i] - A[j] = B and i != j. Return 1 if exists, 0 otherwise.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Check All Pairs)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int solve(int[] A, int B) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && A[i] - A[j] == B) {
                    return 1;
                }
            }
        }
        return 0;
    }
}

// ============================================================
// Approach 2: Optimal (Sort + Two Pointers)
// Time: O(n log n) | Space: O(1) if in-place sort
// ============================================================
class Optimal {
    public static int solve(int[] A, int B) {
        int n = A.length;
        if (n < 2) return 0;

        Arrays.sort(A);

        int i = 1, j = 0;
        while (i < n) {
            long diff = (long) A[i] - (long) A[j]; // avoid overflow
            if (diff == B && i != j) {
                return 1;
            } else if (diff < B) {
                i++;
            } else {
                j++;
                if (j == i) i++;
            }
        }
        return 0;
    }
}

// ============================================================
// Approach 3: Best (HashSet Lookup)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    public static int solve(int[] A, int B) {
        int n = A.length;
        if (n < 2) return 0;

        // Special case: B == 0 means we need duplicates
        if (B == 0) {
            Set<Integer> seen = new HashSet<>();
            for (int a : A) {
                if (seen.contains(a)) return 1;
                seen.add(a);
            }
            return 0;
        }

        // General case: for each a, check if (a - B) exists
        Set<Integer> set = new HashSet<>();
        for (int a : A) set.add(a);

        for (int a : set) {
            if (set.contains(a - B)) {
                return 1;
            }
        }
        return 0;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Pair With Given Difference ===\n");

        int[][][] tests = {
            {{5, 10, 3, 2, 50, 80}, {78}, {1}},
            {{-10, 20}, {30}, {1}},
            {{1, 2, 3}, {0}, {0}},
            {{5, 5, 3}, {0}, {1}},
            {{1}, {5}, {0}},
            {{1, 5, 4, 2}, {3}, {1}},
            {{1, 2, 3, 4, 5}, {-1}, {1}},  // A[i]-A[j] = -1 means A[i] < A[j]
        };

        for (int[][] t : tests) {
            int[] A = t[0];
            int B = t[1][0];
            int expected = t[2][0];

            int b = BruteForce.solve(A.clone(), B);
            int o = Optimal.solve(A.clone(), B);
            int h = Best.solve(A.clone(), B);
            boolean pass = b == expected && o == expected && h == expected;

            System.out.println("Input:    A=" + Arrays.toString(A) + ", B=" + B);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("HashSet:  " + h);
            System.out.println("Expected: " + expected);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
