"""
Problem: Set the Rightmost Unset Bit
Difficulty: EASY | XP: 10

Key Insight: n | (n+1) sets the rightmost 0-bit to 1.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Bit-by-Bit Scan
# Time: O(32) = O(1)  |  Space: O(1)
#
# Scan from LSB, find first 0-bit, set it.
# ============================================================
def brute_force(n: int) -> int:
    for pos in range(32):
        if (n >> pos) & 1 == 0:
            return n | (1 << pos)
    return n  # all bits are 1


# ============================================================
# APPROACH 2: OPTIMAL -- n | (n + 1)
# Time: O(1)  |  Space: O(1)
#
# n+1 flips rightmost 0 (and trailing 1s).
# OR with n restores trailing 1s and keeps the new bit.
# ============================================================
def optimal(n: int) -> int:
    # If all bits set (n = 2^k - 1), return unchanged
    if n > 0 and (n & (n + 1)) == 0:
        return n
    return n | (n + 1)


# ============================================================
# APPROACH 3: BEST -- Isolate with ~n & (n + 1)
# Time: O(1)  |  Space: O(1)
#
# ~n & (n+1) isolates the rightmost 0-bit as a mask.
# OR the mask into n.
# ============================================================
def best(n: int) -> int:
    if n > 0 and (n & (n + 1)) == 0:
        return n
    # For Python's arbitrary-precision ints, we work within 32-bit range
    mask = (~n) & (n + 1) & 0xFFFFFFFF  # isolate rightmost 0
    return n | mask


if __name__ == "__main__":
    print("=== Set the Rightmost Unset Bit ===")
    tests = [6, 9, 15, 0, 1, 10]
    for n in tests:
        print(f"n={n} (binary: {bin(n)})")
        print(f"  Brute:   {brute_force(n)} ({bin(brute_force(n))})")
        print(f"  Optimal: {optimal(n)} ({bin(optimal(n))})")
        print(f"  Best:    {best(n)} ({bin(best(n))})")
        print()
