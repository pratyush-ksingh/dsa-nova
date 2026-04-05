"""
Problem: Zig Zag Traversal
Difficulty: MEDIUM | XP: 25

Return the zigzag level order traversal of a binary tree.
Level 0: left→right. Level 1: right→left. Level 2: left→right. Etc.
Real-life use: Tree serialization, network topology visualization, game trees.
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


# ============================================================
# APPROACH 1: BRUTE FORCE
# Standard BFS; reverse every other level afterwards.
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[List[int]]:
    if not root:
        return []
    result = []
    q: deque = deque([root])
    level = 0
    while q:
        size = len(q)
        row = []
        for _ in range(size):
            node = q.popleft()
            row.append(node.val)
            if node.left:  q.append(node.left)
            if node.right: q.append(node.right)
        if level % 2 == 1:
            row.reverse()
        result.append(row)
        level += 1
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# BFS with deque per level: append to front or back based on direction.
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[List[int]]:
    if not root:
        return []
    result = []
    q: deque = deque([root])
    left_to_right = True
    while q:
        size = len(q)
        row: deque = deque()
        for _ in range(size):
            node = q.popleft()
            if left_to_right:
                row.append(node.val)
            else:
                row.appendleft(node.val)
            if node.left:  q.append(node.left)
            if node.right: q.append(node.right)
        result.append(list(row))
        left_to_right = not left_to_right
    return result


# ============================================================
# APPROACH 3: BEST
# Two-stack approach: alternate push order each level.
# Clean and avoids extra deque — common interview approach.
# Time: O(N)  |  Space: O(N)
# ============================================================
def best(root: Optional[TreeNode]) -> List[List[int]]:
    if not root:
        return []
    result = []
    curr = [root]
    left_to_right = True
    while curr:
        nxt = []
        row = []
        for node in curr:
            row.append(node.val)
            if left_to_right:
                if node.left:  nxt.append(node.left)
                if node.right: nxt.append(node.right)
            else:
                if node.right: nxt.append(node.right)
                if node.left:  nxt.append(node.left)
        # For next level, we reverse the traversal direction
        # by reversing the nxt list (since we push to list, not stack)
        result.append(row)
        curr = list(reversed(nxt))
        left_to_right = not left_to_right
    return result


if __name__ == "__main__":
    print("=== Zig Zag Traversal ===")

    trees = [
        [3, 9, 20, None, None, 15, 7],
        [1],
        [1, 2, 3, 4, 5],
    ]
    for arr in trees:
        root = build_tree(arr)
        print(f"\nTree: {arr}")
        print(f"  Brute  : {brute_force(root)}")
        print(f"  Optimal: {optimal(root)}")
        print(f"  Best   : {best(root)}")
