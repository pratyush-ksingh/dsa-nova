/**
 * Problem: Meeting Rooms (Minimum Meeting Rooms Required)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a list of meeting intervals [start, end], find the minimum number of
 * conference rooms required to hold all meetings without conflict.
 *
 * Real-world: calendar scheduling, resource allocation.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Sort by start; simulate room assignment
    // Time: O(N^2)  |  Space: O(N)
    // For each meeting, try to find an available room (end time <= start).
    // If none found, open a new room.
    // ============================================================
    public static int bruteForce(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<Integer> rooms = new ArrayList<>(); // stores end time of each room
        for (int[] interval : intervals) {
            boolean assigned = false;
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i) <= interval[0]) {
                    rooms.set(i, interval[1]);
                    assigned = true;
                    break;
                }
            }
            if (!assigned) rooms.add(interval[1]);
        }
        return rooms.size();
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Sort by start + min-heap of end times
    // Time: O(N log N)  |  Space: O(N)
    // Min-heap tracks earliest-ending room. If earliest end <= new start,
    // reuse that room (update end); otherwise open a new room.
    // ============================================================
    public static int optimal(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min end times
        for (int[] interval : intervals) {
            if (!minHeap.isEmpty() && minHeap.peek() <= interval[0]) {
                minHeap.poll(); // free up the earliest-ending room
            }
            minHeap.offer(interval[1]);
        }
        return minHeap.size();
    }

    // ============================================================
    // APPROACH 3: BEST - Sweep line with sorted start/end arrays
    // Time: O(N log N)  |  Space: O(N)
    // Separate start and end times, sort independently.
    // Two-pointer sweep: if next_start < next_end, need a new room;
    // else a room was freed. Track max concurrent meetings.
    // ============================================================
    public static int best(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;
        int n = intervals.length;
        int[] starts = new int[n], ends = new int[n];
        for (int i = 0; i < n; i++) { starts[i] = intervals[i][0]; ends[i] = intervals[i][1]; }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int rooms = 0, maxRooms = 0, e = 0;
        for (int s = 0; s < n; s++) {
            if (starts[s] < ends[e]) {
                rooms++;
            } else {
                e++;
            }
            maxRooms = Math.max(maxRooms, rooms);
        }
        return maxRooms;
    }

    public static void main(String[] args) {
        System.out.println("=== Meeting Rooms ===");

        int[][] intervals = {{0,30},{5,10},{15,20}};
        System.out.println("intervals=" + Arrays.deepToString(intervals));
        System.out.println("Brute:   " + bruteForce(intervals));   // 2
        System.out.println("Optimal: " + optimal(intervals));       // 2
        System.out.println("Best:    " + best(intervals));          // 2

        intervals = new int[][]{{7,10},{2,4}};
        System.out.println("\nintervals=" + Arrays.deepToString(intervals));
        System.out.println("Brute:   " + bruteForce(intervals));   // 1
        System.out.println("Optimal: " + optimal(intervals));
        System.out.println("Best:    " + best(intervals));

        intervals = new int[][]{{1,5},{2,6},{3,7},{4,8},{5,9}};
        System.out.println("\nintervals=" + Arrays.deepToString(intervals));
        System.out.println("Brute:   " + bruteForce(intervals));   // 4
        System.out.println("Optimal: " + optimal(intervals));
        System.out.println("Best:    " + best(intervals));
    }
}
