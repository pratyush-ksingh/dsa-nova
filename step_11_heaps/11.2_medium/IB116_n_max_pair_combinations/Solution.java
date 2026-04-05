/**
 * Problem: N Max Pair Combinations
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given two arrays A and B of size N, find the N largest possible pair sums
 * A[i] + B[j]. Return them in non-increasing order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Generate all N^2 sums, sort, take top N
    // Time: O(N^2 log N)  |  Space: O(N^2)
    // ============================================================
    public static int[] bruteForce(int[] A, int[] B) {
        int n = A.length;
        int[] sums = new int[n * n];
        int idx = 0;
        for (int a : A) for (int b : B) sums[idx++] = a + b;
        Arrays.sort(sums);
        int[] res = new int[n];
        for (int i = 0; i < n; i++) res[i] = sums[n * n - 1 - i];
        return res;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Sort both arrays; max-heap with visited set
    // Time: O(N log N)  |  Space: O(N)
    // Sort A and B descending. Start with (A[0]+B[0], 0, 0) in max-heap.
    // Each pop yields the next largest sum; push (i,j+1) and (i+1,j) if not visited.
    // ============================================================
    public static int[] optimal(int[] A, int[] B) {
        int n = A.length;
        Arrays.sort(A); Arrays.sort(B);
        // Max-heap: [-sum, i, j]
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[0] - x[0]);
        Set<Long> visited = new HashSet<>();

        int si = n - 1, sj = n - 1;
        pq.offer(new int[]{A[si] + B[sj], si, sj});
        visited.add(encode(si, sj, n));

        int[] res = new int[n];
        for (int k = 0; k < n; k++) {
            int[] top = pq.poll();
            int sum = top[0], i = top[1], j = top[2];
            res[k] = sum;
            if (i - 1 >= 0 && visited.add(encode(i - 1, j, n)))
                pq.offer(new int[]{A[i-1] + B[j], i-1, j});
            if (j - 1 >= 0 && visited.add(encode(i, j - 1, n)))
                pq.offer(new int[]{A[i] + B[j-1], i, j-1});
        }
        return res;
    }

    private static long encode(int i, int j, int n) {
        return (long) i * n + j;
    }

    // ============================================================
    // APPROACH 3: BEST - Sort A, for each top-N in A pair with sorted B
    //                    using a single linear pass insight
    // Time: O(N log N)  |  Space: O(N)
    // Sort both. The best N pairs come from the top elements of A x top elements of B.
    // Use heap starting at (n-1, n-1), expand left and down (same as Approach 2
    // but without a visited set by noting we only need one direction per row).
    // ============================================================
    public static int[] best(int[] A, int[] B) {
        // Elegant O(N log N): sort A and B; pair A[i] with B[j] via heap
        // Identical algorithm to optimal but without visited set by tracking
        // only (i, j) with j always = current best for row i
        int n = A.length;
        int[] sortedA = A.clone(), sortedB = B.clone();
        Arrays.sort(sortedA); Arrays.sort(sortedB);

        // heap: [sum, i in A, j in B] — j for each i starts at n-1 and decreases
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[0] - x[0]);
        // Initialize: pair each A[i] with B[n-1] (best B)
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{sortedA[i] + sortedB[n-1], i, n-1});
        }
        int[] res = new int[n];
        for (int k = 0; k < n; k++) {
            int[] top = pq.poll();
            res[k] = top[0];
            int i = top[1], j = top[2];
            if (j - 1 >= 0) {
                pq.offer(new int[]{sortedA[i] + sortedB[j-1], i, j-1});
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("=== N Max Pair Combinations ===");

        int[] A = {1, 4, 2, 3};
        int[] B = {2, 5, 1, 6};
        System.out.println("A=" + Arrays.toString(A) + ", B=" + Arrays.toString(B));
        System.out.println("Brute:   " + Arrays.toString(bruteForce(A.clone(), B.clone())));
        System.out.println("Optimal: " + Arrays.toString(optimal(A.clone(), B.clone())));
        System.out.println("Best:    " + Arrays.toString(best(A.clone(), B.clone())));
        // Expected: top 4 sums from {1,2,3,4}x{1,2,5,6} = 4+6=10, 4+5=9, 3+6=9, 4+2=6 -> [10,9,9,8]

        int[] A2 = {3, 2};
        int[] B2 = {1, 4};
        System.out.println("\nA=" + Arrays.toString(A2) + ", B=" + Arrays.toString(B2));
        System.out.println("Brute:   " + Arrays.toString(bruteForce(A2.clone(), B2.clone())));
        System.out.println("Optimal: " + Arrays.toString(optimal(A2.clone(), B2.clone())));
        System.out.println("Best:    " + Arrays.toString(best(A2.clone(), B2.clone())));
        // Expected: [7, 6]
    }
}
