"""
Problem: Iterative Inorder Traversal
Difficulty: MEDIUM | XP: 25
"""
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive Inorder
# Time: O(n)  |  Space: O(h) recursion stack
# Standard recursive approach as baseline.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    result = []

    def recurse(node):
        if node is None:
            return
        recurse(node.left)         # Left
        result.append(node.val)    # Root
        recurse(node.right)        # Right

    recurse(root)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Iterative "Push All Left" Pattern
# Time: O(n)  |  Space: O(h)
# Push entire left spine, pop and process, then go right.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    result = []
    stack = []
    curr = root

    while curr is not None or stack:
        # Phase 1: Push all left nodes
        while curr is not None:
            stack.append(curr)
            curr = curr.left

        # Phase 2: Pop, process, go right
        curr = stack.pop()
        result.append(curr.val)
        curr = curr.right

    return result


# ============================================================
# APPROACH 3: BEST -- Morris Inorder Traversal
# Time: O(n)  |  Space: O(1)
# Thread tree temporarily. Add to result on SECOND visit.
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    result = []
    curr = root

    while curr:
        if curr.left is None:
            # No left child: process and go right
            result.append(curr.val)
            curr = curr.right
        else:
            # Find inorder predecessor
            predecessor = curr.left
            while predecessor.right and predecessor.right is not curr:
                predecessor = predecessor.right

            if predecessor.right is None:
                # First visit: create thread, go left
                predecessor.right = curr
                curr = curr.left
            else:
                # Second visit: remove thread, process, go right
                predecessor.right = None
                result.append(curr.val)  # Inorder: add on SECOND visit
                curr = curr.right

    return result


if __name__ == "__main__":
    print("=== Iterative Inorder Traversal ===")

    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    root = TreeNode(1,
        TreeNode(2, TreeNode(4), TreeNode(5)),
        TreeNode(3))

    print(f"Brute:   {brute_force(root)}")   # [4,2,5,1,3]
    print(f"Optimal: {optimal(root)}")        # [4,2,5,1,3]
    print(f"Best:    {best(root)}")            # [4,2,5,1,3]

    # Edge cases
    print(f"Empty:   {optimal(None)}")                  # []
    print(f"Single:  {optimal(TreeNode(42))}")          # [42]
