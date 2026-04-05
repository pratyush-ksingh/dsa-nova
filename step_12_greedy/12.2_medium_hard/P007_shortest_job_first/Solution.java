/**
 * Problem: Shortest Job First (SJF) Scheduling
 * Difficulty: EASY | XP: 10
 *
 * Given burst times of n processes (all arrive at time 0),
 * compute the average waiting time using SJF (non-preemptive).
 * Sort by burst time, compute prefix sum of waits.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // SORT + PREFIX SUM (Optimal)
    // Time: O(n log n) | Space: O(1) with in-place sort
    // ============================================================
    public static double averageWaitingTime(int[] burst) {
        int n = burst.length;
        Arrays.sort(burst);

        long totalWait = 0;
        long currentTime = 0;

        for (int i = 0; i < n; i++) {
            totalWait += currentTime;   // process i waits for all previous jobs
            currentTime += burst[i];    // process i now runs
        }

        return (double) totalWait / n;
    }

    public static void main(String[] args) {
        System.out.println("=== Shortest Job First ===\n");

        int[][] testCases = {
            {4, 3, 7, 1, 2},
            {1, 2, 3},
            {5, 5, 5},
            {10},
            {2, 1},
            {6, 2, 8, 3, 1},
        };
        double[] expected = {4.0, 1.0, 5.0, 0.0, 1.0, 3.6};

        for (int t = 0; t < testCases.length; t++) {
            double result = averageWaitingTime(testCases[t].clone());
            boolean pass = Math.abs(result - expected[t]) < 0.01;
            System.out.println("burst = " + Arrays.toString(testCases[t]));
            System.out.printf("  Result: %.2f | Expected: %.2f | Pass: %b%n%n", result, expected[t], pass);
        }
    }
}
