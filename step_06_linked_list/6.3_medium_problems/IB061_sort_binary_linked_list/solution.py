"""
Problem: Sort Binary Linked List
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a linked list of 0s and 1s, sort it so all 0s come before 1s.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


def build_list(arr: list[int]) -> Optional[ListNode]:
    if not arr:
        return None
    head = ListNode(arr[0])
    curr = head
    for v in arr[1:]:
        curr.next = ListNode(v)
        curr = curr.next
    return head


def list_to_str(head: Optional[ListNode]) -> str:
    parts = []
    while head:
        parts.append(str(head.val))
        head = head.next
    return " -> ".join(parts) if parts else "(empty)"


# ============================================================
# APPROACH 1: BRUTE FORCE -- Count and Overwrite
# Time: O(n)  |  Space: O(1)
#
# Count 0s in one pass. In a second pass, fill the first count0
# nodes with 0 and the rest with 1.
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    if not head:
        return None

    # Pass 1: count zeros
    count0 = 0
    curr = head
    while curr:
        if curr.val == 0:
            count0 += 1
        curr = curr.next

    # Pass 2: overwrite values
    curr = head
    while curr:
        if count0 > 0:
            curr.val = 0
            count0 -= 1
        else:
            curr.val = 1
        curr = curr.next

    return head


# ============================================================
# APPROACH 2: OPTIMAL -- Split into Two Lists
# Time: O(n)  |  Space: O(1)
#
# Collect 0-nodes into one list, 1-nodes into another, then
# join them. Does NOT modify node values.
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    if not head:
        return None

    dummy0, dummy1 = ListNode(-1), ListNode(-1)
    tail0, tail1 = dummy0, dummy1

    curr = head
    while curr:
        if curr.val == 0:
            tail0.next = curr
            tail0 = tail0.next
        else:
            tail1.next = curr
            tail1 = tail1.next
        curr = curr.next

    # Connect 0-list to 1-list
    tail0.next = dummy1.next
    # CRITICAL: terminate to avoid cycle
    tail1.next = None

    return dummy0.next if dummy0.next else dummy1.next


# ============================================================
# APPROACH 3: BEST -- Clean Single-Pass Split
# Time: O(n)  |  Space: O(1)
#
# Same logic, compact and interview-ready.
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    d0, d1 = ListNode(0), ListNode(0)
    t0, t1 = d0, d1

    c = head
    while c:
        if c.val == 0:
            t0.next = c
            t0 = c
        else:
            t1.next = c
            t1 = c
        c = c.next

    t0.next = d1.next   # join: 0-tail -> 1-head
    t1.next = None       # terminate to prevent cycle
    return d0.next if d0.next else d1.next


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Sort Binary Linked List ===\n")

    tests = [
        [1, 0, 1, 0],
        [0, 0, 0],
        [1, 1, 1],
        [1],
        [],
        [0, 1, 0, 1, 0],
    ]

    for t in tests:
        print(f"Input:  {list_to_str(build_list(t))}")
        print(f"Brute:  {list_to_str(brute_force(build_list(t)))}")
        print(f"Optim:  {list_to_str(optimal(build_list(t)))}")
        print(f"Best:   {list_to_str(best(build_list(t)))}")
        print()
