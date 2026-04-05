"""
Problem: Remove Last Set Bit
Difficulty: EASY | XP: 10

Turn off the rightmost set bit of a non-negative integer n.
Key insight: n & (n-1) clears the lowest set bit (Brian Kernighan's trick).
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Find Position Then Clear)
# Time: O(log n) | Space: O(1)
# ============================================================
def brute_force(n: int) -> int:
    """Find the rightmost set bit position, then XOR to clear it."""
    if n == 0:
        return 0
    for pos in range(32):
        if n & (1 << pos):
            return n ^ (1 << pos)
    return 0


# ============================================================
# APPROACH 2: OPTIMAL (Brian Kernighan's Trick)
# Time: O(1) | Space: O(1)
# ============================================================
def optimal(n: int) -> int:
    """n & (n-1) clears the rightmost set bit in one operation."""
    return n & (n - 1)


# ============================================================
# APPROACH 3: BEST (Isolate and Subtract)
# Time: O(1) | Space: O(1)
# ============================================================
def best(n: int) -> int:
    """n & -n isolates the rightmost set bit; subtracting clears it."""
    return n - (n & -n)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Remove Last Set Bit ===\n")

    test_cases = [
        (12, 8),
        (7, 6),
        (16, 0),
        (0, 0),
        (1, 0),
        (6, 4),
        (255, 254),
    ]

    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        r = best(n)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: {n} (binary: {bin(n)})")
        print(f"  Brute:    {b} (binary: {bin(b)})")
        print(f"  Optimal:  {o} (binary: {bin(o)})")
        print(f"  Best:     {r} (binary: {bin(r)})")
        print(f"  Expected: {expected}  [{status}]\n")
