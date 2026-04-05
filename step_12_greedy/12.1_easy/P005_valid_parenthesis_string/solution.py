"""
Problem: Valid Parenthesis String (LeetCode 678)
Difficulty: MEDIUM | XP: 25

Given a string containing '(', ')' and '*', where '*' can be treated as
'(', ')' or empty string, determine if the string is valid.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE (Recursion / Try All Options)
# Time: O(3^m) where m = number of *s | Space: O(n) recursion stack
# ============================================================
def brute_force(s: str) -> bool:
    """Try all 3 possibilities for each '*' recursively."""
    def helper(idx: int, open_count: int) -> bool:
        if open_count < 0:
            return False
        if idx == len(s):
            return open_count == 0

        ch = s[idx]
        if ch == '(':
            return helper(idx + 1, open_count + 1)
        elif ch == ')':
            return helper(idx + 1, open_count - 1)
        else:  # '*'
            return (helper(idx + 1, open_count + 1) or  # treat as '('
                    helper(idx + 1, open_count - 1) or  # treat as ')'
                    helper(idx + 1, open_count))         # treat as empty

    return helper(0, 0)


# ============================================================
# APPROACH 2: OPTIMAL (Track Min/Max Open Count)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(s: str) -> bool:
    """
    Track the range [lo, hi] of possible open-parenthesis counts.
    - '(' increases both lo and hi by 1.
    - ')' decreases both lo and hi by 1.
    - '*' decreases lo by 1 (treat as ')') and increases hi by 1 (treat as '(').
    If hi < 0, too many ')' even if all '*' are '(': invalid.
    Keep lo >= 0 (we cannot have negative open count).
    At the end, if lo == 0, the string is valid.
    """
    lo, hi = 0, 0

    for ch in s:
        if ch == '(':
            lo += 1
            hi += 1
        elif ch == ')':
            lo -= 1
            hi -= 1
        else:  # '*'
            lo -= 1  # treat as ')'
            hi += 1  # treat as '('

        if hi < 0:
            return False
        lo = max(lo, 0)  # open count cannot go below 0

    return lo == 0


# ============================================================
# APPROACH 3: BEST (Same Two-Counter -- Cleanest Form)
# Time: O(n) | Space: O(1)
# ============================================================
def best(s: str) -> bool:
    """
    Same min/max tracking approach. This is the optimal solution.
    Alternatively expressed as two passes (left-to-right and right-to-left)
    but the single-pass min/max is more elegant.
    """
    min_open, max_open = 0, 0

    for ch in s:
        if ch == '(':
            min_open += 1
            max_open += 1
        elif ch == ')':
            min_open -= 1
            max_open -= 1
        else:
            min_open -= 1
            max_open += 1

        # If max_open < 0, no way to balance
        if max_open < 0:
            return False

        # min_open cannot go below 0
        min_open = max(min_open, 0)

    return min_open == 0


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Valid Parenthesis String ===\n")

    test_cases = [
        ("()", True),
        ("(*)", True),
        ("(*))", True),
        ("(((*)", False),
        ("", True),
        ("*", True),
        ("(((******))", True),
        ("(*()", True),
        ("((*)(*))((*", False),
        (")(", False),
        ("(*)(", False),
    ]

    for s, expected in test_cases:
        b = brute_force(s)
        o = optimal(s)
        h = best(s)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"Input: \"{s}\"")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
