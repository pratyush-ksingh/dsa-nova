"""
Problem: Path Sum (LeetCode #112)
Difficulty: EASY | XP: 10
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1 & 3: RECURSIVE DFS -- Subtract Target
# At each node, subtract val from targetSum.
# At a leaf, check if remainder == 0.
# Time: O(n)  |  Space: O(h)
# ============================================================
def has_path_sum_recursive(root: Optional[TreeNode], target_sum: int) -> bool:
    if root is None:
        return False
    target_sum -= root.val
    if root.left is None and root.right is None:
        return target_sum == 0
    return (has_path_sum_recursive(root.left, target_sum) or
            has_path_sum_recursive(root.right, target_sum))


# ============================================================
# APPROACH 2: ITERATIVE BFS
# Queue stores (node, remaining_sum). Check at leaves.
# Time: O(n)  |  Space: O(w)
# ============================================================
def has_path_sum_bfs(root: Optional[TreeNode], target_sum: int) -> bool:
    if root is None:
        return False

    queue = deque([(root, target_sum - root.val)])

    while queue:
        node, remaining = queue.popleft()
        if node.left is None and node.right is None and remaining == 0:
            return True
        if node.left:
            queue.append((node.left, remaining - node.left.val))
        if node.right:
            queue.append((node.right, remaining - node.right.val))

    return False


# ============================================================
# APPROACH 3 (ALT): ITERATIVE DFS with Stack
# Same logic as BFS but with stack (DFS order).
# Time: O(n)  |  Space: O(h)
# ============================================================
def has_path_sum_iterative_dfs(root: Optional[TreeNode], target_sum: int) -> bool:
    if root is None:
        return False

    stack = [(root, target_sum - root.val)]

    while stack:
        node, remaining = stack.pop()
        if node.left is None and node.right is None and remaining == 0:
            return True
        if node.right:
            stack.append((node.right, remaining - node.right.val))
        if node.left:
            stack.append((node.left, remaining - node.left.val))

    return False


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
    print("=== Path Sum ===\n")

    tests = [
        ([5, 4, 8, 11, None, 13, 4, 7, 2, None, None, None, 1], 22, True),
        ([1, 2, 3], 5, False),
        ([1, 2], 1, False),
        ([], 0, False),
        ([1], 1, True),
        ([1, 2, None, 3, None, 4, None, 5], 15, True),
        ([-2, None, -3], -5, True),
    ]

    for arr, target, expected in tests:
        root = build_tree(arr)
        rec = has_path_sum_recursive(root, target)
        bfs = has_path_sum_bfs(root, target)
        dfs = has_path_sum_iterative_dfs(root, target)
        status = "PASS" if rec == expected == bfs == dfs else "FAIL"
        print(f"Tree: {arr}, target={target}")
        print(f"  Recursive: {rec}, BFS: {bfs}, DFS: {dfs} "
              f"(expected {expected}) {status}\n")
