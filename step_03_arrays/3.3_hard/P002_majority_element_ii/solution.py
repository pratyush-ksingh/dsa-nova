"""
Problem: Majority Element II (LeetCode #229)
Difficulty: HARD | XP: 40

Given an integer array of size n, find all elements that appear more than
floor(n/3) times. There can be at most 2 such elements.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Count each element)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(nums: List[int]) -> List[int]:
    """For each element, count its occurrences. Collect those > n/3."""
    n = len(nums)
    result = []
    seen = set()

    for i in range(n):
        if nums[i] in seen:
            continue
        seen.add(nums[i])
        count = 0
        for j in range(n):
            if nums[j] == nums[i]:
                count += 1
        if count > n // 3:
            result.append(nums[i])

    return sorted(result)


# ============================================================
# APPROACH 2: OPTIMAL (Boyer-Moore Extended Voting - 2 candidates)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(nums: List[int]) -> List[int]:
    """
    Extended Boyer-Moore: maintain 2 candidates and their counts.
    Phase 1: Find potential candidates.
    Phase 2: Verify candidates actually appear > n/3 times.
    """
    n = len(nums)
    cand1, cand2 = None, None
    cnt1, cnt2 = 0, 0

    # Phase 1: Candidate selection
    for num in nums:
        if num == cand1:
            cnt1 += 1
        elif num == cand2:
            cnt2 += 1
        elif cnt1 == 0:
            cand1, cnt1 = num, 1
        elif cnt2 == 0:
            cand2, cnt2 = num, 1
        else:
            # Cancel one vote from each bucket
            cnt1 -= 1
            cnt2 -= 1

    # Phase 2: Verify
    cnt1 = nums.count(cand1)
    cnt2 = nums.count(cand2)

    result = []
    if cnt1 > n // 3:
        result.append(cand1)
    if cnt2 > n // 3 and cand2 != cand1:
        result.append(cand2)

    return sorted(result)


# ============================================================
# APPROACH 3: BEST (Same Boyer-Moore, single verification pass)
# Time: O(n) | Space: O(1)
# ============================================================
def best(nums: List[int]) -> List[int]:
    """
    Same algorithm as Approach 2, but verification uses a manual count
    loop (single pass) instead of list.count() (which is also O(n) but
    called twice). Conceptually cleaner -- one pass each phase.
    """
    n = len(nums)
    cand1, cand2 = None, None
    cnt1, cnt2 = 0, 0

    # Phase 1: Find 2 surviving candidates
    for num in nums:
        if num == cand1:
            cnt1 += 1
        elif num == cand2:
            cnt2 += 1
        elif cnt1 == 0:
            cand1, cnt1 = num, 1
        elif cnt2 == 0:
            cand2, cnt2 = num, 1
        else:
            cnt1 -= 1
            cnt2 -= 1

    # Phase 2: Count exact frequencies in a single pass
    cnt1, cnt2 = 0, 0
    for num in nums:
        if num == cand1:
            cnt1 += 1
        elif num == cand2:
            cnt2 += 1

    result = []
    if cnt1 > n // 3:
        result.append(cand1)
    if cnt2 > n // 3:
        result.append(cand2)

    return sorted(result)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Majority Element II ===\n")

    test_cases = [
        ([3, 2, 3], [3]),
        ([1], [1]),
        ([1, 2], [1, 2]),
        ([1, 1, 1, 3, 3, 2, 2, 2], [1, 2]),
        ([1, 2, 3, 4], []),
        ([0, 0, 0], [0]),
        ([1, 1, 2, 2, 3, 3, 4], []),  # n=7, threshold=2, each appears <= 2 times
    ]

    # Recalculate: [1,2,3,4] n=4, threshold floor(4/3)=1, need > 1 so need >= 2 occurrences
    # each appears once, so []
    # [1,1,2,2,3,3,4] n=7, threshold = 7//3 = 2, need > 2: none qualify

    for nums, expected in test_cases:
        b = brute_force(nums[:])
        o = optimal(nums[:])
        n = best(nums[:])
        status = "PASS" if b == expected and o == expected and n == expected else "FAIL"
        print(f"Input:    {nums}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {n}")
        print(f"  Expected: {expected}  [{status}]\n")
