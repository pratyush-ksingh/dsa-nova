"""
Problem: Find Two Numbers Appearing Once
Difficulty: MEDIUM | XP: 25

Given an array where every number appears exactly twice except for two numbers
which appear exactly once, find those two numbers.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — HashMap frequency count
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(nums: List[int]) -> List[int]:
    """
    Count frequency of each element with a dictionary.
    Return elements with frequency == 1.
    """
    freq = {}
    for n in nums:
        freq[n] = freq.get(n, 0) + 1
    return [k for k, v in freq.items() if v == 1]


# ============================================================
# APPROACH 2: OPTIMAL — XOR + partition by differentiating bit
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> List[int]:
    """
    Step 1: XOR all elements -> result is a XOR b (the two unique numbers).
            Duplicate pairs cancel out (x XOR x = 0).

    Step 2: Find any set bit in (a XOR b). This bit is set in exactly one
            of a or b (they differ at this position).

    Step 3: Partition all numbers into two groups by whether that bit is set.
            XOR each group separately -> one group gives a, the other gives b.
    """
    xor_all = 0
    for n in nums:
        xor_all ^= n        # xor_all = a XOR b

    # Find the rightmost set bit (any set bit works; rightmost is easiest)
    diff_bit = xor_all & (-xor_all)   # isolates lowest set bit

    a, b = 0, 0
    for n in nums:
        if n & diff_bit:
            a ^= n
        else:
            b ^= n

    return [a, b]


# ============================================================
# APPROACH 3: BEST — Same XOR approach (explicit bit scan)
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(nums: List[int]) -> List[int]:
    """
    Same XOR partition as Approach 2, but locates the differentiating bit
    by scanning bit positions explicitly (bit 0 to 31) for clarity.
    Preferred when you want to avoid the two's-complement trick.
    """
    xor_all = 0
    for n in nums:
        xor_all ^= n

    # Find position of any set bit in xor_all
    bit_pos = 0
    while not (xor_all >> bit_pos & 1):
        bit_pos += 1

    a, b = 0, 0
    for n in nums:
        if (n >> bit_pos) & 1:
            a ^= n
        else:
            b ^= n

    return sorted([a, b])


if __name__ == "__main__":
    print("=== Find Two Numbers Appearing Once ===")
    nums = [1, 2, 3, 2, 1, 4]          # unique: 3, 4
    print(f"Brute:   {brute_force(nums)}")
    print(f"Optimal: {optimal(nums)}")
    print(f"Best:    {best(nums)}")

    nums2 = [4, 1, 2, 1, 2, 3]         # unique: 3, 4
    print(f"\nBrute:   {brute_force(nums2)}")
    print(f"Optimal: {optimal(nums2)}")
    print(f"Best:    {best(nums2)}")
