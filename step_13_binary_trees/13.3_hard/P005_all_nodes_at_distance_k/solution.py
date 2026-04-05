"""Problem: All Nodes at Distance K
Difficulty: MEDIUM | XP: 25

Find all nodes at exactly distance K from a target node in a binary tree.
"""
from collections import deque
from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE - Build parent map, then BFS
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[TreeNode], target: TreeNode, k: int) -> List[int]:
    parent = {}

    def build_parents(node, par):
        if not node:
            return
        parent[node.val] = par
        build_parents(node.left, node)
        build_parents(node.right, node)

    build_parents(root, None)

    queue = deque([target])
    visited = {target.val}
    dist = 0

    while queue:
        if dist == k:
            return [node.val for node in queue]
        dist += 1
        for _ in range(len(queue)):
            node = queue.popleft()
            for nb in [node.left, node.right, parent.get(node.val)]:
                if nb and nb.val not in visited:
                    visited.add(nb.val)
                    queue.append(nb)
    return []


# ============================================================
# APPROACH 2: OPTIMAL - Single DFS with backtracking
# Time: O(N)  |  Space: O(H)
# Returns distance from node to target; collects nodes at distance K
# ============================================================
def optimal(root: Optional[TreeNode], target: TreeNode, k: int) -> List[int]:
    result = []

    def collect(node, dist):
        if not node or dist > k:
            return
        if dist == k:
            result.append(node.val)
            return
        collect(node.left, dist + 1)
        collect(node.right, dist + 1)

    def dfs(node) -> int:
        """Returns distance from node to target, or -1 if not found."""
        if not node:
            return -1
        if node.val == target.val:
            collect(node, 0)
            return 1
        left = dfs(node.left)
        if left != -1:
            if left == k:
                result.append(node.val)
            collect(node.right, left + 1)
            return left + 1
        right = dfs(node.right)
        if right != -1:
            if right == k:
                result.append(node.val)
            collect(node.left, right + 1)
            return right + 1
        return -1

    dfs(root)
    return sorted(result)


# ============================================================
# APPROACH 3: BEST - Parent map + BFS (cleaner iterative)
# Time: O(N)  |  Space: O(N)
# ============================================================
def best(root: Optional[TreeNode], target: TreeNode, k: int) -> List[int]:
    parent = {}

    def build_parents(node, par):
        if not node:
            return
        parent[node.val] = par
        build_parents(node.left, node)
        build_parents(node.right, node)

    build_parents(root, None)

    q = deque([(target, 0)])
    seen = {target.val}
    result = []

    while q:
        node, dist = q.popleft()
        if dist == k:
            result.append(node.val)
            continue
        for nb in [node.left, node.right, parent.get(node.val)]:
            if nb and nb.val not in seen:
                seen.add(nb.val)
                q.append((nb, dist + 1))
    return sorted(result)


def build_tree(vals):
    if not vals or vals[0] is None:
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
    # Tree: [3,5,1,6,2,0,8,None,None,7,4], target=5, k=2 -> [1,4,7]
    root = build_tree([3, 5, 1, 6, 2, 0, 8, None, None, 7, 4])

    def find_node(node, val):
        if not node:
            return None
        if node.val == val:
            return node
        return find_node(node.left, val) or find_node(node.right, val)

    target = find_node(root, 5)
    print("BruteForce:", sorted(brute_force(root, target, 2)))  # [1, 4, 7]
    print("Optimal:   ", optimal(root, target, 2))              # [1, 4, 7]
    print("Best:      ", best(root, target, 2))                 # [1, 4, 7]

    # Edge: k=0
    print("k=0:", best(root, target, 0))  # [5]
