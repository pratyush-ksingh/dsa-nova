import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Try all subarrays, track maximum product.
// ============================================================
class BruteForce {
    public static long solve(int[] arr) {
        int n = arr.length;
        long maxProd = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            long prod = 1;
            for (int j = i; j < n; j++) {
                prod *= arr[j];
                maxProd = Math.max(maxProd, prod);
            }
        }
        return maxProd;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Track min & max running product)
// Time: O(n)  |  Space: O(1)
// A negative * negative can become the new maximum.
// Track both curMax and curMin; swap when encountering negative.
// ============================================================
class Optimal {
    public static long solve(int[] arr) {
        long maxProd = arr[0], curMax = arr[0], curMin = arr[0];
        for (int i = 1; i < arr.length; i++) {
            long v = arr[i];
            if (v < 0) { long tmp = curMax; curMax = curMin; curMin = tmp; }
            curMax = Math.max(v, curMax * v);
            curMin = Math.min(v, curMin * v);
            maxProd = Math.max(maxProd, curMax);
        }
        return maxProd;
    }
}

// ============================================================
// APPROACH 3: BEST (Prefix + Suffix product scan)
// Time: O(n)  |  Space: O(1)
// Key insight: the maximum subarray product is either a prefix
// or a suffix of the array (once all zeros are considered as
// natural "reset" boundaries). Scan from both ends and take max.
// ============================================================
class Best {
    public static long solve(int[] arr) {
        int n = arr.length;
        long maxProd = Long.MIN_VALUE;
        long prefix = 1, suffix = 1;
        for (int i = 0; i < n; i++) {
            prefix *= arr[i];
            suffix *= arr[n - 1 - i];
            maxProd = Math.max(maxProd, Math.max(prefix, suffix));
            if (prefix == 0) prefix = 1;
            if (suffix == 0) suffix = 1;
        }
        return maxProd;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Maximum Product Subarray ===");
        int[][] tests = {
            {2, 3, -2, 4},
            {-2, 0, -1},
            {-2, 3, -4},
            {0, 2},
            {-2, -3, 0, -2, -40},
        };
        long[] expected = {6, 0, 24, 2, 80};

        for (int t = 0; t < tests.length; t++) {
            long bf  = BruteForce.solve(tests[t].clone());
            long op  = Optimal.solve(tests[t].clone());
            long bst = Best.solve(tests[t].clone());
            String ok = (bf == op && op == bst && bst == expected[t]) ? "OK" : "MISMATCH";
            System.out.println("arr=" + Arrays.toString(tests[t])
                + "  BF=" + bf + "  OPT=" + op + "  BEST=" + bst
                + "  EXP=" + expected[t] + "  " + ok);
        }
    }
}
