"""
Problem: Pattern 12 - Number Crown Pattern
Difficulty: EASY | XP: 10

Print a number crown: increasing numbers on left, spaces in middle,
mirror-decreasing numbers on right.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Three Inner Loops per Row
# Time: O(N^2)  |  Space: O(1)
# Left numbers + spaces + right numbers using explicit loops.
# ============================================================
def brute_force(n: int) -> None:
    for i in range(1, n + 1):
        # Left side: 1 to i (each number followed by a space)
        for j in range(1, i + 1):
            print(j, end=" ")
        # Middle spaces
        for _ in range(2 * (n - i)):
            print(" ", end="")
        # Right side: i to 1
        for j in range(i, 0, -1):
            print(j, end=" " if j > 1 else "")
        print()


# ============================================================
# APPROACH 2: OPTIMAL -- String Building per Row
# Time: O(N^2)  |  Space: O(N)
# Build each row as a string with three sections.
# ============================================================
def optimal(n: int) -> None:
    for i in range(1, n + 1):
        left = " ".join(str(j) for j in range(1, i + 1))
        right = " ".join(str(j) for j in range(i, 0, -1))
        # Gap: each number takes 2 chars (digit + space), so gap width
        # is 2*(n-i) for the space-separated format
        gap = " " * (2 * (n - i) + 1) if i < n else " "
        print(left + gap + right)


# ============================================================
# APPROACH 3: BEST -- Full Grid String, Single Print
# Time: O(N^2)  |  Space: O(N^2)
# Build entire crown as one string, single I/O call.
# ============================================================
def best(n: int) -> None:
    rows = []
    for i in range(1, n + 1):
        left = ""
        for j in range(1, i + 1):
            left += str(j) + " "
        middle = " " * (2 * (n - i))
        right = ""
        for j in range(i, 0, -1):
            right += str(j)
            if j > 1:
                right += " "
        rows.append(left + middle + right)
    print("\n".join(rows))


if __name__ == "__main__":
    N = 5
    print("=== Pattern 12 - Number Crown Pattern ===")

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
