"""
Problem: Sum of Pairwise Hamming Distance
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an integer array A of N elements, find the sum of bit differences
among all pairs (i, j) where i != j. The result must be taken modulo 10^9+7.

Hamming distance between two integers = number of bit positions where they differ.
"""
from typing import List

MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2 * 32)  |  Space: O(1)
# ============================================================
def brute_force(A: List[int]) -> int:
    """
    For every pair (i, j) with i != j, compute hamming distance by XOR-ing
    and counting set bits, then accumulate the total.
    """
    n = len(A)
    total = 0
    for i in range(n):
        for j in range(n):
            if i != j:
                xor = A[i] ^ A[j]
                total += bin(xor).count('1')
    return total % MOD


# ============================================================
# APPROACH 2: OPTIMAL — Bit-by-bit contribution
# Time: O(32 * n)  |  Space: O(1)
# ============================================================
def optimal(A: List[int]) -> int:
    """
    For each bit position b (0..31):
      - Count how many numbers have that bit set: c
      - Numbers with bit 0: n - c
      - Pairs that differ at this bit: c * (n - c)
      - Each such pair contributes 2 (ordered pairs (i,j) and (j,i))
      So contribution = 2 * c * (n - c)

    Sum over all 32 bits gives the answer.
    """
    n = len(A)
    total = 0
    for b in range(32):
        ones = sum(1 for x in A if (x >> b) & 1)
        zeros = n - ones
        total += 2 * ones * zeros
    return total % MOD


# ============================================================
# APPROACH 3: BEST — Same bit-by-bit, slightly cleaner
# Time: O(32 * n)  |  Space: O(1)
# ============================================================
def best(A: List[int]) -> int:
    """
    Identical complexity to optimal. Written compactly using a single pass
    per bit and early computation inline. The insight: ordered pair count
    for differing pairs at bit b = 2 * c * (n - c).
    """
    n = len(A)
    total = 0
    for b in range(32):
        c = 0
        for x in A:
            c += (x >> b) & 1
        total = (total + 2 * c * (n - c)) % MOD
    return total


if __name__ == "__main__":
    print("=== Sum of Pairwise Hamming Distance ===")
    A1 = [1, 3, 5]
    # Expected: HD(1,3)=1, HD(1,5)=1, HD(3,5)=2 => each counted twice = (1+1+2)*2=8
    print(f"Input: {A1}")
    print(f"Brute:   {brute_force(A1)}")
    print(f"Optimal: {optimal(A1)}")
    print(f"Best:    {best(A1)}")

    A2 = [4, 14, 2]
    print(f"\nInput: {A2}")
    print(f"Brute:   {brute_force(A2)}")
    print(f"Optimal: {optimal(A2)}")
    print(f"Best:    {best(A2)}")
