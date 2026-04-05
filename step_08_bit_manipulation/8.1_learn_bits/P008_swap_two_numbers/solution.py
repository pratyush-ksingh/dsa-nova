"""
Problem: Swap Two Numbers (Without Temp Variable)
Difficulty: EASY | XP: 10

Swap two numbers without using a temporary variable.
"""
from typing import Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE (Using Temp Variable)
# Time: O(1)  |  Space: O(1)
# ============================================================
def brute_force(a: int, b: int) -> Tuple[int, int]:
    """Classic swap using a temporary variable."""
    temp = a
    a = b
    b = temp
    return a, b


# ============================================================
# APPROACH 2: OPTIMAL (XOR Swap)
# Time: O(1)  |  Space: O(1) -- no extra variable
# ============================================================
def optimal(a: int, b: int) -> Tuple[int, int]:
    """XOR swap trick: a^=b; b^=a; a^=b."""
    a = a ^ b  # a now holds a XOR b
    b = a ^ b  # b = (a XOR b) XOR b = a (original)
    a = a ^ b  # a = (a XOR b) XOR a = b (original)
    return a, b


# ============================================================
# APPROACH 3: BEST (Pythonic Tuple Swap)
# Time: O(1)  |  Space: O(1)
# ============================================================
def best(a: int, b: int) -> Tuple[int, int]:
    """Python one-liner using tuple unpacking."""
    a, b = b, a
    return a, b


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Swap Two Numbers ===\n")

    test_cases = [
        (5, 10),
        (0, 7),
        (-3, 4),
        (100, 100),
        (0, 0),
        (-5, -8),
        (2147483647, -2147483648),
    ]

    for a, b in test_cases:
        b1 = brute_force(a, b)
        o1 = optimal(a, b)
        r1 = best(a, b)
        expected = (b, a)
        status = "PASS" if b1 == expected and o1 == expected and r1 == expected else "FAIL"
        print(f"Input: a={a}, b={b}")
        print(f"  Brute:    {b1}")
        print(f"  Optimal:  {o1}")
        print(f"  Best:     {r1}")
        print(f"  Expected: {expected}  [{status}]\n")
