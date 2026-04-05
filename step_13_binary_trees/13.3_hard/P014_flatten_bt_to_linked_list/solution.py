"""
Problem: Flatten Binary Tree to Linked List (LeetCode 114)
Difficulty: MEDIUM | XP: 25

Flatten a binary tree into a linked list in-place using preorder traversal order.
The "linked list" uses the right pointer; left pointers must be null.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


def build_tree(values: List[Optional[int]]) -> Optional[TreeNode]:
    """Helper: build tree from level-order list."""
    if not values:
        return None
    root = TreeNode(values[0])
    queue = deque([root])
    i = 1
    while queue and i < len(values):
        node = queue.popleft()
        if i < len(values) and values[i] is not None:
            node.left = TreeNode(values[i])
            queue.append(node.left)
        i += 1
        if i < len(values) and values[i] is not None:
            node.right = TreeNode(values[i])
            queue.append(node.right)
        i += 1
    return root


def tree_to_list(root: Optional[TreeNode]) -> List[int]:
    """Helper: read flattened right-only chain."""
    result = []
    node = root
    while node:
        result.append(node.val)
        node = node.right
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE
# Collect all nodes in preorder into a list, then relink.
# Time: O(n)  |  Space: O(n)
# ============================================================
def flatten_brute(root: Optional[TreeNode]) -> None:
    """
    Intuition: Do a standard preorder traversal to capture the visit
    sequence, then wire each node's right to the next node and left to None.
    Simple but uses O(n) extra memory.
    """
    nodes: List[TreeNode] = []

    def preorder(node: Optional[TreeNode]) -> None:
        if node is None:
            return
        nodes.append(node)
        preorder(node.left)
        preorder(node.right)

    preorder(root)

    for i in range(len(nodes) - 1):
        nodes[i].left = None
        nodes[i].right = nodes[i + 1]
    if nodes:
        nodes[-1].left = None
        nodes[-1].right = None


# ============================================================
# APPROACH 2: OPTIMAL — Recursive reverse-postorder (right → left → root)
# Time: O(n)  |  Space: O(h) call stack
# ============================================================
def flatten_optimal(root: Optional[TreeNode]) -> None:
    """
    Intuition: Process nodes in reverse preorder (right subtree → left subtree
    → root). Keep a 'prev' pointer. When we visit the root last, we set
    root.right = prev and root.left = None. This stitches the list without
    any extra array.

    Trick: visiting in reverse order (right first, then left) means when we
    process the root, 'prev' already points to the head of the already-flattened
    right subtree — perfect for right-linking.
    """
    prev: List[Optional[TreeNode]] = [None]  # use list to allow mutation in closure

    def dfs(node: Optional[TreeNode]) -> None:
        if node is None:
            return
        dfs(node.right)
        dfs(node.left)
        node.right = prev[0]
        node.left = None
        prev[0] = node

    dfs(root)


# ============================================================
# APPROACH 3: BEST — Morris-style iterative O(1) space
# Time: O(n)  |  Space: O(1)
# ============================================================
def flatten_best(root: Optional[TreeNode]) -> None:
    """
    Intuition: At each node with a left child, find the rightmost node of the
    left subtree (the predecessor in preorder). Thread the current node's right
    subtree onto that predecessor's right pointer, then move the left subtree
    to the right and null out the left pointer. Advance to root.right. Repeat.

    This is the same idea as Morris traversal — no stack, no recursion.
    """
    curr = root
    while curr:
        if curr.left:
            # Find rightmost node of left subtree
            predecessor = curr.left
            while predecessor.right:
                predecessor = predecessor.right
            # Thread right subtree onto predecessor
            predecessor.right = curr.right
            # Move left subtree to right
            curr.right = curr.left
            curr.left = None
        curr = curr.right


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Flatten Binary Tree to Linked List ===\n")

    # Tree:      1
    #           / \
    #          2   5
    #         / \   \
    #        3   4   6
    # Expected preorder: [1, 2, 3, 4, 5, 6]

    for name, fn in [("Brute", flatten_brute),
                     ("Optimal", flatten_optimal),
                     ("Best (Morris)", flatten_best)]:
        root = build_tree([1, 2, 5, 3, 4, None, 6])
        fn(root)
        print(f"{name:15s}: {tree_to_list(root)}")
