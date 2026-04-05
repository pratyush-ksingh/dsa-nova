"""
Problem: Valid Sudoku
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Determine if a 9x9 Sudoku board is valid (partially filled is OK).
Each row, column, and 3x3 box must contain digits 1-9 with no repeats.
Empty cells are represented by '.'.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(1) [81 cells fixed]  |  Space: O(1) [bounded sets]
# ============================================================
def brute_force(board: List[List[str]]) -> bool:
    """
    Three separate passes: validate rows, columns, then 3x3 boxes.
    Real-life: Input validation for puzzle games and constraint-satisfaction solvers.
    """
    for r in range(9):
        seen = set()
        for c in range(9):
            ch = board[r][c]
            if ch == '.':
                continue
            if ch in seen:
                return False
            seen.add(ch)

    for c in range(9):
        seen = set()
        for r in range(9):
            ch = board[r][c]
            if ch == '.':
                continue
            if ch in seen:
                return False
            seen.add(ch)

    for box_r in range(3):
        for box_c in range(3):
            seen = set()
            for r in range(box_r * 3, box_r * 3 + 3):
                for c in range(box_c * 3, box_c * 3 + 3):
                    ch = board[r][c]
                    if ch == '.':
                        continue
                    if ch in seen:
                        return False
                    seen.add(ch)
    return True


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(1) [81 cells]  |  Space: O(1) [bounded set]
# ============================================================
def optimal(board: List[List[str]]) -> bool:
    """
    Single pass: encode (row,digit), (col,digit), (box,digit) as tuples in one set.
    Box index = (row//3, col//3).
    Real-life: Real-time game-state validation in Sudoku apps.
    """
    seen = set()
    for r in range(9):
        for c in range(9):
            ch = board[r][c]
            if ch == '.':
                continue
            box = (r // 3, c // 3)
            keys = (('r', r, ch), ('c', c, ch), ('b', box, ch))
            for key in keys:
                if key in seen:
                    return False
                seen.add(key)
    return True


# ============================================================
# APPROACH 3: BEST
# Time: O(1) [81 cells]  |  Space: O(1) [27 integers]
# ============================================================
def best(board: List[List[str]]) -> bool:
    """
    Bit-mask approach: integer arrays for rows, cols, boxes.
    Each bit represents whether a digit (1-9) has been placed.
    Avoids tuple/string allocation — fastest in practice.
    Real-life: High-performance constraint checking in embedded Sudoku solvers.
    """
    rows  = [0] * 9
    cols  = [0] * 9
    boxes = [0] * 9
    for r in range(9):
        for c in range(9):
            ch = board[r][c]
            if ch == '.':
                continue
            bit     = 1 << (int(ch) - 1)
            box_idx = (r // 3) * 3 + (c // 3)
            if rows[r] & bit or cols[c] & bit or boxes[box_idx] & bit:
                return False
            rows[r]       |= bit
            cols[c]       |= bit
            boxes[box_idx] |= bit
    return True


if __name__ == "__main__":
    print("=== Valid Sudoku ===")
    valid = [
        ['5','3','.','.','7','.','.','.','.'],
        ['6','.','.','1','9','5','.','.','.'],
        ['.','9','8','.','.','.','.','6','.'],
        ['8','.','.','.','6','.','.','.','3'],
        ['4','.','.','8','.','3','.','.','1'],
        ['7','.','.','.','2','.','.','.','6'],
        ['.','6','.','.','.','.','2','8','.'],
        ['.','.','.','4','1','9','.','.','5'],
        ['.','.','.','.','8','.','.','7','9'],
    ]
    invalid = [
        ['8','3','.','.','7','.','.','.','.'],
        ['6','.','.','1','9','5','.','.','.'],
        ['.','9','8','.','.','.','.','6','.'],
        ['8','.','.','.','6','.','.','.','3'],
        ['4','.','.','8','.','3','.','.','1'],
        ['7','.','.','.','2','.','.','.','6'],
        ['.','6','.','.','.','.','2','8','.'],
        ['.','.','.','4','1','9','.','.','5'],
        ['.','.','.','.','8','.','.','7','9'],
    ]
    print(f"\nValid board:")
    print(f"  Brute:   {brute_force(valid)}  (expected True)")
    print(f"  Optimal: {optimal(valid)}  (expected True)")
    print(f"  Best:    {best(valid)}  (expected True)")
    print(f"\nInvalid board (duplicate 8 in first col):")
    print(f"  Brute:   {brute_force(invalid)}  (expected False)")
    print(f"  Optimal: {optimal(invalid)}  (expected False)")
    print(f"  Best:    {best(invalid)}  (expected False)")
