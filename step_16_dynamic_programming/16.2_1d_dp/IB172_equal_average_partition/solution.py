"""
Problem: Equal Average Partition
Difficulty: HARD | XP: 50
Source: InterviewBit

Given an array, partition it into two non-empty subsets such that the
average of both subsets is equal. Return the two subsets.
If multiple solutions exist, return the lexicographically smallest one.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE — Try all subsets
# Time: O(2^n * n)  |  Space: O(2^n)
# ============================================================
def brute_force(nums: List[int]) -> List[List[int]]:
    """
    Generate all possible subsets for the first partition.
    Check if average of subset equals average of remainder.
    Return lexicographically smallest valid partition.
    """
    n = len(nums)
    total = sum(nums)
    nums_sorted = sorted(nums)

    for mask in range(1, (1 << n) - 1):
        subset = []
        rest = []
        for i in range(n):
            if mask & (1 << i):
                subset.append(nums_sorted[i])
            else:
                rest.append(nums_sorted[i])
        s1, s2 = sum(subset), sum(rest)
        l1, l2 = len(subset), len(rest)
        if s1 * l2 == s2 * l1:  # avoid float division
            return [sorted(subset), sorted(rest)]
    return []


# ============================================================
# APPROACH 2: OPTIMAL — DP subset-sum with size constraint
# Time: O(n^2 * S)  |  Space: O(n * S)
# ============================================================
def optimal(nums: List[int]) -> List[List[int]]:
    """
    For the partition to have equal averages, if subset of size k has sum s:
    s/k = (total-s)/(n-k)  =>  s*n = total*k.
    So for each valid k (1..n-1), check if total*k % n == 0, then find
    subset of size k with sum = total*k/n using DP.
    """
    n = len(nums)
    total = sum(nums)
    nums_sorted = sorted(nums)

    # dp[size][s] = True if we can pick 'size' elements summing to s
    # Also track which elements were used to reconstruct the solution
    dp = [[set() for _ in range(total + 1)] for _ in range(n + 1)]
    dp[0][0] = {()}  # empty tuple as sentinel

    for num in nums_sorted:
        # Traverse in reverse to avoid using same element twice
        for size in range(n - 1, 0, -1):
            for s in range(total, num - 1, -1):
                if dp[size - 1][s - num]:
                    for combo in dp[size - 1][s - num]:
                        dp[size][s].add(combo + (num,))

    for k in range(1, n):
        if (total * k) % n != 0:
            continue
        target = total * k // n
        if dp[k][target]:
            subset = sorted(list(dp[k][target])[0])
            rest = list(nums_sorted)
            for v in subset:
                rest.remove(v)
            return [subset, rest]
    return []


# ============================================================
# APPROACH 3: BEST — Backtracking with pruning
# Time: O(n * 2^n) worst case  |  Space: O(n)
# ============================================================
def best(nums: List[int]) -> List[List[int]]:
    """
    Sort the array. For each valid subset size k, use backtracking
    with pruning to find a subset of size k with required sum.
    """
    n = len(nums)
    total = sum(nums)
    nums_sorted = sorted(nums)

    def backtrack(idx, remaining_sum, remaining_count, current):
        if remaining_count == 0:
            return remaining_sum == 0
        if idx >= n or remaining_count > n - idx:
            return False
        if remaining_sum < 0:
            return False

        # Pruning: minimum possible sum with remaining elements
        # Skip duplicates at same recursion level
        for i in range(idx, n - remaining_count + 1):
            if i > idx and nums_sorted[i] == nums_sorted[i - 1]:
                continue
            if nums_sorted[i] > remaining_sum:
                break
            current.append(nums_sorted[i])
            if backtrack(i + 1, remaining_sum - nums_sorted[i],
                         remaining_count - 1, current):
                return True
            current.pop()
        return False

    for k in range(1, n):
        if (total * k) % n != 0:
            continue
        target = total * k // n
        subset = []
        if backtrack(0, target, k, subset):
            rest = list(nums_sorted)
            for v in subset:
                rest.remove(v)
            return [subset, rest]
    return []


if __name__ == "__main__":
    print("=== Equal Average Partition ===")

    nums = [1, 7, 15, 29, 11, 9]
    print(f"Brute:   {brute_force(nums)}")
    print(f"Optimal: {optimal(nums)}")
    print(f"Best:    {best(nums)}")

    nums = [1, 2, 3, 4, 5]
    print(f"Brute:   {brute_force(nums)}")
    print(f"Best:    {best(nums)}")
