"""
Problem: Pattern 16 - Alpha Ramp
Difficulty: EASY | XP: 10

Print a triangle where row i has the i-th letter repeated i times.
A
B B
C C C
D D D D
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
# Time: O(N^2)  |  Space: O(1)
# Outer loop picks the letter, inner loop repeats it.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(n):
        ch = chr(65 + i)
        for j in range(i + 1):
            if j > 0:
                print(" ", end="")
            print(ch, end="")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Repetition per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row using join and repetition.
# ============================================================
def optimal(n: int) -> None:
    for i in range(n):
        ch = chr(65 + i)
        print(" ".join([ch] * (i + 1)))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire grid as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = [" ".join([chr(65 + i)] * (i + 1)) for i in range(n)]
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 16 - Alpha Ramp ===")
    print("--- Brute Force ---")
    brute_force(N)
    print("--- Optimal ---")
    optimal(N)
    print("--- Best ---")
    best(N)
