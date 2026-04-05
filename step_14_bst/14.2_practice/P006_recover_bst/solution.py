"""
Problem: Recover BST
Difficulty: HARD | XP: 50

Two nodes in a BST have been swapped. Restore the BST without
changing its structure.
"""
from typing import Optional, List
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Inorder collect, sort, reassign
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> Optional[TreeNode]:
    nodes: List[TreeNode] = []

    def inorder(node):
        if not node:
            return
        inorder(node.left)
        nodes.append(node)
        inorder(node.right)

    inorder(root)
    vals = sorted(n.val for n in nodes)
    for node, v in zip(nodes, vals):
        node.val = v
    return root


# ============================================================
# APPROACH 2: OPTIMAL - Inorder violation detection
# Time: O(n)  |  Space: O(h) recursion stack
# ============================================================
def optimal(root: Optional[TreeNode]) -> Optional[TreeNode]:
    first = second = prev = None

    def inorder(node):
        nonlocal first, second, prev
        if not node:
            return
        inorder(node.left)
        if prev and prev.val > node.val:
            if first is None:
                first = prev
            second = node
        prev = node
        inorder(node.right)

    inorder(root)
    first.val, second.val = second.val, first.val
    return root


# ============================================================
# APPROACH 3: BEST - Morris Traversal (O(1) space)
# Time: O(n)  |  Space: O(1)
# ============================================================
# Thread null right pointers to inorder successors. Walk the
# tree without recursion or a stack. Detect violations in the
# same pass and swap at the end.
# Real-life use: fixing corrupted BST index records in database
# engines during recovery mode with minimal memory overhead.
# ============================================================
def best(root: Optional[TreeNode]) -> Optional[TreeNode]:
    first = second = prev = None
    curr = root

    while curr:
        if curr.left is None:
            # Visit curr
            if prev and prev.val > curr.val:
                if first is None:
                    first = prev
                second = curr
            prev = curr
            curr = curr.right
        else:
            # Find inorder predecessor
            pred = curr.left
            while pred.right and pred.right is not curr:
                pred = pred.right
            if pred.right is None:
                pred.right = curr       # thread
                curr = curr.left
            else:
                pred.right = None       # remove thread
                if prev and prev.val > curr.val:
                    if first is None:
                        first = prev
                    second = curr
                prev = curr
                curr = curr.right

    first.val, second.val = second.val, first.val
    return root


# ============================================================
# TEST HELPERS
# ============================================================

def build(arr: List) -> Optional[TreeNode]:
    if not arr:
        return None
    root = TreeNode(arr[0])
    q = deque([root])
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


def inorder_vals(root: Optional[TreeNode]) -> List[int]:
    res = []
    def go(n):
        if not n:
            return
        go(n.left)
        res.append(n.val)
        go(n.right)
    go(root)
    return res


if __name__ == "__main__":
    print("=== Recover BST ===")

    # Test 1: [1,3,null,null,2] - 3 and 1 are swapped
    t1 = build([1, 3, None, None, 2])
    best(t1)
    print(f"Test1 Morris  (expect [1,2,3]): {inorder_vals(t1)}")

    # Test 2: [3,1,4,null,null,2] - 3 and 2 are swapped
    t2 = build([3, 1, 4, None, None, 2])
    best(t2)
    print(f"Test2 Morris  (expect [1,2,3,4]): {inorder_vals(t2)}")

    # Test 3: Optimal approach
    t3 = build([3, 1, 4, None, None, 2])
    optimal(t3)
    print(f"Test3 Optimal (expect [1,2,3,4]): {inorder_vals(t3)}")

    # Test 4: Brute force
    t4 = build([1, 3, None, None, 2])
    brute_force(t4)
    print(f"Test4 Brute   (expect [1,2,3]): {inorder_vals(t4)}")

    # Test 5: Adjacent swap in larger tree
    t5 = build([6, 4, 8, 1, 3])
    best(t5)
    print(f"Test5 Morris  (expect [1,3,4,6,8]): {inorder_vals(t5)}")
