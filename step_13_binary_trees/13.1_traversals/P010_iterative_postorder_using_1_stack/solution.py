"""
Problem: Iterative Postorder using 1 Stack
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
        recurse(node.left)
        recurse(node.right)
        result.append(node.val)

    recurse(root)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Single Stack with Last Visited Tracking
# Time: O(n)  |  Space: O(h)
# Track the last visited node. Only process current node when
# its right child is null or was the last visited.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    if root is None:
        return []

    result = []
    stack = []
    curr = root
    last_visited = None

    while curr or stack:
        # Go all the way left
        while curr:
            stack.append(curr)
            curr = curr.left

        # Peek at top of stack
        top = stack[-1]

        # If right child exists and hasn't been visited, go right
        if top.right and top.right is not last_visited:
            curr = top.right
        else:
            # Process this node
            result.append(top.val)
            last_visited = stack.pop()

    return result


# ============================================================
# APPROACH 3: BEST -- Single Stack (same approach, cleaner)
# Time: O(n)  |  Space: O(h)
# Same last-visited tracking, slightly streamlined.
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    if root is None:
        return []

    result = []
    stack = []
    curr = root
    prev = None

    while curr or stack:
        if curr:
            stack.append(curr)
            curr = curr.left
        else:
            curr = stack[-1]
            # Right child not visited yet
            if curr.right and curr.right is not prev:
                curr = curr.right
            else:
                result.append(curr.val)
                prev = stack.pop()
                curr = None  # Don't go left again

    return result


if __name__ == "__main__":
    print("=== Iterative Postorder using 1 Stack ===")

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

    # Edge: single node
    print(f"Single:  {optimal(TreeNode(42))}")  # [42]
