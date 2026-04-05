"""
Problem: Binary Search Tree Iterator
Difficulty: MEDIUM | XP: 25
LeetCode: #173

Implement the BSTIterator class for inorder traversal of a BST:
  - BSTIterator(root): initializes the iterator with root
  - next(): returns the next smallest number (inorder)
  - hasNext(): returns True if there is still a next number

next() and hasNext() must run in average O(1) time and O(h) memory.
"""
from typing import List, Optional
from collections import deque


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE -- Flatten entire inorder into array
# Time: O(n) init  |  O(1) next/hasNext  |  Space: O(n)
# ============================================================
class BSTIteratorBrute:
    """
    Pre-compute the entire inorder traversal into a list.
    next() and hasNext() are O(1) array accesses.
    Simple but uses O(n) space for the full list.
    """

    def __init__(self, root: Optional[TreeNode]):
        self._inorder: List[int] = []
        self._idx = 0
        self._flatten(root)

    def _flatten(self, node: Optional[TreeNode]) -> None:
        if node is None:
            return
        self._flatten(node.left)
        self._inorder.append(node.val)
        self._flatten(node.right)

    def next(self) -> int:
        val = self._inorder[self._idx]
        self._idx += 1
        return val

    def hasNext(self) -> bool:
        return self._idx < len(self._inorder)


# ============================================================
# APPROACH 2: OPTIMAL -- Controlled traversal with stack
# Time: O(h) init  |  Amortized O(1) next()  |  O(1) hasNext()  |  Space: O(h)
# ============================================================
class BSTIteratorOptimal:
    """
    Maintain a stack that holds the current "leftmost path" from the
    current position. next() pops the top (smallest unvisited node),
    then pushes the left spine of its right subtree.

    Each node is pushed and popped exactly once -> amortized O(1) per next().
    Stack depth is at most h (tree height) -> O(h) space.
    """

    def __init__(self, root: Optional[TreeNode]):
        self._stack: List[TreeNode] = []
        self._push_left(root)

    def _push_left(self, node: Optional[TreeNode]) -> None:
        """Push all left-spine nodes from `node` down to the leftmost."""
        while node is not None:
            self._stack.append(node)
            node = node.left

    def next(self) -> int:
        # Top of stack is the smallest unvisited node
        node = self._stack.pop()
        # If it has a right child, push the right child's left spine
        if node.right:
            self._push_left(node.right)
        return node.val

    def hasNext(self) -> bool:
        return len(self._stack) > 0


# ============================================================
# APPROACH 3: BEST -- Same stack approach, explicit naming for clarity
# Time: Amortized O(1) next()  |  O(1) hasNext()  |  Space: O(h)
# (Stack-based is already optimal; this variant is identical in complexity
#  but shows the "controlled DFS" perspective explicitly)
# ============================================================
class BSTIteratorBest:
    """
    Identical algorithm to Optimal, structured to highlight that this is
    essentially a "lazy" inorder DFS -- we only advance as far as needed.
    Useful when iterating a large BST but only needing a few elements.
    """

    def __init__(self, root: Optional[TreeNode]):
        self._stack: List[TreeNode] = []
        self._advance_left(root)

    def _advance_left(self, node: Optional[TreeNode]) -> None:
        """Walk all the way left, pushing every node onto the stack."""
        curr = node
        while curr:
            self._stack.append(curr)
            curr = curr.left

    def next(self) -> int:
        curr = self._stack.pop()
        # After visiting curr, its right subtree is "next" in inorder
        # so we advance left along it
        self._advance_left(curr.right)
        return curr.val

    def hasNext(self) -> bool:
        return bool(self._stack)


# ============================================================
# Helpers
# ============================================================
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
    print("=== BST Iterator ===\n")

    # BST:
    #       7
    #      / \
    #     3   15
    #        /  \
    #       9   20
    # Inorder: [3, 7, 9, 15, 20]

    tests = [
        ([7, 3, 15, None, None, 9, 20], [3, 7, 9, 15, 20]),
        ([4, 2, 6, 1, 3, 5, 7], [1, 2, 3, 4, 5, 6, 7]),
        ([1], [1]),
    ]

    for arr, expected in tests:
        root = build_tree(arr)

        it1 = BSTIteratorBrute(root)
        it2 = BSTIteratorOptimal(root)
        it3 = BSTIteratorBest(root)

        res1, res2, res3 = [], [], []
        while it1.hasNext(): res1.append(it1.next())
        while it2.hasNext(): res2.append(it2.next())
        while it3.hasNext(): res3.append(it3.next())

        ok = res1 == res2 == res3 == expected
        print(f"Tree: {arr}")
        print(f"  Brute:   {res1}")
        print(f"  Optimal: {res2}")
        print(f"  Best:    {res3}")
        print(f"  Expected:{expected}")
        print(f"  {'PASS' if ok else 'FAIL'}\n")

    # Test hasNext + next interleaved
    print("--- Interleaved test (LC example) ---")
    root = build_tree([7, 3, 15, None, None, 9, 20])
    it = BSTIteratorOptimal(root)
    ops = [("next", 3), ("next", 7), ("hasNext", True), ("next", 9),
           ("hasNext", True), ("next", 15), ("hasNext", True), ("next", 20),
           ("hasNext", False)]
    all_pass = True
    for op, exp in ops:
        if op == "next":
            result = it.next()
        else:
            result = it.hasNext()
        status = result == exp
        all_pass = all_pass and status
        print(f"  {op}() = {result}  expected={exp}  {'OK' if status else 'FAIL'}")
    print(f"  Overall: {'PASS' if all_pass else 'FAIL'}")
