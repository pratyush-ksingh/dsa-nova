"""
Problem: Gas Station
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

N gas stations on a circular route. gas[i] = gas available, cost[i] = gas
to travel to next station. Find starting station to complete full circuit.
Return -1 if impossible. Solution is guaranteed unique if it exists.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Try each station as starting point
# Time: O(N^2)  |  Space: O(1)
# ============================================================
def brute_force(gas: List[int], cost: List[int]) -> int:
    """Try each index as starting station; simulate the full circuit."""
    n = len(gas)
    for start in range(n):
        tank = 0
        success = True
        for i in range(n):
            idx = (start + i) % n
            tank += gas[idx] - cost[idx]
            if tank < 0:
                success = False
                break
        if success:
            return start
    return -1


# ============================================================
# APPROACH 2: OPTIMAL - Greedy single pass
# Time: O(N)  |  Space: O(1)
# If total gas >= total cost, a solution exists.
# Reset start whenever tank goes negative: can't start from any station before here.
# ============================================================
def optimal(gas: List[int], cost: List[int]) -> int:
    """
    Greedy: if running tank < 0, we can't start from any station from
    current start to i, so reset start = i+1.
    If total net gas >= 0, the final candidate start is the answer.
    """
    total = tank = 0
    start = 0
    for i in range(len(gas)):
        diff = gas[i] - cost[i]
        total += diff
        tank += diff
        if tank < 0:
            start = i + 1
            tank = 0
    return start if total >= 0 else -1


# ============================================================
# APPROACH 3: BEST - Minimum prefix sum index
# Time: O(N)  |  Space: O(1)
# The optimal start is the index immediately after where
# the cumulative sum is at its global minimum.
# ============================================================
def best(gas: List[int], cost: List[int]) -> int:
    """
    Compute net[i] = gas[i] - cost[i].
    If sum(net) < 0: impossible.
    Otherwise, start = (argmin of prefix sum + 1) % n.
    """
    n = len(gas)
    total = 0
    min_sum = float('inf')
    min_idx = -1
    prefix = 0
    for i in range(n):
        prefix += gas[i] - cost[i]
        total = prefix
        if prefix <= min_sum:
            min_sum = prefix
            min_idx = i
    if total < 0:
        return -1
    return (min_idx + 1) % n


if __name__ == "__main__":
    print("=== Gas Station ===")
    tests = [
        ([1,2,3,4,5], [3,4,5,1,2], 3),
        ([2,3,4], [3,4,3], -1),
        ([5,1,2,3,4], [4,4,1,5,1], 4),
        ([1], [1], 0),
        ([3,1,1], [1,2,2], 0),
    ]
    for gas, cost, exp in tests:
        b = brute_force(gas, cost)
        o = optimal(gas, cost)
        be = best(gas, cost)
        status = "OK" if b == o == be == exp else f"FAIL(b={b},o={o},be={be})"
        print(f"gas={gas}: {b}|{o}|{be} (exp={exp}) [{status}]")
