"""
Problem: Convert Sorted List to BST
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Convert a sorted singly linked list to a height-balanced BST.
"""

from typing import Optional, List


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# ============================================================
# APPROACH 1: BRUTE FORCE
# Convert list to array, then apply sorted array -> BST
# Time: O(N)  |  Space: O(N) for the array
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[TreeNode]:
    arr = []
    cur = head
    while cur:
        arr.append(cur.val)
        cur = cur.next

    def helper(left, right):
        if left > right:
            return None
        mid = (left + right) // 2
        node = TreeNode(arr[mid])
        node.left = helper(left, mid - 1)
        node.right = helper(mid + 1, right)
        return node

    return helper(0, len(arr) - 1)


# ============================================================
# APPROACH 2: OPTIMAL
# Slow/fast pointer to find middle, recurse on sub-lists
# Time: O(N log N)  |  Space: O(log N) recursion stack
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[TreeNode]:
    if not head:
        return None
    if not head.next:
        return TreeNode(head.val)

    # Find middle using slow/fast
    prev, slow, fast = None, head, head
    while fast and fast.next:
        prev = slow
        slow = slow.next
        fast = fast.next.next

    # Disconnect left half
    if prev:
        prev.next = None

    root = TreeNode(slow.val)
    root.left = optimal(head) if prev else None
    root.right = optimal(slow.next)
    return root


# ============================================================
# APPROACH 3: BEST
# Inorder simulation — build BST during inorder traversal
# Advances list pointer as nodes are created; O(N) time
# Time: O(N)  |  Space: O(log N)
# ============================================================
def best(head: Optional[ListNode]) -> Optional[TreeNode]:
    # Count list length
    n, cur = 0, head
    while cur:
        n += 1
        cur = cur.next

    ptr = [head]  # mutable pointer

    def convert(left, right):
        if left > right:
            return None
        mid = (left + right) // 2
        left_child = convert(left, mid - 1)
        root = TreeNode(ptr[0].val)
        ptr[0] = ptr[0].next
        root.left = left_child
        root.right = convert(mid + 1, right)
        return root

    return convert(0, n - 1)


def make_list(*vals):
    dummy = ListNode(0)
    cur = dummy
    for v in vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


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
    print("=== Convert Sorted List to BST ===")

    vals = [-10, -3, 0, 5, 9]
    r1 = brute_force(make_list(*vals))
    r2 = optimal(make_list(*vals))
    r3 = best(make_list(*vals))

    print(f"BruteForce inorder: {inorder(r1)}, height={height(r1)}")
    print(f"Optimal    inorder: {inorder(r2)}, height={height(r2)}")
    print(f"Best       inorder: {inorder(r3)}, height={height(r3)}")

    print(f"Single: {inorder(best(make_list(1)))}")   # [1]
    print(f"Two:    {inorder(best(make_list(1,2)))}") # [1,2]
