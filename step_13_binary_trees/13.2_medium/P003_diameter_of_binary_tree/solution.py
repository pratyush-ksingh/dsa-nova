"""
Problem: Diameter of Binary Tree (LeetCode #543)
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
# At each node, diameter through it = height(left) + height(right).
# Recurse into children and take max.
# Time: O(n^2) worst case  |  Space: O(h)
# ============================================================
def diameter_brute(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    def height(node):
        if node is None:
            return 0
        return 1 + max(height(node.left), height(node.right))

    through_root = height(root.left) + height(root.right)
    left_dia = diameter_brute(root.left)
    right_dia = diameter_brute(root.right)
    return max(through_root, left_dia, right_dia)


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Bottom-Up Single Pass
# Postorder: compute height, update global max diameter.
# Time: O(n)  |  Space: O(h)
# ============================================================
def diameter_optimal(root: Optional[TreeNode]) -> int:
    max_dia = [0]

    def height(node):
        if node is None:
            return 0
        left_h = height(node.left)
        right_h = height(node.right)
        max_dia[0] = max(max_dia[0], left_h + right_h)
        return 1 + max(left_h, right_h)

    height(root)
    return max_dia[0]


# ============================================================
# APPROACH 3 (ALT): ITERATIVE Postorder with Height Dict
# Time: O(n)  |  Space: O(n)
# ============================================================
def diameter_iterative(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0

    stack = []
    height_map = {}
    curr = root
    last_visited = None
    max_dia = 0

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
            max_dia = max(max_dia, left_h + right_h)
            height_map[top] = 1 + max(left_h, right_h)
            last_visited = top

    return max_dia


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
    print("=== Diameter of Binary Tree ===\n")

    tests = [
        ([1, 2, 3, 4, 5], 3),
        ([1, 2], 1),
        ([1], 0),
        ([], 0),
        ([1, 2, 3, 4, 5, None, None, 6, None, None, 7], 5),
        ([1, 2, None, 3, None, 4], 3),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        brute = diameter_brute(root)
        optimal = diameter_optimal(root)
        iterative = diameter_iterative(root)
        status = "PASS" if brute == expected == optimal == iterative else "FAIL"
        print(f"Tree: {arr}")
        print(f"  Brute: {brute}, Optimal: {optimal}, Iterative: {iterative} "
              f"(expected {expected}) {status}\n")
