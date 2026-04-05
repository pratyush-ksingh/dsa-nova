"""
Problem: Combination Sum III
Difficulty: MEDIUM | XP: 25

Find all valid combinations of k numbers that sum to n.
Numbers are from 1-9, each used at most once.
"""
from typing import List
from itertools import combinations


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(9! / (9-k)!)  |  Space: O(k) recursion depth
# Generate all subsets recursively, collect when size==k and sum==n
# ============================================================
def brute_force(k: int, n: int) -> List[List[int]]:
    res = []

    def helper(start: int, current: List[int]) -> None:
        if len(current) == k and sum(current) == n:
            res.append(list(current))
            return
        if len(current) == k:
            return
        for num in range(start, 10):
            current.append(num)
            helper(num + 1, current)
            current.pop()

    helper(1, [])
    return res


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking with early pruning
# Time: O(C(9,k))  |  Space: O(k)
# Track remaining count and remaining sum; prune aggressively
# ============================================================
def optimal(k: int, n: int) -> List[List[int]]:
    res = []

    def backtrack(start: int, k_left: int, remain: int, current: List[int]) -> None:
        if k_left == 0 and remain == 0:
            res.append(list(current))
            return
        if k_left == 0 or remain <= 0:
            return
        for num in range(start, 10):
            if num > remain:
                break  # numbers only get larger, no point continuing
            current.append(num)
            backtrack(num + 1, k_left - 1, remain - num, current)
            current.pop()

    backtrack(1, k, n, [])
    return res


# ============================================================
# APPROACH 3: BEST - Pythonic itertools combinations
# Time: O(C(9,k))  |  Space: O(C(9,k) * k) for output
# Use itertools.combinations to enumerate all size-k subsets of 1..9
# ============================================================
def best(k: int, n: int) -> List[List[int]]:
    return [list(combo) for combo in combinations(range(1, 10), k) if sum(combo) == n]


if __name__ == "__main__":
    print("=== Combination Sum III ===")

    k, n = 3, 7
    print(f"k={k}, n={n}")
    print(f"Brute:   {brute_force(k, n)}")
    print(f"Optimal: {optimal(k, n)}")
    print(f"Best:    {best(k, n)}")

    k, n = 3, 9
    print(f"\nk={k}, n={n}")
    print(f"Brute:   {brute_force(k, n)}")
    print(f"Optimal: {optimal(k, n)}")
    print(f"Best:    {best(k, n)}")

    k, n = 4, 1
    print(f"\nk={k}, n={n} (no solution)")
    print(f"Brute:   {brute_force(k, n)}")
    print(f"Optimal: {optimal(k, n)}")
    print(f"Best:    {best(k, n)}")
