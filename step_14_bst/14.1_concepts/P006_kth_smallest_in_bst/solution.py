"""
Problem: Kth Smallest in BST
Difficulty: MEDIUM | XP: 25
LeetCode 230

Given the root of a Binary Search Tree and an integer k, return the kth
smallest value (1-indexed) of all the values of the nodes in the tree.
"""
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n) — store entire inorder
# Collect all values via inorder traversal into a list, return index k-1.
# ============================================================
def brute_force(root: Optional[TreeNode], k: int) -> int:
    """
    Inorder traversal of BST gives sorted values.
    Dump everything into an array and index directly.
    """
    result: List[int] = []

    def inorder(node: Optional[TreeNode]) -> None:
        if node is None:
            return
        inorder(node.left)
        result.append(node.val)
        inorder(node.right)

    inorder(root)
    return result[k - 1]  # k is 1-indexed


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(k + h)  |  Space: O(h) — iterative stack, stops early
# Iterative inorder with a counter; stop as soon as kth element found.
# ============================================================
def optimal(root: Optional[TreeNode], k: int) -> int:
    """
    Iterative inorder traversal. We stop the moment we've visited k nodes,
    avoiding traversing the entire tree when k is small.
    """
    stack: List[TreeNode] = []
    count = 0
    curr = root

    while curr is not None or stack:
        # Go as far left as possible
        while curr is not None:
            stack.append(curr)
            curr = curr.left

        # Process current node
        curr = stack.pop()
        count += 1
        if count == k:
            return curr.val

        # Move to right subtree
        curr = curr.right

    return -1  # k > total nodes (invalid input)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1) — Morris Traversal (no stack, no recursion)
# Threaded inorder traversal: temporarily links right-most node of left
# subtree back to current node so we can traverse without a stack.
# ============================================================
def best(root: Optional[TreeNode], k: int) -> int:
    """
    Morris Inorder Traversal for O(1) extra space.
    For each node:
      - If no left child: visit, move right.
      - Else: find inorder predecessor (rightmost of left subtree).
        * If predecessor.right is None: create thread (predecessor.right = curr), go left.
        * If predecessor.right is curr: remove thread, visit curr, go right.
    """
    count = 0
    curr = root

    while curr is not None:
        if curr.left is None:
            # No left subtree — visit this node
            count += 1
            if count == k:
                return curr.val
            curr = curr.right
        else:
            # Find inorder predecessor
            predecessor = curr.left
            while predecessor.right is not None and predecessor.right is not curr:
                predecessor = predecessor.right

            if predecessor.right is None:
                # Thread not yet created: create it, go left
                predecessor.right = curr
                curr = curr.left
            else:
                # Thread already exists: remove it, visit curr, go right
                predecessor.right = None
                count += 1
                if count == k:
                    return curr.val
                curr = curr.right

    return -1  # k > total nodes (invalid input)


# ---------------------------------------------------------------------------
# Helpers for testing
# ---------------------------------------------------------------------------
def build_bst(vals):
    """Build BST by inserting values one by one."""
    root = None

    def insert(node, v):
        if node is None:
            return TreeNode(v)
        if v < node.val:
            node.left = insert(node.left, v)
        else:
            node.right = insert(node.right, v)
        return node

    for v in vals:
        root = insert(root, v)
    return root


def build_from_level_order(vals):
    """Build tree from level-order array (BST structure preserved)."""
    if not vals:
        return None
    nodes = [TreeNode(v) if v is not None else None for v in vals]
    for i, node in enumerate(nodes):
        if node is None:
            continue
        l, r = 2 * i + 1, 2 * i + 2
        if l < len(nodes): node.left  = nodes[l]
        if r < len(nodes): node.right = nodes[r]
    return nodes[0]


if __name__ == "__main__":
    print("=== Kth Smallest in BST ===")

    # BST:   3
    #       / \
    #      1   4
    #       \
    #        2
    # Sorted: 1, 2, 3, 4
    root = build_from_level_order([3, 1, 4, None, 2])

    for k in [1, 2, 3, 4]:
        bf = brute_force(build_from_level_order([3, 1, 4, None, 2]), k)
        op = optimal(build_from_level_order([3, 1, 4, None, 2]), k)
        be = best(build_from_level_order([3, 1, 4, None, 2]), k)
        print(f"k={k}  Brute={bf}  Optimal={op}  Best={be}")

    # BST:       5
    #           / \
    #          3   6
    #         / \
    #        2   4
    #       /
    #      1
    # Sorted: 1, 2, 3, 4, 5, 6
    root2 = build_from_level_order([5, 3, 6, 2, 4, None, None, 1])
    print()
    print(f"Larger BST, k=3: Brute={brute_force(build_from_level_order([5,3,6,2,4,None,None,1]), 3)}")
    print(f"Larger BST, k=3: Optimal={optimal(build_from_level_order([5,3,6,2,4,None,None,1]), 3)}")
    print(f"Larger BST, k=3: Best={best(build_from_level_order([5,3,6,2,4,None,None,1]), 3)}")
