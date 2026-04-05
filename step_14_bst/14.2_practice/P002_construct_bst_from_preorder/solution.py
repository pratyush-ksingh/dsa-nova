"""
Problem: Construct BST from Preorder Traversal (LeetCode #1008)
Difficulty: MEDIUM | XP: 25
"""
from typing import Optional, List
import math


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Sequential BST Insertion
# Time: O(n*h) -> O(n^2) worst  |  Space: O(h)
# Insert each element into BST one by one.
# ============================================================
def brute_force(preorder: List[int]) -> Optional[TreeNode]:
    if not preorder:
        return None

    root = TreeNode(preorder[0])
    for val in preorder[1:]:
        curr = root
        while True:
            if val < curr.val:
                if curr.left is None:
                    curr.left = TreeNode(val)
                    break
                curr = curr.left
            else:
                if curr.right is None:
                    curr.right = TreeNode(val)
                    break
                curr = curr.right
    return root


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Partition
# Time: O(n log n) avg, O(n^2) worst  |  Space: O(n)
# Find partition point, recurse on left and right halves.
# ============================================================
def optimal(preorder: List[int]) -> Optional[TreeNode]:
    def build(lo, hi):
        if lo > hi:
            return None
        node = TreeNode(preorder[lo])

        # Find first element greater than root
        split = hi + 1
        for i in range(lo + 1, hi + 1):
            if preorder[i] > preorder[lo]:
                split = i
                break

        node.left = build(lo + 1, split - 1)
        node.right = build(split, hi)
        return node

    return build(0, len(preorder) - 1)


# ============================================================
# APPROACH 3: BEST -- Upper Bound Recursion (O(n))
# Time: O(n)  |  Space: O(h)
# Global index + bound. Each element consumed exactly once.
# ============================================================
def best(preorder: List[int]) -> Optional[TreeNode]:
    idx = [0]  # Use list for mutability in nested function
    n = len(preorder)

    def build(bound):
        if idx[0] >= n or preorder[idx[0]] > bound:
            return None

        node = TreeNode(preorder[idx[0]])
        idx[0] += 1

        node.left = build(node.val)    # Left: values < node
        node.right = build(bound)      # Right: values < parent's bound
        return node

    return build(math.inf)


# Helper: preorder traversal for verification
def preorder_list(node: Optional[TreeNode]) -> List[int]:
    if node is None:
        return []
    return [node.val] + preorder_list(node.left) + preorder_list(node.right)


if __name__ == "__main__":
    print("=== Construct BST from Preorder ===")

    pre1 = [8, 5, 1, 7, 10, 12]
    print(f"Brute:   {preorder_list(brute_force(pre1))}")    # [8,5,1,7,10,12]
    print(f"Optimal: {preorder_list(optimal(pre1))}")        # [8,5,1,7,10,12]
    print(f"Best:    {preorder_list(best(pre1))}")            # [8,5,1,7,10,12]

    # Edge: single element
    print(f"Single:  {preorder_list(best([5]))}")              # [5]

    # Edge: sorted ascending (right-skewed)
    print(f"Sorted:  {preorder_list(best([1,2,3,4,5]))}")     # [1,2,3,4,5]
