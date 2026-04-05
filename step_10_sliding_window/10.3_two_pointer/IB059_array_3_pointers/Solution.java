/**
 * Problem: Array 3 Pointers (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given 3 sorted arrays A, B, C, find indices i, j, k minimizing
 * max(|A[i]-B[j]|, |B[j]-C[k]|, |C[k]-A[i]|).
 *
 * Key insight: max(|a-b|, |b-c|, |c-a|) = max(a,b,c) - min(a,b,c)
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static int computeDiff(int a, int b, int c) {
        return Math.max(Math.max(a, b), c) - Math.min(Math.min(a, b), c);
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n*m*k)  |  Space: O(1)
    //
    // Try all combinations of indices. Return the minimum
    // max pairwise absolute difference.
    // ============================================================
    public static int bruteForce(int[] A, int[] B, int[] C) {
        int result = Integer.MAX_VALUE;
        for (int a : A) {
            for (int b : B) {
                for (int c : C) {
                    result = Math.min(result, computeDiff(a, b, c));
                    if (result == 0) return 0;
                }
            }
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Three Pointers
    // Time: O(n + m + k)  |  Space: O(1)
    //
    // Always advance the pointer to the minimum element.
    // Reasoning: the "spread" = max - min. To decrease it:
    // - Decreasing max: not possible (arrays are sorted ascending, pointers only go right).
    // - Increasing min: possible by advancing the minimum pointer.
    // So advance the pointer that currently points to the minimum value.
    // ============================================================
    public static int optimal(int[] A, int[] B, int[] C) {
        int i = 0, j = 0, k = 0;
        int result = Integer.MAX_VALUE;

        while (i < A.length && j < B.length && k < C.length) {
            int curr = computeDiff(A[i], B[j], C[k]);
            result = Math.min(result, curr);
            if (result == 0) return 0;

            int minVal = Math.min(Math.min(A[i], B[j]), C[k]);
            if (A[i] == minVal)      i++;
            else if (B[j] == minVal) j++;
            else                     k++;
        }

        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Three Pointers with PriorityQueue
    // Time: O((n+m+k) log 3) = O(n+m+k)  |  Space: O(1)
    //
    // Use a min-heap of size 3 holding (value, arrayId, index).
    // At each step: pop the minimum, update max, compute spread.
    // Push the next element from the same array. Stop when exhausted.
    // This naturally generalizes to K sorted arrays.
    // ============================================================
    public static int best(int[] A, int[] B, int[] C) {
        int[][] arrays = {A, B, C};
        // PriorityQueue min-heap: [value, arrayId, indexInArray]
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> x[0] - y[0]);
        int currentMax = Integer.MIN_VALUE;

        for (int id = 0; id < 3; id++) {
            pq.offer(new int[]{arrays[id][0], id, 0});
            currentMax = Math.max(currentMax, arrays[id][0]);
        }

        int result = currentMax - pq.peek()[0];

        while (true) {
            int[] minEntry = pq.poll();
            int nextIdx = minEntry[2] + 1;
            int aid = minEntry[1];

            if (nextIdx >= arrays[aid].length) break; // array exhausted

            int nextVal = arrays[aid][nextIdx];
            pq.offer(new int[]{nextVal, aid, nextIdx});
            currentMax = Math.max(currentMax, nextVal);
            result = Math.min(result, currentMax - pq.peek()[0]);
            if (result == 0) return 0;
        }

        return result;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Array 3 Pointers ===\n");

        int[][][][] tests = {
            {{{1, 4, 10}, {2, 15, 20}, {10, 12}}, {{5}}},
            {{{1, 2, 3},  {4, 5, 6},  {7, 8, 9}},  {{6}}},
            {{{1, 10, 20},{2, 10, 20},{1, 10, 20}}, {{0}}},
            {{{1},        {1},         {1}},         {{0}}},
        };

        for (int[][][] test : tests) {
            int[] A = test[0][0], B = test[0][1], C = test[0][2];
            int expected = test[1][0][0];

            int b = bruteForce(A, B, C);
            int o = optimal(A, B, C);
            int r = best(A, B, C);

            String status = (b == expected && o == expected && r == expected) ? "PASS" : "FAIL";
            System.out.printf("A=%s B=%s C=%s%n", Arrays.toString(A), Arrays.toString(B), Arrays.toString(C));
            System.out.printf("  Expected=%d Brute=%d Optimal=%d Best=%d [%s]%n%n",
                expected, b, o, r, status);
        }
    }
}
