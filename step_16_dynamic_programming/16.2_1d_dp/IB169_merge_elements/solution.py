"""
Problem: Merge Elements
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array, merge adjacent elements with cost = sum of merged elements.
Find minimum total cost to merge all elements into one.
Classic interval DP (like Matrix Chain Multiplication / Stone Merging).
"""

from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE
# Recursive interval DP with memoization
# dp[i][j] = min cost to merge A[i..j] into one element
# Time: O(N^3)  |  Space: O(N^2)
# ============================================================
def brute_force(A: List[int]) -> int:
    n = len(A)
    if n <= 1:
        return 0

    # Prefix sums for range sum queries
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + A[i]

    @lru_cache(maxsize=None)
    def dp(i, j):
        if i == j:
            return 0
        range_sum = prefix[j + 1] - prefix[i]
        return min(dp(i, k) + dp(k + 1, j) + range_sum for k in range(i, j))

    return dp(0, n - 1)


# ============================================================
# APPROACH 2: OPTIMAL
# Bottom-up interval DP
# Time: O(N^3)  |  Space: O(N^2)
# ============================================================
def optimal(A: List[int]) -> int:
    n = len(A)
    if n <= 1:
        return 0

    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + A[i]

    dp = [[0] * n for _ in range(n)]

    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            range_sum = prefix[j + 1] - prefix[i]
            dp[i][j] = float('inf')
            for k in range(i, j):
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j] + range_sum)

    return dp[0][n - 1]


# ============================================================
# APPROACH 3: BEST
# Greedy simulation: always merge the two smallest adjacent elements
# NOTE: This is O(N^2) simulation. For correctness guarantee, use DP above.
# This works for cases where greedy == optimal (Huffman-like scenarios).
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def best(A: List[int]) -> int:
    # Use the correct DP solution for guaranteed optimality
    return optimal(A)


def best_greedy_simulation(A: List[int]) -> int:
    """Greedy heuristic — NOT always optimal, but intuitive."""
    lst = list(A)
    total = 0
    while len(lst) > 1:
        # Find adjacent pair with minimum sum
        min_sum = float('inf')
        min_idx = 0
        for i in range(len(lst) - 1):
            s = lst[i] + lst[i + 1]
            if s < min_sum:
                min_sum = s
                min_idx = i
        total += min_sum
        lst[min_idx] = min_sum
        lst.pop(min_idx + 1)
    return total


if __name__ == "__main__":
    print("=== Merge Elements ===")

    # [1,2,3] => merge(1,2)=3 + merge(3,3)=6 = 9 (optimal)
    A1 = [1, 2, 3]
    print(f"BruteForce [1,2,3]: {brute_force(A1)}")  # 9
    print(f"Optimal    [1,2,3]: {optimal(A1)}")       # 9
    print(f"Best       [1,2,3]: {best(A1)}")          # 9

    # [4,3,2,6] => DP optimal
    A2 = [4, 3, 2, 6]
    print(f"BruteForce [4,3,2,6]: {brute_force(A2)}")  # 29
    print(f"Optimal    [4,3,2,6]: {optimal(A2)}")       # 29

    A3 = [10]
    print(f"Single: {optimal(A3)}")  # 0

    A4 = [1, 1, 1, 1]
    print(f"[1,1,1,1]: {optimal(A4)}")  # 8
