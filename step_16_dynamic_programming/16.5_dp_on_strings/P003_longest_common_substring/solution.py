"""
Problem: Longest Common Substring
Difficulty: MEDIUM | XP: 25

Find length of longest common substring between two strings.
dp[i][j] resets to 0 on mismatch (unlike LCS).
All 4 DP approaches.
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(n^2 * m) in practice | Space: O(min(n,m)) stack
# ============================================================
def lcs_recursive(s1: str, s2: str) -> int:
    """Try every pair of endpoints, extend matching streaks recursively."""
    n, m = len(s1), len(s2)

    def streak(i: int, j: int) -> int:
        """Length of common substring ending at s1[i-1], s2[j-1]."""
        if i == 0 or j == 0:
            return 0
        if s1[i - 1] == s2[j - 1]:
            return 1 + streak(i - 1, j - 1)
        return 0

    max_len = 0
    for i in range(1, n + 1):
        for j in range(1, m + 1):
            max_len = max(max_len, streak(i, j))
    return max_len


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n * m) | Space: O(n * m)
# ============================================================
def lcs_memo(s1: str, s2: str) -> int:
    """Cache streak lengths for each (i, j) pair."""
    n, m = len(s1), len(s2)
    cache = {}

    def streak(i: int, j: int) -> int:
        if i == 0 or j == 0:
            return 0
        if (i, j) in cache:
            return cache[(i, j)]

        if s1[i - 1] == s2[j - 1]:
            cache[(i, j)] = 1 + streak(i - 1, j - 1)
        else:
            cache[(i, j)] = 0

        return cache[(i, j)]

    max_len = 0
    for i in range(1, n + 1):
        for j in range(1, m + 1):
            max_len = max(max_len, streak(i, j))
    return max_len


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n * m) | Space: O(n * m)
# ============================================================
def lcs_tab(s1: str, s2: str) -> int:
    """Fill 2D table. dp[i][j] = streak length ending at s1[i-1], s2[j-1]."""
    n, m = len(s1), len(s2)
    dp = [[0] * (m + 1) for _ in range(n + 1)]

    max_len = 0

    for i in range(1, n + 1):
        for j in range(1, m + 1):
            if s1[i - 1] == s2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
                max_len = max(max_len, dp[i][j])
            else:
                dp[i][j] = 0  # streak resets

    return max_len


# ============================================================
# APPROACH 4: SPACE OPTIMIZED (1D array, right-to-left)
# Time: O(n * m) | Space: O(m)
# ============================================================
def lcs_space(s1: str, s2: str) -> int:
    """
    Single 1D array. Traverse j right-to-left to preserve dp[j-1]
    from the previous row (dp[i][j] depends on dp[i-1][j-1]).
    """
    n, m = len(s1), len(s2)
    dp = [0] * (m + 1)

    max_len = 0

    for i in range(1, n + 1):
        # Right to left: dp[j-1] still holds the value from row i-1
        for j in range(m, 0, -1):
            if s1[i - 1] == s2[j - 1]:
                dp[j] = dp[j - 1] + 1
                max_len = max(max_len, dp[j])
            else:
                dp[j] = 0

    return max_len


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Longest Common Substring ===\n")

    test_cases = [
        ("abcde", "abfce", 2),
        ("abc", "abc", 3),
        ("abc", "def", 0),
        ("abcdxyz", "xyzabcd", 4),
        ("", "abc", 0),
        ("a", "a", 1),
        ("zxabcdezy", "yzabcdezx", 6),
    ]

    header = f"{'s1':<20} {'s2':<20} {'Rec':<6} {'Memo':<6} {'Tab':<6} {'Space':<6} {'Exp':<6} {'Pass':<6}"
    print(header)
    print("-" * len(header))

    for s1, s2, expected in test_cases:
        r = lcs_recursive(s1, s2)
        m = lcs_memo(s1, s2)
        t = lcs_tab(s1, s2)
        s = lcs_space(s1, s2)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"{repr(s1):<20} {repr(s2):<20} {r:<6} {m:<6} {t:<6} {s:<6} {expected:<6} {passes}")

    # Show difference from LCS
    print("\n--- LCS vs Longest Common Substring ---")
    print("s1='abcde', s2='ace'")
    print(f"Longest Common Substring = {lcs_tab('abcde', 'ace')} (only single char matches)")
    print("LCS (subsequence) = 3 (a, c, e -- not contiguous)")

    # Show the actual substring
    print("\n--- Extracting the actual substring ---")
    s1, s2 = "abcdxyz", "xyzabcd"
    n, m = len(s1), len(s2)
    dp = [[0] * (m + 1) for _ in range(n + 1)]
    max_len, end_i = 0, 0
    for i in range(1, n + 1):
        for j in range(1, m + 1):
            if s1[i - 1] == s2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
                if dp[i][j] > max_len:
                    max_len = dp[i][j]
                    end_i = i
    print(f"Longest common substring of '{s1}' and '{s2}': '{s1[end_i - max_len:end_i]}' (length {max_len})")
