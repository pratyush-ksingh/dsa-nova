"""
Problem: Pattern 2 - Right-Angled Triangle
Difficulty: EASY | XP: 10

Print a right-angled triangle of stars with N rows.
Row i (1-indexed) has i stars.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints i stars for row i.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        for j in range(1, i + 1):
            if j > 1:
                print(" ", end="")
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repetition per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row using join, print it.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        print(" ".join(["*"] * i))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire triangle as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" ".join(["*"] * i) for i in range(1, n + 1)]
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 2 - Right-Angled Triangle ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
