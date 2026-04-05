"""
Sum Root to Leaf Numbers (LeetCode #129)

Each root-to-leaf path forms a number. Return the total sum of all numbers.
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Collect all paths as strings
# Time: O(N * H)  |  Space: O(N * H)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    paths = []

    def collect(node, path):
        if not node:
            return
        path += str(node.val)
        if not node.left and not node.right:
            paths.append(path)
            return
        collect(node.left, path)
        collect(node.right, path)

    collect(root, "")
    return sum(int(p) for p in paths)


# ============================================================
# APPROACH 2: OPTIMAL -- Top-down DFS with running number
# Time: O(N)  |  Space: O(H)
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    def dfs(node, current_num):
        if not node:
            return 0
        current_num = current_num * 10 + node.val
        # Leaf: return the complete number
        if not node.left and not node.right:
            return current_num
        # Internal: sum of both subtrees
        return dfs(node.left, current_num) + dfs(node.right, current_num)

    return dfs(root, 0)


# ============================================================
# APPROACH 3: BEST -- Iterative BFS with running numbers
# Time: O(N)  |  Space: O(W)
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    if not root:
        return 0

    total = 0
    queue = deque([(root, root.val)])

    while queue:
        node, num = queue.popleft()

        if not node.left and not node.right:
            total += num
            continue

        if node.left:
            queue.append((node.left, num * 10 + node.left.val))
        if node.right:
            queue.append((node.right, num * 10 + node.right.val))

    return total


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    #      4
    #     / \
    #    9   0
    #   / \
    #  5   1
    root = TreeNode(4)
    root.left = TreeNode(9)
    root.right = TreeNode(0)
    root.left.left = TreeNode(5)
    root.left.right = TreeNode(1)

    print("=== Sum Root to Leaf Numbers ===")
    print(f"Brute:   {brute_force(root)}")   # 1026
    print(f"Optimal: {optimal(root)}")        # 1026
    print(f"Best:    {best(root)}")           # 1026
    # Paths: 495 + 491 + 40 = 1026
