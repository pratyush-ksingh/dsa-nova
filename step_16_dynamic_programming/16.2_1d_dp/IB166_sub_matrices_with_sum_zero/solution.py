"""
Problem: Sub Matrices with Sum Zero
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a 2D matrix, find the number of sub-matrices whose sum equals zero.
"""
from typing import List, Optional
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE — Check all sub-matrices with prefix sum
# Time: O(m^2 * n^2)  |  Space: O(m * n)
# ============================================================
def brute_force(matrix: List[List[int]]) -> int:
    """
    Compute 2D prefix sums, then enumerate all possible sub-matrices
    defined by (r1, c1, r2, c2) and check if sum == 0.
    """
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])

    # Build prefix sum: prefix[i][j] = sum of matrix[0..i-1][0..j-1]
    prefix = [[0] * (n + 1) for _ in range(m + 1)]
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            prefix[i][j] = (matrix[i - 1][j - 1]
                            + prefix[i - 1][j]
                            + prefix[i][j - 1]
                            - prefix[i - 1][j - 1])

    count = 0
    for r1 in range(1, m + 1):
        for r2 in range(r1, m + 1):
            for c1 in range(1, n + 1):
                for c2 in range(c1, n + 1):
                    total = (prefix[r2][c2]
                             - prefix[r1 - 1][c2]
                             - prefix[r2][c1 - 1]
                             + prefix[r1 - 1][c1 - 1])
                    if total == 0:
                        count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL — Fix row pair, use 1D subarray sum = 0
# Time: O(m^2 * n)  |  Space: O(n)
# ============================================================
def optimal(matrix: List[List[int]]) -> int:
    """
    Fix top and bottom rows. Compress columns into a 1D array (column sums).
    Then count subarrays with sum 0 using prefix sum + hashmap.
    """
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    count = 0

    for top in range(m):
        col_sum = [0] * n
        for bottom in range(top, m):
            for c in range(n):
                col_sum[c] += matrix[bottom][c]

            # Count subarrays of col_sum with sum 0
            prefix_count = defaultdict(int)
            prefix_count[0] = 1
            running = 0
            for c in range(n):
                running += col_sum[c]
                count += prefix_count[running]
                prefix_count[running] += 1

    return count


# ============================================================
# APPROACH 3: BEST — Same as optimal (this is the standard best approach)
# Time: O(m^2 * n)  |  Space: O(n)
# ============================================================
def best(matrix: List[List[int]]) -> int:
    """
    Same idea as optimal but swap m and n roles to use the smaller
    dimension for the outer loop, reducing constant factor.
    """
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])

    # Use the smaller dimension for the O(d^2) outer loop
    if m <= n:
        return _count_zero_submatrices(matrix, m, n)
    else:
        # Transpose
        transposed = [[matrix[i][j] for i in range(m)] for j in range(n)]
        return _count_zero_submatrices(transposed, n, m)


def _count_zero_submatrices(matrix, m, n):
    count = 0
    for top in range(m):
        col_sum = [0] * n
        for bottom in range(top, m):
            for c in range(n):
                col_sum[c] += matrix[bottom][c]

            prefix_count = defaultdict(int)
            prefix_count[0] = 1
            running = 0
            for c in range(n):
                running += col_sum[c]
                count += prefix_count[running]
                prefix_count[running] += 1
    return count


if __name__ == "__main__":
    print("=== Sub Matrices with Sum Zero ===")

    matrix = [[1, -1], [-1, 1]]
    print(f"Brute:   {brute_force(matrix)}")   # 5
    print(f"Optimal: {optimal(matrix)}")       # 5
    print(f"Best:    {best(matrix)}")          # 5

    matrix = [[0, 0], [0, 0]]
    print(f"Brute:   {brute_force(matrix)}")   # 9
    print(f"Optimal: {optimal(matrix)}")       # 9
    print(f"Best:    {best(matrix)}")          # 9
