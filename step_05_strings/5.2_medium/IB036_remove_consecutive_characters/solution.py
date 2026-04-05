"""
Problem: Remove Consecutive Characters
Difficulty: EASY | XP: 10
Source: InterviewBit

Remove all consecutive duplicate characters from a string.
"aabccba" -> "abcba"
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Compare with previous character)
# Time: O(n) | Space: O(n)
# ============================================================
def brute_force(s: str) -> str:
    """Build result by skipping characters that match their predecessor."""
    if len(s) <= 1:
        return s

    result = [s[0]]
    for i in range(1, len(s)):
        if s[i] != s[i - 1]:
            result.append(s[i])
    return "".join(result)


# ============================================================
# APPROACH 2: OPTIMAL (Single Pass, compare with last in result)
# Time: O(n) | Space: O(n)
# ============================================================
def optimal(s: str) -> str:
    """Compare each character with the last character added to result."""
    if len(s) <= 1:
        return s

    result = [s[0]]
    for i in range(1, len(s)):
        if s[i] != result[-1]:
            result.append(s[i])
    return "".join(result)


# ============================================================
# APPROACH 3: BEST (itertools.groupby -- Pythonic)
# Time: O(n) | Space: O(n)
# ============================================================
def best(s: str) -> str:
    """Use itertools.groupby to take the first character of each group."""
    from itertools import groupby
    return "".join(key for key, _ in groupby(s))


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Remove Consecutive Characters ===\n")

    test_cases = [
        ("aabccba", "abcba"),
        ("aab", "ab"),
        ("abcd", "abcd"),
        ("aaaa", "a"),
        ("aabbcc", "abc"),
        ("a", "a"),
        ("ababab", "ababab"),
        ("aabc", "abc"),
        ("abcc", "abc"),
    ]

    for s, expected in test_cases:
        b = brute_force(s)
        o = optimal(s)
        be = best(s)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    \"{s}\"")
        print(f"  Brute:    \"{b}\"")
        print(f"  Optimal:  \"{o}\"")
        print(f"  Best:     \"{be}\"")
        print(f"  Expected: \"{expected}\"  [{status}]\n")
