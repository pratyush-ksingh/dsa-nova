"""
Problem: Check if Tree is Balanced (LeetCode #110)
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
# APPROACH 1: BRUTE FORCE -- Top-Down
# At each node, compute height of both subtrees, check balance,
# then recurse into children.
# Time: O(n^2) worst case  |  Space: O(h)
# ============================================================
def is_balanced_brute(root: Optional[TreeNode]) -> bool:
    if root is None:
        return True

    def height(node):
        if node is None:
            return 0
        return 1 + max(height(node.left), height(node.right))

    left_h = height(root.left)
    right_h = height(root.right)
    if abs(left_h - right_h) > 1:
        return False
    return is_balanced_brute(root.left) and is_balanced_brute(root.right)


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Bottom-Up Single Pass
# Postorder: compute height and check balance simultaneously.
# Return -1 as sentinel for "unbalanced".
# Time: O(n)  |  Space: O(h)
# ============================================================
def is_balanced_optimal(root: Optional[TreeNode]) -> bool:
    def check(node):
        if node is None:
            return 0

        left_h = check(node.left)
        if left_h == -1:
            return -1

        right_h = check(node.right)
        if right_h == -1:
            return -1

        if abs(left_h - right_h) > 1:
            return -1

        return 1 + max(left_h, right_h)

    return check(root) != -1


# ============================================================
# APPROACH 3 (ALT): ITERATIVE Postorder with Height Dict
# Explicit stack postorder + dict storing computed heights.
# Time: O(n)  |  Space: O(n)
# ============================================================
def is_balanced_iterative(root: Optional[TreeNode]) -> bool:
    if root is None:
        return True

    stack = []
    height_map = {}
    curr = root
    last_visited = None

    while curr or stack:
        while curr:
            stack.append(curr)
            curr = curr.left
        top = stack[-1]
        if top.right and top.right != last_visited:
            curr = top.right
        else:
            stack.pop()
            left_h = height_map.get(top.left, 0)
            right_h = height_map.get(top.right, 0)
            if abs(left_h - right_h) > 1:
                return False
            height_map[top] = 1 + max(left_h, right_h)
            last_visited = top

    return True


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
    print("=== Check if Tree is Balanced ===\n")

    tests = [
        ([3, 9, 20, None, None, 15, 7], True),
        ([1, 2, 2, 3, 3, None, None, 4, 4], False),
        ([], True),
        ([1], True),
        ([1, 2, None, 3], False),
        ([1, 2, 3, 4, 5, 6, 7], True),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        brute = is_balanced_brute(root)
        optimal = is_balanced_optimal(root)
        iterative = is_balanced_iterative(root)
        status = "PASS" if brute == expected == optimal == iterative else "FAIL"
        print(f"Tree: {arr}")
        print(f"  Brute: {brute}, Optimal: {optimal}, Iterative: {iterative} "
              f"(expected {expected}) {status}\n")
