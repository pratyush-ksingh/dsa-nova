"""
Problem: Find Row with Maximum 1s
Difficulty: MEDIUM | XP: 25

Key Insight: Rows are sorted, so use binary search per row (O(m log n))
or staircase traversal from top-right (O(m + n)).
"""
from typing import List, Optional
from bisect import bisect_left


# ============================================================
# APPROACH 1: BRUTE FORCE -- Count Each Row
# Time: O(m * n)  |  Space: O(1)
#
# Sum each row, track the maximum.
# ============================================================
def brute_force(matrix: List[List[int]]) -> int:
    best_row = 0
    max_count = 0
    for i, row in enumerate(matrix):
        count = sum(row)
        if count > max_count:
            max_count = count
            best_row = i
    return best_row


# ============================================================
# APPROACH 2: OPTIMAL -- Binary Search per Row
# Time: O(m log n)  |  Space: O(1)
#
# Find leftmost 1 in each row via bisect_left.
# count = n - first_one_index.
# ============================================================
def optimal(matrix: List[List[int]]) -> int:
    m, n = len(matrix), len(matrix[0])
    best_row = 0
    max_count = 0

    for i in range(m):
        first_one = bisect_left(matrix[i], 1)
        count = n - first_one
        if count > max_count:
            max_count = count
            best_row = i

    return best_row


# ============================================================
# APPROACH 3: BEST -- Staircase Traversal (Top-Right)
# Time: O(m + n)  |  Space: O(1)
#
# Start top-right. Move left on 1 (better count), down on 0.
# ============================================================
def best(matrix: List[List[int]]) -> int:
    m, n = len(matrix), len(matrix[0])
    row, col = 0, n - 1
    best_row = 0

    while row < m and col >= 0:
        if matrix[row][col] == 1:
            best_row = row  # this row has 1s extending at least to col
            col -= 1        # check if it extends further left
        else:
            row += 1        # this row can't beat current best

    return best_row


if __name__ == "__main__":
    print("=== Find Row with Maximum 1s ===")
    tests = [
        [[0, 1, 1, 1], [0, 0, 1, 1], [1, 1, 1, 1], [0, 0, 0, 0]],
        [[0, 0], [0, 1]],
        [[1, 1], [1, 1]],
        [[0, 0, 0], [0, 0, 0]],
    ]
    for mat in tests:
        print(f"Matrix: {mat}")
        print(f"  Brute:   row {brute_force(mat)}")
        print(f"  Optimal: row {optimal(mat)}")
        print(f"  Best:    row {best(mat)}")
        print()
