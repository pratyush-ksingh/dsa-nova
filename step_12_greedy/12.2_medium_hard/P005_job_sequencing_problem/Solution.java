/**
 * Problem: Job Sequencing Problem
 * Difficulty: MEDIUM | XP: 25
 *
 * Given N jobs each with a deadline and profit, schedule jobs to maximize
 * total profit. Each job takes 1 unit of time. A job must complete by deadline.
 * Returns [count_of_jobs, max_profit].
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Try all permutations — only feasible for tiny N)
// Time: O(n! * n) | Space: O(n)
// ============================================================
class BruteForce {
    static int bestProfit;
    static int bestCount;

    public static int[] solve(int[][] jobs) {
        // jobs[i] = [id, deadline, profit]
        int n = jobs.length;
        int maxDeadline = 0;
        for (int[] job : jobs) maxDeadline = Math.max(maxDeadline, job[1]);

        bestProfit = 0;
        bestCount = 0;

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        permute(perm, 0, jobs, maxDeadline);

        return new int[]{bestCount, bestProfit};
    }

    private static void permute(int[] perm, int start, int[][] jobs, int maxDeadline) {
        if (start == perm.length) {
            boolean[] slots = new boolean[maxDeadline + 1];
            int profit = 0, count = 0;
            for (int idx : perm) {
                int deadline = jobs[idx][1];
                int p = jobs[idx][2];
                for (int t = 1; t <= deadline; t++) {
                    if (!slots[t]) {
                        slots[t] = true;
                        profit += p;
                        count++;
                        break;
                    }
                }
            }
            if (profit > bestProfit) {
                bestProfit = profit;
                bestCount = count;
            }
            return;
        }
        for (int i = start; i < perm.length; i++) {
            int tmp = perm[start]; perm[start] = perm[i]; perm[i] = tmp;
            permute(perm, start + 1, jobs, maxDeadline);
            tmp = perm[start]; perm[start] = perm[i]; perm[i] = tmp;
        }
    }
}

// ============================================================
// Approach 2: Optimal (Greedy — sort by profit desc, linear slot scan)
// Time: O(n²) | Space: O(n)
// ============================================================
class Optimal {
    public static int[] solve(int[][] jobs) {
        // Sort by profit descending
        int[][] sorted = jobs.clone();
        Arrays.sort(sorted, (a, b) -> b[2] - a[2]);

        int maxDeadline = 0;
        for (int[] job : jobs) maxDeadline = Math.max(maxDeadline, job[1]);

        boolean[] slots = new boolean[maxDeadline + 1];
        int totalProfit = 0, count = 0;

        for (int[] job : sorted) {
            int deadline = job[1], profit = job[2];
            // Find latest free slot at or before deadline
            for (int t = Math.min(deadline, maxDeadline); t >= 1; t--) {
                if (!slots[t]) {
                    slots[t] = true;
                    totalProfit += profit;
                    count++;
                    break;
                }
            }
        }
        return new int[]{count, totalProfit};
    }
}

// ============================================================
// Approach 3: Best (Greedy + Union-Find for O(α(n)) slot lookup)
// Time: O(n log n + n * α(n)) | Space: O(n)
// ============================================================
class Best {
    static int[] parent;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    public static int[] solve(int[][] jobs) {
        int[][] sorted = jobs.clone();
        Arrays.sort(sorted, (a, b) -> b[2] - a[2]);

        int maxDeadline = 0;
        for (int[] job : jobs) maxDeadline = Math.max(maxDeadline, job[1]);

        parent = new int[maxDeadline + 1];
        for (int i = 0; i <= maxDeadline; i++) parent[i] = i;

        int totalProfit = 0, count = 0;

        for (int[] job : sorted) {
            int deadline = Math.min(job[1], maxDeadline);
            int freeSlot = find(deadline);
            if (freeSlot > 0) {
                totalProfit += job[2];
                count++;
                parent[freeSlot] = freeSlot - 1; // union to previous
            }
        }
        return new int[]{count, totalProfit};
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Job Sequencing Problem ===\n");

        // jobs[i] = [id, deadline, profit]
        int[][][] inputs = {
            {{1, 4, 20}, {2, 1, 10}, {3, 1, 40}, {4, 1, 30}},
            {{1, 2, 100}, {2, 1, 19}, {3, 2, 27}, {4, 1, 25}, {5, 1, 15}},
            {{1, 1, 5}, {2, 1, 10}},
            {{1, 3, 50}, {2, 2, 30}, {3, 1, 20}},
        };
        int[][] expecteds = {{2, 60}, {2, 127}, {1, 10}, {3, 100}};

        for (int t = 0; t < inputs.length; t++) {
            int[] b = BruteForce.solve(inputs[t]);
            int[] o = Optimal.solve(inputs[t]);
            int[] h = Best.solve(inputs[t]);
            boolean pass = Arrays.equals(o, expecteds[t]) && Arrays.equals(h, expecteds[t]);

            System.out.println("Jobs: " + jobsToString(inputs[t]));
            System.out.println("Brute:    " + Arrays.toString(b));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(h));
            System.out.println("Expected: " + Arrays.toString(expecteds[t]));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }

    private static String jobsToString(int[][] jobs) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < jobs.length; i++) {
            sb.append(Arrays.toString(jobs[i]));
            if (i < jobs.length - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }
}
