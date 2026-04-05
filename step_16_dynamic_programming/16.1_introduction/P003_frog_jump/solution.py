"""
Problem: Frog Jump (Striver's DP Series)
Difficulty: EASY | XP: 10

Frog at stair 0, can jump 1 or 2 stairs. Cost = |height[i] - height[j]|.
Find minimum cost to reach stair N-1.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def frog_recursive(height: List[int]) -> int:
    """Direct recursion. Exponential time."""
    n = len(height)

    def solve(i: int) -> int:
        if i == 0:
            return 0

        one_jump = solve(i - 1) + abs(height[i] - height[i - 1])

        two_jump = float('inf')
        if i >= 2:
            two_jump = solve(i - 2) + abs(height[i] - height[i - 2])

        return min(one_jump, two_jump)

    return solve(n - 1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def frog_memo(height: List[int]) -> int:
    """Cache results to avoid recomputation."""
    n = len(height)
    dp = [-1] * n

    def solve(i: int) -> int:
        if i == 0:
            return 0
        if dp[i] != -1:
            return dp[i]

        one_jump = solve(i - 1) + abs(height[i] - height[i - 1])

        two_jump = float('inf')
        if i >= 2:
            two_jump = solve(i - 2) + abs(height[i] - height[i - 2])

        dp[i] = min(one_jump, two_jump)
        return dp[i]

    return solve(n - 1)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# ============================================================
def frog_tab(height: List[int]) -> int:
    """Fill dp array iteratively."""
    n = len(height)
    if n == 1:
        return 0

    dp = [0] * n
    dp[0] = 0
    dp[1] = abs(height[1] - height[0])

    for i in range(2, n):
        one_jump = dp[i - 1] + abs(height[i] - height[i - 1])
        two_jump = dp[i - 2] + abs(height[i] - height[i - 2])
        dp[i] = min(one_jump, two_jump)

    return dp[n - 1]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n) | Space: O(1)
# ============================================================
def frog_space(height: List[int]) -> int:
    """Only keep two previous costs."""
    n = len(height)
    if n == 1:
        return 0

    prev2 = 0                                    # dp[0]
    prev1 = abs(height[1] - height[0])           # dp[1]

    for i in range(2, n):
        one_jump = prev1 + abs(height[i] - height[i - 1])
        two_jump = prev2 + abs(height[i] - height[i - 2])
        curr = min(one_jump, two_jump)
        prev2 = prev1
        prev1 = curr

    return prev1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Frog Jump ===\n")

    test_cases = [
        ([10, 20, 30, 10], 20),
        ([10, 50, 10], 0),
        ([20, 30, 40, 20], 20),
        ([7], 0),
        ([10, 10, 10, 10], 0),
        ([10, 20, 10, 20, 10], 0),
    ]

    for height, expected in test_cases:
        r = frog_recursive(height)
        m = frog_memo(height)
        t = frog_tab(height)
        s = frog_space(height)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"height = {height}")
        print(f"  Recursive: {r} | Memo: {m} | Tab: {t} | Space: {s}")
        print(f"  Expected: {expected} | Pass: {passes}\n")
