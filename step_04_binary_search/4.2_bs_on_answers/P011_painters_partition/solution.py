"""
Problem: Painters Partition
Difficulty: HARD | XP: 50

Given N boards and K painters, each painter paints contiguous boards.
Each unit of board takes 1 unit of time. Find the minimum time to paint all boards.
(Minimize the maximum work assigned to any single painter.)
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  (DP)
# Time: O(k * n^2)  |  Space: O(k * n)
# ============================================================
def brute_force(boards: List[int], k: int) -> int:
    """
    DP: dp[i][j] = min possible max when partitioning first j boards among i painters.
    Real-life: Task scheduling across limited workers to minimize bottleneck.
    """
    n = len(boards)
    prefix = [0] * (n + 1)
    for i in range(n):
        prefix[i + 1] = prefix[i] + boards[i]

    INF = float('inf')
    dp = [[INF] * (n + 1) for _ in range(k + 1)]
    dp[0][0] = 0
    for j in range(1, n + 1):
        dp[1][j] = prefix[j]

    for i in range(2, k + 1):
        for j in range(i, n + 1):
            for m in range(i - 1, j):
                if dp[i - 1][m] == INF:
                    continue
                cost = max(dp[i - 1][m], prefix[j] - prefix[m])
                dp[i][j] = min(dp[i][j], cost)

    return int(dp[k][n])


# ============================================================
# APPROACH 2: OPTIMAL  (Binary Search on Answer)
# Time: O(n log(sum))  |  Space: O(1)
# ============================================================
def optimal(boards: List[int], k: int) -> int:
    """
    Binary search on the answer. For a given max_time, greedily count painters needed.
    Real-life: Resource allocation in factory scheduling — minimizing the slowest line.
    """
    def is_possible(max_time: int) -> bool:
        painters = 1
        current = 0
        for board in boards:
            if current + board > max_time:
                painters += 1
                current = board
                if painters > k:
                    return False
            else:
                current += board
        return True

    lo, hi = max(boards), sum(boards)
    result = hi
    while lo <= hi:
        mid = (lo + hi) // 2
        if is_possible(mid):
            result = mid
            hi = mid - 1
        else:
            lo = mid + 1
    return result


# ============================================================
# APPROACH 3: BEST
# Time: O(n log(sum))  |  Space: O(1)
# ============================================================
def best(boards: List[int], k: int) -> int:
    """
    Same binary search — already optimal. Handles edge case k >= n.
    Real-life: Distributed computing job scheduler — minimize longest-running node.
    """
    if k >= len(boards):
        return max(boards)
    return optimal(boards, k)


if __name__ == "__main__":
    print("=== Painters Partition ===")
    tests = [
        ([10, 20, 30, 40],        2, 60),
        ([100, 200, 300, 400, 500], 3, 500),
        ([5, 5, 5, 5],            2, 10),
    ]
    for boards, k, exp in tests:
        print(f"\nBoards: {boards}  K={k}  =>  expected: {exp}")
        print(f"  Brute:   {brute_force(boards, k)}")
        print(f"  Optimal: {optimal(boards, k)}")
        print(f"  Best:    {best(boards, k)}")
