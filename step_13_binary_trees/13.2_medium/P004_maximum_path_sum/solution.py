"""
Problem: Maximum Path Sum in Binary Tree
Difficulty: HARD | XP: 50

A path in a binary tree is a sequence of nodes where each pair of adjacent
nodes has an edge. The path doesn't need to pass through the root.
Find the maximum sum path (nodes can have negative values).
"""
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - DFS tracking global max per recursive call
# Time: O(N)  |  Space: O(H) recursion depth
# For each node: compute max gain from left/right child subtrees;
# update global answer = node + left_gain + right_gain
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    max_sum = [float('-inf')]

    def gain(node: Optional[TreeNode]) -> int:
        if not node:
            return 0
        left  = max(gain(node.left),  0)
        right = max(gain(node.right), 0)
        max_sum[0] = max(max_sum[0], node.val + left + right)
        return node.val + max(left, right)

    gain(root)
    return max_sum[0]


# ============================================================
# APPROACH 2: OPTIMAL - Clean postorder DFS, functional style
# Time: O(N)  |  Space: O(H)
# Identical algorithm but uses nonlocal for cleaner closure
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    max_sum = float('-inf')

    def dfs(node: Optional[TreeNode]) -> int:
        nonlocal max_sum
        if not node:
            return 0
        left  = max(dfs(node.left),  0)
        right = max(dfs(node.right), 0)
        max_sum = max(max_sum, node.val + left + right)
        return node.val + max(left, right)

    dfs(root)
    return max_sum


# ============================================================
# APPROACH 3: BEST - Iterative postorder DFS using explicit stack
# Time: O(N)  |  Space: O(N)
# Avoids recursion depth limit for very deep trees.
# Two-pass: first collect postorder, then process.
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    # Collect postorder traversal
    stack, order = [root], []
    while stack:
        node = stack.pop()
        order.append(node)
        if node.left:  stack.append(node.left)
        if node.right: stack.append(node.right)
    # order is reverse postorder; process in reverse
    order.reverse()  # now true postorder

    gain: dict[Optional[TreeNode], int] = {None: 0}
    max_sum = float('-inf')
    for node in order:
        left  = max(gain[node.left],  0)
        right = max(gain[node.right], 0)
        max_sum = max(max_sum, node.val + left + right)
        gain[node] = node.val + max(left, right)

    return max_sum


def build(vals):
    """Build tree from level-order list (None = missing node)."""
    if not vals:
        return None
    root = TreeNode(vals[0])
    queue = [root]
    i = 1
    while queue and i < len(vals):
        node = queue.pop(0)
        if i < len(vals) and vals[i] is not None:
            node.left = TreeNode(vals[i])
            queue.append(node.left)
        i += 1
        if i < len(vals) and vals[i] is not None:
            node.right = TreeNode(vals[i])
            queue.append(node.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Maximum Path Sum ===")

    root = build([-10, 9, 20, None, None, 15, 7])
    print("Tree: -10, [9, 20->[15,7]]")
    print(f"Brute:   {brute_force(root)}")   # 42
    print(f"Optimal: {optimal(root)}")        # 42
    print(f"Best:    {best(root)}")           # 42

    root = build([1, 2, 3])
    print("\nTree: 1->[2,3]")
    print(f"Brute:   {brute_force(root)}")   # 6
    print(f"Optimal: {optimal(root)}")
    print(f"Best:    {best(root)}")

    root = build([-3])
    print("\nTree: single node -3")
    print(f"Brute:   {brute_force(root)}")   # -3
    print(f"Optimal: {optimal(root)}")
    print(f"Best:    {best(root)}")
