import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n * sum(pages))  |  Space: O(1)
// Try every candidate for max pages from max(pages) to sum(pages).
// For each, greedily count how many students are needed.
// Return the smallest valid candidate.
// ============================================================
class BruteForce {
    private static int studentsNeeded(int[] pages, int maxPages) {
        int students = 1, cur = 0;
        for (int p : pages) {
            if (cur + p > maxPages) { students++; cur = p; }
            else cur += p;
        }
        return students;
    }

    public static int solve(int[] pages, int m) {
        if (m > pages.length) return -1;
        int lo = Arrays.stream(pages).max().getAsInt();
        int hi = Arrays.stream(pages).sum();
        for (int limit = lo; limit <= hi; limit++) {
            if (studentsNeeded(pages, limit) <= m) return limit;
        }
        return -1;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Binary Search on Answer
// Time: O(n log(sum))  |  Space: O(1)
// Binary search between max(pages) and sum(pages).
// For each mid, check if m students suffice. Find smallest valid mid.
// ============================================================
class Optimal {
    private static boolean isPossible(int[] pages, int m, int maxPages) {
        int students = 1, cur = 0;
        for (int p : pages) {
            if (p > maxPages) return false;
            if (cur + p > maxPages) { students++; cur = p; }
            else cur += p;
        }
        return students <= m;
    }

    public static int solve(int[] pages, int m) {
        if (m > pages.length) return -1;
        int lo = Arrays.stream(pages).max().getAsInt();
        int hi = Arrays.stream(pages).sum();
        int ans = hi;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (isPossible(pages, m, mid)) { ans = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        return ans;
    }
}

// ============================================================
// APPROACH 3: BEST - Binary search with lo<hi loop form
// Time: O(n log(sum))  |  Space: O(1)
// Same algorithm with lower-mid form for minimization problems
// ============================================================
class Best {
    private static boolean feasible(int[] pages, int m, int limit) {
        int students = 1, cur = 0;
        for (int p : pages) {
            if (p > limit) return false;
            if (cur + p > limit) { students++; cur = p; }
            else cur += p;
            if (students > m) return false;
        }
        return true;
    }

    public static int solve(int[] pages, int m) {
        if (m > pages.length) return -1;
        int lo = Arrays.stream(pages).max().getAsInt();
        int hi = Arrays.stream(pages).sum();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2; // lower mid for minimization
            if (feasible(pages, m, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Book Allocation Problem ===");

        int[][] tests   = {{12, 34, 67, 90}, {10, 20, 30, 40}};
        int[]   ms      = {2, 2};
        int[]   expected = {113, 60};

        for (int t = 0; t < tests.length; t++) {
            int[] pages = tests[t];
            int m = ms[t];
            System.out.printf("Pages=%s, m=%d => Brute=%d, Optimal=%d, Best=%d (exp=%d)%n",
                Arrays.toString(pages), m,
                BruteForce.solve(pages, m),
                Optimal.solve(pages, m),
                Best.solve(pages, m),
                expected[t]);
        }
    }
}
