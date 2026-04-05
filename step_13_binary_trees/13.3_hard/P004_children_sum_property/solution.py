"""
Problem: Children Sum Property
Difficulty: MEDIUM | XP: 25

Given a binary tree, convert it so every node's value equals the sum of
its children's values. You may ONLY increase node values (never decrease).
"""
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(h) recursion stack
# Multiple passes: after fixing leaves upward, re-check entire tree
# ============================================================
def brute_force(root: Optional[TreeNode]) -> bool:
    """
    Check whether the children sum property holds for every node.
    Returns True if property is satisfied everywhere, False otherwise.
    (Does NOT modify the tree - just validates.)
    """
    def check(node: Optional[TreeNode]) -> bool:
        if node is None:
            return True
        if node.left is None and node.right is None:
            return True  # leaf node always satisfies
        child_sum = 0
        if node.left:
            child_sum += node.left.val
        if node.right:
            child_sum += node.right.val
        if node.val != child_sum:
            return False
        return check(node.left) and check(node.right)

    return check(root)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(h) recursion stack
# Single DFS: going DOWN, inflate children so parent constraint holds.
# Coming back UP, set parent = sum of actual children (now correct).
# ============================================================
def optimal(root: Optional[TreeNode]) -> None:
    """
    Convert binary tree in-place to satisfy children sum property.
    Key insight: we can only increase values, so going down we push
    the parent's value into children (inflate), then coming back up
    we fix the parent to equal the real sum of its children.
    """
    def dfs(node: Optional[TreeNode]) -> None:
        if node is None:
            return
        if node.left is None and node.right is None:
            return  # leaf: nothing to do

        # Step 1: compute current child sum
        child_sum = 0
        if node.left:
            child_sum += node.left.val
        if node.right:
            child_sum += node.right.val

        # Step 2: if children are too small, inflate them (going down)
        if child_sum < node.val:
            # spread parent's value into children proportionally (or just push to left)
            if node.left:
                node.left.val = node.val
            elif node.right:
                node.right.val = node.val

        # Step 3: recurse into children
        dfs(node.left)
        dfs(node.right)

        # Step 4: coming back up - set this node = sum of its children
        total = 0
        if node.left:
            total += node.left.val
        if node.right:
            total += node.right.val
        node.val = total

    dfs(root)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(h) recursion stack
# Same single-pass DFS as Optimal; this IS the best known solution.
# Annotated more clearly for interview presentation.
# ============================================================
def best(root: Optional[TreeNode]) -> None:
    """
    Same O(n) DFS, written with explicit commentary for clarity.
    The trick: values can only go UP, so we push parent value DOWN
    to children on the way in, then correct parent on the way out.
    """
    def dfs(node: Optional[TreeNode]) -> None:
        if node is None or (node.left is None and node.right is None):
            return

        left_val  = node.left.val  if node.left  else 0
        right_val = node.right.val if node.right else 0
        child_sum = left_val + right_val

        # Going DOWN: inflate children if needed so they can hold parent value
        if child_sum < node.val:
            diff = node.val - child_sum
            if node.left:
                node.left.val += diff   # push entire deficit to left child
            else:
                node.right.val += diff  # or right if no left

        dfs(node.left)
        dfs(node.right)

        # Going UP: correct this node to equal sum of (now-fixed) children
        total = 0
        if node.left:
            total += node.left.val
        if node.right:
            total += node.right.val
        node.val = total

    dfs(root)


# ---------------------------------------------------------------------------
# Helpers for testing
# ---------------------------------------------------------------------------
def build_tree(vals):
    """Build tree from level-order list (None = missing node)."""
    if not vals:
        return None
    nodes = [TreeNode(v) if v is not None else None for v in vals]
    for i, node in enumerate(nodes):
        if node is None:
            continue
        left_i  = 2 * i + 1
        right_i = 2 * i + 2
        if left_i  < len(nodes): node.left  = nodes[left_i]
        if right_i < len(nodes): node.right = nodes[right_i]
    return nodes[0]


def tree_to_list(root):
    """Level-order serialization for printing."""
    if not root:
        return []
    from collections import deque
    result, q = [], deque([root])
    while q:
        node = q.popleft()
        result.append(node.val)
        if node.left  or node.right:
            q.append(node.left  if node.left  else TreeNode(0))
            q.append(node.right if node.right else TreeNode(0))
    return result


if __name__ == "__main__":
    print("=== Children Sum Property ===")

    # Brute-force: check validity
    t1 = build_tree([10, 8, 2, 3, 5, 1, 1])
    print(f"Brute (check valid tree):   {brute_force(t1)}")   # True

    t2 = build_tree([1, 2, 3])
    print(f"Brute (invalid tree):       {brute_force(t2)}")   # False

    # Optimal: convert in-place
    t3 = build_tree([2, 35, 10, 2, 3, 5, 2])
    optimal(t3)
    print(f"Optimal (converted):        {tree_to_list(t3)}")
    print(f"  Valid after conversion:   {brute_force(t3)}")

    # Best: convert in-place
    t4 = build_tree([2, 35, 10, 2, 3, 5, 2])
    best(t4)
    print(f"Best (converted):           {tree_to_list(t4)}")
    print(f"  Valid after conversion:   {brute_force(t4)}")
