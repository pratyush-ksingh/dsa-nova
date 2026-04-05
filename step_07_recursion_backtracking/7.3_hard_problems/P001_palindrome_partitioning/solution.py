"""
Problem: Palindrome Partitioning
Difficulty: HARD | XP: 50

Given a string s, partition it such that every substring is a palindrome.
Return all possible palindrome partitioning of s.

Example: "aab" -> [["a","a","b"], ["aa","b"]]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Backtracking, check palindrome inline
# Time: O(n * 2^n)  |  Space: O(n) recursion depth
# At each index try every prefix; if it is a palindrome, recurse on the rest
# ============================================================
def brute_force(s: str) -> List[List[str]]:
    res = []

    def is_palin(t: str) -> bool:
        return t == t[::-1]

    def backtrack(start: int, path: List[str]) -> None:
        if start == len(s):
            res.append(list(path))
            return
        for end in range(start + 1, len(s) + 1):
            sub = s[start:end]
            if is_palin(sub):
                path.append(sub)
                backtrack(end, path)
                path.pop()

    backtrack(0, [])
    return res


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking + DP palindrome table
# Time: O(n * 2^n)  |  Space: O(n^2) for DP table
# Precompute dp[i][j]=True if s[i..j] palindrome; O(1) check during backtrack
# ============================================================
def optimal(s: str) -> List[List[str]]:
    n = len(s)
    dp = [[False] * n for _ in range(n)]
    for i in range(n - 1, -1, -1):
        for j in range(i, n):
            dp[i][j] = s[i] == s[j] and (j - i <= 2 or dp[i + 1][j - 1])

    res = []

    def backtrack(start: int, path: List[str]) -> None:
        if start == n:
            res.append(list(path))
            return
        for end in range(start, n):
            if dp[start][end]:
                path.append(s[start:end + 1])
                backtrack(end + 1, path)
                path.pop()

    backtrack(0, [])
    return res


# ============================================================
# APPROACH 3: BEST - DP bottom-up: build partition lists iteratively
# Time: O(n * 2^n)  |  Space: O(n^2)
# dp[i] = all palindrome partitions of s[:i]; build forward using palindrome table
# ============================================================
def best(s: str) -> List[List[str]]:
    n = len(s)
    palin = [[False] * n for _ in range(n)]
    for i in range(n - 1, -1, -1):
        for j in range(i, n):
            palin[i][j] = s[i] == s[j] and (j - i <= 2 or palin[i + 1][j - 1])

    # dp[i] = list of all palindrome partitions of s[:i]
    dp: List[List[List[str]]] = [[] for _ in range(n + 1)]
    dp[0] = [[]]
    for end in range(1, n + 1):
        for start in range(end):
            if palin[start][end - 1]:
                for prev in dp[start]:
                    dp[end].append(prev + [s[start:end]])
    return dp[n]


if __name__ == "__main__":
    print("=== Palindrome Partitioning ===")

    s = "aab"
    print(f's="{s}"')
    print(f"Brute:   {brute_force(s)}")
    print(f"Optimal: {optimal(s)}")
    print(f"Best:    {best(s)}")

    s = "a"
    print(f'\ns="{s}"')
    print(f"Brute:   {brute_force(s)}")
    print(f"Optimal: {optimal(s)}")
    print(f"Best:    {best(s)}")

    s = "racecar"
    print(f'\ns="{s}"')
    print(f"Brute:   {brute_force(s)}")
    print(f"Optimal: {optimal(s)}")
    print(f"Best:    {best(s)}")
