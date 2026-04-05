"""
Problem: Egg Drop Problem
Difficulty: HARD | XP: 50
Source: InterviewBit

Given N eggs and K floors, find the minimum number of trials
needed in the worst case to determine the critical floor
(highest floor from which egg doesn't break).
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE - Classic DP O(n*k^2)
# Time: O(n * k^2)  |  Space: O(n * k)
# ============================================================
def brute_force(n: int, k: int) -> int:
    # dp[i][j] = min trials with i eggs, j floors
    dp = [[0]*(k+1) for _ in range(n+1)]
    for j in range(k+1):
        dp[1][j] = j   # 1 egg: must try every floor
    for i in range(1, n+1):
        dp[i][0] = 0
        dp[i][1] = 1

    for i in range(2, n+1):
        for j in range(2, k+1):
            dp[i][j] = float('inf')
            for x in range(1, j+1):
                worst = 1 + max(dp[i-1][x-1], dp[i][j-x])
                dp[i][j] = min(dp[i][j], worst)
    return dp[n][k]


# ============================================================
# APPROACH 2: OPTIMAL - DP with binary search per state
# Time: O(n * k * log k)  |  Space: O(n * k)
# ============================================================
# As trial floor x increases, dp[i-1][x-1] increases and
# dp[i][j-x] decreases. The optimal x lies at their crossing.
# Binary search finds that x in O(log k).
# ============================================================
def optimal(n: int, k: int) -> int:
    dp = [[0]*(k+1) for _ in range(n+1)]
    for j in range(k+1): dp[1][j] = j
    for i in range(1, n+1): dp[i][0] = 0; dp[i][1] = 1

    for i in range(2, n+1):
        for j in range(2, k+1):
            lo, hi, best = 1, j, float('inf')
            while lo <= hi:
                mid = (lo + hi) // 2
                break_case   = dp[i-1][mid-1]  # egg breaks
                survive_case = dp[i][j-mid]    # egg survives
                worst = 1 + max(break_case, survive_case)
                best = min(best, worst)
                if break_case < survive_case:
                    lo = mid + 1
                else:
                    hi = mid - 1
            dp[i][j] = best
    return dp[n][k]


# ============================================================
# APPROACH 3: BEST - Reverse DP: floors reachable in t trials
# Time: O(n * k)  |  Space: O(n)
# ============================================================
# f(t, eggs) = max floors testable with 'eggs' eggs in 't' trials
# Recurrence: f(t,e) = f(t-1,e-1) + f(t-1,e) + 1
# Increment t until f(t,n) >= k.
# Space-optimized to O(n) rolling array.
# Real-life use: minimum probe count for black-box QA testing,
# bisection under physical resource constraints (crash tests).
# ============================================================
def best(n: int, k: int) -> int:
    # f[j] = max floors testable with j eggs and current t trials
    f = [0] * (n + 1)
    t = 0
    while f[n] < k:
        t += 1
        nf = [0] * (n + 1)
        for j in range(1, n + 1):
            nf[j] = f[j-1] + f[j] + 1
        f = nf
    return t


if __name__ == "__main__":
    print("=== Egg Drop Problem ===")

    print(f"n=2, k=100 Brute   (expect 14): {brute_force(2, 100)}")
    print(f"n=2, k=100 Optimal (expect 14): {optimal(2, 100)}")
    print(f"n=2, k=100 Best    (expect 14): {best(2, 100)}")

    print(f"n=1, k=6   Best    (expect 6):  {best(1, 6)}")
    print(f"n=2, k=1   Best    (expect 1):  {best(2, 1)}")

    b, op, bst = brute_force(3, 14), optimal(3, 14), best(3, 14)
    print(f"n=3, k=14  all match? {b==op==bst} val={b}")

    print(f"n=10, k=1000 Best:  {best(10, 1000)}")
