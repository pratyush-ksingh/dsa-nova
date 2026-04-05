"""
Problem: Delete the Middle Node of a Linked List (LeetCode 2095)
Difficulty: MEDIUM | XP: 25

Given the head of a linked list, delete the middle node and return the
modified head. The middle node is defined as the node at index
floor(n / 2) (0-indexed), where n is the number of nodes.

Examples:
  n=1 => index 0 (only node)
  n=2 => index 1 (second node)
  n=3 => index 1 (second node)
  n=5 => index 2 (third node)
"""
from typing import Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


def build_list(values):
    if not values:
        return None
    head = ListNode(values[0])
    cur = head
    for v in values[1:]:
        cur.next = ListNode(v)
        cur = cur.next
    return head


def list_to_arr(head):
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE  (two-pass)
# Time: O(2n) = O(n)  |  Space: O(1)
# Pass 1: count length n. Middle index = n // 2.
# Pass 2: walk to node at index (n//2 - 1) and skip next.
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Count the list length, compute the middle index, then do a
    second traversal to find the predecessor and unlink the middle.
    """
    if not head or not head.next:
        return None  # single node: delete it, return empty list

    # Pass 1: count length
    length = 0
    cur = head
    while cur:
        length += 1
        cur = cur.next

    mid = length // 2  # 0-based index of the middle node

    # Pass 2: walk to node just before middle
    dummy = ListNode(0, head)
    cur = dummy
    for _ in range(mid):
        cur = cur.next

    cur.next = cur.next.next
    return dummy.next


# ============================================================
# APPROACH 2: OPTIMAL  (slow/fast pointers with prev, single pass)
# Time: O(n)  |  Space: O(1)
# slow moves 1 step, fast moves 2 steps.
# When fast reaches the end, slow is at the middle.
# Track prev so we can unlink slow.
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Classic slow/fast pointer to find the middle in one pass.
    We also maintain a 'prev' pointer one step behind 'slow'
    so that when we find the middle, we can do:
        prev.next = slow.next
    A dummy node before head handles the edge case where the
    middle IS the head (single-element list).
    """
    dummy = ListNode(0, head)
    prev = dummy
    slow = head
    fast = head

    while fast and fast.next:
        prev = slow
        slow = slow.next
        fast = fast.next.next

    # slow is now the middle node; unlink it
    prev.next = slow.next
    return dummy.next


# ============================================================
# APPROACH 3: BEST  (same as optimal, explicit fast start ahead)
# Time: O(n)  |  Space: O(1)
# Start fast one node ahead of slow so that when fast exits,
# slow is already at the predecessor — no separate prev needed.
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Modified slow/fast: start slow at head and fast at head.next.next
    so that slow naturally lands at the node BEFORE the middle.
    Then slow.next = slow.next.next unlinks the middle directly.
    This eliminates the need for an explicit 'prev' variable.
    """
    if not head or not head.next:
        return None

    slow = head
    fast = head.next  # fast is one ahead; when fast.next.next is gone, slow is pre-middle

    while fast.next and fast.next.next:
        slow = slow.next
        fast = fast.next.next

    # slow.next is the middle node
    slow.next = slow.next.next
    return head


if __name__ == "__main__":
    test_cases = [
        ([1, 3, 4, 7, 1, 2, 6], [1, 3, 4, 1, 2, 6]),   # n=7, mid=3 (val 7)
        ([1, 2, 3, 4],           [1, 2, 4]),              # n=4, mid=2 (val 3)
        ([2, 1],                 [2]),                    # n=2, mid=1 (val 1)
        ([1],                    []),                     # n=1, mid=0 (val 1)
        ([1, 2, 3],              [1, 3]),                 # n=3, mid=1 (val 2)
    ]

    print("=== Delete Middle Node ===\n")
    for values, expected in test_cases:
        b   = list_to_arr(brute_force(build_list(values)))
        o   = list_to_arr(optimal(build_list(values)))
        bst = list_to_arr(best(build_list(values)))
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] Input: {values}")
        print(f"       Brute:   {b}")
        print(f"       Optimal: {o}")
        print(f"       Best:    {bst}")
        print(f"       Expect:  {expected}\n")
