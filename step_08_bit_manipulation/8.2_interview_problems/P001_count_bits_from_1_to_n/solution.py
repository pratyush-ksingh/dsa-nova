"""
Problem: Count Total Set Bits from 1 to N
Difficulty: MEDIUM | XP: 25

Count the total number of set bits (1s) in the binary representations
of all integers from 1 to N (inclusive).
E.g., N=3: bin(1)=1 (1 bit), bin(2)=10 (1 bit), bin(3)=11 (2 bits) -> total = 4
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * log n)  |  Space: O(1)
#
# For each number i from 1 to N, count its set bits using
# Brian Kernighan's bit trick (or bin().count('1')).
# Sum all counts.
# ============================================================
def brute_force(n: int) -> int:
    """Count set bits for each number individually and sum."""
    total = 0
    for i in range(1, n + 1):
        # Count set bits in i using Brian Kernighan's algorithm
        x = i
        while x:
            total += 1
            x &= (x - 1)  # clear lowest set bit
    return total


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log^2 n)  |  Space: O(1)
#
# Use the pattern that numbers from 1 to 2^k - 1 have exactly
# k * 2^(k-1) set bits total. For arbitrary N, find the largest
# power of 2 <= N, add the contribution of the full block plus
# the MSB contributions of the remainder, then recurse on remainder.
#
# Key insight:
# - Numbers 1 to 2^k - 1: each bit position i (0..k-1) is set
#   exactly 2^(k-1) times. Total = k * 2^(k-1).
# - For N = 2^k + r where 0 <= r < 2^k:
#     total(N) = total(2^k - 1)       <- full block below 2^k
#              + (r + 1)              <- MSB (bit k) is set for 2^k..N
#              + total(r)             <- lower bits in 2^k..N mirror 0..r
# ============================================================
def _count_upto_power_minus1(k: int) -> int:
    """Total set bits in 1..2^k - 1."""
    # Each of the k bit positions is set in exactly 2^(k-1) numbers
    if k == 0:
        return 0  # 1..0 is empty range
    return k * (1 << (k - 1))


def optimal(n: int) -> int:
    """Pattern-based counting using highest power of 2."""
    if n <= 0:
        return 0

    # Find largest k such that 2^k <= n
    k = n.bit_length() - 1  # 2^k <= n < 2^(k+1)
    power = 1 << k

    if n == power - 1:
        # n is exactly 2^k - 1, use formula directly
        return _count_upto_power_minus1(k)

    # n = power + r where r = n - power
    r = n - power
    # Bits in 1..power-1
    full_block = _count_upto_power_minus1(k)
    # MSB (bit k) is set for all numbers power..n, that's (r+1) numbers
    msb_contribution = r + 1
    # Lower bits in power..n mirror exactly 0..r; set bits in 0..r = set bits in 1..r (0 has no bits)
    lower_bits = optimal(r)

    return full_block + msb_contribution + lower_bits


# ============================================================
# APPROACH 3: BEST
# Time: O(log n)  |  Space: O(1)
#
# Iterative version of the same formula. Instead of recursing,
# use a loop: at each step find the highest bit of n, subtract
# the contribution of the full lower block, account for the MSB
# column, then continue with the remainder.
# This avoids recursion overhead and is truly O(log n).
# ============================================================
def best(n: int) -> int:
    """Iterative bit-pattern counting in O(log n)."""
    total = 0
    while n > 0:
        k = n.bit_length() - 1   # largest k with 2^k <= n
        power = 1 << k
        r = n - power

        # Full block 1..2^k - 1
        total += _count_upto_power_minus1(k)
        # MSB column contribution: numbers power..n all have bit k set
        total += r + 1
        # Continue with remainder (mirrors 0..r)
        n = r

    return total


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Count Total Set Bits from 1 to N ===\n")

    tests = [
        (1,  1),
        (2,  2),
        (3,  4),
        (4,  5),
        (5,  7),
        (6,  9),
        (7, 12),
        (8, 13),
        (17, 35),
        (100, 319),
    ]

    for n, expected in tests:
        b = brute_force(n)
        o = optimal(n)
        r = best(n)
        status = "PASS" if b == o == r == expected else "FAIL"
        print(f"N={n:<5d}  Expected={expected:<5d}  "
              f"Brute={b:<5d}  Optimal={o:<5d}  Best={r:<5d}  [{status}]")
