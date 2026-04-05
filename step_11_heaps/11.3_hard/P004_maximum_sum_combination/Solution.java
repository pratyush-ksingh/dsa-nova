/**
 * Problem: Maximum Sum Combination
 * Difficulty: HARD | XP: 50
 *
 * Given two integer arrays A and B of size N, find the C largest sums
 * formed by A[i] + B[j] for any i, j. Return results in descending order.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Generate all N² sums, sort)
// Time: O(N² log N) | Space: O(N²)
// ============================================================
class BruteForce {
    public static int[] solve(int[] A, int[] B, int C) {
        List<Integer> sums = new ArrayList<>();
        for (int a : A) {
            for (int b : B) {
                sums.add(a + b);
            }
        }
        sums.sort(Collections.reverseOrder());
        int[] result = new int[C];
        for (int i = 0; i < C; i++) result[i] = sums.get(i);
        return result;
    }
}

// ============================================================
// Approach 2: Optimal (Max-Heap + visited set, BFS over sum grid)
// Time: O(N log N + C log C) | Space: O(N + C)
// ============================================================
class Optimal {
    public static int[] solve(int[] A, int[] B, int C) {
        int n = A.length;
        // Sort both arrays descending
        Integer[] As = new Integer[n];
        Integer[] Bs = new Integer[n];
        for (int i = 0; i < n; i++) { As[i] = A[i]; Bs[i] = B[i]; }
        Arrays.sort(As, Collections.reverseOrder());
        Arrays.sort(Bs, Collections.reverseOrder());

        // Max-heap: [sum, i, j]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        Set<Long> visited = new HashSet<>();

        pq.offer(new int[]{As[0] + Bs[0], 0, 0});
        visited.add(0L);

        int[] result = new int[C];
        for (int k = 0; k < C; k++) {
            int[] top = pq.poll();
            result[k] = top[0];
            int i = top[1], j = top[2];

            if (i + 1 < n) {
                long key = (long)(i + 1) * n + j;
                if (visited.add(key)) {
                    pq.offer(new int[]{As[i + 1] + Bs[j], i + 1, j});
                }
            }
            if (j + 1 < n) {
                long key = (long) i * n + (j + 1);
                if (visited.add(key)) {
                    pq.offer(new int[]{As[i] + Bs[j + 1], i, j + 1});
                }
            }
        }
        return result;
    }
}

// ============================================================
// Approach 3: Best (Same Max-Heap — clean production form)
// Time: O(N log N + C log C) | Space: O(N + C)
// ============================================================
class Best {
    public static int[] solve(int[] A, int[] B, int C) {
        int n = A.length;
        Integer[] As = new Integer[n];
        Integer[] Bs = new Integer[n];
        for (int i = 0; i < n; i++) { As[i] = A[i]; Bs[i] = B[i]; }
        Arrays.sort(As, Collections.reverseOrder());
        Arrays.sort(Bs, Collections.reverseOrder());

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        Set<Long> seen = new HashSet<>();

        pq.offer(new int[]{As[0] + Bs[0], 0, 0});
        seen.add(0L);

        int[] result = new int[C];
        int pos = 0;

        while (pos < C) {
            int[] cur = pq.poll();
            result[pos++] = cur[0];
            int i = cur[1], j = cur[2];

            if (i + 1 < n && seen.add((long)(i + 1) * n + j)) {
                pq.offer(new int[]{As[i + 1] + Bs[j], i + 1, j});
            }
            if (j + 1 < n && seen.add((long) i * n + j + 1)) {
                pq.offer(new int[]{As[i] + Bs[j + 1], i, j + 1});
            }
        }
        return result;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Maximum Sum Combination ===\n");

        int[][] As = {{3, 2}, {1, 4, 2, 3}, {1, 2}, {5}};
        int[][] Bs = {{1, 4}, {2, 5, 1, 6}, {3, 4}, {5}};
        int[]   Cs = {2, 4, 3, 1};
        int[][] expecteds = {{7, 6}, {10, 9, 9, 8}, {6, 5, 5}, {10}};

        for (int t = 0; t < As.length; t++) {
            int[] b = BruteForce.solve(As[t], Bs[t], Cs[t]);
            int[] o = Optimal.solve(As[t], Bs[t], Cs[t]);
            int[] h = Best.solve(As[t], Bs[t], Cs[t]);
            boolean pass = Arrays.equals(b, expecteds[t])
                        && Arrays.equals(o, expecteds[t])
                        && Arrays.equals(h, expecteds[t]);

            System.out.println("A=" + Arrays.toString(As[t]) + " B=" + Arrays.toString(Bs[t]) + " C=" + Cs[t]);
            System.out.println("Brute:    " + Arrays.toString(b));
            System.out.println("Optimal:  " + Arrays.toString(o));
            System.out.println("Best:     " + Arrays.toString(h));
            System.out.println("Expected: " + Arrays.toString(expecteds[t]));
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
