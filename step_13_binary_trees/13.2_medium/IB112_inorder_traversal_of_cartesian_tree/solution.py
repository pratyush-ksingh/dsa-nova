"""
Problem: Inorder Traversal of Cartesian Tree (InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given the inorder traversal of a Cartesian tree, construct the tree and return
the root. A Cartesian tree satisfies: root is the maximum element, left subtree
is built from elements to the left of max, right subtree from elements to the right.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive with linear max search
# Time: O(n^2)  |  Space: O(n) recursion stack (skewed tree)
# ============================================================
def build_cartesian_brute(inorder: List[int]) -> Optional[TreeNode]:
    """
    For each subarray, scan linearly to find the max element.
    That max becomes the root; recurse on left and right subarrays.
    """
    def build(arr: List[int]) -> Optional[TreeNode]:
        if not arr:
            return None
        # Find index of max element -- O(n) scan
        max_idx = arr.index(max(arr))
        node = TreeNode(arr[max_idx])
        node.left = build(arr[:max_idx])
        node.right = build(arr[max_idx + 1:])
        return node

    return build(inorder)


def inorder_of_tree(root: Optional[TreeNode]) -> List[int]:
    """Helper: collect inorder traversal of a constructed tree."""
    result = []
    def dfs(node):
        if node is None:
            return
        dfs(node.left)
        result.append(node.val)
        dfs(node.right)
    dfs(root)
    return result


def preorder_of_tree(root: Optional[TreeNode]) -> List[int]:
    """Helper: collect preorder traversal of a constructed tree."""
    result = []
    def dfs(node):
        if node is None:
            return
        result.append(node.val)
        dfs(node.left)
        dfs(node.right)
    dfs(root)
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Sparse Table / Segment Tree for range max
# Time: O(n log n) build + O(n) construction  |  Space: O(n log n)
# ============================================================
class SparseTable:
    """Precompute range max queries in O(n log n), answer in O(1)."""
    def __init__(self, arr: List[int]):
        import math
        n = len(arr)
        LOG = max(1, math.floor(math.log2(n)) + 1) if n > 0 else 1
        self.table = [[0] * n for _ in range(LOG)]
        self.log2 = [0] * (n + 1)
        for i in range(2, n + 1):
            self.log2[i] = self.log2[i // 2] + 1

        # Store indices (not values) so we know the position of max
        self.idx_table = [[i for i in range(n)] for _ in range(LOG)]
        for i in range(n):
            self.idx_table[0][i] = i

        for k in range(1, LOG):
            for i in range(n - (1 << k) + 1):
                l = self.idx_table[k - 1][i]
                r = self.idx_table[k - 1][i + (1 << (k - 1))]
                self.idx_table[k][i] = l if arr[l] >= arr[r] else r
        self.arr = arr

    def query_max_idx(self, lo: int, hi: int) -> int:
        """Return index of maximum in arr[lo..hi] inclusive."""
        if lo > hi:
            return -1
        k = self.log2[hi - lo + 1]
        l = self.idx_table[k][lo]
        r = self.idx_table[k][hi - (1 << k) + 1]
        return l if self.arr[l] >= self.arr[r] else r


def build_cartesian_optimal(inorder: List[int]) -> Optional[TreeNode]:
    """
    Build Sparse Table for O(1) range-max queries, then recurse.
    Eliminates the O(n) scan per call; recursion itself is O(n) total calls.
    """
    if not inorder:
        return None
    st = SparseTable(inorder)

    def build(lo: int, hi: int) -> Optional[TreeNode]:
        if lo > hi:
            return None
        max_idx = st.query_max_idx(lo, hi)
        node = TreeNode(inorder[max_idx])
        node.left = build(lo, max_idx - 1)
        node.right = build(max_idx + 1, hi)
        return node

    return build(0, len(inorder) - 1)


# ============================================================
# APPROACH 3: BEST -- Stack-based O(n) construction
# Time: O(n)  |  Space: O(n)
# ============================================================
def build_cartesian_best(inorder: List[int]) -> Optional[TreeNode]:
    """
    Process elements left-to-right maintaining a decreasing-value stack.
    For each new element x:
      - Pop all stack elements smaller than x (they become x's left child --
        the last popped is the rightmost subtree still smaller than x).
      - x becomes the right child of the current stack top (if any).
    This constructs the Cartesian tree in a single left-to-right pass.
    """
    if not inorder:
        return None

    stack: List[TreeNode] = []
    root = None

    for val in inorder:
        node = TreeNode(val)
        last_popped = None

        # Pop nodes whose values are less than current -- they form left subtree
        while stack and stack[-1].val < val:
            last_popped = stack.pop()

        # The last popped becomes left child of current node
        node.left = last_popped

        # Current node becomes right child of new stack top
        if stack:
            stack[-1].right = node
        else:
            # Stack is empty => current node is the new root
            root = node

        stack.append(node)

    return root


if __name__ == "__main__":
    print("=== Inorder Traversal of Cartesian Tree ===\n")

    tests = [
        [1, 2, 3, 4, 5],          # max-heap: root=5
        [5, 10, 40, 30, 28],      # InterviewBit example: root=40
        [3],                       # single element
        [1],
    ]
    expected_inorders = [
        [1, 2, 3, 4, 5],
        [5, 10, 40, 30, 28],
        [3],
        [1],
    ]

    for arr, exp in zip(tests, expected_inorders):
        r1 = build_cartesian_brute(arr[:])
        r2 = build_cartesian_optimal(arr[:])
        r3 = build_cartesian_best(arr[:])
        io1 = inorder_of_tree(r1)
        io2 = inorder_of_tree(r2)
        io3 = inorder_of_tree(r3)
        pre1 = preorder_of_tree(r1)
        pre2 = preorder_of_tree(r2)
        pre3 = preorder_of_tree(r3)
        print(f"Input inorder: {arr}")
        print(f"  Brute   inorder={io1}  preorder={pre1}")
        print(f"  Optimal inorder={io2}  preorder={pre2}")
        print(f"  Best    inorder={io3}  preorder={pre3}")
        ok = (io1 == io2 == io3 == exp) and (pre1 == pre2 == pre3)
        print(f"  {'PASS' if ok else 'FAIL'}\n")
