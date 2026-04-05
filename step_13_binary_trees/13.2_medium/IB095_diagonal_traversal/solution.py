"""
Problem: Diagonal Traversal (InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit
"""
from typing import Optional, List
from collections import deque, defaultdict


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashMap + DFS
# Time: O(n + d*log(d))  |  Space: O(n)
# Assign diagonal IDs via DFS, group into map, sort keys.
# ============================================================
def brute_force(root: Optional[TreeNode]) -> List[List[int]]:
    diag_map = defaultdict(list)

    def dfs(node, d):
        if node is None:
            return
        diag_map[d].append(node.val)
        dfs(node.left, d + 1)   # Left child: next diagonal
        dfs(node.right, d)       # Right child: same diagonal

    dfs(root, 0)
    return [diag_map[k] for k in sorted(diag_map.keys())]


# ============================================================
# APPROACH 2: OPTIMAL -- BFS with Right-Chain Following
# Time: O(n)  |  Space: O(n)
# Process diagonals one at a time. Follow right chains,
# enqueue left children for the next diagonal.
# ============================================================
def optimal(root: Optional[TreeNode]) -> List[List[int]]:
    if root is None:
        return []

    result = []
    queue = deque([root])

    while queue:
        size = len(queue)
        diagonal = []

        for _ in range(size):
            node = queue.popleft()

            # Follow the entire right chain (same diagonal)
            while node:
                diagonal.append(node.val)
                if node.left:
                    queue.append(node.left)  # Left child -> next diagonal
                node = node.right  # Stay on same diagonal

        result.append(diagonal)

    return result


# ============================================================
# APPROACH 3: BEST -- Streamlined Queue BFS (flat output)
# Time: O(n)  |  Space: O(n)
# Same algorithm, returns flattened diagonal order.
# ============================================================
def best(root: Optional[TreeNode]) -> List[int]:
    if root is None:
        return []

    result = []
    queue = deque([root])

    while queue:
        node = queue.popleft()

        # Follow right chain, enqueue left children
        while node:
            result.append(node.val)
            if node.left:
                queue.append(node.left)
            node = node.right

    return result


if __name__ == "__main__":
    print("=== Diagonal Traversal ===")

    #         8
    #        / \
    #       3   10
    #      / \    \
    #     1   6    14
    #        / \   /
    #       4   7 13
    root = TreeNode(8,
        TreeNode(3,
            TreeNode(1),
            TreeNode(6, TreeNode(4), TreeNode(7))),
        TreeNode(10,
            None,
            TreeNode(14, TreeNode(13), None)))

    print(f"Brute:   {brute_force(root)}")
    # [[8,10,14], [3,6,7,13], [1,4]]

    print(f"Optimal: {optimal(root)}")
    # [[8,10,14], [3,6,7,13], [1,4]]

    print(f"Best (flat): {best(root)}")
    # [8,10,14,3,6,7,13,1,4]

    # Edge cases
    print(f"Empty:   {optimal(None)}")           # []
    print(f"Single:  {optimal(TreeNode(42))}")   # [[42]]
