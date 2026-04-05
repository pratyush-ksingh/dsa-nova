"""
Problem: Min Depth of Binary Tree (LeetCode #111)
Difficulty: EASY | XP: 10
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: RECURSIVE DFS
# Handle three cases: no left, no right, both exist.
# Time: O(n)  |  Space: O(h)
# ============================================================
def min_depth_recursive(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0
    # If no left child, must go right
    if root.left is None:
        return 1 + min_depth_recursive(root.right)
    # If no right child, must go left
    if root.right is None:
        return 1 + min_depth_recursive(root.left)
    # Both children exist: take the shorter path
    return 1 + min(min_depth_recursive(root.left), min_depth_recursive(root.right))


# ============================================================
# APPROACH 2 & 3: BFS with Early Termination
# First leaf encountered = minimum depth. Stops early.
# Time: O(n) worst, O(k) best  |  Space: O(w)
# ============================================================
def min_depth_bfs(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    queue = deque([root])
    depth = 1

    while queue:
        for _ in range(len(queue)):
            node = queue.popleft()
            # First leaf found = minimum depth
            if node.left is None and node.right is None:
                return depth
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        depth += 1

    return depth


# ============================================================
# APPROACH 3 (ALT): ITERATIVE DFS with Stack
# Track min depth across all leaf nodes.
# Time: O(n)  |  Space: O(h)
# ============================================================
def min_depth_iterative_dfs(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    stack = [(root, 1)]
    min_d = float('inf')

    while stack:
        node, depth = stack.pop()
        if node.left is None and node.right is None:
            min_d = min(min_d, depth)
        if node.right:
            stack.append((node.right, depth + 1))
        if node.left:
            stack.append((node.left, depth + 1))

    return min_d


def build_tree(arr: List[Optional[int]]) -> Optional[TreeNode]:
    if not arr or arr[0] is None:
        return None
    root = TreeNode(arr[0])
    queue = deque([root])
    i = 1
    while queue and i < len(arr):
        curr = queue.popleft()
        if i < len(arr) and arr[i] is not None:
            curr.left = TreeNode(arr[i])
            queue.append(curr.left)
        i += 1
        if i < len(arr) and arr[i] is not None:
            curr.right = TreeNode(arr[i])
            queue.append(curr.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Min Depth of Binary Tree ===\n")

    tests = [
        ([3, 9, 20, None, None, 15, 7], 2),
        ([2, None, 3, None, 4, None, 5, None, 6], 5),
        ([1], 1),
        ([1, 2], 2),
        ([], 0),
        ([1, 2, 3, 4, 5], 2),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        rec = min_depth_recursive(root)
        bfs = min_depth_bfs(root)
        dfs = min_depth_iterative_dfs(root)
        status = "PASS" if rec == expected == bfs == dfs else "FAIL"
        print(f"Tree: {arr}")
        print(f"  Recursive: {rec}, BFS: {bfs}, Iterative DFS: {dfs} "
              f"(expected {expected}) {status}\n")
