"""
Problem: Search in 2D Matrix
Difficulty: MEDIUM | XP: 25
LeetCode #74

Key Insight: The matrix is a flattened sorted array.
Use virtual index: row = idx // n, col = idx % n.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Linear Scan
# Time: O(m * n)  |  Space: O(1)
#
# Check every element.
# ============================================================
def brute_force(matrix: List[List[int]], target: int) -> bool:
    for row in matrix:
        for val in row:
            if val == target:
                return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL -- Two Binary Searches
# Time: O(log m + log n)  |  Space: O(1)
#
# Step 1: Binary search to find the correct row.
# Step 2: Binary search within that row.
# ============================================================
def optimal(matrix: List[List[int]], target: int) -> bool:
    m, n = len(matrix), len(matrix[0])

    # Step 1: Find the row where target could reside
    lo, hi = 0, m - 1
    target_row = -1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if target < matrix[mid][0]:
            hi = mid - 1
        elif target > matrix[mid][n - 1]:
            lo = mid + 1
        else:
            target_row = mid
            break

    if target_row == -1:
        return False

    # Step 2: Binary search within the row
    lo, hi = 0, n - 1
    while lo <= hi:
        mid = lo + (hi - lo) // 2
        if matrix[target_row][mid] == target:
            return True
        elif matrix[target_row][mid] < target:
            lo = mid + 1
        else:
            hi = mid - 1

    return False


# ============================================================
# APPROACH 3: BEST -- Single Binary Search (Flattened)
# Time: O(log(m * n))  |  Space: O(1)
#
# Treat matrix as 1D sorted array. Map virtual index to 2D.
# ============================================================
def best(matrix: List[List[int]], target: int) -> bool:
    m, n = len(matrix), len(matrix[0])
    lo, hi = 0, m * n - 1

    while lo <= hi:
        mid = lo + (hi - lo) // 2
        row, col = divmod(mid, n)
        val = matrix[row][col]

        if val == target:
            return True
        elif val < target:
            lo = mid + 1
        else:
            hi = mid - 1

    return False


if __name__ == "__main__":
    print("=== Search in 2D Matrix ===")
    matrix = [
        [1, 3, 5, 7],
        [10, 11, 16, 20],
        [23, 30, 34, 60],
    ]
    targets = [3, 13, 1, 60, 23, 100]

    print(f"Matrix: {matrix}")
    print()

    for t in targets:
        print(f"Target={t}")
        print(f"  Brute:   {brute_force(matrix, t)}")
        print(f"  Optimal: {optimal(matrix, t)}")
        print(f"  Best:    {best(matrix, t)}")
        print()
