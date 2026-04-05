"""
Problem: Edit Distance (LeetCode #72)
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursion
# Time: O(3^(m+n))  |  Space: O(m+n) recursion stack
# At each mismatch, try insert, delete, replace.
# ============================================================
def brute_force(word1: str, word2: str) -> int:
    def solve(i: int, j: int) -> int:
        # If word1 is exhausted, insert remaining of word2
        if i < 0:
            return j + 1
        # If word2 is exhausted, delete remaining of word1
        if j < 0:
            return i + 1
        # Characters match
        if word1[i] == word2[j]:
            return solve(i - 1, j - 1)
        # Try all three operations
        insert = 1 + solve(i, j - 1)      # insert word2[j] into word1
        delete = 1 + solve(i - 1, j)      # delete word1[i]
        replace = 1 + solve(i - 1, j - 1) # replace word1[i] with word2[j]
        return min(insert, delete, replace)

    return solve(len(word1) - 1, len(word2) - 1)


# ============================================================
# APPROACH 2: OPTIMAL -- 2D DP Tabulation
# Time: O(m * n)  |  Space: O(m * n)
# dp[i][j] = edit distance for word1[0..i-1] and word2[0..j-1].
# ============================================================
def optimal(word1: str, word2: str) -> int:
    m, n = len(word1), len(word2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    # Base cases
    for i in range(m + 1):
        dp[i][0] = i  # delete all of word1
    for j in range(n + 1):
        dp[0][j] = j  # insert all of word2

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i - 1] == word2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1]
            else:
                dp[i][j] = 1 + min(
                    dp[i][j - 1],      # insert
                    dp[i - 1][j],      # delete
                    dp[i - 1][j - 1]   # replace
                )

    return dp[m][n]


# ============================================================
# APPROACH 3: BEST -- Space-Optimized 1D DP
# Time: O(m * n)  |  Space: O(min(m, n))
# Use two 1D arrays. Ensure we iterate over the shorter string
# for the inner loop to minimize space.
# ============================================================
def best(word1: str, word2: str) -> int:
    # Make word2 the shorter one for space optimization
    if len(word1) < len(word2):
        word1, word2 = word2, word1

    m, n = len(word1), len(word2)
    prev = list(range(n + 1))  # base case: dp[0][j] = j

    for i in range(1, m + 1):
        curr = [0] * (n + 1)
        curr[0] = i  # base case: dp[i][0] = i
        for j in range(1, n + 1):
            if word1[i - 1] == word2[j - 1]:
                curr[j] = prev[j - 1]
            else:
                curr[j] = 1 + min(
                    curr[j - 1],    # insert
                    prev[j],        # delete
                    prev[j - 1]     # replace
                )
        prev = curr

    return prev[n]


if __name__ == "__main__":
    print("=== Edit Distance ===\n")

    print(f"Brute:   {brute_force('horse', 'ros')}")     # 3
    print(f"Optimal: {optimal('horse', 'ros')}")           # 3
    print(f"Best:    {best('horse', 'ros')}")              # 3

    print(f"\nBrute:   {brute_force('intention', 'execution')}")  # 5
    print(f"Optimal: {optimal('intention', 'execution')}")         # 5
    print(f"Best:    {best('intention', 'execution')}")            # 5

    print(f"\nEmpty:   {best('', 'abc')}")                 # 3
    print(f"Same:    {best('abc', 'abc')}")                # 0
