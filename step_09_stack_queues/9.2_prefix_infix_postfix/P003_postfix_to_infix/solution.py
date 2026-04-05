"""
Problem: Postfix to Infix Conversion
Difficulty: MEDIUM | XP: 25

Convert a postfix expression to its equivalent infix expression.
E.g., "ab+c*" -> "(a+b)*c"
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE (Build Expression Tree, Inorder Traverse)
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(postfix: str) -> str:
    """Build an expression tree from postfix, then do inorder traversal."""

    class Node:
        def __init__(self, val, left=None, right=None):
            self.val = val
            self.left = left
            self.right = right

    operators = set('+-*/^')
    stack = []

    for ch in postfix:
        if ch not in operators:
            stack.append(Node(ch))
        else:
            right = stack.pop()
            left = stack.pop()
            stack.append(Node(ch, left, right))

    def inorder(node: Node) -> str:
        if node is None:
            return ""
        if node.left is None and node.right is None:
            return node.val
        left_expr = inorder(node.left)
        right_expr = inorder(node.right)
        return "(" + left_expr + node.val + right_expr + ")"

    return inorder(stack[0]) if stack else ""


# ============================================================
# APPROACH 2: OPTIMAL (Stack-Based Direct Conversion)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(postfix: str) -> str:
    """Use a stack: push operands, on operator pop two and combine with parens."""
    operators = set('+-*/^')
    stack = []

    for ch in postfix:
        if ch not in operators:
            stack.append(ch)
        else:
            op2 = stack.pop()
            op1 = stack.pop()
            expr = "(" + op1 + ch + op2 + ")"
            stack.append(expr)

    return stack[0] if stack else ""


# ============================================================
# APPROACH 3: BEST (Stack-Based with Minimal Parentheses)
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(postfix: str) -> str:
    """Same stack approach but avoids redundant outer parentheses."""
    operators = set('+-*/^')
    precedence = {'+': 1, '-': 1, '*': 2, '/': 2, '^': 3}
    stack = []  # stores (expression_string, operator_at_root_or_None)

    for ch in postfix:
        if ch not in operators:
            stack.append((ch, None))  # operand has no root operator
        else:
            right_expr, right_op = stack.pop()
            left_expr, left_op = stack.pop()

            # Add parens to left operand if its root op has lower precedence
            if left_op is not None and precedence.get(left_op, 0) < precedence.get(ch, 0):
                left_expr = "(" + left_expr + ")"

            # Add parens to right operand if its root op has lower or equal precedence
            # (equal for left-associative ops to preserve correct evaluation)
            if right_op is not None and precedence.get(right_op, 0) <= precedence.get(ch, 0):
                # But for right-associative ^ or same precedence with *, no extra parens needed
                if precedence.get(right_op, 0) < precedence.get(ch, 0):
                    right_expr = "(" + right_expr + ")"

            expr = left_expr + ch + right_expr
            stack.append((expr, ch))

    return stack[0][0] if stack else ""


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Postfix to Infix ===\n")

    test_cases = [
        ("ab+", "(a+b)"),
        ("ab+c*", "((a+b)*c)"),
        ("abc*+", "(a+(b*c))"),
        ("ab+cd+*", "((a+b)*(c+d))"),
        ("abcd^e-fgh*+^*+i-",  None),  # complex expression
    ]

    for postfix, expected in test_cases:
        b = brute_force(postfix)
        o = optimal(postfix)
        r = best(postfix)
        if expected:
            status = "PASS" if o == expected else "FAIL"
        else:
            status = "CHECK"
        print(f"Postfix: {postfix}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        if expected:
            print(f"  Expected: {expected}  [{status}]")
        print()
