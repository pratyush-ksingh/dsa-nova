"""
Problem: N Digit Numbers with Digit Sum S
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Count N-digit positive integers whose digits sum exactly to S.
First digit: 1-9 (no leading zeros). Remaining digits: 0-9.
Return answer modulo 10^9+7.

DP state: dp[pos][remaining_sum]
  - pos = current digit position (0-indexed, 0 = first digit)
  - remaining = sum still needed from positions pos..N-1

Real-life analogy: counting valid product codes or license plates
with a fixed checksum (digit sum constraint).
"""
from typing import List
import sys
sys.setrecursionlimit(100000)

MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE (Plain recursion, no memoization)
# Time: O(10^N)  |  Space: O(N) recursion stack
# ============================================================
# Try all valid digits at each position and recurse.
# Count paths where remaining sum reaches 0 at the last position.
def brute_force(N: int, S: int) -> int:
    if N == 0 or S <= 0 or S > 9 * N:
        return 0

    def solve(pos: int, remaining: int) -> int:
        if pos == N:
            return 1 if remaining == 0 else 0
        start = 1 if pos == 0 else 0
        count = 0
        for d in range(start, 10):
            if d > remaining:
                break
            count = (count + solve(pos + 1, remaining - d)) % MOD
        return count

    return solve(0, S)


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
# Time: O(N * S * 10)  |  Space: O(N * S)
# ============================================================
# Cache results for each (pos, remaining) pair.
def optimal(N: int, S: int) -> int:
    if N == 0 or S <= 0 or S > 9 * N:
        return 0

    from functools import lru_cache

    @lru_cache(maxsize=None)
    def solve(pos: int, remaining: int) -> int:
        if pos == N:
            return 1 if remaining == 0 else 0
        start = 1 if pos == 0 else 0
        count = 0
        for d in range(start, 10):
            if d > remaining:
                break
            count = (count + solve(pos + 1, remaining - d)) % MOD
        return count

    return solve(0, S)


# ============================================================
# APPROACH 3: BEST (Bottom-Up DP with 1D rolling array)
# Time: O(N * S * 10)  |  Space: O(S)
# ============================================================
# dp[s] = number of ways to have placed `pos` digits summing to s so far.
# At each new position, expand by adding one more digit (0-9, or 1-9 for pos=0).
# Use a rolling 1D array to save space.
def best(N: int, S: int) -> int:
    if N == 0 or S <= 0 or S > 9 * N:
        return 0

    # Initialize for position 0 (first digit, 1..9 only)
    dp = [0] * (S + 1)
    for d in range(1, 10):
        if d <= S:
            dp[d] = 1

    # Fill positions 1..N-1 (digits 0..9)
    for pos in range(1, N):
        new_dp = [0] * (S + 1)
        for s in range(S + 1):
            if dp[s] == 0:
                continue
            for d in range(10):
                if s + d > S:
                    break
                new_dp[s + d] = (new_dp[s + d] + dp[s]) % MOD
        dp = new_dp

    return dp[S]


if __name__ == "__main__":
    print("=== N Digit Numbers with Digit Sum S ===")
    # N=1, S=5  -> {5}                          -> 1
    # N=2, S=5  -> {14,23,32,41,50}             -> 5
    # N=2, S=1  -> {10}                         -> 1
    # N=3, S=2  -> {101,110,200}                -> 3
    # N=1, S=9  -> {9}                          -> 1
    # N=2, S=10 -> {19,28,37,46,55,64,73,82,91} -> 9
    test_cases = [
        (1, 5,  1),
        (2, 5,  5),
        (2, 1,  1),
        (3, 2,  3),
        (1, 9,  1),
        (2, 10, 9),
    ]

    for N, S, expected in test_cases:
        bf = brute_force(N, S)
        op = optimal(N, S)
        be = best(N, S)
        status = "PASS" if bf == op == be == expected else "FAIL"
        print(f"[{status}] N={N}, S={S:<3} | Brute: {bf} | Optimal: {op} | Best: {be} | Expected: {expected}")
