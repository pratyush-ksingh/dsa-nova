"""
Problem: Longest Substring Without Repeating Characters (LeetCode #3)
Difficulty: MEDIUM | XP: 25

Find the length of the longest substring without repeating characters.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Substrings)
# Time: O(n^3) | Space: O(min(n, 128))
# ============================================================
def brute_force(s: str) -> int:
    """Generate every substring, check for uniqueness."""
    n = len(s)
    max_len = 0
    for i in range(n):
        seen = set()
        for j in range(i, n):
            if s[j] in seen:
                break
            seen.add(s[j])
            max_len = max(max_len, j - i + 1)
    return max_len


# ============================================================
# APPROACH 2: SLIDING WINDOW WITH HASH SET
# Time: O(2n) = O(n) | Space: O(min(n, 128))
# ============================================================
def sliding_window_set(s: str) -> int:
    """Expand right, shrink left when duplicate found."""
    n = len(s)
    left = 0
    max_len = 0
    window = set()

    for right in range(n):
        while s[right] in window:
            window.remove(s[left])
            left += 1
        window.add(s[right])
        max_len = max(max_len, right - left + 1)
    return max_len


# ============================================================
# APPROACH 3: BEST (Sliding Window with Hash Map -- Jump)
# Time: O(n) | Space: O(min(n, 128))
# ============================================================
def optimal(s: str) -> int:
    """Store last-seen index per char; jump left past duplicates."""
    n = len(s)
    left = 0
    max_len = 0
    last_seen = {}

    for right in range(n):
        c = s[right]
        if c in last_seen and last_seen[c] >= left:
            left = last_seen[c] + 1
        last_seen[c] = right
        max_len = max(max_len, right - left + 1)
    return max_len


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Longest Substring Without Repeating Characters ===\n")

    test_cases = [
        ("abcabcbb", 3),
        ("bbbbb", 1),
        ("pwwkew", 3),
        ("", 0),
        ("a", 1),
        ("abcdef", 6),
        ("dvdf", 3),
    ]

    all_pass = True
    for s, expected in test_cases:
        b = brute_force(s)
        sw = sliding_window_set(s)
        o = optimal(s)

        ok = b == sw == o == expected
        all_pass &= ok
        print(f"Input: {s!r:15s} | Brute={b} Set={sw} Map={o} | "
              f"Expected={expected} [{'PASS' if ok else 'FAIL'}]")

    print(f"\nAll pass: {all_pass}")
