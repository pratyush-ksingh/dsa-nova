"""
Problem: Shortest Common Supersequence (LeetCode 1092)
Difficulty: HARD | XP: 50

Given two strings str1 and str2, return the shortest string that has
BOTH str1 and str2 as subsequences. If multiple answers exist, return any.

Key insight: SCS length = len(str1) + len(str2) - LCS length.
Build the LCS DP table, then backtrack to reconstruct the actual string.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^(m+n))  |  Space: O(m+n)
# Generate all supersequences of str1 and str2, return shortest
# ============================================================
def brute_force(str1: str, str2: str) -> str:
    """
    Recursively build all possible supersequences and keep the shortest
    one that still contains both str1 and str2 as subsequences.

    At each step, if both pointers are valid:
      - If characters match: consume both and emit the character.
      - Else: try emitting str1[i] (advance i) OR str2[j] (advance j),
        pick the shorter result.
    """
    def helper(i: int, j: int) -> str:
        # Base cases: exhaust one or both strings
        if i == len(str1):
            return str2[j:]
        if j == len(str2):
            return str1[i:]
        if str1[i] == str2[j]:
            return str1[i] + helper(i + 1, j + 1)
        # Try both options, take the shorter
        option_a = str1[i] + helper(i + 1, j)   # take from str1
        option_b = str2[j] + helper(i, j + 1)   # take from str2
        return option_a if len(option_a) <= len(option_b) else option_b

    return helper(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL — LCS DP + backtrack reconstruction
# Time: O(m*n)  |  Space: O(m*n)
# Build full LCS table, then trace back to construct SCS
# ============================================================
def optimal(str1: str, str2: str) -> str:
    """
    1. Compute LCS DP table.
    2. Backtrack from dp[m][n]:
       - If str1[i-1] == str2[j-1]: this char is in LCS -> emit once, move diagonally.
       - Else if dp[i-1][j] >= dp[i][j-1]: emit str1[i-1], move up.
       - Else: emit str2[j-1], move left.
    3. Append any remaining characters from either string.
    4. Reverse the collected characters.
    """
    m, n = len(str1), len(str2)

    # Build LCS DP table
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if str1[i - 1] == str2[j - 1]:
                dp[i][j] = 1 + dp[i - 1][j - 1]
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

    # Backtrack to build SCS
    result = []
    i, j = m, n
    while i > 0 and j > 0:
        if str1[i - 1] == str2[j - 1]:
            result.append(str1[i - 1])  # common char: include once
            i -= 1
            j -= 1
        elif dp[i - 1][j] >= dp[i][j - 1]:
            result.append(str1[i - 1])  # take from str1
            i -= 1
        else:
            result.append(str2[j - 1])  # take from str2
            j -= 1

    # Append remaining characters
    while i > 0:
        result.append(str1[i - 1])
        i -= 1
    while j > 0:
        result.append(str2[j - 1])
        j -= 1

    return ''.join(reversed(result))


# ============================================================
# APPROACH 3: BEST — same O(m*n) but with space-awareness note
# Time: O(m*n)  |  Space: O(m*n)  (reconstruction requires full table)
# Cleaner iterative approach with explicit SCS string building
# ============================================================
def best(str1: str, str2: str) -> str:
    """
    Same algorithm as Approach 2 but builds the SCS character-by-character
    using a forward pass after computing the LCS DP table.
    Noted separately because in interviews you often describe this
    "forward construction" variant which is easier to visualise.
    """
    m, n = len(str1), len(str2)

    # LCS DP (identical to optimal)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if str1[i - 1] == str2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

    # Reconstruct from dp[m][n] using backtracking
    scs = []
    i, j = m, n
    while i > 0 and j > 0:
        if str1[i - 1] == str2[j - 1]:
            scs.append(str1[i - 1])
            i -= 1
            j -= 1
        elif dp[i - 1][j] > dp[i][j - 1]:
            scs.append(str1[i - 1])
            i -= 1
        else:
            scs.append(str2[j - 1])
            j -= 1
    # Drain remaining
    scs.extend(reversed(str1[:i]))
    scs.extend(reversed(str2[:j]))
    return ''.join(reversed(scs))


def _is_supersequence(scs: str, s: str) -> bool:
    """Helper: verify that s is a subsequence of scs."""
    it = iter(scs)
    return all(c in it for c in s)


if __name__ == "__main__":
    test_cases = [
        ("abac", "cab"),       # expected length 5, e.g. "cabac"
        ("geek", "eke"),       # expected length 5, e.g. "geeke"
        ("AGGTAB", "GXTXAYB"), # expected length 9
        ("abc", "abc"),        # same string -> length 3
        ("", "abc"),           # one empty -> return other
    ]

    print("=== Shortest Common Supersequence ===")
    for s1, s2 in test_cases:
        b  = brute_force(s1, s2)
        o  = optimal(s1, s2)
        bs = best(s1, s2)
        # All three must be valid supersequences of same minimal length
        ok_b  = _is_supersequence(b,  s1) and _is_supersequence(b,  s2)
        ok_o  = _is_supersequence(o,  s1) and _is_supersequence(o,  s2)
        ok_bs = _is_supersequence(bs, s1) and _is_supersequence(bs, s2)
        len_eq = len(b) == len(o) == len(bs)
        status = "PASS" if ok_b and ok_o and ok_bs and len_eq else "FAIL"
        print(f"[{status}] '{s1}' + '{s2}' -> brute='{b}'(len={len(b)}) "
              f"optimal='{o}'(len={len(o)}) best='{bs}'(len={len(bs)})")
