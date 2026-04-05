"""
Problem: Two Sum in BST (LeetCode #653)
Difficulty: EASY | XP: 10
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- For each node, BST search complement
# Time: O(n * h)  |  Space: O(h)
# ============================================================
def two_sum_brute(root: Optional[TreeNode], k: int) -> bool:
    def bst_search(node: Optional[TreeNode], val: int) -> bool:
        while node:
            if val == node.val:
                return True
            if val < node.val:
                node = node.left
            else:
                node = node.right
        return False

    def dfs(node: Optional[TreeNode]) -> bool:
        if node is None:
            return False
        complement = k - node.val
        if complement != node.val and bst_search(root, complement):
            return True
        return dfs(node.left) or dfs(node.right)

    return dfs(root)


# ============================================================
# APPROACH 2: OPTIMAL -- HashSet + DFS
# Time: O(n)  |  Space: O(n)
# ============================================================
def two_sum_hashset(root: Optional[TreeNode], k: int) -> bool:
    seen = set()

    def dfs(node: Optional[TreeNode]) -> bool:
        if node is None:
            return False
        if k - node.val in seen:
            return True
        seen.add(node.val)
        return dfs(node.left) or dfs(node.right)

    return dfs(root)


# ============================================================
# APPROACH 3: BEST -- Inorder + Two Pointers
# Time: O(n)  |  Space: O(n)
# ============================================================
def two_sum_two_pointers(root: Optional[TreeNode], k: int) -> bool:
    # Step 1: Inorder traversal -> sorted list
    sorted_vals: List[int] = []

    def inorder(node):
        if node is None:
            return
        inorder(node.left)
        sorted_vals.append(node.val)
        inorder(node.right)

    inorder(root)

    # Step 2: Two pointers
    lo, hi = 0, len(sorted_vals) - 1
    while lo < hi:
        total = sorted_vals[lo] + sorted_vals[hi]
        if total == k:
            return True
        if total < k:
            lo += 1
        else:
            hi -= 1
    return False


def build_tree(arr: List[Optional[int]]) -> Optional[TreeNode]:
    if not arr or arr[0] is None:
        return None
    root = TreeNode(arr[0])
    queue = deque([root])
    i = 1
    while queue and i < len(arr):
        curr = queue.popleft()
        if i < len(arr) and arr[i] is not None:
            curr.left = TreeNode(arr[i])
            queue.append(curr.left)
        i += 1
        if i < len(arr) and arr[i] is not None:
            curr.right = TreeNode(arr[i])
            queue.append(curr.right)
        i += 1
    return root


if __name__ == "__main__":
    print("=== Two Sum in BST ===\n")

    tests = [
        ([5, 3, 6, 2, 4, None, 7], 9, True),
        ([5, 3, 6, 2, 4, None, 7], 28, False),
        ([2, 1, 3], 4, True),
        ([2, 1, 3], 1, False),
        ([1], 2, False),
        ([1, None, 2], 3, True),
    ]

    for arr, k, expected in tests:
        root = build_tree(arr)
        brute = two_sum_brute(root, k)
        hashset = two_sum_hashset(root, k)
        two_ptr = two_sum_two_pointers(root, k)
        status = "PASS" if brute == expected == hashset == two_ptr else "FAIL"
        print(f"Tree={arr}, k={k}")
        print(f"  Brute={brute}, HashSet={hashset}, TwoPtr={two_ptr} (expected={expected}) {status}\n")
