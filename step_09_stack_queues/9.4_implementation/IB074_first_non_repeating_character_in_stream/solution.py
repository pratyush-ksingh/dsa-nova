"""
Problem: First Non-Repeating Character in Stream
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a stream of characters, after each character arrives,
print the first non-repeating character seen so far.
Print '#' if no such character exists.
Real-life use: Real-time analytics, log deduplication, stream processing.
"""
from typing import List
from collections import Counter, OrderedDict, deque


# ============================================================
# APPROACH 1: BRUTE FORCE
# For each new character, rescan all chars to find first with freq=1.
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(stream: str) -> str:
    result = []
    seen = []
    freq: dict = {}
    for c in stream:
        seen.append(c)
        freq[c] = freq.get(c, 0) + 1
        ans = '#'
        for ch in seen:
            if freq[ch] == 1:
                ans = ch
                break
        result.append(ans)
    return "".join(result)


# ============================================================
# APPROACH 2: OPTIMAL
# Queue + frequency array.
# Queue maintains order; pop from front while front is repeating.
# Time: O(N * 26) = O(N)  |  Space: O(26) = O(1)
# ============================================================
def optimal(stream: str) -> str:
    result = []
    freq = [0] * 26
    q: deque = deque()
    for c in stream:
        freq[ord(c) - ord('a')] += 1
        q.append(c)
        while q and freq[ord(q[0]) - ord('a')] > 1:
            q.popleft()
        result.append(q[0] if q else '#')
    return "".join(result)


# ============================================================
# APPROACH 3: BEST
# OrderedDict: preserves insertion order, O(1) deletion.
# When a char repeats, remove it from dict. First key = answer.
# Time: O(N)  |  Space: O(26) = O(1)
# ============================================================
def best(stream: str) -> str:
    result = []
    od: OrderedDict = OrderedDict()
    for c in stream:
        if c in od:
            del od[c]          # already repeating — never first non-rep again
        elif c not in od:
            # Use a set to track seen-more-than-once
            pass
        result.append(next(iter(od)) if od else '#')
    return "".join(result)


# Fix: need to track "seen before" vs "in map"
def best(stream: str) -> str:  # noqa: F811
    result = []
    od: OrderedDict = OrderedDict()
    seen_twice: set = set()
    for c in stream:
        if c in seen_twice:
            pass  # already removed
        elif c in od:
            del od[c]
            seen_twice.add(c)
        else:
            od[c] = True
        result.append(next(iter(od)) if od else '#')
    return "".join(result)


if __name__ == "__main__":
    print("=== First Non-Repeating Character in Stream ===")

    streams = ["aabc", "aabcc", "geeksforgeeks", "a", "zz"]
    for s in streams:
        print(f'\nstream="{s}"')
        print(f"  Brute  : {brute_force(s)}")
        print(f"  Optimal: {optimal(s)}")
        print(f"  Best   : {best(s)}")
