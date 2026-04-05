"""
Problem: Pattern 20 - Symmetric Butterfly
Difficulty: EASY | XP: 10

For n=4, print:
*      *
**    **
***  ***
********
********
***  ***
**    **
*      *
Each row has (stars)(spaces)(stars) symmetrically.
Upper half: row i (1-indexed) has i stars, (2*(n-i)) spaces, i stars.
Lower half: mirror of upper half.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Character-by-Character
# Time: O(n^2)  |  Space: O(1)
# For each row compute stars and spaces, print char by char.
# ============================================================
def brute_force(n: int) -> None:
    # Upper half (rows 1..n)
    for i in range(1, n + 1):
        stars = i
        spaces = 2 * (n - i)
        for _ in range(stars):
            print("*", end="")
        for _ in range(spaces):
            print(" ", end="")
        for _ in range(stars):
            print("*", end="")
        print()
    # Lower half (rows n..1)
    for i in range(n, 0, -1):
        stars = i
        spaces = 2 * (n - i)
        for _ in range(stars):
            print("*", end="")
        for _ in range(spaces):
            print(" ", end="")
        for _ in range(stars):
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Multiplication per Row
# Time: O(n^2)  |  Space: O(n)
# Build each row string using multiplication, then print.
# ============================================================
def optimal(n: int) -> None:
    # Upper half
    for i in range(1, n + 1):
        row = "*" * i + " " * (2 * (n - i)) + "*" * i
        print(row)
    # Lower half
    for i in range(n, 0, -1):
        row = "*" * i + " " * (2 * (n - i)) + "*" * i
        print(row)


# ============================================================
# APPROACH 3: BEST -- Full Grid Built then Single Print
# Time: O(n^2)  |  Space: O(n^2)
# Collect all rows into a list and join for one I/O call.
# ============================================================
def best(n: int) -> None:
    rows = []
    # Upper half
    for i in range(1, n + 1):
        rows.append("*" * i + " " * (2 * (n - i)) + "*" * i)
    # Lower half
    for i in range(n, 0, -1):
        rows.append("*" * i + " " * (2 * (n - i)) + "*" * i)
    print("\n".join(rows))


if __name__ == "__main__":
    N = 4
    print("=== Pattern 20 - Symmetric Butterfly ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
