"""
Problem: Maximum Width of Binary Tree (LeetCode 662)
Difficulty: HARD | XP: 25

The width of a level is defined as the number of nodes between the
leftmost and rightmost non-null nodes (INCLUSIVE), counting null nodes
in between. Return the maximum width among all levels.

Index trick: assign indices like a complete binary tree (1-indexed):
  - Root: index 1
  - Left child of node i: 2*i
  - Right child of node i: 2*i + 1
Width of a level = last_index - first_index + 1
"""
from typing import Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE — BFS with null node padding
# Time: O(2^h) where h = height  |  Space: O(2^h)  [TLE for deep trees]
# ============================================================
def brute_force(root: Optional[TreeNode]) -> int:
    """
    Simulate a complete binary tree level by level using actual null nodes.
    At each level, count nodes from leftmost non-null to rightmost non-null.
    Trim trailing nulls after computing width.
    WARNING: exponential space/time for skewed trees — do NOT use in practice.
    """
    if not root:
        return 0

    queue = deque([root])
    max_width = 0

    while queue:
        level_size = len(queue)
        # Remove leading nulls
        while queue and queue[0] is None:
            queue.popleft()
        # Remove trailing nulls
        while queue and queue[-1] is None:
            queue.pop()

        if not queue:
            break

        max_width = max(max_width, len(queue))

        # Expand one level — include nulls to preserve positions
        next_level = deque()
        for _ in range(level_size):
            if queue:
                node = queue.popleft()
                if node:
                    next_level.append(node.left)
                    next_level.append(node.right)
                else:
                    next_level.append(None)
                    next_level.append(None)
        queue = next_level

    return max_width


# ============================================================
# APPROACH 2: OPTIMAL — BFS with index tracking (normalize per level)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(root: Optional[TreeNode]) -> int:
    """
    Assign a virtual index to each node (like a heap array):
      root -> 1, left child of i -> 2*i, right child of i -> 2*i+1
    Width at a level = last_index - first_index + 1.

    Normalization: subtract the first index of each level from all indices
    to prevent integer overflow (indices can reach 2^h for deep trees).
    This is safe because width = last - first is invariant under shifting.
    """
    if not root:
        return 0

    max_width = 0
    # Queue stores (node, index)
    queue = deque([(root, 1)])

    while queue:
        level_size = len(queue)
        # Normalize: subtract the first index of this level
        first_idx = queue[0][1]
        level_first = level_last = 0

        for i in range(level_size):
            node, idx = queue.popleft()
            idx -= first_idx  # normalize to prevent overflow

            if i == 0:
                level_first = idx
            level_last = idx

            if node.left:
                queue.append((node.left, 2 * (idx + first_idx)))
            if node.right:
                queue.append((node.right, 2 * (idx + first_idx) + 1))

        max_width = max(max_width, level_last - level_first + 1)

    return max_width


# ============================================================
# APPROACH 3: BEST — BFS with index tracking (clean normalization)
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(root: Optional[TreeNode]) -> int:
    """
    Same index-based BFS but with cleaner normalization:
    At the start of each level, record the first index as an offset.
    Children indices are computed as 2*(idx - offset) and 2*(idx - offset)+1
    relative to 0, then offset is applied back. This keeps indices small
    at every level, preventing overflow even for trees with 10^4 nodes.
    """
    if not root:
        return 0

    max_width = 0
    queue = deque([(root, 0)])  # 0-indexed within level

    while queue:
        level_size = len(queue)
        first = queue[0][1]  # leftmost index this level (for offset)
        last  = queue[-1][1] # rightmost (may not equal last popped if there are holes, track explicitly)

        prev_idx = None
        for _ in range(level_size):
            node, idx = queue.popleft()
            normalized = idx - first  # keep values small
            if prev_idx is None:
                level_start = normalized
            level_end = normalized
            prev_idx = normalized

            if node.left:
                queue.append((node.left,  2 * normalized))
            if node.right:
                queue.append((node.right, 2 * normalized + 1))

        max_width = max(max_width, level_end - level_start + 1)

    return max_width


# ---- Helper to build tree ----
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
    print("=== Max Width of Binary Tree ===")

    tests = [
        ([1, 3, 2, 5, 3, None, 9], 4),              # Level 2: [5,3,null,9] -> 4
        ([1, 3, 2, 5, None, None, 9, 6, None, 7], 7), # Level 3: [6,null,...,7] -> 7
        ([1], 1),
        ([1, 3, None, 5, 3], 2),
    ]

    for vals, expected in tests:
        t = build_tree(vals)
        b = brute_force(build_tree(vals))
        o = optimal(build_tree(vals))
        be = best(build_tree(vals))
        print(f"Tree={vals} -> Brute={b}, Optimal={o}, Best={be} (expected={expected})")
