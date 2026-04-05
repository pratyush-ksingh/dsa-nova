"""
Problem: Pattern 14 - Increasing Letter Triangle
Difficulty: EASY | XP: 10

Print a triangle where row i contains letters A through the i-th letter.
A
A B
A B C
A B C D
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints letters A..(A+i).
# ============================================================
def brute_force(n: int) -> None:
    for i in range(n):
        for j in range(i + 1):
            if j > 0:
                print(" ", end="")
            print(chr(65 + j), end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Incremental String Building per Row
# Time: O(N^2)  |  Space: O(N)
# Grow a row string by appending the next letter each iteration.
# ============================================================
def optimal(n: int) -> None:
    row = ""
    for i in range(n):
        if i > 0:
            row += " "
        row += chr(65 + i)
        print(row)


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire grid as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    full = " ".join(chr(65 + j) for j in range(n))
    rows = [full[:2 * i + 1] for i in range(n)]
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 14 - Increasing Letter Triangle ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
