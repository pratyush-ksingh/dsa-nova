"""
Kth Smallest Element

Find the kth smallest element in an unsorted array.
"""
import heapq
import random
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sort + Index
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def brute_force(arr: List[int], k: int) -> int:
    sorted_arr = sorted(arr)
    return sorted_arr[k - 1]


# ============================================================
# APPROACH 2: OPTIMAL -- Max-Heap of size k
# Time: O(N log k)  |  Space: O(k)
# Python's heapq is min-heap, so negate values for max-heap behavior.
# ============================================================
def optimal(arr: List[int], k: int) -> int:
    # Max-heap using negation trick
    max_heap = []

    for num in arr:
        if len(max_heap) < k:
            heapq.heappush(max_heap, -num)
        elif num < -max_heap[0]:
            heapq.heapreplace(max_heap, -num)

    return -max_heap[0]  # root = kth smallest


# ============================================================
# APPROACH 3: BEST -- Quickselect (Lomuto partition)
# Time: O(N) average  |  Space: O(1)
# ============================================================
def best(arr: List[int], k: int) -> int:
    arr_copy = arr[:]
    return quickselect(arr_copy, 0, len(arr_copy) - 1, k - 1)


def quickselect(arr, lo, hi, target_idx):
    if lo == hi:
        return arr[lo]

    # Random pivot to avoid worst case
    rand_idx = random.randint(lo, hi)
    arr[rand_idx], arr[hi] = arr[hi], arr[rand_idx]

    pivot_idx = partition(arr, lo, hi)

    if pivot_idx == target_idx:
        return arr[pivot_idx]
    elif pivot_idx < target_idx:
        return quickselect(arr, pivot_idx + 1, hi, target_idx)
    else:
        return quickselect(arr, lo, pivot_idx - 1, target_idx)


def partition(arr, lo, hi):
    pivot = arr[hi]
    i = lo
    for j in range(lo, hi):
        if arr[j] <= pivot:
            arr[i], arr[j] = arr[j], arr[i]
            i += 1
    arr[i], arr[hi] = arr[hi], arr[i]
    return i


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    arr = [7, 10, 4, 3, 20, 15]
    k = 3

    print("=== Kth Smallest Element ===")
    print(f"Array: {arr}, k={k}")
    print(f"Brute:   {brute_force(arr, k)}")   # 7
    print(f"Optimal: {optimal(arr, k)}")        # 7
    print(f"Best:    {best(arr, k)}")           # 7

    arr2 = [7, 10, 4, 20, 15]
    print(f"\nArray: {arr2}, k=4")
    print(f"Brute:   {brute_force(arr2, 4)}")   # 15
    print(f"Optimal: {optimal(arr2, 4)}")        # 15
    print(f"Best:    {best(arr2, 4)}")           # 15
