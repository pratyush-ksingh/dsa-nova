"""
Problem: Height of Binary Tree / Maximum Depth (LeetCode #104)
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
# APPROACH 1 & 3: RECURSIVE DFS (Postorder)
# Time: O(n)  |  Space: O(h)
# ============================================================
def max_depth_recursive(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0
    left_depth = max_depth_recursive(root.left)
    right_depth = max_depth_recursive(root.right)
    return 1 + max(left_depth, right_depth)


# ============================================================
# APPROACH 2: ITERATIVE BFS (Level-Order)
# Time: O(n)  |  Space: O(w)
# ============================================================
def max_depth_bfs(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    queue = deque([root])
    depth = 0

    while queue:
        for _ in range(len(queue)):
            node = queue.popleft()
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        depth += 1

    return depth


# ============================================================
# BONUS: Iterative DFS with explicit stack
# Time: O(n)  |  Space: O(h)
# ============================================================
def max_depth_iterative_dfs(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    stack = [(root, 1)]
    max_d = 0

    while stack:
        node, depth = stack.pop()
        max_d = max(max_d, depth)
        if node.right:
            stack.append((node.right, depth + 1))
        if node.left:
            stack.append((node.left, depth + 1))

    return max_d


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
    print("=== Height of Binary Tree ===\n")

    tests = [
        ([3, 9, 20, None, None, 15, 7], 3),
        ([1, None, 2], 2),
        ([], 0),
        ([1], 1),
        ([1, 2, 3, 4, None, None, None, 5], 4),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        rec = max_depth_recursive(root)
        bfs = max_depth_bfs(root)
        dfs = max_depth_iterative_dfs(root)
        status = "PASS" if rec == expected == bfs == dfs else "FAIL"
        print(f"Tree: {arr}")
        print(f"  Recursive: {rec}, BFS: {bfs}, Iterative DFS: {dfs} (expected {expected}) {status}\n")
