/**
 * Problem: Find Median from Data Stream
 * Difficulty: HARD | XP: 50
 *
 * Design a data structure that supports:
 * - addNum(num): Add a number to the data stream.
 * - findMedian(): Return the median of all numbers added so far.
 * Real-life use: Real-time analytics, streaming statistics, sensor data.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Store all numbers; sort on every findMedian call.
    // Time: addNum O(1), findMedian O(N log N)  |  Space: O(N)
    // ============================================================
    static class MedianFinderBrute {
        private List<Integer> nums = new ArrayList<>();

        public void addNum(int num) {
            nums.add(num);
        }

        public double findMedian() {
            Collections.sort(nums);
            int n = nums.size();
            if (n % 2 == 1) return nums.get(n / 2);
            return (nums.get(n / 2 - 1) + nums.get(n / 2)) / 2.0;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Two heaps: max-heap (lower half) + min-heap (upper half).
    // Invariant: lower.size() == upper.size() OR lower.size() == upper.size() + 1.
    // Median = lower.peek() if odd total, else average of both tops.
    // Time: addNum O(log N), findMedian O(1)  |  Space: O(N)
    // ============================================================
    static class MedianFinderOptimal {
        private PriorityQueue<Integer> lower = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
        private PriorityQueue<Integer> upper = new PriorityQueue<>(); // min-heap

        public void addNum(int num) {
            lower.offer(num);
            // Balance: move max of lower to upper
            upper.offer(lower.poll());
            // Keep lower size >= upper size
            if (upper.size() > lower.size()) lower.offer(upper.poll());
        }

        public double findMedian() {
            if (lower.size() > upper.size()) return lower.peek();
            return (lower.peek() + upper.peek()) / 2.0;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same two-heap approach but with optimized balancing:
    // add to appropriate heap directly based on value comparison.
    // Slightly fewer swaps than Approach 2.
    // Time: addNum O(log N), findMedian O(1)  |  Space: O(N)
    // ============================================================
    static class MedianFinder {
        private PriorityQueue<Integer> lower = new PriorityQueue<>(Collections.reverseOrder());
        private PriorityQueue<Integer> upper = new PriorityQueue<>();

        public void addNum(int num) {
            if (lower.isEmpty() || num <= lower.peek()) {
                lower.offer(num);
            } else {
                upper.offer(num);
            }
            // Rebalance
            if (lower.size() > upper.size() + 1) {
                upper.offer(lower.poll());
            } else if (upper.size() > lower.size()) {
                lower.offer(upper.poll());
            }
        }

        public double findMedian() {
            if (lower.size() == upper.size()) return (lower.peek() + upper.peek()) / 2.0;
            return lower.peek(); // lower always has extra when odd
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Find Median from Data Stream ===");

        int[] nums = {5, 15, 1, 3};

        System.out.println("\n--- Brute Force ---");
        MedianFinderBrute bf = new MedianFinderBrute();
        for (int n : nums) {
            bf.addNum(n);
            System.out.printf("addNum(%d) -> median=%.1f%n", n, bf.findMedian());
        }

        System.out.println("\n--- Optimal (Two Heaps v1) ---");
        MedianFinderOptimal opt = new MedianFinderOptimal();
        for (int n : nums) {
            opt.addNum(n);
            System.out.printf("addNum(%d) -> median=%.1f%n", n, opt.findMedian());
        }

        System.out.println("\n--- Best (Two Heaps v2) ---");
        MedianFinder best = new MedianFinder();
        for (int n : nums) {
            best.addNum(n);
            System.out.printf("addNum(%d) -> median=%.1f%n", n, best.findMedian());
        }
    }
}
