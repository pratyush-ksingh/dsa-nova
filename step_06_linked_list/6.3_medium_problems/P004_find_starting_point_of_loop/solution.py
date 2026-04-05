"""
Problem: Find Starting Point of Loop (LeetCode #142)
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashSet
# Time: O(n)  |  Space: O(n)
#
# Traverse the list, store visited nodes in a set.
# The first repeated node is the cycle start.
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    visited = set()
    curr = head
    while curr:
        if curr in visited:
            return curr
        visited.add(curr)
        curr = curr.next
    return None


# ============================================================
# APPROACH 2: OPTIMAL -- Floyd's Cycle Detection
# Time: O(n)  |  Space: O(1)
#
# Phase 1: slow/fast pointers meet inside the cycle.
# Phase 2: reset one to head, walk both at speed 1.
# They meet at the cycle start.
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    slow = head
    fast = head

    # Phase 1: Detect cycle (find meeting point)
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow == fast:
            break
    else:
        # No cycle: fast reached end
        return None

    # Phase 2: Find cycle start
    slow = head
    while slow != fast:
        slow = slow.next
        fast = fast.next

    return slow


# ============================================================
# APPROACH 3: BEST -- Floyd's (Cleanest Implementation)
# Time: O(n)  |  Space: O(1)
#
# Same Floyd's algorithm, minimal code.
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    slow = fast = head
    while fast and fast.next:
        slow, fast = slow.next, fast.next.next
        if slow == fast:
            slow = head
            while slow != fast:
                slow, fast = slow.next, fast.next
            return slow
    return None


# ---- Helper functions for testing ----
def build_list_with_cycle(values, pos):
    """Build a linked list from values. pos = index where tail connects (-1 for no cycle)."""
    if not values:
        return None
    nodes = [ListNode(v) for v in values]
    for i in range(len(nodes) - 1):
        nodes[i].next = nodes[i + 1]
    if pos >= 0:
        nodes[-1].next = nodes[pos]
    return nodes[0], (nodes[pos] if pos >= 0 else None)


if __name__ == "__main__":
    print("=== Find Starting Point of Loop ===\n")

    # Test 1: [3,2,0,-4], tail -> node 1
    head, expected = build_list_with_cycle([3, 2, 0, -4], 1)
    r1 = brute_force(head)
    r2 = optimal(head)
    r3 = best(head)
    print(f"Test 1: [3,2,0,-4] cycle at index 1")
    print(f"  Expected: val={expected.val}")
    print(f"  Brute:    val={r1.val if r1 else None}")
    print(f"  Optimal:  val={r2.val if r2 else None}")
    print(f"  Best:     val={r3.val if r3 else None}")
    print()

    # Test 2: [1,2], tail -> node 0
    head, expected = build_list_with_cycle([1, 2], 0)
    r1 = brute_force(head)
    r2 = optimal(head)
    r3 = best(head)
    print(f"Test 2: [1,2] cycle at index 0")
    print(f"  Expected: val={expected.val}")
    print(f"  Brute:    val={r1.val if r1 else None}")
    print(f"  Optimal:  val={r2.val if r2 else None}")
    print(f"  Best:     val={r3.val if r3 else None}")
    print()

    # Test 3: [1], no cycle
    head, expected = build_list_with_cycle([1], -1)
    r1 = brute_force(head)
    r2 = optimal(head)
    r3 = best(head)
    print(f"Test 3: [1] no cycle")
    print(f"  Expected: {expected}")
    print(f"  Brute:    {r1}")
    print(f"  Optimal:  {r2}")
    print(f"  Best:     {r3}")
    print()
