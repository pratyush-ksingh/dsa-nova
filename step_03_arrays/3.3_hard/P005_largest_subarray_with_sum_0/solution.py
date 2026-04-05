"""Problem: Largest Subarray with Sum 0
Difficulty: MEDIUM | XP: 25

Find the length of the longest subarray with sum 0.
Uses prefix sums and a HashMap to detect zero-sum subarrays in O(n).
Real-life use: finding balanced intervals in financial ledgers.
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Try all subarrays
# ============================================================
def brute_force(arr: List[int]) -> int:
    n = len(arr)
    max_len = 0
    for i in range(n):
        total = 0
        for j in range(i, n):
            total += arr[j]
            if total == 0:
                max_len = max(max_len, j - i + 1)
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL (Prefix Sum + HashMap)
# Time: O(n)  |  Space: O(n)
# If prefix[j] == prefix[i], then sum(i+1..j) == 0.
# Store FIRST occurrence for maximum length.
# ============================================================
def optimal(arr: List[int]) -> int:
    first_seen = {0: -1}
    total = 0
    max_len = 0
    for i, v in enumerate(arr):
        total += v
        if total in first_seen:
            max_len = max(max_len, i - first_seen[total])
        else:
            first_seen[total] = i
    return max_len


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# Same as optimal — cleaner with .get() avoiding double lookup
# ============================================================
def best(arr: List[int]) -> int:
    prefix_map = {0: -1}
    prefix = 0
    max_len = 0
    for i, v in enumerate(arr):
        prefix += v
        prev = prefix_map.get(prefix)
        if prev is not None:
            max_len = max(max_len, i - prev)
        else:
            prefix_map[prefix] = i
    return max_len


if __name__ == "__main__":
    cases = [
        ([15, -2, 2, -8, 1, 7, 10, 23], 5),
        ([1, 2, 3], 0),
        ([0, 0, 0], 3),
        ([1, -1, 3, -3, 2], 4),
        ([3, -3, 1, -1, 2], 4),
    ]
    print("=== Largest Subarray with Sum 0 ===")
    for arr, exp in cases:
        b = brute_force(arr[:])
        o = optimal(arr[:])
        bst = best(arr[:])
        ok = "OK" if b == o == bst == exp else f"MISMATCH(BF={b},OPT={o},BEST={bst})"
        print(f"arr={arr} => {ok}  EXP={exp}")
