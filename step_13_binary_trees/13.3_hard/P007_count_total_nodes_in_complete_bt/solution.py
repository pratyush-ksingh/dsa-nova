"""
Problem: Count Total Nodes in Complete Binary Tree
Difficulty: MEDIUM | XP: 25

Given the root of a complete binary tree, count the number of nodes.
A complete binary tree: all levels fully filled except possibly the last,
which is filled left to right.

Naive O(N); optimal O(log^2 N) exploiting CBT structure.
"""
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Simple recursive count
# Time: O(N)  |  Space: O(H)
# Visit every node; ignores complete tree property
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    if not root:
        return 0
    return 1 + brute_force(root.left) + brute_force(root.right)


# ============================================================
# APPROACH 2: OPTIMAL - Exploit CBT property
# Time: O(log^2 N)  |  Space: O(log N)
# Compute left height (always go left) and right height (always go right).
# If equal: perfect subtree -> 2^h - 1 nodes.
# If not: recurse on both children.
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    def left_height(node):
        h = 0
        while node:
            h += 1
            node = node.left
        return h

    def right_height(node):
        h = 0
        while node:
            h += 1
            node = node.right
        return h

    lh = left_height(root)
    rh = right_height(root)
    if lh == rh:
        return (1 << lh) - 1  # 2^h - 1
    return 1 + optimal(root.left) + optimal(root.right)


# ============================================================
# APPROACH 3: BEST - Binary search on last level
# Time: O(log^2 N)  |  Space: O(log N)
# Height h; binary search for rightmost filled node on last level.
# Check existence of a node at last-level index using bit navigation.
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    h = 0
    cur = root
    while cur.left:
        h += 1
        cur = cur.left

    def node_exists(idx: int) -> bool:
        lo, hi = 0, (1 << h) - 1
        node = root
        for _ in range(h):
            mid = (lo + hi) // 2
            if idx <= mid:
                node = node.left
                hi = mid
            else:
                node = node.right
                lo = mid + 1
            if not node:
                return False
        return node is not None

    lo, hi = 0, (1 << h) - 1
    while lo < hi:
        mid = (lo + hi + 1) // 2
        if node_exists(mid):
            lo = mid
        else:
            hi = mid - 1

    return (1 << h) + lo  # 2^h nodes in full levels + (lo+1) in last


def build_complete(vals):
    if not vals:
        return None
    nodes = [TreeNode(v) if v is not None else None for v in vals]
    for i in range(len(nodes)):
        if nodes[i]:
            li, ri = 2*i+1, 2*i+2
            if li < len(nodes): nodes[i].left  = nodes[li]
            if ri < len(nodes): nodes[i].right = nodes[ri]
    return nodes[0]


if __name__ == "__main__":
    print("=== Count Total Nodes in Complete BT ===")

    root = build_complete([1, 2, 3, 4, 5, 6, 7])
    print("Perfect 7-node tree:")
    print(f"Brute:   {brute_force(root)}")   # 7
    print(f"Optimal: {optimal(root)}")        # 7
    print(f"Best:    {best(root)}")           # 7

    root = build_complete([1, 2, 3, 4, 5, 6])
    print("\n6-node complete tree:")
    print(f"Brute:   {brute_force(root)}")   # 6
    print(f"Optimal: {optimal(root)}")
    print(f"Best:    {best(root)}")

    root = build_complete([1])
    print("\nSingle node:")
    print(f"Brute:   {brute_force(root)}")   # 1
    print(f"Optimal: {optimal(root)}")
    print(f"Best:    {best(root)}")
