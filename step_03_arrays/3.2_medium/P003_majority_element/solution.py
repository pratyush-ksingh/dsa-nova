"""
Problem: Majority Element
LeetCode 169 | Difficulty: MEDIUM | XP: 25

Find the element that appears more than n/2 times in an array.
Guaranteed to exist.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²)  |  Space: O(1)
# For each element, count its occurrences. Return the one > n/2.
# ============================================================
def brute_force(nums: List[int]) -> int:
    n = len(nums)
    for i in range(n):
        count = 0
        for j in range(n):
            if nums[j] == nums[i]:
                count += 1
        if count > n // 2:
            return nums[i]
    return -1  # guaranteed not reached


# ============================================================
# APPROACH 2: OPTIMAL  (HashMap / Frequency Count)
# Time: O(n)  |  Space: O(n)
# Count frequencies in a hash map; return the element whose
# count exceeds n/2.
# ============================================================
def optimal(nums: List[int]) -> int:
    count = {}
    n = len(nums)
    for num in nums:
        count[num] = count.get(num, 0) + 1
        if count[num] > n // 2:
            return num
    return -1  # guaranteed not reached


# ============================================================
# APPROACH 3: BEST  (Boyer-Moore Voting Algorithm)
# Time: O(n)  |  Space: O(1)
#
# Key insight: if the majority element exists (appears > n/2
# times), it can "survive" a pairing-cancel process.
# - Maintain a candidate and a vote count.
# - When count reaches 0, adopt the current element as the
#   new candidate.
# - Matching the candidate increments count; any other element
#   decrements it.
# The surviving candidate is the majority element (because its
# excess votes can never be fully cancelled).
# ============================================================
def best(nums: List[int]) -> int:
    candidate = nums[0]
    votes = 1
    for i in range(1, len(nums)):
        if votes == 0:
            candidate = nums[i]
            votes = 1
        elif nums[i] == candidate:
            votes += 1
        else:
            votes -= 1
    return candidate


# ============================================================
# QUICK SELF-TEST
# ============================================================
if __name__ == "__main__":
    tests = [
        ([3, 2, 3], 3),
        ([2, 2, 1, 1, 1, 2, 2], 2),
        ([1], 1),
        ([6, 5, 5], 5),
    ]

    print("=== Majority Element ===")
    for nums, expected in tests:
        b = brute_force(nums[:])
        o = optimal(nums[:])
        best_ans = best(nums[:])
        status = "PASS" if b == o == best_ans == expected else "FAIL"
        print(f"[{status}] nums={nums}  brute={b}  optimal={o}  best={best_ans}  expected={expected}")
