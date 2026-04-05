"""
Problem: Longest Common Subsequence (LeetCode #1143)
Difficulty: MEDIUM | XP: 25

Find length of LCS of two strings.
All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^(m+n)) | Space: O(m+n)
# ============================================================
def lcs_recursive(text1: str, text2: str) -> int:
    """Compare from end: match -> diagonal+1, mismatch -> max(skip one)."""
    def solve(i: int, j: int) -> int:
        if i < 0 or j < 0:
            return 0

        if text1[i] == text2[j]:
            return 1 + solve(i - 1, j - 1)

        return max(solve(i - 1, j), solve(i, j - 1))

    return solve(len(text1) - 1, len(text2) - 1)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(m * n) | Space: O(m * n)
# Uses 1-based indexing to simplify base cases
# ============================================================
def lcs_memo(text1: str, text2: str) -> int:
    """Cache (i, j) states. 1-based indexing."""
    m, n = len(text1), len(text2)
    dp = {}

    def solve(i: int, j: int) -> int:
        if i == 0 or j == 0:
            return 0
        if (i, j) in dp:
            return dp[(i, j)]

        if text1[i - 1] == text2[j - 1]:
            dp[(i, j)] = 1 + solve(i - 1, j - 1)
        else:
            dp[(i, j)] = max(solve(i - 1, j), solve(i, j - 1))

        return dp[(i, j)]

    return solve(m, n)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(m * n) | Space: O(m * n)
# ============================================================
def lcs_tab(text1: str, text2: str) -> int:
    """Build dp[m+1][n+1] table with 1-based indexing."""
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i - 1] == text2[j - 1]:
                dp[i][j] = 1 + dp[i - 1][j - 1]  # match: diagonal + 1
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])  # max(up, left)

    return dp[m][n]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED
# Time: O(m * n) | Space: O(min(m, n))
# ============================================================
def lcs_space(text1: str, text2: str) -> int:
    """Two rolling arrays -- O(min(m, n)) space."""
    m, n = len(text1), len(text2)

    # Make shorter string the column dimension
    if m < n:
        return lcs_space(text2, text1)

    prev = [0] * (n + 1)

    for i in range(1, m + 1):
        curr = [0] * (n + 1)
        for j in range(1, n + 1):
            if text1[i - 1] == text2[j - 1]:
                curr[j] = 1 + prev[j - 1]  # diagonal
            else:
                curr[j] = max(prev[j], curr[j - 1])  # max(up, left)
        prev = curr

    return prev[n]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Longest Common Subsequence ===\n")

    test_cases = [
        ("abcde", "ace", 3),
        ("abc", "abc", 3),
        ("abc", "def", 0),
        ("aab", "azb", 2),
        ("a", "a", 1),
        ("a", "b", 0),
        ("bsbininm", "jmjkbkjkv", 1),
    ]

    for text1, text2, expected in test_cases:
        r = lcs_recursive(text1, text2)
        m = lcs_memo(text1, text2)
        t = lcs_tab(text1, text2)
        s = lcs_space(text1, text2)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f'text1="{text1}", text2="{text2}"')
        print(f"  Rec={r} | Memo={m} | Tab={t} | Space={s}")
        print(f"  Expected={expected} | Pass={passes}\n")
