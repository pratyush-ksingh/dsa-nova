"""
Problem: Next Permutation
Difficulty: MEDIUM | XP: 25

Find the next lexicographically greater permutation of an array in-place.
If no such permutation exists (array is descending), rearrange to smallest (ascending).
"""
from typing import List
from itertools import permutations


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n! * n)  |  Space: O(n!)
# ============================================================
def brute_force(nums: List[int]) -> List[int]:
    """
    Generate all permutations sorted lexicographically, find current, return next.
    Only feasible for very small n. Demonstrates correctness clearly.
    Real-life: Exhaustive schedule generation (all orderings of tasks).
    """
    all_perms = sorted(set(permutations(nums)))
    cur = tuple(nums)
    for i, p in enumerate(all_perms):
        if p == cur:
            return list(all_perms[(i + 1) % len(all_perms)])
    return nums[:]


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> List[int]:
    """
    Three-step algorithm:
    1. Find rightmost index i where nums[i] < nums[i+1]  (pivot)
    2. Find rightmost index j where nums[j] > nums[i], swap them
    3. Reverse suffix from i+1 onward
    Real-life: Generating next test case in combinatorial testing, iterating game states.
    """
    result = nums[:]
    n = len(result)
    i = n - 2
    while i >= 0 and result[i] >= result[i + 1]:
        i -= 1
    if i >= 0:
        j = n - 1
        while result[j] <= result[i]:
            j -= 1
        result[i], result[j] = result[j], result[i]
    # Reverse suffix
    left, right = i + 1, n - 1
    while left < right:
        result[left], result[right] = result[right], result[left]
        left += 1
        right -= 1
    return result


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> None:
    """
    In-place version of the same algorithm (no copy).
    This is the canonical approach with no further improvement possible.
    Real-life: In-place permutation generation in memory-constrained environments.
    """
    n = len(nums)
    i = n - 2
    while i >= 0 and nums[i] >= nums[i + 1]:
        i -= 1
    if i >= 0:
        j = n - 1
        while nums[j] <= nums[i]:
            j -= 1
        nums[i], nums[j] = nums[j], nums[i]
    # Reverse suffix from i+1
    left, right = i + 1, n - 1
    while left < right:
        nums[left], nums[right] = nums[right], nums[left]
        left += 1
        right -= 1


if __name__ == "__main__":
    print("=== Next Permutation ===")
    tests = [
        ([1, 2, 3], [1, 3, 2]),
        ([3, 2, 1], [1, 2, 3]),
        ([1, 1, 5], [1, 5, 1]),
        ([1, 3, 2], [2, 1, 3]),
    ]
    for nums, exp in tests:
        print(f"\nInput: {nums}  =>  expected: {exp}")
        print(f"  Brute:   {brute_force(nums[:])}")
        print(f"  Optimal: {optimal(nums[:])}")
        inp = nums[:]
        best(inp)
        print(f"  Best:    {inp}")
