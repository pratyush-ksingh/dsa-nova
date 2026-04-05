"""
Problem: 3 Sum
LeetCode 15 | Difficulty: HARD | XP: 30

Given an integer array nums, return all unique triplets [a, b, c] such that
a + b + c == 0.  The solution set must not contain duplicate triplets.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE  -  Triple nested loops + set for dedup
# Time: O(n^3)  |  Space: O(k)  where k = number of unique triplets
# ============================================================
def brute_force(nums: List[int]) -> List[List[int]]:
    """
    Try every combination of three indices.
    Sort each triplet before inserting into a set to deduplicate.
    """
    n = len(nums)
    seen = set()
    result = []

    for i in range(n - 2):
        for j in range(i + 1, n - 1):
            for k in range(j + 1, n):
                if nums[i] + nums[j] + nums[k] == 0:
                    triplet = tuple(sorted([nums[i], nums[j], nums[k]]))
                    if triplet not in seen:
                        seen.add(triplet)
                        result.append(list(triplet))

    return result


# ============================================================
# APPROACH 2: OPTIMAL  -  Sort + fix i + two pointers
# Time: O(n^2)  |  Space: O(1) extra  (output not counted)
# ============================================================
def optimal(nums: List[int]) -> List[List[int]]:
    """
    Sort the array.  Fix the first element at index i.
    Use two pointers (lo = i+1, hi = n-1) to find pairs that sum to -nums[i].
    Skip duplicate values for i to avoid duplicate triplets.
    """
    nums.sort()
    n = len(nums)
    result = []

    for i in range(n - 2):
        # Skip duplicate first elements
        if i > 0 and nums[i] == nums[i - 1]:
            continue
        # Early exit: smallest possible sum already positive
        if nums[i] > 0:
            break

        lo, hi = i + 1, n - 1
        while lo < hi:
            s = nums[i] + nums[lo] + nums[hi]
            if s == 0:
                result.append([nums[i], nums[lo], nums[hi]])
                # Skip duplicates for lo and hi
                while lo < hi and nums[lo] == nums[lo + 1]:
                    lo += 1
                while lo < hi and nums[hi] == nums[hi - 1]:
                    hi -= 1
                lo += 1
                hi -= 1
            elif s < 0:
                lo += 1
            else:
                hi -= 1

    return result


# ============================================================
# APPROACH 3: BEST  -  Same two-pointer, with hash-set for second pass
# Time: O(n^2)  |  Space: O(n) for the inner set (marginal improvement
#                            in readability; complexity same as optimal)
# ============================================================
def best(nums: List[int]) -> List[List[int]]:
    """
    Sort array; fix i; use a hash set to find the complement of nums[lo]
    without needing the hi pointer.  Functionally equivalent to optimal
    but demonstrates the hash-set pattern sometimes asked in interviews.
    """
    nums.sort()
    n = len(nums)
    result = []

    for i in range(n - 2):
        if i > 0 and nums[i] == nums[i - 1]:
            continue
        if nums[i] > 0:
            break

        seen_inner = set()
        j = i + 1
        while j < n:
            complement = -nums[i] - nums[j]
            if complement in seen_inner:
                result.append([nums[i], complement, nums[j]])
                # Skip duplicates for nums[j]
                while j + 1 < n and nums[j] == nums[j + 1]:
                    j += 1
            seen_inner.add(nums[j])
            j += 1

    return result


if __name__ == "__main__":
    print("=== 3 Sum ===")
    nums1 = [-1, 0, 1, 2, -1, -4]
    nums2 = [0, 1, 1]
    nums3 = [0, 0, 0]
    print(f"Brute   {nums1}: {brute_force(nums1)}")   # [[-1,-1,2],[-1,0,1]]
    print(f"Optimal {nums2}: {optimal(nums2)}")        # []
    print(f"Best    {nums3}: {best(nums3)}")           # [[0,0,0]]
