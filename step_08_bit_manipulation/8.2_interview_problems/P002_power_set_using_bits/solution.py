"""
Problem: Power Set using Bits
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — Recursive generation
# Time: O(2^n * n)  |  Space: O(2^n * n)
# ============================================================
def _recurse(nums: List[int], index: int,
             current: List[int], result: List[List[int]]) -> None:
    if index == len(nums):
        result.append(current[:])
        return
    # Include nums[index]
    current.append(nums[index])
    _recurse(nums, index + 1, current, result)
    current.pop()
    # Exclude nums[index]
    _recurse(nums, index + 1, current, result)


def brute_force(nums: List[int]) -> List[List[int]]:
    """
    Recursively decide include/exclude for each element.
    Generates all 2^n subsets.
    """
    result: List[List[int]] = []
    _recurse(nums, 0, [], result)
    return result


# ============================================================
# APPROACH 2: OPTIMAL — Bitmask iteration
# Time: O(n * 2^n)  |  Space: O(n * 2^n) for storing subsets
# For each number 0 .. 2^n-1, treat its bits as an inclusion
# mask: bit j set → include nums[j].
# ============================================================
def optimal(nums: List[int]) -> List[List[int]]:
    """
    Iterate over all 2^n masks.  For each mask, collect elements
    whose corresponding bit is set.
    """
    n = len(nums)
    result: List[List[int]] = []
    for mask in range(1 << n):          # 0 to 2^n - 1
        subset = []
        for j in range(n):
            if mask & (1 << j):
                subset.append(nums[j])
        result.append(subset)
    return result


# ============================================================
# APPROACH 3: BEST — Bitmask, bit-count ordered (lexicographic)
# Time: O(n * 2^n)  |  Space: O(n * 2^n)
# Same complexity, but groups subsets by size using Python's
# built-in bin().count('1') — natural for interview readability.
# ============================================================
def best(nums: List[int]) -> List[List[int]]:
    """
    Bitmask approach, subsets ordered by cardinality then value
    for a clean presentation.
    """
    n = len(nums)
    all_masks = sorted(range(1 << n), key=lambda m: bin(m).count('1'))
    result: List[List[int]] = []
    for mask in all_masks:
        subset = [nums[j] for j in range(n) if mask & (1 << j)]
        result.append(subset)
    return result


if __name__ == "__main__":
    print("=== Power Set using Bits ===")
    nums = [1, 2, 3]
    print(f"Input: {nums}")
    print(f"Brute ({len(brute_force(nums))} subsets):   {brute_force(nums)}")
    print(f"Optimal ({len(optimal(nums))} subsets): {optimal(nums)}")
    print(f"Best ({len(best(nums))} subsets, size-ordered): {best(nums)}")
