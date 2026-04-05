"""
Problem: Sum of Subarray Ranges
LeetCode 2104 | Difficulty: MEDIUM | XP: 25

Key Insight: range(subarray) = max - min
             Sum of all ranges = sum_of_subarray_maxes - sum_of_subarray_mins
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> int:
    """
    Enumerate every subarray, track its min and max,
    accumulate (max - min) into the answer.
    """
    n = len(nums)
    total = 0
    for i in range(n):
        cur_min = cur_max = nums[i]
        for j in range(i, n):
            cur_min = min(cur_min, nums[j])
            cur_max = max(cur_max, nums[j])
            total += cur_max - cur_min
    return total


# ============================================================
# APPROACH 2: OPTIMAL  (Monotonic Stack)
# Time: O(n)  |  Space: O(n)
# ============================================================
def _sum_subarray_mins(nums: List[int]) -> int:
    """
    For each element nums[i], count how many subarrays have it
    as the minimum, using Previous Less Element (PLE) and
    Next Less Element (NLE).
    Contribution = nums[i] * (i - PLE[i]) * (NLE[i] - i)
    Use <= on left or right to avoid double-counting equal elements.
    """
    n = len(nums)
    stack = []
    total = 0
    # Extend with sentinels so every element gets processed
    for i in range(n + 1):
        val = nums[i] if i < n else float('-inf')
        while stack and nums[stack[-1]] > val:
            mid = stack.pop()
            left = stack[-1] if stack else -1
            right = i
            total += nums[mid] * (mid - left) * (right - mid)
        stack.append(i)
    return total


def _sum_subarray_maxs(nums: List[int]) -> int:
    """
    Mirror of above but for maximum (PGE / NGE).
    """
    n = len(nums)
    stack = []
    total = 0
    for i in range(n + 1):
        val = nums[i] if i < n else float('inf')
        while stack and nums[stack[-1]] < val:
            mid = stack.pop()
            left = stack[-1] if stack else -1
            right = i
            total += nums[mid] * (mid - left) * (right - mid)
        stack.append(i)
    return total


def optimal(nums: List[int]) -> int:
    """
    Answer = sum_of_maximums - sum_of_minimums.
    Each computed in O(n) via monotonic stack contribution technique.
    """
    return _sum_subarray_maxs(nums) - _sum_subarray_mins(nums)


# ============================================================
# APPROACH 3: BEST  (same O(n), single-pass with two stacks)
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(nums: List[int]) -> int:
    """
    Combine both stacks in one loop to reduce constant factor.
    Stack `min_stk` tracks candidates for minimum contribution,
    `max_stk` for maximum contribution.
    """
    n = len(nums)
    min_stk: List[int] = []
    max_stk: List[int] = []
    sum_min = sum_max = 0

    for i in range(n + 1):
        # Process minimum contributions (monotone increasing stack)
        while min_stk and (i == n or nums[min_stk[-1]] >= nums[i]):
            mid = min_stk.pop()
            left = min_stk[-1] if min_stk else -1
            sum_min += nums[mid] * (mid - left) * (i - mid)
        # Process maximum contributions (monotone decreasing stack)
        while max_stk and (i == n or nums[max_stk[-1]] <= nums[i]):
            mid = max_stk.pop()
            left = max_stk[-1] if max_stk else -1
            sum_max += nums[mid] * (mid - left) * (i - mid)
        if i < n:
            min_stk.append(i)
            max_stk.append(i)

    return sum_max - sum_min


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3], 4),
        ([1, 3, 3], 4),
        ([4, -2, -3, 4, 1], 59),
    ]
    print("=== Sum of Subarray Ranges ===")
    for nums, expected in test_cases:
        b = brute_force(nums)
        o = optimal(nums)
        bst = best(nums)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  nums={nums} => brute={b}, optimal={o}, best={bst} (expected {expected}) [{status}]")
