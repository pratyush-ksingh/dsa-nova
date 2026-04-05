"""
Problem: Length of Loop in Linked List
Difficulty: MEDIUM | XP: 25
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'Optional[ListNode]' = None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashMap of visited nodes
# Time: O(n)  |  Space: O(n)
#
# Traverse the list. Store each node along with the step number
# in a dict. When we see a node already in the dict, a cycle
# exists: length = current_step - step_when_first_seen.
# ============================================================
def brute_force(head: Optional[ListNode]) -> int:
    visited = {}
    step = 0
    curr = head
    while curr:
        if curr in visited:
            return step - visited[curr]
        visited[curr] = step
        curr = curr.next
        step += 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL -- Floyd's Cycle Detection + Count
# Time: O(n)  |  Space: O(1)
#
# Phase 1: Use slow/fast pointers to find the meeting point.
# Phase 2: From the meeting point, keep one pointer fixed and
#           move the other until it returns -- count the steps.
# ============================================================
def optimal(head: Optional[ListNode]) -> int:
    slow = fast = head
    # Phase 1: detect cycle
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow == fast:
            # Phase 2: count loop length from meeting point
            length = 1
            curr = slow.next
            while curr != slow:
                length += 1
                curr = curr.next
            return length
    return 0


# ============================================================
# APPROACH 3: BEST -- Same Floyd's, slightly more explicit
# Time: O(n)  |  Space: O(1)
#
# Same as Optimal. After meeting, use a dedicated counter loop.
# Making the two phases crystal clear aids interview readability.
# ============================================================
def best(head: Optional[ListNode]) -> int:
    slow = fast = head
    has_cycle = False

    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow == fast:
            has_cycle = True
            break

    if not has_cycle:
        return 0

    # Count loop length
    meeting = slow
    length = 1
    ptr = meeting.next
    while ptr != meeting:
        length += 1
        ptr = ptr.next
    return length


# --------------- helpers for testing ---------------
def build_list_with_cycle(values: list, cycle_pos: int) -> Optional[ListNode]:
    """Build a linked list; cycle_pos = index node where tail connects (-1 = no cycle)."""
    if not values:
        return None
    nodes = [ListNode(v) for v in values]
    for i in range(len(nodes) - 1):
        nodes[i].next = nodes[i + 1]
    if cycle_pos >= 0:
        nodes[-1].next = nodes[cycle_pos]
    return nodes[0]


if __name__ == "__main__":
    print("=== Length of Loop in Linked List ===\n")

    tests = [
        # (values, cycle_pos, expected_length)
        ([1, 2, 3, 4, 5], 1, 4),   # cycle: 2->3->4->5->2
        ([1, 2, 3, 4, 5], 0, 5),   # full cycle
        ([1, 2, 3], -1, 0),         # no cycle
        ([1], 0, 1),                 # single node cycle
    ]

    for values, pos, expected in tests:
        head = build_list_with_cycle(values, pos)
        b = brute_force(head)
        head = build_list_with_cycle(values, pos)
        o = optimal(head)
        head = build_list_with_cycle(values, pos)
        bt = best(head)
        status = "OK" if b == o == bt == expected else "MISMATCH"
        print(f"List={values}, cycle_pos={pos}  "
              f"Expected={expected}  Brute={b}  Optimal={o}  Best={bt}  [{status}]")
