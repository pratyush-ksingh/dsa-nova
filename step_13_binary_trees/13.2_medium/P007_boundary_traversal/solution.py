"""
Problem: Boundary Traversal
Difficulty: MEDIUM | XP: 25

Return the boundary of a binary tree in anti-clockwise order:
- Left boundary (top to bottom, excluding leaf)
- All leaves (left to right)
- Right boundary (bottom to top, excluding leaf and root)
Real-life use: Polygon boundary extraction, image contour tracing, map rendering.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val: int = 0,
                 left: 'Optional[TreeNode]' = None,
                 right: 'Optional[TreeNode]' = None):
        self.val = val
        self.left = left
        self.right = right


def build_tree(arr: List[Optional[int]]) -> Optional[TreeNode]:
    if not arr or arr[0] is None:
        return None
    root = TreeNode(arr[0])
    q: deque = deque([root])
    i = 1
    while q and i < len(arr):
        node = q.popleft()
        if i < len(arr) and arr[i] is not None:
            node.left = TreeNode(arr[i])
            q.append(node.left)
        i += 1
        if i < len(arr) and arr[i] is not None:
            node.right = TreeNode(arr[i])
            q.append(node.right)
        i += 1
    return root


def is_leaf(node: TreeNode) -> bool:
    return node.left is None and node.right is None


# ============================================================
# APPROACH 1: BRUTE FORCE
# Three separate recursive passes: left boundary, leaves, right boundary.
# Time: O(N)  |  Space: O(H) recursion
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    if not is_leaf(root):
        result.append(root.val)

    def add_left(node):
        if not node or is_leaf(node):
            return
        result.append(node.val)
        if node.left:
            add_left(node.left)
        else:
            add_left(node.right)

    def add_leaves(node):
        if not node:
            return
        if is_leaf(node):
            result.append(node.val)
            return
        add_leaves(node.left)
        add_leaves(node.right)

    def add_right(node):
        if not node or is_leaf(node):
            return
        if node.right:
            add_right(node.right)
        else:
            add_right(node.left)
        result.append(node.val)  # post-order = bottom-up

    add_left(root.left)
    add_leaves(root)
    add_right(root.right)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Iterative left/right boundary + recursive leaves.
# Time: O(N)  |  Space: O(H)
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    if not is_leaf(root):
        result.append(root.val)

    # Left boundary iterative
    node = root.left
    while node and not is_leaf(node):
        result.append(node.val)
        node = node.left if node.left else node.right

    # Leaves (recursive)
    def add_leaves(n):
        if not n:
            return
        if is_leaf(n):
            result.append(n.val)
            return
        add_leaves(n.left)
        add_leaves(n.right)

    add_leaves(root)

    # Right boundary iterative (collect then reverse)
    right_bound = []
    node = root.right
    while node and not is_leaf(node):
        right_bound.append(node.val)
        node = node.right if node.right else node.left
    result.extend(reversed(right_bound))

    return result


# ============================================================
# APPROACH 3: BEST
# Single DFS: classify each node and add in correct order.
# LEFT_BOUND=0, RIGHT_BOUND=1, NEITHER=2
# Time: O(N)  |  Space: O(N) output + O(H) recursion
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    LEFT_BOUND, RIGHT_BOUND, NEITHER = 0, 1, 2
    result = []

    def dfs(node: Optional[TreeNode], flag: int) -> None:
        if not node:
            return
        leaf = is_leaf(node)
        is_lb = (flag == LEFT_BOUND)
        is_rb = (flag == RIGHT_BOUND)

        if is_lb or leaf:
            result.append(node.val)

        if is_lb:
            l_flag = LEFT_BOUND
            r_flag = LEFT_BOUND if node.left is None else NEITHER
        elif is_rb:
            l_flag = RIGHT_BOUND if node.right is None else NEITHER
            r_flag = RIGHT_BOUND
        else:
            l_flag = r_flag = NEITHER

        dfs(node.left,  l_flag)
        dfs(node.right, r_flag)

        if is_rb and not leaf:
            result.append(node.val)  # post-order for right boundary

    if not root:
        return result
    dfs(root, LEFT_BOUND)
    return result


if __name__ == "__main__":
    print("=== Boundary Traversal ===")

    trees = [
        [1, None, 2, 3, 4],
        [1, 2, 3, 4, 5, 6, None, None, None, 7, 8, 9, 10],
        [1],
    ]
    for arr in trees:
        root = build_tree(arr)
        print(f"\nTree: {arr}")
        print(f"  Brute  : {brute_force(root)}")
        print(f"  Optimal: {optimal(root)}")
        print(f"  Best   : {best(root)}")
