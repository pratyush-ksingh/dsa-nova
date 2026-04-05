"""
Problem: Postorder Traversal (LeetCode #145)
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
# APPROACH 1: RECURSIVE (L - R - Root)
# Time: O(n)  |  Space: O(h)
# ============================================================
def postorder_recursive(root: Optional[TreeNode]) -> List[int]:
    result = []

    def helper(node):
        if node is None:
            return
        helper(node.left)
        helper(node.right)
        result.append(node.val)

    helper(root)
    return result


# ============================================================
# APPROACH 2: ITERATIVE -- Reverse Trick
# Modified preorder (Root-R-L) then reverse = L-R-Root
# Time: O(n)  |  Space: O(n)
# ============================================================
def postorder_reverse(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    stack = [root]

    while stack:
        node = stack.pop()
        result.append(node.val)
        # Push left first, then right (so right pops first -> Root-R-L)
        if node.left:
            stack.append(node.left)
        if node.right:
            stack.append(node.right)

    return result[::-1]  # Reverse: Root-R-L -> L-R-Root


# ============================================================
# APPROACH 3: ITERATIVE -- Single stack with prev pointer
# Time: O(n)  |  Space: O(h)
# ============================================================
def postorder_prev(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    stack = [root]
    prev = None

    while stack:
        curr = stack[-1]

        # Going down
        if prev is None or prev.left is curr or prev.right is curr:
            if curr.left:
                stack.append(curr.left)
            elif curr.right:
                stack.append(curr.right)
            else:
                result.append(stack.pop().val)
        # Coming up from left
        elif curr.left is prev:
            if curr.right:
                stack.append(curr.right)
            else:
                result.append(stack.pop().val)
        # Coming up from right
        else:
            result.append(stack.pop().val)

        prev = curr

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
    print("=== Postorder Traversal ===\n")

    tests = [
        ([], []),
        ([1], [1]),
        ([1, None, 2, 3], [3, 2, 1]),
        ([1, 2, 3, 4, 5, None, 6], [4, 5, 2, 6, 3, 1]),
    ]

    for arr, expected in tests:
        root = build_tree(arr)
        rec = postorder_recursive(root)
        rev = postorder_reverse(root)
        prv = postorder_prev(root)
        print(f"Tree: {arr}")
        print(f"  Recursive:     {rec}")
        print(f"  Reverse trick: {rev}")
        print(f"  Prev pointer:  {prv}")
        print(f"  Expected:      {expected}")
        status = "PASS" if rec == expected == rev == prv else "FAIL"
        print(f"  {status}\n")
