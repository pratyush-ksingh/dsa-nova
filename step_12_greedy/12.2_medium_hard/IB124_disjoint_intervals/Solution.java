/**
 * Problem: Disjoint Intervals
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a list of intervals, find the maximum number of non-overlapping intervals.
 * Equivalent to: Activity Selection Problem.
 * Real-life use: Meeting room scheduling, CPU task scheduling, resource allocation.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Try all 2^N subsets; for each subset check if all intervals are disjoint,
    // track the max size subset.
    // Time: O(2^N * N^2)  |  Space: O(N)
    // ============================================================
    static class BruteForce {
        public static int maxDisjoint(int[][] intervals) {
            int n = intervals.length;
            int max = 0;
            for (int mask = 0; mask < (1 << n); mask++) {
                List<int[]> chosen = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) chosen.add(intervals[i]);
                }
                if (areDisjoint(chosen)) max = Math.max(max, chosen.size());
            }
            return max;
        }

        private static boolean areDisjoint(List<int[]> intervals) {
            for (int i = 0; i < intervals.size(); i++) {
                for (int j = i + 1; j < intervals.size(); j++) {
                    int[] a = intervals.get(i), b = intervals.get(j);
                    if (a[0] < b[1] && b[0] < a[1]) return false;
                }
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Greedy: sort by end time. Always pick the interval that ends earliest
    // (leaves maximum room for future intervals).
    // Time: O(N log N)  |  Space: O(1) extra (excluding sort)
    // ============================================================
    static class Optimal {
        public static int maxDisjoint(int[][] intervals) {
            if (intervals.length == 0) return 0;
            Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
            int count = 1;
            int lastEnd = intervals[0][1];
            for (int i = 1; i < intervals.length; i++) {
                if (intervals[i][0] >= lastEnd) { // non-overlapping
                    count++;
                    lastEnd = intervals[i][1];
                }
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same greedy but uses Integer comparator to handle edge case with
    // intervals sharing endpoints (closed vs open).
    // Also returns the selected intervals for visualization.
    // Time: O(N log N)  |  Space: O(N) for output
    // ============================================================
    static class Best {
        public static int maxDisjoint(int[][] intervals) {
            if (intervals.length == 0) return 0;
            int[][] sorted = intervals.clone();
            Arrays.sort(sorted, Comparator.comparingInt(a -> a[1]));
            int count = 1;
            int lastEnd = sorted[0][1];
            for (int i = 1; i < sorted.length; i++) {
                if (sorted[i][0] >= lastEnd) {
                    count++;
                    lastEnd = sorted[i][1];
                }
            }
            return count;
        }

        public static List<int[]> getDisjointIntervals(int[][] intervals) {
            if (intervals.length == 0) return new ArrayList<>();
            int[][] sorted = intervals.clone();
            Arrays.sort(sorted, Comparator.comparingInt(a -> a[1]));
            List<int[]> result = new ArrayList<>();
            result.add(sorted[0]);
            int lastEnd = sorted[0][1];
            for (int i = 1; i < sorted.length; i++) {
                if (sorted[i][0] >= lastEnd) {
                    result.add(sorted[i]);
                    lastEnd = sorted[i][1];
                }
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Disjoint Intervals (Activity Selection) ===");

        int[][][] tests = {
            {{1, 4}, {2, 3}, {4, 6}, {8, 11}, {9, 12}},
            {{1, 2}, {2, 3}, {3, 4}, {1, 3}},
            {{1, 10}},
        };

        for (int[][] test : tests) {
            System.out.printf("%nIntervals: %s%n", Arrays.deepToString(test));
            System.out.println("  Brute  : " + BruteForce.maxDisjoint(test.clone()));
            System.out.println("  Optimal: " + Optimal.maxDisjoint(test.clone()));
            System.out.println("  Best   : " + Best.maxDisjoint(test.clone()));
            System.out.print("  Selected: ");
            for (int[] iv : Best.getDisjointIntervals(test.clone())) {
                System.out.print(Arrays.toString(iv) + " ");
            }
            System.out.println();
        }
    }
}
