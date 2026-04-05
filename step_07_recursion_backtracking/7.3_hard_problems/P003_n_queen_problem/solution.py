"""
Problem: N Queen Problem
Difficulty: HARD | XP: 50

Place N queens on an N×N board so no two attack each other.
Return all distinct board configurations.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Backtracking with O(row) safety check
# Time: O(N! * N)  |  Space: O(N^2)
# ============================================================
def brute_force(n: int) -> List[List[str]]:
    """Place queens row by row, check all previous rows for conflicts."""
    results = []
    queens = []  # queens[r] = column of queen in row r

    def is_safe(row: int, col: int) -> bool:
        for r, c in enumerate(queens):
            if c == col or abs(c - col) == abs(r - row):
                return False
        return True

    def backtrack(row: int):
        if row == n:
            board = ['.' * c + 'Q' + '.' * (n - c - 1) for c in queens]
            results.append(board)
            return
        for col in range(n):
            if is_safe(row, col):
                queens.append(col)
                backtrack(row + 1)
                queens.pop()

    backtrack(0)
    return results


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking with O(1) safety via sets
# Time: O(N!)  |  Space: O(N)
# Track used columns and diagonals in sets for O(1) lookup.
# ============================================================
def optimal(n: int) -> List[List[str]]:
    """Use sets for O(1) conflict checking."""
    results = []
    cols_used = set()
    diag1 = set()   # row - col
    diag2 = set()   # row + col
    queens = []

    def backtrack(row: int):
        if row == n:
            board = ['.' * c + 'Q' + '.' * (n - c - 1) for c in queens]
            results.append(board)
            return
        for col in range(n):
            if col in cols_used or (row - col) in diag1 or (row + col) in diag2:
                continue
            queens.append(col)
            cols_used.add(col)
            diag1.add(row - col)
            diag2.add(row + col)
            backtrack(row + 1)
            queens.pop()
            cols_used.remove(col)
            diag1.remove(row - col)
            diag2.remove(row + col)

    backtrack(0)
    return results


# ============================================================
# APPROACH 3: BEST - Bitmask backtracking (fastest in practice)
# Time: O(N!)  |  Space: O(N)
# Integer bitmasks for cols, left-diag, right-diag. Iterate set bits.
# ============================================================
def best(n: int) -> List[List[str]]:
    """
    Bitmask: available = all_bits & ~(cols | ld | rd)
    For each set bit (= valid column), place queen and recurse.
    Shift diagonals left/right for next row.
    """
    results = []
    all_bits = (1 << n) - 1
    queens = [0] * n

    def backtrack(row: int, cols: int, ld: int, rd: int):
        if row == n:
            board = ['.' * c + 'Q' + '.' * (n - c - 1) for c in queens]
            results.append(board)
            return
        available = all_bits & ~(cols | ld | rd)
        while available:
            bit = available & (-available)  # lowest set bit
            col = bit.bit_length() - 1
            queens[row] = col
            backtrack(row + 1,
                      cols | bit,
                      (ld | bit) << 1,
                      (rd | bit) >> 1)
            available &= available - 1   # clear lowest set bit

    backtrack(0, 0, 0, 0)
    return results


if __name__ == "__main__":
    print("=== N Queen Problem ===")
    for n in range(1, 7):
        b = len(brute_force(n))
        o = len(optimal(n))
        be = len(best(n))
        print(f"n={n}: brute={b} optimal={o} best={be}")

    print("\nSolutions for n=4:")
    for board in best(4):
        for row in board:
            print(row)
        print()
