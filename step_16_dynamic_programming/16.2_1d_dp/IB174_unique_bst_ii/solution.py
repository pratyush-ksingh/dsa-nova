"""
Problem: Unique BST II
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Count the number of structurally unique BSTs that can store values 1..n.
Answer is the Nth Catalan number.

Catalan recurrence:
  C(0) = 1  (empty tree)
  C(n) = sum_{i=0}^{n-1} C(i) * C(n-1-i)   for n >= 1

For each root r in 1..n:
  left subtree: r-1 nodes  -> C(r-1) unique structures
  right subtree: n-r nodes -> C(n-r) unique structures
  contributions: C(r-1) * C(n-r)

Real-life use case: Counting parse tree structures, expression tree
combinatorics, analyzing BST shape distributions in random insertions.
"""
from typing import List
from functools import lru_cache


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(4^n / n^1.5) - exponential without memo  |  Space: O(n)
# Pure recursion exposing the Catalan recurrence.
# Impractical for large n but illustrates the structure clearly.
# ============================================================
def brute_force(n: int) -> int:
    def count(k: int) -> int:
        if k <= 1:
            return 1
        total = 0
        for root in range(1, k + 1):
            total += count(root - 1) * count(k - root)
        return total

    return count(n)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n^2)  |  Space: O(n)
# Top-down with @lru_cache memoization.
# Avoids recomputing C(k) for the same k across different calls.
# ============================================================
def optimal(n: int) -> int:
    @lru_cache(maxsize=None)
    def count(k: int) -> int:
        if k <= 1:
            return 1
        return sum(count(i) * count(k - 1 - i) for i in range(k))

    return count(n)


# ============================================================
# APPROACH 3: BEST
# Time: O(n^2)  |  Space: O(n)
# Bottom-up DP. Build Catalan numbers iteratively from dp[0] to dp[n].
# dp[i] = sum_{j=0}^{i-1} dp[j] * dp[i-1-j]
# No recursion overhead; clearest and fastest implementation.
# ============================================================
def best(n: int) -> int:
    dp = [0] * (n + 1)
    dp[0] = 1  # empty tree
    if n >= 1:
        dp[1] = 1
    for i in range(2, n + 1):
        for j in range(i):
            dp[i] += dp[j] * dp[i - 1 - j]
    return dp[n]


if __name__ == "__main__":
    print("=== Unique BST II (Count Unique BSTs) ===")

    # Known Catalan numbers
    expected = [1, 1, 2, 5, 14, 42, 132, 429]

    print(f"{'n':<5} {'Expected':<10} {'Brute':<10} {'Optimal':<10} {'Best':<10}")
    for n in range(8):
        print(f"{n:<5} {expected[n]:<10} {brute_force(n):<10} {optimal(n):<10} {best(n):<10}")
