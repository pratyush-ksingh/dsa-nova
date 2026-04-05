"""Problem: Find Peak Element II
Difficulty: HARD | XP: 50

A peak element in a 2D matrix is strictly greater than all its neighbours
(up, down, left, right). Find any one peak and return its [row, col].
Real-life use: finding local maxima in terrain/image data.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(m*n)  |  Space: O(1)
# Check every cell for the peak condition
# ============================================================
def brute_force(mat: List[List[int]]) -> List[int]:
    m, n = len(mat), len(mat[0])
    for i in range(m):
        for j in range(n):
            val = mat[i][j]
            is_peak = True
            for di, dj in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                ni, nj = i + di, j + dj
                if 0 <= ni < m and 0 <= nj < n and mat[ni][nj] >= val:
                    is_peak = False
                    break
            if is_peak:
                return [i, j]
    return [-1, -1]


# ============================================================
# APPROACH 2: OPTIMAL (Binary Search on columns)
# Time: O(m log n)  |  Space: O(1)
# For mid column: find row with max value in that column.
# If neighbour (left/right) is larger, move search there.
# Otherwise mat[maxRow][mid] is a valid peak.
# ============================================================
def optimal(mat: List[List[int]]) -> List[int]:
    m, n = len(mat), len(mat[0])
    lo, hi = 0, n - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        # Find row index with max value in column mid
        max_row = max(range(m), key=lambda r: mat[r][mid])
        left_big  = mid > 0     and mat[max_row][mid - 1] > mat[max_row][mid]
        right_big = mid < n - 1 and mat[max_row][mid + 1] > mat[max_row][mid]
        if not left_big and not right_big:
            return [max_row, mid]
        elif left_big:
            hi = mid - 1
        else:
            lo = mid + 1
    return [-1, -1]


# ============================================================
# APPROACH 3: BEST (Binary Search on rows)
# Time: O(n log m)  |  Space: O(1)
# Symmetric version: binary search on row dimension.
# For mid row, find column with max value, check up/down neighbours.
# ============================================================
def best(mat: List[List[int]]) -> List[int]:
    m, n = len(mat), len(mat[0])
    lo, hi = 0, m - 1
    while lo <= hi:
        mid = (lo + hi) // 2
        max_col = max(range(n), key=lambda c: mat[mid][c])
        up_big   = mid > 0     and mat[mid - 1][max_col] > mat[mid][max_col]
        down_big = mid < m - 1 and mat[mid + 1][max_col] > mat[mid][max_col]
        if not up_big and not down_big:
            return [mid, max_col]
        elif up_big:
            hi = mid - 1
        else:
            lo = mid + 1
    return [-1, -1]


def verify_peak(mat, r, c):
    m, n = len(mat), len(mat[0])
    for di, dj in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
        ni, nj = r + di, c + dj
        if 0 <= ni < m and 0 <= nj < n and mat[ni][nj] >= mat[r][c]:
            return False
    return True


if __name__ == "__main__":
    tests = [
        [[1, 4], [3, 2]],
        [[10, 20, 15], [21, 30, 14], [7, 16, 32]],
        [[1, 2, 3, 4], [2, 3, 4, 5], [3, 4, 5, 6]],
    ]
    print("=== Find Peak Element in 2D Matrix ===")
    for mat in tests:
        bf  = brute_force(mat)
        op  = optimal(mat)
        bst = best(mat)
        print(f"BF  {bf}  val={mat[bf[0]][bf[1]]}  isPeak={verify_peak(mat, bf[0], bf[1])}")
        print(f"OPT {op}  val={mat[op[0]][op[1]]}  isPeak={verify_peak(mat, op[0], op[1])}")
        print(f"BEST{bst}  val={mat[bst[0]][bst[1]]}  isPeak={verify_peak(mat, bst[0], bst[1])}")
        print()
