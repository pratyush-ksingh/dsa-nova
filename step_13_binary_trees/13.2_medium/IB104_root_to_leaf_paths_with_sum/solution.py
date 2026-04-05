"""
Problem: Root to Leaf Paths With Sum
Difficulty: MEDIUM | XP: 25
Source: InterviewBit
"""
from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Collect All Paths, Check Sums
# Time: O(n * h)  |  Space: O(n * h) storing all paths
# Find all root-to-leaf paths, then check if any has target sum.
# ============================================================
def brute_force(root: Optional[TreeNode], target: int) -> bool:
    if root is None:
        return False

    all_paths = []

    def collect_paths(node, path):
        if node is None:
            return
        path.append(node.val)
        if node.left is None and node.right is None:
            all_paths.append(list(path))
        else:
            collect_paths(node.left, path)
            collect_paths(node.right, path)
        path.pop()

    collect_paths(root, [])

    for path in all_paths:
        if sum(path) == target:
            return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL -- DFS with Running Sum Subtraction
# Time: O(n)  |  Space: O(h) recursion stack
# Subtract current node value from target. At leaf, check if
# remaining equals 0.
# ============================================================
def optimal(root: Optional[TreeNode], target: int) -> bool:
    if root is None:
        return False

    # Subtract current node's value
    remaining = target - root.val

    # If leaf node, check if path sum matches
    if root.left is None and root.right is None:
        return remaining == 0

    # Recurse on children
    return optimal(root.left, remaining) or optimal(root.right, remaining)


# ============================================================
# APPROACH 3: BEST -- Same DFS (already optimal)
# Time: O(n)  |  Space: O(h) recursion stack
# Same approach -- iterative version using stack for clarity.
# ============================================================
def best(root: Optional[TreeNode], target: int) -> bool:
    if root is None:
        return False

    # Stack stores (node, remaining_sum)
    stack = [(root, target - root.val)]

    while stack:
        node, remaining = stack.pop()

        # Check if leaf with matching sum
        if node.left is None and node.right is None and remaining == 0:
            return True

        if node.right:
            stack.append((node.right, remaining - node.right.val))
        if node.left:
            stack.append((node.left, remaining - node.left.val))

    return False


if __name__ == "__main__":
    print("=== Root to Leaf Paths With Sum ===")

    #       5
    #      / \
    #     4   8
    #    /   / \
    #   11  13  4
    #  / \       \
    # 7   2       1
    root = TreeNode(5,
        TreeNode(4, TreeNode(11, TreeNode(7), TreeNode(2))),
        TreeNode(8, TreeNode(13), TreeNode(4, None, TreeNode(1))))

    print(f"Brute (22):   {brute_force(root, 22)}")   # True (5->4->11->2)
    print(f"Optimal (22): {optimal(root, 22)}")        # True
    print(f"Best (22):    {best(root, 22)}")            # True

    print(f"Brute (26):   {brute_force(root, 26)}")   # True (5->8->13)
    print(f"Optimal (100): {optimal(root, 100)}")      # False
    print(f"Empty:         {optimal(None, 0)}")        # False
