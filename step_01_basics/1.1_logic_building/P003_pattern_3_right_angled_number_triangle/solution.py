"""
Problem: Pattern 3 - Right Angled Number Triangle
Difficulty: EASY | XP: 10

Print a right-angled triangle where row i prints numbers 1 to i.
Example (N=4):
1
1 2
1 2 3
1 2 3 4
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints 1 to i for row i.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        for j in range(1, i + 1):
            if j > 1:
                print(" ", end="")
            print(j, end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- Join-Based Row Construction
# Time: O(N^2)  |  Space: O(N) for current row string
# Build each row using join and range, one print per row.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        print(" ".join(str(j) for j in range(1, i + 1)))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire triangle as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" ".join(str(j) for j in range(1, i + 1)) for i in range(1, n + 1)]
    print("\n".join(rows))


if __name__ == "__main__":
    test_cases = [1, 3, 5]
    print("=== Pattern 3 - Right Angled Number Triangle ===")

    for n in test_cases:
        print(f"\nN = {n}")
        print("--- Brute Force ---")
        brute_force(n)
        print("--- Optimal ---")
        optimal(n)
        print("--- Best ---")
        best(n)
