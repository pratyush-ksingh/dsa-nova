/**
 * Problem: Max Heap Implementation
 * Difficulty: MEDIUM | XP: 25
 *
 * Implement a Max-Heap with insert, extractMax, and heapify.
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sorted Array (Descending)
    // Time: insert O(n), extractMax O(n), heapify O(n log n)
    // Space: O(n)
    // ============================================================
    static class BruteForceMaxHeap {
        private List<Integer> arr;

        public BruteForceMaxHeap() {
            arr = new ArrayList<>();
        }

        public void insert(int val) {
            int lo = 0, hi = arr.size();
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (arr.get(mid) > val) lo = mid + 1;
                else hi = mid;
            }
            arr.add(lo, val);
        }

        public int extractMax() {
            if (arr.isEmpty()) return -1;
            return arr.remove(0);
        }

        public void heapify(int[] input) {
            arr.clear();
            for (int v : input) arr.add(v);
            arr.sort(Collections.reverseOrder());
        }

        public int peek() {
            return arr.isEmpty() ? -1 : arr.get(0);
        }

        @Override
        public String toString() { return arr.toString(); }
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Binary Max-Heap (array-backed)
    // Time: insert O(log n), extractMax O(log n), heapify O(n)
    // Space: O(n)
    // ============================================================
    static class MaxHeap {
        private List<Integer> heap;

        public MaxHeap() {
            heap = new ArrayList<>();
        }

        private int parent(int i) { return (i - 1) / 2; }
        private int left(int i)   { return 2 * i + 1; }
        private int right(int i)  { return 2 * i + 2; }

        private void swap(int i, int j) {
            int tmp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, tmp);
        }

        private void siftUp(int i) {
            while (i > 0 && heap.get(i) > heap.get(parent(i))) {
                swap(i, parent(i));
                i = parent(i);
            }
        }

        private void siftDown(int i, int size) {
            while (true) {
                int largest = i;
                int l = left(i), r = right(i);
                if (l < size && heap.get(l) > heap.get(largest)) largest = l;
                if (r < size && heap.get(r) > heap.get(largest)) largest = r;
                if (largest == i) break;
                swap(i, largest);
                i = largest;
            }
        }

        public void insert(int val) {
            heap.add(val);
            siftUp(heap.size() - 1);
        }

        public int extractMax() {
            if (heap.isEmpty()) return -1;
            int max = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0, heap.size());
            }
            return max;
        }

        public void heapify(int[] arr) {
            heap = new ArrayList<>();
            for (int v : arr) heap.add(v);
            for (int i = heap.size() / 2 - 1; i >= 0; i--) {
                siftDown(i, heap.size());
            }
        }

        public int peek() {
            return heap.isEmpty() ? -1 : heap.get(0);
        }

        public int size() { return heap.size(); }

        @Override
        public String toString() { return heap.toString(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Max Heap Implementation ===\n");

        // --- Brute Force ---
        System.out.println("--- Approach 1: Brute Force (Sorted Array) ---");
        BruteForceMaxHeap brute = new BruteForceMaxHeap();
        brute.insert(3); brute.insert(5); brute.insert(1); brute.insert(7);
        System.out.println("After inserting [3,5,1,7]: " + brute);
        System.out.println("extractMax: " + brute.extractMax()); // 7
        System.out.println("extractMax: " + brute.extractMax()); // 5
        System.out.println("State: " + brute);

        // --- Optimal ---
        System.out.println("\n--- Approach 2/3: Optimal (Binary Max-Heap) ---");
        MaxHeap heap = new MaxHeap();
        heap.insert(3); heap.insert(5); heap.insert(1); heap.insert(7);
        System.out.println("After inserting [3,5,1,7]: " + heap);
        System.out.println("extractMax: " + heap.extractMax()); // 7
        System.out.println("extractMax: " + heap.extractMax()); // 5
        System.out.println("State: " + heap);

        heap.heapify(new int[]{1, 5, 3, 7, 2});
        System.out.println("heapify([1,5,3,7,2]): " + heap);
        System.out.println("extractMax: " + heap.extractMax()); // 7

        MaxHeap empty = new MaxHeap();
        System.out.println("\nextractMax on empty: " + empty.extractMax()); // -1
    }
}
