"""
Problem: Palindrome Partitioning II
Difficulty: HARD | XP: 50
LeetCode: 132

Given a string s, partition it such that every substring is a palindrome.
Return the minimum number of cuts needed for such a partition.

Example: s = "aab"
  Partition ["aa","b"] requires 1 cut.
  Partition ["a","a","b"] requires 2 cuts.
  Minimum is 1.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Recursive Try All Partitions
# Time: O(2^n * n)  |  Space: O(n) recursion stack
# ============================================================
def brute_force(s: str) -> int:
    """
    At each position i, try every possible cut point j >= i.
    If s[i..j] is a palindrome, recursively solve for s[j+1..n-1] and add 1 cut.
    Accumulate the minimum cuts needed.

    Palindrome check is O(n) per call. Splitting has 2^(n-1) ways -> exponential.
    """
    n = len(s)

    def is_palindrome(l: int, r: int) -> bool:
        while l < r:
            if s[l] != s[r]:
                return False
            l += 1
            r -= 1
        return True

    def recurse(start: int) -> int:
        """Min cuts to partition s[start..n-1] into palindromes."""
        if start == n:
            return 0  # empty string, no cuts needed
        if start == n - 1:
            return 0  # single character is always a palindrome
        if is_palindrome(start, n - 1):
            return 0  # whole remaining string is a palindrome

        min_cuts = float('inf')
        for end in range(start, n - 1):  # don't need to check last position (handled above)
            if is_palindrome(start, end):
                cuts = 1 + recurse(end + 1)
                min_cuts = min(min_cuts, cuts)
        return min_cuts

    return recurse(0)


# ============================================================
# APPROACH 2: OPTIMAL - DP with Palindrome Precomputation
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def optimal(s: str) -> int:
    """
    Two-phase DP:

    Phase 1: Precompute palindrome table.
      pal[i][j] = True if s[i..j] is a palindrome.
      Fill using: pal[i][j] = (s[i] == s[j]) and pal[i+1][j-1]
      Base: pal[i][i] = True (single char), pal[i][i+1] = (s[i] == s[i+1])
      Fill bottom-up for increasing lengths.

    Phase 2: DP for minimum cuts.
      dp[i] = minimum cuts for s[0..i]
      Transition:
        For each i from 0 to n-1:
          If s[0..i] is a palindrome: dp[i] = 0 (no cuts needed)
          Else: dp[i] = min(dp[j-1] + 1) for all j in [1..i] where s[j..i] is palindrome

      Answer: dp[n-1]

    Phase 1 is O(n^2) time and space.
    Phase 2 is O(n^2) time, O(n) space (for dp array).
    Total: O(n^2) time, O(n^2) space.
    """
    n = len(s)
    if n == 0:
        return 0

    # Phase 1: precompute pal[i][j]
    pal = [[False] * n for _ in range(n)]
    for i in range(n):
        pal[i][i] = True
    for i in range(n - 1):
        pal[i][i + 1] = (s[i] == s[i + 1])
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            pal[i][j] = (s[i] == s[j]) and pal[i + 1][j - 1]

    # Phase 2: dp[i] = min cuts for s[0..i]
    dp = [0] * n
    for i in range(n):
        if pal[0][i]:
            dp[i] = 0
        else:
            dp[i] = i  # worst case: cut after every character (i cuts for i+1 chars)
            for j in range(1, i + 1):
                if pal[j][i]:
                    dp[i] = min(dp[i], dp[j - 1] + 1)

    return dp[n - 1]


# ============================================================
# APPROACH 3: BEST - Expand Around Center + DP
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def best(s: str) -> int:
    """
    Key insight: instead of precomputing the full O(n^2) palindrome table,
    expand each palindrome center outward and directly update the dp array.

    For each center (single char at i, or between i and i+1):
      Expand while s[left] == s[right]:
        - We know s[left..right] is a palindrome.
        - Update: dp[right] = min(dp[right], (dp[left-1] + 1 if left > 0 else 0))
        - Expand: left -= 1, right += 1

    This achieves O(n^2) time with O(n) space (no pal[][] table).
    The constant factor is smaller because we avoid many palindrome table lookups
    and fill them naturally through center expansion.

    dp[i] = minimum cuts to partition s[0..i] into palindromes.
    Initialize dp[i] = i (worst case: cut before every character).

    For left == 0 (palindrome starts at beginning of string), dp[right] = 0.
    """
    n = len(s)
    if n == 0:
        return 0

    # dp[i] = min cuts for s[0..i]
    # Worst case initialization: dp[i] = i (i cuts means i+1 single chars)
    dp = list(range(n))  # dp[i] = i

    for center in range(n):
        # Odd-length palindromes (center at index `center`)
        left, right = center, center
        while left >= 0 and right < n and s[left] == s[right]:
            # s[left..right] is a palindrome
            if left == 0:
                dp[right] = 0
            else:
                dp[right] = min(dp[right], dp[left - 1] + 1)
            left  -= 1
            right += 1

        # Even-length palindromes (center between `center` and `center+1`)
        left, right = center, center + 1
        while left >= 0 and right < n and s[left] == s[right]:
            if left == 0:
                dp[right] = 0
            else:
                dp[right] = min(dp[right], dp[left - 1] + 1)
            left  -= 1
            right += 1

    return dp[n - 1]


if __name__ == "__main__":
    print("=== Palindrome Partitioning II ===")

    test_cases = [
        ("aab",         1),   # ["aa","b"] -> 1 cut
        ("a",           0),   # Already palindrome
        ("ab",          1),   # ["a","b"] -> 1 cut
        ("aa",          0),   # ["aa"] -> 0 cuts
        ("abcba",       0),   # Whole string is palindrome
        ("abacaba",     0),   # Whole string is palindrome
        ("raceacar",    5),   # best: "r","a","c","e","a","c","a","r" (7 cuts) or shorter combos
        ("aabb",        1),   # ["aa","bb"] -> 1 cut
        ("ababababcbababababd", 2),
    ]

    for s, expected in test_cases:
        b  = brute_force(s)
        o  = optimal(s)
        be = best(s)
        status = "OK" if o == be == expected else ("BRUTE_DIFF" if b != expected else "MISMATCH")
        print(f"s={s!r:25s} => Brute={b}, Optimal={o}, Best={be} | Expected={expected} [{status}]")
