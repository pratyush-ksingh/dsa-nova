"""Problem: Subarray with K Different Integers
Difficulty: HARD | XP: 50
"""
from collections import defaultdict
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(N^2)  |  Space: O(N)
# Try every subarray, track distinct elements
# ============================================================
def brute_force(nums: List[int], k: int) -> int:
    count = 0
    n = len(nums)
    for i in range(n):
        distinct = set()
        for j in range(i, n):
            distinct.add(nums[j])
            if len(distinct) == k:
                count += 1
            elif len(distinct) > k:
                break
    return count


# ============================================================
# APPROACH 2: OPTIMAL - atMost(K) - atMost(K-1)
# Time: O(N)  |  Space: O(N)
# Subarrays with exactly K distinct = atMost(K) - atMost(K-1)
# ============================================================
def optimal(nums: List[int], k: int) -> int:
    def at_most(k: int) -> int:
        freq = defaultdict(int)
        left = 0
        count = 0
        for right in range(len(nums)):
            freq[nums[right]] += 1
            while len(freq) > k:
                freq[nums[left]] -= 1
                if freq[nums[left]] == 0:
                    del freq[nums[left]]
                left += 1
            count += right - left + 1
        return count

    return at_most(k) - at_most(k - 1)


# ============================================================
# APPROACH 3: BEST - Single pass, two left pointers
# Time: O(N)  |  Space: O(N)
# Avoid two passes by maintaining both windows simultaneously
# ============================================================
def best(nums: List[int], k: int) -> int:
    freq1 = defaultdict(int)   # atMost(k)
    freq2 = defaultdict(int)   # atMost(k-1)
    left1 = left2 = count = 0
    for right in range(len(nums)):
        freq1[nums[right]] += 1
        freq2[nums[right]] += 1
        while len(freq1) > k:
            freq1[nums[left1]] -= 1
            if freq1[nums[left1]] == 0:
                del freq1[nums[left1]]
            left1 += 1
        while len(freq2) > k - 1:
            freq2[nums[left2]] -= 1
            if freq2[nums[left2]] == 0:
                del freq2[nums[left2]]
            left2 += 1
        # number of valid subarrays ending at right with exactly k distinct
        count += left2 - left1
    return count


if __name__ == "__main__":
    tests = [
        ([1, 2, 1, 2, 3], 2, 7),
        ([1, 2, 1, 3, 4], 3, 3),
        ([1, 2, 3], 1, 3),
        ([1], 1, 1),
    ]
    for nums, k, expected in tests:
        b = brute_force(nums, k)
        o = optimal(nums, k)
        be = best(nums, k)
        status = "OK" if b == o == be == expected else "FAIL"
        print(f"[{status}] nums={nums} k={k} -> Brute={b}, Optimal={o}, Best={be} (expected={expected})")
