"""
Problem: Ceil in BST
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
# APPROACH 1: BRUTE FORCE -- Inorder to Sorted Array + Search
# Collect all values via inorder, then find first >= key.
# Time: O(n)  |  Space: O(n)
# ============================================================
def find_ceil_brute(root: Optional[TreeNode], key: int) -> int:
    sorted_vals = []

    def inorder(node):
        if node is None:
            return
        inorder(node.left)
        sorted_vals.append(node.val)
        inorder(node.right)

    inorder(root)

    for val in sorted_vals:
        if val >= key:
            return val
    return -1


# ============================================================
# APPROACH 2: OPTIMAL -- BST-Guided Recursive
# If val == key, return key. If val < key, go right.
# If val > key, record candidate and go left.
# Time: O(h)  |  Space: O(h)
# ============================================================
def find_ceil_recursive(root: Optional[TreeNode], key: int) -> int:
    def helper(node, ceil):
        if node is None:
            return ceil
        if node.val == key:
            return key
        if node.val < key:
            return helper(node.right, ceil)
        # node.val > key: this is a candidate
        return helper(node.left, node.val)

    return helper(root, -1)


# ============================================================
# APPROACH 3: BEST -- BST-Guided Iterative
# Same logic, iterative. O(1) extra space.
# Time: O(h)  |  Space: O(1)
# ============================================================
def find_ceil_iterative(root: Optional[TreeNode], key: int) -> int:
    ceil = -1
    node = root

    while node:
        if node.val == key:
            return key
        elif node.val < key:
            node = node.right
        else:
            ceil = node.val
            node = node.left

    return ceil


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


if __name__ == "__main__":
    print("=== Ceil in BST ===\n")

    # BST:       8
    #          /   \
    #         4    12
    #        / \  / \
    #       2  6 10 14
    bst_arr = [8, 4, 12, 2, 6, 10, 14]

    tests = [
        (5, 6),    # 5 not in tree, next >= is 6
        (7, 8),    # 7 not in tree, next >= is 8
        (8, 8),    # exact match
        (3, 4),    # 3 not in tree, next >= is 4
        (1, 2),    # smaller than min, ceil = min = 2
        (9, 10),   # between 8 and 10
        (13, 14),  # between 12 and 14
        (15, -1),  # larger than max, no ceil
        (14, 14),  # exact match at leaf
        (11, 12),  # between 10 and 12
    ]

    for key, expected in tests:
        root = build_tree(bst_arr)
        brute = find_ceil_brute(root, key)
        optimal = find_ceil_recursive(root, key)
        best = find_ceil_iterative(root, key)
        ok = brute == expected == optimal == best
        status = "PASS" if ok else "FAIL"
        print(f"key={key:2d} -> Brute: {brute:2d}, Optimal: {optimal:2d}, "
              f"Best: {best:2d} (expected {expected:2d}) {status}")

    # Edge case: single node
    print("\nSingle node BST [5]:")
    single = build_tree([5])
    c1 = find_ceil_iterative(single, 3)
    c2 = find_ceil_iterative(single, 6)
    print(f"  key=3 -> ceil={c1} (expected 5) {'PASS' if c1 == 5 else 'FAIL'}")
    print(f"  key=6 -> ceil={c2} (expected -1) {'PASS' if c2 == -1 else 'FAIL'}")
