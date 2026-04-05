import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// For every pair (i, j) where j > i, check A[j] >= A[i]
// and track the maximum j - i.
// ============================================================
class BruteForce {
    public static int solve(int[] A) {
        int n = A.length, maxDist = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                if (A[j] >= A[i]) maxDist = Math.max(maxDist, j - i);
        return maxDist;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// 1. Build prefix-min array (leftMin[i] = min of A[0..i]).
// 2. Build suffix-max array (rightMax[j] = max of A[j..n-1]).
// 3. Two-pointer from ends: if leftMin[i] <= rightMax[j] -> update max,
//    move j right; else move i right.
// ============================================================
class Optimal {
    public static int solve(int[] A) {
        int n = A.length;
        int[] leftMin  = new int[n];
        int[] rightMax = new int[n];

        leftMin[0] = A[0];
        for (int i = 1; i < n; i++) leftMin[i] = Math.min(leftMin[i-1], A[i]);

        rightMax[n-1] = A[n-1];
        for (int j = n-2; j >= 0; j--) rightMax[j] = Math.max(rightMax[j+1], A[j]);

        int i = 0, j = 0, maxDist = 0;
        while (i < n && j < n) {
            if (leftMin[i] <= rightMax[j]) {
                maxDist = Math.max(maxDist, j - i);
                j++;
            } else {
                i++;
            }
        }
        return maxDist;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// Same two-pointer approach, just with cleaner variable names
// and explicit clarification of correctness logic.
// ============================================================
class Best {
    public static int solve(int[] A) {
        int n = A.length;
        // leftMin[i] = minimum of A[0..i]  (never increases left-to-right)
        int[] leftMin = new int[n];
        leftMin[0] = A[0];
        for (int i = 1; i < n; i++) leftMin[i] = Math.min(leftMin[i - 1], A[i]);

        // rightMax[j] = maximum of A[j..n-1]  (never decreases right-to-left)
        int[] rightMax = new int[n];
        rightMax[n - 1] = A[n - 1];
        for (int j = n - 2; j >= 0; j--) rightMax[j] = Math.max(rightMax[j + 1], A[j]);

        // Two pointers: advance j when condition met (widen gap), else advance i
        int lo = 0, hi = 0, ans = 0;
        while (hi < n) {
            if (leftMin[lo] <= rightMax[hi]) {
                ans = Math.max(ans, hi - lo);
                hi++;
            } else {
                lo++;
            }
        }
        return ans;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Max Distance (j-i such that A[j]>=A[i]) ===");
        int[][] tests = {
            {3, 5, 4, 2},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {34, 8, 10, 3, 2, 80, 30, 33, 1},
        };
        int[] expected = {2, 4, 0, 6};
        for (int t = 0; t < tests.length; t++) {
            int bf   = BruteForce.solve(tests[t].clone());
            int op   = Optimal.solve(tests[t].clone());
            int bst  = Best.solve(tests[t].clone());
            System.out.println("arr=" + Arrays.toString(tests[t])
                + "  BF=" + bf + "  OPT=" + op + "  BEST=" + bst + "  EXP=" + expected[t]);
        }
    }
}
