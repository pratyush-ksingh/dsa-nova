"""
Problem: Vertical Order Traversal
Difficulty: HARD | XP: 50

Given a binary tree, return a list of node values grouped by column.
Root is at column 0. Left child: col-1, right child: col+1.
Nodes at same (col, row): sort by value.
"""
from typing import List, Optional
from collections import defaultdict, deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


def build_tree(arr: list) -> Optional[TreeNode]:
    """Build tree from level-order list (None = missing)."""
    if not arr or arr[0] is None:
        return None
    root = TreeNode(arr[0])
    q = deque([root])
    i = 1
    while q and i < len(arr):
        node = q.popleft()
        if i < len(arr) and arr[i] is not None:
            node.left = TreeNode(arr[i])
            q.append(node.left)
        i += 1
        if i < len(arr) and arr[i] is not None:
            node.right = TreeNode(arr[i])
            q.append(node.right)
        i += 1
    return root


# ============================================================
# APPROACH 1: BRUTE FORCE - DFS collect all (col, row, val), sort, group
# Time: O(N log N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[List[int]]:
    """Collect (col, row, val) via DFS, sort, then group by col."""
    nodes = []

    def dfs(node, col, row):
        if not node:
            return
        nodes.append((col, row, node.val))
        dfs(node.left, col - 1, row + 1)
        dfs(node.right, col + 1, row + 1)

    dfs(root, 0, 0)
    nodes.sort()  # sort by (col, row, val)

    result = []
    if not nodes:
        return result
    prev_col = nodes[0][0] - 1
    for col, row, val in nodes:
        if col != prev_col:
            result.append([])
            prev_col = col
        result[-1].append(val)
    return result


# ============================================================
# APPROACH 2: OPTIMAL - BFS with sorted dict grouping
# Time: O(N log N)  |  Space: O(N)
# BFS level order: use defaultdict(list) keyed by col.
# Nodes naturally come in row order; within same row/col sort by val.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[List[int]]:
    """BFS: track (node, col, row). Group by col then sort within group."""
    if not root:
        return []
    # col -> list of (row, val)
    col_map = defaultdict(list)
    queue = deque([(root, 0, 0)])
    while queue:
        node, col, row = queue.popleft()
        col_map[col].append((row, node.val))
        if node.left:
            queue.append((node.left, col - 1, row + 1))
        if node.right:
            queue.append((node.right, col + 1, row + 1))

    result = []
    for col in sorted(col_map):
        col_map[col].sort()  # sort by (row, val)
        result.append([val for _, val in col_map[col]])
    return result


# ============================================================
# APPROACH 3: BEST - DFS + collections.defaultdict (same as brute but cleaner)
# Time: O(N log N)  |  Space: O(N)
# Same approach as optimal but using DFS instead of BFS for traversal.
# ============================================================
def best(root: Optional[TreeNode]) -> List[List[int]]:
    """DFS with (col, row, val) collection, then sort and group."""
    if not root:
        return []
    col_map = defaultdict(list)

    def dfs(node, col, row):
        if not node:
            return
        col_map[col].append((row, node.val))
        dfs(node.left, col - 1, row + 1)
        dfs(node.right, col + 1, row + 1)

    dfs(root, 0, 0)
    return [[val for _, val in sorted(col_map[c])] for c in sorted(col_map)]


if __name__ == "__main__":
    print("=== Vertical Order Traversal ===")
    #     3
    #    / \
    #   9  20
    #      / \
    #     15   7
    t1 = build_tree([3, 9, 20, None, None, 15, 7])
    print("brute: ", brute_force(t1))    # [[9],[3,15],[20],[7]]
    print("optimal:", optimal(t1))
    print("best:   ", best(t1))

    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6  7
    t2 = build_tree([1, 2, 3, 4, 5, 6, 7])
    print("brute: ", brute_force(t2))    # [[4],[2],[1,5,6],[3],[7]]
    print("optimal:", optimal(t2))
    print("best:   ", best(t2))
