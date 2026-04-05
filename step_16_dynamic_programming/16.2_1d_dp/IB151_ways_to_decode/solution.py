"""
Problem: Ways to Decode
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

A message containing letters 'A'-'Z' is encoded as '1'-'26'.
Given an encoded string of digits, count the number of ways to decode it.

Real-life analogy: decoding a compressed SMS protocol where letters map to numbers.
"""
from typing import List
import sys
sys.setrecursionlimit(10000)


# ============================================================
# APPROACH 1: BRUTE FORCE (Plain Recursion)
# Time: O(2^n)  |  Space: O(n) recursion stack
# ============================================================
# At each position, branch into taking 1 digit or 2 digits.
# Return 1 when the whole string is consumed, 0 on invalid paths.
def brute_force(s: str) -> int:
    def solve(idx: int) -> int:
        if idx == len(s):
            return 1                      # successfully decoded
        if s[idx] == '0':
            return 0                      # leading zero is invalid

        ways = solve(idx + 1)             # take one digit

        if idx + 1 < len(s):             # try two digits
            two = int(s[idx:idx + 2])
            if 10 <= two <= 26:
                ways += solve(idx + 2)
        return ways

    if not s:
        return 0
    return solve(0)


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
# Time: O(n)  |  Space: O(n)
# ============================================================
# Cache the result for each starting index to avoid re-computation.
def optimal(s: str) -> int:
    from functools import lru_cache

    if not s:
        return 0

    @lru_cache(maxsize=None)
    def solve(idx: int) -> int:
        if idx == len(s):
            return 1
        if s[idx] == '0':
            return 0

        ways = solve(idx + 1)

        if idx + 1 < len(s):
            two = int(s[idx:idx + 2])
            if 10 <= two <= 26:
                ways += solve(idx + 2)
        return ways

    return solve(0)


# ============================================================
# APPROACH 3: BEST (Bottom-Up DP, O(1) Space)
# Time: O(n)  |  Space: O(1)
# ============================================================
# Use two rolling variables (prev2, prev1) representing dp[i-2] and dp[i-1].
# Transition:
#   curr  += prev1  if s[i-1] != '0'         (single-digit decode)
#   curr  += prev2  if s[i-2:i] in [10..26]  (two-digit decode)
def best(s: str) -> int:
    if not s or s[0] == '0':
        return 0

    n = len(s)
    prev2 = 1   # dp[0]: empty prefix
    prev1 = 1   # dp[1]: first character (already verified non-zero)

    for i in range(2, n + 1):
        curr = 0

        # Single-digit decode
        if s[i - 1] != '0':
            curr += prev1

        # Two-digit decode
        two = int(s[i - 2:i])
        if 10 <= two <= 26:
            curr += prev2

        prev2, prev1 = prev1, curr

    return prev1


if __name__ == "__main__":
    print("=== Ways to Decode ===")
    test_cases = [
        ("12",    2),
        ("226",   3),
        ("06",    0),
        ("0",     0),
        ("10",    1),
        ("27",    1),
        ("11106", 2),
    ]

    for s, expected in test_cases:
        bf = brute_force(s)
        op = optimal(s)
        be = best(s)
        status = "PASS" if bf == op == be == expected else "FAIL"
        print(f"[{status}] Input: {s!r:10s} | Brute: {bf} | Optimal: {op} | Best: {be} | Expected: {expected}")
