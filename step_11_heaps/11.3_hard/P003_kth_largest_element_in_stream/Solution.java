/**
 * Problem: Kth Largest Element in Stream (LeetCode #703)
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sorted List
    // Time: O(n log n) per add  |  Space: O(n)
    // ============================================================
    static class KthLargestBrute {
        private List<Integer> data;
        private int k;

        public KthLargestBrute(int k, int[] nums) {
            this.k = k;
            this.data = new ArrayList<>();
            for (int n : nums) data.add(n);
        }

        public int add(int val) {
            data.add(val);
            Collections.sort(data);
            return data.get(data.size() - k);
        }
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Min-Heap of size k
    // Time: O(log k) per add  |  Space: O(k)
    // ============================================================
    static class KthLargest {
        private PriorityQueue<Integer> minHeap;
        private int k;

        public KthLargest(int k, int[] nums) {
            this.k = k;
            this.minHeap = new PriorityQueue<>();
            for (int n : nums) {
                minHeap.offer(n);
                if (minHeap.size() > k) {
                    minHeap.poll(); // remove smallest, keep top k
                }
            }
        }

        public int add(int val) {
            minHeap.offer(val);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
            return minHeap.peek(); // root = kth largest
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Kth Largest Element in Stream ===\n");

        // --- Brute Force ---
        System.out.println("--- Approach 1: Brute Force ---");
        KthLargestBrute brute = new KthLargestBrute(3, new int[]{4, 5, 8, 2});
        System.out.println("add(3): " + brute.add(3));   // 4
        System.out.println("add(5): " + brute.add(5));   // 5
        System.out.println("add(10): " + brute.add(10)); // 5
        System.out.println("add(9): " + brute.add(9));   // 8
        System.out.println("add(4): " + brute.add(4));   // 8

        // --- Optimal ---
        System.out.println("\n--- Approach 2/3: Min-Heap ---");
        KthLargest optimal = new KthLargest(3, new int[]{4, 5, 8, 2});
        System.out.println("add(3): " + optimal.add(3));   // 4
        System.out.println("add(5): " + optimal.add(5));   // 5
        System.out.println("add(10): " + optimal.add(10)); // 5
        System.out.println("add(9): " + optimal.add(9));   // 8
        System.out.println("add(4): " + optimal.add(4));   // 8

        // Edge: empty initial array
        System.out.println("\n--- Edge: empty initial nums, k=1 ---");
        KthLargest edge = new KthLargest(1, new int[]{});
        System.out.println("add(-3): " + edge.add(-3));  // -3
        System.out.println("add(-2): " + edge.add(-2));  // -2
        System.out.println("add(-4): " + edge.add(-4));  // -2
        System.out.println("add(0): " + edge.add(0));    // 0
    }
}
