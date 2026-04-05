"""
Problem: Pattern 1 - Rectangular Star
Difficulty: EASY | XP: 10

Print a rectangle of stars with N rows and M columns.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N*M)  |  Space: O(1)
# Two nested for-loops: outer for rows, inner for columns.
# ============================================================
def brute_force(n: int, m: int) -> None:
    for i in range(n):
        for j in range(m):
            if j > 0:
                print(" ", end="")
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repetition per Row
# Time: O(N*M)  |  Space: O(M)
# Build one row string using join, print it N times.
# ============================================================
def optimal(n: int, m: int) -> None:
    row = " ".join(["*"] * m)
    for _ in range(n):
        print(row)


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N*M)  |  Space: O(N*M)
# Build entire grid as one string, single I/O call.
# ============================================================
def best(n: int, m: int) -> None:
    row = " ".join(["*"] * m)
    grid = "\n".join([row] * n)
    print(grid)


if __name__ == "__main__":
    N, M = 4, 5
    print("=== Pattern 1 - Rectangular Star ===")
    print("--- Brute Force ---")
    brute_force(N, M)
    print("--- Optimal ---")
    optimal(N, M)
    print("--- Best ---")
    best(N, M)
