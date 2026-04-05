"""
Problem: Minimum Window Substring
Difficulty: HARD | XP: 50

Find the smallest window in string S that contains all characters of T
(including duplicates). Return "" if impossible.
Real-life use: Search highlighting, DNA sequence analysis, substring matching.
"""
from typing import List
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE
# Check every substring of S; verify if it contains all of T.
# Time: O(N^2 * |T|)  |  Space: O(|T|)
# ============================================================
def brute_force(s: str, t: str) -> str:
    if not s or not t:
        return ""
    t_count = Counter(t)
    result = ""
    for i in range(len(s)):
        for j in range(i + len(t), len(s) + 1):
            sub = s[i:j]
            if all(Counter(sub)[c] >= t_count[c] for c in t_count):
                if not result or len(sub) < len(result):
                    result = sub
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Sliding window with frequency counters and "formed" counter.
# Expand right until valid, shrink left to minimize.
# Time: O(|S| + |T|)  |  Space: O(|S| + |T|)
# ============================================================
def optimal(s: str, t: str) -> str:
    if not s or not t:
        return ""
    need = Counter(t)
    have: dict = {}
    required = len(need)  # number of unique chars in T needed
    formed = 0
    left = 0
    min_len = float('inf')
    min_start = 0

    for right, rc in enumerate(s):
        have[rc] = have.get(rc, 0) + 1
        if rc in need and have[rc] == need[rc]:
            formed += 1
        while formed == required:
            if right - left + 1 < min_len:
                min_len = right - left + 1
                min_start = left
            lc = s[left]
            have[lc] -= 1
            if lc in need and have[lc] < need[lc]:
                formed -= 1
            left += 1

    return "" if min_len == float('inf') else s[min_start:min_start + min_len]


# ============================================================
# APPROACH 3: BEST
# Filtered sliding window: only look at positions in S where char is in T.
# Best when |T| << |S| — massively reduces iterations.
# Time: O(|S| + |T|)  |  Space: O(|T|)
# ============================================================
def best(s: str, t: str) -> str:
    if not s or not t:
        return ""
    t_chars = set(t)
    # Keep only positions in S with chars in T
    filtered = [(c, i) for i, c in enumerate(s) if c in t_chars]

    need = Counter(t)
    have: dict = {}
    required = len(need)
    formed = 0
    left = 0
    min_len = float('inf')
    min_start = 0

    for right, (rc, r_idx) in enumerate(filtered):
        have[rc] = have.get(rc, 0) + 1
        if have[rc] == need[rc]:
            formed += 1
        while formed == required:
            l_idx = filtered[left][1]
            if r_idx - l_idx + 1 < min_len:
                min_len = r_idx - l_idx + 1
                min_start = l_idx
            lc = filtered[left][0]
            have[lc] -= 1
            if have[lc] < need[lc]:
                formed -= 1
            left += 1

    return "" if min_len == float('inf') else s[min_start:min_start + min_len]


if __name__ == "__main__":
    print("=== Minimum Window Substring ===")

    tests = [
        ("ADOBECODEBANC", "ABC"),  # "BANC"
        ("a", "a"),                # "a"
        ("a", "aa"),               # ""
        ("aa", "aa"),              # "aa"
    ]
    for s, t in tests:
        print(f'\nS="{s}"  T="{t}"')
        print(f'  Brute  : "{brute_force(s, t)}"')
        print(f'  Optimal: "{optimal(s, t)}"')
        print(f'  Best   : "{best(s, t)}"')
