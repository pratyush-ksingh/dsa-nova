"""Problem: Word Break
Difficulty: MEDIUM | XP: 25"""

from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^n)  |  Space: O(n)
# Pure recursion; try all word splits from current position.
# No memoization — exponential worst case.
# ============================================================
def brute_force(s: str, wordDict: List[str]) -> bool:
    word_set = set(wordDict)

    def check(start):
        if start == len(s):
            return True
        for end in range(start + 1, len(s) + 1):
            if s[start:end] in word_set and check(end):
                return True
        return False

    return check(0)


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
# Time: O(n^3)  |  Space: O(n)
# Recursion + cache: store True/False for each start index.
# ============================================================
def optimal(s: str, wordDict: List[str]) -> bool:
    word_set = set(wordDict)
    from functools import lru_cache

    @lru_cache(maxsize=None)
    def dp(start):
        if start == len(s):
            return True
        for end in range(start + 1, len(s) + 1):
            if s[start:end] in word_set and dp(end):
                return True
        return False

    return dp(0)


# ============================================================
# APPROACH 3: BEST (Bottom-Up DP)
# Time: O(n^2)  |  Space: O(n)
# dp[i] = True iff s[:i] can be segmented.
# ============================================================
def best(s: str, wordDict: List[str]) -> bool:
    word_set = set(wordDict)
    n = len(s)
    dp = [False] * (n + 1)
    dp[0] = True  # empty prefix is always valid

    for i in range(1, n + 1):
        for j in range(i):
            if dp[j] and s[j:i] in word_set:
                dp[i] = True
                break

    return dp[n]


if __name__ == "__main__":
    tests = [
        ("leetcode",      ["leet", "code"],                True),
        ("applepenapple", ["apple", "pen"],                True),
        ("catsandog",     ["cats", "dog", "sand", "and", "cat"], False),
        ("a",             ["a"],                           True),
        ("abcd",          ["a", "abc", "b", "cd"],         True),
    ]
    print("=== Word Break ===")
    for s, wordDict, expected in tests:
        b  = brute_force(s, wordDict)
        o  = optimal(s, wordDict)
        be = best(s, wordDict)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"s={s!r:18} | Brute: {str(b):5} | Optimal: {str(o):5} | Best: {str(be):5} | Expected: {str(expected):5} | {status}")
