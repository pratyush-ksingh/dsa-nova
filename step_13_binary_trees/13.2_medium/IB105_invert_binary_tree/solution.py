"""
Problem: Invert Binary Tree (LeetCode #226)
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
# APPROACH 1 & 3: RECURSIVE DFS (Preorder Swap)
# Swap children, then recurse into both subtrees.
# Time: O(n)  |  Space: O(h)
# ============================================================
def invert_recursive(root: Optional[TreeNode]) -> Optional[TreeNode]:
    if root is None:
        return None
    root.left, root.right = root.right, root.left
    invert_recursive(root.left)
    invert_recursive(root.right)
    return root


# ============================================================
# APPROACH 2: ITERATIVE BFS
# Level-by-level: dequeue, swap children, enqueue children.
# Time: O(n)  |  Space: O(w)
# ============================================================
def invert_bfs(root: Optional[TreeNode]) -> Optional[TreeNode]:
    if root is None:
        return None

    queue = deque([root])
    while queue:
        node = queue.popleft()
        node.left, node.right = node.right, node.left
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)
    return root


# ============================================================
# APPROACH 3 (ALT): ITERATIVE DFS with Stack
# Same as BFS but using a stack.
# Time: O(n)  |  Space: O(h)
# ============================================================
def invert_iterative_dfs(root: Optional[TreeNode]) -> Optional[TreeNode]:
    if root is None:
        return None

    stack = [root]
    while stack:
        node = stack.pop()
        node.left, node.right = node.right, node.left
        if node.left:
            stack.append(node.left)
        if node.right:
            stack.append(node.right)
    return root


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


def tree_to_list(root: Optional[TreeNode]) -> List[Optional[int]]:
    if root is None:
        return []
    result = []
    queue = deque([root])
    while queue:
        node = queue.popleft()
        if node:
            result.append(node.val)
            queue.append(node.left)
            queue.append(node.right)
        else:
            result.append(None)
    # Trim trailing nulls
    while result and result[-1] is None:
        result.pop()
    return result


if __name__ == "__main__":
    print("=== Invert Binary Tree ===\n")

    tests = [
        ([4, 2, 7, 1, 3, 6, 9], [4, 7, 2, 9, 6, 3, 1]),
        ([2, 1, 3], [2, 3, 1]),
        ([1], [1]),
        ([], []),
        ([1, 2], [1, None, 2]),
    ]

    methods = [
        ("Recursive", invert_recursive),
        ("BFS", invert_bfs),
        ("Iterative DFS", invert_iterative_dfs),
    ]

    for arr, expected in tests:
        print(f"Tree: {arr} -> Expected: {expected}")
        for name, fn in methods:
            root = build_tree(arr)
            result = fn(root)
            actual = tree_to_list(result)
            status = "PASS" if actual == expected else "FAIL"
            print(f"  {name}: {actual} {status}")
        print()
