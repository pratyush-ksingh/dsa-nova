"""
Problem: Check Anagram (LeetCode #242)
Difficulty: EASY | XP: 10

Given two strings s and t, return True if t is an anagram of s.
"""
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE (Sort Both Strings)
# Time: O(n log n) | Space: O(n)
# ============================================================
def brute_force(s: str, t: str) -> bool:
    """Sort both strings and compare."""
    if len(s) != len(t):
        return False
    return sorted(s) == sorted(t)


# ============================================================
# APPROACH 2: OPTIMAL (Two Frequency Counters)
# Time: O(n) | Space: O(1) -- at most 26 keys
# ============================================================
def optimal(s: str, t: str) -> bool:
    """Compare character frequency distributions."""
    if len(s) != len(t):
        return False
    return Counter(s) == Counter(t)


# ============================================================
# APPROACH 3: BEST (Single Frequency Array -- increment/decrement)
# Time: O(n) | Space: O(1) -- fixed 26-element array
# ============================================================
def best(s: str, t: str) -> bool:
    """Single array: increment for s, decrement for t, check all zeros."""
    if len(s) != len(t):
        return False

    freq = [0] * 26

    for i in range(len(s)):
        freq[ord(s[i]) - ord('a')] += 1
        freq[ord(t[i]) - ord('a')] -= 1

    return all(c == 0 for c in freq)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Check Anagram ===\n")

    test_cases = [
        ("anagram", "nagaram", True),
        ("rat", "car", False),
        ("a", "a", True),
        ("ab", "ba", True),
        ("abc", "abcd", False),
        ("aab", "abb", False),
        ("aaa", "aaa", True),
    ]

    for s, t, expected in test_cases:
        b = brute_force(s, t)
        o = optimal(s, t)
        be = best(s, t)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    s=\"{s}\", t=\"{t}\"")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
