"""
Problem: Infix to Prefix
Difficulty: MEDIUM | XP: 25

Convert an infix expression to prefix (Polish notation).
Supports +, -, *, /, ^ operators and single-char operands.
"""
from typing import List


def precedence(op: str) -> int:
    return {'+': 1, '-': 1, '*': 2, '/': 2, '^': 3}.get(op, 0)


def is_operand(c: str) -> bool:
    return c.isalnum()


def is_operator(c: str) -> bool:
    return c in '+-*/^'


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive Descent Parser
# Time: O(n) amortized  |  Space: O(n) call stack
#
# Parse the expression using recursive descent with operator
# precedence climbing. Naturally produces prefix output by
# placing the operator before its operands.
# ============================================================
def brute_force(infix: str) -> str:
    pos = [0]

    def parse_expression(min_prec: int) -> str:
        left = parse_primary()

        while pos[0] < len(infix):
            op = infix[pos[0]]
            if not is_operator(op) or precedence(op) < min_prec:
                break

            pos[0] += 1  # consume operator
            # For right-assoc ^, use same precedence; for left-assoc, use prec+1
            next_min = precedence(op) if op == '^' else precedence(op) + 1
            right = parse_expression(next_min)
            left = op + left + right  # prefix: operator before operands

        return left

    def parse_primary() -> str:
        if pos[0] < len(infix) and infix[pos[0]] == '(':
            pos[0] += 1  # skip '('
            result = parse_expression(0)
            pos[0] += 1  # skip ')'
            return result
        c = infix[pos[0]]
        pos[0] += 1
        return c

    return parse_expression(0)


# ============================================================
# APPROACH 2: OPTIMAL -- Reverse + Infix-to-Postfix + Reverse
# Time: O(n)  |  Space: O(n)
#
# Classic trick:
# 1. Reverse the infix string
# 2. Swap ( <-> )
# 3. Apply Shunting Yard with modified associativity rules
# 4. Reverse the result to get prefix
# ============================================================
def optimal(infix: str) -> str:
    # Step 1 & 2: Reverse and swap parentheses
    reversed_infix = []
    for c in reversed(infix):
        if c == '(':
            reversed_infix.append(')')
        elif c == ')':
            reversed_infix.append('(')
        else:
            reversed_infix.append(c)

    # Step 3: Shunting Yard (modified for prefix)
    output = []
    stack = []

    for c in reversed_infix:
        if is_operand(c):
            output.append(c)
        elif c == '(':
            stack.append(c)
        elif c == ')':
            while stack and stack[-1] != '(':
                output.append(stack.pop())
            if stack:
                stack.pop()  # remove '('
        else:
            # Operator: pop only strictly higher precedence
            # (NOT equal -- key difference from standard postfix)
            while (stack and stack[-1] != '(' and
                   precedence(stack[-1]) > precedence(c)):
                output.append(stack.pop())
            stack.append(c)

    while stack:
        output.append(stack.pop())

    # Step 4: Reverse to get prefix
    return ''.join(reversed(output))


# ============================================================
# APPROACH 3: BEST -- Direct Right-to-Left Scan
# Time: O(n)  |  Space: O(n)
#
# Scan infix right-to-left, build prefix directly.
# ')' acts as open bracket, '(' acts as close bracket.
# No explicit string reversals needed (except final result).
# ============================================================
def best(infix: str) -> str:
    stack = []
    result = []

    for i in range(len(infix) - 1, -1, -1):
        c = infix[i]

        if is_operand(c):
            result.append(c)
        elif c == ')':
            stack.append(c)
        elif c == '(':
            while stack and stack[-1] != ')':
                result.append(stack.pop())
            if stack:
                stack.pop()  # remove ')'
        else:
            # Operator
            # Left-assoc: pop >= precedence (they bind tighter from left)
            # Right-assoc ^: pop only strictly > precedence
            while (stack and stack[-1] != ')' and
                   _should_pop_rtl(stack[-1], c)):
                result.append(stack.pop())
            stack.append(c)

    while stack:
        result.append(stack.pop())

    # We built prefix in reverse order
    return ''.join(reversed(result))


def _should_pop_rtl(stack_top: str, incoming: str) -> bool:
    if incoming == '^':
        # Right-associative: do NOT pop same precedence
        return precedence(stack_top) > precedence(incoming)
    # Left-associative: pop same or higher precedence
    return precedence(stack_top) >= precedence(incoming)


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Infix to Prefix ===\n")

    tests = [
        ("A+B",         "+AB"),
        ("A+B*C",       "+A*BC"),
        ("(A+B)*C",     "*+ABC"),
        ("A+B*C-D",     "-+A*BCD"),
        ("A^B^C",       "^A^BC"),
        ("(A+B)*(C-D)", "*+AB-CD"),
        ("A",           "A"),
    ]

    for infix, expected in tests:
        b = brute_force(infix)
        o = optimal(infix)
        be = best(infix)
        print(f"Infix: {infix:<20s} Expected: {expected}")
        print(f"  Brute:   {b:<10s} {'PASS' if b == expected else 'FAIL'}")
        print(f"  Optimal: {o:<10s} {'PASS' if o == expected else 'FAIL'}")
        print(f"  Best:    {be:<10s} {'PASS' if be == expected else 'FAIL'}")
        print()
