"""
Problem: Count Set Bits (Hamming Weight)
Difficulty: EASY | XP: 10
LeetCode #191

Key Insight: n & (n-1) clears the rightmost set bit (Brian Kernighan's trick).
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check Each Bit
# Time: O(32) = O(1)  |  Space: O(1)
#
# Extract last bit with n&1, shift right, repeat.
# ============================================================
def brute_force(n: int) -> int:
    count = 0
    while n:
        count += n & 1
        n >>= 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL -- Brian Kernighan's Algorithm
# Time: O(k) where k = set bits  |  Space: O(1)
#
# n & (n-1) clears rightmost set bit. Loop k times.
# ============================================================
def optimal(n: int) -> int:
    count = 0
    while n:
        n &= (n - 1)  # clear rightmost set bit
        count += 1
    return count


# ============================================================
# APPROACH 3: BEST -- Lookup Table (byte-level)
# Time: O(1) -- 4 lookups  |  Space: O(256) = O(1)
#
# Precompute popcount for all byte values, split int into 4 bytes.
# ============================================================
_BYTE_TABLE = [0] * 256
for _i in range(1, 256):
    _BYTE_TABLE[_i] = _BYTE_TABLE[_i >> 1] + (_i & 1)


def best(n: int) -> int:
    return (
        _BYTE_TABLE[n & 0xFF]
        + _BYTE_TABLE[(n >> 8) & 0xFF]
        + _BYTE_TABLE[(n >> 16) & 0xFF]
        + _BYTE_TABLE[(n >> 24) & 0xFF]
    )


if __name__ == "__main__":
    print("=== Count Set Bits ===")
    tests = [11, 128, 255, 0, 1, 2**31 - 1]
    for n in tests:
        print(f"n={n} (binary: {bin(n)})")
        print(f"  Brute:   {brute_force(n)}")
        print(f"  Optimal: {optimal(n)}")
        print(f"  Best:    {best(n)}")
        print()
