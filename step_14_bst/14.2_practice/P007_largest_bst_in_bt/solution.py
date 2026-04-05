"""
Problem: Largest BST in BT
Difficulty: HARD | XP: 50

Given a binary tree, find the size of the largest subtree
that is also a Binary Search Tree (BST).
"""
from typing import Optional, List, Tuple
from collections import deque
import math


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Check each subtree explicitly
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    def is_bst(node, lo, hi):
        if not node:
            return True
        if not (lo < node.val < hi):
            return False
        return is_bst(node.left, lo, node.val) and is_bst(node.right, node.val, hi)

    def count(node):
        if not node:
            return 0
        return 1 + count(node.left) + count(node.right)

    max_size = [0]

    def dfs(node):
        if not node:
            return
        if is_bst(node, -math.inf, math.inf):
            max_size[0] = max(max_size[0], count(node))
        dfs(node.left)
        dfs(node.right)

    dfs(root)
    return max_size[0]


# ============================================================
# APPROACH 2: OPTIMAL - Postorder single-pass with tuple info
# Time: O(n)  |  Space: O(h)
# ============================================================
# Returns (size, min_val, max_val, is_bst, max_bst_below)
def optimal(root: Optional[TreeNode]) -> int:
    def postorder(node) -> Tuple:
        if not node:
            return (0, math.inf, -math.inf, True, 0)

        ls, lmin, lmax, lbst, lmb = postorder(node.left)
        rs, rmin, rmax, rbst, rmb = postorder(node.right)

        if lbst and rbst and node.val > lmax and node.val < rmin:
            sz = ls + rs + 1
            return (sz, min(node.val, lmin), max(node.val, rmax), True, sz)

        return (0, 0, 0, False, max(lmb, rmb))

    return postorder(root)[4]


# ============================================================
# APPROACH 3: BEST - Postorder with named namedtuple
# Time: O(n)  |  Space: O(h)
# ============================================================
# Same complexity; uses dataclass for clarity. Best practice in
# interviews and production code.
# Real-life use: identifying the largest intact BST index
# segment in a partially corrupted database after a crash.
# ============================================================
from dataclasses import dataclass


@dataclass
class Info:
    size: int
    min_val: float
    max_val: float
    is_bst: bool
    max_bst: int


def best(root: Optional[TreeNode]) -> int:
    def dfs(node) -> Info:
        if not node:
            return Info(0, math.inf, -math.inf, True, 0)

        L = dfs(node.left)
        R = dfs(node.right)

        if L.is_bst and R.is_bst and node.val > L.max_val and node.val < R.min_val:
            sz = L.size + R.size + 1
            return Info(sz, min(node.val, L.min_val), max(node.val, R.max_val), True, sz)

        return Info(0, 0, 0, False, max(L.max_bst, R.max_bst))

    return dfs(root).max_bst


# ============================================================
# TEST HELPERS
# ============================================================

def build(arr: List) -> Optional[TreeNode]:
    if not arr:
        return None
    root = TreeNode(arr[0])
    q = deque([root])
    i = 1
    while q and i < len(arr):
        node = q.popleft()
        if i < len(arr) and arr[i] is not None:
            node.left = TreeNode(arr[i])
            q.append(node.left)
        i += 1
        if i < len(arr) and arr[i] is not None:
            node.right = TreeNode(arr[i])
            q.append(node.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Largest BST in BT ===")

    # Tree: 10->5,15  5->1,8  15->null,7
    # Largest BST = subtree at 5 (1,5,8) => size 3
    t1 = build([10, 5, 15, 1, 8, None, 7])
    print(f"Test1 Brute   (expect 3): {brute_force(t1)}")
    print(f"Test1 Optimal (expect 3): {optimal(t1)}")
    print(f"Test1 Best    (expect 3): {best(t1)}")

    # Full BST => size 7
    t2 = build([4, 2, 6, 1, 3, 5, 7])
    print(f"Test2 Best    (expect 7): {best(t2)}")

    # Single node
    t3 = build([5])
    print(f"Test3 Best    (expect 1): {best(t3)}")

    # Tree: 3->2,5  2->1,null  5->4,6 => whole tree is BST => 6
    t4 = build([3, 2, 5, 1, None, 4, 6])
    print(f"Test4 Best    (expect 6): {best(t4)}")
