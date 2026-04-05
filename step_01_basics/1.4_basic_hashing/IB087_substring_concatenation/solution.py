"""
Problem: Substring Concatenation
Difficulty: HARD | XP: 50
Source: InterviewBit

Given string S and a list of words (all same length), find all starting indices
in S where a concatenation of ALL words (in any order) begins.
"""
from typing import List
from collections import Counter


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * m * w)  |  Space: O(m * w)
# where n=|S|, m=num words, w=word length
# ============================================================
def brute_force(S: str, words: List[str]) -> List[int]:
    """
    At each position i, extract m*w chars, split into w-length chunks,
    count words and compare to required word counts.
    Real-life: Pattern matching in log analysis (finding all required keywords in a window).
    """
    if not S or not words:
        return []
    w = len(words[0])
    m = len(words)
    total = w * m
    n = len(S)
    word_count = Counter(words)
    result = []
    for i in range(n - total + 1):
        seen: Counter = Counter()
        j = 0
        while j < m:
            chunk = S[i + j * w: i + j * w + w]
            if chunk not in word_count:
                break
            seen[chunk] += 1
            if seen[chunk] > word_count[chunk]:
                break
            j += 1
        if j == m:
            result.append(i)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n * w)  |  Space: O(m * w)
# ============================================================
def optimal(S: str, words: List[str]) -> List[int]:
    """
    Sliding window with w starting offsets (0..w-1). For each offset, slide a
    window of m words maintaining a running counter. When over-count, shrink left.
    Real-life: Efficient document search — finding paragraphs containing a required word set.
    """
    if not S or not words:
        return []
    w = len(words[0])
    m = len(words)
    n = len(S)
    word_count = Counter(words)
    result = []

    for offset in range(w):
        window: Counter = Counter()
        count = 0   # number of valid words in current window
        left = offset

        right = offset
        while right + w <= n:
            word = S[right: right + w]
            if word in word_count:
                window[word] += 1
                count += 1
                # Shrink if over quota
                while window[word] > word_count[word]:
                    left_word = S[left: left + w]
                    window[left_word] -= 1
                    count -= 1
                    left += w
                if count == m:
                    result.append(left)
                    left_word = S[left: left + w]
                    window[left_word] -= 1
                    count -= 1
                    left += w
            else:
                window.clear()
                count = 0
                left = right + w
            right += w

    return sorted(result)


# ============================================================
# APPROACH 3: BEST
# Time: O(n * w)  |  Space: O(m * w)
# ============================================================
def best(S: str, words: List[str]) -> List[int]:
    """
    Same sliding-window approach — already optimal. Provided for completeness.
    Real-life: High-throughput text search engines (phrase matching).
    """
    return optimal(S, words)


if __name__ == "__main__":
    print("=== Substring Concatenation ===")
    tests = [
        ("barfoothefoobarman",      ["foo","bar"],          [0, 9]),
        ("wordgoodgoodgoodbestword", ["word","good","best","word"], []),
        ("barfoofoobarthefoobarman", ["bar","foo","the"],   [6, 9, 12]),
    ]
    for S, W, exp in tests:
        print(f"\nS={S}, words={W}")
        print(f"Expected: {exp}")
        print(f"Brute:    {brute_force(S, W)}")
        print(f"Optimal:  {optimal(S, W)}")
        print(f"Best:     {best(S, W)}")
