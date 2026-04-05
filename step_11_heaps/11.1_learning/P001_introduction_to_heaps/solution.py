"""
Problem: Introduction to Heaps
Difficulty: EASY | XP: 10

Implement a Min-Heap with insert, extractMin, and heapify.
"""
from typing import List, Optional
import bisect


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sorted Array
# Time: insert O(n), extractMin O(n), heapify O(n log n)
# Space: O(n)
# ============================================================
class BruteForceSortedHeap:
    def __init__(self):
        self.arr: List[int] = []

    def insert(self, val: int) -> None:
        bisect.insort(self.arr, val)  # O(n) due to shifting

    def extract_min(self) -> int:
        if not self.arr:
            return -1
        return self.arr.pop(0)  # O(n) shift

    def heapify(self, data: List[int]) -> None:
        self.arr = sorted(data)  # O(n log n)

    def peek(self) -> int:
        return self.arr[0] if self.arr else -1

    def __repr__(self):
        return str(self.arr)


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Binary Heap (array-backed)
# Time: insert O(log n), extractMin O(log n), heapify O(n)
# Space: O(n)
# ============================================================
class MinHeap:
    def __init__(self):
        self.heap: List[int] = []

    # --- Index helpers ---
    @staticmethod
    def _parent(i: int) -> int:
        return (i - 1) // 2

    @staticmethod
    def _left(i: int) -> int:
        return 2 * i + 1

    @staticmethod
    def _right(i: int) -> int:
        return 2 * i + 2

    def _swap(self, i: int, j: int) -> None:
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]

    def _sift_up(self, i: int) -> None:
        """Bubble element at index i upward until heap property restored."""
        while i > 0 and self.heap[i] < self.heap[self._parent(i)]:
            self._swap(i, self._parent(i))
            i = self._parent(i)

    def _sift_down(self, i: int, size: int) -> None:
        """Bubble element at index i downward until heap property restored."""
        while True:
            smallest = i
            l, r = self._left(i), self._right(i)
            if l < size and self.heap[l] < self.heap[smallest]:
                smallest = l
            if r < size and self.heap[r] < self.heap[smallest]:
                smallest = r
            if smallest == i:
                break
            self._swap(i, smallest)
            i = smallest

    # --- Public API ---
    def insert(self, val: int) -> None:
        self.heap.append(val)
        self._sift_up(len(self.heap) - 1)

    def extract_min(self) -> int:
        if not self.heap:
            return -1
        min_val = self.heap[0]
        last = self.heap.pop()
        if self.heap:
            self.heap[0] = last
            self._sift_down(0, len(self.heap))
        return min_val

    def heapify(self, arr: List[int]) -> None:
        """Floyd's bottom-up heapify -- O(n)."""
        self.heap = arr[:]
        # Start from last internal node
        for i in range(len(self.heap) // 2 - 1, -1, -1):
            self._sift_down(i, len(self.heap))

    def peek(self) -> int:
        return self.heap[0] if self.heap else -1

    def size(self) -> int:
        return len(self.heap)

    def __repr__(self):
        return str(self.heap)


if __name__ == "__main__":
    print("=== Introduction to Heaps ===\n")

    # --- Brute Force Demo ---
    print("--- Approach 1: Brute Force (Sorted Array) ---")
    brute = BruteForceSortedHeap()
    for v in [5, 3, 7, 1]:
        brute.insert(v)
    print(f"After inserting [5,3,7,1]: {brute}")
    print(f"extractMin: {brute.extract_min()}")  # 1
    print(f"extractMin: {brute.extract_min()}")  # 3
    print(f"State: {brute}")

    brute.heapify([9, 4, 7, 1, 2])
    print(f"heapify([9,4,7,1,2]): {brute}")

    # --- Optimal Demo ---
    print("\n--- Approach 2/3: Optimal (Binary Heap) ---")
    heap = MinHeap()
    for v in [5, 3, 7, 1]:
        heap.insert(v)
    print(f"After inserting [5,3,7,1]: {heap}")
    print(f"extractMin: {heap.extract_min()}")  # 1
    print(f"extractMin: {heap.extract_min()}")  # 3
    print(f"State: {heap}")

    heap.heapify([9, 4, 7, 1, 2])
    print(f"heapify([9,4,7,1,2]): {heap}")
    print(f"extractMin: {heap.extract_min()}")  # 1
    print(f"extractMin: {heap.extract_min()}")  # 2

    # Edge: empty heap
    empty = MinHeap()
    print(f"\nextractMin on empty: {empty.extract_min()}")  # -1
