"""
Problem: Right Side View
LeetCode 199 | Difficulty: MEDIUM | XP: 25

Given the root of a binary tree, imagine yourself standing on the RIGHT side.
Return the values of the nodes you can see, ordered from top to bottom.

Key Insight: The rightmost node at each level is visible from the right side.
             BFS takes the last node per level; DFS (right child first) takes
             the first node seen at each depth.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


def build_tree(values: List) -> Optional[TreeNode]:
    """Build a tree from level-order list (None = missing node)."""
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


# ============================================================
# APPROACH 1: BRUTE FORCE  (BFS — take last node per level)
# Time: O(n)  |  Space: O(n)  [queue holds up to n/2 nodes]
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    """
    Level-order BFS. At each level, process all nodes in the queue.
    The last node processed at that level is the rightmost visible one.
    Append its value to the result.
    """
    if not root:
        return []
    result: List[int] = []
    queue: deque = deque([root])
    while queue:
        level_size = len(queue)
        for i in range(level_size):
            node = queue.popleft()
            if i == level_size - 1:  # last node in this level
                result.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
    return result


# ============================================================
# APPROACH 2: OPTIMAL  (DFS right-first, record first at each depth)
# Time: O(n)  |  Space: O(h)  [h = tree height, recursion stack]
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    """
    DFS traversing RIGHT child before LEFT child.
    Maintain a `result` list indexed by depth.
    The first time we visit a given depth, it is always the rightmost node
    at that level (because we go right first).
    Extend result if this depth is new; otherwise skip (already recorded).
    """
    result: List[int] = []

    def dfs(node: Optional[TreeNode], depth: int) -> None:
        if not node:
            return
        if depth == len(result):
            result.append(node.val)   # first visit at this depth = rightmost
        dfs(node.right, depth + 1)
        dfs(node.left,  depth + 1)

    dfs(root, 0)
    return result


# ============================================================
# APPROACH 3: BEST  (Iterative DFS with explicit stack — avoids recursion limit)
# Time: O(n)  |  Space: O(h)
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    """
    Iterative pre-order DFS using an explicit stack.
    Push (node, depth) pairs; push LEFT before RIGHT so RIGHT is popped first.
    Same logic: first time a depth is seen, record the node value.
    Avoids Python's recursion limit for very deep trees.
    """
    if not root:
        return []
    result: List[int] = []
    stack = [(root, 0)]
    while stack:
        node, depth = stack.pop()
        if depth == len(result):
            result.append(node.val)
        # Push left first so right is processed first (LIFO)
        if node.left:
            stack.append((node.left, depth + 1))
        if node.right:
            stack.append((node.right, depth + 1))
    return result


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3, None, 5, None, 4], [1, 3, 4]),
        ([1, None, 3], [1, 3]),
        ([], []),
        ([1], [1]),
        ([1, 2, 3, 4, None, None, None], [1, 3, 4]),
    ]
    print("=== Right Side View ===")
    for values, expected in test_cases:
        root = build_tree(values)
        b   = brute_force(root)
        o   = optimal(root)
        bst = best(root)
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"  tree={values} => brute={b}, optimal={o}, best={bst} "
              f"(expected {expected}) [{status}]")
