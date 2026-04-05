"""
Problem: Chain of Pairs
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given N pairs, find the length of the longest chain where for consecutive
pairs (a,b) and (c,d): b < c.
Classic LIS variant on pairs.
"""

from typing import List
import bisect


# ============================================================
# APPROACH 1: BRUTE FORCE
# O(N^2) DP after sorting by first element
# dp[i] = length of longest chain ending at pair i
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(pairs: List[List[int]]) -> int:
    pairs = sorted(pairs, key=lambda p: p[0])
    n = len(pairs)
    dp = [1] * n
    ans = 1

    for i in range(1, n):
        for j in range(i):
            if pairs[j][1] < pairs[i][0]:
                dp[i] = max(dp[i], dp[j] + 1)
        ans = max(ans, dp[i])

    return ans


# ============================================================
# APPROACH 2: OPTIMAL
# Greedy: sort by second element; greedily pick pair with smallest end
# (Activity Selection Problem style)
# Time: O(N log N)  |  Space: O(1)
# ============================================================
def optimal(pairs: List[List[int]]) -> int:
    pairs = sorted(pairs, key=lambda p: p[1])
    count = 1
    prev_end = pairs[0][1]

    for a, b in pairs[1:]:
        if a > prev_end:
            count += 1
            prev_end = b

    return count


# ============================================================
# APPROACH 3: BEST
# Binary search LIS (patience sorting) on second elements
# Sort by first element; find LIS where next pair's first > prev pair's second
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def best(pairs: List[List[int]]) -> int:
    pairs = sorted(pairs, key=lambda p: p[0])
    tails = []  # tails[i] = minimum second-element of chains of length i+1

    for a, b in pairs:
        # Find leftmost tail >= a (we need previous end strictly < a)
        pos = bisect.bisect_left(tails, a)
        if pos == len(tails):
            tails.append(b)
        else:
            tails[pos] = min(tails[pos], b)

    return len(tails)


if __name__ == "__main__":
    print("=== Chain of Pairs ===")

    p1 = [[1, 2], [2, 3], [3, 4]]
    print(f"BruteForce p1: {brute_force(p1)}")  # 2
    print(f"Optimal    p1: {optimal(p1)}")       # 2
    print(f"Best       p1: {best(p1)}")          # 2

    p2 = [[5, 24], [15, 25], [27, 40], [50, 60]]
    print(f"BruteForce p2: {brute_force(p2)}")  # 3
    print(f"Optimal    p2: {optimal(p2)}")       # 3
    print(f"Best       p2: {best(p2)}")          # 3

    p3 = [[1, 10], [2, 3], [3, 7], [4, 5]]
    print(f"Optimal p3: {optimal(p3)}")  # 2
