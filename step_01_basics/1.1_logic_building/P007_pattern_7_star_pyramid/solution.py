"""
Problem: Pattern 7 - Star Pyramid
Difficulty: EASY | XP: 10

Print a centered star pyramid with N rows.
Row i has (N-i) leading spaces and (2*i-1) stars.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# One loop for spaces, one loop for stars per row.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        for _ in range(n - i):
            print(" ", end="")
        for _ in range(2 * i - 1):
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repetition per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row using string multiplication.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        print(" " * (n - i) + "*" * (2 * i - 1))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire pyramid as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" " * (n - i) + "*" * (2 * i - 1) for i in range(1, n + 1)]
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
