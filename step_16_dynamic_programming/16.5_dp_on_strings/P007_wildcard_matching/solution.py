"""
Problem: Wildcard Matching (LeetCode 44)
Difficulty: HARD | XP: 50

Given an input string s and a pattern p, implement wildcard matching where:
  '?' matches any single character.
  '*' matches any sequence of characters (including empty sequence).
Return true if s matches the pattern p in its entirety.

Example: s="adceb", p="*a*b" => True
         s="acdcb", p="a*c?b" => False
"""
from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE — Pure Recursion
# Time: O(2^(m+n)) worst case  |  Space: O(m+n) stack
# No memoization. '*' branches into match-empty or match-one.
# ============================================================
def brute_force(s: str, p: str) -> bool:
    """
    Recursive matching:
    - If p[j] == '*': try matching empty (advance j) OR match one char (advance i).
    - If p[j] == '?' or p[j] == s[i]: advance both.
    - Else: no match.
    """
    def rec(i: int, j: int) -> bool:
        # Both exhausted
        if i == len(s) and j == len(p):
            return True
        # Pattern exhausted but string remains
        if j == len(p):
            return False
        # String exhausted
        if i == len(s):
            # Remaining pattern must all be '*'
            return all(c == '*' for c in p[j:])

        if p[j] == '*':
            # Match empty (skip '*') or match one char of s (keep '*')
            return rec(i, j + 1) or rec(i + 1, j)
        elif p[j] == '?' or p[j] == s[i]:
            return rec(i + 1, j + 1)
        else:
            return False

    return rec(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — 2D DP (Bottom-Up)
# Time: O(m * n)  |  Space: O(m * n)
# dp[i][j] = True if s[:i] matches p[:j]
# ============================================================
def optimal(s: str, p: str) -> bool:
    """
    2D DP table where dp[i][j] = True means s[0..i-1] matches p[0..j-1].

    Base cases:
      dp[0][0] = True        (empty string matches empty pattern)
      dp[i][0] = False       (non-empty string can't match empty pattern)
      dp[0][j] = dp[0][j-1] if p[j-1] == '*'  (leading stars match empty)

    Transitions for i >= 1, j >= 1:
      if p[j-1] == '*':
          dp[i][j] = dp[i-1][j] (use '*' to match s[i-1])
                   | dp[i][j-1] (use '*' to match empty)
      elif p[j-1] == '?' or p[j-1] == s[i-1]:
          dp[i][j] = dp[i-1][j-1]
      else:
          dp[i][j] = False
    """
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[0][0] = True

    # Leading '*' in pattern can match empty string
    for j in range(1, n + 1):
        if p[j - 1] == '*':
            dp[0][j] = dp[0][j - 1]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if p[j - 1] == '*':
                dp[i][j] = dp[i - 1][j] or dp[i][j - 1]
            elif p[j - 1] == '?' or p[j - 1] == s[i - 1]:
                dp[i][j] = dp[i - 1][j - 1]
            # else remains False

    return dp[m][n]


# ============================================================
# APPROACH 3: BEST — 1D Space-Optimized DP
# Time: O(m * n)  |  Space: O(n)
# Roll the 2D DP into two 1D arrays (or one with careful updates).
# ============================================================
def best(s: str, p: str) -> bool:
    """
    Space-optimized DP using a single 1D array of size n+1.
    We also track `prev` (the diagonal value dp[i-1][j-1]) separately.

    Optimization note: an even faster approach for pathological cases
    is the two-pointer greedy, but DP is cleaner and O(mn) guaranteed.
    """
    m, n = len(s), len(p)
    # dp[j] = can s[:i] match p[:j]  (current row being filled)
    dp = [False] * (n + 1)
    dp[0] = True

    # Initialize leading stars
    for j in range(1, n + 1):
        if p[j - 1] == '*':
            dp[j] = dp[j - 1]
        else:
            break  # once we hit non-'*', rest stay False

    for i in range(1, m + 1):
        prev = dp[0]   # stores dp[i-1][j-1] before overwrite
        dp[0] = False  # dp[i][0] is False for i >= 1 (s non-empty, p empty)
        for j in range(1, n + 1):
            temp = dp[j]  # save dp[i-1][j] before we overwrite it
            if p[j - 1] == '*':
                dp[j] = dp[j] or dp[j - 1]
                # dp[j] (old) = dp[i-1][j], dp[j-1] = dp[i][j-1]
            elif p[j - 1] == '?' or p[j - 1] == s[i - 1]:
                dp[j] = prev
            else:
                dp[j] = False
            prev = temp

    return dp[n]


if __name__ == "__main__":
    print("=== Wildcard Matching ===")

    test_cases = [
        ("aa",    "a",     False),
        ("aa",    "*",     True),
        ("cb",    "?a",    False),
        ("adceb", "*a*b",  True),
        ("acdcb", "a*c?b", False),
        ("",      "",      True),
        ("",      "*",     True),
        ("",      "?",     False),
        ("abc",   "a?c",   True),
        ("abc",   "a*c",   True),
    ]

    for s, p, expected in test_cases:
        b = brute_force(s, p)
        o = optimal(s, p)
        bt = best(s, p)
        ok = "OK" if b == o == bt == expected else "FAIL"
        print(f"  s={s!r}, p={p!r} => Brute={b}, Optimal={o}, Best={bt}, "
              f"Expected={expected} [{ok}]")
