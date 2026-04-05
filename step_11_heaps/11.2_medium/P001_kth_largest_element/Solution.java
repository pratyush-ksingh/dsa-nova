/**
 * Problem: Kth Largest Element in an Array (LeetCode #215)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sort
    // Time: O(n log n)  |  Space: O(1)
    // Sort and return element at index n-k.
    // ============================================================
    public static int bruteForce(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Min-Heap of Size k
    // Time: O(n log k)  |  Space: O(k)
    // Maintain k largest elements. Heap root = kth largest.
    // ============================================================
    public static int optimal(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(num);
            }
        }
        return minHeap.peek();
    }

    // ============================================================
    // APPROACH 3: BEST -- Quickselect (Randomized)
    // Time: O(n) average  |  Space: O(1)
    // Partition-based selection. Find element at index n-k.
    // ============================================================
    public static int best(int[] nums, int k) {
        int target = nums.length - k;
        return quickselect(nums, 0, nums.length - 1, target);
    }

    private static int quickselect(int[] nums, int lo, int hi, int target) {
        while (lo < hi) {
            int pivotIdx = partition(nums, lo, hi);
            if (pivotIdx == target) return nums[pivotIdx];
            else if (pivotIdx < target) lo = pivotIdx + 1;
            else hi = pivotIdx - 1;
        }
        return nums[lo];
    }

    private static int partition(int[] nums, int lo, int hi) {
        // Random pivot to avoid O(n^2) on sorted input
        int randomIdx = lo + new Random().nextInt(hi - lo + 1);
        swap(nums, randomIdx, hi);

        int pivot = nums[hi];
        int storeIdx = lo;

        for (int i = lo; i < hi; i++) {
            if (nums[i] <= pivot) {
                swap(nums, i, storeIdx);
                storeIdx++;
            }
        }
        swap(nums, storeIdx, hi);
        return storeIdx;
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    public static void main(String[] args) {
        System.out.println("=== Kth Largest Element ===");

        int[] nums1 = {3, 2, 1, 5, 6, 4};
        System.out.println("Brute (k=2):   " + bruteForce(nums1.clone(), 2));   // 5
        System.out.println("Optimal (k=2): " + optimal(nums1.clone(), 2));       // 5
        System.out.println("Best (k=2):    " + best(nums1.clone(), 2));           // 5

        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        System.out.println("Optimal (k=4): " + optimal(nums2.clone(), 4));       // 4
        System.out.println("Best (k=4):    " + best(nums2.clone(), 4));           // 4

        // Edge: single element
        System.out.println("Single:        " + optimal(new int[]{1}, 1));         // 1

        // Edge: all same
        System.out.println("All same:      " + optimal(new int[]{7,7,7}, 2));     // 7
    }
}
