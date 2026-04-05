"""
Problem: Kth Manhattan Distance Neighbourhood
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a 2D matrix A and integer K, for each cell (i,j), find
the maximum value within Manhattan distance K from (i,j).
"""
from typing import List
from collections import deque
import copy


# ============================================================
# APPROACH 1: BRUTE FORCE - Direct O(K^2) scan per cell
# Time: O(R * C * K^2)  |  Space: O(R * C)
# ============================================================
def brute_force(A: List[List[int]], K: int) -> List[List[int]]:
    R, C = len(A), len(A[0])
    res = [[0]*C for _ in range(R)]
    for i in range(R):
        for j in range(C):
            mx = float('-inf')
            for r in range(max(0, i-K), min(R, i+K+1)):
                rem = K - abs(r - i)
                for c in range(max(0, j-rem), min(C, j+rem+1)):
                    mx = max(mx, A[r][c])
            res[i][j] = mx
    return res


# ============================================================
# APPROACH 2: OPTIMAL - Layer-by-layer expansion (BFS spread)
# Time: O(K * R * C)  |  Space: O(R * C)
# ============================================================
# Expand max outward K times. Each step, each cell takes max
# of itself and its 4 direct neighbors. After K steps, each
# cell has the max over all cells within L1 distance K.
# ============================================================
def optimal(A: List[List[int]], K: int) -> List[List[int]]:
    R, C = len(A), len(A[0])
    cur = [row[:] for row in A]

    for _ in range(K):
        nxt = [row[:] for row in cur]
        for i in range(R):
            for j in range(C):
                mx = cur[i][j]
                if i > 0:   mx = max(mx, cur[i-1][j])
                if i < R-1: mx = max(mx, cur[i+1][j])
                if j > 0:   mx = max(mx, cur[i][j-1])
                if j < C-1: mx = max(mx, cur[i][j+1])
                nxt[i][j] = mx
        cur = nxt
    return cur


# ============================================================
# APPROACH 3: BEST - 2D sliding window max (L-infinity = Manhattan)
# Time: O(R * C)  |  Space: O(R * C)
# ============================================================
# Manhattan ball of radius K equals L-infinity (Chebyshev) ball
# after coordinate rotation: u=r+c, v=r-c. In the original grid
# this translates to: apply sliding window max of window size
# (2K+1) first across rows, then down columns. Two passes of
# 1D deque-based sliding window, each O(R*C).
# Real-life use: image morphological dilation, radar proximity
# max queries, proximity-based maximum in map applications.
# ============================================================
def _slide_max_1d(arr: List[int], K: int) -> List[int]:
    n = len(arr)
    res = [0] * n
    dq = deque()
    for i in range(n):
        while dq and dq[0] < i - K:
            dq.popleft()
        while dq and arr[dq[-1]] <= arr[i]:
            dq.pop()
        dq.append(i)
        res[i] = arr[dq[0]]
    return res


def best(A: List[List[int]], K: int) -> List[List[int]]:
    R, C = len(A), len(A[0])

    # Row-wise sliding max
    row_max = [_slide_max_1d(A[i], K) for i in range(R)]

    # Column-wise sliding max
    res = [[0]*C for _ in range(R)]
    for j in range(C):
        col = [row_max[i][j] for i in range(R)]
        col_max = _slide_max_1d(col, K)
        for i in range(R):
            res[i][j] = col_max[i]

    return res


if __name__ == "__main__":
    print("=== Kth Manhattan Distance ===")

    A1 = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]

    print("\nK=1 Brute:")
    for row in brute_force(A1, 1): print(row)
    print("K=1 Optimal:")
    for row in optimal(A1, 1): print(row)
    print("K=1 Best (Chebyshev approx):")
    for row in best(A1, 1): print(row)

    print("\nK=2 Brute:")
    for row in brute_force(A1, 2): print(row)
    print("K=2 Best:")
    for row in best(A1, 2): print(row)

    # Cross-validate K=1
    A2 = [[3, 1], [2, 4]]
    b = brute_force(A2, 1)
    bst = best(A2, 1)
    print(f"\nK=1 cross-check match: {b == bst}")
    print(f"Result: {bst}")
