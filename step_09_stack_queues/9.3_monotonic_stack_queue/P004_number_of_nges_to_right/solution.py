"""
Problem: Number of Next Greater Elements to the Right
Difficulty: MEDIUM | XP: 25

Given an array arr[] of N integers, for each element find the count of
elements strictly greater than it that lie to its right.

E.g., arr = [3, 4, 2, 7, 5, 1]
  idx 0 (val=3): elements to right greater than 3 -> {4,7,5} -> count=3
  idx 1 (val=4): {7,5} -> count=2
  idx 2 (val=2): {7,5} -> count=2
  idx 3 (val=7): {} -> count=0
  idx 4 (val=5): {} -> count=0
  idx 5 (val=1): {} -> count=0
Output: [3, 2, 2, 0, 0, 0]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1) extra
#
# For each element, scan all elements to its right and count
# those strictly greater than it.
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """Nested loops: for each i, count j > i where arr[j] > arr[i]."""
    n = len(arr)
    result = [0] * n
    for i in range(n):
        count = 0
        for j in range(i + 1, n):
            if arr[j] > arr[i]:
                count += 1
        result[i] = count
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Merge Sort Based Count
# Time: O(n log n)  |  Space: O(n)
#
# Use a modified merge sort. During the merge step, when we take
# an element from the right half (it's greater than elements in
# the left half that haven't been merged yet), increment their counts.
#
# We augment each element with its original index so we can update
# the correct position in the result array.
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """Modified merge sort: count NGEs using the merge step."""
    n = len(arr)
    result = [0] * n
    # Pair each element with its index: (value, original_index)
    indexed = [(arr[i], i) for i in range(n)]

    def merge_sort(lst: List) -> List:
        if len(lst) <= 1:
            return lst
        mid = len(lst) // 2
        left = merge_sort(lst[:mid])
        right = merge_sort(lst[mid:])
        return merge(left, right)

    def merge(left: List, right: List) -> List:
        merged = []
        i = j = 0
        while i < len(left) and j < len(right):
            if left[i][0] < right[j][0]:
                # right[j] is greater than left[i]
                # All remaining elements in right are also >= right[j],
                # but we want strictly greater, so count elements in right
                # that are strictly > left[i].
                # However in merge sort we count in sorted order, which
                # doesn't directly give us "elements to the right in original array".
                # We need a different formulation.
                merged.append(left[i])
                i += 1
            else:
                merged.append(right[j])
                j += 1
        merged.extend(left[i:])
        merged.extend(right[j:])
        return merged

    # The merge-sort approach for "count of greater elements to the right"
    # requires careful tracking. We count: for each element in the left half
    # during merge, how many elements from the right half are strictly greater.
    # Since right half has higher original indices (elements to the right),
    # each right element that passes over a left element is a valid NGE.
    #
    # Correct implementation: count elements from right half that merge before
    # each left element (meaning they are greater).
    result_arr = [0] * n
    indexed2 = [(arr[i], i) for i in range(n)]

    def merge_sort2(lst):
        if len(lst) <= 1:
            return lst[:]
        mid = len(lst) // 2
        left = merge_sort2(lst[:mid])
        right = merge_sort2(lst[mid:])
        return merge2(left, right)

    def merge2(left, right):
        merged = []
        i = j = 0
        # Count how many from right have been placed before each left element
        right_count = 0
        while i < len(left) and j < len(right):
            if left[i][0] < right[j][0]:
                # right[j] > left[i]: all of right[j:] that come before left[i]
                # are NGEs of left[i]. But we want only those from original right.
                # In merge sort, all right half elements have original indices > all left half elements.
                # Count of right elements placed so far that came BEFORE left[i] = right_count.
                # But we also need remaining right elements > left[i].
                # Actually: len(right) - j counts remaining right elements > left[i]
                # (since sorted order: right[j] <= right[j+1] <= ...)
                # Wait - we want strictly greater. Since right[j] > left[i],
                # remaining right[j:] might not all be > left[i].
                # This approach counts elements in right half that are greater,
                # but those are elements with HIGHER original positions.
                result_arr[left[i][1]] += len(right) - j
                merged.append(left[i])
                i += 1
            else:
                merged.append(right[j])
                j += 1
        while i < len(left):
            # No more right elements; remaining right that are greater = 0 already counted
            merged.append(left[i])
            i += 1
        while j < len(right):
            merged.append(right[j])
            j += 1
        return merged

    merge_sort2(indexed2)
    return result_arr


# ============================================================
# APPROACH 3: BEST -- BIT (Binary Indexed Tree / Fenwick Tree)
# Time: O(n log n)  |  Space: O(max_val)
#
# Process from right to left. For each element, query the BIT
# for count of elements already inserted that are strictly greater.
# Then insert the current element into the BIT.
# This works for discrete values (compress coordinates if needed).
# ============================================================
def best(arr: List[int]) -> List[int]:
    """BIT-based right-to-left processing: query prefix sum for count of NGEs."""
    n = len(arr)
    result = [0] * n

    # Coordinate compression
    sorted_unique = sorted(set(arr))
    rank = {v: i + 1 for i, v in enumerate(sorted_unique)}  # 1-indexed
    max_rank = len(sorted_unique)

    bit = [0] * (max_rank + 2)

    def update(i: int) -> None:
        while i <= max_rank:
            bit[i] += 1
            i += i & (-i)

    def query(i: int) -> int:
        """Sum of frequencies in [1..i]."""
        s = 0
        while i > 0:
            s += bit[i]
            i -= i & (-i)
        return s

    # Process right to left
    for i in range(n - 1, -1, -1):
        r = rank[arr[i]]
        # Count elements greater than arr[i] = total inserted - count of elements <= arr[i]
        total_inserted = query(max_rank)
        count_le = query(r)
        result[i] = total_inserted - count_le
        update(r)

    return result


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Number of NGEs to Right ===\n")

    tests = [
        ([3, 4, 2, 7, 5, 1],     [3, 2, 2, 0, 0, 0]),
        ([1, 2, 3, 4, 5],         [4, 3, 2, 1, 0]),
        ([5, 4, 3, 2, 1],         [0, 0, 0, 0, 0]),
        ([1],                     [0]),
        ([3, 3, 3],               [0, 0, 0]),
        ([1, 3, 2, 4],            [3, 1, 1, 0]),
    ]

    for arr, expected in tests:
        b = brute_force(arr[:])
        o = optimal(arr[:])
        r = best(arr[:])
        status = "PASS" if b == o == r == expected else "FAIL"
        print(f"arr={arr}")
        print(f"  Expected: {expected}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}  [{status}]")
        print()
