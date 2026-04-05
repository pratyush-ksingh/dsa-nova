"""
Problem: Last Node in Complete Binary Tree
Difficulty: MEDIUM | XP: 25
Source: InterviewBit
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS Level Order
# Time: O(n)  |  Space: O(n)
# Traverse all nodes level by level, return the last one.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    if root is None:
        return -1

    queue = deque([root])
    last = root

    while queue:
        node = queue.popleft()
        last = node
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)

    return last.val


# ============================================================
# APPROACH 2: OPTIMAL -- Binary Search on Last Level
# Time: O(log^2 n)  |  Space: O(log n) recursion
# Use height comparison to binary-search for the last node.
# In a complete tree, if left subtree height == right subtree
# height, the left subtree is perfect; last node is in right.
# Otherwise, right subtree is perfect (one level shorter);
# last node is in left subtree.
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    if root is None:
        return -1

    def get_height(node):
        """Height by always going left (valid for complete trees)."""
        h = 0
        while node:
            h += 1
            node = node.left
        return h

    def find_last(node):
        if node is None:
            return -1
        if node.left is None and node.right is None:
            return node.val

        left_h = get_height(node.left)
        right_h = get_height(node.right)

        if left_h == right_h:
            # Left subtree is perfect -> last node is in right subtree
            return find_last(node.right)
        else:
            # Right subtree is perfect (one level shorter) -> last is in left
            return find_last(node.left)

    return find_last(root)


# ============================================================
# APPROACH 3: BEST -- Same Binary Search (iterative)
# Time: O(log^2 n)  |  Space: O(1)
# Same logic as optimal but iterative to avoid recursion stack.
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    if root is None:
        return -1

    def get_height(node):
        h = 0
        while node:
            h += 1
            node = node.left
        return h

    curr = root
    while curr:
        if curr.left is None:
            # curr is the last node (leaf at bottom)
            return curr.val

        left_h = get_height(curr.left)
        right_h = get_height(curr.right)

        if left_h == right_h:
            # Left is perfect, go right
            curr = curr.right
        else:
            # Right is perfect (shorter), go left
            curr = curr.left

    return -1


if __name__ == "__main__":
    print("=== Last Node in Complete Binary Tree ===")

    #        1
    #       / \
    #      2   3
    #     / \  /
    #    4  5 6
    root = TreeNode(1,
        TreeNode(2, TreeNode(4), TreeNode(5)),
        TreeNode(3, TreeNode(6)))

    print(f"Brute:   {brute_force(root)}")   # 6
    print(f"Optimal: {optimal(root)}")        # 6
    print(f"Best:    {best(root)}")            # 6

    # Perfect tree: last node is rightmost
    #       1
    #      / \
    #     2   3
    perfect = TreeNode(1, TreeNode(2), TreeNode(3))
    print(f"Perfect: {optimal(perfect)}")     # 3

    # Single node
    print(f"Single:  {optimal(TreeNode(42))}")  # 42
    print(f"Empty:   {optimal(None)}")          # -1
