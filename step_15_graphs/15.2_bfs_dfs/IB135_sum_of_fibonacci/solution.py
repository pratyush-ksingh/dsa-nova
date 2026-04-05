"""
Problem: Sum of Fibonacci (Zeckendorf's Representation)
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a positive integer N, represent it as a sum of non-repeating
Fibonacci numbers (Zeckendorf's theorem guarantees a unique representation).

Real-life use: Data compression, number theory, Fibonacci coding in data streams.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(log(N) * log(N))  |  Space: O(log(N))
# Generate all Fibonacci numbers up to N, then greedily subtract
# the largest Fibonacci <= remaining value.
# ============================================================
def brute_force(n: int) -> List[int]:
    """Zeckendorf's greedy: generate fibs list, then greedy subtract."""
    if n <= 0:
        return []
    # Generate all Fibonacci numbers <= n
    fibs = []
    a, b = 1, 2
    fibs.append(a)
    while b <= n:
        fibs.append(b)
        a, b = b, a + b

    result = []
    remaining = n
    for fib in reversed(fibs):
        if fib <= remaining:
            result.append(fib)
            remaining -= fib
        if remaining == 0:
            break
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log(N))  |  Space: O(log(N))
# Walk down the Fibonacci sequence without storing all values.
# Find largest fib <= n by climbing up, then descend greedily.
# Zeckendorf guarantees non-consecutive Fibonacci numbers.
# ============================================================
def optimal(n: int) -> List[int]:
    """Find largest fib <= n by climbing, then greedy descent."""
    if n <= 0:
        return []

    # Climb to find largest Fibonacci <= n
    a, b = 1, 1
    while b <= n:
        a, b = b, a + b
    # Now a is the largest Fibonacci <= n

    result = []
    remaining = n
    curr, prev = a, b
    while remaining > 0 and curr >= 1:
        if curr <= remaining:
            result.append(curr)
            remaining -= curr
        curr, prev = prev - curr, curr
    return result


# ============================================================
# APPROACH 3: BEST
# Time: O(log(N))  |  Space: O(log(N))
# Canonical Zeckendorf algorithm. Proof: any two selected Fibonacci
# numbers must be non-consecutive (greedy ensures this). The
# representation is UNIQUE per Zeckendorf's theorem (1972).
# ============================================================
def best(n: int) -> List[int]:
    """Canonical Zeckendorf: always pick largest valid Fibonacci number."""
    if n <= 0:
        return []

    # Build Fibonacci sequence up to n
    fibs = [1, 2]
    while fibs[-1] + fibs[-2] <= n:
        fibs.append(fibs[-1] + fibs[-2])

    # Remove fibs > n (edge case when n < 2)
    fibs = [f for f in fibs if f <= n]

    zeckendorf = []
    rem = n
    for fib in reversed(fibs):
        if fib <= rem:
            zeckendorf.append(fib)
            rem -= fib
        if rem == 0:
            break
    return zeckendorf


if __name__ == "__main__":
    print("=== Sum of Fibonacci (Zeckendorf's Representation) ===\n")

    tests = [1, 2, 3, 5, 10, 13, 100, 143]
    for n in tests:
        bf = brute_force(n)
        op = optimal(n)
        be = best(n)
        print(f"N={n:3d} | Brute: {str(bf):<30} | Optimal: {str(op):<30} | Best: {be}")

    print("\n--- Verification ---")
    for n in [10, 20, 30, 100]:
        res = best(n)
        total = sum(res)
        print(f"N={n:3d} -> {res}, Sum={total}, Valid={total == n}")
