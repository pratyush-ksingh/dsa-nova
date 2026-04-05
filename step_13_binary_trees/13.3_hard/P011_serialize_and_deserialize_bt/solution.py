"""
Problem: Serialize and Deserialize Binary Tree
Difficulty: HARD | XP: 50

Convert a binary tree to a string and reconstruct it exactly.

Real-life use case: Persisting tree structures to disk/database, sending
trees over network (expression trees, ASTs, decision trees).
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.left: Optional["TreeNode"] = None
        self.right: Optional["TreeNode"] = None


def inorder(root: Optional[TreeNode]) -> str:
    """Helper for verification."""
    if root is None:
        return "#"
    return f"({inorder(root.left)} {root.val} {inorder(root.right)})"


def build_sample_tree() -> TreeNode:
    """Build:  1 / 2  3 / 4 5"""
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.right.left = TreeNode(4)
    root.right.right = TreeNode(5)
    return root


# ============================================================
# APPROACH 1: BRUTE FORCE (Preorder with "#" null markers)
# Time: O(n)  |  Space: O(n)
# DFS preorder: root, left, right. Null nodes encoded as "#".
# Deserialize by consuming a token list recursively in the same order.
# ============================================================
def brute_force_serialize(root: Optional[TreeNode]) -> str:
    parts = []

    def dfs(node: Optional[TreeNode]) -> None:
        if node is None:
            parts.append("#")
            return
        parts.append(str(node.val))
        dfs(node.left)
        dfs(node.right)

    dfs(root)
    return ",".join(parts)


def brute_force_deserialize(data: str) -> Optional[TreeNode]:
    if not data:
        return None
    tokens = iter(data.split(","))

    def build() -> Optional[TreeNode]:
        token = next(tokens, None)
        if token is None or token == "#":
            return None
        node = TreeNode(int(token))
        node.left = build()
        node.right = build()
        return node

    return build()


# ============================================================
# APPROACH 2: OPTIMAL (BFS level-order with "null" markers)
# Time: O(n)  |  Space: O(n)
# Serialize: BFS queue; enqueue left and right even if None.
# Deserialize: BFS queue of parent nodes; assign left/right from token stream.
# Produces the most human-readable format (mirrors the tree visually).
# ============================================================
def optimal_serialize(root: Optional[TreeNode]) -> str:
    if root is None:
        return ""
    parts = []
    queue = deque([root])
    while queue:
        node = queue.popleft()
        if node is None:
            parts.append("null")
        else:
            parts.append(str(node.val))
            queue.append(node.left)
            queue.append(node.right)
    return ",".join(parts)


def optimal_deserialize(data: str) -> Optional[TreeNode]:
    if not data:
        return None
    tokens = data.split(",")
    root = TreeNode(int(tokens[0]))
    queue = deque([root])
    i = 1
    while queue and i < len(tokens):
        node = queue.popleft()
        # Left child
        if i < len(tokens) and tokens[i] != "null":
            node.left = TreeNode(int(tokens[i]))
            queue.append(node.left)
        i += 1
        # Right child
        if i < len(tokens) and tokens[i] != "null":
            node.right = TreeNode(int(tokens[i]))
            queue.append(node.right)
        i += 1
    return root


# ============================================================
# APPROACH 3: BEST (Preorder with index list for O(1) index tracking)
# Time: O(n)  |  Space: O(n)
# Same as BruteForce but uses a mutable list [idx] instead of iterator
# for cleaner recursion (avoids StopIteration / generator overhead).
# ============================================================
def best_serialize(root: Optional[TreeNode]) -> str:
    parts = []

    def dfs(node: Optional[TreeNode]) -> None:
        if node is None:
            parts.append("N")
            return
        parts.append(str(node.val))
        dfs(node.left)
        dfs(node.right)

    dfs(root)
    return ",".join(parts)


def best_deserialize(data: str) -> Optional[TreeNode]:
    if not data:
        return None
    tokens = data.split(",")
    idx = [0]

    def build() -> Optional[TreeNode]:
        if idx[0] >= len(tokens):
            return None
        token = tokens[idx[0]]
        idx[0] += 1
        if token == "N":
            return None
        node = TreeNode(int(token))
        node.left = build()
        node.right = build()
        return node

    return build()


if __name__ == "__main__":
    print("=== Serialize and Deserialize BT ===")
    tree = build_sample_tree()
    print(f"Original inorder: {inorder(tree)}")

    # Brute Force
    s1 = brute_force_serialize(tree)
    t1 = brute_force_deserialize(s1)
    print(f"\n[Brute - Preorder]")
    print(f"  Serialized: {s1}")
    print(f"  Restored inorder: {inorder(t1)}")

    # Optimal
    s2 = optimal_serialize(tree)
    t2 = optimal_deserialize(s2)
    print(f"\n[Optimal - BFS]")
    print(f"  Serialized: {s2}")
    print(f"  Restored inorder: {inorder(t2)}")

    # Best
    s3 = best_serialize(tree)
    t3 = best_deserialize(s3)
    print(f"\n[Best - Preorder+Index]")
    print(f"  Serialized: {s3}")
    print(f"  Restored inorder: {inorder(t3)}")

    # Edge: null
    print(f"\n[Edge: null tree]")
    print(f"  Brute:   {brute_force_deserialize(brute_force_serialize(None))}")
    print(f"  Optimal: {optimal_deserialize(optimal_serialize(None))}")
    print(f"  Best:    {best_deserialize(best_serialize(None))}")

    # Edge: single node
    single = TreeNode(42)
    rs = best_deserialize(best_serialize(single))
    print(f"\n[Edge: single node val=42]")
    print(f"  Restored val: {rs.val if rs else None}")
