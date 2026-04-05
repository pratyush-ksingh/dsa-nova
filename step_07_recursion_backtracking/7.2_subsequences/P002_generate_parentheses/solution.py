"""
Problem: Generate Parentheses (LeetCode #22)
Difficulty: MEDIUM | XP: 25

Given n pairs of parentheses, generate all combinations of well-formed parentheses.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Generate All & Filter)
# Time: O(2^(2n) * n)  |  Space: O(2^(2n) * n)
# ============================================================
def brute_force(n: int) -> List[str]:
    """Generate all 2^(2n) strings of '(' and ')', keep only valid ones."""
    result = []

    def is_valid(s: str) -> bool:
        balance = 0
        for ch in s:
            if ch == '(':
                balance += 1
            else:
                balance -= 1
            if balance < 0:
                return False
        return balance == 0

    def generate(current: str, length: int):
        if len(current) == length:
            if is_valid(current):
                result.append(current)
            return
        generate(current + '(', length)
        generate(current + ')', length)

    generate('', 2 * n)
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Backtracking with Open/Close Counts)
# Time: O(4^n / sqrt(n))  |  Space: O(n) recursion depth
# ============================================================
def optimal(n: int) -> List[str]:
    """Backtrack: only add '(' if open < n, ')' if close < open."""
    result = []

    def backtrack(current: List[str], open_count: int, close_count: int):
        if len(current) == 2 * n:
            result.append(''.join(current))
            return
        if open_count < n:
            current.append('(')
            backtrack(current, open_count + 1, close_count)
            current.pop()
        if close_count < open_count:
            current.append(')')
            backtrack(current, open_count, close_count + 1)
            current.pop()

    backtrack([], 0, 0)
    return result


# ============================================================
# APPROACH 3: BEST (Same Backtracking -- String Concat Variant)
# Time: O(4^n / sqrt(n))  |  Space: O(n) recursion depth
# ============================================================
def best(n: int) -> List[str]:
    """Same backtracking with immutable string passing (cleaner code)."""
    result = []

    def backtrack(s: str, open_count: int, close_count: int):
        if len(s) == 2 * n:
            result.append(s)
            return
        if open_count < n:
            backtrack(s + '(', open_count + 1, close_count)
        if close_count < open_count:
            backtrack(s + ')', open_count, close_count + 1)

    backtrack('', 0, 0)
    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Generate Parentheses ===\n")

    test_cases = [
        (1, ["()"]),
        (2, ["(())", "()()"]),
        (3, ["((()))", "(()())", "(())()", "()(())", "()()()"]),
    ]

    for n, expected in test_cases:
        b = brute_force(n)
        o = optimal(n)
        r = best(n)
        status = "PASS" if sorted(o) == sorted(expected) and sorted(r) == sorted(expected) else "FAIL"
        print(f"n = {n}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  Expected: {expected}  [{status}]\n")
