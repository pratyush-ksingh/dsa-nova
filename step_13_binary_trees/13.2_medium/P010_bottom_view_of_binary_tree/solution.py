"""
Problem: Bottom View of Binary Tree
Difficulty: MEDIUM | XP: 25

The bottom view of a binary tree is the set of nodes visible when the tree
is viewed from the bottom. For each horizontal distance (column), we keep
the LAST node encountered during a level-order (BFS) traversal — i.e., the
node at the greatest depth at that column. Return nodes from left to right.

Horizontal distance (HD):
  - Root has HD = 0
  - Left child: parent HD - 1
  - Right child: parent HD + 1
"""
from typing import List, Optional
from collections import deque, OrderedDict


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE — DFS with depth tracking
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    """
    DFS: for each node, track its (hd, depth).
    At each horizontal distance, keep the node with the maximum depth.
    If two nodes share the same HD and depth (same level), the one
    visited later (right subtree) wins — matches BFS bottom-view behavior.
    Finally, sort by HD and return values.
    """
    if not root:
        return []

    # hd -> (depth, value)
    hd_map = {}

    def dfs(node, hd, depth):
        if not node:
            return
        if hd not in hd_map or depth >= hd_map[hd][0]:
            hd_map[hd] = (depth, node.val)
        dfs(node.left,  hd - 1, depth + 1)
        dfs(node.right, hd + 1, depth + 1)

    dfs(root, 0, 0)
    return [v for _, (_, v) in sorted(hd_map.items())]


# ============================================================
# APPROACH 2: OPTIMAL — BFS with TreeMap/OrderedDict
# Time: O(n log n) for sorting HD keys  |  Space: O(n)
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    """
    BFS (level-order) traversal. For each node, store (node, hd) in queue.
    At each step, update hd_map[hd] = node.val — BFS guarantees that later
    nodes at the same HD are deeper (or at the same level, processed later).
    Either way, the last write is the correct bottom-view node.
    Sort hd_map by key (HD) and return values.
    """
    if not root:
        return []

    hd_map = {}  # hd -> value (last seen at this HD during BFS)
    queue = deque([(root, 0)])

    while queue:
        node, hd = queue.popleft()
        hd_map[hd] = node.val  # overwrite — BFS means later = deeper
        if node.left:
            queue.append((node.left, hd - 1))
        if node.right:
            queue.append((node.right, hd + 1))

    # Sort by horizontal distance and extract values
    return [hd_map[k] for k in sorted(hd_map)]


# ============================================================
# APPROACH 3: BEST — BFS with explicit level separation (clearest intent)
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    """
    Same as Approach 2 (BFS + overwrite), but processes level by level
    explicitly. This makes it crystal clear that we overwrite with the
    last node at each HD per level — nodes at deeper levels always win.
    Uses Python's sorted dict capability for the final output.
    """
    if not root:
        return []

    hd_map = {}
    queue = deque([(root, 0)])

    while queue:
        level_size = len(queue)
        # Process one entire level at a time
        for _ in range(level_size):
            node, hd = queue.popleft()
            hd_map[hd] = node.val  # later in BFS (deeper) overwrites
            if node.left:
                queue.append((node.left, hd - 1))
            if node.right:
                queue.append((node.right, hd + 1))

    return [v for _, v in sorted(hd_map.items())]


# ---- Helper to build tree from level-order list ----
def build_tree(vals):
    if not vals or vals[0] is None:
        return None
    root = TreeNode(vals[0])
    q = deque([root])
    i = 1
    while q and i < len(vals):
        node = q.popleft()
        if i < len(vals) and vals[i] is not None:
            node.left = TreeNode(vals[i])
            q.append(node.left)
        i += 1
        if i < len(vals) and vals[i] is not None:
            node.right = TreeNode(vals[i])
            q.append(node.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Bottom View of Binary Tree ===")

    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6  7
    t1 = build_tree([1, 2, 3, 4, 5, 6, 7])
    print(f"Tree 1 — Brute:   {brute_force(t1)}")   # [4, 2, 6, 3, 7] — wait, let's trace
    # HD: 4→-2, 2→-1, 5→0, 1→0(overwritten by 5 at depth2, 6 at depth2)
    # Actually: 4(-2), 2(-1), 5(0)vs6(0), 3(+1), 7(+2)
    # BFS level 2: 5 at HD=0 first, then 6 at HD=0 → 6 wins
    # Bottom view: [4, 2, 6, 3, 7]  ← wait 6 is at HD=0? Let me recheck:
    # Root(1)=HD0, Left(2)=HD-1, Right(3)=HD+1
    # 2's left(4)=HD-2, 2's right(5)=HD0, 3's left(6)=HD0, 3's right(7)=HD+2
    # So HD0 has: root(1 depth0), node5(depth2), node6(depth2)
    # BFS processes 5 before 6; 6 overwrites → HD0=6
    # Result: HD-2=4, HD-1=2, HD0=6, HD+1=3, HD+2=7 → [4,2,6,3,7]
    print(f"Tree 1 — Optimal: {optimal(build_tree([1,2,3,4,5,6,7]))}")
    print(f"Tree 1 — Best:    {best(build_tree([1,2,3,4,5,6,7]))}")

    #       20
    #      /  \
    #     8    22
    #    / \
    #   5  3
    #     / \
    #    10  14
    t2 = build_tree([20, 8, 22, 5, 3, None, None, None, None, 10, 14])
    print(f"Tree 2 — Optimal: {optimal(t2)}")  # [5,10,3,14,22]
