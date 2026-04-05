"""Problem: Print Max Subarray Sum
Difficulty: MEDIUM | XP: 25

Find the contiguous subarray with the maximum sum (Kadane's Algorithm).
Return the max sum AND the actual subarray (store indices).
Real-life use: max profit window in stock trading, signal peak detection.
"""
from typing import List, Tuple

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Try every (start, end) pair
# ============================================================
def brute_force(arr: List[int]) -> Tuple[int, List[int]]:
    n = len(arr)
    max_sum = float('-inf')
    best_l = best_r = 0
    for i in range(n):
        total = 0
        for j in range(i, n):
            total += arr[j]
            if total > max_sum:
                max_sum = total
                best_l, best_r = i, j
    return max_sum, arr[best_l:best_r + 1]


# ============================================================
# APPROACH 2: OPTIMAL (Kadane's with index tracking)
# Time: O(n)  |  Space: O(1)
# Reset temp_start when current sum drops below 0
# ============================================================
def optimal(arr: List[int]) -> Tuple[int, List[int]]:
    n = len(arr)
    max_sum = float('-inf')
    cur_sum = 0
    best_l = best_r = temp_start = 0

    for i, v in enumerate(arr):
        cur_sum += v
        if cur_sum > max_sum:
            max_sum = cur_sum
            best_l = temp_start
            best_r = i
        if cur_sum < 0:
            cur_sum = 0
            temp_start = i + 1

    return max_sum, arr[best_l:best_r + 1]


# ============================================================
# APPROACH 3: BEST (Handles all-negative arrays properly)
# Time: O(n)  |  Space: O(1)
# Start curSum from arr[0]; branch instead of resetting to 0
# ============================================================
def best(arr: List[int]) -> Tuple[int, List[int]]:
    n = len(arr)
    max_sum = cur_sum = arr[0]
    best_l = best_r = temp_start = 0

    for i in range(1, n):
        if cur_sum < 0:
            cur_sum = arr[i]
            temp_start = i
        else:
            cur_sum += arr[i]
        if cur_sum > max_sum:
            max_sum = cur_sum
            best_l = temp_start
            best_r = i

    return max_sum, arr[best_l:best_r + 1]


if __name__ == "__main__":
    cases = [
        ([-2, 1, -3, 4, -1, 2, 1, -5, 4], 6, [4, -1, 2, 1]),
        ([1, 2, 3, 4, 5], 15, [1, 2, 3, 4, 5]),
        ([-1, -2, -3, -4], -1, [-1]),
        ([-2, -3, 4, -1, -2, 1, 5, -3], 7, [4, -1, -2, 1, 5]),
    ]
    print("=== Print Max Subarray Sum ===")
    for arr, exp_sum, exp_sub in cases:
        b_sum, b_sub = brute_force(arr[:])
        o_sum, o_sub = optimal(arr[:])
        bst_sum, bst_sub = best(arr[:])
        ok = "OK" if b_sum == o_sum == bst_sum == exp_sum else "MISMATCH"
        print(f"arr={arr}")
        print(f"  sum: BF={b_sum} OPT={o_sum} BEST={bst_sum} EXP={exp_sum} {ok}")
        print(f"  sub: BF={b_sub} OPT={o_sub} BEST={bst_sub}")
