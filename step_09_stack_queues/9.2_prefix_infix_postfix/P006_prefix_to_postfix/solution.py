"""
Problem: Prefix to Postfix
Difficulty: MEDIUM | XP: 25
"""
from typing import List


OPERATORS = set("+-*/^")


def _is_operator(ch: str) -> bool:
    return ch in OPERATORS


# ============================================================
# APPROACH 1: BRUTE FORCE — Build expression tree, postorder
# Time: O(n)  |  Space: O(n)
# ============================================================
class _TreeNode:
    def __init__(self, val: str):
        self.val = val
        self.left: "_TreeNode | None" = None
        self.right: "_TreeNode | None" = None


def _build_tree(prefix: str, index: List[int]) -> _TreeNode:
    """
    Parse prefix expression recursively, building a binary tree.
    `index` is a mutable cursor into the string.
    """
    node = _TreeNode(prefix[index[0]])
    index[0] += 1
    if _is_operator(node.val):
        node.left = _build_tree(prefix, index)
        node.right = _build_tree(prefix, index)
    return node


def _postorder(node: "_TreeNode | None", out: List[str]) -> None:
    if node is None:
        return
    _postorder(node.left, out)
    _postorder(node.right, out)
    out.append(node.val)


def brute_force(prefix: str) -> str:
    """
    Build an expression tree from prefix notation, then
    collect a postorder traversal to obtain postfix.
    """
    root = _build_tree(prefix, [0])
    out: List[str] = []
    _postorder(root, out)
    return "".join(out)


# ============================================================
# APPROACH 2: OPTIMAL — Stack-based right-to-left scan
# Time: O(n)  |  Space: O(n)
# Scan the prefix string from RIGHT to LEFT.
# If operand: push.
# If operator: pop two operands op1, op2, push op1 + op2 + op
# ============================================================
def optimal(prefix: str) -> str:
    """
    Single right-to-left pass with a stack.
    Each stack frame holds a postfix sub-expression string.
    """
    stack: List[str] = []
    for ch in reversed(prefix):
        if _is_operator(ch):
            op1 = stack.pop()
            op2 = stack.pop()
            stack.append(op1 + op2 + ch)
        else:
            stack.append(ch)
    return stack[-1]


# ============================================================
# APPROACH 3: BEST — Same stack approach, multi-char tokens
# Time: O(n)  |  Space: O(n)
# Handles multi-character operands (e.g. "AB", "12") by
# splitting on whitespace first, then applying the same logic.
# ============================================================
def best(prefix: str) -> str:
    """
    Handles both single-char tokens and whitespace-separated
    multi-character operands/operators.
    """
    tokens = prefix.split() if " " in prefix else list(prefix)
    stack: List[str] = []
    for token in reversed(tokens):
        if _is_operator(token) and len(token) == 1:
            op1 = stack.pop()
            op2 = stack.pop()
            stack.append(op1 + op2 + token)
        else:
            stack.append(token)
    return stack[-1]


if __name__ == "__main__":
    print("=== Prefix to Postfix ===")
    tests = [
        ("*+AB-CD", "AB+CD-*"),
        ("*-A/BC-/AKL", "ABC/-AK/L--*"),
        ("+AB", "AB+"),
        ("*+PQ-RS", "PQ+RS-*"),
    ]
    for prefix, expected in tests:
        b = brute_force(prefix)
        o = optimal(prefix)
        bst = best(prefix)
        status = "OK" if o == expected else f"EXPECTED {expected}"
        print(f"Prefix: {prefix:20s} -> Optimal: {o:15s} ({status})")
        print(f"  Brute={b}  Best={bst}")
