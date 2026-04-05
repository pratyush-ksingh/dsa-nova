"""
Problem: Sliding Window Maximum
Difficulty: HARD | XP: 50

Given an array and window size k, find the maximum in each sliding window.
Real-life use: Network traffic analysis, stock price tracking, image processing.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE
# For each window, scan all k elements for the maximum.
# Time: O(N * k)  |  Space: O(1) extra
# ============================================================
def brute_force(nums: List[int], k: int) -> List[int]:
    if not nums or k == 0:
        return []
    return [max(nums[i:i + k]) for i in range(len(nums) - k + 1)]


# ============================================================
# APPROACH 2: OPTIMAL
# Monotonic deque (decreasing). Front = index of current window max.
# Time: O(N)  |  Space: O(k)
# ============================================================
def optimal(nums: List[int], k: int) -> List[int]:
    if not nums or k == 0:
        return []
    n = len(nums)
    result = []
    dq: deque = deque()  # stores indices, decreasing values

    for i in range(n):
        # Remove indices outside window
        while dq and dq[0] < i - k + 1:
            dq.popleft()
        # Maintain decreasing order
        while dq and nums[dq[-1]] < nums[i]:
            dq.pop()
        dq.append(i)
        if i >= k - 1:
            result.append(nums[dq[0]])

    return result


# ============================================================
# APPROACH 3: BEST
# Block decomposition: split into blocks of k.
# left[i] = max from block start to i.
# right[i] = max from i to block end.
# Window [i, i+k-1] max = max(right[i], left[i+k-1]).
# Time: O(N)  |  Space: O(N) — cache-friendly, no deque
# ============================================================
def best(nums: List[int], k: int) -> List[int]:
    if not nums or k == 0:
        return []
    n = len(nums)
    left = [0] * n
    right = [0] * n

    for i in range(n):
        if i % k == 0:
            left[i] = nums[i]
        else:
            left[i] = max(left[i - 1], nums[i])

    for i in range(n - 1, -1, -1):
        if i == n - 1 or (i + 1) % k == 0:
            right[i] = nums[i]
        else:
            right[i] = max(right[i + 1], nums[i])

    return [max(right[i], left[i + k - 1]) for i in range(n - k + 1)]


if __name__ == "__main__":
    print("=== Sliding Window Maximum ===")

    tests = [
        ([1, 3, -1, -3, 5, 3, 6, 7], 3),  # [3,3,5,5,6,7]
        ([1], 1),                           # [1]
        ([9, 8, 7, 6, 5], 3),              # [9,8,7]
        ([1, 3, 1, 2, 0, 5], 3),           # [3,3,2,5]
    ]
    for nums, k in tests:
        print(f"\nnums={nums}  k={k}")
        print(f"  Brute  : {brute_force(nums, k)}")
        print(f"  Optimal: {optimal(nums, k)}")
        print(f"  Best   : {best(nums, k)}")
