"""
Problem: Fruits into Baskets (LeetCode #904)
Difficulty: MEDIUM | XP: 25

Find the longest subarray with at most 2 distinct values.
Key insight: Sliding window + frequency map with K=2 distinct limit.
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Subarrays)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(fruits: List[int]) -> int:
    """Try every starting index, extend as far as possible with <= 2 types."""
    n = len(fruits)
    ans = 0
    for i in range(n):
        types = set()
        for j in range(i, n):
            types.add(fruits[j])
            if len(types) > 2:
                break
            ans = max(ans, j - i + 1)
    return ans


# ============================================================
# APPROACH 2: OPTIMAL (Sliding Window -- Shrinking)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(fruits: List[int]) -> int:
    """Sliding window: expand right, shrink left when > 2 distinct types."""
    freq = defaultdict(int)
    left = 0
    ans = 0

    for right in range(len(fruits)):
        freq[fruits[right]] += 1

        # Shrink until at most 2 distinct types
        while len(freq) > 2:
            freq[fruits[left]] -= 1
            if freq[fruits[left]] == 0:
                del freq[fruits[left]]
            left += 1

        ans = max(ans, right - left + 1)

    return ans


# ============================================================
# APPROACH 3: BEST (Sliding Window -- Non-Shrinking)
# Time: O(n) | Space: O(1)
# ============================================================
def best(fruits: List[int]) -> int:
    """Non-shrinking window: slide by 1 when invalid, never shrink below previous best."""
    n = len(fruits)
    freq = defaultdict(int)
    left = 0

    for right in range(n):
        freq[fruits[right]] += 1

        # If invalid, slide window by 1
        if len(freq) > 2:
            freq[fruits[left]] -= 1
            if freq[fruits[left]] == 0:
                del freq[fruits[left]]
            left += 1

    return n - left


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Fruits into Baskets ===\n")

    test_cases = [
        ([1, 2, 1], 3),
        ([0, 1, 2, 2], 3),
        ([1, 2, 3, 2, 2], 4),
        ([3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4], 5),
        ([1], 1),
        ([1, 1, 1, 1], 4),
        ([1, 2, 3, 4, 5], 2),
    ]

    for fruits, expected in test_cases:
        b = brute_force(fruits)
        o = optimal(fruits)
        r = best(fruits)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: {fruits}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
