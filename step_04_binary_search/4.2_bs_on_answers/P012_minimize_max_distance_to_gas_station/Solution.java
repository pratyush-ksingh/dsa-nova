/**
 * Problem: Minimize Max Distance to Gas Station
 * Difficulty: HARD | XP: 50
 *
 * Given existing gas stations on a number line and K new stations to add,
 * place the new stations to minimize the maximum distance between adjacent stations.
 * Return the answer with precision 1e-6.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (Greedy with priority queue)
    // Time: O(K * n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Greedily place each new station in the largest gap using a max-heap.
     * Real-life: Optimally placing cell towers to minimize coverage gaps.
     */
    public static double bruteForce(int[] stations, int k) {
        int n = stations.length;
        // counts[i] = number of new stations placed in gap i (between stations[i] and stations[i+1])
        int[] counts = new int[n - 1];
        // Max-heap: (current_section_length, gap_index)
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(b[0], a[0]));
        for (int i = 0; i < n - 1; i++) {
            double gap = stations[i + 1] - stations[i];
            pq.offer(new double[]{gap, i});
        }

        for (int i = 0; i < k; i++) {
            double[] top = pq.poll();
            int idx = (int) top[1];
            counts[idx]++;
            double newSectionLen = (double)(stations[idx + 1] - stations[idx]) / (counts[idx] + 1);
            pq.offer(new double[]{newSectionLen, idx});
        }

        double maxDist = 0;
        for (int i = 0; i < n - 1; i++) {
            maxDist = Math.max(maxDist, (double)(stations[i + 1] - stations[i]) / (counts[i] + 1));
        }
        return maxDist;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Binary Search on Answer — floating point)
    // Time: O(n log(max_gap / 1e-6))  |  Space: O(1)
    // ============================================================
    /**
     * Binary search on the answer (max distance D). For a given D,
     * count minimum new stations needed: for each gap, ceil(gap/D)-1 stations.
     * If total <= k, D is achievable.
     * Real-life: Minimizing maximum travel distance between distribution centers.
     */
    public static double optimal(int[] stations, int k) {
        double lo = 0;
        double hi = stations[stations.length - 1] - stations[0];
        // Iterate enough times for precision
        for (int iter = 0; iter < 100; iter++) {
            double mid = (lo + hi) / 2;
            if (canPlace(stations, k, mid)) hi = mid;
            else lo = mid;
        }
        return hi;
    }

    private static boolean canPlace(int[] stations, int k, double maxDist) {
        int needed = 0;
        for (int i = 0; i < stations.length - 1; i++) {
            double gap = stations[i + 1] - stations[i];
            needed += (int)(gap / maxDist); // number of sections = ceil(gap/maxDist), stations = sections - 1
        }
        return needed <= k;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n log(max_gap / precision))  |  Space: O(1)
    // ============================================================
    /**
     * Same binary search but uses explicit epsilon termination.
     * Avoids fixed iteration count — terminates when hi-lo < 1e-6.
     * Real-life: Precision-critical placement in geographic information systems.
     */
    public static double best(int[] stations, int k) {
        double lo = 0;
        double hi = stations[stations.length - 1] - stations[0];
        while (hi - lo > 1e-6) {
            double mid = (lo + hi) / 2;
            if (canPlace(stations, k, mid)) hi = mid;
            else lo = mid;
        }
        return hi;
    }

    public static void main(String[] args) {
        System.out.println("=== Minimize Max Distance to Gas Station ===");

        int[][] tests = {
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
            {1, 5, 10},
        };
        int[] ks = {9, 1};
        double[] expected = {0.5, 2.5};

        for (int t = 0; t < tests.length; t++) {
            System.out.println("\nStations: " + Arrays.toString(tests[t]) + "  K=" + ks[t]);
            System.out.printf("Expected: %.6f%n", expected[t]);
            System.out.printf("Brute:   %.6f%n", bruteForce(tests[t], ks[t]));
            System.out.printf("Optimal: %.6f%n", optimal(tests[t], ks[t]));
            System.out.printf("Best:    %.6f%n", best(tests[t], ks[t]));
        }
    }
}
