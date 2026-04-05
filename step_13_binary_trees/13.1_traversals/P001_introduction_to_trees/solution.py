"""
Problem: Introduction to Trees
Difficulty: EASY | XP: 10

Understand tree basics, build a binary tree, and implement utility methods.
"""
from typing import List, Optional
from collections import deque


# ============================================================
# TreeNode definition
# ============================================================
class TreeNode:
    def __init__(self, val: int = 0, left: 'TreeNode' = None, right: 'TreeNode' = None):
        self.val = val
        self.left = left
        self.right = right

    def __repr__(self):
        return f"TreeNode({self.val})"


# ============================================================
# APPROACH 1: Build tree manually (direct construction)
# ============================================================
def build_manually() -> TreeNode:
    """
        1
       / \\
      2   3
     / \\
    4   5
    """
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    return root


# ============================================================
# APPROACH 2: Build tree from level-order array
# Time: O(n) | Space: O(n)
# ============================================================
def build_from_array(arr: List[Optional[int]]) -> Optional[TreeNode]:
    if not arr or arr[0] is None:
        return None

    root = TreeNode(arr[0])
    queue = deque([root])
    i = 1

    while queue and i < len(arr):
        current = queue.popleft()

        # Left child
        if i < len(arr) and arr[i] is not None:
            current.left = TreeNode(arr[i])
            queue.append(current.left)
        i += 1

        # Right child
        if i < len(arr) and arr[i] is not None:
            current.right = TreeNode(arr[i])
            queue.append(current.right)
        i += 1

    return root


# ============================================================
# APPROACH 3: Utility functions
# ============================================================

def count_nodes(root: Optional[TreeNode]) -> int:
    """Count total nodes: O(n)"""
    if root is None:
        return 0
    return 1 + count_nodes(root.left) + count_nodes(root.right)


def height(root: Optional[TreeNode]) -> int:
    """Height of tree: O(n). Empty tree has height -1."""
    if root is None:
        return -1
    return 1 + max(height(root.left), height(root.right))


def is_leaf(node: Optional[TreeNode]) -> bool:
    """Check if a node is a leaf."""
    return node is not None and node.left is None and node.right is None


def count_leaves(root: Optional[TreeNode]) -> int:
    """Count leaf nodes: O(n)"""
    if root is None:
        return 0
    if is_leaf(root):
        return 1
    return count_leaves(root.left) + count_leaves(root.right)


def level_order(root: Optional[TreeNode]) -> List[List[int]]:
    """Level-order traversal (BFS): O(n)"""
    if root is None:
        return []
    result = []
    queue = deque([root])
    while queue:
        level = []
        for _ in range(len(queue)):
            node = queue.popleft()
            level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.append(level)
    return result


if __name__ == "__main__":
    print("=== Introduction to Trees ===\n")

    # --- Manual Construction ---
    print("--- Approach 1: Manual Construction ---")
    root = build_manually()
    print(f"Level order: {level_order(root)}")   # [[1],[2,3],[4,5]]
    print(f"Count: {count_nodes(root)}")          # 5
    print(f"Height: {height(root)}")              # 2
    print(f"Leaves: {count_leaves(root)}")        # 3
    print(f"Is root a leaf? {is_leaf(root)}")     # False
    print(f"Is node 4 a leaf? {is_leaf(root.left.left)}")  # True

    # --- Array Construction ---
    print("\n--- Approach 2: From Array ---")
    arr = [1, 2, 3, 4, 5, None, 6]
    root2 = build_from_array(arr)
    print(f"Level order: {level_order(root2)}")   # [[1],[2,3],[4,5,6]]
    print(f"Count: {count_nodes(root2)}")          # 6
    print(f"Height: {height(root2)}")              # 2

    # --- Edge Cases ---
    print("\n--- Edge Cases ---")
    print(f"Empty tree height: {height(None)}")     # -1
    print(f"Empty tree count: {count_nodes(None)}")  # 0

    single = TreeNode(42)
    print(f"Single node height: {height(single)}")   # 0
    print(f"Single node is leaf: {is_leaf(single)}")  # True

    # Skewed tree
    skewed = TreeNode(1, right=TreeNode(2, right=TreeNode(3, right=TreeNode(4))))
    print(f"Skewed tree height: {height(skewed)}")    # 3
    print(f"Skewed tree levels: {level_order(skewed)}")
