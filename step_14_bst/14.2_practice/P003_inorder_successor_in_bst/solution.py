"""
Problem: Inorder Successor in BST
Difficulty: MEDIUM | XP: 25
"""
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Full Inorder Traversal
# Time: O(n)  |  Space: O(n) for storing traversal
# Do complete inorder, find target, return next element.
# ============================================================
def brute_force(root: Optional[TreeNode], target: TreeNode) -> Optional[TreeNode]:
    if root is None or target is None:
        return None

    inorder = []

    def traverse(node):
        if node is None:
            return
        traverse(node.left)
        inorder.append(node)
        traverse(node.right)

    traverse(root)

    for i in range(len(inorder)):
        if inorder[i] is target and i + 1 < len(inorder):
            return inorder[i + 1]

    return None


# ============================================================
# APPROACH 2: OPTIMAL -- BST Property Traversal
# Time: O(h)  |  Space: O(1)
# If target has right subtree, successor is leftmost in right.
# Otherwise, walk from root tracking the last node where we
# went left (ancestor that is greater than target).
# ============================================================
def optimal(root: Optional[TreeNode], target: TreeNode) -> Optional[TreeNode]:
    if root is None or target is None:
        return None

    # Case 1: target has right subtree
    if target.right:
        node = target.right
        while node.left:
            node = node.left
        return node

    # Case 2: walk from root, track last left-turn ancestor
    successor = None
    curr = root
    while curr:
        if target.val < curr.val:
            successor = curr  # This could be the successor
            curr = curr.left
        elif target.val > curr.val:
            curr = curr.right
        else:
            break  # Found target, successor already tracked

    return successor


# ============================================================
# APPROACH 3: BEST -- BST Search (clean single-pass)
# Time: O(h)  |  Space: O(1)
# Same BST approach, even cleaner: just search for smallest
# value greater than target.val by walking the BST.
# ============================================================
def best(root: Optional[TreeNode], target: TreeNode) -> Optional[TreeNode]:
    if root is None or target is None:
        return None

    successor = None
    curr = root

    while curr:
        if curr.val > target.val:
            successor = curr  # Candidate: this is greater
            curr = curr.left  # Try to find smaller candidate
        else:
            curr = curr.right  # Need to find something greater

    return successor


if __name__ == "__main__":
    print("=== Inorder Successor in BST ===")

    #       20
    #      /  \
    #     8    22
    #    / \
    #   4  12
    #     /  \
    #    10  14
    n4 = TreeNode(4)
    n10 = TreeNode(10)
    n14 = TreeNode(14)
    n12 = TreeNode(12, n10, n14)
    n8 = TreeNode(8, n4, n12)
    n22 = TreeNode(22)
    root = TreeNode(20, n8, n22)

    # Successor of 8 -> 10
    result = brute_force(root, n8)
    print(f"Brute (8):   {result.val if result else None}")   # 10

    result = optimal(root, n8)
    print(f"Optimal (8): {result.val if result else None}")   # 10

    result = best(root, n8)
    print(f"Best (8):    {result.val if result else None}")   # 10

    # Successor of 14 -> 20
    result = optimal(root, n14)
    print(f"Optimal (14): {result.val if result else None}")  # 20

    # Successor of 22 -> None (largest)
    result = optimal(root, n22)
    print(f"Optimal (22): {result.val if result else None}")  # None

    # Successor of 10 -> 12
    result = best(root, n10)
    print(f"Best (10):   {result.val if result else None}")   # 12
