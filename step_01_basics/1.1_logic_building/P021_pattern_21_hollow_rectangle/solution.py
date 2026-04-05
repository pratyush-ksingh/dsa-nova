"""
Problem: Pattern 21 - Hollow Rectangle
Difficulty: EASY | XP: 10

For n=4, m=5:
*****
*   *
*   *
*****
Stars on border cells (first/last row, first/last column); spaces inside.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Cell-by-Cell Check
# Time: O(n*m)  |  Space: O(1)
# For every cell decide star or space using boundary conditions.
# ============================================================
def brute_force(n: int, m: int) -> None:
    for i in range(n):
        for j in range(m):
            if i == 0 or i == n - 1 or j == 0 or j == m - 1:
                print("*", end="")
            else:
                print(" ", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Pre-built Row Strings
# Time: O(n*m)  |  Space: O(m)
# First and last rows are solid; middle rows are star+spaces+star.
# ============================================================
def optimal(n: int, m: int) -> None:
    border_row = "*" * m
    middle_row = "*" + " " * (m - 2) + "*"
    for i in range(n):
        if i == 0 or i == n - 1:
            print(border_row)
        else:
            print(middle_row)


# ============================================================
# APPROACH 3: BEST -- Full Grid Built then Single Print
# Time: O(n*m)  |  Space: O(n*m)
# Collect all row strings then join and print once.
# ============================================================
def best(n: int, m: int) -> None:
    border_row = "*" * m
    middle_row = "*" + " " * (m - 2) + "*"
    rows = []
    for i in range(n):
        rows.append(border_row if (i == 0 or i == n - 1) else middle_row)
    print("\n".join(rows))


if __name__ == "__main__":
    N, M = 4, 5
    print("=== Pattern 21 - Hollow Rectangle ===")
    print("--- Brute Force ---")
    brute_force(N, M)
    print("--- Optimal ---")
    optimal(N, M)
    print("--- Best ---")
    best(N, M)
