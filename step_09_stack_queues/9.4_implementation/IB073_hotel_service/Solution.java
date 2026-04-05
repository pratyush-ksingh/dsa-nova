import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n * m)  |  Space: O(1)
// For each customer, scan all hotels and find the nearest one
// (minimum absolute distance).
// ============================================================
class BruteForce {
    public static int[] solve(int[] hotels, int[] customers) {
        int[] result = new int[customers.length];
        for (int i = 0; i < customers.length; i++) {
            int nearestHotel = hotels[0];
            int minDist = Math.abs(customers[i] - hotels[0]);
            for (int j = 1; j < hotels.length; j++) {
                int dist = Math.abs(customers[i] - hotels[j]);
                if (dist < minDist) {
                    minDist = dist;
                    nearestHotel = hotels[j];
                }
            }
            result[i] = nearestHotel;
        }
        return result;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O((n + m) log n)  |  Space: O(n)
// Sort hotels; for each customer use binary search to find the
// closest hotel (compare the neighbor candidates).
// ============================================================
class Optimal {
    public static int[] solve(int[] hotels, int[] customers) {
        int[] sortedHotels = hotels.clone();
        Arrays.sort(sortedHotels);
        int[] result = new int[customers.length];

        for (int i = 0; i < customers.length; i++) {
            int c = customers[i];
            int pos = Arrays.binarySearch(sortedHotels, c);
            if (pos >= 0) {
                result[i] = c; // exact match
            } else {
                int insertIdx = -(pos + 1);
                int best = sortedHotels[0];
                int bestDist = Integer.MAX_VALUE;
                if (insertIdx > 0) {
                    int d = Math.abs(sortedHotels[insertIdx - 1] - c);
                    if (d < bestDist) { bestDist = d; best = sortedHotels[insertIdx - 1]; }
                }
                if (insertIdx < sortedHotels.length) {
                    int d = Math.abs(sortedHotels[insertIdx] - c);
                    if (d < bestDist) { bestDist = d; best = sortedHotels[insertIdx]; }
                }
                result[i] = best;
            }
        }
        return result;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O((n + m) log n)  |  Space: O(n)
// Same sorted binary-search approach with a cleaner helper.
// A TreeSet would give O(log n) per query without sorting customers.
// ============================================================
class Best {
    private static int findNearest(int[] sortedHotels, int customer) {
        int lo = 0, hi = sortedHotels.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (sortedHotels[mid] < customer) lo = mid + 1;
            else hi = mid;
        }
        // lo is first hotel >= customer
        if (lo == 0) return sortedHotels[0];
        // Compare lo-1 and lo
        int left  = sortedHotels[lo - 1];
        int right = sortedHotels[lo];
        return (Math.abs(left - customer) <= Math.abs(right - customer)) ? left : right;
    }

    public static int[] solve(int[] hotels, int[] customers) {
        int[] sortedHotels = hotels.clone();
        Arrays.sort(sortedHotels);
        int[] result = new int[customers.length];
        for (int i = 0; i < customers.length; i++) {
            result[i] = findNearest(sortedHotels, customers[i]);
        }
        return result;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Hotel Service ===");

        int[] hotels    = {1, 5, 9, 15};
        int[] customers = {3, 7, 10, 12};
        // Expected: customer 3 -> hotel 1(d=2) or 5(d=2): tie -> 1 (leftmost)
        //           customer 7 -> hotel 5(d=2) or 9(d=2): tie -> 5
        //           customer 10 -> hotel 9(d=1)
        //           customer 12 -> hotel 9(d=3) or 15(d=3): tie -> 9
        System.out.println("Hotels:    " + Arrays.toString(hotels));
        System.out.println("Customers: " + Arrays.toString(customers));
        System.out.println("Brute:     " + Arrays.toString(BruteForce.solve(hotels, customers)));
        System.out.println("Optimal:   " + Arrays.toString(Optimal.solve(hotels, customers)));
        System.out.println("Best:      " + Arrays.toString(Best.solve(hotels, customers)));

        // Simple test
        int[] h2 = {2, 8};
        int[] c2 = {1, 5, 7, 10};
        System.out.println("\nHotels:    " + Arrays.toString(h2));
        System.out.println("Customers: " + Arrays.toString(c2));
        System.out.println("Brute:     " + Arrays.toString(BruteForce.solve(h2, c2)));
        System.out.println("Best:      " + Arrays.toString(Best.solve(h2, c2)));
        System.out.println("Expected:  [2, 2, 8, 8]");
    }
}
