"""
Problem: Flip Array
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a positive integer array, negate some elements to minimize |sum|.
Find the MINIMUM number of elements to negate to make the array sum closest to 0.

Approach: Subset sum DP — find minimum-size subset that sums to <= totalSum/2.
Negating that subset minimizes the absolute difference.
"""

from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Try all 2^N subsets of elements to negate; track min |sum| and min flips
# Time: O(2^N)  |  Space: O(N) recursion stack
# ============================================================
def brute_force(A: List[int]) -> int:
    n = len(A)
    total_sum = sum(A)
    best = [float('inf'), n + 1]  # [min_diff, min_flips]

    def solve(idx, curr_sum, flips):
        if idx == n:
            diff = abs(total_sum - 2 * curr_sum)
            if diff < best[0] or (diff == best[0] and flips < best[1]):
                best[0] = diff
                best[1] = flips
            return
        solve(idx + 1, curr_sum, flips)              # don't negate
        solve(idx + 1, curr_sum + A[idx], flips + 1) # negate A[idx]

    solve(0, 0, 0)
    return best[1]


# ============================================================
# APPROACH 2: OPTIMAL
# 2D DP: dp[k][s] = True if we can select exactly k elements summing to s
# Find minimum k where dp[k][s] is True for some s in [0..totalSum/2]
# Time: O(N^2 * S)  |  Space: O(N * S)
# ============================================================
def optimal(A: List[int]) -> int:
    n = len(A)
    total_sum = sum(A)
    target = total_sum // 2

    # dp[k][s] = can we pick k elements summing to s?
    dp = [[False] * (target + 1) for _ in range(n + 1)]
    dp[0][0] = True

    for x in A:
        # Reverse traversal for 0/1 knapsack
        for k in range(n - 1, -1, -1):
            for s in range(target, x - 1, -1):
                if dp[k][s - x]:
                    dp[k + 1][s] = True

    # Find minimum k
    for k in range(n + 1):
        for s in range(target, -1, -1):
            if dp[k][s]:
                return k

    return n


# ============================================================
# APPROACH 3: BEST
# Same DP but using a set of achievable (count, sum) pairs — more memory efficient
# Time: O(N * S * N)  |  Space: O(N * S) — same asymptotic but cleaner code
# ============================================================
def best(A: List[int]) -> int:
    n = len(A)
    total_sum = sum(A)
    target = total_sum // 2

    # dp[s] = minimum elements needed to achieve negated-subset sum of s
    dp = [float('inf')] * (target + 1)
    dp[0] = 0

    for x in A:
        for s in range(target, x - 1, -1):
            if dp[s - x] < float('inf'):
                dp[s] = min(dp[s], dp[s - x] + 1)

    # Find minimum flips for best achievable sum
    return min(dp[s] for s in range(target + 1) if dp[s] < float('inf'))


if __name__ == "__main__":
    print("=== Flip Array ===")

    # [15,10,6]: totalSum=31. Negate {15}=15: |31-30|=1, 1 flip. Negate {10,6}=16: same diff, 2 flips => 1
    A1 = [15, 10, 6]
    print(f"BruteForce [15,10,6]: {brute_force(A1)}")  # 1
    print(f"Optimal    [15,10,6]: {optimal(A1)}")       # 1
    print(f"Best       [15,10,6]: {best(A1)}")          # 1

    # [1,2,3,4]: totalSum=10. Negate {1,4}=5: sum=0, 2 flips
    A2 = [1, 2, 3, 4]
    print(f"BruteForce [1,2,3,4]: {brute_force(A2)}")  # 2
    print(f"Optimal    [1,2,3,4]: {optimal(A2)}")       # 2
    print(f"Best       [1,2,3,4]: {best(A2)}")          # 2

    # [1]: diff=1 regardless; 0 flips to achieve |1|=1 (don't negate) == negate gives |-1|=1
    # prefer fewer flips: 0
    A3 = [1]
    print(f"BruteForce [1]: {brute_force(A3)}")  # 0
    print(f"Optimal    [1]: {optimal(A3)}")       # 0
    print(f"Best       [1]: {best(A3)}")          # 0

    # [100]: don't negate: |100|=100. Negate: |-100|=100. Prefer 0 flips.
    A4 = [100]
    print(f"[100]: {best(A4)}")  # 0
