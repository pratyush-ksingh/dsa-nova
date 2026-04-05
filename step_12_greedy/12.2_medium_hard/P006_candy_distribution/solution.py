"""
Problem: Candy Distribution
Difficulty: HARD | XP: 50

N children in a row with ratings. Rules:
1. Each child gets at least 1 candy.
2. A child with higher rating than a neighbor gets more candies than that neighbor.
Return the minimum total candies needed.
Real-life use: Fair resource allocation, performance-based reward systems.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Repeatedly scan until stable: enforce both neighbor constraints.
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(ratings: List[int]) -> int:
    n = len(ratings)
    candies = [1] * n
    changed = True
    while changed:
        changed = False
        for i in range(n):
            if i > 0 and ratings[i] > ratings[i - 1] and candies[i] <= candies[i - 1]:
                candies[i] = candies[i - 1] + 1
                changed = True
            if i < n - 1 and ratings[i] > ratings[i + 1] and candies[i] <= candies[i + 1]:
                candies[i] = candies[i + 1] + 1
                changed = True
    return sum(candies)


# ============================================================
# APPROACH 2: OPTIMAL
# Two-pass greedy:
# Left pass: ensure left constraint satisfied.
# Right pass: ensure right constraint satisfied (take max).
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(ratings: List[int]) -> int:
    n = len(ratings)
    candies = [1] * n
    # Left to right: satisfy left neighbor constraint
    for i in range(1, n):
        if ratings[i] > ratings[i - 1]:
            candies[i] = candies[i - 1] + 1
    # Right to left: satisfy right neighbor constraint
    for i in range(n - 2, -1, -1):
        if ratings[i] > ratings[i + 1]:
            candies[i] = max(candies[i], candies[i + 1] + 1)
    return sum(candies)


# ============================================================
# APPROACH 3: BEST
# Single-pass O(1) space: track ascending (up) and descending (down) slopes.
# "Peak" is the length of the ascending run before current descent.
# When descending run >= peak, the peak child needs one more candy.
# Time: O(N)  |  Space: O(1)
# ============================================================
def best(ratings: List[int]) -> int:
    if len(ratings) == 1:
        return 1
    n = len(ratings)
    total = 1
    up = down = peak = 0
    for i in range(1, n):
        if ratings[i] > ratings[i - 1]:
            up += 1
            down = 0
            peak = up
            total += up + 1
        elif ratings[i] == ratings[i - 1]:
            up = down = peak = 0
            total += 1
        else:
            up = 0
            down += 1
            # If peak is no longer tall enough to cover the down slope, add 1 to peak
            total += down + 1 + (0 if peak >= down else 1)
    return total


if __name__ == "__main__":
    print("=== Candy Distribution ===")

    tests = [
        [1, 0, 2],              # 5
        [1, 2, 2],              # 4
        [1, 3, 2, 2, 1],        # 7
        [1, 6, 10, 8, 7, 3, 2], # 18
        [1],                    # 1
    ]
    for r in tests:
        print(f"\nratings={r}")
        print(f"  Brute  : {brute_force(r)}")
        print(f"  Optimal: {optimal(r)}")
        print(f"  Best   : {best(r)}")
