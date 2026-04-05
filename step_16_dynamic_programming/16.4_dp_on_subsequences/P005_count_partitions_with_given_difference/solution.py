"""
Problem: Count Partitions with Given Difference
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(2^n)  |  Space: O(n) recursion stack
# Try including/excluding each element. Check if S1-S2 == D.
# Reduces to: count subsets with sum = (totalSum + D) / 2.
# ============================================================
def brute_force(arr: List[int], d: int) -> int:
    total = sum(arr)
    if (total + d) % 2 != 0 or total + d < 0:
        return 0
    target = (total + d) // 2

    def solve(idx: int, remaining: int) -> int:
        if idx == 0:
            if remaining == 0 and arr[0] == 0:
                return 2
            if remaining == 0 or remaining == arr[0]:
                return 1
            return 0
        ways = solve(idx - 1, remaining)
        if arr[idx] <= remaining:
            ways += solve(idx - 1, remaining - arr[idx])
        return ways

    return solve(len(arr) - 1, target)


# ============================================================
# APPROACH 2: OPTIMAL -- DP Tabulation
# Time: O(n * target)  |  Space: O(n * target)
# 2D DP: dp[i][s] = number of ways to form sum s using first i+1 elements.
# ============================================================
def optimal(arr: List[int], d: int) -> int:
    n = len(arr)
    total = sum(arr)
    if (total + d) % 2 != 0 or total + d < 0:
        return 0
    target = (total + d) // 2

    dp = [[0] * (target + 1) for _ in range(n)]

    if arr[0] == 0:
        dp[0][0] = 2
    else:
        dp[0][0] = 1
        if arr[0] <= target:
            dp[0][arr[0]] = 1

    for i in range(1, n):
        for s in range(target + 1):
            not_pick = dp[i - 1][s]
            pick = dp[i - 1][s - arr[i]] if arr[i] <= s else 0
            dp[i][s] = not_pick + pick

    return dp[n - 1][target]


# ============================================================
# APPROACH 3: BEST -- Space-Optimized 1D DP
# Time: O(n * target)  |  Space: O(target)
# Use two 1D arrays prev and curr.
# ============================================================
def best(arr: List[int], d: int) -> int:
    n = len(arr)
    total = sum(arr)
    if (total + d) % 2 != 0 or total + d < 0:
        return 0
    target = (total + d) // 2

    prev = [0] * (target + 1)

    if arr[0] == 0:
        prev[0] = 2
    else:
        prev[0] = 1
        if arr[0] <= target:
            prev[arr[0]] = 1

    for i in range(1, n):
        curr = [0] * (target + 1)
        for s in range(target + 1):
            not_pick = prev[s]
            pick = prev[s - arr[i]] if arr[i] <= s else 0
            curr[s] = not_pick + pick
        prev = curr

    return prev[target]


if __name__ == "__main__":
    print("=== Count Partitions with Given Difference ===\n")

    print(f"Brute:   {brute_force([5, 2, 6, 4], 3)}")   # 1
    print(f"Optimal: {optimal([5, 2, 6, 4], 3)}")         # 1
    print(f"Best:    {best([5, 2, 6, 4], 3)}")            # 1

    print(f"\nBrute:   {brute_force([1, 1, 1, 1], 0)}")   # 6
    print(f"Optimal: {optimal([1, 1, 1, 1], 0)}")          # 6
    print(f"Best:    {best([1, 1, 1, 1], 0)}")             # 6

    print(f"\nWith zeros: {best([0, 0, 1], 1)}")           # 4
