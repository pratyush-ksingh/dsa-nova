"""
Problem: Pattern 10 - Half Diamond Star
Difficulty: EASY | XP: 10

Print a left-aligned half diamond: stars increase from 1 to N,
then decrease from N-1 back to 1. Total (2*N - 1) rows.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Two Separate Loops
# Time: O(N^2)  |  Space: O(1)
# First loop: increasing 1..N. Second loop: decreasing N-1..1.
# ============================================================
def brute_force(n: int) -> None:
    # Increasing half
    for i in range(1, n + 1):
        for j in range(i):
            if j > 0:
                print(" ", end="")
            print("*", end="")
        print()
    # Decreasing half
    for i in range(n - 1, 0, -1):
        for j in range(i):
            if j > 0:
                print(" ", end="")
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Single Loop with abs() Formula
# Time: O(N^2)  |  Space: O(N)
# stars = N - |N - 1 - i| for i in [0, 2N-2].
# ============================================================
def optimal(n: int) -> None:
    total_rows = 2 * n - 1
    for i in range(total_rows):
        stars = n - abs(n - 1 - i)
        print(" ".join(["*"] * stars))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire half diamond as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    total_rows = 2 * n - 1
    rows = []
    for i in range(total_rows):
        stars = n - abs(n - 1 - i)
        rows.append(" ".join(["*"] * stars))
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 10 - Half Diamond Star ===")

    print("--- Brute Force (N=5) ---")
    brute_force(N)

    print("--- Optimal (N=5) ---")
    optimal(N)

    print("--- Best (N=5) ---")
    best(N)

    # Edge cases
    print("--- N=1 ---")
    best(1)

    print("--- N=3 ---")
    best(3)
