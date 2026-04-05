"""
Problem: Tushar's Birthday Party
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

N friends each need A[i] calories satisfied.
M dishes with calories B[j] and cost C[j] (unlimited supply of each).
Return the minimum total cost to satisfy every friend.

Key insight: All friends share the same dish menu. Solve unbounded knapsack
(min cost to reach exactly x calories) ONCE up to max(A), then sum dp[A[i]].

Real-life use case: Resource allocation where shared resources can be reused
per user (e.g., cloud credit plans satisfying individual user quotas).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(N * max(A) * M)  |  Space: O(max(A))
# Solve unbounded knapsack separately for EACH friend.
# Correct but redundant -- recomputes the same dp table N times.
# ============================================================
def brute_force(A: List[int], B: List[int], C: List[int]) -> int:
    INF = float('inf')
    total = 0
    for cal_needed in A:
        dp = [INF] * (cal_needed + 1)
        dp[0] = 0
        for x in range(1, cal_needed + 1):
            for j in range(len(B)):
                if B[j] <= x and dp[x - B[j]] != INF:
                    dp[x] = min(dp[x], dp[x - B[j]] + C[j])
        total += dp[cal_needed] if dp[cal_needed] != INF else 0
    return total


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(max(A) * M + N)  |  Space: O(max(A))
# Compute unbounded knapsack once for all values 0..max(A).
# Each friend answered in O(1) by lookup dp[A[i]].
# ============================================================
def optimal(A: List[int], B: List[int], C: List[int]) -> int:
    INF = float('inf')
    max_cal = max(A)
    dp = [INF] * (max_cal + 1)
    dp[0] = 0
    for x in range(1, max_cal + 1):
        for j in range(len(B)):
            if B[j] <= x and dp[x - B[j]] != INF:
                dp[x] = min(dp[x], dp[x - B[j]] + C[j])
    return sum(dp[a] for a in A if dp[a] != INF)


# ============================================================
# APPROACH 3: BEST
# Time: O(max(A) * M + N)  |  Space: O(max(A))
# Same as Optimal. Explicit loop ordering (capacity outer, items inner)
# ensures every smaller sub-problem is solved before larger ones.
# Also adds guard for empty inputs.
# ============================================================
def best(A: List[int], B: List[int], C: List[int]) -> int:
    if not A or not B:
        return 0
    INF = float('inf')
    max_cal = max(A)
    dp = [INF] * (max_cal + 1)
    dp[0] = 0

    for x in range(1, max_cal + 1):
        for b, c in zip(B, C):
            if b <= x and dp[x - b] != INF:
                dp[x] = min(dp[x], dp[x - b] + c)

    return sum(dp[a] for a in A)


if __name__ == "__main__":
    print("=== Tushar's Birthday Party ===")

    # Test 1: Friends=[3,4], Dishes cal=[1,2,3] cost=[10,3,2]
    # Friend1: 3 cal -> dish3 cost=2
    # Friend2: 4 cal -> 2+2=4 cost=6
    # Total: 8
    A1, B1, C1 = [3, 4], [1, 2, 3], [10, 3, 2]
    print(f"Test 1:")
    print(f"  Brute:   {brute_force(A1, B1, C1)}")   # 8
    print(f"  Optimal: {optimal(A1, B1, C1)}")        # 8
    print(f"  Best:    {best(A1, B1, C1)}")            # 8

    # Test 2: Friend needs 4 cal, dishes=[1,3] cost=[2,5]
    # dp[4]: use 1+3 => cost 7; or 1+1+1+1 => cost 8
    A2, B2, C2 = [4], [1, 3], [2, 5]
    print(f"\nTest 2 (single friend needs 4 cal):")
    print(f"  Brute:   {brute_force(A2, B2, C2)}")    # 7
    print(f"  Optimal: {optimal(A2, B2, C2)}")         # 7
    print(f"  Best:    {best(A2, B2, C2)}")             # 7
