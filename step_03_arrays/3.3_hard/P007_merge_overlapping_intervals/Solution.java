import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2 log n)  |  Space: O(n)
// Repeatedly scan for any overlapping pair and merge until none left
// ============================================================
class BruteForce {
    public static int[][] solve(int[][] intervals) {
        List<int[]> list = new ArrayList<>(Arrays.asList(intervals));
        boolean merged = true;
        while (merged) {
            merged = false;
            list.sort(Comparator.comparingInt(a -> a[0]));
            List<int[]> newList = new ArrayList<>();
            boolean[] used = new boolean[list.size()];
            for (int i = 0; i < list.size(); i++) {
                if (used[i]) continue;
                int[] cur = list.get(i).clone();
                for (int j = i + 1; j < list.size(); j++) {
                    if (!used[j] && list.get(j)[0] <= cur[1]) {
                        cur[1] = Math.max(cur[1], list.get(j)[1]);
                        used[j] = true;
                        merged = true;
                    }
                }
                newList.add(cur);
            }
            list = newList;
        }
        return list.toArray(new int[0][]);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Sort + single linear scan
// Time: O(n log n)  |  Space: O(n)
// Sort by start time. Keep last interval in result list.
// If current interval overlaps last, extend it; otherwise add new.
// ============================================================
class Optimal {
    public static int[][] solve(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval.clone());
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(
                    merged.get(merged.size() - 1)[1], interval[1]);
            }
        }
        return merged.toArray(new int[0][]);
    }
}

// ============================================================
// APPROACH 3: BEST - In-place sort + stack-based merge
// Time: O(n log n)  |  Space: O(n)
// Use a stack to make the merge logic explicit and easy to reason about
// ============================================================
class Best {
    public static int[][] solve(int[][] intervals) {
        if (intervals.length == 0) return intervals;
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(intervals[0].clone());
        for (int i = 1; i < intervals.length; i++) {
            int[] top = stack.peek();
            if (intervals[i][0] <= top[1]) {
                top[1] = Math.max(top[1], intervals[i][1]);
            } else {
                stack.push(intervals[i].clone());
            }
        }
        int[][] res = stack.toArray(new int[0][]);
        // stack is LIFO, reverse for sorted order
        Arrays.sort(res, (a, b) -> a[0] - b[0]);
        return res;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Merge Overlapping Intervals ===");

        int[][][] tests = {
            {{1,3},{2,6},{8,10},{15,18}},  // expected [[1,6],[8,10],[15,18]]
            {{1,4},{4,5}},                  // expected [[1,5]]
            {{1,4},{0,4}},                  // expected [[0,4]]
        };

        for (int[][] intervals : tests) {
            System.out.println("Input: " + Arrays.deepToString(intervals));
            System.out.println("Brute:   " + Arrays.deepToString(BruteForce.solve(intervals)));
            System.out.println("Optimal: " + Arrays.deepToString(Optimal.solve(intervals)));
            System.out.println("Best:    " + Arrays.deepToString(Best.solve(intervals)));
            System.out.println();
        }
    }
}
