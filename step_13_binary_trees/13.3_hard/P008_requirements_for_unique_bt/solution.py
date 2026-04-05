"""
Problem: Requirements for Unique Binary Tree
Difficulty: MEDIUM | XP: 25

Determine which pairs of traversals uniquely identify a binary tree.

Rules:
  UNIQUE:     Preorder + Inorder, Postorder + Inorder, LevelOrder + Inorder
  NOT UNIQUE: Preorder + Postorder (ambiguous for non-full trees)
  NOT UNIQUE: Any single traversal alone

The three approaches implement:
  1. Rule-based uniqueness checker
  2. Reconstruct from preorder + inorder
  3. Reconstruct from postorder + inorder
"""
from typing import Optional, List


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Rule-based uniqueness determination
# Time: O(1)  |  Space: O(1)
# Knowledge-based: state which traversal pairs uniquely define a tree
# ============================================================
def brute_force(t1: str, t2: str) -> str:
    unique_pairs = {
        frozenset(["preorder", "inorder"]),
        frozenset(["postorder", "inorder"]),
        frozenset(["levelorder", "inorder"]),
    }
    pair = frozenset([t1.lower(), t2.lower()])
    return "UNIQUE" if pair in unique_pairs else "NOT UNIQUE"


# ============================================================
# APPROACH 2: OPTIMAL - Reconstruct tree from preorder + inorder
# Time: O(N)  |  Space: O(N) for index map
# Root = preorder[0]; find in inorder to split left/right subtrees.
# ============================================================
def optimal(preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
    in_idx = {v: i for i, v in enumerate(inorder)}

    def build(pre_l: int, pre_r: int, in_l: int, in_r: int) -> Optional[TreeNode]:
        if pre_l > pre_r:
            return None
        root_val = preorder[pre_l]
        root = TreeNode(root_val)
        k = in_idx[root_val] - in_l  # left subtree size
        root.left  = build(pre_l + 1,     pre_l + k, in_l,         in_l + k - 1)
        root.right = build(pre_l + k + 1, pre_r,     in_l + k + 1, in_r)
        return root

    return build(0, len(preorder) - 1, 0, len(inorder) - 1)


# ============================================================
# APPROACH 3: BEST - Reconstruct tree from postorder + inorder
# Time: O(N)  |  Space: O(N)
# Root = postorder[-1]; find in inorder to split left/right subtrees.
# ============================================================
def best(postorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
    in_idx = {v: i for i, v in enumerate(inorder)}

    def build(post_l: int, post_r: int, in_l: int, in_r: int) -> Optional[TreeNode]:
        if post_l > post_r:
            return None
        root_val = postorder[post_r]
        root = TreeNode(root_val)
        k = in_idx[root_val] - in_l  # left subtree size
        root.left  = build(post_l,     post_l + k - 1, in_l,         in_l + k - 1)
        root.right = build(post_l + k, post_r - 1,     in_l + k + 1, in_r)
        return root

    return build(0, len(postorder) - 1, 0, len(inorder) - 1)


def inorder_traversal(root: Optional[TreeNode]) -> List[int]:
    res = []
    def dfs(node):
        if not node: return
        dfs(node.left); res.append(node.val); dfs(node.right)
    dfs(root)
    return res


if __name__ == "__main__":
    print("=== Requirements for Unique Binary Tree ===")

    print("\n-- Uniqueness Rules --")
    pairs = [
        ("preorder", "inorder"),
        ("postorder", "inorder"),
        ("preorder", "postorder"),
        ("levelorder", "inorder"),
        ("preorder", "preorder"),
    ]
    for t1, t2 in pairs:
        print(f"  {t1} + {t2} -> {brute_force(t1, t2)}")

    # Reconstruct from preorder + inorder
    preorder = [3, 9, 20, 15, 7]
    inorder  = [9, 3, 15, 20, 7]
    print("\n-- Reconstruct from preorder + inorder --")
    print(f"Preorder: {preorder}")
    print(f"Inorder:  {inorder}")
    tree = optimal(preorder, inorder)
    print(f"Inorder of result: {inorder_traversal(tree)}")   # [9,3,15,20,7]

    # Reconstruct from postorder + inorder
    postorder = [9, 15, 7, 20, 3]
    print("\n-- Reconstruct from postorder + inorder --")
    print(f"Postorder: {postorder}")
    print(f"Inorder:   {inorder}")
    tree = best(postorder, inorder)
    print(f"Inorder of result: {inorder_traversal(tree)}")   # [9,3,15,20,7]
