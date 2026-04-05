"""
Problem: Level Order Traversal (LeetCode #102)
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: DFS with Depth Tracking
# Preorder DFS, append node.val to result[depth].
# Time: O(n)  |  Space: O(h) + O(n) result
# ============================================================
def level_order_dfs(root: Optional[TreeNode]) -> List[List[int]]:
    result = []

    def dfs(node, depth):
        if node is None:
            return
        if depth == len(result):
            result.append([])
        result[depth].append(node.val)
        dfs(node.left, depth + 1)
        dfs(node.right, depth + 1)

    dfs(root, 0)
    return result


# ============================================================
# APPROACH 2 & 3: BFS with Queue (Standard)
# Snapshot queue size per level. Process level by level.
# Time: O(n)  |  Space: O(w) + O(n) result
# ============================================================
def level_order_bfs(root: Optional[TreeNode]) -> List[List[int]]:
    if root is None:
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

    return result


# ============================================================
# APPROACH 3 (ALT): Two Lists (no deque needed)
# Alternate between "current level" and "next level" lists.
# Time: O(n)  |  Space: O(w) + O(n) result
# ============================================================
def level_order_two_lists(root: Optional[TreeNode]) -> List[List[int]]:
    if root is None:
        return []

    result = []
    current_level = [root]

    while current_level:
        values = []
        next_level = []
        for node in current_level:
            values.append(node.val)
            if node.left:
                next_level.append(node.left)
            if node.right:
                next_level.append(node.right)
        result.append(values)
        current_level = next_level

    return result


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
    print("=== Level Order Traversal ===\n")

    tests = [
        [3, 9, 20, None, None, 15, 7],
        [1],
        [],
        [1, 2, 3, 4, 5],
        [1, 2, None, 3, None, 4],
        [1, 2, 3, 4, 5, 6, 7, 8],
    ]

    for arr in tests:
        root = build_tree(arr)
        dfs = level_order_dfs(root)
        bfs = level_order_bfs(root)
        two_lists = level_order_two_lists(root)
        match = dfs == bfs == two_lists
        status = "PASS" if match else "FAIL"
        print(f"Tree: {arr}")
        print(f"  DFS:       {dfs}")
        print(f"  BFS:       {bfs}")
        print(f"  Two Lists: {two_lists} {status}\n")
