/**
 * Problem: Insert Interval (LeetCode 57)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a list of non-overlapping intervals sorted by start time, and a
 * new interval, insert it and merge any overlapping intervals.
 * Return the resulting sorted list of non-overlapping intervals.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Add, Sort, Merge
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Add newInterval to the list, sort by start, then apply standard merge.
     * Simple but doesn't exploit the fact that input is already sorted.
     */
    public static int[][] bruteForce(int[][] intervals, int[] newInterval) {
        // Combine intervals + newInterval into one list
        List<int[]> all = new ArrayList<>(Arrays.asList(intervals));
        all.add(newInterval);
        all.sort((a, b) -> a[0] - b[0]);

        List<int[]> merged = new ArrayList<>();
        merged.add(all.get(0).clone());
        for (int i = 1; i < all.size(); i++) {
            int[] curr = all.get(i);
            int[] last = merged.get(merged.size() - 1);
            if (curr[0] <= last[1]) {
                last[1] = Math.max(last[1], curr[1]);
            } else {
                merged.add(curr.clone());
            }
        }
        return merged.toArray(new int[0][]);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Linear 3-phase Scan
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Exploit sorted order with 3 phases:
     * 1. Copy all intervals that end before newInterval starts.
     * 2. Merge all intervals overlapping with newInterval.
     * 3. Copy all intervals that start after newInterval ends.
     */
    public static int[][] optimal(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;

        // Phase 1: strictly before newInterval
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i++]);
        }

        // Phase 2: merge overlapping intervals
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);

        // Phase 3: strictly after newInterval
        while (i < n) {
            result.add(intervals[i++]);
        }

        return result.toArray(new int[0][]);
    }

    // ============================================================
    // APPROACH 3: BEST — Single-pass with insertion flag
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Single loop: for each existing interval, decide whether it is:
     *  - Before newInterval (add it as-is)
     *  - After newInterval (insert newInterval first if not done, then add it)
     *  - Overlapping (expand newInterval to cover both)
     * If loop ends and newInterval was never inserted, append it.
     */
    public static int[][] best(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        boolean inserted = false;

        for (int[] interval : intervals) {
            if (interval[1] < newInterval[0]) {
                // Ends before newInterval starts — no overlap
                result.add(interval);
            } else if (interval[0] > newInterval[1]) {
                // Starts after newInterval ends — insert newInterval first
                if (!inserted) {
                    result.add(newInterval);
                    inserted = true;
                }
                result.add(interval);
            } else {
                // Overlap: absorb into newInterval
                newInterval[0] = Math.min(newInterval[0], interval[0]);
                newInterval[1] = Math.max(newInterval[1], interval[1]);
            }
        }

        if (!inserted) result.add(newInterval);
        return result.toArray(new int[0][]);
    }

    private static String fmt(int[][] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int[] a : arr) sb.append(Arrays.toString(a)).append(",");
        if (arr.length > 0) sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Insert Interval ===");

        int[][][] intervalSets = {
            {{1,3},{6,9}},
            {{1,2},{3,5},{6,7},{8,10},{12,16}},
            {},
            {{1,5}},
            {{1,5}}
        };
        int[][] newIntervals = {{2,5},{4,8},{5,7},{2,3},{0,0}};
        String[] expected = {"[[1,5],[6,9]]","[[1,2],[3,10],[12,16]]","[[5,7]]","[[1,5]]","[[0,0],[1,5]]"};

        for (int i = 0; i < intervalSets.length; i++) {
            System.out.println("intervals=" + fmt(intervalSets[i]) + ", new=" + Arrays.toString(newIntervals[i]));
            System.out.println("  Brute:   " + fmt(bruteForce(intervalSets[i], newIntervals[i].clone()))
                + " (expected " + expected[i] + ")");
            System.out.println("  Optimal: " + fmt(optimal(intervalSets[i], newIntervals[i].clone())));
            System.out.println("  Best:    " + fmt(best(intervalSets[i], newIntervals[i].clone())));
        }
    }
}
