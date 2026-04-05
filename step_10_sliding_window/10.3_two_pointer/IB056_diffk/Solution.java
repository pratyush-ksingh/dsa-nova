/**
 * Problem: Diffk (InterviewBit)
 * Difficulty: EASY | XP: 10
 *
 * Given a sorted array A and integer B, find if there exists a pair
 * with absolute difference B. Return 1/0.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Check All Pairs)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int diffk(int[] A, int B) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Array is sorted, so A[j] >= A[i]
                if (A[j] - A[i] == B) {
                    return 1;
                }
            }
        }
        return 0;
    }
}

// ============================================================
// Approach 2: Optimal (Two Pointers -- Same Direction)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int diffk(int[] A, int B) {
        int n = A.length;
        if (n < 2) return 0;

        int i = 0, j = 1;
        while (j < n) {
            int diff = A[j] - A[i];
            if (diff == B && i != j) {
                return 1;
            } else if (diff < B) {
                j++;
            } else {
                i++;
                if (i == j) j++;
            }
        }
        return 0;
    }
}

// ============================================================
// Approach 3: Best (HashSet -- general case, works even unsorted)
// Time: O(n) | Space: O(n)
// ============================================================
class Best {
    public static int diffk(int[] A, int B) {
        int n = A.length;
        if (n < 2) return 0;

        // B == 0: need duplicate values at different indices
        if (B == 0) {
            Set<Integer> seen = new HashSet<>();
            for (int a : A) {
                if (seen.contains(a)) return 1;
                seen.add(a);
            }
            return 0;
        }

        Set<Integer> set = new HashSet<>();
        for (int a : A) set.add(a);

        for (int a : set) {
            if (set.contains(a + B)) {
                return 1;
            }
        }
        return 0;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Diffk ===\n");

        int[][][] tests = {
            {{1, 2, 3, 5}, {4}, {1}},
            {{1, 3, 5}, {4}, {1}},
            {{1, 2, 3}, {0}, {0}},
            {{1, 1, 3}, {0}, {1}},
            {{1}, {5}, {0}},
            {{1, 2, 3, 4, 5}, {1}, {1}},
            {{1, 10, 100}, {90}, {1}},
            {{1, 2, 3, 4}, {5}, {0}},
        };

        for (int[][] t : tests) {
            int[] A = t[0];
            int B = t[1][0];
            int expected = t[2][0];

            int b = BruteForce.diffk(A.clone(), B);
            int o = Optimal.diffk(A.clone(), B);
            int h = Best.diffk(A.clone(), B);
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
