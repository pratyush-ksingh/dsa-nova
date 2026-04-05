"""Problem: Points on Straight Line
Difficulty: HARD | XP: 50
Source: InterviewBit

Given n points on a 2D plane, find the maximum number of points
that lie on the same straight line.
"""
from typing import List
from math import gcd
from collections import defaultdict

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^3)  |  Space: O(1)
# Check every triple of points for collinearity
# ============================================================
def brute_force(x: List[int], y: List[int]) -> int:
    n = len(x)
    if n <= 2:
        return n
    max_pts = 2
    for i in range(n):
        for j in range(i + 1, n):
            count = 2
            for k in range(j + 1, n):
                # Collinear: (y[j]-y[i])*(x[k]-x[i]) == (y[k]-y[i])*(x[j]-x[i])
                cross = (y[j] - y[i]) * (x[k] - x[i]) - (y[k] - y[i]) * (x[j] - x[i])
                if cross == 0:
                    count += 1
            max_pts = max(max_pts, count)
    return max_pts


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n^2)  |  Space: O(n)
# Fix each point; compute reduced slope fraction for all others
# Use HashMap to count points sharing the same slope
# ============================================================
def optimal(x: List[int], y: List[int]) -> int:
    n = len(x)
    if n <= 2:
        return n
    max_pts = 1
    for i in range(n):
        slope_map = defaultdict(int)
        dups = 0
        local_max = 0
        for j in range(i + 1, n):
            dx = x[j] - x[i]
            dy = y[j] - y[i]
            if dx == 0 and dy == 0:
                dups += 1
                continue
            g = gcd(abs(dx), abs(dy))
            dx //= g
            dy //= g
            # Canonical: dx >= 0; if dx == 0 then dy = 1
            if dx < 0:
                dx, dy = -dx, -dy
            key = (dy, dx)
            slope_map[key] += 1
            local_max = max(local_max, slope_map[key])
        max_pts = max(max_pts, local_max + dups + 1)
    return max_pts


# ============================================================
# APPROACH 3: BEST
# Time: O(n^2)  |  Space: O(n)
# Same as optimal with tuple keys — Python dicts handle this
# efficiently. No string conversion needed.
# ============================================================
def best(x: List[int], y: List[int]) -> int:
    n = len(x)
    if n <= 2:
        return n
    max_pts = 1
    for i in range(n):
        slopes = defaultdict(int)
        dups = 0
        local_max = 0
        for j in range(i + 1, n):
            dx, dy = x[j] - x[i], y[j] - y[i]
            if dx == 0 and dy == 0:
                dups += 1
                continue
            g = gcd(abs(dx), abs(dy))
            dx //= g
            dy //= g
            if dx < 0:
                dx, dy = -dx, -dy
            slopes[(dy, dx)] += 1
            local_max = max(local_max, slopes[(dy, dx)])
        max_pts = max(max_pts, local_max + dups + 1)
    return max_pts


if __name__ == "__main__":
    cases = [
        ([1, 2, 3], [1, 2, 3], 3),
        ([1, 1, 2], [1, 1, 2], 3),    # duplicate point
        ([0, 1, -1, 2, -2], [0, 1, -1, 2, -2], 5),
        ([0, 0, 1], [0, 1, 1], 2),
        ([0, 1, 2, 3], [0, 0, 0, 0], 4),  # horizontal
    ]
    print("=== Points on Straight Line ===")
    for xi, yi, exp in cases:
        b = brute_force(xi[:], yi[:])
        o = optimal(xi[:], yi[:])
        bst = best(xi[:], yi[:])
        ok = "OK" if b == o == bst == exp else "MISMATCH"
        print(f"x={xi} y={yi} => BF={b} OPT={o} BEST={bst} EXP={exp} {ok}")
