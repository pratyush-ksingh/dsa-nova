"""
Problem: Merge Two Binary Trees (LeetCode #617)
Difficulty: EASY | XP: 10
Source: InterviewBit
"""
from typing import Optional, List
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Create New Tree
# Time: O(min(n1,n2))  |  Space: O(min(h1,h2)) + new nodes
# Create a brand new merged tree, preserving originals.
# ============================================================
def brute_force(t1: Optional[TreeNode], t2: Optional[TreeNode]) -> Optional[TreeNode]:
    if t1 is None and t2 is None:
        return None
    if t1 is None:
        return TreeNode(t2.val, brute_force(None, t2.left), brute_force(None, t2.right))
    if t2 is None:
        return TreeNode(t1.val, brute_force(t1.left, None), brute_force(t1.right, None))

    merged = TreeNode(t1.val + t2.val)
    merged.left = brute_force(t1.left, t2.left)
    merged.right = brute_force(t1.right, t2.right)
    return merged


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Merge into t1
# Time: O(min(n1,n2))  |  Space: O(min(h1,h2))
# Modify t1 in-place. Attach t2's subtrees where t1 is null.
# ============================================================
def optimal(t1: Optional[TreeNode], t2: Optional[TreeNode]) -> Optional[TreeNode]:
    if t1 is None:
        return t2
    if t2 is None:
        return t1

    t1.val += t2.val
    t1.left = optimal(t1.left, t2.left)
    t1.right = optimal(t1.right, t2.right)
    return t1


# ============================================================
# APPROACH 3: BEST -- Iterative BFS Merge
# Time: O(min(n1,n2))  |  Space: O(min(n1,n2))
# Level-by-level merge using queue. Modifies t1 in-place.
# ============================================================
def best(t1: Optional[TreeNode], t2: Optional[TreeNode]) -> Optional[TreeNode]:
    if t1 is None:
        return t2
    if t2 is None:
        return t1

    queue = deque([(t1, t2)])

    while queue:
        n1, n2 = queue.popleft()
        n1.val += n2.val

        # Handle left children
        if n1.left and n2.left:
            queue.append((n1.left, n2.left))
        elif n1.left is None:
            n1.left = n2.left  # Attach t2's left subtree

        # Handle right children
        if n1.right and n2.right:
            queue.append((n1.right, n2.right))
        elif n1.right is None:
            n1.right = n2.right  # Attach t2's right subtree

    return t1


# Helper: preorder for verification
def preorder(node: Optional[TreeNode]) -> List[int]:
    if node is None:
        return []
    return [node.val] + preorder(node.left) + preorder(node.right)


if __name__ == "__main__":
    print("=== Merge Two Binary Trees ===")

    # t1 = [1,3,2], t2 = [2,1,3] -> [3,4,5]
    t1 = TreeNode(1, TreeNode(3), TreeNode(2))
    t2 = TreeNode(2, TreeNode(1), TreeNode(3))
    print(f"Brute:   {preorder(brute_force(t1, t2))}")  # [3,4,5]

    t1b = TreeNode(1, TreeNode(3), TreeNode(2))
    t2b = TreeNode(2, TreeNode(1), TreeNode(3))
    print(f"Optimal: {preorder(optimal(t1b, t2b))}")    # [3,4,5]

    t1c = TreeNode(1, TreeNode(3), TreeNode(2))
    t2c = TreeNode(2, TreeNode(1), TreeNode(3))
    print(f"Best:    {preorder(best(t1c, t2c))}")        # [3,4,5]

    # Edge: one null
    print(f"One null: {preorder(optimal(None, TreeNode(5)))}")  # [5]
