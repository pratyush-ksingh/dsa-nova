"""
Problem: Print Longest Common Subsequence
Difficulty: MEDIUM | XP: 25

Not just find LENGTH of LCS but actually PRINT the LCS string.
Build DP table, then backtrack through it.
All 4 DP approaches: Recursion -> Memo -> Tab -> Tab + Backtrack (Print)
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION (Length only)
# Time: O(2^(m+n)) | Space: O(m+n)
# ============================================================
def lcs_recursive(s1: str, s2: str) -> int:
    """Recursively compute LCS length with take/skip branching."""
    def solve(i: int, j: int) -> int:
        if i == 0 or j == 0:
            return 0
        if s1[i - 1] == s2[j - 1]:
            return 1 + solve(i - 1, j - 1)
        return max(solve(i - 1, j), solve(i, j - 1))

    return solve(len(s1), len(s2))


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def lcs_memo(s1: str, s2: str) -> int:
    """Cache (i, j) states to avoid recomputation."""
    dp = {}

    def solve(i: int, j: int) -> int:
        if i == 0 or j == 0:
            return 0
        if (i, j) in dp:
            return dp[(i, j)]

        if s1[i - 1] == s2[j - 1]:
            dp[(i, j)] = 1 + solve(i - 1, j - 1)
        else:
            dp[(i, j)] = max(solve(i - 1, j), solve(i, j - 1))
        return dp[(i, j)]

    return solve(len(s1), len(s2))


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def lcs_tab(s1: str, s2: str) -> int:
    """Build dp[m+1][n+1] table iteratively."""
    m, n = len(s1), len(s2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i - 1] == s2[j - 1]:
                dp[i][j] = 1 + dp[i - 1][j - 1]
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

    return dp[m][n]


# ============================================================
# APPROACH 4: TABULATION + BACKTRACKING (PRINT the LCS)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def print_lcs(s1: str, s2: str) -> str:
    """Build DP table, then backtrack to reconstruct the actual LCS string."""
    m, n = len(s1), len(s2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    # Phase 1: Build the DP table
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i - 1] == s2[j - 1]:
                dp[i][j] = 1 + dp[i - 1][j - 1]
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

    # Phase 2: Backtrack to reconstruct the LCS string
    result = []
    i, j = m, n
    while i > 0 and j > 0:
        if s1[i - 1] == s2[j - 1]:
            # This character is part of LCS
            result.append(s1[i - 1])
            i -= 1
            j -= 1
        elif dp[i - 1][j] > dp[i][j - 1]:
            # Move up (skip character from s1)
            i -= 1
        else:
            # Move left (skip character from s2)
            j -= 1

    # Characters were collected in reverse order
    return "".join(reversed(result))


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Print Longest Common Subsequence ===\n")

    test_cases = [
        ("abcde", "ace", 3, "ace"),
        ("acd", "ced", 2, "cd"),
        ("abc", "def", 0, ""),
        ("abcba", "abcbcba", 5, "abcba"),
        ("a", "a", 1, "a"),
        ("a", "b", 0, ""),
        ("", "abc", 0, ""),
    ]

    for s1, s2, exp_len, exp_str in test_cases:
        r = lcs_recursive(s1, s2)
        m = lcs_memo(s1, s2)
        t = lcs_tab(s1, s2)
        printed = print_lcs(s1, s2)

        passes = (r == exp_len and m == exp_len and t == exp_len
                  and printed == exp_str)
        print(f's1="{s1}", s2="{s2}"')
        print(f"  Rec={r} | Memo={m} | Tab={t} | Print=\"{printed}\"")
        print(f"  Expected len={exp_len}, str=\"{exp_str}\" | Pass={passes}\n")
