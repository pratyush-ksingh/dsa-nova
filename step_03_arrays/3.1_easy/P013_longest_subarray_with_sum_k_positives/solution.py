"""Problem: Longest Subarray with Sum K (Positives)
Difficulty: MEDIUM | XP: 25

Given an array of positive integers, find the length of the
longest subarray whose sum equals k.
Real-life use: finding longest window of activity within a time budget.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Try all subarrays — early termination when sum exceeds k
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
            if total > k:
                break  # positives only: sum can only grow
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL (Prefix Sum + HashMap)
# Time: O(n)  |  Space: O(n)
# Works for arrays with negatives too.
# prefix[i] - prefix[j] = k  =>  prefix[j] = prefix[i] - k
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
# APPROACH 3: BEST (Two Pointers / Sliding Window)
# Time: O(n)  |  Space: O(1)
# Only valid for non-negative arrays. Shrink window from left
# when sum exceeds k.
# ============================================================
def best(arr: List[int], k: int) -> int:
    lo = 0
    total = 0
    max_len = 0
    for hi in range(len(arr)):
        total += arr[hi]
        while total > k and lo <= hi:
            total -= arr[lo]
            lo += 1
        if total == k:
            max_len = max(max_len, hi - lo + 1)
    return max_len


if __name__ == "__main__":
    cases = [
        ([1, 2, 3, 1, 1, 1, 1], 3, 3),   # [1,2] or [3] or [1,1,1] -> 3
        ([2, 3, 1, 2, 4, 3], 7, 3),        # [3,1,2,4,3]: [1,2,4]=7 len=3
        ([1, 1, 1, 1, 1], 3, 3),
        ([3, 1, 2, 4], 6, 3),              # [1,2,4]=7 no; [3,1,2]=6 len=3
        ([1, 2, 3], 6, 3),
    ]
    print("=== Longest Subarray with Sum K (Positives) ===")
    for arr, k, exp in cases:
        b = brute_force(arr[:], k)
        o = optimal(arr[:], k)
        bst = best(arr[:], k)
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"arr={arr} k={k} => {ok}  EXP={exp}")
