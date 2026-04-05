"""
Problem: Stringoholics
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a list of strings, find the minimum number of rotation steps such that
ALL strings simultaneously return to their original form, modulo 1e9+7.

Key insight:
- A string S of length n has minimal rotation period p = n - kmp_fail[n-1]
  if p divides n, else period = n.
- Answer = LCM of all periods, mod (10^9 + 7).
"""
from typing import List
from math import gcd

MOD = 10**9 + 7


def kmp_failure(s: str) -> List[int]:
    """KMP failure function (partial match table)."""
    n = len(s)
    fail = [0] * n
    for i in range(1, n):
        j = fail[i - 1]
        while j > 0 and s[i] != s[j]:
            j = fail[j - 1]
        if s[i] == s[j]:
            j += 1
        fail[i] = j
    return fail


def min_period(s: str) -> int:
    """Minimal rotation period of string s."""
    n = len(s)
    fail = kmp_failure(s)
    candidate = n - fail[n - 1]
    return candidate if n % candidate == 0 else n


def lcm(a: int, b: int) -> int:
    return a // gcd(a, b) * b


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(sum(L_i^2))  |  Space: O(max_L)
# Simulate left-rotations for each string until back to original.
# Compute LCM of all step counts.
# ============================================================
def brute_force(A: List[str]) -> int:
    result = 1
    for s in A:
        rotated = s
        steps = 0
        while True:
            rotated = rotated[1:] + rotated[0]
            steps += 1
            if rotated == s:
                break
        result = lcm(result, steps)
    return result % MOD


# ============================================================
# APPROACH 2: OPTIMAL — KMP Period + LCM
# Time: O(sum(L_i))  |  Space: O(max_L)
# Use KMP to compute minimal period; compute LCM of periods.
# ============================================================
def optimal(A: List[str]) -> int:
    result = 1
    for s in A:
        period = min_period(s)
        result = lcm(result, period)
    return result % MOD


# ============================================================
# APPROACH 3: BEST — Same as Optimal with inline KMP
# Time: O(sum(L_i))  |  Space: O(max_L)
# Explicitly inlines the KMP computation for clarity.
# ============================================================
def best(A: List[str]) -> int:
    result = 1
    for s in A:
        if not s:
            continue
        n = len(s)
        # KMP failure function
        fail = [0] * n
        j = 0
        for i in range(1, n):
            while j > 0 and s[i] != s[j]:
                j = fail[j - 1]
            if s[i] == s[j]:
                j += 1
            fail[i] = j
            j = fail[i]  # reset for next iter -- actually keep j
        # Recalculate properly
        fail = [0] * n
        for i in range(1, n):
            jj = fail[i - 1]
            while jj > 0 and s[i] != s[jj]:
                jj = fail[jj - 1]
            if s[i] == s[jj]:
                jj += 1
            fail[i] = jj

        candidate = n - fail[n - 1]
        period = candidate if n % candidate == 0 else n
        result = lcm(result, period)

    return result % MOD


if __name__ == "__main__":
    print("=== Stringoholics ===")
    tests = [
        (["a", "ab", "abc"],   6),   # LCM(1,2,3) = 6
        (["abab", "ababab"],   2),   # LCM(2,2) = 2
        (["aaa"],              1),   # period 1 -> LCM = 1
        (["abc", "abcabc"],   3),   # LCM(3,3) = 3
        (["abcd"],             4),   # period 4 -> 4
    ]
    for A, expected in tests:
        bf = brute_force(A[:])
        op = optimal(A[:])
        be = best(A[:])
        ok = all(x == expected for x in [bf, op, be])
        print(f"A={A} -> Brute={bf}, Optimal={op}, Best={be} | Expected={expected} {'OK' if ok else 'FAIL'}")
