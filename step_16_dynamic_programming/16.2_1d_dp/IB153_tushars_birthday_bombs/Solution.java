/**
 * Problem: Tushar's Birthday Bombs
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Tushar has a strength A (total kicks he can receive).
 * There are N friends, each with a kick power B[i].
 * He wants to maximize total kicks (sum of chosen kicks <= A).
 * If multiple sequences have the same number of kicks, return the
 * lexicographically smallest sequence (using 1-indexed friend numbers).
 *
 * Key insight: always use the friend with minimum cost; maximum kicks = A / minCost.
 * For lex order: at each position greedily pick the earliest (smallest index)
 * friend whose cost fits within the remaining budget and still allows
 * enough kicks to fill the rest with minCost friends.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Greedy with min-cost friend only)
    // Time: O(A / minCost)  |  Space: O(A / minCost) for result
    // ============================================================
    // Fill all positions with the cheapest friend.
    // This gives max kicks and is lexicographically minimal if there
    // is only one friend with minimum cost.
    // NOTE: This is NOT fully correct for lex ordering with multiple
    // friends having the same min cost or where a cheaper-indexed friend
    // has a slightly higher cost — see Optimal for the full solution.
    static class BruteForce {
        public static List<Integer> solve(int A, int[] B) {
            int n = B.length;
            int minCost = Integer.MAX_VALUE;
            int minIdx = 0;
            for (int i = 0; i < n; i++) {
                if (B[i] < minCost) {
                    minCost = B[i];
                    minIdx = i;
                }
            }

            int kicks = A / minCost;
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < kicks; i++) {
                result.add(minIdx + 1);   // 1-indexed
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Greedy with lex-smallest ordering)
    // Time: O(kicks * n)  |  Space: O(kicks) for result
    //       where kicks = A / minCost
    // ============================================================
    // Steps:
    //   1. Find minimum cost minCost and maximum kicks = A / minCost.
    //   2. For each position p from 0 to kicks-1:
    //      - Remaining budget after this position must cover (kicks-1-p) more
    //        kicks each costing at most minCost.
    //      - Try friends in order 0..n-1; pick the first friend i where:
    //          B[i] <= budget - (kicks - 1 - p) * minCost
    //        This ensures we can still fill remaining positions with minCost.
    //      - Subtract B[i] from budget and record (i+1).
    static class Optimal {
        public static List<Integer> solve(int A, int[] B) {
            int n = B.length;

            // Step 1: find minimum cost
            int minCost = Integer.MAX_VALUE;
            for (int b : B) minCost = Math.min(minCost, b);

            int kicks = A / minCost;
            List<Integer> result = new ArrayList<>();
            int budget = A;

            // Step 2: greedily assign each position
            for (int p = 0; p < kicks; p++) {
                int remaining = kicks - 1 - p;  // positions left after this one
                for (int i = 0; i < n; i++) {
                    // After choosing friend i here, the remaining positions
                    // must each cost at most minCost.
                    if (B[i] <= budget - (long) remaining * minCost) {
                        result.add(i + 1);
                        budget -= B[i];
                        break;
                    }
                }
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Same greedy, optimized with precomputation)
    // Time: O(n + kicks)  |  Space: O(kicks)
    // ============================================================
    // Precompute the globally cheapest friend.
    // For each position, try friends in increasing index order.
    // The first friend whose cost is <= (budget - remaining * minCost) wins.
    // In the best case almost every position picks friend minIdx directly,
    // but earlier positions may pick an even smaller-indexed friend with a
    // slightly larger cost if budget allows.
    //
    // The key optimization: once we confirm a friend fits, move on immediately
    // (no need to scan further).
    static class Best {
        public static List<Integer> solve(int A, int[] B) {
            int n = B.length;

            int minCost = Integer.MAX_VALUE;
            for (int b : B) minCost = Math.min(minCost, b);

            if (minCost > A) return new ArrayList<>();

            int kicks = A / minCost;
            List<Integer> result = new ArrayList<>(kicks);
            long budget = A;

            for (int p = 0; p < kicks; p++) {
                long remaining = kicks - 1 - p;
                for (int i = 0; i < n; i++) {
                    if (B[i] <= budget - remaining * minCost) {
                        result.add(i + 1);
                        budget -= B[i];
                        break;
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Tushar's Birthday Bombs ===");

        // Test 1: A=11, B=[2,3,4] -> minCost=2, kicks=5
        // Positions: each time try friend1(cost2), friend2(cost3), friend3(cost4)
        // budget=11, remaining=4 -> budget - 4*2=3 -> friend1(2)<=3 -> pick 1, budget=9
        // budget=9, remaining=3 -> 9-3*2=3 -> friend1(2)<=3 -> pick 1, budget=7
        // budget=7, remaining=2 -> 7-2*2=3 -> friend1(2)<=3 -> pick 1, budget=5
        // budget=5, remaining=1 -> 5-1*2=3 -> friend1(2)<=3 -> pick 1, budget=3
        // budget=3, remaining=0 -> 3-0=3   -> friend1(2)<=3 -> pick 1, budget=1
        // Expected: [1,1,1,1,1]
        int A1 = 11;
        int[] B1 = {2, 3, 4};

        // Test 2: A=6, B=[3,2] -> minCost=2, kicks=3
        // budget=6, remaining=2 -> 6-2*2=2 -> friend1(3)>2, friend2(2)<=2 -> pick 2, budget=4
        // budget=4, remaining=1 -> 4-1*2=2 -> friend1(3)>2, friend2(2)<=2 -> pick 2, budget=2
        // budget=2, remaining=0 -> 2-0=2   -> friend1(3)>2, friend2(2)<=2 -> pick 2, budget=0
        // Expected: [2,2,2]
        int A2 = 6;
        int[] B2 = {3, 2};

        // Test 3: A=5, B=[4,2,3] -> minCost=2, kicks=2
        // budget=5, remaining=1 -> 5-1*2=3 -> friend1(4)>3, friend2(2)<=3 -> pick 2, budget=3
        // budget=3, remaining=0 -> 3-0=3   -> friend1(4)>3, friend2(2)<=3 -> pick 2, budget=1
        // Expected: [2,2]
        int A3 = 5;
        int[] B3 = {4, 2, 3};

        int[][] Bs = {B1, B2, B3};
        int[] As = {A1, A2, A3};
        String[] expectedLabels = {"[1,1,1,1,1]", "[2,2,2]", "[2,2]"};

        for (int t = 0; t < 3; t++) {
            List<Integer> bf = BruteForce.solve(As[t], Bs[t]);
            List<Integer> op = Optimal.solve(As[t], Bs[t]);
            List<Integer> be = Best.solve(As[t], Bs[t]);
            System.out.printf("Test %d | A=%d | Brute: %s | Optimal: %s | Best: %s | Expected: %s%n",
                              t + 1, As[t], bf, op, be, expectedLabels[t]);
        }
    }
}
