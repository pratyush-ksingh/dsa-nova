"""
Problem: Letter Phone
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a digit string (2-9), return all possible letter combinations
as a phone keypad would produce (T9 style).
Real-life use: Predictive text, spell-checking, password generation.
"""
from typing import List

KEYS = ["", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"]


# ============================================================
# APPROACH 1: BRUTE FORCE
# Iterative BFS: for each digit, expand every existing combination.
# Time: O(4^N * N)  |  Space: O(4^N * N)
# ============================================================
def brute_force(digits: str) -> List[str]:
    if not digits:
        return []
    result = [""]
    for d in digits:
        letters = KEYS[int(d)]
        result = [combo + c for combo in result for c in letters]
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Recursive DFS backtracking.
# Time: O(4^N * N)  |  Space: O(N) recursion stack
# ============================================================
def optimal(digits: str) -> List[str]:
    if not digits:
        return []
    result = []

    def backtrack(idx: int, path: List[str]) -> None:
        if idx == len(digits):
            result.append("".join(path))
            return
        for c in KEYS[int(digits[idx])]:
            path.append(c)
            backtrack(idx + 1, path)
            path.pop()

    backtrack(0, [])
    return result


# ============================================================
# APPROACH 3: BEST
# Iterative approach using Python's itertools.product — most Pythonic.
# Equivalent to BFS but expressed cleanly.
# Time: O(4^N * N)  |  Space: O(4^N * N)
# ============================================================
def best(digits: str) -> List[str]:
    if not digits:
        return []
    from itertools import product
    letter_groups = [KEYS[int(d)] for d in digits]
    return ["".join(combo) for combo in product(*letter_groups)]


if __name__ == "__main__":
    print("=== Letter Phone ===")

    for digits in ["23", "2", "9", ""]:
        print(f'\ndigits="{digits}"')
        print(f"  Brute  : {brute_force(digits)}")
        print(f"  Optimal: {optimal(digits)}")
        print(f"  Best   : {best(digits)}")
