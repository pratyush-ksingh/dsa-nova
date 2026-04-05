"""
Problem: Two Out of Three
Difficulty: EASY | XP: 10
Source: InterviewBit (LeetCode 2032)

Given three integer arrays nums1, nums2, nums3, return a sorted list of all
values that are present in at least two of the three arrays.

Example:
  nums1=[1,1,3,2], nums2=[2,3], nums3=[3]  ->  [2, 3]
  nums1=[3,1], nums2=[2,3], nums3=[1,2]    ->  [1, 2, 3]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Search Across Arrays
# Time: O((n+m+k) * max_val) roughly O(n*m + n*k + m*k)  |  Space: O(n+m+k)
# For every element in each array, check if it appears in at
# least one other array using linear scan. Collect uniques.
# ============================================================
def brute_force(nums1: List[int], nums2: List[int], nums3: List[int]) -> List[int]:
    arrays = [nums1, nums2, nums3]
    candidates = set(nums1) | set(nums2) | set(nums3)
    result = []
    for val in candidates:
        count = sum(1 for arr in arrays if val in arr)
        if count >= 2:
            result.append(val)
    return sorted(result)


# ============================================================
# APPROACH 2: OPTIMAL -- Pairwise Set Intersections Union
# Time: O(n + m + k)  |  Space: O(n + m + k)
# Convert each array to a set; union the three pairwise
# intersections gives exactly elements in >= 2 arrays.
# ============================================================
def optimal(nums1: List[int], nums2: List[int], nums3: List[int]) -> List[int]:
    s1, s2, s3 = set(nums1), set(nums2), set(nums3)
    result = (s1 & s2) | (s1 & s3) | (s2 & s3)
    return sorted(result)


# ============================================================
# APPROACH 3: BEST -- Occurrence Counting with a Single Dict
# Time: O(n + m + k)  |  Space: O(n + m + k)
# For each array, add its distinct values to a counter dict.
# Elements with count >= 2 appear in at least 2 arrays.
# Single pass over each array; most concise and general.
# ============================================================
def best(nums1: List[int], nums2: List[int], nums3: List[int]) -> List[int]:
    from collections import defaultdict
    count: dict = defaultdict(int)
    for arr in [nums1, nums2, nums3]:
        for val in set(arr):       # set() deduplicates within same array
            count[val] += 1
    return sorted(val for val, cnt in count.items() if cnt >= 2)


if __name__ == "__main__":
    a, b, c = [1, 1, 3, 2], [2, 3], [3]
    print("=== Two Out of Three ===")
    print(f"Brute:   {brute_force(a, b, c)}")
    print(f"Optimal: {optimal(a, b, c)}")
    print(f"Best:    {best(a, b, c)}")

    a2, b2, c2 = [3, 1], [2, 3], [1, 2]
    print(f"Brute:   {brute_force(a2, b2, c2)}")
    print(f"Optimal: {optimal(a2, b2, c2)}")
    print(f"Best:    {best(a2, b2, c2)}")
