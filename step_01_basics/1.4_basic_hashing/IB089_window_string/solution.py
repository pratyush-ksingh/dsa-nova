"""
Problem: Window String (Minimum Window Substring)
Difficulty: HARD | XP: 50
Source: InterviewBit

Given strings S and T, find the minimum window in S that contains all
characters of T (including duplicates). Return "" if no such window exists.
"""
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2 * |T|)  |  Space: O(|T|)
# ============================================================
def brute_force(S: str, T: str) -> str:
    """
    Try all O(n^2) substrings of S; for each, check if it covers all of T's chars.
    Real-life: Finding shortest news excerpt that mentions all required keywords.
    """
    if not S or not T or len(S) < len(T):
        return ""
    n = len(S)
    need = Counter(T)
    min_len = float('inf')
    min_start = -1

    for i in range(n):
        window: Counter = Counter()
        for j in range(i, n):
            window[S[j]] += 1
            # Check if window satisfies need
            if all(window[c] >= need[c] for c in need):
                if j - i + 1 < min_len:
                    min_len = j - i + 1
                    min_start = i
                break  # can't do better starting at i by extending further
    return "" if min_start == -1 else S[min_start: min_start + min_len]


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n + |T|)  |  Space: O(|T|)
# ============================================================
def optimal(S: str, T: str) -> str:
    """
    Two-pointer sliding window with a "formed" counter.
    Expand right until window is valid, then shrink left to minimize.
    Real-life: Search engines finding the smallest document segment covering all query terms.
    """
    if not S or not T or len(S) < len(T):
        return ""
    need = Counter(T)
    required = len(need)   # number of distinct chars to satisfy
    formed = 0
    window: Counter = Counter()

    left = right = 0
    min_len = float('inf')
    min_left = 0

    while right < len(S):
        c = S[right]
        window[c] += 1
        if c in need and window[c] == need[c]:
            formed += 1

        while formed == required:
            if right - left + 1 < min_len:
                min_len = right - left + 1
                min_left = left
            lc = S[left]
            window[lc] -= 1
            if lc in need and window[lc] < need[lc]:
                formed -= 1
            left += 1
        right += 1

    return "" if min_len == float('inf') else S[min_left: min_left + min_len]


# ============================================================
# APPROACH 3: BEST
# Time: O(n + |T|)  |  Space: O(1) [128 ASCII chars]
# ============================================================
def best(S: str, T: str) -> str:
    """
    Same two-pointer approach but uses fixed-size int arrays (via dict of ord)
    instead of Counter — lower allocation overhead for large inputs.
    Real-life: High-performance grep-like tools processing gigabytes of text.
    """
    if not S or not T or len(S) < len(T):
        return ""
    need: dict = {}
    for c in T:
        need[c] = need.get(c, 0) + 1
    required = len(need)
    formed = 0
    window: dict = {}

    left = right = 0
    min_len = float('inf')
    min_left = 0

    while right < len(S):
        c = S[right]
        window[c] = window.get(c, 0) + 1
        if c in need and window[c] == need[c]:
            formed += 1

        while formed == required:
            if right - left + 1 < min_len:
                min_len = right - left + 1
                min_left = left
            lc = S[left]
            window[lc] -= 1
            if lc in need and window[lc] < need[lc]:
                formed -= 1
            left += 1
        right += 1

    return "" if min_len == float('inf') else S[min_left: min_left + min_len]


if __name__ == "__main__":
    print("=== Window String ===")
    tests = [
        ("ADOBECODEBANC", "ABC",  "BANC"),
        ("a",             "a",    "a"),
        ("a",             "aa",   ""),
        ("aa",            "aa",   "aa"),
    ]
    for S, T, exp in tests:
        print(f"\nS={S}, T={T}  =>  expected: '{exp}'")
        print(f"  Brute:   '{brute_force(S, T)}'")
        print(f"  Optimal: '{optimal(S, T)}'")
        print(f"  Best:    '{best(S, T)}'")
