"""
Problem: Pattern 11 - Binary Number Triangle
Difficulty: EASY | XP: 10

Print a triangle of N rows with alternating 1s and 0s.
Odd rows start with 1, even rows start with 0.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Toggle Variable per Row
# Time: O(N^2)  |  Space: O(1)
# Set start value based on row parity, toggle after each print.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        val = 1 if i % 2 == 1 else 0
        parts = []
        for j in range(i):
            parts.append(str(val))
            val = 1 - val
        print(" ".join(parts))


# ============================================================
# APPROACH 2: OPTIMAL -- Direct Formula (i+j) % 2
# Time: O(N^2)  |  Space: O(1)
# Value at (i, j) = (i + j) % 2 (0-indexed).
# ============================================================
def optimal(n: int) -> None:
    for i in range(n):
        row = " ".join(str((i + j) % 2) for j in range(i + 1))
        print(row)


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire triangle as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = []
    for i in range(n):
        rows.append(" ".join(str((i + j) % 2) for j in range(i + 1)))
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 11 - Binary Number Triangle ===")

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
