"""
Problem: Length of Longest Subsequence
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an array of integers, find the length of the longest subsequence such
that elements in the subsequence are consecutive integers (differ by exactly 1
from the previous element). The subsequence need not be contiguous.

Note: Unlike LCS, elements must differ by EXACTLY 1 each step.
Example: [3,10,3,4,2,1,5] => longest is [3,4,5] or [1,2,3,4,5] => length 5
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^n * n)  |  Space: O(n)
# Generate all subsequences, check validity, track max length.
# ============================================================
def brute_force(arr: List[int]) -> int:
    """
    Enumerate all 2^n subsequences. For each, check if consecutive elements
    differ by exactly 1 (i.e. each next element == previous + 1).
    Return the length of the longest valid subsequence.
    """
    n = len(arr)
    if n == 0:
        return 0

    max_len = 1

    def generate(index: int, current: List[int]) -> None:
        nonlocal max_len
        if current:
            # Check if current subsequence has all diffs == 1
            valid = all(current[i] - current[i - 1] == 1
                        for i in range(1, len(current)))
            if valid:
                max_len = max(max_len, len(current))

        for i in range(index, n):
            generate(i + 1, current + [arr[i]])

    generate(0, [])
    return max_len


# ============================================================
# APPROACH 2: OPTIMAL — HashMap DP
# Time: O(n)  |  Space: O(n)
# dp[v] = length of longest valid subsequence ending at value v.
# For each element x: dp[x] = dp[x-1] + 1
# ============================================================
def optimal(arr: List[int]) -> int:
    """
    Process each element. The longest subsequence ending at value x can extend
    any subsequence that ended at x-1, so dp[x] = dp.get(x-1, 0) + 1.
    Answer is the max over all dp values.
    """
    if not arr:
        return 0

    dp = {}  # value -> longest subsequence length ending at that value
    max_len = 1

    for x in arr:
        dp[x] = dp.get(x - 1, 0) + 1
        max_len = max(max_len, dp[x])

    return max_len


# ============================================================
# APPROACH 3: BEST — Same HashMap DP (already O(n)/O(n) optimal)
# Time: O(n)  |  Space: O(n)
# Slight code clarity improvement: single-pass with explicit handling.
# For this problem, Approach 2 is already optimal; this is a cleaner variant.
# ============================================================
def best(arr: List[int]) -> int:
    """
    Same O(n) DP but explicitly shows that we only need the value one less
    than current to extend a chain. HashMap lookup is O(1) on average.
    """
    if not arr:
        return 0

    # Map: last element value -> best chain length ending there
    chain = {}
    ans = 1

    for val in arr:
        prev_len = chain.get(val - 1, 0)
        new_len = prev_len + 1
        # Only update if this creates a longer chain at val
        chain[val] = max(chain.get(val, 0), new_len)
        ans = max(ans, chain[val])

    return ans


if __name__ == "__main__":
    print("=== Length of Longest Subsequence ===")

    test_cases = [
        ([3, 10, 3, 4, 2, 1, 5], 3),       # [3,4,5] at indices 2,3,6
        ([1, 2, 3, 4, 5], 5),               # whole array
        ([5, 4, 3], 1),                     # all decreasing, no two differ by +1 in order
        ([1, 9, 3, 0, 18, 2], 2),           # best is [1,2] at indices 0,5 or [9,10] — no 10; [0,1] at idx 3,0 invalid; [1,2] at 0,5 = 2
        ([1], 1),
        ([], 0),
    ]

    for arr, expected in test_cases:
        b = brute_force(arr)
        o = optimal(arr)
        bt = best(arr)
        status = "OK" if b == o == bt == expected else "FAIL"
        print(f"  arr={arr}")
        print(f"    Brute={b}, Optimal={o}, Best={bt}, Expected={expected} [{status}]")
