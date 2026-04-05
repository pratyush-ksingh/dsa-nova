"""
Problem: Remove Half Nodes (InterviewBit)
Difficulty: EASY | XP: 10
Source: InterviewBit
"""
from typing import Optional, List
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Repeated BFS Passes
# Time: O(n^2) worst  |  Space: O(n)
# Do BFS, remove half nodes found, repeat until clean.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> Optional[TreeNode]:
    if root is None:
        return None

    changed = True
    while changed:
        changed = False
        # Handle root being a half node
        while root:
            if root.left and not root.right:
                root = root.left
                changed = True
            elif root.right and not root.left:
                root = root.right
                changed = True
            else:
                break

        if root is None:
            return None

        # BFS and fix children
        queue = deque([root])
        while queue:
            node = queue.popleft()
            # Check left child
            if node.left:
                child = node.left
                if child.left and not child.right:
                    node.left = child.left
                    changed = True
                elif child.right and not child.left:
                    node.left = child.right
                    changed = True
                queue.append(node.left)
            # Check right child
            if node.right:
                child = node.right
                if child.left and not child.right:
                    node.right = child.left
                    changed = True
                elif child.right and not child.left:
                    node.right = child.right
                    changed = True
                queue.append(node.right)

    return root


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Post-Order DFS
# Time: O(n)  |  Space: O(h)
# Bottom-up: clean children first, then check current node.
# ============================================================
def optimal(node: Optional[TreeNode]) -> Optional[TreeNode]:
    if node is None:
        return None

    node.left = optimal(node.left)
    node.right = optimal(node.right)

    # Half node: only left child
    if node.left and not node.right:
        return node.left
    # Half node: only right child
    if node.right and not node.left:
        return node.right

    # Leaf or full node: keep
    return node


# ============================================================
# APPROACH 3: BEST -- Iterative Post-Order with Parent Map
# Time: O(n)  |  Space: O(n)
# Same logic, no recursion. Explicit stack + parent tracking.
# ============================================================
def best(root: Optional[TreeNode]) -> Optional[TreeNode]:
    if root is None:
        return None

    # Build post-order sequence and parent map
    stack1 = [root]
    post_order = []
    parent = {}     # child -> (parent_node, is_left)

    while stack1:
        node = stack1.pop()
        post_order.append(node)
        if node.left:
            stack1.append(node.left)
            parent[id(node.left)] = (node, True)
        if node.right:
            stack1.append(node.right)
            parent[id(node.right)] = (node, False)

    # Process in reverse (post-order)
    for node in reversed(post_order):
        has_left = node.left is not None
        has_right = node.right is not None

        if (has_left and not has_right) or (not has_left and has_right):
            replacement = node.left if has_left else node.right
            if id(node) in parent:
                par, is_left = parent[id(node)]
                if is_left:
                    par.left = replacement
                else:
                    par.right = replacement
                parent[id(replacement)] = (par, is_left)
            else:
                root = replacement

    return root


# Helper to print tree in preorder for verification
def preorder(node: Optional[TreeNode]) -> List[int]:
    if node is None:
        return []
    return [node.val] + preorder(node.left) + preorder(node.right)


if __name__ == "__main__":
    print("=== Remove Half Nodes ===")

    # Test: 1(2(null,3),4) -> should become 1(3,4)
    t1 = TreeNode(1, TreeNode(2, None, TreeNode(3)), TreeNode(4))
    print(f"Optimal 1(2(null,3),4): {preorder(optimal(t1))}")  # [1,3,4]

    # Test: chain 1->2->3 -> should become 3
    t2 = TreeNode(1, None, TreeNode(2, None, TreeNode(3)))
    res2 = optimal(t2)
    print(f"Optimal chain 1->2->3: {res2.val}")  # 3

    # Test with brute force
    t3 = TreeNode(1, TreeNode(2, None, TreeNode(3)), TreeNode(4))
    print(f"Brute 1(2(null,3),4): {preorder(brute_force(t3))}")  # [1,3,4]
