"""
Problem: Rotate a Linked List (LeetCode #61)
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- Rotate One at a Time
# Time: O(n * k)  |  Space: O(1)
#
# Perform k individual right-rotations. Each rotation moves
# the last node to the front.
# ============================================================
def brute_force(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    if not head or not head.next or k == 0:
        return head

    # Find length to reduce k
    length = 0
    curr = head
    while curr:
        length += 1
        curr = curr.next
    k = k % length
    if k == 0:
        return head

    for _ in range(k):
        # Find second-to-last node
        prev = None
        curr = head
        while curr.next:
            prev = curr
            curr = curr.next
        # curr is the last node, prev is second-to-last
        prev.next = None
        curr.next = head
        head = curr

    return head


# ============================================================
# APPROACH 2: OPTIMAL -- Find Length, Make Circular, Break
# Time: O(n)  |  Space: O(1)
#
# Compute length, k %= length, connect tail->head (circular),
# walk (len-k) steps from tail to find new tail, break.
# ============================================================
def optimal(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    if not head or not head.next or k == 0:
        return head

    # Find length and tail
    length = 1
    tail = head
    while tail.next:
        length += 1
        tail = tail.next

    k = k % length
    if k == 0:
        return head

    # Make circular
    tail.next = head

    # Walk (length - k) steps from tail to find new tail
    steps = length - k
    new_tail = tail
    for _ in range(steps):
        new_tail = new_tail.next

    new_head = new_tail.next
    new_tail.next = None

    return new_head


# ============================================================
# APPROACH 3: BEST -- Same Approach, Clean Single Pass
# Time: O(n)  |  Space: O(1)
#
# Same circular trick, most concise implementation.
# ============================================================
def best(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    if not head or not head.next:
        return head

    # Find length and tail in one pass
    tail, length = head, 1
    while tail.next:
        tail, length = tail.next, length + 1

    k %= length
    if k == 0:
        return head

    tail.next = head  # make circular
    # Walk to new tail: (length - k) steps from tail
    for _ in range(length - k):
        tail = tail.next
    head = tail.next
    tail.next = None
    return head


# ---- Helpers ----
def build_list(values):
    if not values:
        return None
    head = ListNode(values[0])
    curr = head
    for v in values[1:]:
        curr.next = ListNode(v)
        curr = curr.next
    return head


def to_list(head):
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


if __name__ == "__main__":
    print("=== Rotate a Linked List ===\n")

    tests = [
        ([1, 2, 3, 4, 5], 2, [4, 5, 1, 2, 3]),
        ([0, 1, 2], 4, [2, 0, 1]),
        ([1], 0, [1]),
        ([1, 2], 1, [2, 1]),
    ]

    for values, k, expected in tests:
        print(f"Input: {values}, k={k}")
        print(f"Expected: {expected}")
        print(f"Brute:    {to_list(brute_force(build_list(values), k))}")
        print(f"Optimal:  {to_list(optimal(build_list(values), k))}")
        print(f"Best:     {to_list(best(build_list(values), k))}")
        print()
