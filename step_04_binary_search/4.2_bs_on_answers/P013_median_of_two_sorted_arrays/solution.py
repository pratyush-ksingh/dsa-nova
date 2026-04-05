"""
Problem: Median of Two Sorted Arrays
Difficulty: HARD | XP: 50

Given two sorted arrays nums1 and nums2, find the median of the combined
sorted array. Time complexity must be O(log(min(m, n))).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O((m+n) log(m+n))  |  Space: O(m+n)
# ============================================================
def brute_force(nums1: List[int], nums2: List[int]) -> float:
    """
    Merge both arrays, sort, then find median directly.
    Real-life: Combining sorted patient records to find median age/reading.
    """
    merged = sorted(nums1 + nums2)
    total = len(merged)
    if total % 2 == 1:
        return float(merged[total // 2])
    return (merged[total // 2 - 1] + merged[total // 2]) / 2.0


# ============================================================
# APPROACH 2: OPTIMAL  (Two-pointer merge)
# Time: O(m+n)  |  Space: O(1)
# ============================================================
def optimal(nums1: List[int], nums2: List[int]) -> float:
    """
    Two-pointer virtual merge until reaching median position.
    No extra space needed beyond pointer variables.
    Real-life: Finding median in sorted streaming data from two sources.
    """
    m, n = len(nums1), len(nums2)
    total = m + n
    target = total // 2
    i = j = 0
    prev = curr = -1

    for _ in range(target + 1):
        prev = curr
        if i < m and (j >= n or nums1[i] <= nums2[j]):
            curr = nums1[i]; i += 1
        else:
            curr = nums2[j]; j += 1

    if total % 2 == 1:
        return float(curr)
    return (prev + curr) / 2.0


# ============================================================
# APPROACH 3: BEST  (Binary Search — O(log(min(m,n))))
# Time: O(log(min(m,n)))  |  Space: O(1)
# ============================================================
def best(nums1: List[int], nums2: List[int]) -> float:
    """
    Binary search on the smaller array. Partition both arrays so that
    all elements in left partition <= all in right. The median is the
    boundary values.
    Real-life: Distributed statistics computation where merging full arrays is too costly.
    """
    if len(nums1) > len(nums2):
        return best(nums2, nums1)

    m, n = len(nums1), len(nums2)
    lo, hi = 0, m
    half_len = (m + n + 1) // 2

    while lo <= hi:
        cut1 = (lo + hi) // 2
        cut2 = half_len - cut1

        left1  = nums1[cut1 - 1] if cut1 > 0 else float('-inf')
        right1 = nums1[cut1]     if cut1 < m else float('inf')
        left2  = nums2[cut2 - 1] if cut2 > 0 else float('-inf')
        right2 = nums2[cut2]     if cut2 < n else float('inf')

        if left1 <= right2 and left2 <= right1:
            if (m + n) % 2 == 1:
                return float(max(left1, left2))
            return (max(left1, left2) + min(right1, right2)) / 2.0
        elif left1 > right2:
            hi = cut1 - 1
        else:
            lo = cut1 + 1

    return 0.0  # never reached with valid inputs


if __name__ == "__main__":
    print("=== Median of Two Sorted Arrays ===")
    tests = [
        ([1, 3],  [2],      2.0),
        ([1, 2],  [3, 4],   2.5),
        ([0, 0],  [0, 0],   0.0),
        ([2],     [],        2.0),
    ]
    for a, b, exp in tests:
        print(f"\nnums1={a}  nums2={b}  =>  expected: {exp:.5f}")
        print(f"  Brute:   {brute_force(a, b):.5f}")
        print(f"  Optimal: {optimal(a, b):.5f}")
        print(f"  Best:    {best(a, b):.5f}")
