"""
Problem: Preorder Traversal (LeetCode #144)
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
# APPROACH 1: RECURSIVE (Root - L - R)
# Time: O(n)  |  Space: O(h)
# ============================================================
def preorder_recursive(root: Optional[TreeNode]) -> List[int]:
    result = []

    def helper(node):
        if node is None:
            return
        result.append(node.val)
        helper(node.left)
        helper(node.right)

    helper(root)
    return result


# ============================================================
# APPROACH 2: ITERATIVE with stack
# Time: O(n)  |  Space: O(h)
# ============================================================
def preorder_iterative(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    stack = [root]

    while stack:
        node = stack.pop()
        result.append(node.val)
        # Push right first so left is processed first
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)

    return result


# ============================================================
# APPROACH 3: MORRIS PREORDER (O(1) space)
# Time: O(n)  |  Space: O(1)
# ============================================================
def preorder_morris(root: Optional[TreeNode]) -> List[int]:
    result = []
    current = root

    while current:
        if current.left is None:
            result.append(current.val)
            current = current.right
        else:
            predecessor = current.left
            while predecessor.right and predecessor.right is not current:
                predecessor = predecessor.right

            if predecessor.right is None:
                # Visit NOW (preorder: process before going left)
                result.append(current.val)
                predecessor.right = current
                current = current.left
            else:
                # Remove thread, go right
                predecessor.right = None
                current = current.right

    return result


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
    print("=== Preorder Traversal ===\n")

    tests = [
        ([], []),
        ([1], [1]),
        ([1, None, 2, 3], [1, 2, 3]),
        ([1, 2, 3, 4, 5, None, 6], [1, 2, 4, 5, 3, 6]),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        rec = preorder_recursive(root)
        itr = preorder_iterative(root)
        mor = preorder_morris(root)
        print(f"Tree: {arr}")
        print(f"  Recursive: {rec}")
        print(f"  Iterative: {itr}")
        print(f"  Morris:    {mor}")
        print(f"  Expected:  {expected}")
        status = "PASS" if rec == expected == itr == mor else "FAIL"
        print(f"  {status}\n")
