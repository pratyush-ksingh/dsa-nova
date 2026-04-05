"""
Problem: Redundant Braces
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a valid expression with operators +, -, *, / and parentheses,
determine if the expression contains redundant braces.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Repeated Inner-Pair Check
# Time: O(n^2)  |  Space: O(n)
# Find innermost parens, check if they contain an operator.
# ============================================================
def brute_force(s: str) -> bool:
    operators = {'+', '-', '*', '/'}
    s = list(s)  # Mutable for deletions

    while True:
        # Find the innermost closing paren
        close_idx = -1
        for i, c in enumerate(s):
            if c == ')':
                close_idx = i
                break
        if close_idx == -1:
            break  # No more parentheses

        # Find its matching opening paren (scan backwards)
        open_idx = -1
        for i in range(close_idx - 1, -1, -1):
            if s[i] == '(':
                open_idx = i
                break

        # Check content between open_idx and close_idx
        has_op = any(s[i] in operators for i in range(open_idx + 1, close_idx))

        if not has_op:
            return True  # Redundant!

        # Remove this pair of parentheses and continue
        s.pop(close_idx)
        s.pop(open_idx)

    return False


# ============================================================
# APPROACH 2: OPTIMAL -- Stack Scan
# Time: O(n)  |  Space: O(n)
# Push everything except ')'. On ')', pop until '(' and check
# if any operator was found between them.
# ============================================================
def optimal(s: str) -> bool:
    stack = []

    for c in s:
        if c == ')':
            has_operator = False

            # Pop until we find the matching '('
            while stack[-1] != '(':
                popped = stack.pop()
                if popped in {'+', '-', '*', '/'}:
                    has_operator = True

            stack.pop()  # Remove the '('

            if not has_operator:
                return True  # Redundant braces!
        else:
            stack.append(c)

    return False


# ============================================================
# APPROACH 3: BEST -- Stack with Operator Set (Clean Production Code)
# Time: O(n)  |  Space: O(n)
# Same logic with a clean operator set for extensibility.
# ============================================================
def best(s: str) -> bool:
    operators = {'+', '-', '*', '/'}
    stack = []

    for c in s:
        if c == ')':
            has_operator = False
            count = 0  # Track elements between ( and )

            while stack[-1] != '(':
                popped = stack.pop()
                if popped in operators:
                    has_operator = True
                count += 1

            stack.pop()  # Remove '('

            # Redundant if: no operator found, or nothing between parens
            if not has_operator or count == 0:
                return True
        else:
            stack.append(c)

    return False


if __name__ == "__main__":
    print("=== Redundant Braces ===\n")

    test_cases = [
        "((a+b))",      # true  -- outer braces redundant
        "(a+b)",        # false -- braces contain operator
        "(a)",          # true  -- no operator inside
        "a+(b*c)",      # false -- braces contain *
        "((a))",        # true  -- both layers redundant
        "a+b",          # false -- no braces at all
        "(a+b)*(c+d)",  # false -- both pairs have operators
    ]

    for s in test_cases:
        print(f"Input: {s:18s}  "
              f"Brute: {str(brute_force(s)):5s}  "
              f"Optimal: {str(optimal(s)):5s}  "
              f"Best: {str(best(s)):5s}")
