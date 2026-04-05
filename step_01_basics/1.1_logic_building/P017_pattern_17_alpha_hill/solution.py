"""
Problem: Pattern 17 - Alpha Hill
Difficulty: EASY | XP: 10

Print an alpha hill pattern. For n=3:
  A
 ABA
ABCBA
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Use three inner loops per row: spaces, ascending chars, descending chars.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(n):
        # leading spaces
        for j in range(n - i - 1):
            print(" ", end="")
        # ascending characters A -> chr('A' + i)
        for j in range(i + 1):
            print(chr(ord('A') + j), end="")
        # descending characters chr('A' + i - 1) -> A
        for j in range(i - 1, -1, -1):
            print(chr(ord('A') + j), end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Symmetry with Single Inner Loop
# Time: O(N^2)  |  Space: O(1)
# Each row has (2*i+1) character positions. Use distance from center
# to determine the character: center is row i, distance d gives chr('A'+i-d).
# ============================================================
def optimal(n: int) -> None:
    for i in range(n):
        # leading spaces
        print(" " * (n - i - 1), end="")
        # 2*i + 1 characters in this row
        for j in range(2 * i + 1):
            # distance from center of this row
            dist = abs(i - j)
            print(chr(ord('A') + i - dist), end="")
        print()


# ============================================================
# APPROACH 3: BEST -- String Building with center()
# Time: O(N^2)  |  Space: O(N) per row
# Build each row as a string and center it in width 2*n-1.
# ============================================================
def best(n: int) -> None:
    for i in range(n):
        # ascending: A B C ... up to chr('A'+i)
        left = "".join(chr(ord('A') + j) for j in range(i + 1))
        # mirror: drop last char of left, reverse
        right = left[-2::-1]
        row = left + right
        print(row.center(2 * n - 1))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 17 - Alpha Hill ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
