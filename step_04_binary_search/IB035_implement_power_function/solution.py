"""
Problem: Implement Power Function (LeetCode #50 / InterviewBit)
Difficulty: EASY | XP: 10

Implement pow(x, n) using binary exponentiation in O(log n).
Key insight: x^n = (x^(n/2))^2 when n is even, x * x^(n-1) when n is odd.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Multiply n Times)
# Time: O(|n|) | Space: O(1)
# NOTE: TLEs for large n. Shown for completeness.
# ============================================================
def brute_force(x: float, n: int) -> float:
    """Multiply x by itself |n| times. Invert if n is negative."""
    N = n
    if N < 0:
        x = 1.0 / x
        N = -N

    # Cap for testing to avoid infinite loops
    if N > 1_000_000:
        return float("nan")

    result = 1.0
    for _ in range(N):
        result *= x
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Iterative Binary Exponentiation)
# Time: O(log n) | Space: O(1)
# ============================================================
def optimal(x: float, n: int) -> float:
    """Binary exponentiation: square base, halve exponent each step."""
    N = n
    if N < 0:
        x = 1.0 / x
        N = -N

    result = 1.0
    while N > 0:
        if N % 2 == 1:
            result *= x  # odd exponent: take one x out
        x *= x            # square the base
        N //= 2           # halve the exponent
    return result


# ============================================================
# APPROACH 3: BEST (Recursive Binary Exponentiation)
# Time: O(log n) | Space: O(log n) recursion stack
# ============================================================
def best(x: float, n: int) -> float:
    """Recursive divide-and-conquer: x^n = (x^(n/2))^2."""
    if n < 0:
        x = 1.0 / x
        n = -n

    def helper(x: float, n: int) -> float:
        if n == 0:
            return 1.0
        half = helper(x, n // 2)
        if n % 2 == 0:
            return half * half
        else:
            return half * half * x

    return helper(x, n)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Implement Power Function ===\n")

    test_cases = [
        (2.0, 10, 1024.0),
        (2.1, 3, 9.261),
        (2.0, -2, 0.25),
        (1.0, 2147483647, 1.0),
        (0.0, 1, 0.0),
        (-1.0, 2, 1.0),
        (-1.0, 3, -1.0),
        (2.0, 0, 1.0),
    ]

    for x, n, expected in test_cases:
        o = optimal(x, n)
        r = best(x, n)

        # Brute force: skip huge exponents
        if abs(n) <= 1_000_000:
            b = brute_force(x, n)
            brute_str = f"{b:.3f}"
        else:
            brute_str = "SKIPPED (n too large)"

        pass_check = abs(o - expected) < 0.01 and abs(r - expected) < 0.01
        status = "PASS" if pass_check else "FAIL"
        print(f"Input: x={x}, n={n}")
        print(f"  Brute:    {brute_str}")
        print(f"  Optimal:  {o:.3f}")
        print(f"  Best:     {r:.3f}")
        print(f"  Expected: {expected:.3f}  [{status}]\n")
