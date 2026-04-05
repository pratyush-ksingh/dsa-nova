"""
Problem: Pattern 22 - The Number Pattern
Difficulty: MEDIUM | XP: 15

For n=4, print a (2n-1)x(2n-1) grid where each cell value equals
n - min(distance to top, bottom, left, right border).

Example (n=4):
4444444
4333334
4322234
4321234
4322234
4333334
4444444
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Per-Cell Distance Computation
# Time: O(n^2)  |  Space: O(1)
# For each cell, compute min of 4 border distances explicitly.
# ============================================================
def brute_force(n: int) -> None:
    size = 2 * n - 1
    for i in range(size):
        for j in range(size):
            dist_top    = i
            dist_bottom = size - 1 - i
            dist_left   = j
            dist_right  = size - 1 - j
            val = n - min(dist_top, dist_bottom, dist_left, dist_right)
            print(val, end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Same Formula, String-Per-Row
# Time: O(n^2)  |  Space: O(n)
# Identical logic but collects digits into a list per row.
# ============================================================
def optimal(n: int) -> None:
    size = 2 * n - 1
    for i in range(size):
        row = []
        for j in range(size):
            row.append(str(n - min(i, j, size - 1 - i, size - 1 - j)))
        print("".join(row))


# ============================================================
# APPROACH 3: BEST -- Full Grid Built then Single Print
# Time: O(n^2)  |  Space: O(n^2)
# Build all rows, join and print in one I/O call.
# ============================================================
def best(n: int) -> None:
    size = 2 * n - 1
    rows = []
    for i in range(size):
        row = []
        for j in range(size):
            row.append(str(n - min(i, j, size - 1 - i, size - 1 - j)))
        rows.append("".join(row))
    print("\n".join(rows))


if __name__ == "__main__":
    N = 4
    print("=== Pattern 22 - The Number Pattern ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
