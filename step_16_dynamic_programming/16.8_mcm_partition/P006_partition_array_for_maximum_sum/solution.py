"""
Problem: Partition Array for Maximum Sum (LeetCode 1043)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(k^n)  |  Space: O(n) recursion stack
# At each index, try all partition lengths 1..k recursively.
# ============================================================
def brute_force(arr: List[int], k: int) -> int:
    n = len(arr)

    def solve(idx: int) -> int:
        if idx == n:
            return 0
        max_val = 0
        best_sum = 0
        for length in range(1, k + 1):
            if idx + length > n:
                break
            max_val = max(max_val, arr[idx + length - 1])
            curr = max_val * length + solve(idx + length)
            best_sum = max(best_sum, curr)
        return best_sum

    return solve(0)


# ============================================================
# APPROACH 2: OPTIMAL -- DP Tabulation
# Time: O(n * k)  |  Space: O(n)
# dp[i] = max sum for arr[i..n-1].
# For each position, try all partition lengths 1..k.
# ============================================================
def optimal(arr: List[int], k: int) -> int:
    n = len(arr)
    dp = [0] * (n + 1)  # dp[n] = 0 (base)

    for i in range(n - 1, -1, -1):
        max_val = 0
        best = 0
        for length in range(1, k + 1):
            if i + length > n:
                break
            max_val = max(max_val, arr[i + length - 1])
            curr = max_val * length + dp[i + length]
            best = max(best, curr)
        dp[i] = best

    return dp[0]


# ============================================================
# APPROACH 3: BEST -- DP (forward direction, same complexity)
# Time: O(n * k)  |  Space: O(n)
# dp[i] = max sum for arr[0..i-1] (first i elements).
# For each i, look back up to k steps.
# ============================================================
def best(arr: List[int], k: int) -> int:
    n = len(arr)
    dp = [0] * (n + 1)  # dp[0] = 0

    for i in range(1, n + 1):
        max_val = 0
        best_val = 0
        for length in range(1, k + 1):
            if i - length < 0:
                break
            max_val = max(max_val, arr[i - length])
            curr = max_val * length + dp[i - length]
            best_val = max(best_val, curr)
        dp[i] = best_val

    return dp[n]


if __name__ == "__main__":
    print("=== Partition Array for Maximum Sum ===\n")

    arr1 = [1, 15, 7, 9, 2, 5, 10]
    k1 = 3
    print(f"Brute:   {brute_force(arr1, k1)}")   # 84
    print(f"Optimal: {optimal(arr1, k1)}")         # 84
    print(f"Best:    {best(arr1, k1)}")            # 84

    arr2 = [1, 4, 1, 5, 7, 3, 6, 1, 9, 9, 3]
    k2 = 4
    print(f"\nBrute:   {brute_force(arr2, k2)}")  # 83
    print(f"Optimal: {optimal(arr2, k2)}")          # 83
    print(f"Best:    {best(arr2, k2)}")             # 83

    arr3 = [1]
    k3 = 1
    print(f"\nSingle: {best(arr3, k3)}")  # 1
