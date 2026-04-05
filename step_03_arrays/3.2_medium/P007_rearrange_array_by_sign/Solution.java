import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// Separate positives and negatives into two lists, then interleave.
// Assumption: equal number of positives and negatives (Leetcode 2149 variant).
// ============================================================
class BruteForce {
    public static int[] solve(int[] nums) {
        List<Integer> pos = new ArrayList<>();
        List<Integer> neg = new ArrayList<>();
        for (int x : nums) {
            if (x >= 0) pos.add(x); else neg.add(x);
        }
        int[] res = new int[nums.length];
        for (int i = 0; i < pos.size(); i++) {
            res[2 * i]     = pos.get(i);
            res[2 * i + 1] = neg.get(i);
        }
        return res;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Two-pointer placement
// Time: O(n)  |  Space: O(n)
// Use two index pointers posIdx=0, negIdx=1. Fill result array
// directly without separating into extra lists first.
// ============================================================
class Optimal {
    public static int[] solve(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        int posIdx = 0, negIdx = 1;
        for (int x : nums) {
            if (x >= 0) { res[posIdx] = x; posIdx += 2; }
            else         { res[negIdx] = x; negIdx += 2; }
        }
        return res;
    }
}

// ============================================================
// APPROACH 3: BEST - In-place variant for unequal counts
// Time: O(n^2) worst  |  Space: O(1) extra
// Handles case where #positives != #negatives:
// place what you can alternately, then append remainder.
// Uses O(n) extra for the result (unavoidable for reordering).
// ============================================================
class Best {
    public static int[] solve(int[] nums) {
        // General version: equal-count guaranteed by problem, same as Optimal
        // but we handle unequal counts gracefully.
        List<Integer> pos = new ArrayList<>(), neg = new ArrayList<>();
        for (int x : nums) { if (x >= 0) pos.add(x); else neg.add(x); }
        int[] res = new int[nums.length];
        int i = 0, pi = 0, ni = 0;
        // Interleave while both available
        while (pi < pos.size() && ni < neg.size()) {
            res[i++] = pos.get(pi++);
            res[i++] = neg.get(ni++);
        }
        while (pi < pos.size()) res[i++] = pos.get(pi++);
        while (ni < neg.size()) res[i++] = neg.get(ni++);
        return res;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Rearrange Array by Sign ===");

        int[][] tests = {
            {3, 1, -2, -5, 2, -4},    // expected [3,-2,1,-5,2,-4]
            {-1, 1},                   // expected [1,-1]
            {1, 2, 3, -1, -2, -3},    // expected [1,-1,2,-2,3,-3]
        };

        for (int[] A : tests) {
            System.out.println("Input:   " + Arrays.toString(A));
            System.out.println("Brute:   " + Arrays.toString(BruteForce.solve(A)));
            System.out.println("Optimal: " + Arrays.toString(Optimal.solve(A)));
            System.out.println("Best:    " + Arrays.toString(Best.solve(A)));
            System.out.println();
        }
    }
}
