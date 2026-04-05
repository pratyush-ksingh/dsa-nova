"""
Cousins in Binary Tree (LeetCode #993)

Two nodes are cousins if they have the same depth but different parents.
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Two separate DFS calls
# Time: O(N)  |  Space: O(H)
# ============================================================
def brute_force(root: Optional[TreeNode], x: int, y: int) -> bool:
    def find(node, parent, depth, target):
        if not node:
            return None
        if node.val == target:
            return (depth, parent)
        left = find(node.left, node.val, depth + 1, target)
        if left:
            return left
        return find(node.right, node.val, depth + 1, target)

    info_x = find(root, -1, 0, x)
    info_y = find(root, -1, 0, y)

    if not info_x or not info_y:
        return False
    return info_x[0] == info_y[0] and info_x[1] != info_y[1]


# ============================================================
# APPROACH 2: OPTIMAL -- Single DFS for both nodes
# Time: O(N)  |  Space: O(H)
# ============================================================
def optimal(root: Optional[TreeNode], x: int, y: int) -> bool:
    result = {}  # val -> (depth, parent_val)

    def dfs(node, parent_val, depth):
        if not node:
            return
        if node.val == x or node.val == y:
            result[node.val] = (depth, parent_val)
        dfs(node.left, node.val, depth + 1)
        dfs(node.right, node.val, depth + 1)

    dfs(root, -1, 0)
    if x not in result or y not in result:
        return False
    return result[x][0] == result[y][0] and result[x][1] != result[y][1]


# ============================================================
# APPROACH 3: BEST -- BFS level-order with sibling check
# Time: O(N)  |  Space: O(W)
# ============================================================
def best(root: Optional[TreeNode], x: int, y: int) -> bool:
    if not root:
        return False

    queue = deque([root])

    while queue:
        size = len(queue)
        found_x = found_y = False

        for _ in range(size):
            node = queue.popleft()

            # Sibling check
            if node.left and node.right:
                if ({node.left.val, node.right.val} == {x, y}):
                    return False  # siblings

            if node.val == x:
                found_x = True
            if node.val == y:
                found_y = True

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        if found_x and found_y:
            return True   # same level, not siblings
        if found_x or found_y:
            return False  # different levels

    return False


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    #      1
    #     / \
    #    2   3
    #     \   \
    #      4   5
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.right = TreeNode(4)
    root.right.right = TreeNode(5)

    print("=== Cousins in Binary Tree ===")
    print(f"Brute (4,5):   {brute_force(root, 4, 5)}")   # True
    print(f"Optimal (4,5): {optimal(root, 4, 5)}")         # True
    print(f"Best (4,5):    {best(root, 4, 5)}")            # True

    print(f"Brute (2,3):   {brute_force(root, 2, 3)}")   # False (siblings)
    print(f"Optimal (2,3): {optimal(root, 2, 3)}")         # False
    print(f"Best (2,3):    {best(root, 2, 3)}")            # False
