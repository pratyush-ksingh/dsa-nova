"""
Problem: Sum of Beauty of All Substrings (LeetCode #1781)
Difficulty: MEDIUM | XP: 25
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Generate All Substrings + Count
# Time: O(n³)  |  Space: O(26)
#
# For every substring s[i..j], build a frequency array,
# find max_freq and min_freq (only for chars that appear),
# add (max_freq - min_freq) to the total.
# ============================================================
def brute_force(s: str) -> int:
    n = len(s)
    total = 0
    for i in range(n):
        for j in range(i + 1, n):
            freq = {}
            for c in s[i:j + 1]:
                freq[c] = freq.get(c, 0) + 1
            total += max(freq.values()) - min(freq.values())
    return total


# ============================================================
# APPROACH 2: OPTIMAL -- O(n²) sliding window with freq array
# Time: O(n² * 26)  |  Space: O(26)
#
# Fix start index i, expand j rightward. Maintain a freq array
# of size 26. For each new character, update max/min in O(26).
# ============================================================
def optimal(s: str) -> int:
    n = len(s)
    total = 0
    for i in range(n):
        freq = [0] * 26
        for j in range(i, n):
            freq[ord(s[j]) - ord('a')] += 1
            max_f = max(freq)
            min_f = min(v for v in freq if v > 0)
            total += max_f - min_f
    return total


# ============================================================
# APPROACH 3: BEST -- O(n²) with explicit max/min tracking
# Time: O(n² * 26)  |  Space: O(26)
#
# Same as optimal but avoids calling min() on entire array
# each iteration by only scanning non-zero entries.
# For long substrings, this has better constant factors.
# ============================================================
def best(s: str) -> int:
    n = len(s)
    total = 0
    for i in range(n):
        freq = [0] * 26
        max_f = 0
        for j in range(i, n):
            idx = ord(s[j]) - ord('a')
            freq[idx] += 1
            if freq[idx] > max_f:
                max_f = freq[idx]
            # min_f: smallest non-zero frequency
            min_f = max_f  # worst case upper bound
            for v in freq:
                if 0 < v < min_f:
                    min_f = v
            total += max_f - min_f
    return total


if __name__ == "__main__":
    print("=== Sum of Beauty of All Substrings ===\n")

    tests = [
        ("aabcb", 5),
        ("aabcbaa", 17),
        ("a", 0),
        ("aa", 0),
        ("abcd", 0),
    ]

    for s, expected in tests:
        b = brute_force(s)
        o = optimal(s)
        bt = best(s)
        status = "OK" if b == o == bt == expected else "MISMATCH"
        print(f"Input: \"{s}\"  Expected={expected}  "
              f"Brute={b}  Optimal={o}  Best={bt}  [{status}]")
