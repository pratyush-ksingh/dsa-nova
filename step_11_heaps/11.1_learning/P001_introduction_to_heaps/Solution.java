/**
 * Problem: Introduction to Heaps
 * Difficulty: EASY | XP: 10
 *
 * Implement a Min-Heap with insert, extractMin, and heapify.
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sorted Array
    // Time: insert O(n), extractMin O(n), heapify O(n log n)
    // Space: O(n)
    // ============================================================
    static class BruteForceSortedHeap {
        private List<Integer> arr;

        public BruteForceSortedHeap() {
            arr = new ArrayList<>();
        }

        public void insert(int val) {
            // Binary search for insertion point
            int lo = 0, hi = arr.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (arr.get(mid) < val) lo = mid + 1;
                else hi = mid;
            }
            arr.add(lo, val); // O(n) shift
        }

        public int extractMin() {
            if (arr.isEmpty()) return -1;
            return arr.remove(0); // O(n) shift
        }

        public void heapify(int[] input) {
            arr.clear();
            for (int v : input) arr.add(v);
            arr.sort(null); // O(n log n)
        }

        public int peek() {
            return arr.isEmpty() ? -1 : arr.get(0);
        }

        @Override
        public String toString() {
            return arr.toString();
        }
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Binary Heap (array-backed)
    // Time: insert O(log n), extractMin O(log n), heapify O(n)
    // Space: O(n)
    // ============================================================
    static class MinHeap {
        private List<Integer> heap;

        public MinHeap() {
            heap = new ArrayList<>();
        }

        // --- Core helpers ---
        private int parent(int i) { return (i - 1) / 2; }
        private int left(int i)   { return 2 * i + 1; }
        private int right(int i)  { return 2 * i + 2; }

        private void swap(int i, int j) {
            int tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }

        // Bubble up: restore heap property after insertion at index i
        private void siftUp(int i) {
            while (i > 0 && heap.get(i) < heap.get(parent(i))) {
                swap(i, parent(i));
                i = parent(i);
            }
        }

        // Bubble down: restore heap property after placing element at index i
        private void siftDown(int i, int size) {
            while (true) {
                int smallest = i;
                int l = left(i), r = right(i);
                if (l < size && heap.get(l) < heap.get(smallest)) smallest = l;
                if (r < size && heap.get(r) < heap.get(smallest)) smallest = r;
                if (smallest == i) break;
                swap(i, smallest);
                i = smallest;
            }
        }

        // --- Public API ---
        public void insert(int val) {
            heap.add(val);
            siftUp(heap.size() - 1);
        }

        public int extractMin() {
            if (heap.isEmpty()) return -1;
            int min = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0, heap.size());
            }
            return min;
        }

        // Floyd's bottom-up heapify -- O(n)
        public void heapify(int[] arr) {
            heap = new ArrayList<>();
            for (int v : arr) heap.add(v);
            // Start from last internal node and sift down
            for (int i = heap.size() / 2 - 1; i >= 0; i--) {
                siftDown(i, heap.size());
            }
        }

        public int peek() {
            return heap.isEmpty() ? -1 : heap.get(0);
        }

        public int size() {
            return heap.size();
        }

        @Override
        public String toString() {
            return heap.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Introduction to Heaps ===\n");

        // --- Brute Force Demo ---
        System.out.println("--- Approach 1: Brute Force (Sorted Array) ---");
        BruteForceSortedHeap brute = new BruteForceSortedHeap();
        brute.insert(5); brute.insert(3); brute.insert(7); brute.insert(1);
        System.out.println("After inserting [5,3,7,1]: " + brute);
        System.out.println("extractMin: " + brute.extractMin()); // 1
        System.out.println("extractMin: " + brute.extractMin()); // 3
        System.out.println("State: " + brute);

        brute.heapify(new int[]{9, 4, 7, 1, 2});
        System.out.println("heapify([9,4,7,1,2]): " + brute);

        // --- Optimal Demo ---
        System.out.println("\n--- Approach 2/3: Optimal (Binary Heap) ---");
        MinHeap heap = new MinHeap();
        heap.insert(5); heap.insert(3); heap.insert(7); heap.insert(1);
        System.out.println("After inserting [5,3,7,1]: " + heap);
        System.out.println("extractMin: " + heap.extractMin()); // 1
        System.out.println("extractMin: " + heap.extractMin()); // 3
        System.out.println("State: " + heap);

        heap.heapify(new int[]{9, 4, 7, 1, 2});
        System.out.println("heapify([9,4,7,1,2]): " + heap);
        System.out.println("extractMin: " + heap.extractMin()); // 1
        System.out.println("extractMin: " + heap.extractMin()); // 2

        // Edge: empty heap
        MinHeap empty = new MinHeap();
        System.out.println("\nextractMin on empty: " + empty.extractMin()); // -1
    }
}
