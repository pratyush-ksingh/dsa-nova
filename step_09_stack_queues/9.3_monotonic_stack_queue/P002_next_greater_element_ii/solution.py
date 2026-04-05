"""
Problem: Next Greater Element II (LeetCode #503)
Difficulty: MEDIUM | XP: 25

Given a circular array, find the next greater element for each position.
Wrap around the end to the beginning.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Circular Linear Scan
# Time: O(n^2)  |  Space: O(n)
#
# For each element, scan the next n-1 elements circularly.
# ============================================================
def brute_force(nums: List[int]) -> List[int]:
    n = len(nums)
    result = [-1] * n

    for i in range(n):
        for j in range(1, n):
            idx = (i + j) % n
            if nums[idx] > nums[i]:
                result[i] = nums[idx]
                break

    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Monotonic Stack Left-to-Right, 2n
# Time: O(n)  |  Space: O(n)
#
# Traverse 0..2n-1. Stack stores indices waiting for their NGE.
# When nums[i%n] > nums[stack[-1]], pop and record answer.
# Only push indices < n.
# ============================================================
def optimal(nums: List[int]) -> List[int]:
    n = len(nums)
    result = [-1] * n
    stack = []  # stores indices

    for i in range(2 * n):
        val = nums[i % n]
        while stack and nums[stack[-1]] < val:
            result[stack.pop()] = val
        if i < n:
            stack.append(i)

    return result


# ============================================================
# APPROACH 3: BEST -- Monotonic Stack Right-to-Left, 2n
# Time: O(n)  |  Space: O(n)
#
# Traverse 2n-1 down to 0. Stack holds values (candidates for
# NGE) in decreasing order. Pop all <= current. Stack top is
# the NGE. Record only when i < n.
# ============================================================
def best(nums: List[int]) -> List[int]:
    n = len(nums)
    result = [-1] * n
    stack = []  # stores values

    for i in range(2 * n - 1, -1, -1):
        val = nums[i % n]
        # Pop elements blocked by current
        while stack and stack[-1] <= val:
            stack.pop()
        # Record result for original indices only
        if i < n:
            result[i] = stack[-1] if stack else -1
        stack.append(val)

    return result


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Next Greater Element II ===\n")

    tests = [
        ([1, 2, 1],        [2, -1, 2]),
        ([1, 2, 3, 4, 3],  [2, 3, 4, -1, 4]),
        ([5, 4, 3, 2, 1],  [-1, 5, 5, 5, 5]),
        ([1, 1, 1],         [-1, -1, -1]),
        ([3],               [-1]),
    ]

    for nums, expected in tests:
        print(f"Input:    {nums}")
        print(f"Expected: {expected}")

        b = brute_force(nums)
        o = optimal(nums)
        be = best(nums)

        print(f"Brute:    {b}  {'PASS' if b == expected else 'FAIL'}")
        print(f"Optimal:  {o}  {'PASS' if o == expected else 'FAIL'}")
        print(f"Best:     {be}  {'PASS' if be == expected else 'FAIL'}")
        print()
