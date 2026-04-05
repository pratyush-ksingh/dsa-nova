"""
Problem: LCA of Binary Tree (LeetCode 236)
Difficulty: HARD | XP: 25

Given the root of a binary tree and two nodes p and q, find the Lowest
Common Ancestor (LCA). The LCA is the deepest node that is an ancestor
of both p and q (a node can be an ancestor of itself).
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE — Find paths, then compare
# Time: O(n)  |  Space: O(n) for path storage
# ============================================================
def brute_force(root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    """
    Find the path from root to p and from root to q as lists of nodes.
    Then walk both paths simultaneously from the root until they diverge.
    The last common node before divergence is the LCA.
    """
    def find_path(root, target, path):
        if not root:
            return False
        path.append(root)
        if root is target:
            return True
        if find_path(root.left, target, path) or find_path(root.right, target, path):
            return True
        path.pop()
        return False

    path_p, path_q = [], []
    find_path(root, p, path_p)
    find_path(root, q, path_q)

    # Walk both paths to find the last common node
    lca = None
    for a, b in zip(path_p, path_q):
        if a is b:
            lca = a
        else:
            break
    return lca


# ============================================================
# APPROACH 2: OPTIMAL — Single recursive pass
# Time: O(n)  |  Space: O(h) recursion stack (h = height)
# ============================================================
def optimal(root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    """
    Recursive post-order traversal.
    Base case: if root is None, p, or q — return root.
    Recurse into left and right subtrees.
    - If both sides return non-None: current node is the LCA (p in one subtree, q in other).
    - If only one side returns non-None: that side found both p and q (or one of them); bubble up.
    - If both sides return None: neither p nor q found here; return None.
    """
    if root is None or root is p or root is q:
        return root

    left  = optimal(root.left, p, q)
    right = optimal(root.right, p, q)

    if left and right:
        return root   # p and q on opposite sides
    return left if left else right  # both in same subtree


# ============================================================
# APPROACH 3: BEST — Iterative with parent pointers
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    """
    Iterative approach using a parent-pointer map (BFS/DFS to populate).
    1. BFS to build parent map: node -> its parent.
    2. Build the set of all ancestors of p by walking up via parent map.
    3. Walk up from q; the first ancestor in p's ancestor set is the LCA.
    This avoids recursion stack overflow for very deep/skewed trees.
    """
    parent = {root: None}
    stack = [root]

    # Step 1: populate parent map until both p and q are found
    while p not in parent or q not in parent:
        node = stack.pop()
        if node.left:
            parent[node.left] = node
            stack.append(node.left)
        if node.right:
            parent[node.right] = node
            stack.append(node.right)

    # Step 2: collect all ancestors of p (including p itself)
    ancestors = set()
    cur = p
    while cur:
        ancestors.add(cur)
        cur = parent[cur]

    # Step 3: walk up from q until we hit an ancestor of p
    cur = q
    while cur not in ancestors:
        cur = parent[cur]
    return cur


# ---- Helper to build tree ----
def build_tree(vals):
    if not vals or vals[0] is None:
        return None, {}
    nodes = {}
    root = TreeNode(vals[0])
    nodes[vals[0]] = root
    q = deque([root])
    i = 1
    while q and i < len(vals):
        node = q.popleft()
        if i < len(vals) and vals[i] is not None:
            node.left = TreeNode(vals[i])
            nodes[vals[i]] = node.left
            q.append(node.left)
        i += 1
        if i < len(vals) and vals[i] is not None:
            node.right = TreeNode(vals[i])
            nodes[vals[i]] = node.right
            q.append(node.right)
        i += 1
    return root, nodes


if __name__ == "__main__":
    print("=== LCA of Binary Tree ===")

    #       3
    #      / \
    #     5   1
    #    / \ / \
    #   6  2 0  8
    #     / \
    #    7   4
    root, nodes = build_tree([3, 5, 1, 6, 2, 0, 8, None, None, 7, 4])

    tests = [(5, 1), (5, 4), (3, 5)]
    for pv, qv in tests:
        p, q = nodes[pv], nodes[qv]
        b = brute_force(root, p, q)
        o = optimal(root, p, q)
        be = best(root, p, q)
        print(f"LCA({pv},{qv}): Brute={b.val}, Optimal={o.val}, Best={be.val}")
