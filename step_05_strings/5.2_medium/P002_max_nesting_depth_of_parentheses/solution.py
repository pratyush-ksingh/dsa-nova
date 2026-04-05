"""
Problem: Max Nesting Depth of Parentheses (LeetCode #1614)
Difficulty: EASY | XP: 10
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Explicit Stack
# Time: O(n)  |  Space: O(n)
#
# Push '(' onto a stack, pop on ')'. Track maximum stack size.
# ============================================================
def brute_force(s: str) -> int:
    stack = []
    max_depth = 0

    for c in s:
        if c == '(':
            stack.append(c)
            max_depth = max(max_depth, len(stack))
        elif c == ')':
            stack.pop()

    return max_depth


# ============================================================
# APPROACH 2: OPTIMAL -- Counter-Based
# Time: O(n)  |  Space: O(1)
#
# Replace the stack with a single integer depth counter.
# Increment on '(', decrement on ')'. Track running max.
# ============================================================
def optimal(s: str) -> int:
    depth = 0
    max_depth = 0

    for c in s:
        if c == '(':
            depth += 1
            max_depth = max(max_depth, depth)
        elif c == ')':
            depth -= 1

    return max_depth


# ============================================================
# APPROACH 3: BEST -- Same as Optimal (provably optimal)
# Time: O(n)  |  Space: O(1)
#
# Cannot improve beyond O(n) time or O(1) space.
# Compact variant using conditional expressions.
# ============================================================
def best(s: str) -> int:
    depth = max_depth = 0
    for c in s:
        if c == '(':
            depth += 1
            if depth > max_depth:
                max_depth = depth
        elif c == ')':
            depth -= 1
    return max_depth


if __name__ == "__main__":
    print("=== Max Nesting Depth of Parentheses ===\n")

    tests = [
        ("(1+(2*3)+((8)/4))+1", 3),
        ("(1)+((2))+(((3)))", 3),
        ("1+(2*3)/(2-1)", 1),
        ("1", 0),
    ]

    for s, expected in tests:
        print(f"Input:    \"{s}\"")
        print(f"Expected: {expected}")
        print(f"Brute:    {brute_force(s)}")
        print(f"Optimal:  {optimal(s)}")
        print(f"Best:     {best(s)}")
        print()
