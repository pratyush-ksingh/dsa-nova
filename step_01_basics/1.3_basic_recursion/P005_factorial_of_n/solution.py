"""
Problem: Factorial of N
Difficulty: EASY | XP: 10

Compute N! = N * (N-1) * ... * 1, with 0! = 1.
Discuss stack overflow for large N.
"""
import sys


# ============================================================
# APPROACH 1: BRUTE FORCE (Iterative Loop)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(n: int) -> int:
    """Multiply 1 * 2 * ... * N in a loop."""
    if n < 0:
        raise ValueError("Factorial undefined for negative numbers")
    result = 1
    for i in range(2, n + 1):
        result *= i
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Head Recursion)
# Time: O(n) | Space: O(n) recursion stack
# ============================================================
def optimal(n: int) -> int:
    """fact(N) = N * fact(N-1), base case: fact(0) = 1."""
    if n < 0:
        raise ValueError("Factorial undefined for negative numbers")
    if n == 0:
        return 1
    return n * optimal(n - 1)


# ============================================================
# APPROACH 3: BEST (Tail Recursion with Accumulator)
# Time: O(n) | Space: O(1) with TCO, O(n) without
# ============================================================
def best(n: int, acc: int = 1) -> int:
    """
    Tail-recursive: accumulator carries the product.
    fact(N, acc) = fact(N-1, acc * N), base case: fact(0, acc) = acc.
    Python does NOT optimize tail calls, but this shows the pattern.
    """
    if n < 0:
        raise ValueError("Factorial undefined for negative numbers")
    if n == 0:
        return acc
    return best(n - 1, acc * n)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Factorial of N ===\n")

    sys.setrecursionlimit(2000)

    test_cases = [
        (0, 1),
        (1, 1),
        (2, 2),
        (5, 120),
        (10, 3628800),
        (13, 6227020800),
        (20, 2432902008176640000),
        (25, 15511210043330985984000000),  # Python handles big ints natively
    ]

    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        s = best(n)
        status = "PASS" if b == expected and o == expected and s == expected else "FAIL"
        print(f"N = {n}")
        print(f"  Loop:      {b}")
        print(f"  Recursion: {o}")
        print(f"  Tail Rec:  {s}")
        print(f"  Expected:  {expected}  [{status}]\n")

    # Large N demo (Python handles arbitrary precision)
    print("--- Large N Demo ---")
    large = brute_force(100)
    print(f"100! = {large}")
    print(f"Number of digits: {len(str(large))}\n")

    # Negative input test
    print("--- Negative Input ---")
    try:
        brute_force(-1)
    except ValueError as e:
        print(f"factorial(-1) raised: {e} [PASS]")
