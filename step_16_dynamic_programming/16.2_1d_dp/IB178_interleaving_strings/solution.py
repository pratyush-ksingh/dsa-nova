"""
Problem: Interleaving Strings
Difficulty: HARD | XP: 50
Source: InterviewBit

Given strings s1, s2, and s3, determine whether s3 is formed by an
interleaving of s1 and s2. An interleaving preserves the relative order
of characters from each source string.
"""
from typing import List, Optional
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE — Pure recursion
# Time: O(2^(m+n))  |  Space: O(m + n) stack
# ============================================================
def brute_force(s1: str, s2: str, s3: str) -> bool:
    """
    At each position in s3, try matching with the next char from s1 or s2.
    """
    if len(s1) + len(s2) != len(s3):
        return False

    def solve(i, j, k):
        if k == len(s3):
            return i == len(s1) and j == len(s2)
        result = False
        if i < len(s1) and s1[i] == s3[k]:
            result = solve(i + 1, j, k + 1)
        if not result and j < len(s2) and s2[j] == s3[k]:
            result = solve(i, j + 1, k + 1)
        return result

    return solve(0, 0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — Recursion with memoization
# Time: O(m * n)  |  Space: O(m * n)
# ============================================================
def optimal(s1: str, s2: str, s3: str) -> bool:
    """
    Memoized recursion. State is (i, j) since k = i + j.
    """
    if len(s1) + len(s2) != len(s3):
        return False

    @lru_cache(maxsize=None)
    def dp(i: int, j: int) -> bool:
        k = i + j
        if k == len(s3):
            return i == len(s1) and j == len(s2)
        result = False
        if i < len(s1) and s1[i] == s3[k]:
            result = dp(i + 1, j)
        if not result and j < len(s2) and s2[j] == s3[k]:
            result = dp(i, j + 1)
        return result

    return dp(0, 0)


# ============================================================
# APPROACH 3: BEST — Bottom-up DP with O(n) space
# Time: O(m * n)  |  Space: O(n)
# ============================================================
def best(s1: str, s2: str, s3: str) -> bool:
    """
    dp[j] = whether s1[0:i] and s2[0:j] can interleave to form s3[0:i+j].
    Only keep one row at a time.
    """
    m, n = len(s1), len(s2)
    if m + n != len(s3):
        return False

    dp = [False] * (n + 1)
    dp[0] = True

    # First row: only s2 characters
    for j in range(1, n + 1):
        dp[j] = dp[j - 1] and s2[j - 1] == s3[j - 1]

    for i in range(1, m + 1):
        # First column: only s1 characters
        dp[0] = dp[0] and s1[i - 1] == s3[i - 1]
        for j in range(1, n + 1):
            k = i + j - 1
            dp[j] = ((dp[j] and s1[i - 1] == s3[k]) or
                      (dp[j - 1] and s2[j - 1] == s3[k]))

    return dp[n]


if __name__ == "__main__":
    print("=== Interleaving Strings ===")

    s1, s2, s3 = "aabcc", "dbbca", "aadbbcbcac"
    print(f"Brute:   {brute_force(s1, s2, s3)}")   # True
    print(f"Optimal: {optimal(s1, s2, s3)}")       # True
    print(f"Best:    {best(s1, s2, s3)}")          # True

    s1, s2, s3 = "aabcc", "dbbca", "aadbbbaccc"
    print(f"Brute:   {brute_force(s1, s2, s3)}")   # False
    print(f"Optimal: {optimal(s1, s2, s3)}")       # False
    print(f"Best:    {best(s1, s2, s3)}")          # False

    s1, s2, s3 = "", "", ""
    print(f"Brute:   {brute_force(s1, s2, s3)}")   # True
    print(f"Optimal: {optimal(s1, s2, s3)}")       # True
    print(f"Best:    {best(s1, s2, s3)}")          # True
