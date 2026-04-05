/**
 * Problem: Candy Distribution
 * Difficulty: HARD | XP: 50
 *
 * N children in a row with ratings. Rules:
 * 1. Each child gets at least 1 candy.
 * 2. A child with a higher rating than a neighbor gets more candies than that neighbor.
 * Return the minimum total candies needed.
 * Real-life use: Fair resource allocation, performance-based reward systems.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Repeatedly scan the array; whenever a constraint is violated, add a candy.
    // Continue until no changes are needed.
    // Time: O(N^2)  |  Space: O(N)
    // ============================================================
    static class BruteForce {
        public static int candy(int[] ratings) {
            int n = ratings.length;
            int[] candies = new int[n];
            Arrays.fill(candies, 1);
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int i = 0; i < n; i++) {
                    if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
                        candies[i] = candies[i - 1] + 1;
                        changed = true;
                    }
                    if (i < n - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                        candies[i] = candies[i + 1] + 1;
                        changed = true;
                    }
                }
            }
            return Arrays.stream(candies).sum();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Two-pass greedy:
    // Pass 1 (left→right): if rating[i] > rating[i-1], candy[i] = candy[i-1]+1.
    // Pass 2 (right→left): if rating[i] > rating[i+1], candy[i] = max(candy[i], candy[i+1]+1).
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    static class Optimal {
        public static int candy(int[] ratings) {
            int n = ratings.length;
            int[] candies = new int[n];
            Arrays.fill(candies, 1);
            // Left to right
            for (int i = 1; i < n; i++) {
                if (ratings[i] > ratings[i - 1]) {
                    candies[i] = candies[i - 1] + 1;
                }
            }
            // Right to left
            for (int i = n - 2; i >= 0; i--) {
                if (ratings[i] > ratings[i + 1]) {
                    candies[i] = Math.max(candies[i], candies[i + 1] + 1);
                }
            }
            return Arrays.stream(candies).sum();
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Single-pass O(1) space using slope counting (up/down/peak tracking).
    // Track ascending slope length, descending slope length, and peak.
    // When slope direction changes, account for the peak correctly.
    // Time: O(N)  |  Space: O(1)
    // ============================================================
    static class Best {
        public static int candy(int[] ratings) {
            if (ratings.length == 1) return 1;
            int n = ratings.length;
            int total = 1; // first child always gets 1
            int up = 0, down = 0, peak = 0;
            for (int i = 1; i < n; i++) {
                if (ratings[i] > ratings[i - 1]) {
                    // Ascending slope
                    up++;
                    down = 0;
                    peak = up;
                    total += up + 1;
                } else if (ratings[i] == ratings[i - 1]) {
                    // Flat: reset all
                    up = 0; down = 0; peak = 0;
                    total += 1;
                } else {
                    // Descending slope
                    up = 0;
                    down++;
                    // If peak is no longer high enough, "extend" it by 1
                    total += down + 1 + (peak >= down ? 0 : 1);
                }
            }
            return total;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Candy Distribution ===");

        int[][] tests = {
            {1, 0, 2},             // 5
            {1, 2, 2},             // 4
            {1, 3, 2, 2, 1},       // 7
            {1, 6, 10, 8, 7, 3, 2}, // 18
            {1},                   // 1
        };

        for (int[] r : tests) {
            System.out.printf("%nratings=%s%n", Arrays.toString(r));
            System.out.println("  Brute  : " + BruteForce.candy(r));
            System.out.println("  Optimal: " + Optimal.candy(r));
            System.out.println("  Best   : " + Best.candy(r));
        }
    }
}
