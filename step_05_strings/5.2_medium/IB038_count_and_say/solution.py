"""
Problem: Count and Say
LeetCode 38 | Difficulty: MEDIUM | XP: 25

The "count-and-say" sequence:
  countAndSay(1) = "1"
  countAndSay(n) = the run-length encoding of countAndSay(n-1).

Example: "1211" -> one 1, one 2, two 1s -> "111221"
"""
from typing import List
import re


# ============================================================
# APPROACH 1: BRUTE FORCE  -  Iterative character-by-character
# Time: O(n * L)  where L = max length of any term  |  Space: O(L)
# ============================================================
def brute_force(n: int) -> str:
    """
    Start with "1".  For each of the next n-1 steps, scan the current
    string and build the next one by counting consecutive same characters.
    """
    result = "1"

    for _ in range(n - 1):
        next_result = []
        i = 0
        while i < len(result):
            char = result[i]
            count = 0
            while i < len(result) and result[i] == char:
                count += 1
                i += 1
            next_result.append(str(count))
            next_result.append(char)
        result = "".join(next_result)

    return result


# ============================================================
# APPROACH 2: OPTIMAL  -  Same iteration but using groupby
# Time: O(n * L)  |  Space: O(L)
# ============================================================
def optimal(n: int) -> str:
    """
    Use itertools.groupby to group consecutive equal characters
    cleanly.  Functionally same complexity, but more Pythonic.
    """
    from itertools import groupby

    result = "1"

    for _ in range(n - 1):
        result = "".join(
            str(len(list(group))) + char
            for char, group in groupby(result)
        )

    return result


# ============================================================
# APPROACH 3: BEST  -  Iterative with regex (run-length encoding)
# Time: O(n * L)  |  Space: O(L)
# ============================================================
def best(n: int) -> str:
    """
    Use a regex to find all maximal runs of the same digit.
    r'(.)\\1*' matches a character followed by zero or more of itself.
    For each match, emit count + character.
    """
    result = "1"

    for _ in range(n - 1):
        result = re.sub(
            r"(.)\1*",
            lambda m: str(len(m.group(0))) + m.group(1),
            result
        )

    return result


if __name__ == "__main__":
    print("=== Count And Say ===")
    for i in range(1, 8):
        b = brute_force(i)
        o = optimal(i)
        be = best(i)
        print(f"n={i}  Brute={b}  Optimal={o}  Best={be}")
    # Expected sequence: 1, 11, 21, 1211, 111221, 312211, 13112221
