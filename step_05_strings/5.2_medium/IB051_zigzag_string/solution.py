"""
Problem: Zigzag String
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Write the string in zigzag pattern across numRows rows, then read row by row.
Example: "PAYPALISHIRING" with 3 rows:
  P   A   H   N
  A P L S I I G
  Y   I   R
=> "PAHNAPLSIIGYIR"
"""


# ============================================================
# APPROACH 1: BRUTE FORCE  (2D matrix simulation)
# Time: O(n)  |  Space: O(numRows * n)
# ============================================================
def brute_force(s: str, num_rows: int) -> str:
    """
    Simulate the zigzag path on a 2D grid, then read left-to-right, row-by-row.
    Real-life: Encoding text for visual display patterns (LED matrix scrolling).
    """
    if num_rows == 1 or num_rows >= len(s):
        return s
    n = len(s)
    grid = [[' '] * n for _ in range(num_rows)]
    row, col = 0, 0
    going_down = True

    for ch in s:
        grid[row][col] = ch
        if going_down:
            if row == num_rows - 1:
                going_down = False
                row -= 1
                col += 1
            else:
                row += 1
        else:
            if row == 0:
                going_down = True
                row += 1
            else:
                row -= 1
                col += 1

    return ''.join(ch for r in grid for ch in r if ch != ' ')


# ============================================================
# APPROACH 2: OPTIMAL  (Row-by-row lists)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(s: str, num_rows: int) -> str:
    """
    Maintain one list per row. Assign each character to the current row,
    flip direction at the top and bottom. Concatenate all rows.
    Real-life: Cipher encoding for simple substitution-like transformations.
    """
    if num_rows == 1 or num_rows >= len(s):
        return s
    rows = [[] for _ in range(num_rows)]
    cur_row = 0
    going_down = False

    for ch in s:
        rows[cur_row].append(ch)
        if cur_row == 0 or cur_row == num_rows - 1:
            going_down = not going_down
        cur_row += 1 if going_down else -1

    return ''.join(ch for row in rows for ch in row)


# ============================================================
# APPROACH 3: BEST  (Direct index computation)
# Time: O(n)  |  Space: O(n) for output only
# ============================================================
def best(s: str, num_rows: int) -> str:
    """
    Compute exactly which characters land in each row mathematically.
    Cycle length = 2*(num_rows-1). No simulation needed.
    Real-life: Zero-allocation zigzag encoding in performance-critical systems.
    """
    if num_rows == 1 or num_rows >= len(s):
        return s
    n = len(s)
    cycle = 2 * (num_rows - 1)
    result = []
    for row in range(num_rows):
        j = 0
        while j + row < n:
            result.append(s[j + row])
            if row != 0 and row != num_rows - 1:
                idx2 = j + cycle - row
                if idx2 < n:
                    result.append(s[idx2])
            j += cycle
    return ''.join(result)


if __name__ == "__main__":
    print("=== Zigzag String ===")
    tests = [
        ("PAYPALISHIRING", 3, "PAHNAPLSIIGYIR"),
        ("PAYPALISHIRING", 4, "PINALSIGYAHRPI"),
        ("A",              1, "A"),
        ("AB",             1, "AB"),
    ]
    for s, rows, exp in tests:
        print(f"\nInput: \"{s}\"  rows={rows}  =>  expected: \"{exp}\"")
        print(f"  Brute:   \"{brute_force(s, rows)}\"")
        print(f"  Optimal: \"{optimal(s, rows)}\"")
        print(f"  Best:    \"{best(s, rows)}\"")
