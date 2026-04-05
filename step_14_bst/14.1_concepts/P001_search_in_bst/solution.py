"""
Problem: Search in BST (LeetCode #700)
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
# APPROACH 1: BRUTE FORCE -- DFS ignoring BST property
# Time: O(n)  |  Space: O(h)
# ============================================================
def search_brute(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    if root is None:
        return None
    if root.val == val:
        return root
    left = search_brute(root.left, val)
    if left:
        return left
    return search_brute(root.right, val)


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive BST Search
# Time: O(h)  |  Space: O(h)
# ============================================================
def search_recursive(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    if root is None:
        return None
    if val == root.val:
        return root
    if val < root.val:
        return search_recursive(root.left, val)
    return search_recursive(root.right, val)


# ============================================================
# APPROACH 3: BEST -- Iterative BST Search
# Time: O(h)  |  Space: O(1)
# ============================================================
def search_iterative(root: Optional[TreeNode], val: int) -> Optional[TreeNode]:
    current = root
    while current:
        if val == current.val:
            return current
        if val < current.val:
            current = current.left
        else:
            current = current.right
    return None


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


def level_order(root: Optional[TreeNode]) -> List[int]:
    if not root:
        return []
    result = []
    queue = deque([root])
    while queue:
        node = queue.popleft()
        result.append(node.val)
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)
    return result


if __name__ == "__main__":
    print("=== Search in BST ===\n")

    root = build_tree([4, 2, 7, 1, 3])

    # Test 1: val exists
    r1 = search_iterative(root, 2)
    print(f"Search for 2: {level_order(r1)}")  # [2, 1, 3]

    # Test 2: val does not exist
    r2 = search_iterative(root, 5)
    print(f"Search for 5: {level_order(r2)}")  # []

    # Test 3: val at root
    r3 = search_recursive(root, 4)
    print(f"Search for 4: {level_order(r3)}")  # [4, 2, 7, 1, 3]

    # Test 4: single node
    single = TreeNode(1)
    print(f"Search 1 in [1]: {'found' if search_iterative(single, 1) else 'null'}")
    print(f"Search 2 in [1]: {'found' if search_iterative(single, 2) else 'null'}")

    # Test 5: empty tree
    print(f"Search in empty: {'found' if search_iterative(None, 1) else 'null'}")

    # Consistency check
    print("\n--- Consistency Check ---")
    for v in [1, 2, 3, 4, 5, 7]:
        b = search_brute(root, v)
        r = search_recursive(root, v)
        it = search_iterative(root, v)
        bv = b.val if b else None
        rv = r.val if r else None
        iv = it.val if it else None
        match = bv == rv == iv
        print(f"  val={v}: brute={bv}, recursive={rv}, iterative={iv} {'MATCH' if match else 'MISMATCH'}")
