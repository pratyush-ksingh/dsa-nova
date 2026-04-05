"""
Problem: Paint House
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

There are n houses in a row. Each house can be painted with one of 3 colors:
red, blue, green. The cost of painting each house with a certain color is
given by an n x 3 cost matrix. No two adjacent houses can have the same color.
Find the minimum total cost.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE — Recursion (try all combos)
# Time: O(3 * 2^n)  |  Space: O(n) recursion stack
# ============================================================
def brute_force(costs: List[List[int]]) -> int:
    """
    Try every valid color assignment recursively.
    At each house, pick a color different from the previous house.
    """
    if not costs:
        return 0
    n = len(costs)

    def helper(house, last_color):
        if house == n:
            return 0
        min_cost = float('inf')
        for color in range(3):
            if color != last_color:
                min_cost = min(min_cost, costs[house][color] + helper(house + 1, color))
        return min_cost

    return helper(0, -1)


# ============================================================
# APPROACH 2: OPTIMAL — DP with O(n) space
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(costs: List[List[int]]) -> int:
    """
    dp[i][c] = min cost to paint houses 0..i where house i is color c.
    dp[i][c] = costs[i][c] + min(dp[i-1][other colors]).
    """
    if not costs:
        return 0
    n = len(costs)
    dp = [[0] * 3 for _ in range(n)]
    dp[0] = costs[0][:]

    for i in range(1, n):
        dp[i][0] = costs[i][0] + min(dp[i - 1][1], dp[i - 1][2])
        dp[i][1] = costs[i][1] + min(dp[i - 1][0], dp[i - 1][2])
        dp[i][2] = costs[i][2] + min(dp[i - 1][0], dp[i - 1][1])

    return min(dp[n - 1])


# ============================================================
# APPROACH 3: BEST — DP with O(1) space
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(costs: List[List[int]]) -> int:
    """
    Only keep the previous row's costs. Update in place with 3 variables.
    """
    if not costs:
        return 0

    prev_r, prev_b, prev_g = costs[0]

    for i in range(1, len(costs)):
        cur_r = costs[i][0] + min(prev_b, prev_g)
        cur_b = costs[i][1] + min(prev_r, prev_g)
        cur_g = costs[i][2] + min(prev_r, prev_b)
        prev_r, prev_b, prev_g = cur_r, cur_b, cur_g

    return min(prev_r, prev_b, prev_g)


if __name__ == "__main__":
    print("=== Paint House ===")

    costs = [[17, 2, 17], [16, 16, 5], [14, 3, 19]]
    print(f"Brute:   {brute_force(costs)}")   # 10
    print(f"Optimal: {optimal(costs)}")       # 10
    print(f"Best:    {best(costs)}")          # 10

    costs = [[7, 6, 2]]
    print(f"Brute:   {brute_force(costs)}")   # 2
    print(f"Optimal: {optimal(costs)}")       # 2
    print(f"Best:    {best(costs)}")          # 2
