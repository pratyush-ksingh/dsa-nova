"""
Problem: Delete Node in BST (LeetCode 450)
Difficulty: MEDIUM | XP: 25

Given a root of a BST and a key, delete the node with that key and return
the (possibly new) root. The resulting tree must remain a valid BST.

Three deletion cases:
  1. Node is a leaf              -> simply remove it
  2. Node has only one child     -> replace with that child
  3. Node has two children       -> replace value with inorder successor
     (smallest in right subtree), then delete that successor
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


def build_bst(values: List[Optional[int]]) -> Optional[TreeNode]:
    """Build a BST by inserting values one by one (preserves BST property)."""
    def insert(root, v):
        if root is None:
            return TreeNode(v)
        if v < root.val:
            root.left = insert(root.left, v)
        elif v > root.val:
            root.right = insert(root.right, v)
        return root

    root = None
    for v in values:
        if v is not None:
            root = insert(root, v)
    return root


def inorder(root: Optional[TreeNode]) -> List[int]:
    """Return sorted inorder traversal (validates BST property)."""
    result = []
    def _inorder(node):
        if node:
            _inorder(node.left)
            result.append(node.val)
            _inorder(node.right)
    _inorder(root)
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE — Inorder to array, remove, rebuild
# Time: O(n)  |  Space: O(n)
# ============================================================
def delete_brute(root: Optional[TreeNode], key: int) -> Optional[TreeNode]:
    """
    Intuition: Extract all BST values via inorder traversal into a sorted
    list, remove the key from the list, then rebuild the BST from the
    sorted array (insert from the middle for balance, or just re-insert).
    Simple but destroys tree structure and uses O(n) space.
    """
    # Step 1: collect all values in sorted order
    vals: List[int] = []

    def collect(node: Optional[TreeNode]) -> None:
        if node is None:
            return
        collect(node.left)
        vals.append(node.val)
        collect(node.right)

    collect(root)

    # Step 2: remove the key (if present)
    if key not in vals:
        return root
    vals.remove(key)

    # Step 3: rebuild BST by inserting sorted values (slightly unbalanced but valid)
    def insert(r: Optional[TreeNode], v: int) -> TreeNode:
        if r is None:
            return TreeNode(v)
        if v < r.val:
            r.left = insert(r.left, v)
        else:
            r.right = insert(r.right, v)
        return r

    new_root: Optional[TreeNode] = None
    # Insert from the middle to keep tree roughly balanced
    def build_from_sorted(lo: int, hi: int) -> None:
        nonlocal new_root
        if lo > hi:
            return
        mid = (lo + hi) // 2
        new_root = insert(new_root, vals[mid])
        build_from_sorted(lo, mid - 1)
        build_from_sorted(mid + 1, hi)

    build_from_sorted(0, len(vals) - 1)
    return new_root


# ============================================================
# APPROACH 2: OPTIMAL — Recursive 3-case BST deletion
# Time: O(h)  |  Space: O(h) call stack  (h = tree height)
# ============================================================
def delete_optimal(root: Optional[TreeNode], key: int) -> Optional[TreeNode]:
    """
    Intuition: Use BST ordering to navigate to the node. Once found, handle
    the three classic cases:
      - Leaf: return None (parent will disconnect it)
      - One child: return that child (parent adopts it)
      - Two children: find the inorder successor (leftmost of right subtree),
        copy its value to current node, then delete the successor recursively
        (which is guaranteed to be in case 1 or 2).
    """
    if root is None:
        return None

    if key < root.val:
        root.left = delete_optimal(root.left, key)
    elif key > root.val:
        root.right = delete_optimal(root.right, key)
    else:
        # Found the node to delete
        if root.left is None:
            return root.right          # case 1 (leaf) or case 2 (right child only)
        if root.right is None:
            return root.left           # case 2 (left child only)
        # Case 3: two children — find inorder successor (min of right subtree)
        successor = root.right
        while successor.left:
            successor = successor.left
        root.val = successor.val       # overwrite value
        root.right = delete_optimal(root.right, successor.val)  # delete successor

    return root


# ============================================================
# APPROACH 3: BEST — Iterative 3-case BST deletion (O(1) extra space)
# Time: O(h)  |  Space: O(1)
# ============================================================
def delete_best(root: Optional[TreeNode], key: int) -> Optional[TreeNode]:
    """
    Intuition: Same 3-case logic as Approach 2 but implemented iteratively
    to avoid recursion stack overhead. We track both the node to delete and
    its parent so we can rewire the parent pointer directly.

    When two children: instead of copying the successor value, we physically
    attach the left subtree of the deleted node as the leftmost child of the
    right subtree — which also satisfies the BST property.
    """
    def attach_left_to_rightmost(node: TreeNode, left_subtree: Optional[TreeNode]) -> None:
        """Attach left_subtree to the leftmost position of node's subtree."""
        curr = node
        while curr.left:
            curr = curr.left
        curr.left = left_subtree

    parent: Optional[TreeNode] = None
    curr = root

    # Navigate to the node
    while curr and curr.val != key:
        parent = curr
        if key < curr.val:
            curr = curr.left
        else:
            curr = curr.right

    if curr is None:
        return root  # key not found

    # Determine replacement subtree
    if curr.left is None:
        replacement = curr.right
    elif curr.right is None:
        replacement = curr.left
    else:
        # Two children: attach left subtree to leftmost of right subtree
        attach_left_to_rightmost(curr.right, curr.left)
        replacement = curr.right

    # Rewire parent (or update root)
    if parent is None:
        return replacement
    if parent.left is curr:
        parent.left = replacement
    else:
        parent.right = replacement

    return root


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Delete Node in BST ===\n")
    # BST values: 5, 3, 6, 2, 4, 7  =>  tree structure:
    #       5
    #      / \
    #     3   6
    #    / \   \
    #   2   4   7
    test_vals = [5, 3, 6, 2, 4, 7]
    delete_key = 3
    # Expected inorder after deleting 3: [2, 4, 5, 6, 7]

    for name, fn in [("Brute", delete_brute),
                     ("Optimal", delete_optimal),
                     ("Best (Iterative)", delete_best)]:
        root = build_bst(test_vals)
        new_root = fn(root, delete_key)
        print(f"{name:20s}: inorder = {inorder(new_root)}")
