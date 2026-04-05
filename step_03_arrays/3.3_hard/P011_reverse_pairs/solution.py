"""
Problem: Reverse Pairs
Difficulty: HARD | XP: 50

Count the number of "important reverse pairs": pairs (i, j) where
  i < j  and  nums[i] > 2 * nums[j]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    For every pair (i, j) with i < j, check if nums[i] > 2 * nums[j].
    Real-life: Finding anomalous pairs in financial data (sudden price drops).
    """
    n = len(nums)
    count = 0
    for i in range(n):
        for j in range(i + 1, n):
            if nums[i] > 2 * nums[j]:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL  (Modified Merge Sort)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(nums: List[int]) -> int:
    """
    During merge sort, before merging two sorted halves, count pairs where
    left[i] > 2 * right[j] using two pointers. Then merge normally.
    Real-life: Counting inversions in ranking comparisons, stock anomaly detection.
    """
    def merge_sort(arr: List[int]) -> int:
        if len(arr) <= 1:
            return 0
        mid = len(arr) // 2
        left, right = arr[:mid], arr[mid:]
        count = merge_sort(left) + merge_sort(right)

        # Count pairs: left[i] > 2 * right[j]
        j = 0
        for val in left:
            while j < len(right) and val > 2 * right[j]:
                j += 1
            count += j

        # Merge
        i = k = 0
        j = 0
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                arr[k] = left[i]; i += 1
            else:
                arr[k] = right[j]; j += 1
            k += 1
        while i < len(left):
            arr[k] = left[i]; i += 1; k += 1
        while j < len(right):
            arr[k] = right[j]; j += 1; k += 1
        return count

    arr = nums[:]
    return merge_sort(arr)


# ============================================================
# APPROACH 3: BEST
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Same merge sort — already optimal. Clean iterative-friendly version
    using index-based merging on the original array.
    Real-life: Same as optimal — canonical approach for interview settings.
    """
    def merge_count(arr: List[int], lo: int, hi: int) -> int:
        if lo >= hi:
            return 0
        mid = (lo + hi) // 2
        count = merge_count(arr, lo, mid) + merge_count(arr, mid + 1, hi)

        # Count reverse pairs
        j = mid + 1
        for i in range(lo, mid + 1):
            while j <= hi and arr[i] > 2 * arr[j]:
                j += 1
            count += j - (mid + 1)

        # Merge sorted halves
        tmp = []
        l, r = lo, mid + 1
        while l <= mid and r <= hi:
            if arr[l] <= arr[r]:
                tmp.append(arr[l]); l += 1
            else:
                tmp.append(arr[r]); r += 1
        while l <= mid:
            tmp.append(arr[l]); l += 1
        while r <= hi:
            tmp.append(arr[r]); r += 1
        arr[lo:hi + 1] = tmp
        return count

    arr = nums[:]
    return merge_count(arr, 0, len(arr) - 1)


if __name__ == "__main__":
    print("=== Reverse Pairs ===")
    tests = [
        ([1, 3, 2, 3, 1], 2),
        ([2, 4, 3, 5, 1], 3),
        ([5, 4, 3, 2, 1], 4),
        ([1, 2, 3, 4, 5], 0),
    ]
    for nums, exp in tests:
        print(f"\nInput: {nums}  =>  expected: {exp}")
        print(f"  Brute:   {brute_force(nums[:])}")
        print(f"  Optimal: {optimal(nums[:])}")
        print(f"  Best:    {best(nums[:])}")
