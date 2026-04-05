"""
Problem: Longest Common Prefix (LeetCode #14)
Difficulty: EASY | XP: 10

Find the longest common prefix string amongst an array of strings.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Horizontal Scanning)
# Time: O(S) where S = sum of all chars | Space: O(m)
# ============================================================
def brute_force(strs: List[str]) -> str:
    """Start with first string as prefix, trim against each subsequent string."""
    if not strs:
        return ""
    prefix = strs[0]
    for s in strs[1:]:
        while not s.startswith(prefix):
            prefix = prefix[:-1]
            if not prefix:
                return ""
    return prefix


# ============================================================
# APPROACH 2: OPTIMAL (Vertical Scanning)
# Time: O(n * m) | Space: O(1) extra
# ============================================================
def optimal(strs: List[str]) -> str:
    """Compare character by character at each column across all strings."""
    if not strs:
        return ""
    min_len = min(len(s) for s in strs)
    for col in range(min_len):
        c = strs[0][col]
        for s in strs[1:]:
            if s[col] != c:
                return strs[0][:col]
    return strs[0][:min_len]


# ============================================================
# APPROACH 3: SORT-BASED
# Time: O(n * m * log n) | Space: O(m)
# ============================================================
def sort_based(strs: List[str]) -> str:
    """Sort lexicographically; LCP of all = LCP of first and last after sorting."""
    if not strs:
        return ""
    strs_sorted = sorted(strs)
    first, last = strs_sorted[0], strs_sorted[-1]
    i = 0
    while i < len(first) and i < len(last) and first[i] == last[i]:
        i += 1
    return first[:i]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Longest Common Prefix ===\n")

    test_cases = [
        (["flower", "flow", "flight"], "fl"),
        (["dog", "racecar", "car"], ""),
        (["interspecies", "interstellar", "interstate"], "inters"),
        (["a"], "a"),
        (["", "abc"], ""),
        (["abc", "abc", "abc"], "abc"),
        (["apple", "ape", "art"], "a"),
    ]

    for strs, expected in test_cases:
        b = brute_force(strs)
        o = optimal(strs)
        s = sort_based(strs)
        status = "PASS" if b == expected and o == expected and s == expected else "FAIL"
        print(f"Input: {strs}")
        print(f"  Brute:     \"{b}\"")
        print(f"  Optimal:   \"{o}\"")
        print(f"  SortBased: \"{s}\"")
        print(f"  Expected:  \"{expected}\"  [{status}]\n")
