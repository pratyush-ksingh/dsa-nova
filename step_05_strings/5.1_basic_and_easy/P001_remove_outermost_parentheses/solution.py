"""
Problem: Remove Outermost Parentheses (LeetCode #1021)
Difficulty: EASY | XP: 10
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Stack-Based Primitive Extraction
# Time: O(n)  |  Space: O(n)
#
# Use a stack to locate each primitive, then slice off the
# first and last character of each primitive substring.
# ============================================================
def brute_force(s: str) -> str:
    stack = []
    result = []
    start = 0

    for i, c in enumerate(s):
        if c == '(':
            stack.append(i)
        else:
            stack.pop()
            if not stack:
                # Found a complete primitive from start to i (inclusive)
                # Strip the outermost parentheses
                result.append(s[start + 1 : i])
                start = i + 1

    return "".join(result)


# ============================================================
# APPROACH 2: OPTIMAL -- Depth Counter with Conditional Append
# Time: O(n)  |  Space: O(1) extra
#
# Track depth with an integer. The outermost '(' raises depth
# from 0->1 and the outermost ')' drops it from 1->0. Skip
# those characters; append everything else.
# ============================================================
def optimal(s: str) -> str:
    result = []
    depth = 0

    for c in s:
        if c == '(':
            depth += 1
            if depth > 1:
                result.append(c)
        else:
            if depth > 1:
                result.append(c)
            depth -= 1

    return "".join(result)


# ============================================================
# APPROACH 3: BEST -- Same as Optimal (provably optimal)
# Time: O(n)  |  Space: O(1) extra
#
# O(n) time is the lower bound (must read every character).
# O(1) extra space is minimal. Compact single-pass variant.
# ============================================================
def best(s: str) -> str:
    result = []
    depth = 0

    for c in s:
        if c == '(':
            depth += 1
            if depth > 1:
                result.append(c)
        else:
            depth -= 1
            if depth > 0:
                result.append(c)

    return "".join(result)


if __name__ == "__main__":
    print("=== Remove Outermost Parentheses ===\n")

    tests = [
        ("(()())(())", "()()()"),
        ("(()())(())(()(()))", "()()()()(())"),
        ("()()", ""),
    ]

    for s, expected in tests:
        print(f"Input:    \"{s}\"")
        print(f"Expected: \"{expected}\"")
        print(f"Brute:    \"{brute_force(s)}\"")
        print(f"Optimal:  \"{optimal(s)}\"")
        print(f"Best:     \"{best(s)}\"")
        print()
