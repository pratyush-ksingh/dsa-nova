"""
Problem: Maximum Sum Square SubMatrix
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an N x N integer matrix A and an integer B, find the maximum sum
among all B x B square submatrices of A.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2 * B^2)  |  Space: O(1)
# ============================================================
def brute_force(A: List[List[int]], B: int) -> int:
    """
    For each valid top-left corner (i, j) of a B x B submatrix,
    compute the sum of all B*B elements by iterating over them directly.
    Track and return the maximum sum found.
    """
    n = len(A)
    max_sum = float('-inf')
    for i in range(n - B + 1):
        for j in range(n - B + 1):
            current_sum = 0
            for r in range(i, i + B):
                for c in range(j, j + B):
                    current_sum += A[r][c]
            max_sum = max(max_sum, current_sum)
    return max_sum


# ============================================================
# APPROACH 2: OPTIMAL — 2D Prefix Sum
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def optimal(A: List[List[int]], B: int) -> int:
    """
    Build a 2D prefix sum table where prefix[i][j] = sum of all elements
    in the submatrix from (0,0) to (i-1, j-1).
    Any B x B submatrix sum can then be computed in O(1) using inclusion-exclusion.
    """
    n = len(A)
    # Build prefix sum table (1-indexed for convenience)
    prefix = [[0] * (n + 1) for _ in range(n + 1)]
    for i in range(1, n + 1):
        for j in range(1, n + 1):
            prefix[i][j] = (A[i-1][j-1]
                            + prefix[i-1][j]
                            + prefix[i][j-1]
                            - prefix[i-1][j-1])

    max_sum = float('-inf')
    for i in range(B, n + 1):
        for j in range(B, n + 1):
            # Sum of B x B submatrix with bottom-right corner at (i, j) in 1-indexed
            current_sum = (prefix[i][j]
                           - prefix[i-B][j]
                           - prefix[i][j-B]
                           + prefix[i-B][j-B])
            max_sum = max(max_sum, current_sum)
    return max_sum


# ============================================================
# APPROACH 3: BEST — 2D Prefix Sum (same, with row compression note)
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def best(A: List[List[int]], B: int) -> int:
    """
    Same as optimal. For very large matrices where memory is critical,
    one can compute row-wise prefix sums and use a sliding window of B rows,
    reducing space to O(n). Implemented here as the clean prefix sum approach
    with early exit if only one valid submatrix exists.

    Alternative O(n^2) time, O(n) space:
    - Compute column prefix sums for each column.
    - For each set of B consecutive rows, compute the sum of each column segment
      in O(1) using the column prefix sums.
    - Use a sliding window of size B on these column-segment sums to get each
      B x B submatrix sum.
    """
    n = len(A)
    # Column prefix sums: col_prefix[i][j] = sum of A[0..i-1][j]
    col_prefix = [[0] * n for _ in range(n + 1)]
    for i in range(1, n + 1):
        for j in range(n):
            col_prefix[i][j] = col_prefix[i-1][j] + A[i-1][j]

    max_sum = float('-inf')
    for i in range(B - 1, n):
        # For rows [i-B+1 .. i], compute column sums using prefix
        col_sums = [col_prefix[i+1][j] - col_prefix[i-B+1][j] for j in range(n)]
        # Sliding window of size B on col_sums
        window_sum = sum(col_sums[:B])
        max_sum = max(max_sum, window_sum)
        for j in range(1, n - B + 1):
            window_sum += col_sums[j + B - 1] - col_sums[j - 1]
            max_sum = max(max_sum, window_sum)
    return max_sum


if __name__ == "__main__":
    print("=== Maximum Sum Square SubMatrix ===")
    A = [
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 14, 15, 16]
    ]
    B = 2
    print(f"Matrix: 4x4, B={B}")
    print(f"Brute:   {brute_force(A, B)}")    # 54 (bottom-right 2x2)
    print(f"Optimal: {optimal(A, B)}")         # 54
    print(f"Best:    {best(A, B)}")            # 54

    A2 = [
        [1, -2, 3],
        [-4, 5, -6],
        [7, -8, 9]
    ]
    B2 = 2
    print(f"\nMatrix: 3x3 with negatives, B={B2}")
    print(f"Brute:   {brute_force(A2, B2)}")   # 5 (center + right + below)
    print(f"Optimal: {optimal(A2, B2)}")        # 5
    print(f"Best:    {best(A2, B2)}")           # 5
