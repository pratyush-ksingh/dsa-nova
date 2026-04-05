"""
Problem: Excel Column Number
Difficulty: EASY | XP: 10
Source: InterviewBit

Convert an Excel column title (e.g. "A", "AB", "ZY") to its corresponding
column number. Treats the title as a base-26 number where A=1, B=2, ..., Z=26.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Right-to-Left Positional Value
# Time: O(n)  |  Space: O(1)
# Treat each character as a digit in base-26, multiply by 26^position.
# ============================================================
def brute_force(s: str) -> int:
    result = 0
    n = len(s)
    for i, ch in enumerate(s):
        value = ord(ch) - ord('A') + 1          # A=1, B=2, ..., Z=26
        power = n - 1 - i                        # rightmost position = 0
        result += value * (26 ** power)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Left-to-Right Horner's Method
# Time: O(n)  |  Space: O(1)
# Accumulate: result = result * 26 + digit_value  (Horner's evaluation)
# Avoids computing powers explicitly; cleaner and slightly faster.
# ============================================================
def optimal(s: str) -> int:
    result = 0
    for ch in s:
        result = result * 26 + (ord(ch) - ord('A') + 1)
    return result


# ============================================================
# APPROACH 3: BEST -- Same as Optimal (already O(n), O(1))
# Time: O(n)  |  Space: O(1)
# Identical algorithm written as a reduce for conciseness.
# ============================================================
def best(s: str) -> int:
    from functools import reduce
    return reduce(lambda acc, ch: acc * 26 + (ord(ch) - ord('A') + 1), s, 0)


if __name__ == "__main__":
    test_cases = [("A", 1), ("Z", 26), ("AA", 27), ("AB", 28), ("AZ", 52), ("ZY", 701)]
    print("=== Excel Column Number ===")
    for title, expected in test_cases:
        b = brute_force(title)
        o = optimal(title)
        bst = best(title)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  '{title}' -> Brute={b}  Optimal={o}  Best={bst}  Expected={expected}  [{status}]")
