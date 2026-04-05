"""
Problem: Longest Substring with At Most K Distinct Characters
LeetCode 340 | Difficulty: HARD | XP: 50

Key Insight: Use a sliding window. Expand right, shrink left whenever
             the number of distinct characters exceeds k.
"""
from typing import List
from collections import defaultdict, OrderedDict


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(k)
# ============================================================
def brute_force(s: str, k: int) -> int:
    """
    Generate every substring starting at index i.
    Track distinct characters with a set.
    Stop the inner loop as soon as distinct count exceeds k.
    """
    if k == 0 or not s:
        return 0
    n = len(s)
    best_len = 0
    for i in range(n):
        seen = set()
        for j in range(i, n):
            seen.add(s[j])
            if len(seen) > k:
                break
            best_len = max(best_len, j - i + 1)
    return best_len


# ============================================================
# APPROACH 2: OPTIMAL  (Sliding Window + HashMap)
# Time: O(n)  |  Space: O(k)
# ============================================================
def optimal(s: str, k: int) -> int:
    """
    Two-pointer sliding window [left, right].
    `freq` maps character -> count in current window.
    When len(freq) > k, advance left until we drop a character completely.
    """
    if k == 0 or not s:
        return 0
    freq: dict = defaultdict(int)
    left = 0
    best_len = 0
    for right, ch in enumerate(s):
        freq[ch] += 1
        # Shrink window until at most k distinct chars
        while len(freq) > k:
            lch = s[left]
            freq[lch] -= 1
            if freq[lch] == 0:
                del freq[lch]
            left += 1
        best_len = max(best_len, right - left + 1)
    return best_len


# ============================================================
# APPROACH 3: BEST  (Sliding Window + OrderedDict for O(1) shrink)
# Time: O(n)  |  Space: O(k)
# ============================================================
def best(s: str, k: int) -> int:
    """
    Same sliding window, but use an OrderedDict to remember the
    MOST RECENT position of each character.
    When window has k+1 distinct chars, we can instantly find and
    remove the leftmost character without scanning the whole window.

    This turns the worst-case inner while loop into O(1) amortised,
    keeping total time O(n) even with a tighter constant.
    """
    if k == 0 or not s:
        return 0
    # Maps char -> rightmost index seen in current window
    last_seen: OrderedDict = OrderedDict()
    left = 0
    best_len = 0
    for right, ch in enumerate(s):
        # Refresh position of ch (move to end of OrderedDict)
        if ch in last_seen:
            del last_seen[ch]
        last_seen[ch] = right

        # Window has k+1 distinct chars: remove the one seen earliest
        if len(last_seen) > k:
            _, leftmost_idx = last_seen.popitem(last=False)
            left = leftmost_idx + 1

        best_len = max(best_len, right - left + 1)
    return best_len


if __name__ == "__main__":
    test_cases = [
        ("eceba", 2, 3),
        ("aa", 1, 2),
        ("abcadcacacaca", 3, 11),
        ("", 2, 0),
        ("abc", 0, 0),
    ]
    print("=== Longest Substring with At Most K Distinct ===")
    for s, k, expected in test_cases:
        b   = brute_force(s, k)
        o   = optimal(s, k)
        bst = best(s, k)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  s={s!r} k={k} => brute={b}, optimal={o}, best={bst} (expected {expected}) [{status}]")
