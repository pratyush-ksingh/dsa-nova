"""
Problem: Longest String Chain (LeetCode #1048)
Difficulty: MEDIUM | XP: 25
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS Trying All Permutations
# Time: O(n^2 * L)  |  Space: O(n * L)
# Sort by length, for each pair check if predecessor, DFS.
# ============================================================
def brute_force(words: List[str]) -> int:
    words.sort(key=len)
    n = len(words)

    def is_predecessor(short: str, long: str) -> bool:
        """Check if short is a predecessor of long (differ by one insertion)."""
        if len(long) != len(short) + 1:
            return False
        i = j = 0
        skipped = False
        while i < len(short) and j < len(long):
            if short[i] == long[j]:
                i += 1
                j += 1
            elif not skipped:
                skipped = True
                j += 1
            else:
                return False
        return True

    # dp[i] = longest chain ending at words[i]
    dp = [1] * n
    ans = 1

    for i in range(1, n):
        for j in range(i):
            if is_predecessor(words[j], words[i]):
                dp[i] = max(dp[i], dp[j] + 1)
        ans = max(ans, dp[i])

    return ans


# ============================================================
# APPROACH 2: OPTIMAL -- Sort by Length + DP with HashMap
# Time: O(n * L^2)  |  Space: O(n * L)
# For each word, generate all predecessors by removing one char
# and look up in a HashMap.
# ============================================================
def optimal(words: List[str]) -> int:
    words.sort(key=len)
    dp = {}  # word -> longest chain ending at this word
    ans = 1

    for word in words:
        dp[word] = 1
        # Try removing each character to form a predecessor
        for i in range(len(word)):
            predecessor = word[:i] + word[i + 1:]
            if predecessor in dp:
                dp[word] = max(dp[word], dp[predecessor] + 1)
        ans = max(ans, dp[word])

    return ans


# ============================================================
# APPROACH 3: BEST -- Same HashMap Approach (Optimal for this problem)
# Time: O(n * L^2)  |  Space: O(n * L)
# The HashMap approach IS the best known approach. We can add
# minor optimizations like early termination.
# ============================================================
def best(words: List[str]) -> int:
    words.sort(key=len)
    dp = {}
    ans = 1

    for word in words:
        best_chain = 1
        for i in range(len(word)):
            predecessor = word[:i] + word[i + 1:]
            if predecessor in dp:
                best_chain = max(best_chain, dp[predecessor] + 1)
        dp[word] = best_chain
        ans = max(ans, best_chain)

    return ans


if __name__ == "__main__":
    print("=== Longest String Chain ===\n")

    w1 = ["a", "b", "ba", "bca", "bda", "bdca"]
    print(f"Brute:   {brute_force(w1)}")   # 4 (a -> ba -> bda -> bdca)
    print(f"Optimal: {optimal(w1)}")        # 4
    print(f"Best:    {best(w1)}")           # 4

    w2 = ["xbc", "pcxbcf", "xb", "cxbc", "pcxbc"]
    print(f"\nBrute:   {brute_force(w2)}")  # 5
    print(f"Optimal: {optimal(w2)}")         # 5
    print(f"Best:    {best(w2)}")            # 5

    w3 = ["abcd", "dbqca"]
    print(f"\nNo chain: {best(w3)}")         # 1

    w4 = ["a"]
    print(f"Single:   {best(w4)}")           # 1
