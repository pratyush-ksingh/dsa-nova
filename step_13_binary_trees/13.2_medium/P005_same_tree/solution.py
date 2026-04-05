"""
Problem: Same Tree (LeetCode #100)
Difficulty: EASY | XP: 10
"""
from typing import Optional, List
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Serialize & Compare
# Time: O(n)  |  Space: O(n)
# Serialize both trees to lists (including None markers), compare.
# ============================================================
def brute_force(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
    def serialize(node: Optional[TreeNode]) -> List:
        if node is None:
            return [None]
        return [node.val] + serialize(node.left) + serialize(node.right)

    return serialize(p) == serialize(q)


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Simultaneous DFS
# Time: O(n)  |  Space: O(h)
# Compare node by node. Short-circuits on mismatch.
# ============================================================
def optimal(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
    if p is None and q is None:
        return True
    if p is None or q is None:
        return False
    if p.val != q.val:
        return False
    return optimal(p.left, q.left) and optimal(p.right, q.right)


# ============================================================
# APPROACH 3: BEST -- Iterative with Stack
# Time: O(n)  |  Space: O(h)
# Same logic, explicit stack, no recursion overhead.
# ============================================================
def best(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
    stack = [(p, q)]
    while stack:
        a, b = stack.pop()
        if a is None and b is None:
            continue
        if a is None or b is None:
            return False
        if a.val != b.val:
            return False
        stack.append((a.left, b.left))
        stack.append((a.right, b.right))
    return True


if __name__ == "__main__":
    print("=== Same Tree ===")

    # Test: [1,2,3] vs [1,2,3] -> True
    p1 = TreeNode(1, TreeNode(2), TreeNode(3))
    q1 = TreeNode(1, TreeNode(2), TreeNode(3))

    # Test: [1,2] vs [1,null,2] -> False
    p2 = TreeNode(1, TreeNode(2))
    q2 = TreeNode(1, None, TreeNode(2))

    print(f"Brute [1,2,3] vs [1,2,3]: {brute_force(p1, q1)}")    # True
    print(f"Brute [1,2] vs [1,null,2]: {brute_force(p2, q2)}")    # False
    print(f"Optimal [1,2,3] vs [1,2,3]: {optimal(p1, q1)}")       # True
    print(f"Optimal [1,2] vs [1,null,2]: {optimal(p2, q2)}")       # False
    print(f"Best [1,2,3] vs [1,2,3]: {best(p1, q1)}")             # True
    print(f"Best [1,2] vs [1,null,2]: {best(p2, q2)}")             # False
