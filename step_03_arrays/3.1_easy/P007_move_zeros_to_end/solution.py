"""
Problem: Move Zeros to End (LeetCode #283)
Difficulty: EASY | XP: 10

Move all zeros to the end of the array while maintaining
the relative order of non-zero elements. In-place.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Extra Array)
# Time: O(n) | Space: O(n)
# ============================================================
def brute_force(nums: List[int]) -> List[int]:
    """Create a new array: non-zeros first, then zeros."""
    result = [x for x in nums if x != 0]
    result.extend([0] * (len(nums) - len(result)))
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Two-Pointer Write + Backfill)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> List[int]:
    """Write pointer places non-zeros, then backfill zeros."""
    result = nums[:]
    write_idx = 0

    for i in range(len(result)):
        if result[i] != 0:
            result[write_idx] = result[i]
            write_idx += 1

    # Fill remaining with zeros
    for i in range(write_idx, len(result)):
        result[i] = 0

    return result


# ============================================================
# APPROACH 3: BEST (Swap Variant -- single pass)
# Time: O(n) | Space: O(1)
# ============================================================
def best(nums: List[int]) -> List[int]:
    """Swap non-zeros to the front in one pass."""
    result = nums[:]
    write_idx = 0

    for i in range(len(result)):
        if result[i] != 0:
            result[i], result[write_idx] = result[write_idx], result[i]
            write_idx += 1

    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Move Zeros to End ===\n")

    test_cases = [
        ([0, 1, 0, 3, 12], [1, 3, 12, 0, 0]),
        ([0], [0]),
        ([1, 2, 3], [1, 2, 3]),
        ([0, 0, 0, 1], [1, 0, 0, 0]),
        ([0, 0, 0], [0, 0, 0]),
        ([-1, 0, 3, 0, -5], [-1, 3, -5, 0, 0]),
    ]

    for nums, expected in test_cases:
        b = brute_force(nums)
        o = optimal(nums)
        be = best(nums)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    {nums}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
