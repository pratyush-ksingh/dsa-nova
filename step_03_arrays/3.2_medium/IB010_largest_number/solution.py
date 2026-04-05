"""
Problem: Largest Number
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a list of non-negative integers, arrange them so that they form
the largest possible number. Return it as a string.

Key insight: comparator a > b iff str(a)+str(b) > str(b)+str(a).
"""
from typing import List
from itertools import permutations
import functools


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n! * n)  |  Space: O(n!)
# Try all permutations, pick the one forming the max number.
# Only feasible for tiny inputs.
# ============================================================
def brute_force(A: List[int]) -> str:
    if not A:
        return ""
    nums = [str(x) for x in A]
    best_val = ""
    for perm in permutations(nums):
        candidate = "".join(perm)
        if candidate > best_val:
            best_val = candidate
    # Remove leading zeros
    return best_val.lstrip("0") or "0"


# ============================================================
# APPROACH 2: OPTIMAL — Custom Comparator Sort
# Time: O(n log n * L)  |  Space: O(n * L)
# Sort by comparator: a should precede b if a+b > b+a.
# ============================================================
def optimal(A: List[int]) -> str:
    if not A:
        return ""
    nums = [str(x) for x in A]

    def cmp(a: str, b: str) -> int:
        if a + b > b + a:
            return -1  # a should come first
        elif a + b < b + a:
            return 1
        return 0

    nums.sort(key=functools.cmp_to_key(cmp))
    result = "".join(nums)
    return result.lstrip("0") or "0"


# ============================================================
# APPROACH 3: BEST — One-liner using sorted() with key trick
# Time: O(n log n * L)  |  Space: O(n * L)
# Python's string comparison on repeated concatenation works as
# a key: sort by (x * 10) as string proxy isn't perfect, so
# we still use functools.cmp_to_key but with a lambda.
# Same asymptotic complexity, cleaner code.
# ============================================================
def best(A: List[int]) -> str:
    if not A:
        return ""
    nums = [str(x) for x in A]
    nums.sort(key=functools.cmp_to_key(lambda a, b: (1 if a + b < b + a else -1 if a + b > b + a else 0)))
    result = "".join(nums)
    return result.lstrip("0") or "0"


if __name__ == "__main__":
    print("=== Largest Number ===")
    tests = [
        ([3, 30, 34, 5, 9],  "9534330"),
        ([10, 2],             "210"),
        ([0, 0],              "0"),
        ([1],                 "1"),
        ([121, 12],           "12121"),
        ([9, 99, 999],        "999999"),
    ]
    for A, expected in tests:
        bf = brute_force(A[:])
        op = optimal(A[:])
        be = best(A[:])
        ok = all(x == expected for x in [bf, op, be])
        print(f"A={A} -> Brute={bf}, Optimal={op}, Best={be} | Expected={expected} {'OK' if ok else 'FAIL'}")
