"""
Problem: Min Heap Implementation
Difficulty: MEDIUM | XP: 25

Full min-heap: insert, extract_min, get_min, decrease_key, delete, heapify.
"""
from typing import List, Optional
import bisect
import math


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sorted Array
# Insert: O(n)  |  ExtractMin: O(n)  |  GetMin: O(1)
# ============================================================
class SortedArrayHeap:
    def __init__(self):
        self.arr: List[int] = []

    def insert(self, val: int) -> None:
        bisect.insort(self.arr, val)  # O(n) due to shifting

    def get_min(self) -> int:
        if not self.arr:
            raise IndexError("Heap is empty")
        return self.arr[0]

    def extract_min(self) -> int:
        if not self.arr:
            raise IndexError("Heap is empty")
        return self.arr.pop(0)  # O(n) shift

    def size(self) -> int:
        return len(self.arr)


# ============================================================
# APPROACH 2 & 3: OPTIMAL/BEST -- Array-Based Min Heap
# Insert: O(log n)  |  ExtractMin: O(log n)  |  GetMin: O(1)
# DecreaseKey: O(log n)  |  Delete: O(log n)
# Heapify (build): O(n)
# ============================================================
class MinHeap:
    def __init__(self, arr: Optional[List[int]] = None):
        if arr is None:
            self.heap: List[int] = []
        else:
            self.heap = arr[:]
            # O(n) bottom-up heapify
            for i in range(len(self.heap) // 2 - 1, -1, -1):
                self._bubble_down(i)

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

    # --- Core operations ---
    def _bubble_up(self, i: int) -> None:
        while i > 0 and self.heap[i] < self.heap[self._parent(i)]:
            pi = self._parent(i)
            self.heap[i], self.heap[pi] = self.heap[pi], self.heap[i]
            i = pi

    def _bubble_down(self, i: int) -> None:
        n = len(self.heap)
        while True:
            smallest = i
            left = self._left(i)
            right = self._right(i)

            if left < n and self.heap[left] < self.heap[smallest]:
                smallest = left
            if right < n and self.heap[right] < self.heap[smallest]:
                smallest = right

            if smallest == i:
                break

            self.heap[i], self.heap[smallest] = self.heap[smallest], self.heap[i]
            i = smallest

    # --- Public API ---
    def insert(self, val: int) -> None:
        self.heap.append(val)
        self._bubble_up(len(self.heap) - 1)

    def get_min(self) -> int:
        if not self.heap:
            raise IndexError("Heap is empty")
        return self.heap[0]

    def extract_min(self) -> int:
        if not self.heap:
            raise IndexError("Heap is empty")
        min_val = self.heap[0]
        last = self.heap.pop()
        if self.heap:
            self.heap[0] = last
            self._bubble_down(0)
        return min_val

    def decrease_key(self, i: int, new_val: int) -> None:
        if new_val > self.heap[i]:
            raise ValueError("New value is larger than current")
        self.heap[i] = new_val
        self._bubble_up(i)

    def delete(self, i: int) -> None:
        # Decrease to -infinity, then extract
        self.heap[i] = -math.inf
        self._bubble_up(i)
        self.extract_min()

    def size(self) -> int:
        return len(self.heap)

    def __repr__(self) -> str:
        return str(self.heap)


if __name__ == "__main__":
    print("=== Min Heap Implementation ===")

    # Test Approach 1: Sorted Array
    print("\n--- Brute Force (Sorted Array) ---")
    sa = SortedArrayHeap()
    sa.insert(10)
    sa.insert(5)
    sa.insert(15)
    sa.insert(3)
    print(f"Min: {sa.get_min()}")           # 3
    print(f"Extract: {sa.extract_min()}")   # 3
    print(f"Min: {sa.get_min()}")           # 5

    # Test Approach 2: Min Heap
    print("\n--- Optimal (Min Heap) ---")
    h = MinHeap()
    h.insert(10)
    print(f"After insert 10: {h}")
    h.insert(5)
    print(f"After insert 5:  {h}")
    h.insert(15)
    print(f"After insert 15: {h}")
    h.insert(3)
    print(f"After insert 3:  {h}")
    print(f"Min: {h.get_min()}")             # 3
    print(f"Extract: {h.extract_min()}")     # 3
    print(f"After extract:   {h}")

    # Test decreaseKey
    h.decrease_key(2, 1)  # Decrease 15 to 1
    print(f"After decrease_key(2,1): {h}")
    print(f"Min: {h.get_min()}")             # 1

    # Test delete
    h.delete(1)
    print(f"After delete(1): {h}")

    # Test Approach 3: Heapify
    print("\n--- Best (Heapify) ---")
    heapified = MinHeap([10, 5, 15, 3, 8])
    print(f"Heapified [10,5,15,3,8]: {heapified}")
    print(f"Min: {heapified.get_min()}")  # 3
