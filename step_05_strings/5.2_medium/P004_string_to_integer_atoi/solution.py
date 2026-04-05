"""
Problem: String to Integer Atoi (LeetCode #8)
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional
import re

INT_MAX = 2**31 - 1   # 2147483647
INT_MIN = -(2**31)     # -2147483648


# ============================================================
# APPROACH 1: BRUTE FORCE -- Strip + Parse with Many Checks
# Time: O(n)  |  Space: O(1)
#
# Use lstrip for whitespace, then manually check sign and
# parse digit by digit with explicit overflow checks.
# ============================================================
def brute_force(s: str) -> int:
    s = s.lstrip()
    if not s:
        return 0

    sign = 1
    i = 0

    if s[i] == '-':
        sign = -1
        i += 1
    elif s[i] == '+':
        i += 1

    result = 0
    while i < len(s) and s[i].isdigit():
        digit = int(s[i])

        # Overflow check before accumulation
        if result > (INT_MAX - digit) // 10:
            return INT_MAX if sign == 1 else INT_MIN

        result = result * 10 + digit
        i += 1

    return sign * result


# ============================================================
# APPROACH 2: OPTIMAL -- Clean Single Pass with Overflow Guard
# Time: O(n)  |  Space: O(1)
#
# Three phases in one pass: skip whitespace, read sign, read
# digits. Pre-emptive overflow check before each accumulation.
# ============================================================
def optimal(s: str) -> int:
    n = len(s)
    i = 0

    # Phase 1: Skip whitespace
    while i < n and s[i] == ' ':
        i += 1

    if i == n:
        return 0

    # Phase 2: Read sign
    sign = 1
    if s[i] == '-':
        sign = -1
        i += 1
    elif s[i] == '+':
        i += 1

    # Phase 3: Read digits
    result = 0
    while i < n and s[i].isdigit():
        digit = ord(s[i]) - ord('0')

        # Pre-emptive overflow check
        if result > (INT_MAX - digit) // 10:
            return INT_MAX if sign == 1 else INT_MIN

        result = result * 10 + digit
        i += 1

    return sign * result


# ============================================================
# APPROACH 3: BEST -- Regex One-Liner Style
# Time: O(n)  |  Space: O(1)
#
# Use regex to extract the valid numeric prefix, convert,
# and clamp to 32-bit signed range.
# ============================================================
def best(s: str) -> int:
    match = re.match(r'^\s*([+-]?\d+)', s)
    if not match:
        return 0
    num = int(match.group(1))
    return max(INT_MIN, min(INT_MAX, num))


if __name__ == "__main__":
    print("=== String to Integer Atoi ===\n")

    tests = [
        ("42", 42),
        ("   -42", -42),
        ("4193 with words", 4193),
        ("words and 987", 0),
        ("-91283472332", -2147483648),
        ("", 0),
        ("   ", 0),
        ("+-12", 0),
        ("+1", 1),
        ("2147483648", 2147483647),
        ("  0000042", 42),
    ]

    for s, expected in tests:
        print(f"Input:    \"{s}\"")
        print(f"Expected: {expected}")
        print(f"Brute:    {brute_force(s)}")
        print(f"Optimal:  {optimal(s)}")
        print(f"Best:     {best(s)}")
        print()
