"""
Problem: Search a 2D Matrix II (LeetCode #240)
Difficulty: MEDIUM | XP: 25

Each row sorted left to right. Each column sorted top to bottom.
Search for a target value.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Linear Scan)
# Time: O(m * n) | Space: O(1)
# ============================================================
def brute_force(matrix: List[List[int]], target: int) -> bool:
    """Scan every element in the matrix."""
    for row in matrix:
        for val in row:
            if val == target:
                return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL (Staircase / Top-Right Corner Search)
# Time: O(m + n) | Space: O(1)
# ============================================================
def optimal(matrix: List[List[int]], target: int) -> bool:
    """Start from top-right corner. Go left if target < current, down if target > current."""
    if not matrix or not matrix[0]:
        return False

    m, n = len(matrix), len(matrix[0])
    row, col = 0, n - 1  # start at top-right

    while row < m and col >= 0:
        if matrix[row][col] == target:
            return True
        elif matrix[row][col] > target:
            col -= 1   # current too large, eliminate this column
        else:
            row += 1   # current too small, eliminate this row
    return False


# ============================================================
# APPROACH 3: BEST (Binary Search Per Row)
# Time: O(m * log(n)) | Space: O(1)
# ============================================================
def best(matrix: List[List[int]], target: int) -> bool:
    """Binary search within each row. Skip rows where target is out of range."""
    if not matrix or not matrix[0]:
        return False

    m, n = len(matrix), len(matrix[0])

    for row in matrix:
        # Early skip: if target < first element or > last element of this row
        if row[0] > target:
            break       # all subsequent rows are even larger
        if row[-1] < target:
            continue    # target not in this row

        # Binary search in this row
        lo, hi = 0, n - 1
        while lo <= hi:
            mid = lo + (hi - lo) // 2
            if row[mid] == target:
                return True
            elif row[mid] < target:
                lo = mid + 1
            else:
                hi = mid - 1
    return False


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Search a 2D Matrix II ===\n")

    matrix1 = [
        [1,  4,  7, 11, 15],
        [2,  5,  8, 12, 19],
        [3,  6,  9, 16, 22],
        [10, 13, 14, 17, 24],
        [18, 21, 23, 26, 30]
    ]

    test_cases = [
        (matrix1, 5, True),
        (matrix1, 20, False),
        (matrix1, 1, True),
        (matrix1, 30, True),
        (matrix1, 0, False),
        ([[1]], 1, True),
        ([[1]], 2, False),
        ([], 1, False),
    ]

    for matrix, target, expected in test_cases:
        b = brute_force(matrix, target)
        o = optimal(matrix, target)
        n = best(matrix, target)
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Target: {target}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
