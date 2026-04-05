"""
Problem: Reverse Alternate K Nodes
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a linked list and integer K:
  - Reverse the first K nodes
  - Skip the next K nodes (leave them as-is)
  - Repeat until end of list
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
# APPROACH 1: BRUTE FORCE  (Collect values, rebuild)
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    Collect all values into a list, apply the alternating reverse/skip pattern,
    then rebuild the linked list.
    Real-life: Alternating audio channel reversal in signal processing pipelines.
    """
    vals = []
    cur = head
    while cur:
        vals.append(cur.val)
        cur = cur.next

    n = len(vals)
    result = [0] * n
    i = 0
    while i < n:
        end = min(i + k, n)
        # Reverse block
        for j in range(i, end):
            result[j] = vals[i + end - 1 - j]
        i = end
        # Skip block
        skip_end = min(i + k, n)
        for j in range(i, skip_end):
            result[j] = vals[j]
        i = skip_end

    dummy = ListNode(-1)
    cur = dummy
    for v in result:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


# ============================================================
# APPROACH 2: OPTIMAL  (In-place pointer manipulation)
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    In-place: alternate between reversing k nodes and skipping k nodes.
    Real-life: In-place packet reordering in network routers.
    """
    if not head or k <= 1:
        return head

    dummy = ListNode(-1)
    dummy.next = head
    prev_group_tail = dummy
    curr = head

    while curr:
        # Check at least k nodes remain
        check = curr
        for _ in range(k):
            if not check:
                return dummy.next
            check = check.next

        # Reverse k nodes
        prev = None
        node = curr
        group_tail = curr  # after reversal, curr becomes tail
        for _ in range(k):
            if not node:
                break
            nxt = node.next
            node.next = prev
            prev = node
            node = nxt
        # prev = new head, group_tail = tail of reversed block
        prev_group_tail.next = prev
        group_tail.next = node
        prev_group_tail = group_tail
        curr = node

        # Skip k nodes
        for _ in range(k):
            if not curr:
                break
            prev_group_tail = curr
            curr = curr.next

    return dummy.next


# ============================================================
# APPROACH 3: BEST  (Recursive)
# Time: O(n)  |  Space: O(n/k) recursion stack
# ============================================================
def best(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    Recursive version: reverse k, skip k, recurse on the rest.
    Real-life: Recursive data stream transformations in functional pipelines.
    """
    def reverse_alternate(node: Optional[ListNode], should_reverse: bool) -> Optional[ListNode]:
        if not node:
            return None
        if not should_reverse:
            # Skip k nodes
            curr = node
            for _ in range(k - 1):
                if not curr.next:
                    break
                curr = curr.next
            curr.next = reverse_alternate(curr.next, True)
            return node

        # Check k nodes available
        check = node
        for _ in range(k):
            if not check:
                return node  # fewer than k, leave as-is
            check = check.next

        # Reverse k nodes
        prev = None
        curr = node
        group_tail = node
        for _ in range(k):
            if not curr:
                break
            nxt = curr.next
            curr.next = prev
            prev = curr
            curr = nxt
        group_tail.next = reverse_alternate(curr, False)
        return prev

    if not head or k <= 1:
        return head
    return reverse_alternate(head, True)


if __name__ == "__main__":
    print("=== Reverse Alternate K Nodes ===")
    tests = [
        ([1, 2, 3, 4, 5, 6, 7, 8], 2, [2, 1, 3, 4, 6, 5, 7, 8]),
        ([1, 2, 3, 4, 5],           3, [3, 2, 1, 4, 5]),
        ([1, 2, 3, 4, 5, 6],        2, [2, 1, 3, 4, 6, 5]),
    ]
    for vals, k, exp in tests:
        print(f"\nInput: {vals}  K={k}  =>  expected: {exp}")
        print(f"  Brute:   {to_list(brute_force(build(vals), k))}")
        print(f"  Optimal: {to_list(optimal(build(vals), k))}")
        print(f"  Best:    {to_list(best(build(vals), k))}")
