"""
Problem: Seats
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

A row of seats as string of 'x' (person) and '.' (empty).
Minimize total moves to group all people consecutively.
One move = shift one person by one position. Return answer % 10^7.

Key insight: optimal gathering point minimizes sum of |pos[i] - target[i]|.
Normalize positions: adj[i] = pos[i] - i removes natural gap between neighbors.
Optimal meeting point = median of adj[], cost = sum |adj[i] - median|.
"""
from typing import List

MOD = 10**7


# ============================================================
# APPROACH 1: BRUTE FORCE - Try all contiguous block placements
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(s: str) -> int:
    """Try every possible starting position of the k-person block."""
    pos = [i for i, c in enumerate(s) if c == 'x']
    k = len(pos)
    if k <= 1:
        return 0
    n = len(s)
    min_cost = float('inf')
    for start in range(n - k + 1):
        cost = sum(abs(pos[i] - (start + i)) for i in range(k))
        min_cost = min(min_cost, cost)
    return min_cost % MOD


# ============================================================
# APPROACH 2: OPTIMAL - Median trick with index normalization
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(s: str) -> int:
    """
    Normalize: adj[i] = pos[i] - i.
    This converts the problem to: minimize sum |adj[i] - c| for some constant c.
    Solution: c = median(adj), cost = sum of absolute deviations from median.
    """
    pos = [i for i, c in enumerate(s) if c == 'x']
    k = len(pos)
    if k <= 1:
        return 0
    adj = [p - i for i, p in enumerate(pos)]
    median = adj[k // 2]
    cost = sum(abs(a - median) for a in adj)
    return cost % MOD


# ============================================================
# APPROACH 3: BEST - Prefix sum for even faster absolute deviation sum
# Time: O(N)  |  Space: O(N)
# For sorted adj[], split at median: left sum and right sum via prefix sums.
# Avoids recomputing abs per element in large arrays.
# ============================================================
def best(s: str) -> int:
    """
    Using prefix sums on normalized positions to compute
    sum of absolute deviations from median in O(N).
    """
    pos = [i for i, c in enumerate(s) if c == 'x']
    k = len(pos)
    if k <= 1:
        return 0
    adj = [p - i for i, p in enumerate(pos)]  # already sorted
    # Prefix sum
    prefix = [0] * (k + 1)
    for i in range(k):
        prefix[i + 1] = prefix[i] + adj[i]
    # Cost for median index m = k//2
    m = k // 2
    median = adj[m]
    # Left part cost: median*m - prefix[m]
    left_cost = median * m - prefix[m]
    # Right part cost: (prefix[k] - prefix[m+1]) - median*(k-m-1)
    right_cost = (prefix[k] - prefix[m + 1]) - median * (k - m - 1)
    return (left_cost + right_cost) % MOD


if __name__ == "__main__":
    print("=== Seats ===")
    tests = [
        ("....x..xx...x..", 5),
        ("...xxx...", 0),
        ("x.x.x", 2),
        ("xx..x", 2),
        ("x", 0),
        ("x.x", 1),
    ]
    for s, exp in tests:
        b = brute_force(s)
        o = optimal(s)
        be = best(s)
        status = "OK" if b == o == be == exp else f"FAIL(b={b},o={o},be={be})"
        print(f"s={s!r}: {b}|{o}|{be} (exp={exp}) [{status}]")
