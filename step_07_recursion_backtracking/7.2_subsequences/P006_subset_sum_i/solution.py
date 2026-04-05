"""
Problem: Subset Sum I
Difficulty: MEDIUM | XP: 25

Given an array of non-negative integers and a target sum,
check whether any subset sums to the target.
Real-life use: Budget allocation, partition problems, knapsack decisions.
"""
from typing import List, Dict, Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE
# Pure recursion: include or exclude each element.
# Time: O(2^N)  |  Space: O(N) recursion stack
# ============================================================
def brute_force(arr: List[int], target: int) -> bool:
    def solve(idx: int, remaining: int) -> bool:
        if remaining == 0:
            return True
        if idx == len(arr) or remaining < 0:
            return False
        return solve(idx + 1, remaining - arr[idx]) or solve(idx + 1, remaining)

    return solve(0, target)


# ============================================================
# APPROACH 2: OPTIMAL
# Top-down DP with memoization. State = (index, remaining).
# Time: O(N * target)  |  Space: O(N * target)
# ============================================================
def optimal(arr: List[int], target: int) -> bool:
    memo: Dict[Tuple[int, int], bool] = {}

    def solve(idx: int, remaining: int) -> bool:
        if remaining == 0:
            return True
        if idx == len(arr) or remaining < 0:
            return False
        if (idx, remaining) in memo:
            return memo[(idx, remaining)]
        result = solve(idx + 1, remaining - arr[idx]) or solve(idx + 1, remaining)
        memo[(idx, remaining)] = result
        return result

    return solve(0, target)


# ============================================================
# APPROACH 3: BEST
# Bottom-up DP (space-optimized 1D). Traverse sum right-to-left
# to avoid counting same element twice.
# Time: O(N * target)  |  Space: O(target)
# ============================================================
def best(arr: List[int], target: int) -> bool:
    dp = [False] * (target + 1)
    dp[0] = True
    for num in arr:
        for s in range(target, num - 1, -1):
            dp[s] = dp[s] or dp[s - num]
    return dp[target]


if __name__ == "__main__":
    print("=== Subset Sum I ===")

    tests = [
        ([3, 34, 4, 12, 5, 2], 9),    # True  (4+5 or 3+4+2)
        ([3, 34, 4, 12, 5, 2], 30),   # False
        ([1, 2, 3], 7),               # False (max=6)
        ([1, 2, 3], 6),               # True  (1+2+3)
    ]

    for arr, t in tests:
        print(f"\narr={arr}  target={t}")
        print(f"  Brute  : {brute_force(arr, t)}")
        print(f"  Optimal: {optimal(arr, t)}")
        print(f"  Best   : {best(arr, t)}")

    print("\nEdge: [] target=0 ->", best([], 0))
    print("Edge: [5] target=5 ->", best([5], 5))
    print("Edge: [5] target=3 ->", best([5], 3))
