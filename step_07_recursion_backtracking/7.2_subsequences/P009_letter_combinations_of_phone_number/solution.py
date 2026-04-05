"""
Problem: Letter Combinations of Phone Number
Difficulty: MEDIUM | XP: 25

Given a string of digits 2-9, return all possible letter combinations
from the phone keypad (T9 mapping).
"""
from typing import List
from collections import deque
from itertools import product

PHONE = {
    '2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl',
    '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'
}


# ============================================================
# APPROACH 1: BRUTE FORCE - Iterative, expand layer by layer
# Time: O(4^n * n)  |  Space: O(4^n * n)
# For each digit, append each mapped letter to all existing prefixes
# ============================================================
def brute_force(digits: str) -> List[str]:
    if not digits:
        return []
    result = ['']
    for d in digits:
        result = [prefix + c for prefix in result for c in PHONE[d]]
    return result


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking DFS
# Time: O(4^n * n)  |  Space: O(n) recursion depth (excluding output)
# Build combinations character-by-character, backtrack on each choice
# ============================================================
def optimal(digits: str) -> List[str]:
    if not digits:
        return []
    res = []

    def backtrack(idx: int, path: List[str]) -> None:
        if idx == len(digits):
            res.append(''.join(path))
            return
        for c in PHONE[digits[idx]]:
            path.append(c)
            backtrack(idx + 1, path)
            path.pop()

    backtrack(0, [])
    return res


# ============================================================
# APPROACH 3: BEST - itertools.product (most Pythonic)
# Time: O(4^n * n)  |  Space: O(4^n * n)
# Cartesian product of all mapped letter groups
# ============================================================
def best(digits: str) -> List[str]:
    if not digits:
        return []
    groups = [PHONE[d] for d in digits]
    return [''.join(combo) for combo in product(*groups)]


if __name__ == "__main__":
    print("=== Letter Combinations of Phone Number ===")

    digits = "23"
    print(f'digits="{digits}"')
    print(f"Brute:   {brute_force(digits)}")
    print(f"Optimal: {optimal(digits)}")
    print(f"Best:    {best(digits)}")

    digits = ""
    print(f'\ndigits="" (empty)')
    print(f"Brute:   {brute_force(digits)}")
    print(f"Optimal: {optimal(digits)}")
    print(f"Best:    {best(digits)}")

    digits = "79"
    print(f'\ndigits="{digits}" (pqrs x wxyz)')
    print(f"Brute:   {brute_force(digits)}")
    print(f"Optimal: {optimal(digits)}")
    print(f"Best:    {best(digits)}")
