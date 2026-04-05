"""
Problem: Pattern 19 - Symmetric Void Pattern
Difficulty: EASY | XP: 10

Print symmetric void (hourglass of spaces between stars). For n=5:
**********
****  ****
***    ***
**      **
*        *
*        *
**      **
***    ***
****  ****
**********
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Separate Top and Bottom Halves
# Time: O(N^2)  |  Space: O(1)
# Print upper half (n rows) then lower half (n rows) with
# three inner loops each: left stars, spaces, right stars.
# ============================================================
def brute_force(n: int) -> None:
    # Upper half (rows 0..n-1)
    for i in range(n):
        # left stars: n - i
        for j in range(n - i):
            print("*", end="")
        # middle spaces: 2 * i
        for j in range(2 * i):
            print(" ", end="")
        # right stars: n - i
        for j in range(n - i):
            print("*", end="")
        print()
    # Lower half (rows 0..n-1), mirror of upper
    for i in range(n):
        # left stars: i + 1
        for j in range(i + 1):
            print("*", end="")
        # middle spaces: 2 * (n - 1 - i)
        for j in range(2 * (n - 1 - i)):
            print(" ", end="")
        # right stars: i + 1
        for j in range(i + 1):
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Unified Loop with Conditional
# Time: O(N^2)  |  Space: O(1)
# Single loop of 2*n rows. Compute stars and spaces from row index.
# ============================================================
def optimal(n: int) -> None:
    total_rows = 2 * n
    for i in range(total_rows):
        if i < n:
            stars = n - i
            spaces = 2 * i
        else:
            stars = i - n + 1
            spaces = 2 * (2 * n - 1 - i)
        print("*" * stars + " " * spaces + "*" * stars)


# ============================================================
# APPROACH 3: BEST -- String Multiplication, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build all rows as strings, join, single print.
# ============================================================
def best(n: int) -> None:
    lines = []
    for i in range(2 * n):
        if i < n:
            stars = n - i
            spaces = 2 * i
        else:
            stars = i - n + 1
            spaces = 2 * (2 * n - 1 - i)
        lines.append("*" * stars + " " * spaces + "*" * stars)
    print("\n".join(lines))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 19 - Symmetric Void Pattern ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
