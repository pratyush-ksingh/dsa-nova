"""
Problem: Infix to Postfix
Difficulty: MEDIUM | XP: 25

Convert an infix expression to postfix (Reverse Polish Notation)
using Dijkstra's Shunting Yard algorithm.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Simple Shunting Yard (basic operators)
# Time: O(n)  |  Space: O(n)
# Handles +, -, *, / with left-associativity only.
# ============================================================
def brute_force(s: str) -> str:
    def precedence(op: str) -> int:
        if op in ('+', '-'):
            return 1
        if op in ('*', '/'):
            return 2
        return 0

    output = []
    stack = []

    for c in s:
        if c.isalnum():
            output.append(c)
        elif c == '(':
            stack.append(c)
        elif c == ')':
            while stack and stack[-1] != '(':
                output.append(stack.pop())
            if stack:
                stack.pop()  # Remove '('
        else:
            while (stack and stack[-1] != '('
                   and precedence(stack[-1]) >= precedence(c)):
                output.append(stack.pop())
            stack.append(c)

    while stack:
        output.append(stack.pop())

    return ''.join(output)


# ============================================================
# APPROACH 2: OPTIMAL -- Shunting Yard Algorithm
# Time: O(n)  |  Space: O(n)
# Operands to output. Operators on stack with precedence-based popping.
# Parentheses act as barriers.
# ============================================================
def optimal(s: str) -> str:
    def precedence(op: str) -> int:
        if op in ('+', '-'):
            return 1
        if op in ('*', '/'):
            return 2
        return 0

    output = []
    stack = []

    for c in s:
        if c.isalnum():
            # Operands go directly to output
            output.append(c)

        elif c == '(':
            # Left paren is a barrier -- push it
            stack.append(c)

        elif c == ')':
            # Pop everything until matching '('
            while stack and stack[-1] != '(':
                output.append(stack.pop())
            if stack:
                stack.pop()  # Discard '('

        else:
            # Operator: pop stack while top has >= precedence (left-assoc)
            while (stack and stack[-1] != '('
                   and precedence(stack[-1]) >= precedence(c)):
                output.append(stack.pop())
            stack.append(c)

    # Pop all remaining operators
    while stack:
        output.append(stack.pop())

    return ''.join(output)


# ============================================================
# APPROACH 3: BEST -- Shunting Yard with Full Operator Support
# Time: O(n)  |  Space: O(n)
# Handles right-associative operators (^), configurable precedence.
# ============================================================
def best(s: str) -> str:
    precedence_map = {'+': 1, '-': 1, '*': 2, '/': 2, '^': 3}
    associativity = {'+': 'L', '-': 'L', '*': 'L', '/': 'L', '^': 'R'}

    def should_pop(stack_top: str, current: str) -> bool:
        """
        Determine if stack top operator should be popped before pushing current.
        Left-associative: pop when stackTop >= current
        Right-associative: pop when stackTop > current (NOT equal)
        """
        top_prec = precedence_map.get(stack_top, 0)
        cur_prec = precedence_map.get(current, 0)

        if associativity.get(current, 'L') == 'L':
            return top_prec >= cur_prec
        else:
            return top_prec > cur_prec  # Right-associative: strict greater

    output = []
    stack = []

    for c in s:
        if c.isalnum():
            output.append(c)

        elif c == '(':
            stack.append(c)

        elif c == ')':
            while stack and stack[-1] != '(':
                output.append(stack.pop())
            if stack:
                stack.pop()

        elif c in precedence_map:
            # Operator with associativity-aware popping
            while (stack and stack[-1] != '('
                   and stack[-1] in precedence_map
                   and should_pop(stack[-1], c)):
                output.append(stack.pop())
            stack.append(c)

    while stack:
        output.append(stack.pop())

    return ''.join(output)


if __name__ == "__main__":
    print("=== Infix to Postfix ===\n")

    test_cases = [
        ("a+b",         "ab+"),
        ("a+b*c",       "abc*+"),
        ("(a+b)*c",     "ab+c*"),
        ("a+b+c",       "ab+c+"),
        ("a*(b+c*d)+e", "abcd*+*e+"),
        ("((a+b))",     "ab+"),
        ("a^b^c",       "abc^^"),       # Right-associative
        ("a+b*c-d/e",   "abc*+de/-"),
    ]

    for expr, expected in test_cases:
        result = best(expr)
        status = "PASS" if result == expected else "FAIL"
        print(f"[{status}] Input: {expr:18s}  Expected: {expected:10s}  Got: {result:10s}")

    # Show all three approaches for a standard case
    print("\n--- Comparison (a+b*c) ---")
    print(f"  Brute:   {brute_force('a+b*c')}")
    print(f"  Optimal: {optimal('a+b*c')}")
    print(f"  Best:    {best('a+b*c')}")
