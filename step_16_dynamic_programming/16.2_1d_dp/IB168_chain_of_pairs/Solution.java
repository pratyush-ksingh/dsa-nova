import java.util.*;

/**
 * Problem: Chain of Pairs
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given N pairs, find the length of the longest chain where for consecutive
 * pairs (a,b) and (c,d) in the chain: b < c.
 * Classic LIS variant — sort by second element, then find LIS on second elements
 * where each next pair's first element is greater than previous pair's second.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// O(N^2) DP after sorting by first element
// dp[i] = length of longest chain ending at pair i
// Time: O(N^2)  |  Space: O(N)
// ============================================================
class BruteForce {
    static int longestChain(int[][] pairs) {
        // Sort by first element (or second)
        Arrays.sort(pairs, (a, b) -> a[0] - b[0]);
        int n = pairs.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int ans = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Pair j can precede pair i if pairs[j][1] < pairs[i][0]
                if (pairs[j][1] < pairs[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Greedy: sort by SECOND element; greedily pick pairs with smallest end
// Similar to patience sorting (Activity Selection Problem style)
// Time: O(N log N)  |  Space: O(1)
// ============================================================
class Optimal {
    static int longestChain(int[][] pairs) {
        Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        int count = 1;
        int prevEnd = pairs[0][1];

        for (int i = 1; i < pairs.length; i++) {
            if (pairs[i][0] > prevEnd) {
                count++;
                prevEnd = pairs[i][1];
            }
        }
        return count;
    }
}

// ============================================================
// APPROACH 3: BEST
// Binary search LIS on second element (after sorting by first element)
// Uses patience sorting with tails array
// Time: O(N log N)  |  Space: O(N)
// ============================================================
class Best {
    static int longestChain(int[][] pairs) {
        // Sort by first element; b[i] = second element forms basis of LIS
        Arrays.sort(pairs, (a, b) -> a[0] - b[0]);
        List<Integer> tails = new ArrayList<>();

        for (int[] pair : pairs) {
            int a = pair[0], b = pair[1];
            // Find position where tails[pos] >= a (strict: last chain's end < a)
            // We want: can extend chain ending with some value < a
            int lo = 0, hi = tails.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (tails.get(mid) < a) lo = mid + 1;
                else hi = mid;
            }
            if (lo == tails.size()) tails.add(b);
            else tails.set(lo, Math.min(tails.get(lo), b));
        }
        return tails.size();
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Chain of Pairs ===");

        // pairs: (1,2),(2,3),(3,4) => chain (1,2)->(3,4) => length 2
        int[][] p1 = {{1,2},{2,3},{3,4}};
        System.out.println("BruteForce p1: " + BruteForce.longestChain(p1));  // 2
        System.out.println("Optimal    p1: " + Optimal.longestChain(p1));     // 2

        // (5,24),(15,25),(27,40),(50,60) => chain all 4: (5,24)->(27,40)->(50,60)? No, 4 not all
        // Actually (5,24)->(27,40)->(50,60) = length 3
        int[][] p2 = {{5,24},{15,25},{27,40},{50,60}};
        System.out.println("BruteForce p2: " + BruteForce.longestChain(p2));  // 3
        System.out.println("Optimal    p2: " + Optimal.longestChain(p2));     // 3

        int[][] p3 = {{1,10},{2,3},{3,7},{4,5}};
        System.out.println("Optimal p3: " + Optimal.longestChain(p3));  // 2
    }
}
