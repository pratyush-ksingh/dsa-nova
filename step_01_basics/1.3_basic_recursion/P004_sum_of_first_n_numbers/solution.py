"""
Problem: Sum of First N Numbers
Difficulty: EASY | XP: 10

Find 1 + 2 + 3 + ... + N using loop, recursion, and formula.
"""
import sys


# ============================================================
# APPROACH 1: BRUTE FORCE (Iterative Loop)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(n: int) -> int:
    """Simple accumulator loop from 1 to N."""
    total = 0
    for i in range(1, n + 1):
        total += i
    return total


# ============================================================
# APPROACH 2: OPTIMAL (Head Recursion)
# Time: O(n) | Space: O(n) recursion stack
# ============================================================
def optimal(n: int) -> int:
    """sum(N) = N + sum(N-1), base case: sum(0) = 0."""
    if n == 0:
        return 0
    return n + optimal(n - 1)


# ============================================================
# APPROACH 3: BEST (Gauss's Formula)
# Time: O(1) | Space: O(1)
# ============================================================
def best(n: int) -> int:
    """Closed-form: N * (N + 1) // 2."""
    return n * (n + 1) // 2


# ============================================================
# BONUS: Tail Recursion
# ============================================================
def tail_recursive(n: int, acc: int = 0) -> int:
    """
    Tail-recursive: accumulator passed as parameter.
    Python does not optimize tail calls, but this demonstrates the concept.
    """
    if n == 0:
        return acc
    return tail_recursive(n - 1, acc + n)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Sum of First N Numbers ===\n")

    # Increase recursion limit for testing (default is 1000)
    sys.setrecursionlimit(2000)

    test_cases = [
        (0, 0),
        (1, 1),
        (5, 15),
        (10, 55),
        (100, 5050),
        (1000, 500500),
    ]

    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        s = best(n)
        tr = tail_recursive(n)
        status = "PASS" if b == expected and o == expected and s == expected and tr == expected else "FAIL"
        print(f"N = {n}")
        print(f"  Loop:       {b}")
        print(f"  Recursion:  {o}")
        print(f"  Formula:    {s}")
        print(f"  Tail Rec:   {tr}")
        print(f"  Expected:   {expected}  [{status}]\n")

    # Large N test (formula + loop only -- recursion would hit stack limit)
    large_n = 1_000_000
    formula_result = best(large_n)
    loop_result = brute_force(large_n)
    print(f"Large N = {large_n}")
    print(f"  Formula: {formula_result}")
    print(f"  Loop:    {loop_result}")
    print(f"  Match:   {formula_result == loop_result}")
