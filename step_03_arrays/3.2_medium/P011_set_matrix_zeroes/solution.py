"""
Problem: Set Matrix Zeroes
Difficulty: MEDIUM | XP: 25
LeetCode: 73

Given an m x n integer matrix, if an element is 0, set its entire row and
column to 0. Do this in-place.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(m*n*(m+n))  |  Space: O(m*n)
# ============================================================
def brute_force(matrix: List[List[int]]) -> None:
    """
    Copy the matrix. For each 0 in the original, mark the entire row and
    column in the copy. Then overwrite the original with the copy.
    Avoids the problem of marking new zeros and then incorrectly zeroing
    additional rows/columns based on those new zeros.
    """
    m, n = len(matrix), len(matrix[0])
    copy = [row[:] for row in matrix]

    for i in range(m):
        for j in range(n):
            if copy[i][j] == 0:
                # Zero out row i in the original
                for c in range(n):
                    matrix[i][c] = 0
                # Zero out column j in the original
                for r in range(m):
                    matrix[r][j] = 0


# ============================================================
# APPROACH 2: OPTIMAL — Row/Column Boolean Sets
# Time: O(m*n)  |  Space: O(m+n)
# ============================================================
def optimal(matrix: List[List[int]]) -> None:
    """
    First pass: record which rows and columns contain zeros in two sets.
    Second pass: zero out every cell whose row or column is in those sets.
    Two passes, O(m+n) extra space.
    """
    m, n = len(matrix), len(matrix[0])
    zero_rows = set()
    zero_cols = set()

    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 0:
                zero_rows.add(i)
                zero_cols.add(j)

    for i in range(m):
        for j in range(n):
            if i in zero_rows or j in zero_cols:
                matrix[i][j] = 0


# ============================================================
# APPROACH 3: BEST — In-Place O(1) Space Using First Row/Column as Markers
# Time: O(m*n)  |  Space: O(1)
# ============================================================
def best(matrix: List[List[int]]) -> None:
    """
    Use the first row and first column of the matrix itself as marker arrays.
    - matrix[0][j] == 0 means column j should be zeroed.
    - matrix[i][0] == 0 means row i should be zeroed.
    Need an extra boolean to track if the first row itself had a zero,
    and another for the first column.
    """
    m, n = len(matrix), len(matrix[0])

    # Check if first row/column originally contain zeros
    first_row_has_zero = any(matrix[0][j] == 0 for j in range(n))
    first_col_has_zero = any(matrix[i][0] == 0 for i in range(m))

    # Use first row and column as markers for the rest of the matrix
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][j] == 0:
                matrix[i][0] = 0  # mark row
                matrix[0][j] = 0  # mark column

    # Zero out cells based on markers (skip first row/col for now)
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][0] == 0 or matrix[0][j] == 0:
                matrix[i][j] = 0

    # Handle first row
    if first_row_has_zero:
        for j in range(n):
            matrix[0][j] = 0

    # Handle first column
    if first_col_has_zero:
        for i in range(m):
            matrix[i][0] = 0


def print_matrix(matrix):
    for row in matrix:
        print(" ", row)


if __name__ == "__main__":
    print("=== Set Matrix Zeroes ===")

    import copy

    matrix1 = [[1, 1, 1], [1, 0, 1], [1, 1, 1]]
    m1a, m1b, m1c = copy.deepcopy(matrix1), copy.deepcopy(matrix1), copy.deepcopy(matrix1)
    print("Input:"); print_matrix(matrix1)
    brute_force(m1a); print("Brute:"); print_matrix(m1a)
    optimal(m1b);     print("Optimal:"); print_matrix(m1b)
    best(m1c);        print("Best:"); print_matrix(m1c)

    matrix2 = [[0, 1, 2, 0], [3, 4, 5, 2], [1, 3, 1, 5]]
    m2a, m2b, m2c = copy.deepcopy(matrix2), copy.deepcopy(matrix2), copy.deepcopy(matrix2)
    print("\nInput:"); print_matrix(matrix2)
    brute_force(m2a); print("Brute:"); print_matrix(m2a)
    optimal(m2b);     print("Optimal:"); print_matrix(m2b)
    best(m2c);        print("Best:"); print_matrix(m2c)
