"""
Problem: Sort Characters by Frequency (LeetCode #451)
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashMap + Sort
# Time: O(n log n)  |  Space: O(n)
#
# Count frequencies, sort unique chars by freq descending,
# rebuild string by repeating each char.
# ============================================================
def brute_force(s: str) -> str:
    freq = {}
    for c in s:
        freq[c] = freq.get(c, 0) + 1

    # Sort characters by frequency descending
    sorted_chars = sorted(freq.keys(), key=lambda c: -freq[c])

    result = []
    for c in sorted_chars:
        result.append(c * freq[c])

    return ''.join(result)


# ============================================================
# APPROACH 2: OPTIMAL -- HashMap + Bucket Sort
# Time: O(n)  |  Space: O(n)
#
# Use bucket sort: bucket[i] holds chars with frequency i.
# Iterate buckets from n down to 1 to build result.
# ============================================================
def optimal(s: str) -> str:
    freq = {}
    for c in s:
        freq[c] = freq.get(c, 0) + 1

    # Bucket sort: index = frequency
    n = len(s)
    buckets = [[] for _ in range(n + 1)]
    for c, count in freq.items():
        buckets[count].append(c)

    result = []
    for i in range(n, 0, -1):
        for c in buckets[i]:
            result.append(c * i)

    return ''.join(result)


# ============================================================
# APPROACH 3: BEST -- Counter + Bucket Sort (compact)
# Time: O(n)  |  Space: O(n)
#
# Same bucket sort logic, using Counter for conciseness.
# ============================================================
def best(s: str) -> str:
    freq = Counter(s)
    n = len(s)
    buckets = [[] for _ in range(n + 1)]
    for c, count in freq.items():
        buckets[count].append(c)
    return ''.join(c * i for i in range(n, 0, -1) for c in buckets[i])


if __name__ == "__main__":
    print("=== Sort Characters by Frequency ===\n")

    tests = [
        ("tree", "eert"),
        ("cccaaa", "aaaccc"),
        ("Aabb", "bbAa"),
        ("a", "a"),
    ]

    for s, expected in tests:
        print(f"Input:    \"{s}\"")
        print(f"Expected: \"{expected}\" (or valid variant)")
        print(f"Brute:    \"{brute_force(s)}\"")
        print(f"Optimal:  \"{optimal(s)}\"")
        print(f"Best:     \"{best(s)}\"")
        print()
