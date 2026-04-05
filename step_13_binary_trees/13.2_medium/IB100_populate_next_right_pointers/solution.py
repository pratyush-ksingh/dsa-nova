"""
Populate Next Right Pointers (LeetCode #116)

Connect each node to its next right node at the same level in a perfect binary tree.
"""
from typing import Optional
from collections import deque


class Node:
    def __init__(self, val=0, left=None, right=None, next=None):
        self.val = val
        self.left = left
        self.right = right
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- BFS with Queue
# Time: O(N)  |  Space: O(N)
# ============================================================
def brute_force(root: Optional[Node]) -> Optional[Node]:
    if not root:
        return None

    queue = deque([root])

    while queue:
        size = len(queue)
        for i in range(size):
            node = queue.popleft()
            # Connect to next node in level (not the last one)
            node.next = queue[0] if i < size - 1 else None

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

    return root


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive DFS (pre-order)
# Time: O(N)  |  Space: O(log N) recursion stack
# ============================================================
def optimal(root: Optional[Node]) -> Optional[Node]:
    if not root:
        return None

    def connect(node):
        if not node or not node.left:
            return
        # Same parent: left -> right
        node.left.next = node.right
        # Cross-link: right -> next subtree's left
        if node.next:
            node.right.next = node.next.left
        connect(node.left)
        connect(node.right)

    connect(root)
    return root


# ============================================================
# APPROACH 3: BEST -- O(1) Space using established next pointers
# Time: O(N)  |  Space: O(1)
# ============================================================
def best(root: Optional[Node]) -> Optional[Node]:
    if not root:
        return None

    leftmost = root

    while leftmost.left:
        curr = leftmost
        while curr:
            # Connection 1: left child -> right child
            curr.left.next = curr.right
            # Connection 2: right child -> next node's left child
            if curr.next:
                curr.right.next = curr.next.left
            curr = curr.next
        leftmost = leftmost.left

    return root


# ============================================================
# HELPER: Print next pointers level by level
# ============================================================
def print_next_pointers(root):
    leftmost = root
    while leftmost:
        curr = leftmost
        parts = []
        while curr:
            parts.append(str(curr.val))
            curr = curr.next
        parts.append("null")
        print(" -> ".join(parts))
        leftmost = leftmost.left


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    def build_tree():
        root = Node(1)
        root.left = Node(2)
        root.right = Node(3)
        root.left.left = Node(4)
        root.left.right = Node(5)
        root.right.left = Node(6)
        root.right.right = Node(7)
        return root

    print("=== Brute Force ===")
    r1 = build_tree()
    brute_force(r1)
    print_next_pointers(r1)

    print("=== Optimal ===")
    r2 = build_tree()
    optimal(r2)
    print_next_pointers(r2)

    print("=== Best ===")
    r3 = build_tree()
    best(r3)
    print_next_pointers(r3)
    # Expected for each:
    # 1 -> null
    # 2 -> 3 -> null
    # 4 -> 5 -> 6 -> 7 -> null
