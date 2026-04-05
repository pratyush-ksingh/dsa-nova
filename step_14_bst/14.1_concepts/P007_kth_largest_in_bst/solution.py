"""Problem: Kth Largest in BST
Difficulty: MEDIUM | XP: 25

Find the kth largest element in a BST. Reverse inorder traversal visits nodes
in descending order, so we stop at kth visit.
"""
from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Full inorder, index from end
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode], k: int) -> int:
    vals = []

    def inorder(node):
        if not node:
            return
        inorder(node.left)
        vals.append(node.val)
        inorder(node.right)

    inorder(root)
    return vals[-k]


# ============================================================
# APPROACH 2: OPTIMAL - Reverse inorder DFS with early stop
# Time: O(H + k)  |  Space: O(H)
# Right -> Root -> Left gives descending order
# ============================================================
def optimal(root: Optional[TreeNode], k: int) -> int:
    state = [0, None]  # [count, result]

    def reverse_inorder(node):
        if not node or state[0] >= k:
            return
        reverse_inorder(node.right)
        state[0] += 1
        if state[0] == k:
            state[1] = node.val
            return
        reverse_inorder(node.left)

    reverse_inorder(root)
    return state[1]


# ============================================================
# APPROACH 3: BEST - Iterative reverse inorder
# Time: O(H + k)  |  Space: O(H)
# Avoids recursion depth issues on skewed trees
# ============================================================
def best(root: Optional[TreeNode], k: int) -> int:
    stack = []
    cur = root
    count = 0
    while cur or stack:
        while cur:
            stack.append(cur)
            cur = cur.right  # go right first
        cur = stack.pop()
        count += 1
        if count == k:
            return cur.val
        cur = cur.left
    return -1


def build_bst(vals):
    """Insert list of values into BST."""
    root = None

    def insert(node, val):
        if not node:
            return TreeNode(val)
        if val < node.val:
            node.left = insert(node.left, val)
        else:
            node.right = insert(node.right, val)
        return node

    for v in vals:
        root = insert(root, v)
    return root


if __name__ == "__main__":
    # BST: insert [5,3,6,2,4,1]
    # Inorder: [1,2,3,4,5,6]
    tests = [
        ([5, 3, 6, 2, 4, 1], 1, 6),
        ([5, 3, 6, 2, 4, 1], 3, 4),
        ([5, 3, 6, 2, 4, 1], 6, 1),
        ([10], 1, 10),
    ]
    for vals, k, expected in tests:
        root = build_bst(vals)
        bf = brute_force(root, k)
        root = build_bst(vals)
        opt = optimal(root, k)
        root = build_bst(vals)
        be = best(root, k)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] BST={sorted(vals)} k={k} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
