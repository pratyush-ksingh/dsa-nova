"""
Problem: LCA of BST (LeetCode #235)
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
# APPROACH 1: BRUTE FORCE -- General Binary Tree LCA
# Ignore BST property. Postorder search both subtrees.
# Time: O(n)  |  Space: O(h)
# ============================================================
def lca_brute(root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    if root is None or root.val == p.val or root.val == q.val:
        return root
    left = lca_brute(root.left, p, q)
    right = lca_brute(root.right, p, q)
    if left and right:
        return root
    return left if left else right


# ============================================================
# APPROACH 2: OPTIMAL -- BST-Guided Recursive
# Use BST ordering: if both < node go left, both > go right,
# else this is the split point = LCA.
# Time: O(h)  |  Space: O(h)
# ============================================================
def lca_recursive(root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    if p.val < root.val and q.val < root.val:
        return lca_recursive(root.left, p, q)
    if p.val > root.val and q.val > root.val:
        return lca_recursive(root.right, p, q)
    return root  # Split point


# ============================================================
# APPROACH 3: BEST -- BST-Guided Iterative
# Same logic as recursive, but iterative for O(1) space.
# Time: O(h)  |  Space: O(1)
# ============================================================
def lca_iterative(root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    node = root
    while node:
        if p.val < node.val and q.val < node.val:
            node = node.left
        elif p.val > node.val and q.val > node.val:
            node = node.right
        else:
            return node
    return None


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


def find_node(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    if root is None:
        return None
    if root.val == val:
        return root
    left = find_node(root.left, val)
    return left if left else find_node(root.right, val)


if __name__ == "__main__":
    print("=== LCA of BST ===\n")

    bst_arr = [6, 2, 8, 0, 4, 7, 9, None, None, 3, 5]

    queries = [
        (2, 8, 6),
        (2, 4, 2),
        (0, 5, 2),
        (3, 5, 4),
        (7, 9, 8),
        (0, 9, 6),
    ]

    for p_val, q_val, expected in queries:
        root = build_tree(bst_arr)
        p = find_node(root, p_val)
        q = find_node(root, q_val)

        brute = lca_brute(root, p, q)

        root = build_tree(bst_arr)
        p = find_node(root, p_val)
        q = find_node(root, q_val)
        recursive = lca_recursive(root, p, q)

        root = build_tree(bst_arr)
        p = find_node(root, p_val)
        q = find_node(root, q_val)
        iterative = lca_iterative(root, p, q)

        ok = brute.val == expected == recursive.val == iterative.val
        status = "PASS" if ok else "FAIL"
        print(f"p={p_val}, q={q_val} -> LCA expected={expected}")
        print(f"  Brute: {brute.val}, Recursive: {recursive.val}, "
              f"Iterative: {iterative.val} {status}\n")

    # Edge case: two-node tree
    small = build_tree([2, 1])
    p = find_node(small, 2)
    q = find_node(small, 1)
    lca = lca_iterative(small, p, q)
    status = "PASS" if lca.val == 2 else "FAIL"
    print(f"Two-node tree [2,1]: p=2, q=1 -> LCA={lca.val} (expected 2) {status}")
