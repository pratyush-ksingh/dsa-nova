"""
Problem: Time to Burn Binary Tree
Difficulty: HARD | XP: 50

Find minimum time for fire to burn entire tree starting from target node.
Fire spreads to adjacent nodes (parent, left child, right child) each second.
"""

from collections import deque
from typing import Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# BFS to build parent map, then find target, then BFS from target
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode], target: int) -> int:
    if not root:
        return 0

    parent_map = {}

    # Build parent map via BFS
    queue = deque([root])
    while queue:
        node = queue.popleft()
        if node.left:
            parent_map[node.left] = node
            queue.append(node.left)
        if node.right:
            parent_map[node.right] = node
            queue.append(node.right)

    # Find target node via BFS
    start = None
    bfs = deque([root])
    while bfs:
        node = bfs.popleft()
        if node.val == target:
            start = node
            break
        if node.left:
            bfs.append(node.left)
        if node.right:
            bfs.append(node.right)

    # BFS from target to find max spread time
    visited = {start}
    queue = deque([start])
    time = 0

    while queue:
        size = len(queue)
        any_new = False
        for _ in range(size):
            cur = queue.popleft()
            for neighbor in [cur.left, cur.right, parent_map.get(cur)]:
                if neighbor and neighbor not in visited:
                    visited.add(neighbor)
                    queue.append(neighbor)
                    any_new = True
        if any_new:
            time += 1

    return time


# ============================================================
# APPROACH 2: OPTIMAL
# Single DFS to build parent map + find target simultaneously, then BFS
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(root: Optional[TreeNode], target: int) -> int:
    parent_map = {}
    target_node = [None]

    def dfs(node, parent):
        if not node:
            return
        parent_map[node] = parent
        if node.val == target:
            target_node[0] = node
        dfs(node.left, node)
        dfs(node.right, node)

    dfs(root, None)

    start = target_node[0]
    if not start:
        return -1

    visited = {start}
    queue = deque([start])
    time = 0

    while queue:
        size = len(queue)
        any_new = False
        for _ in range(size):
            cur = queue.popleft()
            for neighbor in [cur.left, cur.right, parent_map.get(cur)]:
                if neighbor and neighbor not in visited:
                    visited.add(neighbor)
                    queue.append(neighbor)
                    any_new = True
        if any_new:
            time += 1

    return time


# ============================================================
# APPROACH 3: BEST
# Pure recursive DFS — returns distance from node to target.
# When target is found in one subtree, max burn = dist + other_subtree_height.
# Time: O(N)  |  Space: O(H) recursion stack only
# ============================================================
def best(root: Optional[TreeNode], target: int) -> int:
    ans = [0]

    def height(node):
        if not node:
            return 0
        return 1 + max(height(node.left), height(node.right))

    def dfs(node):
        """Returns non-negative distance to target if found, else -1."""
        if not node:
            return -1
        if node.val == target:
            ans[0] = max(ans[0], height(node))
            return 0

        left = dfs(node.left)
        right = dfs(node.right)

        if left >= 0:
            # target in left subtree; fire can also spread to right
            spread = left + 1 + height(node.right)
            ans[0] = max(ans[0], spread)
            return left + 1
        if right >= 0:
            spread = right + 1 + height(node.left)
            ans[0] = max(ans[0], spread)
            return right + 1
        return -1

    dfs(root)
    return ans[0]


def build_tree(vals):
    if not vals:
        return None
    root = TreeNode(vals[0])
    queue = deque([root])
    i = 1
    while queue and i < len(vals):
        node = queue.popleft()
        if i < len(vals) and vals[i] is not None:
            node.left = TreeNode(vals[i])
            queue.append(node.left)
        i += 1
        if i < len(vals) and vals[i] is not None:
            node.right = TreeNode(vals[i])
            queue.append(node.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Time to Burn Binary Tree ===")

    # Tree: 1->2,3; 2->4,5; 3->null,6; target=2 => time=3
    vals = [1, 2, 3, 4, 5, None, 6]
    target = 2
    print(f"BruteForce (target={target}): {brute_force(build_tree(vals), target)}")  # 3
    print(f"Optimal   (target={target}): {optimal(build_tree(vals), target)}")        # 3
    print(f"Best      (target={target}): {best(build_tree(vals), target)}")           # 3

    # target at root
    print(f"Target at root (target=1): {optimal(build_tree(vals), 1)}")  # 3

    # Single node
    single = TreeNode(1)
    print(f"Single node: {brute_force(single, 1)}")  # 0
