"""
Problem: Pattern 4 - Right Angled Number Triangle II
Difficulty: EASY | XP: 10

Print a right-angled triangle where row i prints the number i repeated i times.
Example (N=4):
1
2 2
3 3 3
4 4 4 4
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop for rows, inner loop prints i exactly i times.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        for j in range(1, i + 1):
            if j > 1:
                print(" ", end="")
            print(i, end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repeat per Row
# Time: O(N^2)  |  Space: O(N) for current row string
# Use string multiplication to build each row in one expression.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        print(" ".join([str(i)] * i))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire triangle as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" ".join([str(i)] * i) for i in range(1, n + 1)]
    print("\n".join(rows))


if __name__ == "__main__":
    test_cases = [1, 3, 5]
    print("=== Pattern 4 - Right Angled Number Triangle II ===")

    for n in test_cases:
        print(f"\nN = {n}")
        print("--- Brute Force ---")
        brute_force(n)
        print("--- Optimal ---")
        optimal(n)
        print("--- Best ---")
        best(n)
