"""
Problem: Top View of Binary Tree
Difficulty: MEDIUM | XP: 25

Return the top view of a binary tree: for each horizontal distance (column),
return the first node seen when looking from above (minimum level node).
Output left to right.
"""
from typing import List, Optional
from collections import deque, defaultdict


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - DFS collect (col, level, val), keep min level per col
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    """DFS: record (col -> (min_level, val)). Return sorted by col."""
    if not root:
        return []
    col_map = {}  # col -> (min_level, val)

    def dfs(node, col, level):
        if not node:
            return
        if col not in col_map or level < col_map[col][0]:
            col_map[col] = (level, node.val)
        dfs(node.left, col - 1, level + 1)
        dfs(node.right, col + 1, level + 1)

    dfs(root, 0, 0)
    return [val for _, val in sorted(col_map.items())]


# ============================================================
# APPROACH 2: OPTIMAL - BFS (first visit per column = top view)
# Time: O(N)  |  Space: O(N)
# BFS processes nodes level by level; first time a column is seen = top.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    """BFS: first node at each column = top view. Sort by column for output."""
    if not root:
        return []
    top_map = {}
    queue = deque([(root, 0)])
    while queue:
        node, col = queue.popleft()
        if col not in top_map:
            top_map[col] = node.val
        if node.left:
            queue.append((node.left, col - 1))
        if node.right:
            queue.append((node.right, col + 1))
    return [top_map[c] for c in sorted(top_map)]


# ============================================================
# APPROACH 3: BEST - BFS with tracked col range for direct array build
# Time: O(N)  |  Space: O(N)
# Track min/max column during BFS to avoid sorting keys.
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    """BFS with column range tracking; build output array directly."""
    if not root:
        return []
    top_map = {}
    min_col = max_col = 0
    queue = deque([(root, 0)])
    while queue:
        node, col = queue.popleft()
        if col not in top_map:
            top_map[col] = node.val
        min_col = min(min_col, col)
        max_col = max(max_col, col)
        if node.left:
            queue.append((node.left, col - 1))
        if node.right:
            queue.append((node.right, col + 1))
    return [top_map[c] for c in range(min_col, max_col + 1)]


if __name__ == "__main__":
    print("=== Top View of Binary Tree ===")
    #     1
    #    / \
    #   2   3
    #  / \   \
    # 4   5   6
    root1 = TreeNode(1, TreeNode(2, TreeNode(4), TreeNode(5)), TreeNode(3, None, TreeNode(6)))
    print("brute: ", brute_force(root1))    # [4,2,1,3,6]
    print("optimal:", optimal(root1))
    print("best:   ", best(root1))

    #   1
    #  / \
    # 2   3
    #  \
    #   4
    #    \
    #     5
    root2 = TreeNode(1, TreeNode(2, None, TreeNode(4, None, TreeNode(5))), TreeNode(3))
    print("brute: ", brute_force(root2))    # [2,1,3,5] or [2,1,3,4,5]?
    print("optimal:", optimal(root2))
    print("best:   ", best(root2))
