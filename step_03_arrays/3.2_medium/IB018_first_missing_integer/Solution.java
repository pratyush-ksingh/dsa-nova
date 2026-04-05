import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n log n)  |  Space: O(1)
// Sort the array; scan for the first missing positive integer.
// ============================================================
class BruteForce {
    public static int solve(int[] A) {
        Arrays.sort(A);
        int missing = 1;
        for (int val : A) {
            if (val == missing) missing++;
            else if (val > missing) break;
        }
        return missing;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (HashSet)
// Time: O(n)  |  Space: O(n)
// Put all positives in a set; iterate 1, 2, 3... until miss.
// ============================================================
class Optimal {
    public static int solve(int[] A) {
        Set<Integer> set = new HashSet<>();
        for (int v : A) if (v > 0) set.add(v);
        int missing = 1;
        while (set.contains(missing)) missing++;
        return missing;
    }
}

// ============================================================
// APPROACH 3: BEST (Index marking / Cyclic sort variant)
// Time: O(n)  |  Space: O(1)
// Place each number x in range [1,n] at index x-1.
// Scan for the first index where A[i] != i+1.
// ============================================================
class Best {
    public static int solve(int[] A) {
        int n = A.length;
        // Phase 1: put A[i] at A[i]-1 if A[i] in [1,n]
        for (int i = 0; i < n; i++) {
            while (A[i] > 0 && A[i] <= n && A[A[i] - 1] != A[i]) {
                int tmp = A[A[i] - 1];
                A[A[i] - 1] = A[i];
                A[i] = tmp;
            }
        }
        // Phase 2: find first position where value is wrong
        for (int i = 0; i < n; i++)
            if (A[i] != i + 1) return i + 1;
        return n + 1;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== First Missing Positive Integer ===");
        int[][] tests = {
            {1, 2, 0},
            {3, 4, -1, 1},
            {7, 8, 9, 11, 12},
            {1, 2, 3},
            {-5, -3, -1},
        };
        int[] expected = {3, 2, 1, 4, 1};

        for (int t = 0; t < tests.length; t++) {
            int bf  = BruteForce.solve(tests[t].clone());
            int op  = Optimal.solve(tests[t].clone());
            int bst = Best.solve(tests[t].clone());
            System.out.println("arr=" + Arrays.toString(tests[t])
                + "  BF=" + bf + "  OPT=" + op + "  BEST=" + bst + "  EXP=" + expected[t]);
        }
    }
}
