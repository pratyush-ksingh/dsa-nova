"""
Problem: Symmetric Tree
Difficulty: EASY | XP: 10
"""
from typing import Optional, List
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Mirror Tree and Compare
# Time: O(n)  |  Space: O(n) for mirrored copy
# Create a mirrored copy, then check if original == mirror.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> bool:
    if root is None:
        return True

    def mirror(node):
        """Create a mirrored copy of the tree."""
        if node is None:
            return None
        new_node = TreeNode(node.val)
        new_node.left = mirror(node.right)
        new_node.right = mirror(node.left)
        return new_node

    def is_same(t1, t2):
        """Check if two trees are identical."""
        if t1 is None and t2 is None:
            return True
        if t1 is None or t2 is None:
            return False
        return (t1.val == t2.val and
                is_same(t1.left, t2.left) and
                is_same(t1.right, t2.right))

    mirrored = mirror(root)
    return is_same(root, mirrored)


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Mirror Comparison
# Time: O(n)  |  Space: O(h) recursion stack
# Compare left.left with right.right and left.right with
# right.left simultaneously. No extra tree needed.
# ============================================================
def optimal(root: Optional[TreeNode]) -> bool:
    if root is None:
        return True

    def is_mirror(left, right):
        if left is None and right is None:
            return True
        if left is None or right is None:
            return False
        return (left.val == right.val and
                is_mirror(left.left, right.right) and
                is_mirror(left.right, right.left))

    return is_mirror(root.left, root.right)


# ============================================================
# APPROACH 3: BEST -- Iterative with Queue
# Time: O(n)  |  Space: O(n) for queue
# Use a queue to compare mirror pairs iteratively.
# ============================================================
def best(root: Optional[TreeNode]) -> bool:
    if root is None:
        return True

    queue = deque()
    queue.append(root.left)
    queue.append(root.right)

    while queue:
        left = queue.popleft()
        right = queue.popleft()

        if left is None and right is None:
            continue
        if left is None or right is None:
            return False
        if left.val != right.val:
            return False

        # Enqueue in mirror order
        queue.append(left.left)
        queue.append(right.right)
        queue.append(left.right)
        queue.append(right.left)

    return True


if __name__ == "__main__":
    print("=== Symmetric Tree ===")

    #       1
    #      / \
    #     2   2
    #    / \ / \
    #   3  4 4  3
    root = TreeNode(1,
        TreeNode(2, TreeNode(3), TreeNode(4)),
        TreeNode(2, TreeNode(4), TreeNode(3)))

    print(f"Brute:   {brute_force(root)}")   # True
    print(f"Optimal: {optimal(root)}")        # True
    print(f"Best:    {best(root)}")            # True

    # Not symmetric
    #       1
    #      / \
    #     2   2
    #      \   \
    #       3   3
    root2 = TreeNode(1,
        TreeNode(2, None, TreeNode(3)),
        TreeNode(2, None, TreeNode(3)))

    print(f"Not sym: {optimal(root2)}")       # False

    print(f"Empty:   {optimal(None)}")         # True
    print(f"Single:  {optimal(TreeNode(1))}")  # True
