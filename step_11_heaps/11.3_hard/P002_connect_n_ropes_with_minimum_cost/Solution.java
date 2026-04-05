import java.util.*;

/**
 * Connect N Ropes with Minimum Cost
 *
 * Minimize total cost of connecting all ropes, where cost of
 * connecting two ropes = sum of their lengths.
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sort + linear insert
    // Time: O(N^2)  |  Space: O(N)
    // ============================================================
    public static long bruteForce(int[] ropes) {
        if (ropes.length <= 1) return 0;

        List<Integer> list = new ArrayList<>();
        for (int r : ropes) list.add(r);
        Collections.sort(list);

        long totalCost = 0;

        while (list.size() > 1) {
            int a = list.remove(0);
            int b = list.remove(0);
            int merged = a + b;
            totalCost += merged;

            // Insert merged back in sorted position
            int pos = Collections.binarySearch(list, merged);
            if (pos < 0) pos = -(pos + 1);
            list.add(pos, merged);
        }

        return totalCost;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Min-Heap greedy
    // Time: O(N log N)  |  Space: O(N)
    // ============================================================
    public static long optimal(int[] ropes) {
        if (ropes.length <= 1) return 0;

        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        for (int r : ropes) {
            minHeap.offer((long) r);
        }

        long totalCost = 0;

        while (minHeap.size() > 1) {
            long a = minHeap.poll();
            long b = minHeap.poll();
            long merged = a + b;
            totalCost += merged;
            minHeap.offer(merged);
        }

        return totalCost;
    }

    // ============================================================
    // APPROACH 3: BEST -- Min-Heap (same algorithm, clean implementation)
    // Time: O(N log N)  |  Space: O(N)
    // ============================================================
    public static long best(int[] ropes) {
        int n = ropes.length;
        if (n <= 1) return 0;

        // Build heap in O(N) using heapify
        PriorityQueue<Long> pq = new PriorityQueue<>(n);
        for (int r : ropes) pq.add((long) r);

        long cost = 0;
        while (pq.size() > 1) {
            long sum = pq.poll() + pq.poll();
            cost += sum;
            pq.add(sum);
        }

        return cost;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        int[] ropes1 = {4, 3, 2, 6};
        System.out.println("=== Connect N Ropes ===");
        System.out.println("Ropes: " + Arrays.toString(ropes1));
        System.out.println("Brute:   " + bruteForce(ropes1));  // 29
        System.out.println("Optimal: " + optimal(ropes1));      // 29
        System.out.println("Best:    " + best(ropes1));          // 29

        int[] ropes2 = {1, 2, 3};
        System.out.println("\nRopes: " + Arrays.toString(ropes2));
        System.out.println("Brute:   " + bruteForce(ropes2));  // 9
        System.out.println("Optimal: " + optimal(ropes2));      // 9
        System.out.println("Best:    " + best(ropes2));          // 9

        int[] ropes3 = {5};
        System.out.println("\nSingle rope: " + bruteForce(ropes3)); // 0
    }
}
