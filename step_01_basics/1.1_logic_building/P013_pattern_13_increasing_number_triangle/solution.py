"""
Problem: Pattern 13 - Increasing Number Triangle (Floyd's Triangle)
Difficulty: EASY | XP: 10

Print triangle with continuously increasing numbers across rows.
Row 1: 1, Row 2: 2 3, Row 3: 4 5 6, ...
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Counter Variable
# Time: O(N^2)  |  Space: O(1)
# Persistent counter incremented across all rows.
# ============================================================
def brute_force(n: int) -> None:
    counter = 1
    for i in range(1, n + 1):
        parts = []
        for _ in range(i):
            parts.append(str(counter))
            counter += 1
        print(" ".join(parts))


# ============================================================
# APPROACH 2: OPTIMAL -- Direct Formula per Row
# Time: O(N^2)  |  Space: O(1)
# Row i starts at i*(i-1)//2 + 1 (triangular number formula).
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        start = i * (i - 1) // 2 + 1
        print(" ".join(str(start + j) for j in range(i)))


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire triangle as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = []
    counter = 1
    for i in range(1, n + 1):
        parts = []
        for _ in range(i):
            parts.append(str(counter))
            counter += 1
        rows.append(" ".join(parts))
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 13 - Increasing Number Triangle ===")

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
