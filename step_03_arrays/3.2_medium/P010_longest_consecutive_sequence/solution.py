"""
Problem: Longest Consecutive Sequence
LeetCode 128 | Difficulty: MEDIUM | XP: 25

Given an unsorted array of integers, find the length of the
longest sequence of consecutive integers.
Must run in O(n) time.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  (Sort + Linear Scan)
# Time: O(n log n)  |  Space: O(1)  (or O(n) if sort is not in-place)
#
# Sort the array, then scan to find the longest consecutive run.
# Skip duplicates while scanning.
# ============================================================
def brute_force(nums: List[int]) -> int:
    if not nums:
        return 0
    nums_sorted = sorted(nums)
    longest = 1
    current = 1
    for i in range(1, len(nums_sorted)):
        if nums_sorted[i] == nums_sorted[i - 1]:
            continue          # duplicate – skip, don't break streak
        if nums_sorted[i] == nums_sorted[i - 1] + 1:
            current += 1
            longest = max(longest, current)
        else:
            current = 1       # streak broken
    return longest


# ============================================================
# APPROACH 2: OPTIMAL  (HashSet – start of sequence detection)
# Time: O(n)  |  Space: O(n)
#
# Key insight: only start counting a sequence from its *lowest*
# element. An element x is the start of a sequence iff (x-1) is
# NOT in the set.  Counting from every non-start is wasteful; this
# check makes the inner while-loop execute at most n times total
# across all outer iterations → O(n) overall.
# ============================================================
def optimal(nums: List[int]) -> int:
    if not nums:
        return 0
    num_set = set(nums)
    longest = 0
    for num in num_set:
        # Only start counting from the beginning of a sequence
        if num - 1 not in num_set:
            current_num = num
            length = 1
            while current_num + 1 in num_set:
                current_num += 1
                length += 1
            longest = max(longest, length)
    return longest


# ============================================================
# APPROACH 3: BEST  (same HashSet, minor style improvement)
# Time: O(n)  |  Space: O(n)
#
# Identical complexity to Approach 2. Written slightly more
# compactly and iterating over the original list (so duplicates
# in the input are skipped naturally when checking the set).
# ============================================================
def best(nums: List[int]) -> int:
    num_set = set(nums)
    longest = 0
    for num in num_set:
        if num - 1 not in num_set:
            length = 1
            while num + length in num_set:
                length += 1
            longest = max(longest, length)
    return longest


# ============================================================
# QUICK SELF-TEST
# ============================================================
if __name__ == "__main__":
    tests = [
        ([100, 4, 200, 1, 3, 2], 4),      # 1-2-3-4
        ([0, 3, 7, 2, 5, 8, 4, 6, 0, 1], 9),  # 0-8
        ([], 0),
        ([1], 1),
        ([1, 2, 0, 1], 3),                 # with duplicate
    ]

    print("=== Longest Consecutive Sequence ===")
    for nums, expected in tests:
        b = brute_force(nums[:])
        o = optimal(nums[:])
        be = best(nums[:])
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"[{status}] nums={nums}  brute={b}  optimal={o}  best={be}  expected={expected}")
