"""
Problem: Scramble String
Difficulty: HARD | XP: 50
Source: InterviewBit

Given two strings s1 and s2 of the same length, determine if s2 is a
scrambled version of s1. A scramble is formed by splitting s into two
non-empty parts, optionally swapping them, and recursively scrambling
each part.
"""
from typing import List, Optional
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE — Pure recursion
# Time: O(5^n) exponential  |  Space: O(n) recursion stack
# ============================================================
def brute_force(s1: str, s2: str) -> bool:
    """
    Try every split point. For each split, check two cases:
    1. No swap: left1 matches left2 AND right1 matches right2
    2. Swap: left1 matches right2 AND right1 matches left2
    """
    if len(s1) != len(s2):
        return False
    if s1 == s2:
        return True
    if sorted(s1) != sorted(s2):
        return False

    n = len(s1)
    for i in range(1, n):
        # No swap
        if (brute_force(s1[:i], s2[:i]) and brute_force(s1[i:], s2[i:])):
            return True
        # Swap
        if (brute_force(s1[:i], s2[n - i:]) and brute_force(s1[i:], s2[:n - i])):
            return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL — Recursion with memoization
# Time: O(n^4)  |  Space: O(n^4)
# ============================================================
def optimal(s1: str, s2: str) -> bool:
    """
    Memoized recursion using lru_cache on (s1, s2) pairs.
    """
    @lru_cache(maxsize=None)
    def solve(a: str, b: str) -> bool:
        if a == b:
            return True
        if sorted(a) != sorted(b):
            return False
        n = len(a)
        for i in range(1, n):
            if (solve(a[:i], b[:i]) and solve(a[i:], b[i:])):
                return True
            if (solve(a[:i], b[n - i:]) and solve(a[i:], b[:n - i])):
                return True
        return False

    if len(s1) != len(s2):
        return False
    return solve(s1, s2)


# ============================================================
# APPROACH 3: BEST — Bottom-up 3D DP
# Time: O(n^4)  |  Space: O(n^3)
# ============================================================
def best(s1: str, s2: str) -> bool:
    """
    dp[length][i][j] = True if s1[i:i+length] is a scramble of s2[j:j+length].
    Base: dp[1][i][j] = (s1[i] == s2[j]).
    Transition: for each split k in 1..length-1:
      dp[length][i][j] |= (dp[k][i][j] and dp[length-k][i+k][j+k])   # no swap
      dp[length][i][j] |= (dp[k][i][j+length-k] and dp[length-k][i+k][j])  # swap
    """
    n = len(s1)
    if n != len(s2):
        return False
    if s1 == s2:
        return True
    if sorted(s1) != sorted(s2):
        return False

    dp = [[[False] * n for _ in range(n)] for _ in range(n + 1)]

    # Base case: single characters
    for i in range(n):
        for j in range(n):
            dp[1][i][j] = (s1[i] == s2[j])

    for length in range(2, n + 1):
        for i in range(n - length + 1):
            for j in range(n - length + 1):
                for k in range(1, length):
                    if dp[k][i][j] and dp[length - k][i + k][j + k]:
                        dp[length][i][j] = True
                        break
                    if dp[k][i][j + length - k] and dp[length - k][i + k][j]:
                        dp[length][i][j] = True
                        break

    return dp[n][0][0]


if __name__ == "__main__":
    print("=== Scramble String ===")

    s1, s2 = "great", "rgeat"
    print(f"Brute:   {brute_force(s1, s2)}")   # True
    print(f"Optimal: {optimal(s1, s2)}")       # True
    print(f"Best:    {best(s1, s2)}")          # True

    s1, s2 = "abcde", "caebd"
    print(f"Brute:   {brute_force(s1, s2)}")   # False
    print(f"Optimal: {optimal(s1, s2)}")       # False
    print(f"Best:    {best(s1, s2)}")          # False
