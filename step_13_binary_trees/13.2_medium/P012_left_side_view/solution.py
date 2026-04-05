"""Problem: Left Side View of Binary Tree
Difficulty: MEDIUM | XP: 25
"""
from collections import deque
from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - BFS, take first node at each level
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    queue = deque([root])
    while queue:
        size = len(queue)
        for i in range(size):
            node = queue.popleft()
            if i == 0:
                result.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
    return result


# ============================================================
# APPROACH 2: OPTIMAL - DFS preorder (root, left, right)
# Time: O(N)  |  Space: O(H)
# First node visited at each depth is the leftmost visible one
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    result = []

    def dfs(node, depth):
        if not node:
            return
        if depth == len(result):
            result.append(node.val)
        dfs(node.left, depth + 1)
        dfs(node.right, depth + 1)

    dfs(root, 0)
    return result


# ============================================================
# APPROACH 3: BEST - Iterative DFS with explicit stack
# Time: O(N)  |  Space: O(H)
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    stack = [(root, 0)]
    while stack:
        node, depth = stack.pop()
        if depth == len(result):
            result.append(node.val)
        # Push right before left so left is processed first
        if node.right:
            stack.append((node.right, depth + 1))
        if node.left:
            stack.append((node.left, depth + 1))
    return result


def build_tree(values):
    """Build tree from level-order list (None = missing node)."""
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
    tests = [
        ([1, 2, 3, None, 5, None, 4], [1, 2, 5]),
        ([1, None, 3], [1, 3]),
        ([], []),
        ([1], [1]),
        ([1, 2, None, 3], [1, 2, 3]),
    ]
    for vals, expected in tests:
        root = build_tree(vals)
        bf = brute_force(root)
        root = build_tree(vals)
        opt = optimal(root)
        root = build_tree(vals)
        be = best(root)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] tree={vals} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
