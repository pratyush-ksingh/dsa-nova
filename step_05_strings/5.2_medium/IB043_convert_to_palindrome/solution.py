"""
Problem: Convert to Palindrome (Valid Palindrome II - LeetCode 680 / InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a non-empty string s, check if it can become a palindrome
after deleting at most one character.
Return 1 (True) if possible, 0 (False) otherwise.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Try removing each character
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def brute_force(s: str) -> int:
    """
    For every index i, try the string with character i removed and check
    if the result is a palindrome. Also check the original string.
    Return 1 if any attempt succeeds, 0 otherwise.
    """
    def is_palindrome(t: str) -> bool:
        return t == t[::-1]

    if is_palindrome(s):
        return 1

    n = len(s)
    for i in range(n):
        candidate = s[:i] + s[i+1:]
        if is_palindrome(candidate):
            return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL - Two pointers, skip one char on mismatch
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(s: str) -> int:
    """
    Use two pointers (left, right) from both ends.
    When all characters match, it's a palindrome.
    When a mismatch is found, we have exactly one chance to skip:
      - Skip left character:  check if s[left+1..right] is a palindrome
      - Skip right character: check if s[left..right-1] is a palindrome
    If either is a palindrome, return 1. Otherwise return 0.
    """
    def is_palindrome_range(t: str, lo: int, hi: int) -> bool:
        while lo < hi:
            if t[lo] != t[hi]:
                return False
            lo += 1
            hi -= 1
        return True

    left, right = 0, len(s) - 1
    while left < right:
        if s[left] != s[right]:
            # Try skipping left or right character
            return 1 if (is_palindrome_range(s, left + 1, right) or
                         is_palindrome_range(s, left, right - 1)) else 0
        left += 1
        right -= 1
    return 1  # fully matched, already a palindrome


# ============================================================
# APPROACH 3: BEST - Same two pointer (explicit early return + cleaner)
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(s: str) -> int:
    """
    Same two-pointer approach as Approach 2, written more concisely.
    The helper validates a substring [lo, hi] as a palindrome in O(n).
    The key insight: we only ever get ONE mismatch budget.
    """
    def check(lo: int, hi: int) -> bool:
        while lo < hi:
            if s[lo] != s[hi]:
                return False
            lo += 1
            hi -= 1
        return True

    lo, hi = 0, len(s) - 1
    while lo < hi:
        if s[lo] != s[hi]:
            return 1 if check(lo + 1, hi) or check(lo, hi - 1) else 0
        lo += 1
        hi -= 1
    return 1


if __name__ == "__main__":
    print("=== Convert to Palindrome ===")
    test_cases = [
        ("aba",      1),  # already palindrome
        ("abca",     1),  # remove 'c' -> "aba"
        ("abc",      0),  # no single removal makes palindrome
        ("a",        1),
        ("raceacar", 1),  # remove 'e' (index 3) -> "racacar" is a palindrome
        ("deeee",    1),  # remove first 'd' -> "eeee"
        ("aguokepatgbnvfqmgmlcupuufxoohdfpgjdmysgvhmvffcnqxjjxqncffvmhvgsymdjgpfdhooxfuupuculmgmqfvnbgtapekouga", 1),
    ]
    for s, expected in test_cases:
        b   = brute_force(s)
        o   = optimal(s)
        bst = best(s)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"[{status}] s={s[:30]!r}{'...' if len(s)>30 else ''} | "
              f"Brute={b}, Optimal={o}, Best={bst} (expected {expected})")
