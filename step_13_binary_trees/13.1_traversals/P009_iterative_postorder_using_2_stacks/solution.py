"""
Problem: Iterative Postorder using 2 Stacks
Difficulty: HARD | XP: 50
"""
from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive Postorder
# Time: O(n)  |  Space: O(h) recursion stack
# Standard recursive approach as baseline.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    result = []

    def recurse(node):
        if node is None:
            return
        recurse(node.left)        # Left
        recurse(node.right)       # Right
        result.append(node.val)   # Root

    recurse(root)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Two Stacks
# Time: O(n)  |  Space: O(n)
# Push root to s1, pop from s1, push popped to s2, push
# left then right children to s1. s2 gives postorder.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    if root is None:
        return []

    s1 = [root]
    s2 = []

    while s1:
        node = s1.pop()
        s2.append(node)

        # Push left first, then right (so right is popped first
        # from s1, pushed to s2 first -> ends up deeper in s2)
        if node.left:
            s1.append(node.left)
        if node.right:
            s1.append(node.right)

    # s2 has nodes in reverse postorder, so reverse it
    result = []
    while s2:
        result.append(s2.pop().val)

    return result


# ============================================================
# APPROACH 3: BEST -- Two Stacks (same, compact)
# Time: O(n)  |  Space: O(n)
# Same two-stack approach with cleaner code.
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    if root is None:
        return []

    s1, s2 = [root], []

    while s1:
        node = s1.pop()
        s2.append(node.val)
        if node.left:
            s1.append(node.left)
        if node.right:
            s1.append(node.right)

    return s2[::-1]


if __name__ == "__main__":
    print("=== Iterative Postorder using 2 Stacks ===")

    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    root = TreeNode(1,
        TreeNode(2, TreeNode(4), TreeNode(5)),
        TreeNode(3))

    print(f"Brute:   {brute_force(root)}")   # [4,5,2,3,1]
    print(f"Optimal: {optimal(root)}")        # [4,5,2,3,1]
    print(f"Best:    {best(root)}")            # [4,5,2,3,1]

    # Edge: empty tree
    print(f"Empty:   {optimal(None)}")         # []
