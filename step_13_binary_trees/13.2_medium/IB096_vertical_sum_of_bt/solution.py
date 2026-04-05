"""
Vertical Sum of Binary Tree

Sum all node values at each vertical line (horizontal distance from root).
Return sums from leftmost to rightmost vertical line.
"""
from typing import List, Optional
from collections import defaultdict, deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- DFS + dict + sort keys
# Time: O(N + k log k)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []

    hd_map = defaultdict(int)

    def dfs(node, hd):
        if not node:
            return
        hd_map[hd] += node.val
        dfs(node.left, hd - 1)
        dfs(node.right, hd + 1)

    dfs(root, 0)
    return [hd_map[k] for k in sorted(hd_map.keys())]


# ============================================================
# APPROACH 2: OPTIMAL -- DFS + manual min/max tracking
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []

    hd_map = defaultdict(int)
    bounds = [0, 0]  # [min_hd, max_hd]

    def dfs(node, hd):
        if not node:
            return
        hd_map[hd] += node.val
        bounds[0] = min(bounds[0], hd)
        bounds[1] = max(bounds[1], hd)
        dfs(node.left, hd - 1)
        dfs(node.right, hd + 1)

    dfs(root, 0)
    return [hd_map[hd] for hd in range(bounds[0], bounds[1] + 1)]


# ============================================================
# APPROACH 3: BEST -- BFS + HashMap + min/max HD (iterative)
# Time: O(N)  |  Space: O(N)
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []

    hd_map = defaultdict(int)
    min_hd = max_hd = 0

    queue = deque([(root, 0)])
    while queue:
        node, hd = queue.popleft()
        hd_map[hd] += node.val
        min_hd = min(min_hd, hd)
        max_hd = max(max_hd, hd)

        if node.left:
            queue.append((node.left, hd - 1))
        if node.right:
            queue.append((node.right, hd + 1))

    return [hd_map[hd] for hd in range(min_hd, max_hd + 1)]


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    #        1
    #       / \
    #      2    3
    #     / \  / \
    #    4   5 6   7
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    root.right.left = TreeNode(6)
    root.right.right = TreeNode(7)

    print("=== Vertical Sum of Binary Tree ===")
    print(f"Brute:   {brute_force(root)}")    # [4, 2, 12, 3, 7]
    print(f"Optimal: {optimal(root)}")         # [4, 2, 12, 3, 7]
    print(f"Best:    {best(root)}")            # [4, 2, 12, 3, 7]
