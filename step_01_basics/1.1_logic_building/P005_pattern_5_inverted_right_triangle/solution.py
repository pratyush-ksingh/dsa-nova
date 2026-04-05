"""
Problem: Pattern 5 - Inverted Right Triangle
Difficulty: EASY | XP: 10

Print an inverted right-angled triangle of stars.
Row i (1-indexed) has (N - i + 1) stars.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints decreasing stars.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        for j in range(1, n - i + 2):
            if j > 1:
                print(" ", end="")
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repetition per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row string using join, print row by row.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        stars = n - i + 1
        print(" ".join(["*"] * stars))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire triangle as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" ".join(["*"] * (n - i)) for i in range(n)]
    print("\n".join(rows))


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    for n in [1, 3, 5]:
        print(f"=== N = {n} ===")

        print("--- Brute Force ---")
        brute_force(n)

        print("--- Optimal ---")
        optimal(n)

        print("--- Best ---")
        best(n)

        print()
