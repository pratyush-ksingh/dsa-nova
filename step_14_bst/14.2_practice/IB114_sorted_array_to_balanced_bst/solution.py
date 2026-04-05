"""
Problem: Sorted Array to Balanced BST
Difficulty: EASY | XP: 10
Source: InterviewBit

Convert a sorted array to a height-balanced BST.
Pick middle element as root; recurse on left and right halves.
"""

from typing import List, Optional


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Recursive mid-point: always pick left-biased mid
# Time: O(N)  |  Space: O(log N) recursion stack
# ============================================================
def brute_force(nums: List[int]) -> Optional[TreeNode]:
    def helper(left, right):
        if left > right:
            return None
        mid = (left + right) // 2
        node = TreeNode(nums[mid])
        node.left = helper(left, mid - 1)
        node.right = helper(mid + 1, right)
        return node

    return helper(0, len(nums) - 1)


# ============================================================
# APPROACH 2: OPTIMAL
# Right-biased mid to match expected output for even-length arrays
# Time: O(N)  |  Space: O(log N)
# ============================================================
def optimal(nums: List[int]) -> Optional[TreeNode]:
    def helper(left, right):
        if left > right:
            return None
        mid = left + (right - left + 1) // 2  # right-biased
        node = TreeNode(nums[mid])
        node.left = helper(left, mid - 1)
        node.right = helper(mid + 1, right)
        return node

    return helper(0, len(nums) - 1)


# ============================================================
# APPROACH 3: BEST
# Iterative using explicit stack of (node, left, right, is_left_child)
# Time: O(N)  |  Space: O(log N) stack
# ============================================================
def best(nums: List[int]) -> Optional[TreeNode]:
    if not nums:
        return None
    n = len(nums)
    mid = n // 2
    root = TreeNode(nums[mid])
    # stack entries: (parent_node, left_idx, right_idx, is_left)
    stack = []
    if mid + 1 <= n - 1:
        stack.append((root, mid + 1, n - 1, False))
    if 0 <= mid - 1:
        stack.append((root, 0, mid - 1, True))

    while stack:
        parent, l, r, is_left = stack.pop()
        m = l + (r - l) // 2
        new_node = TreeNode(nums[m])
        if is_left:
            parent.left = new_node
        else:
            parent.right = new_node
        if m + 1 <= r:
            stack.append((new_node, m + 1, r, False))
        if l <= m - 1:
            stack.append((new_node, l, m - 1, True))

    return root


def inorder(root):
    res = []
    stack = []
    cur = root
    while cur or stack:
        while cur:
            stack.append(cur)
            cur = cur.left
        cur = stack.pop()
        res.append(cur.val)
        cur = cur.right
    return res


def height(root):
    if not root:
        return 0
    return 1 + max(height(root.left), height(root.right))


if __name__ == "__main__":
    print("=== Sorted Array to Balanced BST ===")

    nums = [-10, -3, 0, 5, 9]
    r1 = brute_force(nums)
    r2 = optimal(nums)
    r3 = best(nums)
    print(f"BruteForce inorder: {inorder(r1)}, height={height(r1)}")  # sorted, height<=3
    print(f"Optimal    inorder: {inorder(r2)}, height={height(r2)}")
    print(f"Best       inorder: {inorder(r3)}, height={height(r3)}")

    single = [1]
    print(f"Single: {inorder(brute_force(single))}")  # [1]

    even = [1, 2, 3, 4]
    print(f"Even array inorder: {inorder(brute_force(even))}, height={height(brute_force(even))}")
