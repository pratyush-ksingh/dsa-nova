"""
Problem: Minimum Characters to Add to Make String a Palindrome
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a string A, find the minimum number of characters to prepend to A
to make it a palindrome.

Key insight: answer = len(A) - (length of longest palindromic prefix of A).
The longest palindromic prefix can be found efficiently using KMP on:
    combined = A + "#" + reverse(A)
The LPS value at the last position of 'combined' gives the length of the
longest palindromic prefix of A.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Check each prefix length
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def brute_force(s: str) -> int:
    """
    For decreasing prefix lengths (from full string down to length 1),
    check if s[0:k] is a palindrome. The minimum chars to prepend is n - k
    where k is the longest palindromic prefix.
    """
    n = len(s)

    def is_palindrome(t: str) -> bool:
        return t == t[::-1]

    for k in range(n, 0, -1):
        if is_palindrome(s[:k]):
            return n - k
    return n  # worst case: prepend n chars (e.g., single char repeated n times)


# ============================================================
# APPROACH 2: OPTIMAL - KMP failure function on s + "#" + reverse(s)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(s: str) -> int:
    """
    Construct combined = s + "#" + reverse(s).
    Build the KMP LPS (failure function) array for 'combined'.
    lps[-1] gives the length of the longest prefix of s that is also
    a suffix of reverse(s) — which is exactly the longest palindromic prefix of s.
    Answer = n - lps[-1].

    The '#' separator prevents the LPS from crossing the boundary between s and rev.
    """
    n = len(s)
    if n == 0:
        return 0

    rev = s[::-1]
    combined = s + "#" + rev
    m = len(combined)

    lps = [0] * m
    length = 0
    i = 1
    while i < m:
        if combined[i] == combined[length]:
            length += 1
            lps[i] = length
            i += 1
        else:
            if length != 0:
                length = lps[length - 1]
            else:
                lps[i] = 0
                i += 1

    # lps[-1] = length of longest palindromic prefix of s
    return n - lps[-1]


# ============================================================
# APPROACH 3: BEST - Same as Optimal (KMP is already optimal for this problem)
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(s: str) -> int:
    """
    Same KMP approach. Presented with slightly cleaner variable naming
    and an explicit explanation of why the '#' separator is necessary:
    without it, the LPS could match across the boundary and give a wrong answer
    (e.g., s="aab", rev="baa" -> combined without # = "aabbaa" which would
    incorrectly report lps=4 instead of lps=1 from s's perspective).
    """
    n = len(s)
    if n == 0:
        return 0

    combined = s + "#" + s[::-1]
    lps = [0] * len(combined)
    j = 0

    for i in range(1, len(combined)):
        while j > 0 and combined[i] != combined[j]:
            j = lps[j - 1]
        if combined[i] == combined[j]:
            j += 1
        lps[i] = j

    return n - lps[-1]


if __name__ == "__main__":
    print("=== Minimum Characters for Palindrome ===")
    test_cases = [
        ("ABC",    2),   # prepend "CB" -> "CBABC"
        ("AACECAAAA", 2),  # prepend "AA" -> "AAAACECAAAA"  wait let's verify
        ("abcd",   3),   # prepend "dcb" -> "dcbabcd"
        ("a",      0),
        ("aa",     0),
        ("ab",     1),   # prepend "b" -> "bab"
        ("aba",    0),
        ("aab",    1),   # prepend "b" -> "baab"
    ]
    for s, expected in test_cases:
        b   = brute_force(s)
        o   = optimal(s)
        bst = best(s)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"[{status}] s={s!r} | Brute={b}, KMP={o}, Best={bst} (expected {expected})")
