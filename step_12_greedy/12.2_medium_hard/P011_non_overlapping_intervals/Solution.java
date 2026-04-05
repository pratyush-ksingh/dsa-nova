/**
 * Problem: Non Overlapping Intervals
 * LeetCode 435 | Difficulty: MEDIUM | XP: 25
 *
 * Given an array of intervals, return the minimum number of intervals to remove
 * to make the remaining intervals non-overlapping.
 *
 * Key Insight: Equivalent to finding the maximum set of non-overlapping intervals
 *              (Activity Selection), then answer = n - max_kept.
 *              Greedy: sort by end time, keep earliest-ending intervals.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (Try all subsets)
    // Time: O(2^n * n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Generate all 2^n subsets. For each subset, check if it is
         * non-overlapping (sort by start, verify no two intervals overlap).
         * Track the maximum non-overlapping subset size.
         * Answer = n - max_kept.
         */
        public int eraseOverlapIntervals(int[][] intervals) {
            int n = intervals.length;
            int maxKept = 0;
            for (int mask = 0; mask < (1 << n); mask++) {
                List<int[]> subset = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    if ((mask >> i & 1) == 1) subset.add(intervals[i]);
                }
                if (isNonOverlapping(subset)) {
                    maxKept = Math.max(maxKept, subset.size());
                }
            }
            return n - maxKept;
        }

        private boolean isNonOverlapping(List<int[]> subset) {
            subset.sort(Comparator.comparingInt(a -> a[0]));
            for (int i = 1; i < subset.size(); i++) {
                if (subset.get(i)[0] < subset.get(i - 1)[1]) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Greedy — sort by end time)
    // Time: O(n log n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Sort intervals by end time.
         * Greedily keep any interval whose start >= lastEnd.
         * This maximises the count of non-overlapping intervals kept
         * (Activity Selection Theorem proof: exchange argument).
         * Answer = n - kept.
         */
        public int eraseOverlapIntervals(int[][] intervals) {
            if (intervals.length == 0) return 0;
            Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
            int kept = 0;
            int lastEnd = Integer.MIN_VALUE;
            for (int[] iv : intervals) {
                if (iv[0] >= lastEnd) {
                    kept++;
                    lastEnd = iv[1];
                }
            }
            return intervals.length - kept;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Same greedy, count removals directly)
    // Time: O(n log n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Count removals directly: when an interval overlaps with the last
         * kept interval (current start < lastEnd), increment removals.
         * We keep the interval with the smaller end (already sorted ascending
         * by end, so lastEnd is always the smaller — no update on removal).
         */
        public int eraseOverlapIntervals(int[][] intervals) {
            if (intervals.length == 0) return 0;
            Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
            int removals = 0;
            int lastEnd = Integer.MIN_VALUE;
            for (int[] iv : intervals) {
                if (iv[0] < lastEnd) {
                    removals++;   // remove current (it ends later, worse for future)
                } else {
                    lastEnd = iv[1];
                }
            }
            return removals;
        }
    }

    public static void main(String[] args) {
        int[][][] tests = {
            {{1,2},{2,3},{3,4},{1,3}},
            {{1,2},{1,2},{1,2}},
            {{1,2},{2,3}},
            {{1,100},{11,22},{1,11},{2,12}}
        };
        int[] exps = {1, 2, 0, 2};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== Non Overlapping Intervals ===");
        for (int t = 0; t < tests.length; t++) {
            int b  = bf.eraseOverlapIntervals(tests[t]);
            int o  = opt.eraseOverlapIntervals(tests[t]);
            int be = bst.eraseOverlapIntervals(tests[t]);
            String status = (b == o && o == be && be == exps[t]) ? "OK" : "FAIL";
            System.out.printf("  brute=%d opt=%d best=%d (exp=%d) [%s]%n",
                              b, o, be, exps[t], status);
        }
    }
}
