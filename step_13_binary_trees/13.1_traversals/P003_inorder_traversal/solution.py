"""
Problem: Inorder Traversal (LeetCode #94)
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
# APPROACH 1: RECURSIVE (L - Root - R)
# Time: O(n)  |  Space: O(h)
# ============================================================
def inorder_recursive(root: Optional[TreeNode]) -> List[int]:
    result = []

    def helper(node):
        if node is None:
            return
        helper(node.left)
        result.append(node.val)
        helper(node.right)

    helper(root)
    return result


# ============================================================
# APPROACH 2: ITERATIVE with explicit stack
# Time: O(n)  |  Space: O(h)
# ============================================================
def inorder_iterative(root: Optional[TreeNode]) -> List[int]:
    result = []
    stack = []
    current = root

    while current or stack:
        # Push all left children
        while current:
            stack.append(current)
            current = current.left
        # Pop, visit, go right
        current = stack.pop()
        result.append(current.val)
        current = current.right

    return result


# ============================================================
# APPROACH 3: MORRIS TRAVERSAL (O(1) space)
# Time: O(n)  |  Space: O(1)
# ============================================================
def inorder_morris(root: Optional[TreeNode]) -> List[int]:
    result = []
    current = root

    while current:
        if current.left is None:
            result.append(current.val)
            current = current.right
        else:
            # Find inorder predecessor
            predecessor = current.left
            while predecessor.right and predecessor.right is not current:
                predecessor = predecessor.right

            if predecessor.right is None:
                # Create thread
                predecessor.right = current
                current = current.left
            else:
                # Remove thread, visit, go right
                predecessor.right = None
                result.append(current.val)
                current = current.right

    return result


# Helper: build tree from level-order array
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
    print("=== Inorder Traversal ===\n")

    tests = [
        ([], []),
        ([1], [1]),
        ([1, None, 2, 3], [1, 3, 2]),
        ([4, 2, 6, None, 5, 7, 8], [2, 5, 4, 7, 6, 8]),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        rec = inorder_recursive(root)
        itr = inorder_iterative(root)
        mor = inorder_morris(root)
        print(f"Tree: {arr}")
        print(f"  Recursive: {rec}")
        print(f"  Iterative: {itr}")
        print(f"  Morris:    {mor}")
        print(f"  Expected:  {expected}")
        status = "PASS" if rec == expected == itr == mor else "FAIL"
        print(f"  {status}\n")
