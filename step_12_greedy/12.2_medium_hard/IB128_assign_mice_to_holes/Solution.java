import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(N^2 * N!)  |  Space: O(N)
// Try all permutations of hole assignments, pick min max-distance
// (only feasible for very small N)
// ============================================================
class BruteForce {
    private static int[] holes;
    private static int n;
    private static int minMaxDist;

    public static int solve(int[] mice, int[] holesArr) {
        n = mice.length;
        holes = holesArr.clone();
        int[] miceClone = mice.clone();
        Arrays.sort(miceClone);
        Arrays.sort(holes);
        minMaxDist = Integer.MAX_VALUE;
        permute(miceClone, 0);
        return minMaxDist;
    }

    private static void permute(int[] m, int start) {
        if (start == n) {
            int maxDist = 0;
            for (int i = 0; i < n; i++) maxDist = Math.max(maxDist, Math.abs(m[i] - holes[i]));
            minMaxDist = Math.min(minMaxDist, maxDist);
            return;
        }
        for (int i = start; i < n; i++) {
            int tmp = m[start]; m[start] = m[i]; m[i] = tmp;
            permute(m, start + 1);
            tmp = m[start]; m[start] = m[i]; m[i] = tmp;
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Sort and pair greedily
// Time: O(N log N)  |  Space: O(1)
// Sort both mice and holes. Pair i-th mouse with i-th hole.
// The optimal assignment minimizes the maximum |mouse[i] - hole[i]|.
// Proof by exchange argument: any other pairing is worse or equal.
// ============================================================
class Optimal {
    public static int solve(int[] mice, int[] holes) {
        int[] m = mice.clone();
        int[] h = holes.clone();
        Arrays.sort(m);
        Arrays.sort(h);
        int maxDist = 0;
        for (int i = 0; i < m.length; i++) {
            maxDist = Math.max(maxDist, Math.abs(m[i] - h[i]));
        }
        return maxDist;
    }
}

// ============================================================
// APPROACH 3: BEST - Same as Optimal (already O(N log N) is best possible)
// Time: O(N log N)  |  Space: O(1)
// Streamlined with a single-pass after sorting
// ============================================================
class Best {
    public static int solve(int[] mice, int[] holes) {
        Arrays.sort(mice);
        Arrays.sort(holes);
        int maxDist = 0;
        for (int i = 0; i < mice.length; i++) maxDist = Math.max(maxDist, Math.abs(mice[i] - holes[i]));
        return maxDist;
    }
}

public class Solution {
    public static void main(String[] args) {
        // IB Example: mice=[-4,2,3], holes=[0,1,9] -> sort mice[-4,2,3], holes[0,1,9]
        // pairs: (-4,0)->4, (2,1)->1, (3,9)->6. Max=6
        int[] mice1 = {-4, 2, 3};
        int[] holes1 = {0, 1, 9};
        System.out.println("Test 1: expected 6");
        System.out.println("  BruteForce = " + BruteForce.solve(mice1, holes1));
        System.out.println("  Optimal    = " + Optimal.solve(mice1, holes1));
        System.out.println("  Best       = " + Best.solve(mice1, holes1.clone()));

        int[] mice2 = {0, 3};
        int[] holes2 = {1, 4};
        System.out.println("Test 2: expected 1");
        System.out.println("  BruteForce = " + BruteForce.solve(mice2, holes2));
        System.out.println("  Optimal    = " + Optimal.solve(mice2, holes2));
        System.out.println("  Best       = " + Best.solve(mice2.clone(), holes2.clone()));
    }
}
