"""
Problem: Check if Number is Power of 2 (LeetCode #231)
Difficulty: EASY | XP: 10

Return true if n is a power of two.
Key insight: A power of 2 has exactly one set bit, so n & (n-1) == 0.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Repeated Division)
# Time: O(log n) | Space: O(1)
# ============================================================
def brute_force(n: int) -> bool:
    """Keep dividing by 2. If we reach 1, it's a power of 2."""
    if n <= 0:
        return False
    while n % 2 == 0:
        n //= 2
    return n == 1


# ============================================================
# APPROACH 2: OPTIMAL (Bit Trick)
# Time: O(1) | Space: O(1)
# ============================================================
def optimal(n: int) -> bool:
    """n & (n-1) clears the rightmost set bit. If result is 0, exactly one bit was set."""
    return n > 0 and (n & (n - 1)) == 0


# ============================================================
# APPROACH 3: BEST (bin count -- clarity variant)
# Time: O(1) | Space: O(1)
# ============================================================
def best(n: int) -> bool:
    """Exactly one set bit means power of 2."""
    return n > 0 and bin(n).count("1") == 1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Check if Number is Power of 2 ===\n")

    test_cases = [
        (1, True),
        (2, True),
        (4, True),
        (16, True),
        (1024, True),
        (3, False),
        (0, False),
        (-1, False),
        (-4, False),
        (6, False),
        (1073741824, True),
    ]

    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        r = best(n)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: {n}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
