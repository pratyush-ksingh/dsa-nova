"""
Problem: Reverse Level Order Traversal
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a binary tree, return its reverse level order traversal --
node values level by level from bottom to top, left to right within
each level. Return as a list of lists.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val: int = 0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS + Reverse the Result
# Time: O(N)  |  Space: O(N)
# Standard level order traversal using a queue, then reverse
# the list of levels at the end.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[List[int]]:
    """BFS level order, then reverse the result list."""
    if not root:
        return []

    result = []
    queue = deque([root])

    while queue:
        level_size = len(queue)
        current_level = []
        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.append(current_level)

    result.reverse()
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- BFS with Deque (appendleft each level)
# Time: O(N)  |  Space: O(N)
# Instead of reversing at the end, insert each level at the
# front of a deque so the result is already bottom-up.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[List[int]]:
    """BFS with deque -- insert each level at front, no final reverse."""
    if not root:
        return []

    result = deque()
    queue = deque([root])

    while queue:
        level_size = len(queue)
        current_level = []
        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.appendleft(current_level)

    return list(result)


# ============================================================
# APPROACH 3: BEST -- DFS with Level Tracking + Reverse
# Time: O(N)  |  Space: O(N)
# DFS records each node's value into a dict keyed by depth,
# then iterates levels from max depth to 0.
# ============================================================
def best(root: Optional[TreeNode]) -> List[List[int]]:
    """DFS with depth dict, then build result from deepest level up."""
    if not root:
        return []

    levels = {}

    def dfs(node: TreeNode, depth: int) -> None:
        if not node:
            return
        if depth not in levels:
            levels[depth] = []
        levels[depth].append(node.val)
        dfs(node.left, depth + 1)
        dfs(node.right, depth + 1)

    dfs(root, 0)

    max_depth = max(levels.keys())
    return [levels[d] for d in range(max_depth, -1, -1)]


# ============================================================
# HELPERS
# ============================================================
def build_tree(values: list) -> Optional[TreeNode]:
    """Build a binary tree from a level-order list (None = no node)."""
    if not values or values[0] is None:
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


if __name__ == "__main__":
    test_cases = [
        ([3, 9, 20, None, None, 15, 7], "Standard tree"),
        ([1], "Single node"),
        ([], "Empty tree"),
        ([1, 2, 3, 4, 5, 6, 7], "Complete binary tree"),
        ([1, 2, None, 3, None, 4, None], "Left-skewed tree"),
    ]
    print("=== Reverse Level Order Traversal ===")

    for values, desc in test_cases:
        root = build_tree(values)
        print(f"\nTree: {values}  ({desc})")
        print(f"  Brute Force: {brute_force(root)}")
        print(f"  Optimal:     {optimal(root)}")
        print(f"  Best:        {best(root)}")
