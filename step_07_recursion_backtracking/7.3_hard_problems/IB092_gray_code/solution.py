"""
Problem: Gray Code
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Generate the n-bit Gray code sequence. A sequence of 2^n integers where
consecutive numbers (including last->first) differ by exactly 1 bit.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive Reflect-and-Prefix
# Time: O(2^N)  |  Space: O(2^N)
# G(1) = [0, 1]. G(n) = G(n-1) + [x | highBit for x in reversed(G(n-1))]
# ============================================================
def brute_force(n: int) -> List[int]:
    """Build Gray code by reflection: prefix 0 to G(n-1), then prefix 1 to its reverse."""
    if n == 0:
        return [0]
    prev = brute_force(n - 1)
    high = 1 << (n - 1)
    return prev + [high | x for x in reversed(prev)]


# ============================================================
# APPROACH 2: OPTIMAL - XOR formula: G(i) = i ^ (i >> 1)
# Time: O(2^N)  |  Space: O(2^N)
# The i-th element in the Gray code sequence is i XOR floor(i/2).
# ============================================================
def optimal(n: int) -> List[int]:
    """Direct formula: i-th Gray code = i XOR (i >> 1)."""
    return [i ^ (i >> 1) for i in range(1 << n)]


# ============================================================
# APPROACH 3: BEST - Iterative single-bit-flip construction
# Time: O(2^N)  |  Space: O(2^N)
# At step k, flip the bit corresponding to the lowest set bit of k.
# Builds sequence step by step with simple XOR operations.
# ============================================================
def best(n: int) -> List[int]:
    """
    Start from 0. At each step k, flip bit at position of (k & -k).
    This directly constructs the Gray code sequence one flip at a time.
    """
    total = 1 << n
    result = [0]
    cur = 0
    for k in range(1, total):
        cur ^= (k & -k)
        result.append(cur)
    return result


if __name__ == "__main__":
    print("=== Gray Code ===")
    for n in [1, 2, 3, 4]:
        b = brute_force(n)
        o = optimal(n)
        be = best(n)
        match = b == o == be
        print(f"n={n}: {b}  match={match}")
    # Verify Gray property: consecutive differ by 1 bit
    seq = optimal(4)
    seq_circ = seq + [seq[0]]
    valid = all(bin(a ^ b).count('1') == 1 for a, b in zip(seq_circ, seq_circ[1:]))
    print(f"n=4 Gray property valid: {valid}")
