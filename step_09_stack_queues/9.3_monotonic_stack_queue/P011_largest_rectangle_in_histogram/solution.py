"""
Problem: Largest Rectangle in Histogram
Difficulty: HARD | XP: 50

Given heights of histogram bars, find the area of the largest rectangle
that fits entirely within the histogram.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Try all pairs as left/right boundaries
# Time: O(N^2)  |  Space: O(1)
# ============================================================
def brute_force(heights: List[int]) -> int:
    """For every subarray, the rectangle height is the minimum bar in range."""
    n = len(heights)
    max_area = 0
    for i in range(n):
        min_h = heights[i]
        for j in range(i, n):
            min_h = min(min_h, heights[j])
            max_area = max(max_area, min_h * (j - i + 1))
    return max_area


# ============================================================
# APPROACH 2: OPTIMAL - Monotonic Stack (single pass with sentinel)
# Time: O(N)  |  Space: O(N)
# Maintain strictly increasing stack of indices.
# When height[i] < height[top], pop and compute area.
# Width = i - stack[-1] - 1 (or i if stack empty).
# ============================================================
def optimal(heights: List[int]) -> int:
    """Single-pass monotonic stack with end sentinel."""
    stack = []
    max_area = 0
    heights = heights + [0]  # sentinel to flush remaining bars
    for i, h in enumerate(heights):
        while stack and heights[stack[-1]] > h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)
    return max_area


# ============================================================
# APPROACH 3: BEST - Precompute NSL / NSR arrays
# Time: O(N)  |  Space: O(N)
# For each bar: area = height[i] * (nsr[i] - nsl[i] - 1)
# where nsl[i] = nearest smaller to left, nsr[i] = nearest smaller to right.
# ============================================================
def best(heights: List[int]) -> int:
    """Two-pass stack: find next-smaller-left and next-smaller-right for each bar."""
    n = len(heights)
    nsl = [-1] * n   # index of nearest smaller to left
    nsr = [n] * n    # index of nearest smaller to right
    stack = []

    for i in range(n):
        while stack and heights[stack[-1]] >= heights[i]:
            stack.pop()
        nsl[i] = stack[-1] if stack else -1
        stack.append(i)

    stack.clear()
    for i in range(n - 1, -1, -1):
        while stack and heights[stack[-1]] >= heights[i]:
            stack.pop()
        nsr[i] = stack[-1] if stack else n
        stack.append(i)

    return max(heights[i] * (nsr[i] - nsl[i] - 1) for i in range(n)) if n else 0


if __name__ == "__main__":
    print("=== Largest Rectangle in Histogram ===")
    tests = [
        ([2, 1, 5, 6, 2, 3], 10),
        ([2, 4], 4),
        ([1], 1),
        ([6, 7, 5, 2, 4, 5, 9, 3], 16),
        ([0], 0),
        ([3, 3, 3], 9),
    ]
    for h, exp in tests:
        b = brute_force(h)
        o = optimal(h)
        be = best(h)
        status = "OK" if b == o == be == exp else "FAIL"
        print(f"heights={h}: brute={b} opt={o} best={be} (exp={exp}) [{status}]")
