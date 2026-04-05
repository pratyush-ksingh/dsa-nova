"""
Problem: Regular Expression Match
Difficulty: HARD | XP: 50
Source: InterviewBit

Implement regular expression matching with support for '.' and '*'.
'.' matches any single character.
'*' matches zero or more of the preceding element.
The matching should cover the entire input string.
"""
from typing import List, Optional
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE — Pure recursion
# Time: O(2^(m+n)) worst case  |  Space: O(m + n) stack
# ============================================================
def brute_force(s: str, p: str) -> bool:
    """
    Recursive matching. If pattern has a '*' as second char, either
    skip the pattern pair (zero matches) or consume one char if it matches.
    """
    if not p:
        return not s

    first_match = bool(s) and (p[0] == s[0] or p[0] == '.')

    if len(p) >= 2 and p[1] == '*':
        # Skip this pattern (zero occurrences) or consume one char
        return (brute_force(s, p[2:]) or
                (first_match and brute_force(s[1:], p)))
    else:
        return first_match and brute_force(s[1:], p[1:])


# ============================================================
# APPROACH 2: OPTIMAL — Recursion with memoization
# Time: O(m * n)  |  Space: O(m * n)
# ============================================================
def optimal(s: str, p: str) -> bool:
    """
    Top-down DP with memoization on indices (i, j).
    """
    @lru_cache(maxsize=None)
    def dp(i: int, j: int) -> bool:
        if j == len(p):
            return i == len(s)

        first_match = i < len(s) and (p[j] == s[i] or p[j] == '.')

        if j + 1 < len(p) and p[j + 1] == '*':
            return (dp(i, j + 2) or
                    (first_match and dp(i + 1, j)))
        else:
            return first_match and dp(i + 1, j + 1)

    return dp(0, 0)


# ============================================================
# APPROACH 3: BEST — Bottom-up DP table
# Time: O(m * n)  |  Space: O(m * n)
# ============================================================
def best(s: str, p: str) -> bool:
    """
    dp[i][j] = whether s[0:i] matches p[0:j].
    Build table bottom-up.
    """
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[0][0] = True

    # Handle patterns like a*, a*b*, a*b*c* matching empty string
    for j in range(2, n + 1):
        if p[j - 1] == '*':
            dp[0][j] = dp[0][j - 2]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if p[j - 1] == '*':
                # Zero occurrences of preceding element
                dp[i][j] = dp[i][j - 2]
                # One or more occurrences
                if p[j - 2] == s[i - 1] or p[j - 2] == '.':
                    dp[i][j] = dp[i][j] or dp[i - 1][j]
            elif p[j - 1] == s[i - 1] or p[j - 1] == '.':
                dp[i][j] = dp[i - 1][j - 1]

    return dp[m][n]


if __name__ == "__main__":
    print("=== Regular Expression Match ===")

    print(f"Brute:   {brute_force('aa', 'a')}")        # False
    print(f"Optimal: {optimal('aa', 'a')}")            # False
    print(f"Best:    {best('aa', 'a')}")               # False

    print(f"Brute:   {brute_force('aa', 'a*')}")       # True
    print(f"Optimal: {optimal('aa', 'a*')}")           # True
    print(f"Best:    {best('aa', 'a*')}")              # True

    print(f"Brute:   {brute_force('ab', '.*')}")       # True
    print(f"Optimal: {optimal('ab', '.*')}")           # True
    print(f"Best:    {best('ab', '.*')}")              # True

    print(f"Brute:   {brute_force('aab', 'c*a*b')}")   # True
    print(f"Optimal: {optimal('aab', 'c*a*b')}")      # True
    print(f"Best:    {best('aab', 'c*a*b')}")         # True
