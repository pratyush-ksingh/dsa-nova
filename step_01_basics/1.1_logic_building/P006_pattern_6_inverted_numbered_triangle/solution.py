"""
Problem: Pattern 6 - Inverted Numbered Triangle
Difficulty: EASY | XP: 10

Print an inverted triangle where row i has numbers 1 to (N-i+1).
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints 1..(N-i+1).
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        for j in range(1, n - i + 2):
            if j > 1:
                print(" ", end="")
            print(j, end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Range-Based String per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row with join, print row by row.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        count = n - i + 1
        print(" ".join(str(j) for j in range(1, count + 1)))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire grid as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" ".join(str(j) for j in range(1, n - i + 1)) for i in range(n)]
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
