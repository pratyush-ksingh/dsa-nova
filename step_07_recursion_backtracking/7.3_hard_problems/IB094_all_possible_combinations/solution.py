"""
Problem: All Possible Combinations (Power Set)
Difficulty: EASY | XP: 10
Source: InterviewBit

Generate all subsets of a given set of distinct integers.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative Cascading
# Time: O(N * 2^N)  |  Space: O(N * 2^N)
# Start with [[]], for each num duplicate all existing subsets
# and append num to the copies.
# ============================================================
def brute_force(nums: List[int]) -> List[List[int]]:
    result = [[]]
    for num in nums:
        result += [subset + [num] for subset in result]
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Backtracking (Include / Exclude)
# Time: O(N * 2^N)  |  Space: O(N) stack + O(N * 2^N) output
# At each index, choose to include or exclude the element.
# ============================================================
def optimal(nums: List[int]) -> List[List[int]]:
    result = []

    def backtrack(index: int, current: List[int]) -> None:
        if index == len(nums):
            result.append(current[:])  # copy!
            return
        # Exclude nums[index]
        backtrack(index + 1, current)
        # Include nums[index]
        current.append(nums[index])
        backtrack(index + 1, current)
        current.pop()  # backtrack

    backtrack(0, [])
    return result


# ============================================================
# APPROACH 3: BEST -- Bitmask Enumeration
# Time: O(N * 2^N)  |  Space: O(N * 2^N) output
# Each integer 0..2^N-1 encodes a subset via its bits.
# ============================================================
def best(nums: List[int]) -> List[List[int]]:
    n = len(nums)
    total = 1 << n  # 2^N
    result = []
    for mask in range(total):
        subset = [nums[j] for j in range(n) if mask & (1 << j)]
        result.append(subset)
    return result


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    test_cases = [
        ([], 1),
        ([1], 2),
        ([1, 2], 4),
        ([1, 2, 3], 8),
    ]

    print("=== All Possible Combinations (Power Set) ===\n")
    for nums, expected_size in test_cases:
        b = brute_force(nums)
        o = optimal(nums)
        bt = best(nums)

        size_ok = len(b) == len(o) == len(bt) == expected_size
        # Normalize for comparison (sort each subset, compare as sets of tuples)
        normalize = lambda lst: set(tuple(sorted(s)) for s in lst)
        content_ok = normalize(b) == normalize(o) == normalize(bt)

        status = "PASS" if size_ok and content_ok else "FAIL"
        print(f"[{status}] nums={str(nums):<12} | Count: Brute={len(b)}, Backtrack={len(o)}, Bitmask={len(bt)} | Expected={expected_size}")
        print(f"  Subsets (Bitmask): {bt}")
        print()
