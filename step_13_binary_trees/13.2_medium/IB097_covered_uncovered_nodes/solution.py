"""
Covered and Uncovered Nodes

Find |sum_uncovered - sum_covered| in a binary tree.
Uncovered = boundary nodes (left boundary + right boundary + leaves).
Covered = all other interior nodes.
"""
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


def total_sum(node: Optional[TreeNode]) -> int:
    if not node:
        return 0
    return node.val + total_sum(node.left) + total_sum(node.right)


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashSet to mark uncovered nodes
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    total = total_sum(root)
    uncovered = set()

    # Left boundary walk
    cur = root
    while cur:
        uncovered.add(id(cur))
        cur = cur.left if cur.left else cur.right

    # Right boundary walk
    cur = root
    while cur:
        uncovered.add(id(cur))
        cur = cur.right if cur.right else cur.left

    # All leaves
    def add_leaves(node):
        if not node:
            return
        if not node.left and not node.right:
            uncovered.add(id(node))
            return
        add_leaves(node.left)
        add_leaves(node.right)

    add_leaves(root)

    # Build id -> val mapping for sum
    node_map = {}

    def build_map(node):
        if not node:
            return
        node_map[id(node)] = node.val
        build_map(node.left)
        build_map(node.right)

    build_map(root)

    uncovered_sum = sum(node_map[nid] for nid in uncovered)
    covered_sum = total - uncovered_sum
    return abs(uncovered_sum - covered_sum)


# ============================================================
# APPROACH 2: OPTIMAL -- Direct uncovered sum (no set)
# Time: O(N)  |  Space: O(H)
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    total = total_sum(root)

    uncovered = root.val

    # Left boundary from root.left downward
    cur = root.left
    while cur:
        uncovered += cur.val
        cur = cur.left if cur.left else cur.right

    # Right boundary from root.right downward
    cur = root.right
    while cur:
        uncovered += cur.val
        cur = cur.right if cur.right else cur.left

    covered = total - uncovered
    return abs(uncovered - covered)


# ============================================================
# APPROACH 3: BEST -- Formula |2 * uncovered - total|
# Time: O(N)  |  Space: O(H)
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    total = total_sum(root)

    # Compute uncovered sum
    uncovered = root.val

    cur = root.left
    while cur:
        uncovered += cur.val
        cur = cur.left if cur.left else cur.right

    cur = root.right
    while cur:
        uncovered += cur.val
        cur = cur.right if cur.right else cur.left

    return abs(2 * uncovered - total)


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    #         8
    #        / \
    #       3   10
    #      / \    \
    #     1   6    14
    #        / \   /
    #       4   7 13
    root = TreeNode(8)
    root.left = TreeNode(3)
    root.right = TreeNode(10)
    root.left.left = TreeNode(1)
    root.left.right = TreeNode(6)
    root.right.right = TreeNode(14)
    root.left.right.left = TreeNode(4)
    root.left.right.right = TreeNode(7)
    root.right.right.left = TreeNode(13)

    print("=== Covered Uncovered Nodes ===")
    print(f"Brute:   {brute_force(root)}")   # 54
    print(f"Optimal: {optimal(root)}")        # 54
    print(f"Best:    {best(root)}")           # 54
