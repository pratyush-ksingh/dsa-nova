"""
Problem: Count Number of Substrings with Exactly K Distinct Characters
Difficulty: MEDIUM | XP: 25
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check All Substrings
# Time: O(n³)  |  Space: O(k)
#
# Generate every substring, count distinct characters in each,
# and increment counter if it equals k.
# ============================================================
def brute_force(s: str, k: int) -> int:
    n = len(s)
    count = 0
    for i in range(n):
        for j in range(i + 1, n + 1):
            substr = s[i:j]
            if len(set(substr)) == k:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL -- atMost(k) - atMost(k-1) Sliding Window
# Time: O(n)  |  Space: O(k)
#
# Key insight: exactly(k) = atMost(k) - atMost(k-1).
# atMost(k) counts substrings with AT MOST k distinct chars
# using a two-pointer sliding window.
# ============================================================
def optimal(s: str, k: int) -> int:
    def at_most(k: int) -> int:
        freq = defaultdict(int)
        left = 0
        count = 0
        for right in range(len(s)):
            freq[s[right]] += 1
            while len(freq) > k:
                freq[s[left]] -= 1
                if freq[s[left]] == 0:
                    del freq[s[left]]
                left += 1
            # All substrings ending at 'right' starting from left..right
            count += right - left + 1
        return count

    return at_most(k) - at_most(k - 1)


# ============================================================
# APPROACH 3: BEST -- Same sliding window, slightly cleaner
# Time: O(n)  |  Space: O(1) -- at most 26 letters
#
# Same atMost trick but uses a fixed-size int array[26] instead
# of a defaultdict, avoiding hash overhead for lowercase letters.
# ============================================================
def best(s: str, k: int) -> int:
    def at_most(k: int) -> int:
        freq = [0] * 26
        distinct = 0
        left = 0
        count = 0
        for right in range(len(s)):
            idx = ord(s[right]) - ord('a')
            if freq[idx] == 0:
                distinct += 1
            freq[idx] += 1
            while distinct > k:
                lidx = ord(s[left]) - ord('a')
                freq[lidx] -= 1
                if freq[lidx] == 0:
                    distinct -= 1
                left += 1
            count += right - left + 1
        return count

    return at_most(k) - at_most(k - 1)


if __name__ == "__main__":
    print("=== Count Number of Substrings with Exactly K Distinct Chars ===\n")

    tests = [
        ("abc", 2, 2),
        ("aba", 2, 3),
        ("aa", 1, 3),
        ("aabab", 2, 9),
    ]

    for s, k, expected in tests:
        b = brute_force(s, k)
        o = optimal(s, k)
        bt = best(s, k)
        status = "OK" if b == o == bt == expected else "MISMATCH"
        print(f"Input: s=\"{s}\", k={k}  |  Expected={expected}  "
              f"Brute={b}  Optimal={o}  Best={bt}  [{status}]")
