import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n * sum(nums))  |  Space: O(1)
// Try every candidate from max(nums) to sum(nums).
// Return the smallest value where k pieces suffice.
// ============================================================
class BruteForce {
    private static int piecesNeeded(int[] nums, int limit) {
        int pieces = 1, cur = 0;
        for (int x : nums) {
            if (cur + x > limit) { pieces++; cur = x; }
            else cur += x;
        }
        return pieces;
    }

    public static int solve(int[] nums, int k) {
        int lo = Arrays.stream(nums).max().getAsInt();
        int hi = Arrays.stream(nums).sum();
        for (int limit = lo; limit <= hi; limit++) {
            if (piecesNeeded(nums, limit) <= k) return limit;
        }
        return -1;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Binary Search on Answer
// Time: O(n log(sum))  |  Space: O(1)
// Binary search between max(nums) and sum(nums).
// For a given max-sum mid, greedily count minimum subarrays needed.
// Find smallest mid where count <= k.
// ============================================================
class Optimal {
    private static boolean canSplit(int[] nums, int k, int maxSum) {
        int pieces = 1, cur = 0;
        for (int x : nums) {
            if (cur + x > maxSum) { pieces++; cur = x; }
            else cur += x;
            if (pieces > k) return false;
        }
        return true;
    }

    public static int solve(int[] nums, int k) {
        int lo = Arrays.stream(nums).max().getAsInt();
        int hi = Arrays.stream(nums).sum();
        int ans = hi;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (canSplit(nums, k, mid)) { ans = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        return ans;
    }
}

// ============================================================
// APPROACH 3: BEST - lo<hi minimization template
// Time: O(n log(sum))  |  Space: O(1)
// Same algorithm with lo<hi loop (lower-mid) — elegant minimization form
// ============================================================
class Best {
    private static boolean feasible(int[] nums, int k, int limit) {
        int pieces = 1, cur = 0;
        for (int x : nums) {
            if (x > limit) return false;
            if (cur + x > limit) { pieces++; cur = x; }
            else cur += x;
        }
        return pieces <= k;
    }

    public static int solve(int[] nums, int k) {
        int lo = Arrays.stream(nums).max().getAsInt();
        int hi = Arrays.stream(nums).sum();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (feasible(nums, k, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Split Array Largest Sum ===");

        int[][] tests   = {{7,2,5,10,8}, {1,2,3,4,5}};
        int[]   ks      = {2, 2};
        int[]   expected = {18, 9};

        for (int t = 0; t < tests.length; t++) {
            int[] nums = tests[t];
            int k = ks[t];
            System.out.printf("nums=%s, k=%d => Brute=%d, Optimal=%d, Best=%d (exp=%d)%n",
                Arrays.toString(nums), k,
                BruteForce.solve(nums, k),
                Optimal.solve(nums, k),
                Best.solve(nums, k),
                expected[t]);
        }
    }
}
