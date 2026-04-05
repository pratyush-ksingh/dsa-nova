import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(max_height * n)  |  Space: O(1)
// Try every possible height from max down to 0; return first
// height where total wood collected >= B.
// ============================================================
class BruteForce {
    public static int solve(int[] A, int B) {
        int maxH = 0;
        for (int h : A) maxH = Math.max(maxH, h);

        for (int h = maxH; h >= 0; h--) {
            long wood = 0;
            for (int tree : A)
                if (tree > h) wood += tree - h;
            if (wood >= B) return h;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Binary Search on Answer)
// Time: O(n log(max_height))  |  Space: O(1)
// Binary search on the cut height [0, max].
// For a given height h, wood = sum(max(0, tree-h)) for all trees.
// We want the maximum h such that wood >= B.
// ============================================================
class Optimal {
    private static long computeWood(int[] A, long h) {
        long wood = 0;
        for (int tree : A) if (tree > h) wood += tree - h;
        return wood;
    }

    public static int solve(int[] A, int B) {
        int lo = 0, hi = 0;
        for (int h : A) hi = Math.max(hi, h);
        int ans = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (computeWood(A, mid) >= B) {
                ans = mid;
                lo = mid + 1; // try a higher cut
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n log(max_height))  |  Space: O(1)
// Same as Optimal with long arithmetic to prevent overflow
// (tree heights can be up to 10^9 and B up to 2*10^9).
// ============================================================
class Best {
    public static int solve(int[] A, long B) {
        long lo = 0, hi = 0;
        for (int h : A) if (h > hi) hi = h;
        long ans = 0;
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;
            long wood = 0;
            for (int tree : A) if (tree > mid) wood += tree - mid;
            if (wood >= B) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return (int) ans;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== WoodCutting Made Easy ===");
        int[][] arrays = {
            {20, 15, 10, 17},
            {4, 42, 40, 26, 46},
            {1, 2},
        };
        int[] Bs = {7, 20, 3};
        int[] expected = {15, 36, 0};

        for (int t = 0; t < arrays.length; t++) {
            int bf  = BruteForce.solve(arrays[t], Bs[t]);
            int op  = Optimal.solve(arrays[t], Bs[t]);
            int bst = Best.solve(arrays[t], Bs[t]);
            String ok = (bf == op && op == bst && bst == expected[t]) ? "OK" : "MISMATCH";
            System.out.println("trees=" + Arrays.toString(arrays[t]) + " B=" + Bs[t]
                + "  BF=" + bf + "  OPT=" + op + "  BEST=" + bst + "  EXP=" + expected[t] + "  " + ok);
        }
    }
}
