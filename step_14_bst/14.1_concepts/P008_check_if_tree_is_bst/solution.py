"""Problem: Check if Tree is BST
Difficulty: MEDIUM | XP: 25

Validate whether a binary tree satisfies the BST property:
all left descendants < node < all right descendants.
"""
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Inorder traversal, check strictly increasing
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> bool:
    vals = []

    def inorder(node):
        if not node:
            return
        inorder(node.left)
        vals.append(node.val)
        inorder(node.right)

    inorder(root)
    return all(vals[i] < vals[i + 1] for i in range(len(vals) - 1))


# ============================================================
# APPROACH 2: OPTIMAL - Min/Max range validation (top-down)
# Time: O(N)  |  Space: O(H)
# Pass valid range down: left child must be < node, right must be > node
# ============================================================
def optimal(root: Optional[TreeNode]) -> bool:
    def validate(node, lo, hi):
        if not node:
            return True
        if not (lo < node.val < hi):
            return False
        return validate(node.left, lo, node.val) and validate(node.right, node.val, hi)

    return validate(root, float('-inf'), float('inf'))


# ============================================================
# APPROACH 3: BEST - Iterative inorder with prev tracking
# Time: O(N)  |  Space: O(H)
# No extra list; just track previous value seen in inorder
# ============================================================
def best(root: Optional[TreeNode]) -> bool:
    stack = []
    cur = root
    prev = float('-inf')
    while cur or stack:
        while cur:
            stack.append(cur)
            cur = cur.left
        cur = stack.pop()
        if cur.val <= prev:
            return False
        prev = cur.val
        cur = cur.right
    return True


def make_tree(root_val, left=None, right=None):
    node = TreeNode(root_val)
    node.left = left
    node.right = right
    return node


if __name__ == "__main__":
    # Valid: 5 -> 3,7 -> 1,4
    valid = make_tree(5,
                      make_tree(3, TreeNode(1), TreeNode(4)),
                      TreeNode(7))
    # Invalid: 5 -> 3,7 -> 1,6  (6 > 5, wrong)
    invalid = make_tree(5,
                        make_tree(3, TreeNode(1), TreeNode(6)),
                        TreeNode(7))
    # Tricky invalid: [5,4,6,None,None,3,7]  (3 < 5, wrong for right subtree)
    tricky = make_tree(5,
                       TreeNode(4),
                       make_tree(6, TreeNode(3), TreeNode(7)))

    tests = [
        (valid, True, "valid BST"),
        (invalid, False, "invalid (6 in left subtree > root)"),
        (tricky, False, "tricky (3 in right subtree < root)"),
        (TreeNode(1), True, "single node"),
    ]
    for tree, expected, desc in tests:
        bf = brute_force(tree)
        opt = optimal(tree)
        be = best(tree)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] {desc}: Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
