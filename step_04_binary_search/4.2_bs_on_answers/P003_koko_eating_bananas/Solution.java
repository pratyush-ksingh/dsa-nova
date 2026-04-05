/**
 * Problem: Koko Eating Bananas (LeetCode #875)
 * Difficulty: MEDIUM | XP: 25
 *
 * Koko can eat bananas at speed k per hour. Each pile takes ceil(pile/k) hours.
 * Given piles array and h hours, find minimum k so she can eat all within h hours.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(max(piles) * n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int minEatingSpeed(int[] piles, int h) {
        int maxPile = 0;
        for (int p : piles) maxPile = Math.max(maxPile, p);

        for (int k = 1; k <= maxPile; k++) {
            long hours = 0;
            for (int p : piles) {
                hours += (p + k - 1) / k; // ceil(p / k)
            }
            if (hours <= h) return k;
        }
        return maxPile;
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search on Answer)
// Time: O(n * log(max(piles))) | Space: O(1)
// ============================================================
class Optimal {
    public static int minEatingSpeed(int[] piles, int h) {
        int maxPile = 0;
        for (int p : piles) maxPile = Math.max(maxPile, p);

        int lo = 1, hi = maxPile, ans = maxPile;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            long hours = 0;
            for (int p : piles) {
                hours += (p + mid - 1) / mid;
            }
            if (hours <= h) {
                ans = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Binary Search + Early Termination)
// Time: O(n * log(max(piles))) | Space: O(1)
// ============================================================
class Best {
    public static int minEatingSpeed(int[] piles, int h) {
        int maxPile = 0;
        for (int p : piles) maxPile = Math.max(maxPile, p);

        int lo = 1, hi = maxPile;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canFinish(piles, mid, h)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    private static boolean canFinish(int[] piles, int k, int h) {
        long hours = 0;
        for (int p : piles) {
            hours += (p + k - 1) / k;
            if (hours > h) return false; // early termination
        }
        return true;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Koko Eating Bananas ===\n");

        int[][] pilesArr = {
            {3, 6, 7, 11},
            {30, 11, 23, 4, 20},
            {30, 11, 23, 4, 20},
            {1},
            {1000000000}
        };
        int[] hArr = {8, 5, 6, 1, 2};
        int[] expected = {4, 30, 23, 1, 500000000};

        for (int t = 0; t < pilesArr.length; t++) {
            int b = BruteForce.minEatingSpeed(pilesArr[t], hArr[t]);
            int o = Optimal.minEatingSpeed(pilesArr[t], hArr[t]);
            int n = Best.minEatingSpeed(pilesArr[t], hArr[t]);
            boolean pass = (b == expected[t] && o == expected[t] && n == expected[t]);

            System.out.println("Input:    piles=" + java.util.Arrays.toString(pilesArr[t]) + ", h=" + hArr[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + n);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
