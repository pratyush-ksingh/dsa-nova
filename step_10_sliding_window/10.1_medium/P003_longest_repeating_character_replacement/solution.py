"""
Problem: Longest Repeating Character Replacement (LeetCode #424)
Difficulty: MEDIUM | XP: 25

Replace at most k characters to get the longest repeating substring.
Key insight: window is valid when (windowLen - maxFreq) <= k.
"""
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Substrings)
# Time: O(n^2) | Space: O(1)
# ============================================================
def brute_force(s: str, k: int) -> int:
    """Try every substring, check if it can become all-same with <= k replacements."""
    n = len(s)
    ans = 0
    for i in range(n):
        freq = defaultdict(int)
        max_freq = 0
        for j in range(i, n):
            freq[s[j]] += 1
            max_freq = max(max_freq, freq[s[j]])
            window_len = j - i + 1
            if window_len - max_freq <= k:
                ans = max(ans, window_len)
    return ans


# ============================================================
# APPROACH 2: OPTIMAL (Sliding Window -- Shrinking)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(s: str, k: int) -> int:
    """Sliding window: expand right, shrink left when invalid."""
    freq = defaultdict(int)
    max_freq = 0
    left = 0
    ans = 0

    for right in range(len(s)):
        freq[s[right]] += 1
        max_freq = max(max_freq, freq[s[right]])

        # Shrink window until valid
        while (right - left + 1) - max_freq > k:
            freq[s[left]] -= 1
            left += 1

        ans = max(ans, right - left + 1)

    return ans


# ============================================================
# APPROACH 3: BEST (Sliding Window -- Non-Shrinking)
# Time: O(n) | Space: O(1)
# ============================================================
def best(s: str, k: int) -> int:
    """Non-shrinking window: maxFreq never decreases, window only grows or slides."""
    n = len(s)
    freq = defaultdict(int)
    max_freq = 0
    left = 0

    for right in range(n):
        freq[s[right]] += 1
        max_freq = max(max_freq, freq[s[right]])

        # If invalid, slide (not shrink) -- move left by 1
        if (right - left + 1) - max_freq > k:
            freq[s[left]] -= 1
            left += 1

    # Window size never shrinks, so final size = n - left
    return n - left


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Longest Repeating Character Replacement ===\n")

    test_cases = [
        ("ABAB", 2, 4),
        ("AABABBA", 1, 4),
        ("AAAA", 2, 4),
        ("ABCDE", 0, 1),
        ("A", 1, 1),
        ("ABBB", 2, 4),
    ]

    for s, k, expected in test_cases:
        b = brute_force(s, k)
        o = optimal(s, k)
        r = best(s, k)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: s=\"{s}\", k={k}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
