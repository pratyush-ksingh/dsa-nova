"""
Problem: Excel Column Title
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a positive integer n, return the corresponding Excel column title.
1 -> "A", 26 -> "Z", 27 -> "AA", 28 -> "AB", 701 -> "ZY"
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(log26 n)  |  Space: O(log26 n)
# Iterative with string concatenation (same algorithm, less clean)
# ============================================================
def brute_force(n: int) -> str:
    """
    Build result character by character using modulo arithmetic.
    Prepend each character to a result string.
    Key insight: treat as base-26 but 1-indexed, so subtract 1 before modding.
    """
    result = ""
    while n > 0:
        n -= 1                          # shift to 0-indexed: A=0, B=1, ..., Z=25
        remainder = n % 26
        result = chr(ord('A') + remainder) + result  # prepend character
        n //= 26
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log26 n)  |  Space: O(log26 n)
# Collect chars in list then join (avoids repeated string concatenation)
# ============================================================
def optimal(n: int) -> str:
    """
    Collect characters in a list, then reverse and join.
    Appending to a list is O(1) amortized vs O(k) for string prepend.
    """
    chars = []
    while n > 0:
        n -= 1                          # make 0-indexed
        chars.append(chr(ord('A') + n % 26))
        n //= 26
    return ''.join(reversed(chars))


# ============================================================
# APPROACH 3: BEST
# Time: O(log26 n)  |  Space: O(log26 n)
# Recursive version — elegant and interview-friendly
# ============================================================
def best(n: int) -> str:
    """
    Recursive: base case n==0 returns empty string.
    Each call contributes the last character and recurses on the quotient.
    """
    if n == 0:
        return ""
    n -= 1                              # shift to 0-indexed
    return best(n // 26) + chr(ord('A') + n % 26)


if __name__ == "__main__":
    print("=== Excel Column Title ===")
    test_cases = [(1, "A"), (26, "Z"), (27, "AA"), (28, "AB"), (701, "ZY"), (702, "ZZ"), (703, "AAA")]
    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        be = best(n)
        status = "OK" if b == o == be == expected else "FAIL"
        print(f"n={n:4d} | Brute: {b:5s} | Optimal: {o:5s} | Best: {be:5s} | Expected: {expected:5s} | {status}")
