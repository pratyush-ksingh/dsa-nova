"""
Problem: Build BT from Inorder Preorder
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional


class TreeNode:
    def __init__(self, val: int = 0,
                 left: "Optional[TreeNode]" = None,
                 right: "Optional[TreeNode]" = None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²)  |  Space: O(n)  (recursion stack)
# ============================================================
# Recursive: preorder[0] is root; find it in inorder via linear scan
# to split left/right subtrees.  Each level costs O(n) => O(n²) total.
def brute_force(preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
    if not preorder:
        return None

    root_val = preorder[0]
    root = TreeNode(root_val)

    mid = inorder.index(root_val)           # linear O(n) search

    root.left  = brute_force(preorder[1 : 1 + mid], inorder[:mid])
    root.right = brute_force(preorder[1 + mid:],    inorder[mid + 1:])
    return root


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# ============================================================
# Same recursion but use a pre-built index map (value -> inorder idx)
# so each root lookup is O(1).  Use index pointers to avoid slicing.
def optimal(preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
    in_map = {val: i for i, val in enumerate(inorder)}
    pre_iter = iter(preorder)

    def build(in_left: int, in_right: int) -> Optional[TreeNode]:
        if in_left > in_right:
            return None
        root_val = next(pre_iter)
        root = TreeNode(root_val)
        mid = in_map[root_val]
        root.left  = build(in_left,  mid - 1)
        root.right = build(mid + 1,  in_right)
        return root

    return build(0, len(inorder) - 1)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# ============================================================
# Iterative stack-based approach — no recursion overhead.
# Maintains a stack of nodes whose left subtree is still being built.
# When preorder[i] matches inorder[in_idx], we've exhausted all left
# children of the stack top; pop nodes and attach new node on right.
def best(preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
    if not preorder:
        return None

    root = TreeNode(preorder[0])
    stack = [root]
    in_idx = 0

    for i in range(1, len(preorder)):
        node = TreeNode(preorder[i])
        parent = None

        while stack and stack[-1].val == inorder[in_idx]:
            parent = stack.pop()
            in_idx += 1

        if parent:
            parent.right = node
        else:
            stack[-1].left = node

        stack.append(node)

    return root


# ---- helpers ----

def inorder_traversal(root: Optional[TreeNode]) -> List[int]:
    result: List[int] = []
    def dfs(node):
        if not node:
            return
        dfs(node.left)
        result.append(node.val)
        dfs(node.right)
    dfs(root)
    return result


def preorder_traversal(root: Optional[TreeNode]) -> List[int]:
    result: List[int] = []
    def dfs(node):
        if not node:
            return
        result.append(node.val)
        dfs(node.left)
        dfs(node.right)
    dfs(root)
    return result


if __name__ == "__main__":
    print("=== Build BT from Inorder Preorder ===\n")

    pre1 = [3, 9, 20, 15, 7]
    in1  = [9, 3, 15, 20, 7]

    print("[Approach 1 - BruteForce: Linear Search O(n²)]")
    t1 = brute_force(pre1, in1)
    print("Inorder: ", inorder_traversal(t1))
    print("Preorder:", preorder_traversal(t1))

    print("\n[Approach 2 - Optimal: HashMap O(n)]")
    t2 = optimal(pre1, in1)
    print("Inorder: ", inorder_traversal(t2))
    print("Preorder:", preorder_traversal(t2))

    print("\n[Approach 3 - Best: Iterative Stack O(n)]")
    t3 = best(pre1, in1)
    print("Inorder: ", inorder_traversal(t3))
    print("Preorder:", preorder_traversal(t3))

    print("\n[Single node]")
    s = best([1], [1])
    print(f"Root: {s.val}, left: {s.left}, right: {s.right}")

    print("\n[Left-skewed: preorder=[4,3,2,1], inorder=[1,2,3,4]]")
    ls = best([4, 3, 2, 1], [1, 2, 3, 4])
    print("Inorder: ", inorder_traversal(ls))
    print("Preorder:", preorder_traversal(ls))
