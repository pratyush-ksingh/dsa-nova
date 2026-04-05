"""
Problem: Pattern 18 - Alpha Triangle
Difficulty: EASY | XP: 10

Print reverse alpha triangle. For n=5 (E):
E
DE
CDE
BCDE
ABCDE
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# For each row i, determine start character and print ascending.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(n):
        # Row i starts at chr('A' + n - 1 - i) and goes up to chr('A' + n - 1)
        start = n - 1 - i  # 0-indexed offset from 'A'
        for j in range(i + 1):
            print(chr(ord('A') + start + j), end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Direct Character Range
# Time: O(N^2)  |  Space: O(1)
# Row i prints characters from (E-i) to E where E = chr('A'+n-1).
# Use a single inner loop with clear bounds.
# ============================================================
def optimal(n: int) -> None:
    end_char = ord('A') + n - 1  # e.g., 'E' for n=5
    for i in range(n):
        start_char = end_char - i
        for ch in range(start_char, end_char + 1):
            print(chr(ch), end="")
        print()


# ============================================================
# APPROACH 3: BEST -- String Slicing, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Pre-build the full alphabet string, slice for each row.
# ============================================================
def best(n: int) -> None:
    alpha = "".join(chr(ord('A') + i) for i in range(n))
    # Row i uses alpha[n-1-i : n]
    lines = [alpha[n - 1 - i:] for i in range(n)]
    print("\n".join(lines))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 18 - Alpha Triangle ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
