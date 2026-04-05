"""
Problem: Ninja's Training (2D DP)
Difficulty: MEDIUM | XP: 25

N days, 3 activities per day. Can't repeat same activity on consecutive days.
Maximize total merit points.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""
from typing import List


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^N) | Space: O(N) recursion stack
# ============================================================
def ninja_recursive(points: List[List[int]]) -> int:
    """Try all valid combinations recursively."""
    n = len(points)

    def solve(day: int, last: int) -> int:
        # Base case: day 0 -- pick best activity that isn't 'last'
        if day == 0:
            return max(points[0][j] for j in range(3) if j != last)

        # Try all activities except 'last'
        max_val = 0
        for j in range(3):
            if j != last:
                val = points[day][j] + solve(day - 1, j)
                max_val = max(max_val, val)
        return max_val

    return solve(n - 1, 3)  # 3 = no restriction


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(N * 4 * 3) = O(N) | Space: O(N * 4) = O(N)
# ============================================================
def ninja_memo(points: List[List[int]]) -> int:
    """Cache (day, last) states to avoid recomputation."""
    n = len(points)
    dp = [[-1] * 4 for _ in range(n)]

    def solve(day: int, last: int) -> int:
        if day == 0:
            return max(points[0][j] for j in range(3) if j != last)

        if dp[day][last] != -1:
            return dp[day][last]

        max_val = 0
        for j in range(3):
            if j != last:
                val = points[day][j] + solve(day - 1, j)
                max_val = max(max_val, val)

        dp[day][last] = max_val
        return max_val

    return solve(n - 1, 3)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(N * 4 * 3) = O(N) | Space: O(N * 4) = O(N)
# ============================================================
def ninja_tab(points: List[List[int]]) -> int:
    """Build dp table iteratively from day 0."""
    n = len(points)
    dp = [[0] * 4 for _ in range(n)]

    # Base case: day 0
    dp[0][0] = max(points[0][1], points[0][2])  # can't do activity 0
    dp[0][1] = max(points[0][0], points[0][2])  # can't do activity 1
    dp[0][2] = max(points[0][0], points[0][1])  # can't do activity 2
    dp[0][3] = max(points[0])                    # no restriction

    for day in range(1, n):
        for last in range(4):
            dp[day][last] = 0
            for j in range(3):
                if j != last:
                    dp[day][last] = max(
                        dp[day][last],
                        points[day][j] + dp[day - 1][j]
                    )

    return dp[n - 1][3]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(N * 4 * 3) = O(N) | Space: O(1)
# ============================================================
def ninja_space(points: List[List[int]]) -> int:
    """Only store previous day's dp values."""
    n = len(points)

    # Base case: day 0
    prev = [0] * 4
    prev[0] = max(points[0][1], points[0][2])
    prev[1] = max(points[0][0], points[0][2])
    prev[2] = max(points[0][0], points[0][1])
    prev[3] = max(points[0])

    for day in range(1, n):
        curr = [0] * 4
        for last in range(4):
            for j in range(3):
                if j != last:
                    curr[last] = max(curr[last], points[day][j] + prev[j])
        prev = curr

    return prev[3]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Ninja's Training (2D DP) ===\n")

    test_cases = [
        ([[10, 40, 70], [20, 50, 80], [30, 60, 90]], 210),
        ([[10, 50, 1], [5, 100, 11]], 110),
        ([[18, 11, 28], [72, 13, 91], [55, 12, 99], [64, 80, 78], [22, 77, 44]], 345),
        ([[10, 20, 30]], 30),
        ([[100, 1, 1], [1, 1, 100]], 200),
    ]

    for points, expected in test_cases:
        r = ninja_recursive(points)
        m = ninja_memo(points)
        t = ninja_tab(points)
        s = ninja_space(points)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"points = {points}")
        print(f"  Rec: {r} | Memo: {m} | Tab: {t} | Space: {s}")
        print(f"  Expected: {expected} | Pass: {passes}\n")
