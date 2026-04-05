"""
Problem: Number of Longest Increasing Subsequences (LeetCode 673)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Generate all subsequences
# Time: O(2^n * n)  |  Space: O(n)
# Find max LIS length, then count all subsequences of that length
# that are strictly increasing.
# ============================================================
def brute_force(nums: List[int]) -> int:
    n = len(nums)
    max_len = 0
    count = 0

    def backtrack(idx: int, last: int, length: int) -> None:
        nonlocal max_len, count
        if length > max_len:
            max_len = length
            count = 1
        elif length == max_len:
            count += 1
        for i in range(idx, n):
            if nums[i] > last:
                backtrack(i + 1, nums[i], length + 1)

    backtrack(0, float('-inf'), 0)
    return count


# ============================================================
# APPROACH 2: OPTIMAL -- DP with length[] and count[]
# Time: O(n^2)  |  Space: O(n)
# dp_len[i] = length of LIS ending at index i.
# dp_cnt[i] = number of LIS of that length ending at i.
# ============================================================
def optimal(nums: List[int]) -> int:
    n = len(nums)
    if n == 0:
        return 0

    dp_len = [1] * n
    dp_cnt = [1] * n

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                if dp_len[j] + 1 > dp_len[i]:
                    dp_len[i] = dp_len[j] + 1
                    dp_cnt[i] = dp_cnt[j]
                elif dp_len[j] + 1 == dp_len[i]:
                    dp_cnt[i] += dp_cnt[j]

    max_len = max(dp_len)
    return sum(dp_cnt[i] for i in range(n) if dp_len[i] == max_len)


# ============================================================
# APPROACH 3: BEST -- Same DP (O(n log n) with segment tree
# exists but is overkill; O(n^2) is the standard interview answer)
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def best(nums: List[int]) -> int:
    n = len(nums)
    if n == 0:
        return 0

    dp_len = [1] * n
    dp_cnt = [1] * n

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                new_len = dp_len[j] + 1
                if new_len > dp_len[i]:
                    dp_len[i] = new_len
                    dp_cnt[i] = dp_cnt[j]
                elif new_len == dp_len[i]:
                    dp_cnt[i] += dp_cnt[j]

    max_len = max(dp_len)
    return sum(c for l, c in zip(dp_len, dp_cnt) if l == max_len)


if __name__ == "__main__":
    print("=== Number of Longest Increasing Subsequences ===\n")

    nums1 = [1, 3, 5, 4, 7]
    print(f"Brute:   {brute_force(nums1)}")   # 2
    print(f"Optimal: {optimal(nums1)}")         # 2
    print(f"Best:    {best(nums1)}")            # 2

    nums2 = [2, 2, 2, 2, 2]
    print(f"\nBrute:   {brute_force(nums2)}")  # 5
    print(f"Optimal: {optimal(nums2)}")          # 5
    print(f"Best:    {best(nums2)}")             # 5

    nums3 = [1, 2, 4, 3, 5, 4, 7, 2]
    print(f"\nBrute:   {brute_force(nums3)}")   # 3
    print(f"Optimal: {optimal(nums3)}")           # 3
    print(f"Best:    {best(nums3)}")              # 3

    # Single element
    print(f"\nSingle:  {best([5])}")  # 1
