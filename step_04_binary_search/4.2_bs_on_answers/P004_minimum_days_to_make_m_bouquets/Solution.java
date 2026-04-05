/**
 * Problem: Minimum Days to Make M Bouquets (LeetCode #1482)
 * Difficulty: MEDIUM | XP: 25
 *
 * bloomDay[i] = day flower i blooms. Need m bouquets of k adjacent flowers.
 * Find minimum days to make m bouquets, or -1 if impossible.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Try Every Day)
// Time: O(n * max(bloomDay)) | Space: O(1)
// ============================================================
class BruteForce {
    public static int minDays(int[] bloomDay, int m, int k) {
        int n = bloomDay.length;
        if ((long) m * k > n) return -1;

        int maxDay = 0;
        for (int d : bloomDay) maxDay = Math.max(maxDay, d);

        for (int day = 1; day <= maxDay; day++) {
            if (countBouquets(bloomDay, day, k) >= m) return day;
        }
        return -1;
    }

    private static int countBouquets(int[] bloomDay, int day, int k) {
        int bouquets = 0, consecutive = 0;
        for (int bd : bloomDay) {
            if (bd <= day) {
                consecutive++;
                if (consecutive == k) {
                    bouquets++;
                    consecutive = 0;
                }
            } else {
                consecutive = 0;
            }
        }
        return bouquets;
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search on Days)
// Time: O(n * log(max - min)) | Space: O(1)
// ============================================================
class Optimal {
    public static int minDays(int[] bloomDay, int m, int k) {
        int n = bloomDay.length;
        if ((long) m * k > n) return -1;

        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE;
        for (int d : bloomDay) {
            lo = Math.min(lo, d);
            hi = Math.max(hi, d);
        }

        int ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (countBouquets(bloomDay, mid, k) >= m) {
                ans = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }

    private static int countBouquets(int[] bloomDay, int day, int k) {
        int bouquets = 0, consecutive = 0;
        for (int bd : bloomDay) {
            if (bd <= day) {
                consecutive++;
                if (consecutive == k) {
                    bouquets++;
                    consecutive = 0;
                }
            } else {
                consecutive = 0;
            }
        }
        return bouquets;
    }
}

// ============================================================
// Approach 3: Best (Binary Search + Early Termination)
// Time: O(n * log(max - min)) | Space: O(1)
// ============================================================
class Best {
    public static int minDays(int[] bloomDay, int m, int k) {
        int n = bloomDay.length;
        if ((long) m * k > n) return -1;

        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE;
        for (int d : bloomDay) {
            lo = Math.min(lo, d);
            hi = Math.max(hi, d);
        }

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canMake(bloomDay, mid, m, k)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return canMake(bloomDay, lo, m, k) ? lo : -1;
    }

    private static boolean canMake(int[] bloomDay, int day, int m, int k) {
        int bouquets = 0, consecutive = 0;
        for (int bd : bloomDay) {
            if (bd <= day) {
                consecutive++;
                if (consecutive == k) {
                    bouquets++;
                    if (bouquets >= m) return true; // early termination
                    consecutive = 0;
                }
            } else {
                consecutive = 0;
            }
        }
        return false;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Minimum Days to Make M Bouquets ===\n");

        int[][] bloomDays = {
            {1, 10, 3, 10, 2},
            {1, 10, 3, 10, 2},
            {7, 7, 7, 7, 12, 7, 7},
            {1, 10, 2, 9, 3, 8, 4, 7, 5, 6},
            {1}
        };
        int[] mArr = {3, 3, 2, 4, 1};
        int[] kArr = {1, 2, 3, 2, 1};
        int[] expected = {3, -1, 12, 9, 1};

        for (int t = 0; t < bloomDays.length; t++) {
            int b = BruteForce.minDays(bloomDays[t], mArr[t], kArr[t]);
            int o = Optimal.minDays(bloomDays[t], mArr[t], kArr[t]);
            int n = Best.minDays(bloomDays[t], mArr[t], kArr[t]);
            boolean pass = (b == expected[t] && o == expected[t] && n == expected[t]);

            System.out.println("Input:    bloomDay=" + java.util.Arrays.toString(bloomDays[t])
                + ", m=" + mArr[t] + ", k=" + kArr[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + n);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
