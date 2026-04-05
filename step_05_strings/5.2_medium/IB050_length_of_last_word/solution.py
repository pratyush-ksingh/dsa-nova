"""
Problem: Length of Last Word
Difficulty: EASY | XP: 10
Source: InterviewBit | LeetCode #58

Key Insight: Scan from right -- skip trailing spaces, then count non-spaces.
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE -- Split and Take Last
# Time: O(n)  |  Space: O(n)
#
# Split by spaces, return length of last non-empty token.
# ============================================================
def brute_force(s: str) -> int:
    words = s.split()  # split() handles multiple spaces and strips
    return len(words[-1]) if words else 0


# ============================================================
# APPROACH 2: OPTIMAL -- Right-to-Left Scan
# Time: O(k)  |  Space: O(1)
#
# Phase 1: skip trailing spaces.
# Phase 2: count non-space characters.
# ============================================================
def optimal(s: str) -> int:
    i = len(s) - 1

    # Phase 1: skip trailing spaces
    while i >= 0 and s[i] == ' ':
        i -= 1

    # Phase 2: count last word characters
    length = 0
    while i >= 0 and s[i] != ' ':
        length += 1
        i -= 1

    return length


# ============================================================
# APPROACH 3: BEST -- Single loop variant
# Time: O(k)  |  Space: O(1)
#
# Combines both phases with a flag for cleaner code.
# ============================================================
def best(s: str) -> int:
    length = 0
    found_word = False

    for i in range(len(s) - 1, -1, -1):
        if s[i] != ' ':
            found_word = True
            length += 1
        elif found_word:
            break

    return length


if __name__ == "__main__":
    print("=== Length of Last Word ===")
    tests = [
        "Hello World",
        "   fly me   to   the moon  ",
        "luffy is still joyboy",
        "a",
        "   ",
    ]
    for t in tests:
        print(f'Input: "{t}"')
        print(f"  Brute:   {brute_force(t)}")
        print(f"  Optimal: {optimal(t)}")
        print(f"  Best:    {best(t)}")
        print()
