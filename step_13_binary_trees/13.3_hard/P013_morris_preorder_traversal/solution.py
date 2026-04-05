"""
Problem: Morris Preorder Traversal
Difficulty: HARD | XP: 50

O(1) space preorder traversal using threaded binary tree (Morris algorithm).
No stack, no recursion — temporarily modifies tree then restores it.
"""

from collections import deque
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Iterative preorder using explicit stack
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    result = []
    if not root:
        return result
    stack = [root]
    while stack:
        node = stack.pop()
        result.append(node.val)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Morris Traversal — O(1) space threaded tree
# For each node: no left child => visit + go right.
# Has left child => find predecessor:
#   if pred.right is None => thread it, visit current, go left
#   if pred.right == current => unthread, go right
# Time: O(N)  |  Space: O(1)
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    result = []
    curr = root
    while curr:
        if not curr.left:
            result.append(curr.val)
            curr = curr.right
        else:
            # Find inorder predecessor
            pred = curr.left
            while pred.right and pred.right is not curr:
                pred = pred.right
            if pred.right is None:
                # First visit: thread and visit current (preorder)
                result.append(curr.val)
                pred.right = curr
                curr = curr.left
            else:
                # Second visit: unthread, move right
                pred.right = None
                curr = curr.right
    return result


# ============================================================
# APPROACH 3: BEST
# Recursive DFS — clean reference implementation
# Time: O(N)  |  Space: O(H) recursion stack
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    result = []

    def dfs(node):
        if not node:
            return
        result.append(node.val)
        dfs(node.left)
        dfs(node.right)

    dfs(root)
    return result


def build_tree(vals):
    if not vals:
        return None
    root = TreeNode(vals[0])
    queue = deque([root])
    i = 1
    while queue and i < len(vals):
        node = queue.popleft()
        if i < len(vals) and vals[i] is not None:
            node.left = TreeNode(vals[i])
            queue.append(node.left)
        i += 1
        if i < len(vals) and vals[i] is not None:
            node.right = TreeNode(vals[i])
            queue.append(node.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Morris Preorder Traversal ===")

    # Tree: 1->2,3; 2->4,5 => preorder [1,2,4,5,3]
    vals = [1, 2, 3, 4, 5]
    print(f"BruteForce:       {brute_force(build_tree(vals))}")  # [1,2,4,5,3]
    print(f"Optimal (Morris): {optimal(build_tree(vals))}")       # [1,2,4,5,3]
    print(f"Best (Recursive): {best(build_tree(vals))}")          # [1,2,4,5,3]

    # Skewed left tree: 1->2->3
    skewed = TreeNode(1, TreeNode(2, TreeNode(3)))
    print(f"Skewed Morris: {optimal(skewed)}")  # [1,2,3]

    # Single node
    print(f"Single node: {optimal(TreeNode(42))}")  # [42]
