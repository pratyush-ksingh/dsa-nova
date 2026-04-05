/**
 * Problem: Merge Intervals (LeetCode 56)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of intervals, merge all overlapping intervals and return
 * an array of non-overlapping intervals that cover all input intervals.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Nested repeated merge O(n²)
    // Time: O(n²)  |  Space: O(n)
    // ============================================================
    /**
     * Repeatedly scan all pairs and merge any two overlapping intervals
     * until a full pass finds no more merges possible.
     */
    public static int[][] bruteForce(int[][] intervals) {
        List<int[]> list = new ArrayList<>();
        for (int[] iv : intervals) list.add(iv.clone());

        boolean changed = true;
        while (changed) {
            changed = false;
            List<int[]> next = new ArrayList<>();
            boolean[] used = new boolean[list.size()];
            for (int i = 0; i < list.size(); i++) {
                if (used[i]) continue;
                int[] a = list.get(i).clone();
                for (int j = i + 1; j < list.size(); j++) {
                    if (used[j]) continue;
                    int[] b = list.get(j);
                    // Overlap check
                    if (a[0] <= b[1] && b[0] <= a[1]) {
                        a[0] = Math.min(a[0], b[0]);
                        a[1] = Math.max(a[1], b[1]);
                        used[j] = true;
                        changed = true;
                    }
                }
                next.add(a);
            }
            list = next;
        }

        list.sort((a, b) -> a[0] - b[0]);
        return list.toArray(new int[0][]);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Sort by start, single-pass merge O(n log n)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Sort intervals by start time. Iterate: if current.start <= last.end,
     * merge by updating last.end = max(last.end, current.end).
     * Otherwise, add current as a new interval.
     */
    public static int[][] optimal(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0].clone());

        for (int i = 1; i < intervals.length; i++) {
            int[] curr = intervals[i];
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
    // APPROACH 3: BEST — Same approach, variable tracking for clarity
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Sort intervals, then track current merge window [curStart, curEnd]
     * explicitly as variables. When a gap is found, flush the window
     * to result and start a new window. Slightly cleaner than using
     * the last element of the result list as the "active" interval.
     */
    public static int[][] best(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        List<int[]> result = new ArrayList<>();
        int curStart = intervals[0][0];
        int curEnd = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            int start = intervals[i][0];
            int end   = intervals[i][1];
            if (start <= curEnd) {
                curEnd = Math.max(curEnd, end);
            } else {
                result.add(new int[]{curStart, curEnd});
                curStart = start;
                curEnd = end;
            }
        }
        result.add(new int[]{curStart, curEnd});
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
        System.out.println("=== Merge Intervals ===");

        int[][][] tests = {
            {{1,3},{2,6},{8,10},{15,18}},
            {{1,4},{4,5}},
            {{1,4},{2,3}},
            {{1,4},{0,2},{3,5}},
            {{1,2}}
        };
        String[] expected = {
            "[[1,6],[8,10],[15,18]]",
            "[[1,5]]",
            "[[1,4]]",
            "[[0,5]]",
            "[[1,2]]"
        };

        for (int i = 0; i < tests.length; i++) {
            System.out.println("Input: " + fmt(tests[i]));
            System.out.println("  Brute:   " + fmt(bruteForce(tests[i])) + " (expected " + expected[i] + ")");
            System.out.println("  Optimal: " + fmt(optimal(tests[i])));
            System.out.println("  Best:    " + fmt(best(tests[i])));
        }
    }
}
