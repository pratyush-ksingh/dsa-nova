"""
Floor in BST

Find the largest value in BST that is <= given key.
Return -1 if no such value exists.
"""
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Inorder to sorted array + search
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode], key: int) -> int:
    sorted_vals = []

    def inorder(node):
        if not node:
            return
        inorder(node.left)
        sorted_vals.append(node.val)
        inorder(node.right)

    inorder(root)

    floor = -1
    for val in sorted_vals:
        if val <= key:
            floor = val
        else:
            break
    return floor


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive BST search
# Time: O(H)  |  Space: O(H)
# ============================================================
def optimal(root: Optional[TreeNode], key: int) -> int:
    if not root:
        return -1

    if root.val == key:
        return key

    if root.val > key:
        return optimal(root.left, key)

    # root.val < key: candidate found, check right for better
    right_floor = optimal(root.right, key)
    return right_floor if right_floor != -1 else root.val


# ============================================================
# APPROACH 3: BEST -- Iterative BST search
# Time: O(H)  |  Space: O(1)
# ============================================================
def best(root: Optional[TreeNode], key: int) -> int:
    floor = -1
    curr = root

    while curr:
        if curr.val == key:
            return key
        elif curr.val < key:
            floor = curr.val  # update candidate
            curr = curr.right  # look for larger candidate
        else:
            curr = curr.left  # current too large

    return floor


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    #       10
    #      /  \
    #     5    15
    #    / \   / \
    #   2   7 12  20
    root = TreeNode(10)
    root.left = TreeNode(5)
    root.right = TreeNode(15)
    root.left.left = TreeNode(2)
    root.left.right = TreeNode(7)
    root.right.left = TreeNode(12)
    root.right.right = TreeNode(20)

    test_keys = [13, 10, 1, 15, 8, 20, 25]

    print("=== Floor in BST ===")
    for key in test_keys:
        print(f"Key={key:2d} -> Brute: {brute_force(root, key):2d} | "
              f"Optimal: {optimal(root, key):2d} | Best: {best(root, key):2d}")
    # Key=13 -> 12, Key=10 -> 10, Key=1 -> -1, Key=15 -> 15
    # Key=8 -> 7, Key=20 -> 20, Key=25 -> 20
