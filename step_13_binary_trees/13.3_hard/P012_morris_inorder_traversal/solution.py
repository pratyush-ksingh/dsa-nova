"""
Problem: Morris Inorder Traversal
Difficulty: HARD | XP: 50
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val: int = 0,
                 left: "Optional[TreeNode]" = None,
                 right: "Optional[TreeNode]" = None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)  (recursion call stack)
# ============================================================
# Standard recursive inorder: left -> root -> right.
# O(h) space for the call stack (h = height; O(n) for skewed tree).
def brute_force(root: Optional[TreeNode]) -> List[int]:
    result: List[int] = []

    def recurse(node: Optional[TreeNode]) -> None:
        if not node:
            return
        recurse(node.left)
        result.append(node.val)
        recurse(node.right)

    recurse(root)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)  (explicit stack)
# ============================================================
# Iterative inorder with an explicit stack — avoids Python's
# recursion limit but still O(n) space in the worst case.
def optimal(root: Optional[TreeNode]) -> List[int]:
    result: List[int] = []
    stack: List[TreeNode] = []
    curr = root

    while curr or stack:
        # Descend to leftmost node
        while curr:
            stack.append(curr)
            curr = curr.left
        curr = stack.pop()
        result.append(curr.val)
        curr = curr.right

    return result


# ============================================================
# APPROACH 3: BEST  — Morris Inorder Traversal
# Time: O(n)  |  Space: O(1)  (no stack, no recursion)
# ============================================================
# Creates temporary "threads" in the tree so we can navigate
# back up from left subtrees without a stack.
#
# For each node `curr`:
#   Case A — curr has no left child:
#     Collect curr.val, move to curr.right.
#   Case B — curr has a left child:
#     Find inorder predecessor (rightmost of left subtree).
#     If predecessor.right is None:
#         Create thread: predecessor.right = curr; go left.
#     If predecessor.right is curr:
#         Remove thread (restore tree); collect curr.val; go right.
#
# Each node is processed at most twice => O(n).
# The tree structure is fully restored on exit.
def best(root: Optional[TreeNode]) -> List[int]:
    result: List[int] = []
    curr = root

    while curr:
        if curr.left is None:
            # Case A: no left subtree — visit and go right
            result.append(curr.val)
            curr = curr.right
        else:
            # Case B: find inorder predecessor
            predecessor = curr.left
            while predecessor.right and predecessor.right is not curr:
                predecessor = predecessor.right

            if predecessor.right is None:
                # First encounter: create thread, descend left
                predecessor.right = curr
                curr = curr.left
            else:
                # Second encounter: remove thread, visit, go right
                predecessor.right = None
                result.append(curr.val)
                curr = curr.right

    return result


# ---- helpers ----

def build_tree(level_order: list) -> Optional[TreeNode]:
    """Build tree from level-order list (None = missing node)."""
    if not level_order or level_order[0] is None:
        return None
    root = TreeNode(level_order[0])
    queue = deque([root])
    i = 1
    while queue and i < len(level_order):
        node = queue.popleft()
        if i < len(level_order) and level_order[i] is not None:
            node.left = TreeNode(level_order[i])
            queue.append(node.left)
        i += 1
        if i < len(level_order) and level_order[i] is not None:
            node.right = TreeNode(level_order[i])
            queue.append(node.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Morris Inorder Traversal ===\n")

    # Tree:       4
    #            / \
    #           2   6
    #          / \ / \
    #         1  3 5  7
    # Expected inorder: [1, 2, 3, 4, 5, 6, 7]
    tree1 = build_tree([4, 2, 6, 1, 3, 5, 7])

    print("[Approach 1 - BruteForce: Recursive]")
    print("Inorder:", brute_force(tree1))

    print("\n[Approach 2 - Optimal: Iterative Stack]")
    print("Inorder:", optimal(tree1))

    print("\n[Approach 3 - Best: Morris Threading O(1) space]")
    print("Inorder:", best(tree1))
    # Verify tree is fully restored
    print("Tree restored (inorder again):", brute_force(tree1))

    # Left-skewed: 3 -> 2 -> 1
    tree2 = build_tree([3, 2, None, 1])
    print("\n[Left-skewed: 3->2->1]")
    print("Morris:", best(tree2))

    # Single node
    print("\n[Single node: 42]")
    print("Morris:", best(TreeNode(42)))

    # Right-skewed: 1 -> 2 -> 3 -> 4
    r = TreeNode(1)
    r.right = TreeNode(2)
    r.right.right = TreeNode(3)
    r.right.right.right = TreeNode(4)
    print("\n[Right-skewed: 1->2->3->4]")
    print("Morris:", best(r))
