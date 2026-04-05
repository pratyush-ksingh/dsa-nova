"""
Problem: Subset Sum II
Difficulty: MEDIUM | XP: 25

Given an array (may contain duplicates) and a target,
find all UNIQUE subsets that sum to the target.
Real-life use: Combinatorial search, resource allocation with duplicate items.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Generate all subsets via bitmask, filter by sum, deduplicate with a set.
# Time: O(2^N * N)  |  Space: O(2^N * N)
# ============================================================
def brute_force(arr: List[int], target: int) -> List[List[int]]:
    arr = sorted(arr)
    n = len(arr)
    seen = set()
    result = []
    for mask in range(1 << n):
        subset = []
        total = 0
        for i in range(n):
            if mask & (1 << i):
                subset.append(arr[i])
                total += arr[i]
        if total == target:
            key = tuple(subset)
            if key not in seen:
                seen.add(key)
                result.append(subset)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Backtracking with sorting + skip duplicates at the same depth level.
# Time: O(2^N)  |  Space: O(N) recursion stack
# ============================================================
def optimal(arr: List[int], target: int) -> List[List[int]]:
    arr = sorted(arr)
    result = []

    def backtrack(start: int, remaining: int, current: List[int]) -> None:
        if remaining == 0:
            result.append(list(current))
            return
        for i in range(start, len(arr)):
            if i > start and arr[i] == arr[i - 1]:
                continue  # skip duplicate at same recursion level
            if arr[i] > remaining:
                break      # sorted array — no point going further
            current.append(arr[i])
            backtrack(i + 1, remaining - arr[i], current)
            current.pop()

    backtrack(0, target, [])
    return result


# ============================================================
# APPROACH 3: BEST
# Same backtracking but tracks current sum explicitly (cleaner logic).
# Time: O(2^N)  |  Space: O(N)
# ============================================================
def best(arr: List[int], target: int) -> List[List[int]]:
    arr = sorted(arr)
    result = []

    def backtrack(start: int, curr_sum: int, current: List[int]) -> None:
        if curr_sum == target:
            result.append(list(current))
            return
        for i in range(start, len(arr)):
            if i > start and arr[i] == arr[i - 1]:
                continue
            if curr_sum + arr[i] > target:
                break
            current.append(arr[i])
            backtrack(i + 1, curr_sum + arr[i], current)
            current.pop()

    backtrack(0, 0, [])
    return result


if __name__ == "__main__":
    print("=== Subset Sum II ===")

    tests = [
        ([1, 1, 2, 2], 3),
        ([10, 1, 2, 7, 6, 1, 5], 8),
        ([2, 5, 2, 1, 2], 5),
    ]

    for arr, t in tests:
        print(f"\narr={arr}  target={t}")
        print(f"  Brute  : {brute_force(arr, t)}")
        print(f"  Optimal: {optimal(arr, t)}")
        print(f"  Best   : {best(arr, t)}")
