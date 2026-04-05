"""
Problem: Reverse Words in a String (LeetCode 151)
Difficulty: MEDIUM | XP: 25

Given a string s, reverse the order of the words.
A word is defined as a sequence of non-space characters.
The returned string must not contain leading/trailing spaces,
and words must be separated by a single space.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Split on whitespace (handles multiple spaces), filter empties,
# reverse the list, then join with single space.
# ============================================================
def brute_force(s: str) -> str:
    """
    Split the string on any whitespace (Python's default split()),
    which automatically handles multiple spaces, leading/trailing spaces.
    Reverse the resulting word list and rejoin.
    """
    words = s.split()          # ["the", "sky", "is", "blue"]
    words.reverse()            # ["blue", "is", "sky", "the"]
    return " ".join(words)


# ============================================================
# APPROACH 2: OPTIMAL  (in-place on char array — O(1) extra)
# Time: O(n)  |  Space: O(n)  [O(1) ignoring the char list itself]
# 1. Convert to char list, strip leading/trailing spaces.
# 2. Collapse multiple spaces to single spaces.
# 3. Reverse the entire array.
# 4. Reverse each individual word back.
# ============================================================
def optimal(s: str) -> str:
    """
    Classic in-place two-step reversal on a character array:
    - Reverse the whole string  => words are reversed but each word itself is backwards.
    - Reverse each word individually => every word is restored to normal order.
    Also handles extra spaces by cleaning up first.
    """
    # Step 1: Clean extra spaces (strip + collapse internal spaces)
    chars = list(" ".join(s.split()))   # still O(n) space for char list

    n = len(chars)

    def reverse_range(arr, lo, hi):
        while lo < hi:
            arr[lo], arr[hi] = arr[hi], arr[lo]
            lo += 1
            hi -= 1

    # Step 2: Reverse the entire string
    reverse_range(chars, 0, n - 1)

    # Step 3: Reverse each word
    start = 0
    for i in range(n + 1):
        if i == n or chars[i] == ' ':
            reverse_range(chars, start, i - 1)
            start = i + 1

    return "".join(chars)


# ============================================================
# APPROACH 3: BEST  (two pointers scanning from the end)
# Time: O(n)  |  Space: O(n)
# Walk backwards through the string collecting word boundaries,
# append each word to a result list, then join.
# Avoids any full-string reversal; reads each char at most twice.
# ============================================================
def best(s: str) -> str:
    """
    Two-pointer scan from the right end of the string.
    'right' marks the end of the current word, 'left' walks left
    until it hits a space (or the start), then we slice out the word.
    This naturally produces words in reversed order without reversing
    anything twice.
    """
    result = []
    n = len(s)
    right = n - 1

    while right >= 0:
        # Skip trailing spaces
        while right >= 0 and s[right] == ' ':
            right -= 1
        if right < 0:
            break

        # 'right' now points to the last char of a word
        left = right
        while left >= 0 and s[left] != ' ':
            left -= 1
        # s[left+1 : right+1] is the word
        result.append(s[left + 1: right + 1])
        right = left - 1

    return " ".join(result)


if __name__ == "__main__":
    test_cases = [
        ("the sky is blue", "blue is sky the"),
        ("  hello world  ", "world hello"),
        ("a good   example", "example good a"),
        ("  Bob    Loves  Alice   ", "Alice Loves Bob"),
        ("Alice does not even like bob", "bob like even not does Alice"),
    ]

    print("=== Reverse Words in a String ===\n")
    for s, expected in test_cases:
        b = brute_force(s)
        o = optimal(s)
        bst = best(s)
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] Input: {repr(s)}")
        print(f"       Brute:   {repr(b)}")
        print(f"       Optimal: {repr(o)}")
        print(f"       Best:    {repr(bst)}")
        print(f"       Expect:  {repr(expected)}\n")
