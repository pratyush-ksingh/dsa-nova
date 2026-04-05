"""
Problem: Root to Node Path in Binary Tree
Difficulty: MEDIUM | XP: 25
Source: Striver A2Z / GFG / Custom

Given the root of a binary tree and a target value, find and return the path
from root to the node containing that value. Return an empty list if not found.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS with parent tracking
# Time: O(n)  |  Space: O(n) -- stores all parent pointers
# ============================================================
def root_to_node_brute(root: Optional[TreeNode], target: int) -> List[int]:
    """
    BFS to find the target node while recording each node's parent.
    Once found, backtrack from target to root via parent map to reconstruct path.
    """
    if root is None:
        return []

    parent: dict = {root: None}
    target_node = None
    queue = deque([root])

    while queue:
        node = queue.popleft()
        if node.val == target:
            target_node = node
            break
        if node.left:
            parent[node.left] = node
            queue.append(node.left)
        if node.right:
            parent[node.right] = node
            queue.append(node.right)

    if target_node is None:
        return []

    # Backtrack from target to root
    path = []
    curr = target_node
    while curr is not None:
        path.append(curr.val)
        curr = parent[curr]

    path.reverse()
    return path


# ============================================================
# APPROACH 2: OPTIMAL -- DFS recursive with backtracking
# Time: O(n)  |  Space: O(h) -- only path stored on stack
# ============================================================
def root_to_node_optimal(root: Optional[TreeNode], target: int) -> List[int]:
    """
    DFS recursion. Add node to path; if it's the target, return True.
    On backtrack, remove node from path (backtracking).
    O(h) space since only the current path is on the recursion stack.
    """
    path: List[int] = []

    def dfs(node: Optional[TreeNode]) -> bool:
        if node is None:
            return False
        path.append(node.val)
        if node.val == target:
            return True
        if dfs(node.left) or dfs(node.right):
            return True
        path.pop()  # backtrack
        return False

    dfs(root)
    return path


# ============================================================
# APPROACH 3: BEST -- Iterative DFS with explicit stack
# Time: O(n)  |  Space: O(h) -- no recursion overhead
# ============================================================
def root_to_node_best(root: Optional[TreeNode], target: int) -> List[int]:
    """
    Iterative DFS using an explicit stack. Each stack entry is (node, path_so_far).
    When we find the target, return the path immediately.
    Avoids Python recursion limit for very deep trees.
    """
    if root is None:
        return []

    # Stack stores (node, path_to_this_node)
    stack: List[tuple] = [(root, [root.val])]

    while stack:
        node, path = stack.pop()
        if node.val == target:
            return path
        # Push right first so left is processed first (DFS order)
        if node.right:
            stack.append((node.right, path + [node.right.val]))
        if node.left:
            stack.append((node.left, path + [node.left.val]))

    return []  # target not found


# ============================================================
# Helpers
# ============================================================
def build_tree(arr: List[Optional[int]]) -> Optional[TreeNode]:
    if not arr or arr[0] is None:
        return None
    root = TreeNode(arr[0])
    queue = deque([root])
    i = 1
    while queue and i < len(arr):
        curr = queue.popleft()
        if i < len(arr) and arr[i] is not None:
            curr.left = TreeNode(arr[i])
            queue.append(curr.left)
        i += 1
        if i < len(arr) and arr[i] is not None:
            curr.right = TreeNode(arr[i])
            queue.append(curr.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Root to Node Path ===\n")

    # Tree:    1
    #        /   \
    #       2     3
    #      / \   / \
    #     4   5 6   7
    tree_arr = [1, 2, 3, 4, 5, 6, 7]

    tests = [
        (tree_arr, 5, [1, 2, 5]),
        (tree_arr, 7, [1, 3, 7]),
        (tree_arr, 1, [1]),
        (tree_arr, 4, [1, 2, 4]),
        (tree_arr, 9, []),       # not in tree
        ([], 1, []),             # empty tree
    ]

    for arr, target, expected in tests:
        root = build_tree(arr)
        r1 = root_to_node_brute(root, target)
        r2 = root_to_node_optimal(root, target)
        r3 = root_to_node_best(root, target)
        ok = r1 == r2 == r3 == expected
        print(f"Tree={arr}, target={target}")
        print(f"  Brute:   {r1}")
        print(f"  Optimal: {r2}")
        print(f"  Best:    {r3}")
        print(f"  Expected:{expected}")
        print(f"  {'PASS' if ok else 'FAIL'}\n")
