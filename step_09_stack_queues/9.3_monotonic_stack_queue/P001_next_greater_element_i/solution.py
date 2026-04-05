"""
Problem: Next Greater Element I (LeetCode #496)
Difficulty: MEDIUM | XP: 25

Given nums1 (subset of nums2), for each element in nums1 find
the next greater element to its right in nums2.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Linear Scan
# Time: O(m * n)  |  Space: O(m)
#
# For each element in nums1, find it in nums2 and scan right
# for the first greater element.
# ============================================================
def brute_force(nums1: List[int], nums2: List[int]) -> List[int]:
    result = []
    for target in nums1:
        found = False
        nge = -1
        for val in nums2:
            if val == target:
                found = True
            if found and val > target:
                nge = val
                break
        result.append(nge)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Monotonic Stack Left-to-Right + HashMap
# Time: O(n + m)  |  Space: O(n)
#
# Traverse nums2 left-to-right with a decreasing stack.
# When a larger element arrives, pop smaller ones and record
# their NGE in a dictionary.
# ============================================================
def optimal(nums1: List[int], nums2: List[int]) -> List[int]:
    nge = {}
    stack = []

    for val in nums2:
        # Pop elements smaller than current -- they found their NGE
        while stack and stack[-1] < val:
            nge[stack.pop()] = val
        stack.append(val)

    # Remaining elements have no NGE
    while stack:
        nge[stack.pop()] = -1

    return [nge[x] for x in nums1]


# ============================================================
# APPROACH 3: BEST -- Monotonic Stack Right-to-Left + HashMap
# Time: O(n + m)  |  Space: O(n)
#
# Traverse nums2 right-to-left. Stack holds "candidates" for
# NGE. Pop those <= current (they are blocked by current).
# Stack top is the answer. More intuitive reasoning.
# ============================================================
def best(nums1: List[int], nums2: List[int]) -> List[int]:
    nge = {}
    stack = []

    for i in range(len(nums2) - 1, -1, -1):
        val = nums2[i]
        # Pop elements that can never be NGE for anything at or left of i
        while stack and stack[-1] <= val:
            stack.pop()
        nge[val] = stack[-1] if stack else -1
        stack.append(val)

    return [nge[x] for x in nums1]


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Next Greater Element I ===\n")

    tests = [
        ([4, 1, 2], [1, 3, 4, 2], [-1, 3, -1]),
        ([2, 4], [1, 2, 3, 4], [3, -1]),
        ([1], [1], [-1]),
        ([1, 3, 5], [6, 5, 4, 3, 2, 1], [-1, -1, -1]),
    ]

    for nums1, nums2, expected in tests:
        print(f"nums1={nums1}, nums2={nums2}")
        b = brute_force(nums1, nums2)
        o = optimal(nums1, nums2)
        be = best(nums1, nums2)
        print(f"  Brute:   {b}  {'PASS' if b == expected else 'FAIL'}")
        print(f"  Optimal: {o}  {'PASS' if o == expected else 'FAIL'}")
        print(f"  Best:    {be}  {'PASS' if be == expected else 'FAIL'}")
        print()
