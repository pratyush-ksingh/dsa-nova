"""
Problem: Min Steps in Infinite Grid
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a sequence of points on a 2D grid, find the minimum number of steps
to visit all points in order. Movement allowed in 8 directions.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Simulate Step-by-Step)
# Time: O(sum of distances) | Space: O(1)
# Too slow for large coordinates but demonstrates the idea.
# ============================================================
def brute_force(X: List[int], Y: List[int]) -> int:
    """Simulate moving one step at a time toward each target."""
    total = 0
    for i in range(len(X) - 1):
        cx, cy = X[i], Y[i]
        tx, ty = X[i + 1], Y[i + 1]
        while cx != tx or cy != ty:
            if cx < tx:
                cx += 1
            elif cx > tx:
                cx -= 1
            if cy < ty:
                cy += 1
            elif cy > ty:
                cy -= 1
            total += 1
    return total


# ============================================================
# APPROACH 2: OPTIMAL (Chebyshev Distance Formula)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(X: List[int], Y: List[int]) -> int:
    """Sum of Chebyshev distances: max(|dx|, |dy|) for each consecutive pair."""
    total = 0
    for i in range(len(X) - 1):
        dx = abs(X[i + 1] - X[i])
        dy = abs(Y[i + 1] - Y[i])
        total += max(dx, dy)
    return total


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Min Steps in Infinite Grid ===\n")

    test_cases = [
        ([0, 1, 1], [0, 1, 5], 5),
        ([0, 3], [0, 4], 4),
        ([0, 0], [0, 0], 0),
        ([0], [0], 0),
        ([-3, 3], [-3, 3], 6),
        ([0, 5, 5, 0], [0, 0, 5, 5], 15),
    ]

    for X, Y, expected in test_cases:
        b = brute_force(X, Y)
        o = optimal(X, Y)
        status = "PASS" if b == expected and o == expected else "FAIL"
        print(f"X={X}, Y={Y}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Expected: {expected}  [{status}]\n")
