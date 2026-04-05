"""
Problem: List Cycle
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a linked list, return the node where the cycle begins.
If there is no cycle, return None.

Real-life use case: Detecting circular references in dependency graphs,
infinite loops in OS process scheduling queues.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.next: Optional["ListNode"] = None


def build_list(vals: list[int], pos: int) -> Optional[ListNode]:
    """Helper: build list with cycle at index pos (-1 = no cycle)."""
    if not vals:
        return None
    nodes = [ListNode(v) for v in vals]
    for i in range(len(nodes) - 1):
        nodes[i].next = nodes[i + 1]
    if pos >= 0:
        nodes[-1].next = nodes[pos]
    return nodes[0]


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Store visited nodes in a set. First node seen twice is cycle start.
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    visited = set()
    curr = head
    while curr:
        if id(curr) in visited:
            return curr
        visited.add(id(curr))
        curr = curr.next
    return None


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# Floyd's cycle detection.
# Phase 1: slow moves 1 step, fast moves 2 steps until they meet.
# Phase 2: reset slow to head, advance both 1 step at a time until equal.
# Meeting point is the cycle start (mathematical proof via distance equations).
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    slow = fast = head
    # Phase 1: detect
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            break
    else:
        return None  # no cycle

    # Phase 2: find start
    slow = head
    while slow is not fast:
        slow = slow.next
        fast = fast.next
    return slow


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# Floyd's with explicit cycle-length calculation.
# After detecting the meeting node, count cycle length L,
# then advance one pointer L steps ahead from head; move both together.
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    slow = fast = head
    # Phase 1: detect
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            break
    else:
        return None

    # Count cycle length
    cycle_len = 1
    temp = slow.next
    while temp is not slow:
        cycle_len += 1
        temp = temp.next

    # Phase 2: two pointers distance cycle_len apart
    p1 = p2 = head
    for _ in range(cycle_len):
        p2 = p2.next
    while p1 is not p2:
        p1 = p1.next
        p2 = p2.next
    return p1


if __name__ == "__main__":
    print("=== List Cycle ===")

    # Test 1: 3->2->0->-4->back to index 1 (val=2)
    h1 = build_list([3, 2, 0, -4], 1)
    r1 = brute_force(h1)
    print(f"Brute   (cycle at val=2): {r1.val if r1 else None}")   # 2

    h2 = build_list([3, 2, 0, -4], 1)
    r2 = optimal(h2)
    print(f"Optimal (cycle at val=2): {r2.val if r2 else None}")   # 2

    h3 = build_list([3, 2, 0, -4], 1)
    r3 = best(h3)
    print(f"Best    (cycle at val=2): {r3.val if r3 else None}")   # 2

    # Test 2: no cycle
    h4 = build_list([1, 2, 3], -1)
    print(f"\nNo cycle Brute:   {brute_force(h4)}")    # None
    print(f"No cycle Optimal: {optimal(h4)}")          # None
    print(f"No cycle Best:    {best(h4)}")              # None

    # Test 3: cycle at head (pos=0)
    h5 = build_list([1, 2], 0)
    r5 = optimal(h5)
    print(f"\nCycle at head Optimal (val=1): {r5.val if r5 else None}")  # 1
