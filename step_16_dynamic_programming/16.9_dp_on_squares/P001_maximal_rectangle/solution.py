"""
Problem: Maximal Rectangle  (LeetCode 85)
Difficulty: HARD | XP: 50

Given a rows x cols binary matrix filled with '0's and '1's, find the largest
rectangle containing only 1s and return its area.

Key insight (Approach 2 & 3): For each row, build a histogram of consecutive
1s above (including the current row). The answer is the maximum rectangle in
any of these histograms. Largest Rectangle in Histogram (LeetCode 84) is the
core sub-problem, solvable in O(cols) with a monotonic stack.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(m^2 * n^2)  |  Space: O(1)
# ============================================================
def brute_force(matrix: List[List[str]]) -> int:
    """
    For every pair of rows (r1, r2) and every pair of columns (c1, c2),
    check whether the sub-rectangle defined by (r1, c1) to (r2, c2) is
    entirely 1s, then update the max area.

    Optimization: for a fixed top-left (r1, c1), extend right keeping a
    column-minimum bitmap — if any cell in a column is 0, the column
    cannot be part of a valid rectangle anchored at r1.
    Still O(m^2 * n^2) in the worst case.
    """
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    max_area = 0

    for r1 in range(m):
        for c1 in range(n):
            if matrix[r1][c1] == '0':
                continue
            # Extend downward from r1
            min_width = n   # maximum possible width
            for r2 in range(r1, m):
                # Update the maximum continuous width ending at c1 going right
                # by checking each column
                width = 0
                for c in range(c1, n):
                    if matrix[r2][c] == '1':
                        width += 1
                    else:
                        break
                min_width = min(min_width, width)
                if min_width == 0:
                    break
                height = r2 - r1 + 1
                max_area = max(max_area, height * min_width)

    return max_area


# ============================================================
# HELPER: Largest Rectangle in Histogram (monotonic stack)
# Time: O(n)  |  Space: O(n)
# ============================================================
def largest_rect_in_histogram(heights: List[int]) -> int:
    """
    Classic monotonic stack solution.
    Maintain an increasing stack of indices. When we see a bar shorter than
    the stack top, pop and compute the rectangle with that height.
    Use a sentinel 0 at the end to flush the stack.
    """
    stack = []        # stack of indices, heights are non-decreasing
    max_area = 0
    heights = heights + [0]   # sentinel

    for i, h in enumerate(heights):
        while stack and heights[stack[-1]] > h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)

    return max_area


# ============================================================
# APPROACH 2: OPTIMAL (Row-by-row histogram + monotonic stack)
# Time: O(m * n)  |  Space: O(n)
# ============================================================
def optimal(matrix: List[List[str]]) -> int:
    """
    Build a heights[] array row by row.
    heights[c] = number of consecutive 1s ending at the current row in column c.
    For each row, solve "Largest Rectangle in Histogram" on heights[] using
    a monotonic stack in O(n). Total: O(m*n).
    """
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    heights = [0] * n
    max_area = 0

    for r in range(m):
        # Update histogram heights
        for c in range(n):
            heights[c] = heights[c] + 1 if matrix[r][c] == '1' else 0

        # Find largest rectangle in this histogram
        max_area = max(max_area, largest_rect_in_histogram(heights))

    return max_area


# ============================================================
# APPROACH 3: BEST (DP on heights — same complexity, cleaner code)
# Time: O(m * n)  |  Space: O(n)
# ============================================================
def best(matrix: List[List[str]]) -> int:
    """
    Same histogram approach as Approach 2, but makes the heights[] update
    and the stack-based LRH inline for a single clean function.
    Also adds the 'left' and 'right' boundary DP variant as an alternative
    (pure DP without explicit stack) to show the width-based formulation:
      - height[c] = consecutive 1s above (and including) current row at col c
      - left[c]   = leftmost column reachable at height >= height[c]
      - right[c]  = rightmost column reachable at height >= height[c]
      - area[c]   = height[c] * (right[c] - left[c])
    """
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    height = [0] * n
    left   = [0] * n       # leftmost boundary for current height
    right  = [n] * n       # rightmost boundary (exclusive)
    max_area = 0

    for r in range(m):
        # Update height
        for c in range(n):
            height[c] = height[c] + 1 if matrix[r][c] == '1' else 0

        # Update left boundary: scan left-to-right
        cur_left = 0
        for c in range(n):
            if matrix[r][c] == '1':
                left[c] = max(left[c], cur_left)
            else:
                left[c] = 0
                cur_left = c + 1

        # Update right boundary: scan right-to-left
        cur_right = n
        for c in range(n - 1, -1, -1):
            if matrix[r][c] == '1':
                right[c] = min(right[c], cur_right)
            else:
                right[c] = n
                cur_right = c

        # Compute max area
        for c in range(n):
            max_area = max(max_area, height[c] * (right[c] - left[c]))

    return max_area


if __name__ == "__main__":
    test_cases = [
        (
            [["1","0","1","0","0"],
             ["1","0","1","1","1"],
             ["1","1","1","1","1"],
             ["1","0","0","1","0"]],
            6
        ),
        ([["0"]], 0),
        ([["1"]], 1),
        (
            [["0","1"],
             ["1","0"]],
            1
        ),
        (
            [["1","1","1","1"],
             ["1","1","1","1"],
             ["1","1","1","1"]],
            12
        ),
    ]

    print("=== Maximal Rectangle ===\n")
    for matrix, expected in test_cases:
        b  = brute_force(matrix)
        o  = optimal(matrix)
        be = best(matrix)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"matrix rows={len(matrix)} cols={len(matrix[0])}")
        print(f"  Brute:   {b}")
        print(f"  Optimal: {o}")
        print(f"  Best:    {be}")
        print(f"  Expected:{expected}  [{status}]\n")
