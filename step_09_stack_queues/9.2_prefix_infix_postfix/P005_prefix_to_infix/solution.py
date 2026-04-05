"""
Problem: Prefix to Infix Conversion
Difficulty: MEDIUM | XP: 25

Convert a prefix expression to its equivalent fully parenthesized infix expression.
E.g., "*+AB-CD" -> "((A+B)*(C-D))"
Operands are single characters (A-Z or 0-9).
Operators: +, -, *, /, ^
"""


OPERATORS = set('+-*/^')


# ============================================================
# APPROACH 1: BRUTE FORCE -- Build Expression Tree, Inorder Traverse
# Time: O(n)  |  Space: O(n)
#
# Build an expression tree from the prefix expression by scanning
# left to right with a recursive descent approach (or stack-based).
# Then do inorder traversal: left, root, right.
# ============================================================
def brute_force(prefix: str) -> str:
    """Build expression tree from prefix (recursive descent), then inorder traverse."""

    class Node:
        def __init__(self, val: str, left=None, right=None):
            self.val = val
            self.left = left
            self.right = right

    pos = [0]  # mutable index

    def build() -> Node:
        if pos[0] >= len(prefix):
            return None
        ch = prefix[pos[0]]
        pos[0] += 1
        if ch not in OPERATORS:
            return Node(ch)
        left = build()
        right = build()
        return Node(ch, left, right)

    def inorder(node: Node) -> str:
        if node is None:
            return ""
        if node.left is None and node.right is None:
            return node.val
        left_str = inorder(node.left)
        right_str = inorder(node.right)
        return "(" + left_str + node.val + right_str + ")"

    root = build()
    return inorder(root) if root else ""


# ============================================================
# APPROACH 2: OPTIMAL -- Stack-Based Scan Right-to-Left
# Time: O(n)  |  Space: O(n)
#
# Scan the prefix string from RIGHT to LEFT:
# - Operand: push it.
# - Operator: pop op1 and op2, form "(op1 operator op2)", push.
# Final stack top is the infix expression (fully parenthesized).
# ============================================================
def optimal(prefix: str) -> str:
    """Scan right-to-left: on operator pop two sub-expressions and wrap with parens."""
    stack = []
    for i in range(len(prefix) - 1, -1, -1):
        ch = prefix[i]
        if ch not in OPERATORS:
            stack.append(ch)
        else:
            op1 = stack.pop()
            op2 = stack.pop()
            infix_expr = "(" + op1 + ch + op2 + ")"
            stack.append(infix_expr)
    return stack[0] if stack else ""


# ============================================================
# APPROACH 3: BEST -- Same Stack, Minimal Allocations
# Time: O(n)  |  Space: O(n)
#
# The right-to-left stack approach is already optimal. This variant
# makes it explicit using a deque for clarity and uses join-based
# string construction to show idiomatic Python.
# ============================================================
def best(prefix: str) -> str:
    """Right-to-left stack scan with deque and explicit string joining."""
    from collections import deque
    stack = deque()

    for ch in reversed(prefix):
        if ch not in OPERATORS:
            stack.append(ch)
        else:
            op1 = stack.pop()
            op2 = stack.pop()
            stack.append("(" + op1 + ch + op2 + ")")

    return stack[-1] if stack else ""


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Prefix to Infix ===\n")

    tests = [
        ("+AB",        "(A+B)"),
        ("*+AB-CD",    "((A+B)*(C-D))"),
        ("+A*BC",      "(A+(B*C))"),
        ("*+ABC",      "((A+B)*C)"),
        ("-+A*BCD",    "((A+(B*C))-D)"),
        ("A",          "A"),
    ]

    for prefix, expected in tests:
        b = brute_force(prefix)
        o = optimal(prefix)
        r = best(prefix)
        status = "PASS" if b == o == r == expected else "FAIL"
        print(f"Prefix: {prefix:<12s}  Expected: {expected:<20s}  "
              f"Brute: {b:<20s}  Optimal: {o:<20s}  [{status}]")
