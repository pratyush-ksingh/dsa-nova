"""
Problem: Longest Arithmetic Progression
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a sorted array A, find the length of the longest subsequence
that forms an Arithmetic Progression (AP).

DP approach:
  dp[j][diff] = length of longest AP ending at index j with common difference diff.
  For each pair (i, j) where i < j:
    diff = A[j] - A[i]
    dp[j][diff] = dp[i].get(diff, 1) + 1
  Answer = max over all dp values, minimum 2 (any two elements form an AP).

Real-life analogy: finding evenly-spaced data points in a time series,
like sensor readings at regular intervals.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (O(n^3) exhaustive)
# Time: O(n^3)  |  Space: O(1)
# ============================================================
# For every pair (i, j) as the first two AP terms, scan right
# to greedily extend the AP. Track maximum length found.
def brute_force(A: List[int]) -> int:
    n = len(A)
    if n <= 2:
        return n

    best = 2
    for i in range(n - 1):
        for j in range(i + 1, n):
            diff = A[j] - A[i]
            length = 2
            last = A[j]
            for k in range(j + 1, n):
                if A[k] - last == diff:
                    length += 1
                    last = A[k]
            best = max(best, length)
    return best


# ============================================================
# APPROACH 2: OPTIMAL (DP with dict per index)
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
# dp[j] maps: diff -> length of longest AP ending at j with that diff.
# For every pair (i, j), diff = A[j]-A[i]; extend AP from i.
def optimal(A: List[int]) -> int:
    n = len(A)
    if n <= 2:
        return n

    # dp[j] = {diff: max_length_of_AP_ending_at_j_with_this_diff}
    dp = [dict() for _ in range(n)]
    best = 2

    for j in range(1, n):
        for i in range(j):
            diff = A[j] - A[i]
            # AP ending at i with diff has length dp[i].get(diff, 1)
            # (1 because A[i] alone counts as length-1 start)
            length = dp[i].get(diff, 1) + 1
            dp[j][diff] = max(dp[j].get(diff, 0), length)
            best = max(best, dp[j][diff])

    return best


# ============================================================
# APPROACH 3: BEST (DP with 2D table + value map)
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
# dp[i][j] = length of longest AP whose last two elements are A[j] and A[i] (j < i).
# For each pair (j, i), compute diff = A[i]-A[j] and look up if A[j]-diff exists.
# If it does at index k < j: dp[j][i] = dp[k][j] + 1, else dp[j][i] = 2.
def best(A: List[int]) -> int:
    n = len(A)
    if n <= 2:
        return n

    # dp[i][j] = length of AP ending with ...A[j], A[i]  (j < i)
    dp = [[2] * n for _ in range(n)]
    index_map = {val: idx for idx, val in enumerate(A)}
    best_len = 2

    for i in range(1, n):
        for j in range(i):
            diff = A[i] - A[j]
            prev_val = A[j] - diff
            k = index_map.get(prev_val, -1)
            if k != -1 and k < j:
                dp[j][i] = dp[k][j] + 1
            best_len = max(best_len, dp[j][i])

    return best_len


if __name__ == "__main__":
    print("=== Longest Arithmetic Progression ===")
    test_cases = [
        ([1, 7, 10, 13, 14, 19], 4),  # AP: 1,7,13,19
        ([1, 2, 3],              3),  # AP: 1,2,3
        ([3, 6, 9, 12],          4),  # AP: full array
        ([1, 2, 4, 5, 7, 8, 10], 4), # AP: 1,4,7,10 or 2,5,8
        ([2, 4, 6, 8, 10],       5), # AP: full array
    ]

    for A, expected in test_cases:
        A_sorted = sorted(A)
        bf = brute_force(A_sorted)
        op = optimal(A_sorted)
        be = best(A_sorted)
        status = "PASS" if bf == op == be == expected else "FAIL"
        print(f"[{status}] A={A_sorted} | Brute: {bf} | Optimal: {op} | Best: {be} | Expected: {expected}")
