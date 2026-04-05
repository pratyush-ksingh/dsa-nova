"""Problem: Rotate Matrix 90 Degrees
Difficulty: MEDIUM | XP: 25

Rotate an NxN matrix 90 degrees clockwise in-place.
Real-life use: image rotation in graphics pipelines without extra memory.
"""
from typing import List
import copy

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(n^2)
# Create new matrix using the rotation formula: new[j][n-1-i] = old[i][j]
# ============================================================
def brute_force(matrix: List[List[int]]) -> List[List[int]]:
    n = len(matrix)
    result = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            result[j][n - 1 - i] = matrix[i][j]
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Transpose + Reverse Rows)
# Time: O(n^2)  |  Space: O(1)
# Transpose the matrix, then reverse each row in-place.
# ============================================================
def optimal(matrix: List[List[int]]) -> List[List[int]]:
    n = len(matrix)
    # Transpose
    for i in range(n):
        for j in range(i + 1, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    # Reverse each row
    for i in range(n):
        matrix[i].reverse()
    return matrix


# ============================================================
# APPROACH 3: BEST (4-way cyclic swap by layer)
# Time: O(n^2)  |  Space: O(1)
# Process each concentric layer, 4-way rotate elements.
# ============================================================
def best(matrix: List[List[int]]) -> List[List[int]]:
    n = len(matrix)
    for layer in range(n // 2):
        first, last = layer, n - 1 - layer
        for i in range(first, last):
            offset = i - first
            top = matrix[first][i]
            # left -> top
            matrix[first][i] = matrix[last - offset][first]
            # bottom -> left
            matrix[last - offset][first] = matrix[last][last - offset]
            # right -> bottom
            matrix[last][last - offset] = matrix[i][last]
            # top -> right
            matrix[i][last] = top
    return matrix


if __name__ == "__main__":
    def matrix_str(m):
        return '\n  '.join(str(row) for row in m)

    tests = [
        [[1, 2, 3], [4, 5, 6], [7, 8, 9]],
        [[5, 1, 9, 11], [2, 4, 8, 10], [13, 3, 6, 7], [15, 14, 12, 16]],
    ]
    expected = [
        [[7, 4, 1], [8, 5, 2], [9, 6, 3]],
        [[15, 13, 2, 5], [14, 3, 4, 1], [12, 6, 8, 9], [16, 7, 10, 11]],
    ]
    print("=== Rotate Matrix 90 Degrees Clockwise ===")
    for m, exp in zip(tests, expected):
        bf = brute_force(copy.deepcopy(m))
        op = optimal(copy.deepcopy(m))
        bst = best(copy.deepcopy(m))
        ok = "OK" if bf == op == bst == exp else "MISMATCH"
        print(f"Input:\n  {matrix_str(m)}")
        print(f"BF result:\n  {matrix_str(bf)}")
        print(f"OPT result:\n  {matrix_str(op)}")
        print(f"BEST result:\n  {matrix_str(bst)}")
        print(f"Expected:\n  {matrix_str(exp)}")
        print(f"Status: {ok}\n")
