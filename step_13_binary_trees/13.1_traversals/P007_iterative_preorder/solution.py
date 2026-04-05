"""
Problem: Iterative Preorder Traversal
Difficulty: MEDIUM | XP: 25
"""
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive Preorder
# Time: O(n)  |  Space: O(h) recursion stack
# Standard recursive approach as baseline.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    result = []

    def recurse(node):
        if node is None:
            return
        result.append(node.val)   # Root
        recurse(node.left)        # Left
        recurse(node.right)       # Right

    recurse(root)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Iterative with Explicit Stack
# Time: O(n)  |  Space: O(h)
# Push right first, then left. Pop = process immediately.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    if root is None:
        return []

    result = []
    stack = [root]

    while stack:
        node = stack.pop()
        result.append(node.val)

        # Push right first so left is processed first (LIFO)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)

    return result


# ============================================================
# APPROACH 3: BEST -- Morris Preorder Traversal
# Time: O(n)  |  Space: O(1)
# Thread the tree temporarily to eliminate stack usage.
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    result = []
    curr = root

    while curr:
        if curr.left is None:
            # No left child: process and go right
            result.append(curr.val)
            curr = curr.right
        else:
            # Find inorder predecessor (rightmost in left subtree)
            predecessor = curr.left
            while predecessor.right and predecessor.right is not curr:
                predecessor = predecessor.right

            if predecessor.right is None:
                # First visit: create thread, process node, go left
                predecessor.right = curr
                result.append(curr.val)  # Preorder: add on FIRST visit
                curr = curr.left
            else:
                # Second visit: remove thread, go right
                predecessor.right = None
                curr = curr.right

    return result


if __name__ == "__main__":
    print("=== Iterative Preorder Traversal ===")

    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    root = TreeNode(1,
        TreeNode(2, TreeNode(4), TreeNode(5)),
        TreeNode(3))

    print(f"Brute:   {brute_force(root)}")   # [1,2,4,5,3]
    print(f"Optimal: {optimal(root)}")        # [1,2,4,5,3]
    print(f"Best:    {best(root)}")            # [1,2,4,5,3]

    # Edge: empty tree
    print(f"Empty:   {optimal(None)}")         # []
