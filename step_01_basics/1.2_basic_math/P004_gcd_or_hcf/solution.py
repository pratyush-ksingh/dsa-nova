"""
Problem: GCD or HCF
Difficulty: EASY | XP: 10

Find the Greatest Common Divisor (GCD) / Highest Common Factor (HCF)
of two positive integers.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterate from min(a,b) down to 1
# Time: O(min(a,b))  |  Space: O(1)
# Check every number from min(a,b) down to 1; first that divides
# both is the GCD.
# ============================================================
def brute_force(a: int, b: int) -> int:
    """Find GCD by iterating downward from min(a, b)."""
    if a == 0:
        return b
    if b == 0:
        return a
    result = 1
    for i in range(min(a, b), 0, -1):
        if a % i == 0 and b % i == 0:
            result = i
            break
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Euclidean Algorithm (Iterative)
# Time: O(log(min(a,b)))  |  Space: O(1)
# Repeatedly replace the larger number with (larger % smaller)
# until one becomes 0. The other is the GCD.
# ============================================================
def optimal(a: int, b: int) -> int:
    """Find GCD using iterative Euclidean algorithm."""
    while b != 0:
        a, b = b, a % b
    return a


# ============================================================
# APPROACH 3: BEST -- Euclidean Algorithm (Recursive)
# Time: O(log(min(a,b)))  |  Space: O(log(min(a,b))) call stack
# Same logic as iterative but expressed recursively for clarity.
# ============================================================
def best(a: int, b: int) -> int:
    """Find GCD using recursive Euclidean algorithm."""
    if b == 0:
        return a
    return best(b, a % b)


if __name__ == "__main__":
    test_cases = [(12, 8), (54, 24), (7, 13), (0, 5), (100, 100), (48, 18), (1, 1000000)]
    print("=== GCD or HCF ===")

    for a, b in test_cases:
        print(f"GCD({a}, {b})")
        print(f"  Brute Force: {brute_force(a, b)}")
        print(f"  Optimal:     {optimal(a, b)}")
        print(f"  Best:        {best(a, b)}")
