"""
Problem: Consecutive Parent Child
Difficulty: MEDIUM | XP: 25
Source: InterviewBit
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS checking each parent-child
# Time: O(n)  |  Space: O(n) for queue
# Level order traversal, check each parent-child pair.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    count = 0
    queue = deque([root])

    while queue:
        node = queue.popleft()

        if node.left:
            if node.left.val == node.val + 1:
                count += 1
            queue.append(node.left)

        if node.right:
            if node.right.val == node.val + 1:
                count += 1
            queue.append(node.right)

    return count


# ============================================================
# APPROACH 2: OPTIMAL -- DFS (cleaner recursive)
# Time: O(n)  |  Space: O(h) recursion stack
# Recurse through tree, at each node check children.
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    count = 0

    def dfs(node):
        nonlocal count
        if node is None:
            return

        if node.left:
            if node.left.val == node.val + 1:
                count += 1
            dfs(node.left)

        if node.right:
            if node.right.val == node.val + 1:
                count += 1
            dfs(node.right)

    dfs(root)
    return count


# ============================================================
# APPROACH 3: BEST -- DFS returning count (functional style)
# Time: O(n)  |  Space: O(h) recursion stack
# Same DFS but returns count instead of using nonlocal.
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    def dfs(node):
        if node is None:
            return 0

        count = 0

        if node.left:
            if node.left.val == node.val + 1:
                count += 1
            count += dfs(node.left)

        if node.right:
            if node.right.val == node.val + 1:
                count += 1
            count += dfs(node.right)

        return count

    return dfs(root)


if __name__ == "__main__":
    print("=== Consecutive Parent Child ===")

    #       1
    #      / \
    #     2   3
    #    / \   \
    #   4   5   4
    # Consecutive pairs: (1,2), (3,4) -> count = 2
    root = TreeNode(1,
        TreeNode(2, TreeNode(4), TreeNode(5)),
        TreeNode(3, None, TreeNode(4)))

    print(f"Brute:   {brute_force(root)}")   # 2
    print(f"Optimal: {optimal(root)}")        # 2
    print(f"Best:    {best(root)}")            # 2

    # All consecutive
    #     1
    #    / \
    #   2   2
    root2 = TreeNode(1, TreeNode(2), TreeNode(2))
    print(f"All:     {best(root2)}")           # 2

    # No consecutive
    root3 = TreeNode(1, TreeNode(5), TreeNode(10))
    print(f"None:    {best(root3)}")           # 0

    print(f"Empty:   {best(None)}")            # 0
