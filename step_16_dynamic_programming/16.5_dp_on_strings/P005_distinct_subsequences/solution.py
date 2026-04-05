"""
Problem: Distinct Subsequences (LeetCode 115)
Difficulty: HARD | XP: 50

Given two strings s and t, return the number of distinct subsequences of s
which equals t. A subsequence is formed by deleting some (or no) characters
from s without changing the relative order of remaining characters.

Example: s="rabbbit", t="rabbit" => 3
  (three ways to choose which 'b' to delete)
"""
from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE — Pure Recursion
# Time: O(2^m)  |  Space: O(m) recursion stack  (m = len(s))
# At each position in s, decide to use or skip the character.
# Exponential because no memoization.
# ============================================================
def brute_force(s: str, t: str) -> int:
    """
    Recursive approach: for each character in s, either:
    1. Match it with current character in t (if equal), advance both pointers.
    2. Skip it, advance only s pointer.
    Sum both choices when characters match.
    """
    def rec(i: int, j: int) -> int:
        # All of t matched
        if j == len(t):
            return 1
        # s exhausted but t not finished
        if i == len(s):
            return 0
        # Skip s[i] always possible
        result = rec(i + 1, j)
        # Use s[i] if it matches t[j]
        if s[i] == t[j]:
            result += rec(i + 1, j + 1)
        return result

    return rec(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — 2D DP (Bottom-Up)
# Time: O(m * n)  |  Space: O(m * n)
# dp[i][j] = # distinct subseq of s[0..i-1] that equal t[0..j-1]
# ============================================================
def optimal(s: str, t: str) -> int:
    """
    Build a 2D table where dp[i][j] = number of ways to match t[:j]
    using characters from s[:i].

    Recurrence:
      dp[i][0] = 1  (empty t matched by any prefix of s)
      dp[0][j] = 0  for j > 0  (can't match non-empty t with empty s)
      if s[i-1] == t[j-1]:
          dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
                   (use s[i-1] to match t[j-1]) + (skip s[i-1])
      else:
          dp[i][j] = dp[i-1][j]  (skip s[i-1], must)
    """
    m, n = len(s), len(t)
    if n > m:
        return 0

    dp = [[0] * (n + 1) for _ in range(m + 1)]
    # Base case: empty t can be matched by any prefix of s
    for i in range(m + 1):
        dp[i][0] = 1

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            dp[i][j] = dp[i - 1][j]  # skip s[i-1]
            if s[i - 1] == t[j - 1]:
                dp[i][j] += dp[i - 1][j - 1]  # use s[i-1]

    return dp[m][n]


# ============================================================
# APPROACH 3: BEST — 1D DP (Space-Optimized)
# Time: O(m * n)  |  Space: O(n)
# Only need the previous row of dp, so use a single 1D array.
# Iterate j from right to left to avoid using updated values.
# ============================================================
def best(s: str, t: str) -> int:
    """
    Space-optimized DP using a single 1D array of size n+1.
    dp[j] represents dp[i][j] from the 2D version.
    We iterate j from n down to 1 so that dp[j-1] still holds
    the value from the previous row (dp[i-1][j-1]).
    """
    m, n = len(s), len(t)
    if n > m:
        return 0

    dp = [0] * (n + 1)
    dp[0] = 1  # empty t is always matchable

    for i in range(m):
        # Traverse right to left to preserve dp[j-1] from previous row
        for j in range(n, 0, -1):
            if s[i] == t[j - 1]:
                dp[j] += dp[j - 1]

    return dp[n]


if __name__ == "__main__":
    print("=== Distinct Subsequences ===")

    test_cases = [
        ("rabbbit", "rabbit", 3),
        ("babgbag", "bag", 5),
        ("b", "b", 1),
        ("b", "a", 0),
        ("aaa", "a", 3),
        ("aaa", "aa", 3),
        ("", "a", 0),
        ("a", "", 1),
    ]

    for s, t, expected in test_cases:
        b = brute_force(s, t)
        o = optimal(s, t)
        bt = best(s, t)
        ok = "OK" if b == o == bt == expected else "FAIL"
        print(f"  s={s!r}, t={t!r} => Brute={b}, Optimal={o}, Best={bt}, "
              f"Expected={expected} [{ok}]")
