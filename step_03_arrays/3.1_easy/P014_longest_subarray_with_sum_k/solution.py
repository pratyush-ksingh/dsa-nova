"""Problem: Longest Subarray with Sum K
Difficulty: MEDIUM | XP: 25

Given an array that may contain negatives, find the length of the
longest subarray whose sum equals k.
Real-life use: finding the longest period where net cash flow equals a target.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Check every (i,j) pair — no early exit since negatives allowed
# ============================================================
def brute_force(arr: List[int], k: int) -> int:
    n = len(arr)
    max_len = 0
    for i in range(n):
        total = 0
        for j in range(i, n):
            total += arr[j]
            if total == k:
                max_len = max(max_len, j - i + 1)
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL (Prefix Sum + HashMap)
# Time: O(n)  |  Space: O(n)
# prefix[j] - prefix[i] = k  =>  look up prefix[i] = prefix[j] - k
# Store FIRST occurrence of each prefix sum to maximize window
# ============================================================
def optimal(arr: List[int], k: int) -> int:
    prefix_map = {0: -1}
    total = 0
    max_len = 0
    for i, val in enumerate(arr):
        total += val
        if total - k in prefix_map:
            max_len = max(max_len, i - prefix_map[total - k])
        if total not in prefix_map:
            prefix_map[total] = i
    return max_len


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# Same algorithm — this is the optimal for negative arrays.
# Slight optimization: use get() to avoid double lookup.
# ============================================================
def best(arr: List[int], k: int) -> int:
    first_seen = {0: -1}
    total = 0
    max_len = 0
    for i, val in enumerate(arr):
        total += val
        prev = first_seen.get(total - k)
        if prev is not None:
            max_len = max(max_len, i - prev)
        if total not in first_seen:
            first_seen[total] = i
    return max_len


if __name__ == "__main__":
    cases = [
        ([10, 5, 2, 7, 1, -10], 15, 6),
        ([-5, 8, -14, 2, 4, 12], -5, 5),
        ([0, 0, 5, 5, 0, 0], 5, 3),  # [0,0,5] at index 0..2, len=3
        ([1, -1, 5, -2, 3], 3, 4),
        ([1, 2, 3], 6, 3),
    ]
    print("=== Longest Subarray with Sum K (With Negatives) ===")
    for arr, k, exp in cases:
        b = brute_force(arr[:], k)
        o = optimal(arr[:], k)
        bst = best(arr[:], k)
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"arr={arr} k={k} => {ok}  EXP={exp}")
