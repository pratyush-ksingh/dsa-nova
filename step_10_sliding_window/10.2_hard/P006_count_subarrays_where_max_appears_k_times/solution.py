"""
Problem: Count Subarrays Where Max Appears K Times
Difficulty: HARD | XP: 50

Count subarrays where the global maximum of the entire array appears
at least k times in that subarray.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Check every subarray
# Time: O(N^2)  |  Space: O(1)
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    """Enumerate all subarrays and count those with global max >= k times."""
    n = len(nums)
    global_max = max(nums)
    count = 0
    for i in range(n):
        max_cnt = 0
        for j in range(i, n):
            if nums[j] == global_max:
                max_cnt += 1
            if max_cnt >= k:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL - Positions array + range counting
# Time: O(N)  |  Space: O(N)
# Collect all positions of global max. For each group of k consecutive
# positions, count valid (left, right) pairs.
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    """
    positions[i] = index of i-th occurrence of global max.
    For the r-th window of k maxes [r-k+1 .. r]:
      right end ranges in [positions[r], positions[r+1)-1]
      left start ranges in [prev_pos+1, positions[r-k+1]]
    """
    n = len(nums)
    global_max = max(nums)
    positions = [i for i, x in enumerate(nums) if x == global_max]
    m = len(positions)
    if m < k:
        return 0

    count = 0
    for r in range(k - 1, m):
        right_end = positions[r + 1] - 1 if r + 1 < m else n - 1
        left_start = positions[r - k] + 1 if r - k >= 0 else 0
        left_bound = positions[r - k + 1]
        count += (right_end - positions[r] + 1) * (left_bound - left_start + 1)
    return count


# ============================================================
# APPROACH 3: BEST - Sliding window with running positions list
# Time: O(N)  |  Space: O(N) for positions
# For each right end, maintain list of global_max positions seen so far.
# When list has >= k elements, all lefts from 0..positions[size-k] are valid.
# ============================================================
def best(nums: List[int], k: int) -> int:
    """
    For each right index, if we have seen >= k occurrences of global_max,
    the number of valid left starts = positions[len-k] + 1
    (left can be anywhere from 0 to that position, guaranteeing >= k maxes).
    """
    global_max = max(nums)
    positions = []  # positions of global_max seen so far
    result = 0
    for right, x in enumerate(nums):
        if x == global_max:
            positions.append(right)
        if len(positions) >= k:
            result += positions[-k] + 1
    return result


if __name__ == "__main__":
    print("=== Count Subarrays Where Max Appears K Times ===")
    tests = [
        ([1, 3, 2, 3, 1], 3, 6),
        ([1, 4, 2, 1], 3, 0),
        ([1, 3, 2, 3, 3], 2, 6),
        ([3, 3, 3], 2, 6),
    ]
    for nums, k, exp in tests:
        b = brute_force(nums, k)
        o = optimal(nums, k)
        be = best(nums, k)
        status = "OK" if b == o == be == exp else f"FAIL(b={b},o={o},be={be})"
        print(f"nums={nums}, k={k}: {b}|{o}|{be} (exp={exp}) [{status}]")
