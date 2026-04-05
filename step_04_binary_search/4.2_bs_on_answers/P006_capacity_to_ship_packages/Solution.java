/**
 * Problem: Capacity to Ship Packages Within D Days (LeetCode #1011)
 * Difficulty: MEDIUM | XP: 25
 *
 * A conveyor belt has packages that must be shipped from one port to another
 * within D days. The i-th package has weight weights[i]. Each day we load
 * packages in order (no splitting). Find the minimum ship capacity to ship
 * all packages within D days.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Try every capacity from max to sum)
// Time: O(n * (sum - max)) | Space: O(1)
// ============================================================
class BruteForce {
    private static int daysNeeded(int[] weights, int capacity) {
        int days = 1, current = 0;
        for (int w : weights) {
            if (current + w > capacity) {
                days++;
                current = 0;
            }
            current += w;
        }
        return days;
    }

    public static int shipWithinDays(int[] weights, int days) {
        int lo = 0, hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);
            hi += w;
        }
        for (int cap = lo; cap <= hi; cap++) {
            if (daysNeeded(weights, cap) <= days) return cap;
        }
        return hi;
    }
}

// ============================================================
// Approach 2: Optimal (Binary search on capacity)
// Time: O(n * log(sum - max)) | Space: O(1)
// ============================================================
class Optimal {
    private static int daysNeeded(int[] weights, int capacity) {
        int days = 1, current = 0;
        for (int w : weights) {
            if (current + w > capacity) {
                days++;
                current = 0;
            }
            current += w;
        }
        return days;
    }

    public static int shipWithinDays(int[] weights, int days) {
        int lo = 0, hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);
            hi += w;
        }
        int ans = hi;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (daysNeeded(weights, mid) <= days) {
                ans = mid;      // feasible; try smaller capacity
                hi = mid - 1;
            } else {
                lo = mid + 1;   // not feasible; need larger capacity
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Binary search + lo < hi + early exit)
// Time: O(n * log(sum - max)) | Space: O(1)
// ============================================================
class Best {
    private static boolean canShip(int[] weights, int capacity, int maxDays) {
        int days = 1, current = 0;
        for (int w : weights) {
            if (current + w > capacity) {
                days++;
                current = 0;
                if (days > maxDays) return false; // early exit
            }
            current += w;
        }
        return true;
    }

    public static int shipWithinDays(int[] weights, int days) {
        int lo = 0, hi = 0;
        for (int w : weights) {
            lo = Math.max(lo, w);
            hi += w;
        }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canShip(weights, mid, days)) {
                hi = mid;       // feasible; try smaller
            } else {
                lo = mid + 1;   // not feasible; need larger
            }
        }
        return lo;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Capacity to Ship Packages Within D Days ===\n");

        int[][] inputs   = {
            {1,2,3,4,5,6,7,8,9,10},
            {3,2,2,4,1,4},
            {1,2,3,1,1},
            {10},
            {1,1,1,1,1,1,1,1,1,1}
        };
        int[] daysList   = {5,  3, 4, 1, 10};
        int[] expected   = {15, 6, 3, 10, 1};

        for (int t = 0; t < inputs.length; t++) {
            int[] w = inputs[t];
            int   d = daysList[t];

            int b = BruteForce.shipWithinDays(w, d);
            int o = Optimal.shipWithinDays(w, d);
            int n = Best.shipWithinDays(w, d);
            boolean pass = b == expected[t] && o == expected[t] && n == expected[t];

            System.out.println("Input:    weights=" + Arrays.toString(w) + ", days=" + d);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + n);
            System.out.println("Expected: " + expected[t] + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
