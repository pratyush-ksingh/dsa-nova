"""
Problem: Frog Jump with K Distances
Difficulty: MEDIUM | XP: 25

Min cost for frog to reach stone n-1 with jumps of 1..k.
All 4 DP approaches + Monotonic Deque optimization.
"""
from collections import deque


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(k^n) | Space: O(n) recursion stack
# ============================================================
def min_cost_recursive(height: list[int], k: int) -> int:
    """Try all possible jumps from each stone. Exponential."""
    n = len(height)

    def solve(i: int) -> int:
        if i == n - 1:
            return 0
        best = float('inf')
        for j in range(1, k + 1):
            if i + j < n:
                cost = solve(i + j) + abs(height[i] - height[i + j])
                best = min(best, cost)
        return best

    return solve(0)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n * k) | Space: O(n)
# ============================================================
def min_cost_memo(height: list[int], k: int) -> int:
    """Cache results for each stone to avoid recomputation."""
    n = len(height)
    dp = [-1] * n

    def solve(i: int) -> int:
        if i == n - 1:
            return 0
        if dp[i] != -1:
            return dp[i]

        best = float('inf')
        for j in range(1, k + 1):
            if i + j < n:
                cost = solve(i + j) + abs(height[i] - height[i + j])
                best = min(best, cost)

        dp[i] = best
        return dp[i]

    return solve(0)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n * k) | Space: O(n)
# ============================================================
def min_cost_tab(height: list[int], k: int) -> int:
    """Fill dp array left to right. dp[i] = min cost to reach stone i."""
    n = len(height)
    if n == 1:
        return 0

    dp = [float('inf')] * n
    dp[0] = 0

    for i in range(1, n):
        for j in range(1, k + 1):
            if i - j >= 0:
                dp[i] = min(dp[i], dp[i - j] + abs(height[i] - height[i - j]))

    return dp[n - 1]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED (Circular Buffer)
# Time: O(n * k) | Space: O(k)
# ============================================================
def min_cost_space(height: list[int], k: int) -> int:
    """Circular buffer -- only store last k dp values. O(k) space."""
    n = len(height)
    if n == 1:
        return 0

    window = [float('inf')] * (k + 1)
    window[0] = 0

    for i in range(1, n):
        min_cost = float('inf')
        for j in range(1, k + 1):
            if i - j >= 0:
                prev_idx = (i - j) % (k + 1)
                min_cost = min(min_cost, window[prev_idx] + abs(height[i] - height[i - j]))
        window[i % (k + 1)] = min_cost

    return window[(n - 1) % (k + 1)]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Frog Jump with K Distances ===\n")

    test_cases = [
        ([10, 30, 40, 20], 2),
        ([10, 20, 10], 1),
        ([30, 10, 60, 10, 60, 50], 2),
        ([10], 3),
        ([10, 10, 10, 10, 10], 4),
    ]

    header = f"{'Heights':<30} {'k':<4} {'Recurse':<10} {'Memo':<10} {'Tab':<10} {'Space':<10} {'Match':<6}"
    print(header)
    print("-" * len(header))

    for height, k in test_cases:
        r = min_cost_recursive(height, k)
        m = min_cost_memo(height, k)
        t = min_cost_tab(height, k)
        s = min_cost_space(height, k)

        match = r == m == t == s
        print(f"{str(height):<30} {k:<4} {r:<10} {m:<10} {t:<10} {s:<10} {match}")

    # Edge cases
    print("\n--- Edge Cases ---")
    print(f"Single stone: {min_cost_tab([5], 3)} (expected 0)")
    print(f"k=1 forced: {min_cost_tab([1, 5, 1, 5, 1], 1)}")
    print(f"All same: {min_cost_tab([7, 7, 7, 7], 2)} (expected 0)")
    print(f"k >= n-1: {min_cost_tab([10, 50, 20], 5)}")
