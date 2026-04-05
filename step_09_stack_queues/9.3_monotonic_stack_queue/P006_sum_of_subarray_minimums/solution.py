"""
Problem: Sum of Subarray Minimums (LeetCode 907)
Difficulty: MEDIUM | XP: 25
"""
from typing import List

MOD = 10 ** 9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²)  |  Space: O(1)
# Enumerate all O(n²) subarrays, track running minimum.
# ============================================================
def brute_force(arr: List[int]) -> int:
    """
    For every pair (i, j), compute min(arr[i..j]) and sum them.
    """
    n = len(arr)
    total = 0
    for i in range(n):
        min_val = arr[i]
        for j in range(i, n):
            min_val = min(min_val, arr[j])
            total += min_val
    return total % MOD


# ============================================================
# APPROACH 2: OPTIMAL — Monotonic stack (contribution technique)
# Time: O(n)  |  Space: O(n)
#
# For each element arr[i], compute:
#   left[i]  = number of subarrays ending at i where arr[i] is min
#            = i - PLE[i]   (PLE = Previous Less Element index)
#   right[i] = number of subarrays starting at i where arr[i] is min
#            = NLE[i] - i   (NLE = Next Less or Equal Element index)
#
# contribution of arr[i] = arr[i] * left[i] * right[i]
#
# Use strict '<' for PLE and '<='' for NLE to avoid double-counting
# duplicates.
# ============================================================
def optimal(arr: List[int]) -> int:
    """
    Monotonic stack to find PLE and NLE in O(n), then sum
    contributions.
    """
    n = len(arr)
    # PLE[i]: index of previous element strictly less than arr[i]
    ple = [-1] * n
    stack: List[int] = []
    for i in range(n):
        while stack and arr[stack[-1]] >= arr[i]:
            stack.pop()
        ple[i] = stack[-1] if stack else -1
        stack.append(i)

    # NLE[i]: index of next element less than OR equal to arr[i]
    nle = [n] * n
    stack = []
    for i in range(n - 1, -1, -1):
        while stack and arr[stack[-1]] > arr[i]:
            stack.pop()
        nle[i] = stack[-1] if stack else n
        stack.append(i)

    total = 0
    for i in range(n):
        left = i - ple[i]           # subarrays to the left including i
        right = nle[i] - i          # subarrays to the right including i
        total = (total + arr[i] * left * right) % MOD
    return total


# ============================================================
# APPROACH 3: BEST — Single-pass monotonic stack
# Time: O(n)  |  Space: O(n)
# Compute the contribution of each element as it is popped
# from the stack, avoiding two separate PLE/NLE passes.
# ============================================================
def best(arr: List[int]) -> int:
    """
    Process elements left to right.  When an element is popped
    (because a smaller element arrived), compute its contribution
    immediately using the current indices as boundaries.
    Append a sentinel 0 to flush the stack at the end.
    """
    n = len(arr)
    stack: List[int] = []      # stores indices; monotone increasing
    total = 0

    # Sentinel value at index n to flush remaining stack entries
    for i in range(n + 1):
        cur = arr[i] if i < n else 0
        while stack and arr[stack[-1]] >= cur:
            mid = stack.pop()
            left = stack[-1] if stack else -1
            right = i
            left_count = mid - left
            right_count = right - mid
            total = (total + arr[mid] * left_count * right_count) % MOD
        stack.append(i)

    return total


if __name__ == "__main__":
    print("=== Sum of Subarray Minimums ===")
    tests = [
        ([3, 1, 2, 4], 17),
        ([11, 7, 2, 4], 228),       # known LeetCode example variant
        ([1], 1),
        ([3, 1, 2, 4], 17),
    ]
    for arr, expected in tests:
        b = brute_force(arr)
        o = optimal(arr)
        bst = best(arr)
        ok = "OK" if o == expected and bst == expected else f"EXP {expected}"
        print(f"arr={arr}  brute={b}  optimal={o}  best={bst}  [{ok}]")
