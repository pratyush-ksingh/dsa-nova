"""
Problem: Detect Loop in Linked List
Difficulty: EASY | XP: 10

Given the head of a singly linked list, determine if it contains a cycle.
LeetCode #141 - Linked List Cycle
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next=None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- HashSet Visited Tracking
# Time: O(n)  |  Space: O(n)
# Store each visited node in a set. If we revisit, there's a cycle.
# ============================================================
def brute_force(head: Optional[ListNode]) -> bool:
    visited = set()
    current = head

    while current is not None:
        if current in visited:
            return True  # Already seen this node -- cycle!
        visited.add(current)
        current = current.next

    return False  # Reached None -- no cycle


# ============================================================
# APPROACH 2: OPTIMAL -- Floyd's Tortoise and Hare
# Time: O(n)  |  Space: O(1)
# Two pointers: slow (1 step) and fast (2 steps). If they meet, cycle exists.
# ============================================================
def optimal(head: Optional[ListNode]) -> bool:
    slow = head
    fast = head

    while fast is not None and fast.next is not None:
        slow = slow.next          # Move 1 step
        fast = fast.next.next     # Move 2 steps

        if slow is fast:
            return True           # They met inside the cycle

    return False  # Fast reached None -- no cycle


# ============================================================
# APPROACH 3: BEST -- Floyd's with Cycle Start Detection
# Time: O(n)  |  Space: O(1)
# Same Floyd's detection, plus utility to find where the cycle begins.
# ============================================================
def best(head: Optional[ListNode]) -> bool:
    slow = head
    fast = head

    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            return True

    return False


def find_cycle_start(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Bonus: Find the node where the cycle begins (None if no cycle).
    After slow and fast meet, reset one to head.
    Move both at speed 1 -- they meet at the cycle start.
    """
    slow = head
    fast = head

    # Phase 1: Detect cycle
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            # Phase 2: Find cycle start
            slow = head
            while slow is not fast:
                slow = slow.next
                fast = fast.next
            return slow  # Cycle start node

    return None  # No cycle


if __name__ == "__main__":
    print("=== Detect Loop in Linked List ===\n")

    # Test 1: List with cycle (1->2->3->4->2)
    n1, n2, n3, n4 = ListNode(1), ListNode(2), ListNode(3), ListNode(4)
    n1.next, n2.next, n3.next, n4.next = n2, n3, n4, n2  # cycle at 2

    print("Test 1: 1->2->3->4->2 (cycle)")
    print(f"  Brute Force: {brute_force(n1)}")
    print(f"  Optimal:     {optimal(n1)}")
    print(f"  Best:        {best(n1)}")
    print(f"  Cycle start: {find_cycle_start(n1).val}")

    # Test 2: No cycle (1->2->3->None)
    a1, a2, a3 = ListNode(1), ListNode(2), ListNode(3)
    a1.next, a2.next = a2, a3

    print("\nTest 2: 1->2->3->None (no cycle)")
    print(f"  Brute Force: {brute_force(a1)}")
    print(f"  Optimal:     {optimal(a1)}")
    print(f"  Best:        {best(a1)}")

    # Test 3: Single node, no cycle
    single = ListNode(1)
    print("\nTest 3: Single node (no cycle)")
    print(f"  Brute Force: {brute_force(single)}")
    print(f"  Optimal:     {optimal(single)}")

    # Test 4: Single node pointing to itself
    self_loop = ListNode(1)
    self_loop.next = self_loop
    print("\nTest 4: Single node self-loop")
    print(f"  Brute Force: {brute_force(self_loop)}")
    print(f"  Optimal:     {optimal(self_loop)}")

    # Test 5: Empty list
    print("\nTest 5: Empty list")
    print(f"  Brute Force: {brute_force(None)}")
    print(f"  Optimal:     {optimal(None)}")
