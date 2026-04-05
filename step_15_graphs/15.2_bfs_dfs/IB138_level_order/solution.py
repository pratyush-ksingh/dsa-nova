"""
Problem: Level Order Traversal (Binary Tree / Graph)
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a binary tree (or graph), return its values grouped by level.
Output: List of lists, where output[i] = all node values at depth i.
"""
from typing import List, Optional
from collections import defaultdict, deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE — DFS with depth tracking
# Time: O(n)  |  Space: O(n) — result + O(h) recursion stack
# Use DFS; maintain current depth; append node to bucket[depth].
# The result buckets are then returned in level order.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[List[int]]:
    """
    DFS-based level grouping. Preorder DFS with depth parameter.
    Elegant but slightly non-intuitive for 'level order'.
    """
    result: List[List[int]] = []

    def dfs(node: Optional[TreeNode], depth: int) -> None:
        if node is None:
            return
        if depth == len(result):
            result.append([])       # new level discovered
        result[depth].append(node.val)
        dfs(node.left,  depth + 1)
        dfs(node.right, depth + 1)

    dfs(root, 0)
    return result


# ============================================================
# APPROACH 2: OPTIMAL — BFS with queue + level separator
# Time: O(n)  |  Space: O(n) — queue holds at most one full level
# Classic BFS. Track level boundaries using queue size snapshot.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[List[int]]:
    """
    BFS level-by-level. At each step we know exactly how many nodes
    are in the current level (the current queue size), so we dequeue
    that many nodes and collect them before moving to the next level.
    """
    if root is None:
        return []

    result: List[List[int]] = []
    queue: deque = deque([root])

    while queue:
        level_size = len(queue)   # number of nodes at this depth
        level: List[int] = []

        for _ in range(level_size):
            node = queue.popleft()
            level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(level)

    return result


# ============================================================
# APPROACH 3: BEST — BFS with None sentinel (compact variant)
# Time: O(n)  |  Space: O(n)
# Uses None as a level separator in the queue instead of size snapshot.
# Same complexity, slightly different implementation style.
# ============================================================
def best(root: Optional[TreeNode]) -> List[List[int]]:
    """
    BFS with a None sentinel to mark end of each level.
    When we dequeue None, we know we just finished a level.
    This is functionally identical to Approach 2 in complexity;
    it's a common alternative style seen in interviews.
    """
    if root is None:
        return []

    result: List[List[int]] = []
    queue: deque = deque([root, None])  # None = end-of-level marker
    level: List[int] = []

    while queue:
        node = queue.popleft()

        if node is None:
            # Finished this level
            result.append(level)
            level = []
            if queue:             # more levels remain
                queue.append(None)
        else:
            level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

    return result


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------
def build_tree(vals):
    if not vals:
        return None
    nodes = [TreeNode(v) if v is not None else None for v in vals]
    for i, node in enumerate(nodes):
        if node is None:
            continue
        l, r = 2 * i + 1, 2 * i + 2
        if l < len(nodes): node.left  = nodes[l]
        if r < len(nodes): node.right = nodes[r]
    return nodes[0]


if __name__ == "__main__":
    print("=== Level Order Traversal ===")

    #        3
    #       / \
    #      9  20
    #        /  \
    #       15   7
    root = build_tree([3, 9, 20, None, None, 15, 7])

    print(f"Brute:   {brute_force(root)}")   # [[3],[9,20],[15,7]]
    print(f"Optimal: {optimal(root)}")        # [[3],[9,20],[15,7]]
    print(f"Best:    {best(root)}")           # [[3],[9,20],[15,7]]

    # Single node
    root2 = build_tree([1])
    print(f"\nSingle node:")
    print(f"Brute:   {brute_force(root2)}")
    print(f"Optimal: {optimal(root2)}")
    print(f"Best:    {best(root2)}")

    # Empty tree
    print(f"\nEmpty tree:")
    print(f"Brute:   {brute_force(None)}")
    print(f"Optimal: {optimal(None)}")
    print(f"Best:    {best(None)}")
