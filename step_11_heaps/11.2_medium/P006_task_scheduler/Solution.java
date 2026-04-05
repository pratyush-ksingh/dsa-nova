/**
 * Problem: Task Scheduler (LeetCode 621)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a list of CPU tasks (A-Z) and a cooldown n, find the minimum number
 * of intervals needed to finish all tasks. CPU must wait at least n intervals
 * between two executions of the same task. CPU can be idle.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Simulation with max-heap + cooldown queue
    // Time: O(T * 26) where T = total intervals  |  Space: O(26)
    // ============================================================
    /**
     * Simulate the CPU step by step.
     * Max-heap always yields the most frequent available task.
     * Cooldown queue holds tasks that must wait before being re-scheduled.
     */
    public static int bruteForce(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;

        // Max-heap: store negative counts
        PriorityQueue<Integer> heap = new PriorityQueue<>(Collections.reverseOrder());
        for (int f : freq) if (f > 0) heap.offer(f);

        // Queue: [available_time, remaining_count]
        Queue<int[]> cooldown = new LinkedList<>();
        int time = 0;

        while (!heap.isEmpty() || !cooldown.isEmpty()) {
            time++;
            // Release tasks whose cooldown expired
            if (!cooldown.isEmpty() && cooldown.peek()[0] <= time) {
                int[] entry = cooldown.poll();
                heap.offer(entry[1]);
            }
            if (!heap.isEmpty()) {
                int cnt = heap.poll() - 1;
                if (cnt > 0) {
                    cooldown.offer(new int[]{time + n + 1, cnt});
                }
            }
            // else: CPU idles this interval
        }
        return time;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Math formula O(26)
    // Time: O(26) ≈ O(1)  |  Space: O(26) ≈ O(1)
    // ============================================================
    /**
     * Frame-based math formula.
     *
     * The most frequent task(s) create "frames" of size (n+1).
     * Example with n=2, tasks=[A,A,A,B,B,B]:
     *   Frame 1: [A B _]
     *   Frame 2: [A B _]
     *   Last:    [A B]
     *   Total = (3-1)*(2+1) + 2 = 8
     *
     * Answer = max(total_tasks, (maxFreq-1)*(n+1) + countOfMaxFreq)
     */
    public static int optimal(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;

        int maxFreq = 0;
        for (int f : freq) maxFreq = Math.max(maxFreq, f);

        int countOfMax = 0;
        for (int f : freq) if (f == maxFreq) countOfMax++;

        int frameTime = (maxFreq - 1) * (n + 1) + countOfMax;
        return Math.max(tasks.length, frameTime);
    }

    // ============================================================
    // APPROACH 3: BEST — Same formula with sorted frequencies
    // Time: O(26 log 26) ≈ O(1)  |  Space: O(26) ≈ O(1)
    // ============================================================
    /**
     * Sort frequencies descending. The top value is maxFreq.
     * Count how many share that max. Apply the same formula.
     * Sorting makes counting ties at the top trivial.
     */
    public static int best(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;

        Arrays.sort(freq); // ascending
        int maxFreq = freq[25];

        int countOfMax = 0;
        for (int i = 25; i >= 0 && freq[i] == maxFreq; i--) countOfMax++;

        int frameTime = (maxFreq - 1) * (n + 1) + countOfMax;
        return Math.max(tasks.length, frameTime);
    }

    public static void main(String[] args) {
        System.out.println("=== Task Scheduler ===");

        char[][] taskSets = {
            {'A','A','A','B','B','B'},
            {'A','A','A','B','B','B'},
            {'A','A','A','A','A','A','B','C','D','E','F','G'},
            {'A','B','C','D','E','F'}
        };
        int[] ns = {2, 0, 2, 2};
        int[] expected = {8, 6, 16, 6};

        for (int i = 0; i < taskSets.length; i++) {
            System.out.println("tasks=" + Arrays.toString(taskSets[i]) + ", n=" + ns[i]);
            System.out.println("  Brute:   " + bruteForce(taskSets[i], ns[i]) + " (expected " + expected[i] + ")");
            System.out.println("  Optimal: " + optimal(taskSets[i], ns[i]));
            System.out.println("  Best:    " + best(taskSets[i], ns[i]));
        }
    }
}
