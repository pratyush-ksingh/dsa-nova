import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(K * N log N)  |  Space: O(N)
// Simulate K rounds: sort bags each round, pick max, halve, reinsert
// ============================================================
class BruteForce {
    public static long solve(int[] bags, int k) {
        int n = bags.length;
        int[] b = bags.clone();
        long total = 0;
        for (int t = 0; t < k; t++) {
            Arrays.sort(b);
            int maxVal = b[n - 1];
            total += maxVal;
            b[n - 1] = maxVal / 2;
        }
        return total;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Max-Heap
// Time: O(K log N)  |  Space: O(N)
// Use a max-heap: each turn extract max, add to profit, push max/2
// ============================================================
class Optimal {
    public static long solve(int[] bags, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int b : bags) pq.offer(b);
        long total = 0;
        for (int t = 0; t < k; t++) {
            int maxVal = pq.poll();
            total += maxVal;
            pq.offer(maxVal / 2);
        }
        return total;
    }
}

// ============================================================
// APPROACH 3: BEST - Max-Heap with early exit
// Time: O(K log N)  |  Space: O(N)
// Same as Optimal but stop early if max bag is 0
// ============================================================
class Best {
    public static long solve(int[] bags, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int b : bags) pq.offer(b);
        long total = 0;
        for (int t = 0; t < k; t++) {
            int maxVal = pq.peek();
            if (maxVal == 0) break;  // all bags empty
            pq.poll();
            total += maxVal;
            pq.offer(maxVal / 2);
        }
        return total;
    }
}

public class Solution {
    public static void main(String[] args) {
        // IB Example: A=[6,5], k=3 -> 6+5+3=14 (6->3, 5->2; next max=3->1; next=2)
        // Actually: round1: max=6, total=6, push 3. Heap: [5,3]
        //           round2: max=5, total=11, push 2. Heap: [3,2]
        //           round3: max=3, total=14, push 1. Answer=14
        int[] bags1 = {6, 5};
        System.out.println("Test 1 (bags=[6,5], k=3):");
        System.out.println("  BruteForce = " + BruteForce.solve(bags1, 3));  // 14
        System.out.println("  Optimal    = " + Optimal.solve(bags1, 3));     // 14
        System.out.println("  Best       = " + Best.solve(bags1, 3));        // 14

        int[] bags2 = {1, 2, 3};
        System.out.println("Test 2 (bags=[1,2,3], k=4):");
        System.out.println("  BruteForce = " + BruteForce.solve(bags2, 4));
        System.out.println("  Optimal    = " + Optimal.solve(bags2, 4));
        System.out.println("  Best       = " + Best.solve(bags2, 4));
        // round1: max=3, total=3, push 1. [2,1,1]
        // round2: max=2, total=5, push 1. [1,1,1]
        // round3: max=1, total=6, push 0. [1,1,0]
        // round4: max=1, total=7, push 0. => 7
    }
}
