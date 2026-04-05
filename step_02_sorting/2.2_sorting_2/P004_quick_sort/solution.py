"""
Problem: Quick Sort
Difficulty: MEDIUM | XP: 25
Source: Striver A2Z

Implement the Quick Sort algorithm to sort an array in ascending order.
Three approaches: naive pivot selection (first element), Lomuto partition
with random pivot, and Hoare partition scheme.
"""
from typing import List
import random


# ============================================================
# APPROACH 1: BRUTE FORCE (Naive Quick Sort — first element as pivot)
# Time: O(n²) worst case (sorted/reverse-sorted input)  |  Space: O(n) stack
# Uses Lomuto partition with always picking the first element as pivot.
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """
    Quick Sort with first element as pivot.
    Worst case O(n^2) when array is already sorted (pivot is always smallest).
    Creates a copy to avoid mutating the input.
    """
    arr = arr[:]  # work on a copy

    def partition(a: List[int], low: int, high: int) -> int:
        pivot = a[low]                   # always pick first element
        # Move pivot to end by swapping with high
        a[low], a[high] = a[high], a[low]
        store = low
        for i in range(low, high):
            if a[i] <= pivot:
                a[store], a[i] = a[i], a[store]
                store += 1
        a[store], a[high] = a[high], a[store]
        return store

    def quicksort(a: List[int], low: int, high: int) -> None:
        if low < high:
            pi = partition(a, low, high)
            quicksort(a, low, pi - 1)
            quicksort(a, pi + 1, high)

    quicksort(arr, 0, len(arr) - 1)
    return arr


# ============================================================
# APPROACH 2: OPTIMAL (Lomuto Partition + Random Pivot)
# Time: O(n log n) average  |  Space: O(log n) stack average
# Randomising pivot avoids worst-case O(n^2) on sorted/adversarial inputs.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """
    Lomuto partition scheme with a randomly chosen pivot.
    Random pivot selection gives expected O(n log n) on any input.
    Pivot is swapped to the end before partitioning.
    """
    arr = arr[:]

    def partition(a: List[int], low: int, high: int) -> int:
        # Pick random pivot and move it to the end
        rand_idx = random.randint(low, high)
        a[rand_idx], a[high] = a[high], a[rand_idx]
        pivot = a[high]
        i = low - 1                      # index of last element <= pivot
        for j in range(low, high):
            if a[j] <= pivot:
                i += 1
                a[i], a[j] = a[j], a[i]
        a[i + 1], a[high] = a[high], a[i + 1]
        return i + 1

    def quicksort(a: List[int], low: int, high: int) -> None:
        if low < high:
            pi = partition(a, low, high)
            quicksort(a, low, pi - 1)
            quicksort(a, pi + 1, high)

    quicksort(arr, 0, len(arr) - 1)
    return arr


# ============================================================
# APPROACH 3: BEST (Hoare Partition Scheme + Random Pivot)
# Time: O(n log n) average  |  Space: O(log n) stack average
# Hoare's original scheme: two pointers moving inward.
# Fewer swaps than Lomuto on average (~3x fewer).
# ============================================================
def best(arr: List[int]) -> List[int]:
    """
    Hoare partition scheme.
    Uses two pointers starting from opposite ends, swapping elements that
    are on the wrong side of the pivot. The pivot is NOT necessarily placed
    at its final position after partition (unlike Lomuto).
    Note: partition returns index j such that all elements in [low..j] are
    <= pivot and all in [j+1..high] are >= pivot.
    """
    arr = arr[:]

    def partition(a: List[int], low: int, high: int) -> int:
        # Random pivot for safety, placed at low
        rand_idx = random.randint(low, high)
        a[rand_idx], a[low] = a[low], a[rand_idx]
        pivot = a[low]
        i = low - 1
        j = high + 1
        while True:
            i += 1
            while a[i] < pivot:
                i += 1
            j -= 1
            while a[j] > pivot:
                j -= 1
            if i >= j:
                return j
            a[i], a[j] = a[j], a[i]

    def quicksort(a: List[int], low: int, high: int) -> None:
        if low < high:
            pi = partition(a, low, high)
            quicksort(a, low, pi)
            quicksort(a, pi + 1, high)

    quicksort(arr, 0, len(arr) - 1)
    return arr


if __name__ == "__main__":
    print("=== Quick Sort ===")
    test_cases = [
        ([64, 34, 25, 12, 22, 11, 90], [11, 12, 22, 25, 34, 64, 90]),
        ([5, 4, 3, 2, 1],              [1, 2, 3, 4, 5]),
        ([1, 2, 3, 4, 5],              [1, 2, 3, 4, 5]),
        ([1],                           [1]),
        ([],                            []),
        ([3, 3, 3, 1, 2],              [1, 2, 3, 3, 3]),
    ]
    for arr, expected in test_cases:
        b  = brute_force(arr)
        o  = optimal(arr)
        be = best(arr)
        status = "OK" if b == o == be == expected else "FAIL"
        print(f"Input: {arr} -> Brute: {b} | OK={status}")
