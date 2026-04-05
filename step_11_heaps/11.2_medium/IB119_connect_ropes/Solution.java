/**
 * Problem: Connect Ropes
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given N ropes with different lengths, connect them into one rope with minimum cost.
 * Cost of connecting two ropes = sum of their lengths.
 *
 * Greedy insight: always merge the two shortest ropes first (Huffman coding).
 * Use a min-heap.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Sort repeatedly, merge two smallest
    // Time: O(N^2 log N)  |  Space: O(N)
    // After each merge, re-sort the array to find the new two smallest
    // ============================================================
    public static long bruteForce(int[] ropes) {
        List<Integer> list = new ArrayList<>();
        for (int r : ropes) list.add(r);
        long totalCost = 0;
        while (list.size() > 1) {
            Collections.sort(list);
            int merged = list.get(0) + list.get(1);
            totalCost += merged;
            list.remove(0);
            list.remove(0);
            list.add(merged);
        }
        return totalCost;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Min-Heap (Priority Queue)
    // Time: O(N log N)  |  Space: O(N)
    // Always extract two minimum, add their sum back; accumulate cost
    // ============================================================
    public static long optimal(int[] ropes) {
        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (int r : ropes) pq.offer((long) r);
        long totalCost = 0;
        while (pq.size() > 1) {
            long a = pq.poll(), b = pq.poll();
            long merged = a + b;
            totalCost += merged;
            pq.offer(merged);
        }
        return totalCost;
    }

    // ============================================================
    // APPROACH 3: BEST - Same as Optimal; note this is already optimal.
    //             Demonstrate with explicit Huffman tree cost calculation.
    // Time: O(N log N)  |  Space: O(N)
    // The total cost equals sum of (rope_length * depth_in_Huffman_tree).
    // Min-heap naturally constructs the optimal Huffman tree.
    // ============================================================
    public static long best(int[] ropes) {
        // Identical to optimal — min-heap IS the optimal greedy strategy
        return optimal(ropes);
    }

    public static void main(String[] args) {
        System.out.println("=== Connect Ropes ===");

        int[] ropes = {4, 3, 2, 6};
        System.out.println("ropes=" + Arrays.toString(ropes));
        System.out.println("Brute:   " + bruteForce(ropes.clone()));  // 29
        System.out.println("Optimal: " + optimal(ropes.clone()));      // 29
        System.out.println("Best:    " + best(ropes.clone()));         // 29

        ropes = new int[]{1, 2, 3, 4, 5};
        System.out.println("\nropes=" + Arrays.toString(ropes));
        System.out.println("Brute:   " + bruteForce(ropes.clone()));  // 33
        System.out.println("Optimal: " + optimal(ropes.clone()));
        System.out.println("Best:    " + best(ropes.clone()));

        ropes = new int[]{5};
        System.out.println("\nropes=" + Arrays.toString(ropes) + " (single rope)");
        System.out.println("Brute:   " + bruteForce(ropes.clone()));  // 0
        System.out.println("Optimal: " + optimal(ropes.clone()));
        System.out.println("Best:    " + best(ropes.clone()));
    }
}
