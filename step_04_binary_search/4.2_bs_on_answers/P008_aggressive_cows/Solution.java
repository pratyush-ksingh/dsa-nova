import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n log n + n * maxDist)  |  Space: O(1)
// Try every possible minimum distance from 1 to (max-min).
// For each candidate dist, greedily check if we can place all k cows.
// ============================================================
class BruteForce {
    private static boolean canPlace(int[] stalls, int k, int minDist) {
        int count = 1, last = stalls[0];
        for (int i = 1; i < stalls.length; i++) {
            if (stalls[i] - last >= minDist) { last = stalls[i]; count++; }
            if (count == k) return true;
        }
        return count >= k;
    }

    public static int solve(int[] stalls, int k) {
        Arrays.sort(stalls);
        int ans = 0;
        for (int d = 1; d <= stalls[stalls.length - 1] - stalls[0]; d++) {
            if (canPlace(stalls, k, d)) ans = d;
            else break;
        }
        return ans;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Binary Search on Answer
// Time: O(n log n + n log(maxDist))  |  Space: O(1)
// Binary search on the minimum distance. For a given mid distance,
// greedily check if k cows can be placed. Answer is the largest valid mid.
// ============================================================
class Optimal {
    private static boolean canPlace(int[] stalls, int k, int minDist) {
        int count = 1, last = stalls[0];
        for (int i = 1; i < stalls.length; i++) {
            if (stalls[i] - last >= minDist) { last = stalls[i]; count++; }
        }
        return count >= k;
    }

    public static int solve(int[] stalls, int k) {
        Arrays.sort(stalls);
        int lo = 1, hi = stalls[stalls.length - 1] - stalls[0], ans = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (canPlace(stalls, k, mid)) { ans = mid; lo = mid + 1; }
            else hi = mid - 1;
        }
        return ans;
    }
}

// ============================================================
// APPROACH 3: BEST - Same binary search, cleaner loop structure
// Time: O(n log n + n log(maxDist))  |  Space: O(1)
// Identical complexity; "Best" = same as Optimal (BS on answer IS optimal)
// ============================================================
class Best {
    private static boolean feasible(int[] stalls, int k, int dist) {
        int cows = 1, prev = stalls[0];
        for (int s : stalls) {
            if (s - prev >= dist) { prev = s; cows++; if (cows == k) return true; }
        }
        return cows >= k;
    }

    public static int solve(int[] stalls, int k) {
        Arrays.sort(stalls);
        int lo = 1, hi = stalls[stalls.length - 1] - stalls[0];
        while (lo < hi) {
            int mid = lo + (hi - lo + 1) / 2; // upper-mid to avoid infinite loop
            if (feasible(stalls, k, mid)) lo = mid;
            else hi = mid - 1;
        }
        return lo;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Aggressive Cows ===");

        int[][] tests = {{2, 4, 12, 5, 3}, {1, 2, 4, 8, 9}};
        int[] cows   = {3, 3};
        int[] expected = {3, 3};

        for (int t = 0; t < tests.length; t++) {
            int[] stalls = tests[t];
            int k = cows[t];
            System.out.printf("Stalls=%s, k=%d => Brute=%d, Optimal=%d, Best=%d (exp=%d)%n",
                Arrays.toString(stalls), k,
                BruteForce.solve(stalls.clone(), k),
                Optimal.solve(stalls.clone(), k),
                Best.solve(stalls.clone(), k),
                expected[t]);
        }
    }
}
