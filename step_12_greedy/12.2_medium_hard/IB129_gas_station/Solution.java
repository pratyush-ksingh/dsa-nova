/**
 * Problem: Gas Station
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * N gas stations in a circle. gas[i] = gas available at station i.
 * cost[i] = gas needed to travel from station i to station i+1.
 * Find the starting station index to complete the full circuit (or -1 if impossible).
 * If a solution exists, it is guaranteed to be unique.
 *
 * @author DSA_Nova
 */

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Try each station as start
    // Time: O(N^2)  |  Space: O(1)
    // ============================================================
    public static int bruteForce(int[] gas, int[] cost) {
        int n = gas.length;
        for (int start = 0; start < n; start++) {
            int tank = 0;
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                int idx = (start + i) % n;
                tank += gas[idx] - cost[idx];
                if (tank < 0) { ok = false; break; }
            }
            if (ok) return start;
        }
        return -1;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Greedy single pass
    // Time: O(N)  |  Space: O(1)
    // If total gas >= total cost, solution exists.
    // The starting station is the one after the station where cumulative tank
    // hits its minimum. Equivalently: if tank drops below 0, reset start to i+1.
    // ============================================================
    public static int optimal(int[] gas, int[] cost) {
        int total = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            int diff = gas[i] - cost[i];
            total += diff;
            tank += diff;
            if (tank < 0) {
                start = i + 1;
                tank = 0;
            }
        }
        return total >= 0 ? start : -1;
    }

    // ============================================================
    // APPROACH 3: BEST - Minimum prefix sum approach
    // Time: O(N)  |  Space: O(1)
    // Compute net[i] = gas[i] - cost[i]. Find index of minimum prefix sum.
    // Start from the index after the minimum prefix (same result as greedy).
    // ============================================================
    public static int best(int[] gas, int[] cost) {
        int n = gas.length;
        int total = 0, minSum = 0, minIdx = -1;
        int prefixSum = 0;
        for (int i = 0; i < n; i++) {
            prefixSum += gas[i] - cost[i];
            total = prefixSum;
            if (prefixSum <= minSum) {
                minSum = prefixSum;
                minIdx = i;
            }
        }
        if (total < 0) return -1;
        return (minIdx + 1) % n;
    }

    public static void main(String[] args) {
        System.out.println("=== Gas Station ===");

        int[] gas1 = {1, 2, 3, 4, 5};
        int[] cost1 = {3, 4, 5, 1, 2};
        // Expected: 3
        System.out.printf("Test1: brute=%d opt=%d best=%d (exp=3)%n",
            bruteForce(gas1, cost1), optimal(gas1, cost1), best(gas1, cost1));

        int[] gas2 = {2, 3, 4};
        int[] cost2 = {3, 4, 3};
        // Expected: -1 (total gas 9 < total cost 10)
        System.out.printf("Test2: brute=%d opt=%d best=%d (exp=-1)%n",
            bruteForce(gas2, cost2), optimal(gas2, cost2), best(gas2, cost2));

        int[] gas3 = {5, 1, 2, 3, 4};
        int[] cost3 = {4, 4, 1, 5, 1};
        // Expected: 4
        System.out.printf("Test3: brute=%d opt=%d best=%d (exp=4)%n",
            bruteForce(gas3, cost3), optimal(gas3, cost3), best(gas3, cost3));

        int[] gas4 = {1};
        int[] cost4 = {1};
        System.out.printf("Test4: brute=%d opt=%d best=%d (exp=0)%n",
            bruteForce(gas4, cost4), optimal(gas4, cost4), best(gas4, cost4));
    }
}
