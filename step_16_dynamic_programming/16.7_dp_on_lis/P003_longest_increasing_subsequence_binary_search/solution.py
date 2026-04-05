"""
Problem: Longest Increasing Subsequence -- Binary Search
Difficulty: MEDIUM | XP: 25

O(n log n) LIS using patience sorting / tails array.
Shows all 4 approaches: Recursion, Memo, Tabulation O(n^2), Tails O(n log n).
"""
from bisect import bisect_left
from functools import lru_cache


# ============================================================
# APPROACH 1: PLAIN RECURSION (Take / Not Take)
# Time: O(2^n) | Space: O(n) stack
# ============================================================
def lis_recursive(nums: list[int]) -> int:
    """Try including or excluding each element."""
    n = len(nums)

    def solve(idx: int, prev_idx: int) -> int:
        if idx == n:
            return 0

        not_take = solve(idx + 1, prev_idx)
        take = 0
        if prev_idx == -1 or nums[idx] > nums[prev_idx]:
            take = 1 + solve(idx + 1, idx)

        return max(take, not_take)

    return solve(0, -1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n^2) | Space: O(n^2)
# ============================================================
def lis_memo(nums: list[int]) -> int:
    """Cache (idx, prev_idx) states."""
    n = len(nums)
    cache = {}

    def solve(idx: int, prev_idx: int) -> int:
        if idx == n:
            return 0
        if (idx, prev_idx) in cache:
            return cache[(idx, prev_idx)]

        not_take = solve(idx + 1, prev_idx)
        take = 0
        if prev_idx == -1 or nums[idx] > nums[prev_idx]:
            take = 1 + solve(idx + 1, idx)

        cache[(idx, prev_idx)] = max(take, not_take)
        return cache[(idx, prev_idx)]

    return solve(0, -1)


# ============================================================
# APPROACH 3: TABULATION -- Classic O(n^2) DP
# Time: O(n^2) | Space: O(n)
# ============================================================
def lis_tab(nums: list[int]) -> int:
    """dp[i] = LIS length ending at index i."""
    n = len(nums)
    dp = [1] * n  # each element is a subsequence of length 1

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)

    return max(dp)


# ============================================================
# APPROACH 4: TAILS ARRAY + BINARY SEARCH (Patience Sorting)
# Time: O(n log n) | Space: O(n)
# ============================================================
def lis_binary_search(nums: list[int]) -> int:
    """
    Maintain sorted tails array.
    tails[i] = smallest tail element for increasing subsequence of length i+1.

    For each num:
    - Binary search for leftmost position where tails[pos] >= num
    - If pos == len(tails): append (extends LIS)
    - Else: tails[pos] = num (replace with smaller tail)
    """
    tails = []

    for num in nums:
        pos = bisect_left(tails, num)  # leftmost position where tails[pos] >= num
        if pos == len(tails):
            tails.append(num)  # extends LIS
        else:
            tails[pos] = num  # replace with smaller tail

    return len(tails)


def lis_with_reconstruction(nums: list[int]) -> list[int]:
    """
    O(n log n) LIS with actual subsequence reconstruction.
    Track which position each element was placed at and its predecessor.
    """
    n = len(nums)
    tails = []
    pos_holder = []  # pos_holder[pos] = index in nums at this tails position
    parent = [-1] * n

    for i in range(n):
        pos = bisect_left(tails, nums[i])
        if pos == len(tails):
            tails.append(nums[i])
            pos_holder.append(i)
        else:
            tails[pos] = nums[i]
            pos_holder[pos] = i

        if pos > 0:
            parent[i] = pos_holder[pos - 1]

    # Backtrack
    result = []
    idx = pos_holder[len(tails) - 1]
    while idx != -1:
        result.append(nums[idx])
        idx = parent[idx]

    return result[::-1]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Longest Increasing Subsequence (Binary Search) ===\n")

    test_cases = [
        ([10, 9, 2, 5, 3, 7, 101, 18], 4),
        ([0, 1, 0, 3, 2, 3], 4),
        ([7, 7, 7, 7, 7], 1),
        ([1, 2, 3, 4, 5], 5),
        ([5, 4, 3, 2, 1], 1),
        ([3, 1, 4, 1, 5, 9, 2, 6], 5),
        ([1], 1),
    ]

    header = f"{'nums':<35} {'Rec':<6} {'Memo':<6} {'Tab':<6} {'BS':<6} {'Exp':<6} {'Pass':<6}"
    print(header)
    print("-" * len(header))

    for nums, expected in test_cases:
        r = lis_recursive(nums)
        m = lis_memo(nums)
        t = lis_tab(nums)
        b = lis_binary_search(nums)

        passes = r == expected and m == expected and t == expected and b == expected
        print(f"{str(nums):<35} {r:<6} {m:<6} {t:<6} {b:<6} {expected:<6} {passes}")

    # Show tails array evolution
    print("\n--- Tails Array Evolution ---")
    demo = [10, 9, 2, 5, 3, 7, 101, 18]
    tails = []
    for num in demo:
        pos = bisect_left(tails, num)
        if pos == len(tails):
            tails.append(num)
        else:
            tails[pos] = num
        print(f"  Process {num:>3} -> tails = {tails}")
    print(f"LIS length = {len(tails)}")

    # Reconstruct actual LIS
    print("\n--- LIS Reconstruction ---")
    actual_lis = lis_with_reconstruction(demo)
    print(f"One valid LIS: {actual_lis}")

    # Non-decreasing variant
    print("\n--- Strictly Increasing vs Non-Decreasing ---")
    arr = [1, 3, 3, 5]
    print(f"Array: {arr}")
    print(f"Strictly increasing LIS: {lis_binary_search(arr)}")
    # For non-decreasing, use bisect_right instead of bisect_left
    from bisect import bisect_right
    tails_nd = []
    for num in arr:
        pos = bisect_right(tails_nd, num)
        if pos == len(tails_nd):
            tails_nd.append(num)
        else:
            tails_nd[pos] = num
    print(f"Non-decreasing LIS: {len(tails_nd)}")
