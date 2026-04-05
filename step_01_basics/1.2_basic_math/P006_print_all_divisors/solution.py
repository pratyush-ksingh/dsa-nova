"""
Problem: Print All Divisors
Difficulty: EASY | XP: 10

Given N, print all divisors of N in sorted ascending order.
"""
import math
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check Every Number 1 to N
# Time: O(N)  |  Space: O(d)
# Iterate 1..N, collect those that divide N evenly.
# ============================================================
def brute_force(n: int) -> List[int]:
    return [i for i in range(1, n + 1) if n % i == 0]


# ============================================================
# APPROACH 2: OPTIMAL -- Iterate to sqrt(N), Sort
# Time: O(sqrt(N) + d*log(d))  |  Space: O(d)
# Use divisor pairing: if i divides N, so does N/i.
# ============================================================
def optimal(n: int) -> List[int]:
    result = []
    i = 1
    while i * i <= n:
        if n % i == 0:
            result.append(i)
            if i != n // i:
                result.append(n // i)
        i += 1
    result.sort()
    return result


# ============================================================
# APPROACH 3: BEST -- sqrt(N) with Two-List Merge (No Sort)
# Time: O(sqrt(N))  |  Space: O(d)
# Maintain low and high lists to avoid sorting.
# ============================================================
def best(n: int) -> List[int]:
    low = []
    high = []
    i = 1
    while i * i <= n:
        if n % i == 0:
            low.append(i)
            if i != n // i:
                high.append(n // i)
        i += 1
    # high is descending, reverse to ascending
    return low + high[::-1]


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    test_cases = [1, 7, 12, 36, 100]

    print("=== Print All Divisors ===\n")
    for n in test_cases:
        b = brute_force(n)
        o = optimal(n)
        bt = best(n)
        match = b == o == bt
        status = "PASS" if match else "FAIL"
        print(f"[{status}] N={n:<6} | Brute: {b}")
        print(f"              | Optimal: {o}")
        print(f"              | Best:    {bt}\n")
