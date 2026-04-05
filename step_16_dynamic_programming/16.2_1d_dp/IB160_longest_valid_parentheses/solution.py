"""
Problem: Longest Valid Parentheses
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a string of '(' and ')', find the length of the longest
valid (well-formed) parentheses substring.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Check every even-length substring
# Time: O(n^3)  |  Space: O(1)
# ============================================================
def brute_force(s: str) -> int:
    def is_valid(lo, hi):
        cnt = 0
        for i in range(lo, hi + 1):
            if s[i] == '(':
                cnt += 1
            else:
                cnt -= 1
            if cnt < 0:
                return False
        return cnt == 0

    n = len(s)
    max_len = 0
    for i in range(n):
        for j in range(i + 1, n, 2):  # only even-length substrings
            if is_valid(i, j):
                max_len = max(max_len, j - i + 1)
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL - Stack-based O(n)
# Time: O(n)  |  Space: O(n)
# ============================================================
# Use a stack with a sentinel at -1. Push '(' indices.
# For ')', pop:
#   - Stack empty after pop: push current index as new base.
#   - Otherwise: length = current index - stack top.
# ============================================================
def optimal(s: str) -> int:
    stack = [-1]  # sentinel
    max_len = 0

    for i, ch in enumerate(s):
        if ch == '(':
            stack.append(i)
        else:
            stack.pop()
            if not stack:
                stack.append(i)
            else:
                max_len = max(max_len, i - stack[-1])
    return max_len


# ============================================================
# APPROACH 3: BEST - Two-pass left-right counters (O(1) space)
# Time: O(n)  |  Space: O(1)
# ============================================================
# Left-to-right: count ( and ). Equal => valid length. close>open => reset.
# Right-to-left: handles cases like "((()" where L-R misses.
# Together they cover all cases in two linear passes.
# Real-life use: real-time syntax checking in code editors,
# streaming HTML/XML tag balance validation.
# ============================================================
def best(s: str) -> int:
    max_len = open_cnt = close_cnt = 0

    for ch in s:
        if ch == '(':
            open_cnt += 1
        else:
            close_cnt += 1
        if open_cnt == close_cnt:
            max_len = max(max_len, 2 * close_cnt)
        elif close_cnt > open_cnt:
            open_cnt = close_cnt = 0

    open_cnt = close_cnt = 0
    for ch in reversed(s):
        if ch == '(':
            open_cnt += 1
        else:
            close_cnt += 1
        if open_cnt == close_cnt:
            max_len = max(max_len, 2 * open_cnt)
        elif open_cnt > close_cnt:
            open_cnt = close_cnt = 0

    return max_len


if __name__ == "__main__":
    print("=== Longest Valid Parentheses ===")

    tests   = ["(()",  ")()())", "",  "()", "(()(",  "))(",  "(()((()("]
    expects = [2,      4,        0,   2,    2,       0,      2]

    for s, exp in zip(tests, expects):
        b, op, bst = brute_force(s), optimal(s), best(s)
        ok = b == op == bst == exp
        print(f"s={s!r:15} expect={exp}  b={b} op={op} bst={bst}  {'OK' if ok else 'FAIL'}")
