"""
Problem: Max Heap Implementation
Difficulty: MEDIUM | XP: 25

Implement a Max-Heap with insert, extractMax, and heapify.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sorted Array (Descending)
# Time: insert O(n), extractMax O(n), heapify O(n log n)
# Space: O(n)
# ============================================================
class BruteForceMaxHeap:
    def __init__(self):
        self.arr: List[int] = []

    def insert(self, val: int) -> None:
        pos = 0
        while pos < len(self.arr) and self.arr[pos] > val:
            pos += 1
        self.arr.insert(pos, val)

    def extract_max(self) -> int:
        if not self.arr:
            return -1
        return self.arr.pop(0)

    def heapify(self, data: List[int]) -> None:
        self.arr = sorted(data, reverse=True)

    def peek(self) -> int:
        return self.arr[0] if self.arr else -1

    def __repr__(self):
        return str(self.arr)


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Binary Max-Heap (array-backed)
# Time: insert O(log n), extractMax O(log n), heapify O(n)
# Space: O(n)
# ============================================================
class MaxHeap:
    def __init__(self):
        self.heap: List[int] = []

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
        while i > 0 and self.heap[i] > self.heap[self._parent(i)]:
            self._swap(i, self._parent(i))
            i = self._parent(i)

    def _sift_down(self, i: int, size: int) -> None:
        while True:
            largest = i
            l, r = self._left(i), self._right(i)
            if l < size and self.heap[l] > self.heap[largest]:
                largest = l
            if r < size and self.heap[r] > self.heap[largest]:
                largest = r
            if largest == i:
                break
            self._swap(i, largest)
            i = largest

    def insert(self, val: int) -> None:
        self.heap.append(val)
        self._sift_up(len(self.heap) - 1)

    def extract_max(self) -> int:
        if not self.heap:
            return -1
        max_val = self.heap[0]
        last = self.heap.pop()
        if self.heap:
            self.heap[0] = last
            self._sift_down(0, len(self.heap))
        return max_val

    def heapify(self, arr: List[int]) -> None:
        self.heap = arr[:]
        for i in range(len(self.heap) // 2 - 1, -1, -1):
            self._sift_down(i, len(self.heap))

    def peek(self) -> int:
        return self.heap[0] if self.heap else -1

    def size(self) -> int:
        return len(self.heap)

    def __repr__(self):
        return str(self.heap)


if __name__ == "__main__":
    print("=== Max Heap Implementation ===\n")

    print("--- Approach 1: Brute Force (Sorted Array) ---")
    brute = BruteForceMaxHeap()
    for v in [3, 5, 1, 7]:
        brute.insert(v)
    print(f"After inserting [3,5,1,7]: {brute}")
    print(f"extractMax: {brute.extract_max()}")  # 7
    print(f"extractMax: {brute.extract_max()}")  # 5
    print(f"State: {brute}")

    print("\n--- Approach 2/3: Optimal (Binary Max-Heap) ---")
    heap = MaxHeap()
    for v in [3, 5, 1, 7]:
        heap.insert(v)
    print(f"After inserting [3,5,1,7]: {heap}")
    print(f"extractMax: {heap.extract_max()}")  # 7
    print(f"extractMax: {heap.extract_max()}")  # 5
    print(f"State: {heap}")

    heap.heapify([1, 5, 3, 7, 2])
    print(f"heapify([1,5,3,7,2]): {heap}")
    print(f"extractMax: {heap.extract_max()}")  # 7

    empty = MaxHeap()
    print(f"\nextractMax on empty: {empty.extract_max()}")  # -1
