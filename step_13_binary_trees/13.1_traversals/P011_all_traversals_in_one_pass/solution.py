"""
Problem: All Traversals in One Pass
Difficulty: HARD | XP: 50

Given a binary tree, compute its preorder, inorder, and postorder traversals
in a single iterative pass using a stack with a visit counter per node.
"""
from typing import List, Optional, Tuple


class TreeNode:
    def __init__(self, val: int = 0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE (Three separate recursive traversals)
# Time: O(3n) = O(n) | Space: O(n) stack space × 3
# ============================================================
def brute_force(root: Optional[TreeNode]) -> Tuple[List[int], List[int], List[int]]:
    """
    Perform three independent recursive DFS calls — one for each order.
    Simple and readable but traverses the tree three times.
    """
    def preorder(node):
        if not node:
            return []
        return [node.val] + preorder(node.left) + preorder(node.right)

    def inorder(node):
        if not node:
            return []
        return inorder(node.left) + [node.val] + inorder(node.right)

    def postorder(node):
        if not node:
            return []
        return postorder(node.left) + postorder(node.right) + [node.val]

    return preorder(root), inorder(root), postorder(root)


# ============================================================
# APPROACH 2: OPTIMAL (Single iterative pass with (node, state) stack)
# Time: O(n) | Space: O(n)
# ============================================================
def optimal(root: Optional[TreeNode]) -> Tuple[List[int], List[int], List[int]]:
    """
    Use a single stack where each entry is (node, visit_count).
    visit_count tracks how many times we've "visited" this node:
      - visit_count == 1 → first visit → add to preorder, push back with count=2,
                          push left child with count=1
      - visit_count == 2 → second visit (returned from left) → add to inorder,
                          push back with count=3, push right child with count=1
      - visit_count == 3 → third visit (returned from right) → add to postorder

    This mirrors the recursive call stack: each node is processed 3 times
    (before left, after left/before right, after right).
    """
    if not root:
        return [], [], []

    pre, ino, post = [], [], []
    stack = [(root, 1)]  # (node, visit_count)

    while stack:
        node, cnt = stack.pop()

        if cnt == 1:
            pre.append(node.val)
            stack.append((node, 2))
            if node.left:
                stack.append((node.left, 1))

        elif cnt == 2:
            ino.append(node.val)
            stack.append((node, 3))
            if node.right:
                stack.append((node.right, 1))

        else:  # cnt == 3
            post.append(node.val)

    return pre, ino, post


# ============================================================
# APPROACH 3: BEST (Same single-pass — clean with explicit state enum)
# Time: O(n) | Space: O(n)
# ============================================================
def best(root: Optional[TreeNode]) -> Tuple[List[int], List[int], List[int]]:
    """
    Same algorithm as Approach 2, expressed with named state constants
    for clarity. The visit counter simulates the call stack state machine:
      PRE (1)  → push node value to preorder output
      IN  (2)  → push node value to inorder output
      POST (3) → push node value to postorder output

    Key insight: this single traversal visits each node exactly 3 times,
    so total work is O(3n) = O(n) — same asymptotically as three separate passes
    but with only one stack and one loop.
    """
    PRE, IN, POST = 1, 2, 3
    if not root:
        return [], [], []

    pre, ino, post = [], [], []
    stack = [(root, PRE)]

    while stack:
        node, state = stack.pop()

        if state == PRE:
            pre.append(node.val)
            stack.append((node, IN))
            if node.left:
                stack.append((node.left, PRE))

        elif state == IN:
            ino.append(node.val)
            stack.append((node, POST))
            if node.right:
                stack.append((node.right, PRE))

        else:  # POST
            post.append(node.val)

    return pre, ino, post


# ============================================================
# HELPER: Build tree from level-order list
# ============================================================
def build_tree(values: List[Optional[int]]) -> Optional[TreeNode]:
    if not values or values[0] is None:
        return None
    root = TreeNode(values[0])
    queue = [root]
    i = 1
    while queue and i < len(values):
        node = queue.pop(0)
        if i < len(values) and values[i] is not None:
            node.left = TreeNode(values[i])
            queue.append(node.left)
        i += 1
        if i < len(values) and values[i] is not None:
            node.right = TreeNode(values[i])
            queue.append(node.right)
        i += 1
    return root


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== All Traversals in One Pass ===\n")

    # Tree:     1
    #          / \
    #         2   3
    #        / \
    #       4   5
    test_cases = [
        (
            [1, 2, 3, 4, 5],
            ([1, 2, 4, 5, 3], [4, 2, 5, 1, 3], [4, 5, 2, 3, 1]),
        ),
        (
            [1],
            ([1], [1], [1]),
        ),
        (
            [1, 2, None, 3],
            ([1, 2, 3], [3, 2, 1], [3, 2, 1]),
        ),
    ]

    for values, expected in test_cases:
        root = build_tree(values)
        pre_b, ino_b, post_b = brute_force(root)
        pre_o, ino_o, post_o = optimal(root)
        pre_h, ino_h, post_h = best(root)
        exp_pre, exp_ino, exp_post = expected

        ok_b = (pre_b, ino_b, post_b) == expected
        ok_o = (pre_o, ino_o, post_o) == expected
        ok_h = (pre_h, ino_h, post_h) == expected
        status = "PASS" if ok_b and ok_o and ok_h else "FAIL"

        print(f"Tree (level-order): {values}")
        print(f"  Brute   pre={pre_b} | in={ino_b} | post={post_b}")
        print(f"  Optimal pre={pre_o} | in={ino_o} | post={post_o}")
        print(f"  Best    pre={pre_h} | in={ino_h} | post={post_h}")
        print(f"  Expected pre={exp_pre} | in={exp_ino} | post={exp_post}  [{status}]\n")
