"""
Problem: Construct Binary Tree from Inorder and Postorder Traversal
Difficulty: MEDIUM | XP: 25
LeetCode: #106

Given two integer arrays inorder and postorder (both of the same binary tree),
construct and return the binary tree.

Key insight:
- Postorder: [left][right][ROOT] -- last element is always the root
- Inorder:   [left][ROOT][right] -- root splits it into left and right subtrees
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive with linear inorder search
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def build_tree_brute(inorder: List[int], postorder: List[int]) -> Optional[TreeNode]:
    """
    Recursively:
    1. Last element of postorder is the root.
    2. Find it in inorder with a linear scan -> splits into left/right.
    3. Recurse on left and right sublists.
    """
    if not inorder or not postorder:
        return None

    root_val = postorder[-1]
    root = TreeNode(root_val)

    # Find root in inorder -- O(n) scan
    mid = inorder.index(root_val)

    # Elements left of mid in inorder = left subtree
    # Corresponding postorder chunk has the same count
    left_size = mid
    root.left = build_tree_brute(inorder[:mid], postorder[:left_size])
    root.right = build_tree_brute(inorder[mid + 1:], postorder[left_size:-1])

    return root


# ============================================================
# APPROACH 2: OPTIMAL -- HashMap for O(1) inorder lookup
# Time: O(n)  |  Space: O(n)
# ============================================================
def build_tree_optimal(inorder: List[int], postorder: List[int]) -> Optional[TreeNode]:
    """
    Precompute a map of value -> index in inorder for O(1) lookups.
    Pass index boundaries instead of slicing arrays.
    """
    idx_map = {val: i for i, val in enumerate(inorder)}
    post_idx = [len(postorder) - 1]  # mutable pointer into postorder (rightmost first)

    def build(in_lo: int, in_hi: int) -> Optional[TreeNode]:
        if in_lo > in_hi:
            return None

        root_val = postorder[post_idx[0]]
        post_idx[0] -= 1
        root = TreeNode(root_val)

        mid = idx_map[root_val]

        # IMPORTANT: build right subtree FIRST because postorder is processed right-to-left
        root.right = build(mid + 1, in_hi)
        root.left = build(in_lo, mid - 1)

        return root

    return build(0, len(inorder) - 1)


# ============================================================
# APPROACH 3: BEST -- Same as Optimal, explicit post_end parameter
# Time: O(n)  |  Space: O(n) -- cleaner without mutable list trick
# ============================================================
def build_tree_best(inorder: List[int], postorder: List[int]) -> Optional[TreeNode]:
    """
    Pass postorder end index explicitly through the recursion.
    Same O(n) time/space but slightly cleaner parameterization.
    The postorder end index shrinks by (left_size + right_size + 1) at each level.
    """
    idx_map = {val: i for i, val in enumerate(inorder)}

    def build(in_lo: int, in_hi: int, post_end: int) -> Optional[TreeNode]:
        if in_lo > in_hi:
            return None

        root_val = postorder[post_end]
        root = TreeNode(root_val)
        mid = idx_map[root_val]

        left_size = mid - in_lo
        right_size = in_hi - mid

        # Right subtree uses postorder[post_end - right_size .. post_end - 1]
        root.right = build(mid + 1, in_hi, post_end - 1)
        # Left subtree uses postorder[post_end - right_size - left_size .. post_end - right_size - 1]
        root.left = build(in_lo, mid - 1, post_end - right_size - 1)

        return root

    if not inorder:
        return None
    return build(0, len(inorder) - 1, len(postorder) - 1)


# ============================================================
# Helpers
# ============================================================
def inorder_of(root: Optional[TreeNode]) -> List[int]:
    res = []
    def dfs(node):
        if node:
            dfs(node.left)
            res.append(node.val)
            dfs(node.right)
    dfs(root)
    return res


def postorder_of(root: Optional[TreeNode]) -> List[int]:
    res = []
    def dfs(node):
        if node:
            dfs(node.left)
            dfs(node.right)
            res.append(node.val)
    dfs(root)
    return res


if __name__ == "__main__":
    print("=== Build Binary Tree from Inorder and Postorder ===\n")

    tests = [
        ([9, 3, 15, 20, 7], [9, 15, 7, 20, 3]),   # LC #106 example
        ([2, 1, 3], [2, 3, 1]),                     # root=1
        ([1], [1]),                                 # single node
        ([-1], [-1]),                               # negative
    ]

    for inorder, postorder in tests:
        r1 = build_tree_brute(inorder[:], postorder[:])
        r2 = build_tree_optimal(inorder[:], postorder[:])
        r3 = build_tree_best(inorder[:], postorder[:])

        io1, po1 = inorder_of(r1), postorder_of(r1)
        io2, po2 = inorder_of(r2), postorder_of(r2)
        io3, po3 = inorder_of(r3), postorder_of(r3)

        ok = (io1 == io2 == io3 == inorder) and (po1 == po2 == po3 == postorder)
        print(f"inorder={inorder}, postorder={postorder}")
        print(f"  Brute   -> inorder={io1}, postorder={po1}")
        print(f"  Optimal -> inorder={io2}, postorder={po2}")
        print(f"  Best    -> inorder={io3}, postorder={po3}")
        print(f"  {'PASS' if ok else 'FAIL'}\n")
