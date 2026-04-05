"""
Problem: Valid Parentheses
Difficulty: EASY | XP: 10

Given a string containing only '(', ')', '{', '}', '[', ']',
determine if the input string is valid.
LeetCode #20 - Valid Parentheses
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Repeated Removal
# Time: O(n^2)  |  Space: O(n)
# Repeatedly remove "()", "[]", "{}" until no more can be removed.
# ============================================================
def brute_force(s: str) -> bool:
    prev = ""
    while s != prev:
        prev = s
        s = s.replace("()", "")
        s = s.replace("[]", "")
        s = s.replace("{}", "")
    return len(s) == 0


# ============================================================
# APPROACH 2: OPTIMAL -- Stack-Based Matching
# Time: O(n)  |  Space: O(n)
# Push openers, pop on closers, verify match.
# ============================================================
def optimal(s: str) -> bool:
    stack = []
    matching = {'(': ')', '[': ']', '{': '}'}

    for c in s:
        if c in matching:
            stack.append(c)
        else:
            # It's a closer
            if not stack:
                return False
            top = stack.pop()
            if matching[top] != c:
                return False

    return len(stack) == 0  # All openers must be matched


# ============================================================
# APPROACH 3: BEST -- Stack with Map Lookup (Production-Ready)
# Time: O(n)  |  Space: O(n)
# Map each closer to its expected opener for cleaner code.
# ============================================================
def best(s: str) -> bool:
    # Early exit: odd-length strings can never be valid
    if len(s) % 2 != 0:
        return False

    # Map each closer to its expected opener
    close_to_open = {')': '(', ']': '[', '}': '{'}
    stack = []

    for c in s:
        if c in close_to_open:
            # It's a closer -- check for matching opener
            if not stack or stack.pop() != close_to_open[c]:
                return False
        else:
            # It's an opener -- push to stack
            stack.append(c)

    return len(stack) == 0


if __name__ == "__main__":
    print("=== Valid Parentheses ===\n")

    test_cases = ["()", "()[]{}", "(]", "([)]", "{[]}", "", "(", "((([[]])))" ]

    for s in test_cases:
        display = s if s else "(empty)"
        print(f"Input: {display:15s}  "
              f"Brute: {str(brute_force(s)):5s}  "
              f"Optimal: {str(optimal(s)):5s}  "
              f"Best: {str(best(s)):5s}")
