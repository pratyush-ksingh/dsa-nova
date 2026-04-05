"""
Problem: Add Binary Strings
Difficulty: EASY | XP: 10
Source: InterviewBit | LeetCode #67

Key Insight: Right-to-left addition with carry, just like paper math.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Integer Conversion
# Time: O(n)  |  Space: O(n)
#
# Python handles arbitrarily large ints, so no overflow here.
# But this "cheats" -- interviewers want the manual approach.
# ============================================================
def brute_force(a: str, b: str) -> str:
    return bin(int(a, 2) + int(b, 2))[2:]  # [2:] strips "0b" prefix


# ============================================================
# APPROACH 2: OPTIMAL -- Right-to-Left with Carry
# Time: O(max(m, n))  |  Space: O(max(m, n))
#
# Process from right, add digits + carry, build result reversed.
# ============================================================
def optimal(a: str, b: str) -> str:
    result = []
    i, j = len(a) - 1, len(b) - 1
    carry = 0

    while i >= 0 or j >= 0 or carry:
        digit_a = int(a[i]) if i >= 0 else 0
        digit_b = int(b[j]) if j >= 0 else 0

        total = digit_a + digit_b + carry
        result.append(str(total % 2))
        carry = total // 2

        i -= 1
        j -= 1

    return "".join(reversed(result))


# ============================================================
# APPROACH 3: BEST -- Bitwise Carry Computation
# Time: O(max(m, n))  |  Space: O(max(m, n))
#
# Uses XOR for sum bit and AND/OR for carry -- mirrors hardware.
# ============================================================
def best(a: str, b: str) -> str:
    result = []
    i, j = len(a) - 1, len(b) - 1
    carry = 0

    while i >= 0 or j >= 0 or carry:
        x = int(a[i]) if i >= 0 else 0
        y = int(b[j]) if j >= 0 else 0

        sum_bit = x ^ y ^ carry
        carry = (x & y) | (x & carry) | (y & carry)
        result.append(str(sum_bit))

        i -= 1
        j -= 1

    return "".join(reversed(result))


if __name__ == "__main__":
    print("=== Add Binary Strings ===")
    tests = [("11", "1"), ("1010", "1011"), ("0", "0"), ("1", "1")]
    for a, b in tests:
        print(f'a="{a}", b="{b}"')
        print(f"  Brute:   {brute_force(a, b)}")
        print(f"  Optimal: {optimal(a, b)}")
        print(f"  Best:    {best(a, b)}")
        print()
