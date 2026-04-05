/**
 * Problem: Min Heap Implementation
 * Difficulty: MEDIUM | XP: 25
 *
 * Full min-heap: insert, extractMin, getMin, decreaseKey, delete, heapify.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sorted Array
    // Insert: O(n)  |  ExtractMin: O(n)  |  GetMin: O(1)
    // ============================================================
    static class SortedArrayHeap {
        List<Integer> arr = new ArrayList<>();

        void insert(int val) {
            // Binary search for insertion point
            int pos = Collections.binarySearch(arr, val);
            if (pos < 0) pos = -(pos + 1);
            arr.add(pos, val);  // O(n) shift
        }

        int extractMin() {
            if (arr.isEmpty()) throw new NoSuchElementException("Heap is empty");
            return arr.remove(0);  // O(n) shift
        }

        int getMin() {
            if (arr.isEmpty()) throw new NoSuchElementException("Heap is empty");
            return arr.get(0);
        }

        int size() { return arr.size(); }
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL/BEST -- Array-Based Min Heap
    // Insert: O(log n)  |  ExtractMin: O(log n)  |  GetMin: O(1)
    // DecreaseKey: O(log n)  |  Delete: O(log n)
    // Heapify (build): O(n)
    // ============================================================
    static class MinHeap {
        private List<Integer> heap;

        MinHeap() {
            heap = new ArrayList<>();
        }

        // --- O(n) bottom-up heapify (BEST approach for building) ---
        MinHeap(int[] arr) {
            heap = new ArrayList<>();
            for (int val : arr) heap.add(val);
            // Start from last internal node, bubble down each
            for (int i = heap.size() / 2 - 1; i >= 0; i--) {
                bubbleDown(i);
            }
        }

        // --- Core helpers ---
        private int parent(int i) { return (i - 1) / 2; }
        private int leftChild(int i) { return 2 * i + 1; }
        private int rightChild(int i) { return 2 * i + 2; }

        private void swap(int i, int j) {
            int tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }

        private void bubbleUp(int i) {
            while (i > 0 && heap.get(i) < heap.get(parent(i))) {
                swap(i, parent(i));
                i = parent(i);
            }
        }

        private void bubbleDown(int i) {
            int size = heap.size();
            while (true) {
                int smallest = i;
                int left = leftChild(i);
                int right = rightChild(i);

                if (left < size && heap.get(left) < heap.get(smallest)) {
                    smallest = left;
                }
                if (right < size && heap.get(right) < heap.get(smallest)) {
                    smallest = right;
                }
                if (smallest == i) break;

                swap(i, smallest);
                i = smallest;
            }
        }

        // --- Public API ---
        void insert(int val) {
            heap.add(val);
            bubbleUp(heap.size() - 1);
        }

        int getMin() {
            if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
            return heap.get(0);
        }

        int extractMin() {
            if (heap.isEmpty()) throw new NoSuchElementException("Heap is empty");
            int min = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                bubbleDown(0);
            }
            return min;
        }

        void decreaseKey(int i, int newVal) {
            if (newVal > heap.get(i)) {
                throw new IllegalArgumentException("New value is larger than current");
            }
            heap.set(i, newVal);
            bubbleUp(i);
        }

        void delete(int i) {
            // Decrease to negative infinity, then extract
            decreaseKey(i, Integer.MIN_VALUE);
            extractMin();
        }

        int size() { return heap.size(); }

        @Override
        public String toString() { return heap.toString(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Min Heap Implementation ===");

        // Test Approach 1: Sorted Array
        System.out.println("\n--- Brute Force (Sorted Array) ---");
        SortedArrayHeap sorted = new SortedArrayHeap();
        sorted.insert(10);
        sorted.insert(5);
        sorted.insert(15);
        sorted.insert(3);
        System.out.println("Min: " + sorted.getMin());         // 3
        System.out.println("Extract: " + sorted.extractMin()); // 3
        System.out.println("Min: " + sorted.getMin());         // 5

        // Test Approach 2: Min Heap
        System.out.println("\n--- Optimal (Min Heap) ---");
        MinHeap heap = new MinHeap();
        heap.insert(10);
        System.out.println("After insert 10: " + heap);
        heap.insert(5);
        System.out.println("After insert 5:  " + heap);
        heap.insert(15);
        System.out.println("After insert 15: " + heap);
        heap.insert(3);
        System.out.println("After insert 3:  " + heap);
        System.out.println("Min: " + heap.getMin());             // 3
        System.out.println("Extract: " + heap.extractMin());     // 3
        System.out.println("After extract:   " + heap);

        // Test decreaseKey
        heap.decreaseKey(2, 1);  // Decrease 15 to 1
        System.out.println("After decreaseKey(2,1): " + heap);
        System.out.println("Min: " + heap.getMin());             // 1

        // Test delete
        heap.delete(1);  // Delete element at index 1
        System.out.println("After delete(1): " + heap);

        // Test Approach 3: Heapify
        System.out.println("\n--- Best (Heapify) ---");
        MinHeap heapified = new MinHeap(new int[]{10, 5, 15, 3, 8});
        System.out.println("Heapified [10,5,15,3,8]: " + heapified);
        System.out.println("Min: " + heapified.getMin());  // 3
    }
}
