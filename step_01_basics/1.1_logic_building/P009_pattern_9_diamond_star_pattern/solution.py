"""
Problem: Pattern 9 - Diamond Star Pattern
Difficulty: EASY | XP: 10

Print a full diamond: upright pyramid (N rows) + inverted pyramid (N rows).
Total 2*N rows. Widest row has (2*N - 1) stars.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Two Separate Pyramid Loops
# Time: O(N^2)  |  Space: O(1)
# Print upper half, then lower half, each with nested loops.
# ============================================================
def brute_force(n: int) -> None:
    # Upper half: upright pyramid
    for i in range(n):
        for _ in range(n - 1 - i):
            print(" ", end="")
        for j in range(2 * i + 1):
            if j > 0:
                print(" ", end="")
            print("*", end="")
        print()
    # Lower half: inverted pyramid
    for i in range(n):
        for _ in range(i):
            print(" ", end="")
        star_count = 2 * (n - i) - 1
        for j in range(star_count):
            if j > 0:
                print(" ", end="")
            print("*", end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Single Loop with Conditional
# Time: O(N^2)  |  Space: O(N)
# One loop over 2N rows, conditional formula for each half.
# ============================================================
def optimal(n: int) -> None:
    for i in range(2 * n):
        if i < n:
            spaces = n - 1 - i
            stars = 2 * i + 1
        else:
            j = i - n
            spaces = j
            stars = 2 * (n - j) - 1
        print(" " * spaces + " ".join(["*"] * stars))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire diamond as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = []
    for i in range(2 * n):
        if i < n:
            spaces = n - 1 - i
            stars = 2 * i + 1
        else:
            j = i - n
            spaces = j
            stars = 2 * (n - j) - 1
        rows.append(" " * spaces + " ".join(["*"] * stars))
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 9 - Diamond Star Pattern ===")

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
