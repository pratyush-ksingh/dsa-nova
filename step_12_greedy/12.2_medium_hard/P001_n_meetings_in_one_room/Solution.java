/**
 * Problem: N Meetings in One Room (Activity Selection)
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- All subsets
    // Time: O(2^n * n)  |  Space: O(n)
    // ============================================================
    public static int bruteForce(int[] start, int[] end) {
        int n = start.length;
        int maxCount = 0;
        // Try all 2^n subsets (only feasible for small n)
        for (int mask = 0; mask < (1 << n); mask++) {
            List<int[]> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(new int[]{start[i], end[i]});
                }
            }
            // Sort by start time and check non-overlapping
            subset.sort((a, b) -> a[0] - b[0]);
            boolean valid = true;
            for (int i = 1; i < subset.size(); i++) {
                if (subset.get(i)[0] < subset.get(i - 1)[1]) {
                    valid = false;
                    break;
                }
            }
            if (valid) maxCount = Math.max(maxCount, subset.size());
        }
        return maxCount;
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Sort by end time + Greedy
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    public static int maxMeetings(int[] start, int[] end) {
        int n = start.length;
        // Create meeting indices and sort by end time
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> {
            if (end[a] != end[b]) return end[a] - end[b];
            return start[a] - start[b];
        });

        int count = 0;
        int lastEnd = -1;
        List<Integer> selected = new ArrayList<>();

        for (int idx : indices) {
            if (start[idx] >= lastEnd) {
                count++;
                lastEnd = end[idx];
                selected.add(idx + 1); // 1-indexed for display
            }
        }

        System.out.println("  Selected meetings (1-indexed): " + selected);
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== N Meetings in One Room ===\n");

        // Test 1
        int[] s1 = {1, 3, 0, 5, 8, 5};
        int[] e1 = {2, 4, 6, 7, 9, 9};
        System.out.println("--- Test 1 ---");
        System.out.println("Optimal: " + maxMeetings(s1, e1)); // 4

        // Test 2: all overlap
        int[] s2 = {1, 1, 1};
        int[] e2 = {2, 3, 4};
        System.out.println("\n--- Test 2 (all overlap) ---");
        System.out.println("Optimal: " + maxMeetings(s2, e2)); // 1

        // Test 3: no overlap
        int[] s3 = {1, 3, 5};
        int[] e3 = {2, 4, 6};
        System.out.println("\n--- Test 3 (no overlap) ---");
        System.out.println("Optimal: " + maxMeetings(s3, e3)); // 3

        // Test 4: single meeting
        int[] s4 = {5};
        int[] e4 = {10};
        System.out.println("\n--- Test 4 (single) ---");
        System.out.println("Optimal: " + maxMeetings(s4, e4)); // 1

        // Brute force verification (small input)
        System.out.println("\n--- Brute Force Verification ---");
        System.out.println("Brute (test1): " + bruteForce(s1, e1)); // 4
        System.out.println("Brute (test2): " + bruteForce(s2, e2)); // 1
    }
}
