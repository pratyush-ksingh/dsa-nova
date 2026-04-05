"""
Problem: Amazing Subarrays
Difficulty: EASY | XP: 10
Source: InterviewBit

Key Insight: A vowel at index i starts exactly (n - i) substrings.
"""
from typing import List, Optional

MOD = 10007
VOWELS = set("aeiouAEIOU")


# ============================================================
# APPROACH 1: BRUTE FORCE -- Enumerate All Substrings
# Time: O(n^2)  |  Space: O(1)
# ============================================================
def brute_force(s: str) -> int:
    """Check every substring; count those starting with a vowel."""
    n = len(s)
    count = 0
    for i in range(n):
        if s[i] in VOWELS:
            for j in range(i, n):
                count = (count + 1) % MOD
    return count


# ============================================================
# APPROACH 2: OPTIMAL -- Single Pass Math
# Time: O(n)  |  Space: O(1)
#
# Vowel at index i contributes (n - i) amazing substrings.
# ============================================================
def optimal(s: str) -> int:
    n = len(s)
    count = 0
    for i in range(n):
        if s[i] in VOWELS:
            count = (count + (n - i)) % MOD
    return count


# ============================================================
# APPROACH 3: BEST -- Same logic, tighter constant factor
# Time: O(n)  |  Space: O(1)
#
# Uses a pre-built boolean array for O(1) vowel lookup.
# ============================================================
def best(s: str) -> int:
    vowel_map = [False] * 128
    for c in "aeiouAEIOU":
        vowel_map[ord(c)] = True

    n = len(s)
    count = 0
    for i in range(n):
        if vowel_map[ord(s[i])]:
            count = (count + (n - i)) % MOD
    return count


if __name__ == "__main__":
    print("=== Amazing Subarrays ===")
    tests = ["ABEC", "AAA", "BCD", ""]
    for t in tests:
        print(f'Input: "{t}"')
        print(f"  Brute:   {brute_force(t)}")
        print(f"  Optimal: {optimal(t)}")
        print(f"  Best:    {best(t)}")
        print()
