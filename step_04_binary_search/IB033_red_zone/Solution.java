import java.util.*;

/**
 * Problem: Red Zone
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given a 1D array representing a row of cells, some are initially infected
 * (value 1) and others are healthy (value 0). Each second, infection spreads
 * to adjacent cells (left and right). Find the time at which ALL cells
 * become infected.
 *
 * Equivalent to: given sorted positions of infected cells, find the maximum
 * time for any uninfected gap to be fully covered from both ends.
 *
 * For a gap between infected cells at positions L and R (0-indexed in
 * original array), the cells inside take ceil((R-L-1)/2) seconds from
 * the nearest infected cell — but since spread comes from both sides
 * simultaneously, the gap of length g = R-L-1 takes ceil(g/2) = (g+1)/2 steps.
 * Edge gaps (before first or after last infected) take their full length.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — BFS simulation
    // Time: O(n * t)  |  Space: O(n)
    // Simulate the infection spread second by second using BFS.
    // ============================================================
    public static int bruteForce(int[] A) {
        int n = A.length;
        int[] state = A.clone();
        int time = 0;
        while (true) {
            boolean allInfected = true;
            for (int x : state) if (x == 0) { allInfected = false; break; }
            if (allInfected) return time;

            int[] next = state.clone();
            for (int i = 0; i < n; i++) {
                if (state[i] == 1) {
                    if (i > 0) next[i - 1] = 1;
                    if (i < n - 1) next[i + 1] = 1;
                }
            }
            state = next;
            time++;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — BFS (Multi-Source) with queue
    // Time: O(n)  |  Space: O(n)
    // Use BFS starting from all initially infected cells at time 0.
    // Standard multi-source BFS; return the time of last infection.
    // ============================================================
    public static int optimal(int[] A) {
        int n = A.length;
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            if (A[i] == 1) {
                dist[i] = 0;
                q.add(i);
            }
        }

        int maxTime = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int nb : new int[]{cur - 1, cur + 1}) {
                if (nb >= 0 && nb < n && dist[nb] == -1) {
                    dist[nb] = dist[cur] + 1;
                    maxTime = Math.max(maxTime, dist[nb]);
                    q.add(nb);
                }
            }
        }
        return maxTime;
    }

    // ============================================================
    // APPROACH 3: BEST — Gap Analysis (O(n), no queue)
    // Time: O(n)  |  Space: O(1)
    // Scan left-to-right tracking distance from last seen infected.
    // Then scan right-to-left. For each cell, actual infection time
    // = min of forward and backward distances from nearest infected.
    // Answer = max over all cells of min(fwd[i], bwd[i]).
    // ============================================================
    public static int best(int[] A) {
        int n = A.length;
        int[] fwd = new int[n];  // distance from nearest infected to the left
        int[] bwd = new int[n];  // distance from nearest infected to the right

        // Forward pass
        int dist = Integer.MAX_VALUE / 2;
        for (int i = 0; i < n; i++) {
            if (A[i] == 1) dist = 0;
            else dist++;
            fwd[i] = dist;
        }

        // Backward pass
        dist = Integer.MAX_VALUE / 2;
        for (int i = n - 1; i >= 0; i--) {
            if (A[i] == 1) dist = 0;
            else dist++;
            bwd[i] = dist;
        }

        int maxTime = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == 0) {
                maxTime = Math.max(maxTime, Math.min(fwd[i], bwd[i]));
            }
        }
        return maxTime;
    }

    public static void main(String[] args) {
        System.out.println("=== Red Zone ===");
        int[][] tests = {
            {0, 1, 0, 0, 0, 1, 0},  // gaps: 1 left, 3 middle (ceil=2), 1 right => max=2
            {1, 0, 0, 0, 0, 0, 1},  // middle gap = 5, ceil(5/2)=3 => max=3
            {1, 1, 1},              // all infected => 0
            {0, 0, 0, 1},           // left tail of 3 => max=3
            {1, 0, 0, 0},           // right tail of 3 => max=3
        };
        int[] expected = {2, 3, 0, 3, 3};
        for (int t = 0; t < tests.length; t++) {
            int bf = bruteForce(tests[t]);
            int op = optimal(tests[t]);
            int be = best(tests[t]);
            System.out.printf("A=%s -> Brute=%d, Optimal=%d, Best=%d | Expected=%d%n",
                    Arrays.toString(tests[t]), bf, op, be, expected[t]);
        }
    }
}
