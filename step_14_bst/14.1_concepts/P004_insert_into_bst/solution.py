"""
Problem: Insert into a Binary Search Tree
Difficulty: MEDIUM | XP: 25
LeetCode: #701

Given the root of a BST and a value to insert, insert the value into the BST
and return the root of the modified tree. The input is guaranteed not to contain
a duplicate value. There may be multiple valid ways to insert; return any valid BST.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative traversal to find insertion point
# Time: O(h)  |  Space: O(h) -- no actual "brute"; iterative is the straightforward approach
# ============================================================
def insert_into_bst_brute(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    """
    Iteratively walk the BST: go left if val < current, right if val > current.
    When we reach a null slot, create the new node there.
    This is the "obvious" iterative solution -- track the parent to attach the new node.
    """
    new_node = TreeNode(val)

    if root is None:
        return new_node

    current = root
    while True:
        if val < current.val:
            if current.left is None:
                current.left = new_node
                break
            current = current.left
        else:
            if current.right is None:
                current.right = new_node
                break
            current = current.right

    return root


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive insert
# Time: O(h)  |  Space: O(h) -- recursion stack depth = tree height
# ============================================================
def insert_into_bst_optimal(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    """
    Recursively traverse BST. At the null position, return a new node.
    The recursive structure elegantly wires up the parent link via return values.
    """
    if root is None:
        return TreeNode(val)

    if val < root.val:
        root.left = insert_into_bst_optimal(root.left, val)
    else:
        root.right = insert_into_bst_optimal(root.right, val)

    return root


# ============================================================
# APPROACH 3: BEST -- Iterative with parent tracking, O(1) extra space
# Time: O(h)  |  Space: O(1) -- no recursion stack, only two pointers
# ============================================================
def insert_into_bst_best(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    """
    Iterative approach with explicit parent pointer.
    Tracks `parent` and `is_left` to directly link the new node.
    O(1) extra space (no recursion overhead).
    """
    new_node = TreeNode(val)

    if root is None:
        return new_node

    parent = None
    current = root
    is_left = False

    while current is not None:
        parent = current
        if val < current.val:
            current = current.left
            is_left = True
        else:
            current = current.right
            is_left = False

    # Parent is the node just before the null slot
    if is_left:
        parent.left = new_node
    else:
        parent.right = new_node

    return root


# ============================================================
# Helpers
# ============================================================
def build_bst(values: List[int]) -> Optional[TreeNode]:
    """Build BST by inserting values one by one."""
    root = None
    for v in values:
        root = insert_into_bst_optimal(root, v)
    return root


def inorder_of(root: Optional[TreeNode]) -> List[int]:
    result = []
    def dfs(node):
        if node:
            dfs(node.left)
            result.append(node.val)
            dfs(node.right)
    dfs(root)
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
    print("=== Insert into BST ===\n")

    # LC #701: root=[4,2,7,1,3], val=5  => insert 5 -> [4,2,7,1,3,5]
    tests = [
        ([4, 2, 7, 1, 3], 5, [1, 2, 3, 4, 5, 7]),   # LC example
        ([40, 20, 60, 10, 30, 50, 70], 25, [10, 20, 25, 30, 40, 50, 60, 70]),
        ([], 5, [5]),                                 # empty tree
        ([1], 2, [1, 2]),                             # single node, insert right
        ([5], 3, [3, 5]),                             # single node, insert left
    ]

    for tree_arr, val, expected_inorder in tests:
        r1 = insert_into_bst_brute(build_tree(tree_arr), val)
        r2 = insert_into_bst_optimal(build_tree(tree_arr), val)
        r3 = insert_into_bst_best(build_tree(tree_arr), val)

        io1 = inorder_of(r1)
        io2 = inorder_of(r2)
        io3 = inorder_of(r3)

        ok = io1 == io2 == io3 == expected_inorder
        print(f"Tree={tree_arr}, insert={val}")
        print(f"  Brute:   {io1}")
        print(f"  Optimal: {io2}")
        print(f"  Best:    {io3}")
        print(f"  Expected:{expected_inorder}")
        print(f"  {'PASS' if ok else 'FAIL'}\n")
