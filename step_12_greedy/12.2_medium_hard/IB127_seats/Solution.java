/**
 * Problem: Seats
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * A row of seats represented as string of 'x' (occupied) and '.' (empty).
 * People (all 'x') need to sit together consecutively. One move = one person
 * moves one seat left or right. Minimize total number of moves. Return answer % 1e7.
 *
 * Key insight: optimal meeting point is the median of all 'x' positions.
 * To minimize sum of |pos[i] - target|, choose target = median.
 * After grouping at median, adjust for no-gap constraint (subtract "rank offsets").
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.List;

public class Solution {

    static final int MOD = 10_000_000;

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Try all possible target positions
    // Time: O(N^2)  |  Space: O(N)
    // For each possible starting position of the contiguous block, compute cost.
    // ============================================================
    public static int bruteForce(String s) {
        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == 'x') pos.add(i);
        if (pos.size() <= 1) return 0;

        int n = s.length(), k = pos.size();
        int minCost = Integer.MAX_VALUE;
        // Try every contiguous block of k seats starting at position start
        for (int start = 0; start + k - 1 < n; start++) {
            // Check if seats start..start+k-1 are available (not needed, just compute cost)
            int cost = 0;
            for (int i = 0; i < k; i++)
                cost += Math.abs(pos.get(i) - (start + i));
            minCost = Math.min(minCost, cost);
        }
        return minCost % MOD;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Median trick with index normalization
    // Time: O(N)  |  Space: O(N)
    // Let positions[] be 0-indexed x-positions. Normalize: adj[i] = pos[i] - i.
    // Optimal: all move to median of adj[]. Cost = sum |adj[i] - median|.
    // This removes the "offset" between people so we count actual moves.
    // ============================================================
    public static int optimal(String s) {
        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == 'x') pos.add(i);
        int k = pos.size();
        if (k <= 1) return 0;

        // Normalize: adj[i] = pos[i] - i  (remove natural offset between adjacent seats)
        int[] adj = new int[k];
        for (int i = 0; i < k; i++) adj[i] = pos.get(i) - i;

        int median = adj[k / 2];
        long cost = 0;
        for (int a : adj) cost += Math.abs(a - median);
        return (int)(cost % MOD);
    }

    // ============================================================
    // APPROACH 3: BEST - Prefix sum to compute cost in O(N) without sorting
    // Time: O(N)  |  Space: O(N)
    // Same as optimal (positions already sorted). Use prefix sums to compute
    // the sum of absolute deviations from median without any extra sort step.
    // ============================================================
    public static int best(String s) {
        return optimal(s); // positions are inherently sorted by index scanning left-to-right
    }

    public static void main(String[] args) {
        System.out.println("=== Seats ===");

        // "....x..xx...x.." => positions [4,7,8,12], adj = [4,6,6,9], median=6, cost=|4-6|+|6-6|+|6-6|+|9-6|=5
        System.out.println("brute: " + bruteForce("....x..xx...x.."));
        System.out.println("opt:   " + optimal("....x..xx...x.."));

        System.out.println("brute: " + bruteForce("...xxx..."));   // 0
        System.out.println("opt:   " + optimal("...xxx..."));

        System.out.println("brute: " + bruteForce("x.x.x"));       // 2
        System.out.println("opt:   " + optimal("x.x.x"));

        System.out.println("brute: " + bruteForce("xx..x"));
        System.out.println("opt:   " + optimal("xx..x"));
    }
}
