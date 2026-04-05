"""
Problem: Minimum Window Subsequence
Difficulty: HARD | XP: 50

Find the smallest window in S such that T appears as a SUBSEQUENCE
(characters must appear in order, not necessarily contiguous).
Return "" if no such window exists.
Real-life use: Sequence alignment, DNA pattern search, diff tools.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Check every substring of S; verify if T is a subsequence of it.
# Time: O(N^2 * |T|)  |  Space: O(1)
# ============================================================
def brute_force(s: str, t: str) -> str:
    def is_subseq(window: str, t: str) -> bool:
        j = 0
        for c in window:
            if j < len(t) and c == t[j]:
                j += 1
        return j == len(t)

    result = ""
    for i in range(len(s)):
        for j in range(i + len(t), len(s) + 1):
            window = s[i:j]
            if is_subseq(window, t):
                if not result or len(window) < len(result):
                    result = window
                break  # shortest window starting at i
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Two-pointer approach:
# 1. Forward: match T left-to-right in S to find window end.
# 2. Backward: from end, match T right-to-left to shrink window start.
# Time: O(|S| * |T|) — O(|S|) amortized  |  Space: O(1)
# ============================================================
def optimal(s: str, t: str) -> str:
    min_len = float('inf')
    result = ""
    i = 0
    while i < len(s):
        # Forward pass
        j = 0
        while i < len(s) and j < len(t):
            if s[i] == t[j]:
                j += 1
            i += 1
        if j < len(t):
            break
        # Backward pass: shrink from end
        end = i - 1
        j = len(t) - 1
        while j >= 0:
            if s[end] == t[j]:
                j -= 1
            end -= 1
        start = end + 1
        if i - start < min_len:
            min_len = i - start
            result = s[start:i]
        i = start + 1  # restart from just after window start
    return result


# ============================================================
# APPROACH 3: BEST
# DP: dp[i][j] = start index in S from which T[0..j-1] is a subseq ending at S[i-1].
# Time: O(|S| * |T|)  |  Space: O(|S| * |T|)
# ============================================================
def best(s: str, t: str) -> str:
    m, n = len(s), len(t)
    # dp[i][j]: start index in S if T[0..j-1] matched ending at s[i-1]
    dp = [[-1] * (n + 1) for _ in range(m + 1)]
    for i in range(m + 1):
        dp[i][0] = i  # empty T matched at position i (start = i)

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s[i - 1] == t[j - 1]:
                dp[i][j] = dp[i - 1][j - 1]
            else:
                dp[i][j] = dp[i - 1][j]

    min_len = float('inf')
    start = -1
    for i in range(n, m + 1):
        if dp[i][n] != -1 and i - dp[i][n] < min_len:
            min_len = i - dp[i][n]
            start = dp[i][n]

    return "" if start == -1 else s[start:start + min_len]


if __name__ == "__main__":
    print("=== Minimum Window Subsequence ===")

    tests = [
        ("abcdebdde", "bde"),
        ("cnhczmccqouqadqtmjjzl", "cm"),
        ("a", "a"),
        ("abc", "xyz"),
    ]
    for s, t in tests:
        print(f'\nS="{s}"  T="{t}"')
        print(f'  Brute  : "{brute_force(s, t)}"')
        print(f'  Optimal: "{optimal(s, t)}"')
        print(f'  Best   : "{best(s, t)}"')
