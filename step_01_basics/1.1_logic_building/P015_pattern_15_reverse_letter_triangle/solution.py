"""
Problem: Pattern 15 - Reverse Letter Triangle
Difficulty: EASY | XP: 10

Print an inverted triangle where each row starts at A and shrinks.
A B C D E
A B C D
A B C
A B
A
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints A..(A + N-i-1).
# ============================================================
def brute_force(n: int) -> None:
    for i in range(n):
        for j in range(n - i):
            if j > 0:
                print(" ", end="")
            print(chr(65 + j), end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Substring per Row
# Time: O(N^2)  |  Space: O(N)
# Precompute full row, print shrinking slices.
# ============================================================
def optimal(n: int) -> None:
    full = " ".join(chr(65 + j) for j in range(n))
    for i in range(n):
        length = 2 * (n - i) - 1
        print(full[:length])


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire grid as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    full = " ".join(chr(65 + j) for j in range(n))
    rows = [full[:2 * (n - i) - 1] for i in range(n)]
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 15 - Reverse Letter Triangle ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
