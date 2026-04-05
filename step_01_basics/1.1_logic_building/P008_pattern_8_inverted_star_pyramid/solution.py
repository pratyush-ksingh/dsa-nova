"""
Problem: Pattern 8 - Inverted Star Pyramid
Difficulty: EASY | XP: 10

Print an inverted centered star pyramid with N rows.
Row 0 (top) has (2*N - 1) stars, row N-1 (bottom) has 1 star.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Three loops per row: spaces, stars, newline.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(n):
        # Print leading spaces
        for _ in range(i):
            print(" ", end="")
        # Print stars with spaces between
        star_count = 2 * (n - i) - 1
        for j in range(star_count):
            if j > 0:
                print(" ", end="")
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repetition per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row using string multiplication and join.
# ============================================================
def optimal(n: int) -> None:
    for i in range(n):
        spaces = " " * i
        stars = " ".join(["*"] * (2 * (n - i) - 1))
        print(spaces + stars)


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire pattern as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = []
    for i in range(n):
        spaces = " " * i
        stars = " ".join(["*"] * (2 * (n - i) - 1))
        rows.append(spaces + stars)
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 8 - Inverted Star Pyramid ===")

    print("--- Brute Force (N=5) ---")
    brute_force(N)

    print("--- Optimal (N=5) ---")
    optimal(N)

    print("--- Best (N=5) ---")
    best(N)

    # Edge case tests
    print("--- N=1 ---")
    brute_force(1)

    print("--- N=3 ---")
    best(3)
