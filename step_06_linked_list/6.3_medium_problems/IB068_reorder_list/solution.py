"""
Problem: Reorder List
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a linked list L0 -> L1 -> ... -> Ln-1 -> Ln,
reorder it to: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...
Modify in-place. Do not change node values.
"""
from typing import Optional, List


class ListNode:
    def __init__(self, val: int = 0, next: 'Optional[ListNode]' = None):
        self.val = val
        self.next = next


def build(vals: List[int]) -> Optional[ListNode]:
    dummy = ListNode(-1)
    cur = dummy
    for v in vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


def to_list(head: Optional[ListNode]) -> List[int]:
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE  (Store nodes + two-pointer interleave)
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(head: Optional[ListNode]) -> None:
    """
    Store all nodes in a list, then use two pointers from both ends to relink.
    Real-life: Interleaving two data streams into a single combined feed.
    """
    if not head or not head.next:
        return
    nodes = []
    cur = head
    while cur:
        nodes.append(cur)
        cur = cur.next

    lo, hi = 0, len(nodes) - 1
    while lo < hi:
        nodes[lo].next = nodes[hi]
        lo += 1
        if lo == hi:
            break
        nodes[hi].next = nodes[lo]
        hi -= 1
    nodes[lo].next = None


# ============================================================
# APPROACH 2: OPTIMAL  (Find middle + reverse second half + merge)
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(head: Optional[ListNode]) -> None:
    """
    Three steps:
    1. Find the middle (slow/fast pointers)
    2. Reverse the second half
    3. Interleave first and reversed second halves
    Real-life: In-place list restructuring in memory-constrained systems.
    """
    if not head or not head.next:
        return

    # Step 1: Find middle
    slow, fast = head, head
    while fast.next and fast.next.next:
        slow = slow.next
        fast = fast.next.next

    # Step 2: Reverse second half
    def reverse(node: Optional[ListNode]) -> Optional[ListNode]:
        prev, curr = None, node
        while curr:
            nxt = curr.next
            curr.next = prev
            prev = curr
            curr = nxt
        return prev

    second = reverse(slow.next)
    slow.next = None  # cut first half

    # Step 3: Merge
    first = head
    while second:
        f1 = first.next
        s1 = second.next
        first.next = second
        second.next = f1
        first = f1
        second = s1


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(head: Optional[ListNode]) -> None:
    """
    Same as optimal — O(n)/O(1) is already the best achievable.
    Real-life: Media playlist shuffle that alternates first and last items.
    """
    optimal(head)


if __name__ == "__main__":
    print("=== Reorder List ===")
    tests = [
        ([1, 2, 3, 4],    [1, 4, 2, 3]),
        ([1, 2, 3, 4, 5], [1, 5, 2, 4, 3]),
        ([1],             [1]),
        ([1, 2],          [1, 2]),
    ]
    for vals, exp in tests:
        print(f"\nInput: {vals}  =>  expected: {exp}")
        h1 = build(vals)
        brute_force(h1)
        print(f"  Brute:   {to_list(h1)}")
        h2 = build(vals)
        optimal(h2)
        print(f"  Optimal: {to_list(h2)}")
        h3 = build(vals)
        best(h3)
        print(f"  Best:    {to_list(h3)}")
