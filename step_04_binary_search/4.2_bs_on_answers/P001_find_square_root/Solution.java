/**
 * Problem: Find Square Root (LeetCode #69)
 * Difficulty: EASY | XP: 10
 *
 * Given non-negative integer x, return floor(sqrt(x)).
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(sqrt(x)) | Space: O(1)
// ============================================================
class BruteForce {
    public static int mySqrt(int x) {
        if (x < 2) return x;
        int r = 1;
        // Use long to avoid overflow in r*r
        while ((long) r * r <= x) {
            r++;
        }
        return r - 1;
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search on Answer)
// Time: O(log x) | Space: O(1)
// ============================================================
class Optimal {
    public static int mySqrt(int x) {
        if (x < 2) return x;
        int lo = 1, hi = x / 2;
        int ans = 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            // Use mid <= x / mid to avoid overflow (instead of mid*mid <= x)
            if (mid <= x / mid) {
                ans = mid;       // mid*mid <= x, so mid is a candidate
                lo = mid + 1;    // try larger
            } else {
                hi = mid - 1;    // mid*mid > x, too large
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Newton's Method -- Integer Version)
// Time: O(log(log(x))) | Space: O(1)
// ============================================================
class Best {
    public static int mySqrt(int x) {
        if (x < 2) return x;
        long r = x / 2;
        while (r * r > x) {
            r = (r + x / r) / 2;
        }
        return (int) r;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Find Square Root ===\n");

        int[] inputs = {0, 1, 4, 8, 15, 16, 100, 2147483647};
        int[] expected = {0, 1, 2, 2, 3, 4, 10, 46340};

        for (int t = 0; t < inputs.length; t++) {
            int b = BruteForce.mySqrt(inputs[t]);
            int o = Optimal.mySqrt(inputs[t]);
            int n = Best.mySqrt(inputs[t]);
            boolean pass = (b == expected[t] && o == expected[t] && n == expected[t]);

            System.out.println("Input:    " + inputs[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Newton:   " + n);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
