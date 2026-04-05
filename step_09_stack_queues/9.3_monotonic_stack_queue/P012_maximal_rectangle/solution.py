"""
Problem: Maximal Rectangle
Difficulty: HARD | XP: 50

Given a binary matrix of '0' and '1', find the largest rectangle
containing only '1's and return its area.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Prefix sums + try all sub-rectangles
# Time: O(M^2 * N^2)  |  Space: O(MN)
# ============================================================
def brute_force(matrix: List[List[str]]) -> int:
    """Use row prefix sums to compute rectangle area for all (r1,r2,c2) combos."""
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    # rs[i][j] = consecutive 1s ending at column j in row i
    rs = [[0]*n for _ in range(m)]
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == '1':
                rs[i][j] = (rs[i][j-1] + 1) if j > 0 else 1

    max_area = 0
    for r2 in range(m):
        for c2 in range(n):
            min_w = rs[r2][c2]
            for r1 in range(r2, -1, -1):
                min_w = min(min_w, rs[r1][c2])
                max_area = max(max_area, min_w * (r2 - r1 + 1))
    return max_area


# ============================================================
# APPROACH 2: OPTIMAL - Histogram per row + largest rect in histogram
# Time: O(M * N)  |  Space: O(N)
# Build cumulative height histogram row by row; apply stack-based O(N) solution.
# ============================================================
def optimal(matrix: List[List[str]]) -> int:
    """Row-by-row histogram approach with monotonic stack."""
    if not matrix or not matrix[0]:
        return 0

    def largest_rect(heights: List[int]) -> int:
        stack, max_a = [], 0
        for i, h in enumerate(heights + [0]):
            while stack and heights[stack[-1]] > h:
                ht = heights[stack.pop()]
                w = i if not stack else i - stack[-1] - 1
                max_a = max(max_a, ht * w)
            stack.append(i)
        return max_a

    m, n = len(matrix), len(matrix[0])
    heights = [0] * n
    max_area = 0
    for i in range(m):
        heights = [heights[j] + 1 if matrix[i][j] == '1' else 0 for j in range(n)]
        max_area = max(max_area, largest_rect(heights))
    return max_area


# ============================================================
# APPROACH 3: BEST - DP with height/left/right arrays
# Time: O(M * N)  |  Space: O(N)
# height[j] = consecutive 1s above row i at column j.
# left[j]   = leftmost column from which height[j] is continuous.
# right[j]  = rightmost column (exclusive) same.
# area = height[j] * (right[j] - left[j])
# ============================================================
def best(matrix: List[List[str]]) -> int:
    """DP: maintain height, left boundary, right boundary arrays."""
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    height = [0] * n
    left = [0] * n
    right = [n] * n
    max_area = 0

    for i in range(m):
        # Update height
        for j in range(n):
            height[j] = height[j] + 1 if matrix[i][j] == '1' else 0

        # Update left
        cur_left = 0
        for j in range(n):
            if matrix[i][j] == '1':
                left[j] = max(left[j], cur_left)
            else:
                left[j] = 0
                cur_left = j + 1

        # Update right
        cur_right = n
        for j in range(n - 1, -1, -1):
            if matrix[i][j] == '1':
                right[j] = min(right[j], cur_right)
            else:
                right[j] = n
                cur_right = j

        # Compute area
        for j in range(n):
            max_area = max(max_area, height[j] * (right[j] - left[j]))

    return max_area


if __name__ == "__main__":
    print("=== Maximal Rectangle ===")
    tests = [
        ([['1','0','1','0','0'],
          ['1','0','1','1','1'],
          ['1','1','1','1','1'],
          ['1','0','0','1','0']], 6),
        ([['0']], 0),
        ([['1']], 1),
        ([['1','1','1','1'],['1','1','1','1']], 8),
    ]
    for mat, exp in tests:
        b = brute_force(mat)
        o = optimal(mat)
        be = best(mat)
        status = "OK" if b == o == be == exp else "FAIL"
        print(f"brute={b} opt={o} best={be} (exp={exp}) [{status}]")
