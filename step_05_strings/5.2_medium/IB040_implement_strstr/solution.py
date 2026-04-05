"""
Problem: Implement StrStr (LeetCode 28 / InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Return the index of the first occurrence of needle in haystack,
or -1 if needle is not part of haystack.
If needle is empty, return 0.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Sliding window character comparison
# Time: O(n * m)  |  Space: O(1)
# n = len(haystack), m = len(needle)
# ============================================================
def brute_force(haystack: str, needle: str) -> int:
    """
    Try every starting position in haystack and compare the next m characters
    with needle. Return the first matching index.
    """
    n, m = len(haystack), len(needle)
    if m == 0:
        return 0
    if m > n:
        return -1

    for i in range(n - m + 1):
        # Check if haystack[i:i+m] == needle
        match = True
        for j in range(m):
            if haystack[i + j] != needle[j]:
                match = False
                break
        if match:
            return i
    return -1


# ============================================================
# APPROACH 2: OPTIMAL - KMP (Knuth-Morris-Pratt) algorithm
# Time: O(n + m)  |  Space: O(m)
# ============================================================
def optimal(haystack: str, needle: str) -> int:
    """
    KMP preprocesses needle to build a failure function (lps array).
    lps[i] = length of longest proper prefix of needle[0..i] that is also a suffix.
    This allows us to skip redundant comparisons when a mismatch occurs.
    """
    n, m = len(haystack), len(needle)
    if m == 0:
        return 0
    if m > n:
        return -1

    # Build LPS (Longest Prefix Suffix) array
    lps = [0] * m
    length = 0  # length of previous longest prefix suffix
    i = 1
    while i < m:
        if needle[i] == needle[length]:
            length += 1
            lps[i] = length
            i += 1
        else:
            if length != 0:
                length = lps[length - 1]  # fall back, don't increment i
            else:
                lps[i] = 0
                i += 1

    # KMP search
    i = 0  # index for haystack
    j = 0  # index for needle
    while i < n:
        if haystack[i] == needle[j]:
            i += 1
            j += 1
        if j == m:
            return i - j  # found at index i - j
        elif i < n and haystack[i] != needle[j]:
            if j != 0:
                j = lps[j - 1]  # skip using lps
            else:
                i += 1
    return -1


# ============================================================
# APPROACH 3: BEST - Rabin-Karp rolling hash
# Time: O(n + m) average, O(n*m) worst  |  Space: O(1)
# ============================================================
def best(haystack: str, needle: str) -> int:
    """
    Rabin-Karp uses a rolling hash to compare windows in O(1) per slide.
    - Compute hash of needle and first window of haystack.
    - Slide window: subtract leftmost char's hash, add new rightmost char's hash.
    - On hash match, verify with character comparison (to handle collisions).

    Uses base=31 and a large prime modulus to minimize collisions.
    """
    n, m = len(haystack), len(needle)
    if m == 0:
        return 0
    if m > n:
        return -1

    BASE = 31
    MOD  = 10**9 + 7

    # Precompute BASE^(m-1) % MOD
    power = pow(BASE, m - 1, MOD)

    def char_val(c: str) -> int:
        return ord(c) - ord('a') + 1

    # Compute hash of needle and first window
    needle_hash = 0
    window_hash = 0
    for i in range(m):
        needle_hash = (needle_hash * BASE + char_val(needle[i])) % MOD
        window_hash = (window_hash * BASE + char_val(haystack[i])) % MOD

    if window_hash == needle_hash and haystack[:m] == needle:
        return 0

    for i in range(1, n - m + 1):
        # Roll: remove leftmost char, add new rightmost char
        window_hash = (window_hash - char_val(haystack[i - 1]) * power) % MOD
        window_hash = (window_hash * BASE + char_val(haystack[i + m - 1])) % MOD
        window_hash %= MOD

        if window_hash == needle_hash:
            if haystack[i:i + m] == needle:  # confirm to handle hash collision
                return i
    return -1


if __name__ == "__main__":
    print("=== Implement StrStr ===")
    test_cases = [
        ("sadbutsad", "sad", 0),
        ("leetcode", "leeto", -1),
        ("hello", "ll", 2),
        ("aaa", "aaaa", -1),
        ("a", "a", 0),
        ("", "", 0),
        ("mississippi", "issip", 4),
    ]
    for haystack, needle, expected in test_cases:
        b   = brute_force(haystack, needle)
        o   = optimal(haystack, needle)
        bst = best(haystack, needle)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"[{status}] haystack={haystack!r}, needle={needle!r} | "
              f"Brute={b}, KMP={o}, RK={bst} (expected {expected})")
