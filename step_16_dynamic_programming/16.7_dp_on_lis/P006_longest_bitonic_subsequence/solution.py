"""
Problem: Longest Bitonic Subsequence
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Try all subsequences
# Time: O(2^n * n)  |  Space: O(n)
# Generate all subsequences, check if bitonic, track maximum length.
# ============================================================
def brute_force(nums: List[int]) -> int:
    n = len(nums)
    best_len = 1

    def is_bitonic(subseq: List[int]) -> bool:
        if len(subseq) < 1:
            return False
        i = 0
        # Increasing phase
        while i + 1 < len(subseq) and subseq[i] < subseq[i + 1]:
            i += 1
        if i == 0:
            return False  # no increasing part (pure decreasing is not bitonic)
        if i == len(subseq) - 1:
            return False  # pure increasing (no decreasing part)
        # Decreasing phase
        while i + 1 < len(subseq) and subseq[i] > subseq[i + 1]:
            i += 1
        return i == len(subseq) - 1

    def backtrack(idx: int, current: List[int]) -> None:
        nonlocal best_len
        if len(current) >= 3 and is_bitonic(current):
            best_len = max(best_len, len(current))
        for i in range(idx, n):
            current.append(nums[i])
            backtrack(i + 1, current)
            current.pop()

    backtrack(0, [])
    return best_len


# ============================================================
# APPROACH 2: OPTIMAL -- LIS from left + LIS from right
# Time: O(n^2)  |  Space: O(n)
# lis[i] = length of LIS ending at i (left to right).
# lds[i] = length of LIS starting at i (right to left = LDS from right).
# Answer = max(lis[i] + lds[i] - 1) where both > 1.
# ============================================================
def optimal(nums: List[int]) -> int:
    n = len(nums)
    if n == 0:
        return 0

    # LIS ending at each index (increasing from left)
    lis = [1] * n
    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                lis[i] = max(lis[i], lis[j] + 1)

    # LDS starting at each index (decreasing to right = LIS from right)
    lds = [1] * n
    for i in range(n - 2, -1, -1):
        for j in range(i + 1, n):
            if nums[j] < nums[i]:
                lds[i] = max(lds[i], lds[j] + 1)

    # Combine: need strictly increasing then strictly decreasing
    ans = 0
    for i in range(n):
        if lis[i] > 1 and lds[i] > 1:
            ans = max(ans, lis[i] + lds[i] - 1)

    return ans if ans > 0 else 1


# ============================================================
# APPROACH 3: BEST -- Same O(n^2) with O(n log n) LIS possible
# Time: O(n^2)  |  Space: O(n)
# Identical algorithm to Approach 2 but written more concisely.
# (O(n log n) LIS via patience sorting would make this O(n log n)
# but requires careful reconstruction -- O(n^2) is standard in interviews.)
# ============================================================
def best(nums: List[int]) -> int:
    n = len(nums)
    if n == 0:
        return 0

    lis = [1] * n
    lds = [1] * n

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                lis[i] = max(lis[i], lis[j] + 1)

    for i in range(n - 2, -1, -1):
        for j in range(i + 1, n):
            if nums[j] < nums[i]:
                lds[i] = max(lds[i], lds[j] + 1)

    return max((lis[i] + lds[i] - 1 for i in range(n)
                if lis[i] > 1 and lds[i] > 1), default=1)


if __name__ == "__main__":
    print("=== Longest Bitonic Subsequence ===\n")

    nums1 = [1, 11, 2, 10, 4, 5, 2, 1]
    print(f"Brute:   {brute_force(nums1)}")   # 6
    print(f"Optimal: {optimal(nums1)}")         # 6
    print(f"Best:    {best(nums1)}")            # 6

    nums2 = [12, 11, 40, 5, 3, 1]
    print(f"\nBrute:   {brute_force(nums2)}")  # 5
    print(f"Optimal: {optimal(nums2)}")          # 5
    print(f"Best:    {best(nums2)}")             # 5

    nums3 = [80, 60, 30]
    print(f"\nPure decreasing: {best(nums3)}")   # 1

    nums4 = [1, 2, 3, 4, 5]
    print(f"Pure increasing: {best(nums4)}")      # 1
