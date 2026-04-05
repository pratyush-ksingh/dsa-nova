"""
Problem: Sudoku Solver
Difficulty: HARD | XP: 50

Fill a 9×9 Sudoku board. '.' = empty. Exactly one solution exists.
Each row, column, and 3×3 box must contain digits 1-9 exactly once.
"""
from typing import List
import copy


# ============================================================
# APPROACH 1: BRUTE FORCE - Backtracking with linear scan validation
# Time: O(9^(empty cells))  |  Space: O(81) recursion stack
# ============================================================
def brute_force(board: List[List[str]]) -> bool:
    """Try digits 1-9 for each empty cell; validate by scanning row/col/box."""
    def is_valid(r: int, c: int, d: str) -> bool:
        for i in range(9):
            if board[r][i] == d: return False
            if board[i][c] == d: return False
            if board[3*(r//3) + i//3][3*(c//3) + i%3] == d: return False
        return True

    for r in range(9):
        for c in range(9):
            if board[r][c] == '.':
                for d in '123456789':
                    if is_valid(r, c, d):
                        board[r][c] = d
                        if brute_force(board):
                            return True
                        board[r][c] = '.'
                return False
    return True


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking with bitmask conflict tracking
# Time: O(9^(empty cells))  |  Space: O(27) bitmasks
# Use 9 row/col/box bitmasks for O(1) conflict check and update.
# ============================================================
def optimal(board: List[List[str]]) -> bool:
    """Bitmask backtracking: O(1) validity via bitwise OR."""
    row_m = [0] * 9
    col_m = [0] * 9
    box_m = [0] * 9

    for r in range(9):
        for c in range(9):
            if board[r][c] != '.':
                bit = 1 << (int(board[r][c]) - 1)
                row_m[r] |= bit
                col_m[c] |= bit
                box_m[(r//3)*3 + c//3] |= bit

    def solve(r: int, c: int) -> bool:
        if r == 9:
            return True
        nr, nc = (r + 1, 0) if c == 8 else (r, c + 1)
        if board[r][c] != '.':
            return solve(nr, nc)
        box = (r//3)*3 + c//3
        used = row_m[r] | col_m[c] | box_m[box]
        for d in range(9):
            if not (used >> d & 1):
                bit = 1 << d
                board[r][c] = str(d + 1)
                row_m[r] |= bit; col_m[c] |= bit; box_m[box] |= bit
                if solve(nr, nc):
                    return True
                board[r][c] = '.'
                row_m[r] &= ~bit; col_m[c] &= ~bit; box_m[box] &= ~bit
        return False

    return solve(0, 0)


# ============================================================
# APPROACH 3: BEST - MRV (Minimum Remaining Values) heuristic
# Time: O(9^(empty cells)) with heavy pruning  |  Space: O(81)
# Always pick the empty cell with fewest valid candidates first.
# Reduces branching factor dramatically on hard puzzles.
# ============================================================
def best(board: List[List[str]]) -> bool:
    """
    Constraint propagation via MRV heuristic:
    At each step, choose the cell with the minimum number of valid candidates.
    This dramatically reduces the search space.
    """
    row_m = [0] * 9
    col_m = [0] * 9
    box_m = [0] * 9

    for r in range(9):
        for c in range(9):
            if board[r][c] != '.':
                bit = 1 << (int(board[r][c]) - 1)
                row_m[r] |= bit
                col_m[c] |= bit
                box_m[(r//3)*3 + c//3] |= bit

    def solve() -> bool:
        # Find cell with fewest candidates (MRV)
        min_count = 10
        best_r = best_c = -1
        for r in range(9):
            for c in range(9):
                if board[r][c] == '.':
                    used = row_m[r] | col_m[c] | box_m[(r//3)*3 + c//3]
                    avail = (~used) & 0x1FF  # 9-bit mask
                    cnt = bin(avail).count('1')
                    if cnt == 0:
                        return False   # dead end
                    if cnt < min_count:
                        min_count = cnt
                        best_r, best_c = r, c
        if best_r == -1:
            return True  # all filled
        r, c = best_r, best_c
        box = (r//3)*3 + c//3
        avail = (~(row_m[r] | col_m[c] | box_m[box])) & 0x1FF
        while avail:
            bit = avail & (-avail)
            d = bit.bit_length() - 1
            board[r][c] = str(d + 1)
            row_m[r] |= bit; col_m[c] |= bit; box_m[box] |= bit
            if solve():
                return True
            board[r][c] = '.'
            row_m[r] &= ~bit; col_m[c] &= ~bit; box_m[box] &= ~bit
            avail &= avail - 1
        return False

    return solve()


if __name__ == "__main__":
    print("=== Sudoku Solver ===")
    puzzle = [
        list("53..7...."),
        list("6..195..."),
        list(".98....6."),
        list("8...6...3"),
        list("4..8.3..1"),
        list("7...2...6"),
        list(".6....28."),
        list("...419..5"),
        list("....8..79"),
    ]

    for name, fn in [("Brute Force", brute_force), ("Optimal", optimal), ("Best (MRV)", best)]:
        b = copy.deepcopy(puzzle)
        fn(b)
        print(f"\n--- {name} ---")
        for row in b:
            print(''.join(row))
