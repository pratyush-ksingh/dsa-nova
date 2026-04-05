"""
Problem: Tushar's Birthday Bombs
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Tushar has a strength A (total kicks he can absorb).
There are N friends, each with a kick power B[i].
Maximize total kicks (sum of chosen kick costs <= A).
Among all sequences with the same maximum kicks, return the
lexicographically smallest sequence (1-indexed friend numbers).

Key insight:
  - The friend with minimum cost determines max kicks = A // minCost.
  - For lex order: at each position, try friends in ascending index order;
    pick the first friend i where B[i] <= budget - (remaining * minCost).
    This guarantees we can still fill all remaining positions.

Real-life analogy: allocating tasks to team members where each person has
a different workload cost, maximizing throughput while preferring senior
members (lower index = higher seniority).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (All min-cost friend, no lex optimization)
# Time: O(n + kicks)  |  Space: O(kicks)
# ============================================================
# Simply find the cheapest friend and fill all positions with them.
# This gives max kicks but NOT the correct lex order when a lower-indexed
# friend (with higher cost) could fill some earlier positions.
def brute_force(A: int, B: List[int]) -> List[int]:
    if not B:
        return []
    min_cost = min(B)
    min_idx = B.index(min_cost) + 1  # 1-indexed
    kicks = A // min_cost
    return [min_idx] * kicks


# ============================================================
# APPROACH 2: OPTIMAL (Greedy with budget awareness for lex order)
# Time: O(kicks * n)  |  Space: O(kicks)
# ============================================================
# For each of the `kicks` positions:
#   remaining = kicks - 1 - pos  (positions still to fill after this)
#   Pick the smallest-indexed friend i such that:
#       B[i] <= budget - remaining * minCost
#   This ensures remaining slots can still be filled at minCost each.
def optimal(A: int, B: List[int]) -> List[int]:
    if not B:
        return []
    n = len(B)
    min_cost = min(B)
    kicks = A // min_cost

    result = []
    budget = A

    for pos in range(kicks):
        remaining = kicks - 1 - pos
        for i in range(n):
            if B[i] <= budget - remaining * min_cost:
                result.append(i + 1)   # 1-indexed
                budget -= B[i]
                break

    return result


# ============================================================
# APPROACH 3: BEST (Same greedy, cleaner implementation)
# Time: O(kicks * n)  |  Space: O(kicks)
# ============================================================
# Identical logic to Optimal but written more cleanly.
# The inner loop exits as soon as the first valid friend is found.
# In practice, for most positions the cheapest friend is chosen in O(1)
# (constant-time scan), giving nearly O(kicks) overall.
def best(A: int, B: List[int]) -> List[int]:
    if not B:
        return []
    n = len(B)
    min_cost = min(B)
    kicks = A // min_cost
    if kicks == 0:
        return []

    result = []
    budget = A

    for pos in range(kicks):
        remaining = kicks - 1 - pos
        threshold = budget - remaining * min_cost
        chosen = next(i for i in range(n) if B[i] <= threshold)
        result.append(chosen + 1)   # 1-indexed
        budget -= B[chosen]

    return result


if __name__ == "__main__":
    print("=== Tushar's Birthday Bombs ===")
    # Test 1: A=11, B=[2,3,4] -> maxKicks=5, all friend 1 -> [1,1,1,1,1]
    # Test 2: A=6,  B=[3,2]   -> maxKicks=3, all friend 2 -> [2,2,2]
    # Test 3: A=5,  B=[4,2,3] -> maxKicks=2, all friend 2 -> [2,2]
    # Test 4: A=7,  B=[2,3]   -> maxKicks=3, lex: try friend1(2)
    #   pos=0: budget=7,remaining=2 -> 7-2*2=3, friend1(2)<=3 yes -> pick 1, budget=5
    #   pos=1: budget=5,remaining=1 -> 5-1*2=3, friend1(2)<=3 yes -> pick 1, budget=3
    #   pos=2: budget=3,remaining=0 -> 3,       friend1(2)<=3 yes -> pick 1, budget=1
    #   Expected: [1,1,1]
    test_cases = [
        (11, [2, 3, 4], [1, 1, 1, 1, 1]),
        (6,  [3, 2],    [2, 2, 2]),
        (5,  [4, 2, 3], [2, 2]),
        (7,  [2, 3],    [1, 1, 1]),
    ]

    for A, B, expected in test_cases:
        bf = brute_force(A, B)
        op = optimal(A, B)
        be = best(A, B)
        status_op = "PASS" if op == expected else "FAIL"
        status_be = "PASS" if be == expected else "FAIL"
        print(f"A={A}, B={B}")
        print(f"  Brute (no lex): {bf}")
        print(f"  Optimal [{status_op}]:  {op}  (Expected: {expected})")
        print(f"  Best    [{status_be}]:  {be}")
        print()
