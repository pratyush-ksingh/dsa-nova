import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// For each index check all left elements < A[i] and all right > A[i]
// ============================================================
class BruteForce {
    public static int solve(int[] A) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            boolean ok = true;
            for (int j = 0; j < i; j++) if (A[j] >= A[i]) { ok = false; break; }
            if (!ok) continue;
            for (int j = i + 1; j < n; j++) if (A[j] <= A[i]) { ok = false; break; }
            if (ok) return 1;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Prefix max + suffix min arrays
// Time: O(n)  |  Space: O(n)
// prefMax[i] = max of A[0..i-1], suffMin[i] = min of A[i+1..n-1]
// Index i is perfect peak if prefMax[i] < A[i] < suffMin[i]
// ============================================================
class Optimal {
    public static int solve(int[] A) {
        int n = A.length;
        if (n == 1) return 1;

        int[] prefMax = new int[n];
        int[] suffMin = new int[n];

        prefMax[0] = Integer.MIN_VALUE;
        for (int i = 1; i < n; i++) prefMax[i] = Math.max(prefMax[i - 1], A[i - 1]);

        suffMin[n - 1] = Integer.MAX_VALUE;
        for (int i = n - 2; i >= 0; i--) suffMin[i] = Math.min(suffMin[i + 1], A[i + 1]);

        for (int i = 0; i < n; i++) {
            if (prefMax[i] < A[i] && A[i] < suffMin[i]) return 1;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 3: BEST - Running left max + precomputed suffix min
// Time: O(n)  |  Space: O(n) for suffix array only
// Avoids prefix array by tracking running max during forward scan
// ============================================================
class Best {
    public static int solve(int[] A) {
        int n = A.length;
        if (n == 1) return 1;

        int[] suffMin = new int[n];
        suffMin[n - 1] = Integer.MAX_VALUE;
        for (int i = n - 2; i >= 0; i--) suffMin[i] = Math.min(suffMin[i + 1], A[i + 1]);

        int leftMax = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (leftMax < A[i] && A[i] < suffMin[i]) return 1;
            leftMax = Math.max(leftMax, A[i]);
        }
        return 0;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Perfect Peak of Array ===");

        int[][] tests = {
            {5, 1, 4, 3, 6, 8, 10, 7, 9},  // expected 1 (A[4]=6 is perfect peak)
            {1, 2, 3, 4, 5},                 // expected 0
            {5},                             // expected 1 (single element)
            {2, 3, 4, 5, 1},                 // expected 0
        };
        int[] expected = {1, 0, 1, 0};

        for (int t = 0; t < tests.length; t++) {
            int[] A = tests[t];
            int bf = BruteForce.solve(A);
            int op = Optimal.solve(A);
            int be = Best.solve(A);
            System.out.printf("A=%s => Brute=%d Optimal=%d Best=%d (expected %d)%n",
                Arrays.toString(A), bf, op, be, expected[t]);
        }
    }
}
