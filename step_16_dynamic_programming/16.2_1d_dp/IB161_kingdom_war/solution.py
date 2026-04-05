"""
Problem: Kingdom War
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a 2D matrix (possibly with negative values), find the maximum sum sub-rectangle.
Extension of Kadane's 1D algorithm to 2D.
"""

from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Try all pairs of top-left and bottom-right corners
# Time: O(N^2 * M^2)  |  Space: O(1)
# ============================================================
def brute_force(matrix: List[List[int]]) -> int:
    if not matrix or not matrix[0]:
        return 0
    rows, cols = len(matrix), len(matrix[0])
    max_sum = float('-inf')

    for r1 in range(rows):
        for r2 in range(r1, rows):
            for c1 in range(cols):
                for c2 in range(c1, cols):
                    total = sum(matrix[r][c] for r in range(r1, r2 + 1) for c in range(c1, c2 + 1))
                    max_sum = max(max_sum, total)

    return max_sum


# ============================================================
# APPROACH 2: OPTIMAL
# Fix top and bottom rows; collapse to 1D array; apply Kadane's algorithm
# Time: O(N^2 * M)  |  Space: O(M)
# ============================================================
def optimal(matrix: List[List[int]]) -> int:
    if not matrix or not matrix[0]:
        return 0
    rows, cols = len(matrix), len(matrix[0])

    def kadane(arr):
        max_s = curr = arr[0]
        for x in arr[1:]:
            curr = max(x, curr + x)
            max_s = max(max_s, curr)
        return max_s

    max_sum = float('-inf')
    for top in range(rows):
        col_sum = [0] * cols
        for bottom in range(top, rows):
            for c in range(cols):
                col_sum[c] += matrix[bottom][c]
            max_sum = max(max_sum, kadane(col_sum))

    return max_sum


# ============================================================
# APPROACH 3: BEST
# Transpose if rows < cols to iterate over shorter dimension first (better cache)
# Time: O(min(N,M)^2 * max(N,M))  |  Space: O(max(N,M))
# ============================================================
def best(matrix: List[List[int]]) -> int:
    if not matrix or not matrix[0]:
        return 0
    rows, cols = len(matrix), len(matrix[0])

    if rows > cols:
        # Transpose
        matrix = [[matrix[r][c] for r in range(rows)] for c in range(cols)]

    return optimal(matrix)


if __name__ == "__main__":
    print("=== Kingdom War ===")

    m1 = [
        [1, 2, -1, -4, -20],
        [-8, -3, 4, 2, 1],
        [3, 8, 10, 1, 3],
        [-4, -1, 1, 7, -6]
    ]
    print(f"BruteForce: {brute_force(m1)}")  # 29
    print(f"Optimal:    {optimal(m1)}")       # 29
    print(f"Best:       {best(m1)}")          # 29

    m2 = [[-1, -2], [-3, -4]]
    print(f"All negative: {optimal(m2)}")  # -1

    m3 = [[2, 1], [-1, 3]]
    print(f"2x2: {optimal(m3)}")  # 5
