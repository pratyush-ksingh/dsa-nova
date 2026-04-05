"""
Problem: Climbing Stairs (LeetCode #70)
Difficulty: EASY | XP: 10

N stairs, can climb 1 or 2 steps. How many distinct ways to reach the top?
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n)
# ============================================================
def climb_recursive(n: int) -> int:
    """Direct recurrence. Exponential time."""
    if n <= 2:
        return n
    return climb_recursive(n - 1) + climb_recursive(n - 2)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n) | Space: O(n)
# ============================================================
def climb_memo(n: int) -> int:
    """Cache computed results to avoid redundant calls."""
    dp = [-1] * (n + 1)

    def solve(n: int) -> int:
        if n <= 2:
            return n
        if dp[n] != -1:
            return dp[n]
        dp[n] = solve(n - 1) + solve(n - 2)
        return dp[n]

    return solve(n)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n) | Space: O(n)
# ============================================================
def climb_tab(n: int) -> int:
    """Build dp array from base cases upward."""
    if n <= 2:
        return n

    dp = [0] * (n + 1)
    dp[1] = 1
    dp[2] = 2

    for i in range(3, n + 1):
        dp[i] = dp[i - 1] + dp[i - 2]

    return dp[n]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(n) | Space: O(1)
# ============================================================
def climb_space(n: int) -> int:
    """Only keep two previous values."""
    if n <= 2:
        return n

    prev2, prev1 = 1, 2  # ways(1), ways(2)

    for i in range(3, n + 1):
        curr = prev1 + prev2
        prev2 = prev1
        prev1 = curr

    return prev1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Climbing Stairs ===\n")

    test_cases = [
        (1, 1), (2, 2), (3, 3), (4, 5), (5, 8),
        (10, 89), (20, 10946), (45, 1836311903),
    ]

    print(f"{'n':<5} {'Recurse':<12} {'Memo':<12} {'Tab':<12} {'SpaceOpt':<14} {'Expected':<14} {'Pass':<6}")
    print("-" * 75)

    for n, expected in test_cases:
        r = climb_recursive(n) if n <= 25 else "skip"
        m = climb_memo(n)
        t = climb_tab(n)
        s = climb_space(n)

        passes = m == expected and t == expected and s == expected
        if n <= 25:
            passes = passes and (r == expected)

        print(f"{n:<5} {str(r):<12} {m:<12} {t:<12} {s:<14} {expected:<14} {passes}")
