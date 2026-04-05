import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - DP with O(n*k^2) linear search
// Time: O(n * k^2)  |  Space: O(n * k)
// ============================================================
// dp[i][j] = min trials needed with i eggs and j floors.
// For each floor x from 1..j, try dropping: if egg breaks,
// check (i-1, x-1); if survives, check (i, j-x). Take the
// worst case (max), then minimize over all x.
// Base cases: 0 floors -> 0 trials; 1 floor -> 1 trial;
// 1 egg -> j trials (must check linearly from bottom).
// ============================================================

class BruteForce {
    public int solve(int n, int k) {
        // dp[eggs][floors]
        int[][] dp = new int[n + 1][k + 1];
        // 1 egg: need j trials for j floors
        for (int j = 0; j <= k; j++) dp[1][j] = j;
        // 0 or 1 floors: 0 or 1 trial
        for (int i = 1; i <= n; i++) { dp[i][0] = 0; dp[i][1] = 1; }

        for (int i = 2; i <= n; i++) {
            for (int j = 2; j <= k; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    int worst = 1 + Math.max(dp[i-1][x-1], dp[i][j-x]);
                    dp[i][j] = Math.min(dp[i][j], worst);
                }
            }
        }
        return dp[n][k];
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - DP + Binary Search on monotone function
// Time: O(n * k * log k)  |  Space: O(n * k)
// ============================================================
// For fixed eggs i and floors j, as trial floor x increases:
// dp[i-1][x-1] increases (more floors below) and dp[i][j-x]
// decreases (fewer floors above). The optimal x is where they
// cross. Use binary search to find crossing point in O(log k).
// ============================================================

class Optimal {
    public int solve(int n, int k) {
        int[][] dp = new int[n + 1][k + 1];
        for (int j = 0; j <= k; j++) dp[1][j] = j;
        for (int i = 1; i <= n; i++) { dp[i][0] = 0; dp[i][1] = 1; }

        for (int i = 2; i <= n; i++) {
            for (int j = 2; j <= k; j++) {
                int lo = 1, hi = j, best = Integer.MAX_VALUE;
                while (lo <= hi) {
                    int mid = (lo + hi) / 2;
                    int breakCase   = dp[i-1][mid-1]; // egg breaks
                    int survivCase  = dp[i][j-mid];   // egg survives
                    int worst = 1 + Math.max(breakCase, survivCase);
                    best = Math.min(best, worst);
                    if (breakCase < survivCase) lo = mid + 1;
                    else hi = mid - 1;
                }
                dp[i][j] = best;
            }
        }
        return dp[n][k];
    }
}

// ============================================================
// APPROACH 3: BEST - Reverse DP: floors reachable in t trials
// Time: O(n * k)  |  Space: O(n)
// ============================================================
// Reformulation: given n eggs and t trials, what is the maximum
// number of floors we can test? f(t,n) = f(t-1,n-1) + f(t-1,n) + 1
// (floors above + floors below + the current floor).
// We binary search on t (number of trials) until f(t,n) >= k.
// Inner DP table is just O(n) size, updated in O(n) per t step.
// Real-life use: minimum probe count for black-box testing,
// optimal binary search depth under resource constraints.
// ============================================================

class Best {
    public int solve(int n, int k) {
        // f[j] = max floors testable with j eggs and current number of trials t
        int[] f = new int[n + 1];
        int t = 0;
        while (f[n] < k) {
            t++;
            // Update in reverse to avoid using updated values in same step
            int[] nf = new int[n + 1];
            for (int j = 1; j <= n; j++) {
                nf[j] = f[j-1] + f[j] + 1;
            }
            f = nf;
        }
        return t;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Egg Drop Problem ===");

        // Classic: 2 eggs, 100 floors => 14 trials
        System.out.println("n=2, k=100 Brute   (expect 14): " + new BruteForce().solve(2, 100));
        System.out.println("n=2, k=100 Optimal (expect 14): " + new Optimal().solve(2, 100));
        System.out.println("n=2, k=100 Best    (expect 14): " + new Best().solve(2, 100));

        // n=1, k=6: must go linearly => 6 trials
        System.out.println("n=1, k=6  Best     (expect 6): " + new Best().solve(1, 6));

        // n=3, k=14
        int b = new BruteForce().solve(3, 14);
        int op = new Optimal().solve(3, 14);
        int bst = new Best().solve(3, 14);
        System.out.println("n=3, k=14 all match? " + (b==op && op==bst) + " val=" + b);

        // n=10, k=1000
        System.out.println("n=10, k=1000 Best: " + new Best().solve(10, 1000));

        // n=2, k=1 => 1 trial
        System.out.println("n=2, k=1  Best     (expect 1): " + new Best().solve(2, 1));
    }
}
