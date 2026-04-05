"""Problem: Maximum Product Subarray
Difficulty: MEDIUM | XP: 25

Find the contiguous subarray with the largest product.
Array can contain negatives and zeros.
Real-life use: maximising gain in multiplicative chains (compound interest windows).
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Try every (i,j) pair and compute product
# ============================================================
def brute_force(arr: List[int]) -> int:
    n = len(arr)
    max_prod = float('-inf')
    for i in range(n):
        prod = 1
        for j in range(i, n):
            prod *= arr[j]
            max_prod = max(max_prod, prod)
    return max_prod


# ============================================================
# APPROACH 2: OPTIMAL (Track curMax and curMin)
# Time: O(n)  |  Space: O(1)
# Negative * negative = positive, so always track both extremes.
# Swap curMax and curMin when element is negative.
# ============================================================
def optimal(arr: List[int]) -> int:
    cur_max = cur_min = max_prod = arr[0]
    for v in arr[1:]:
        if v < 0:
            cur_max, cur_min = cur_min, cur_max
        cur_max = max(v, cur_max * v)
        cur_min = min(v, cur_min * v)
        max_prod = max(max_prod, cur_max)
    return max_prod


# ============================================================
# APPROACH 3: BEST (Prefix & Suffix Product Scan)
# Time: O(n)  |  Space: O(1)
# Scan from both ends; the max product subarray must be a
# prefix or suffix once zero-boundaries are reset.
# ============================================================
def best(arr: List[int]) -> int:
    n = len(arr)
    max_prod = float('-inf')
    prefix = suffix = 1
    for i in range(n):
        prefix *= arr[i]
        suffix *= arr[n - 1 - i]
        max_prod = max(max_prod, prefix, suffix)
        if prefix == 0:
            prefix = 1
        if suffix == 0:
            suffix = 1
    return max_prod


if __name__ == "__main__":
    cases = [
        ([2, 3, -2, 4], 6),
        ([-2, 0, -1], 0),
        ([-2, 3, -4], 24),
        ([0, 2], 2),
        ([-2, -3, 0, -2, -40], 80),
    ]
    print("=== Maximum Product Subarray ===")
    for arr, exp in cases:
        b = brute_force(arr[:])
        o = optimal(arr[:])
        bst = best(arr[:])
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"arr={arr} => {ok}  EXP={exp}")
