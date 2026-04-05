"""
Problem: Reverse a Doubly Linked List
Difficulty: EASY | XP: 10

Given the head of a doubly linked list, reverse it by swapping
prev and next pointers at every node. Return the new head.
"""
from typing import Optional


class DLLNode:
    def __init__(self, val: int = 0, prev=None, next=None):
        self.val = val
        self.prev = prev
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- Stack-Based Value Reversal
# Time: O(n)  |  Space: O(n)
# Push all values to a stack, then pop and overwrite.
# ============================================================
def brute_force(head: Optional[DLLNode]) -> Optional[DLLNode]:
    if head is None:
        return None

    # Pass 1: Collect all values
    stack = []
    current = head
    while current:
        stack.append(current.val)
        current = current.next

    # Pass 2: Overwrite values in reverse order
    current = head
    while current:
        current.val = stack.pop()
        current = current.next

    return head  # Same head node, reversed values


# ============================================================
# APPROACH 2: OPTIMAL -- Iterative Pointer Swap
# Time: O(n)  |  Space: O(1)
# Walk through each node, swap prev and next pointers.
# ============================================================
def optimal(head: Optional[DLLNode]) -> Optional[DLLNode]:
    if head is None:
        return None

    current = head
    new_head = None

    while current is not None:
        # Swap prev and next
        current.prev, current.next = current.next, current.prev

        # This node might be the new head
        new_head = current

        # Move forward: after swap, original next is now current.prev
        current = current.prev

    return new_head


# ============================================================
# APPROACH 3: BEST -- Clean Iterative with Explicit Last Tracking
# Time: O(n)  |  Space: O(1)
# Same algorithm, cleanest form with explicit last-node tracking.
# ============================================================
def best(head: Optional[DLLNode]) -> Optional[DLLNode]:
    if head is None:
        return None

    current = head
    last = None

    while current is not None:
        last = current

        # Swap prev and next pointers
        current.prev, current.next = current.next, current.prev

        # Advance to the next unprocessed node (original next, now prev)
        current = current.prev

    return last  # The last node we visited is the new head


def build_dll(values: list) -> Optional[DLLNode]:
    """Helper: build a DLL from a Python list."""
    if not values:
        return None
    head = DLLNode(values[0])
    current = head
    for v in values[1:]:
        node = DLLNode(v)
        current.next = node
        node.prev = current
        current = node
    return head


def dll_to_string(head: Optional[DLLNode]) -> str:
    """Helper: convert DLL to string for display."""
    if head is None:
        return "null"
    parts = []
    current = head
    while current:
        parts.append(str(current.val))
        current = current.next
    return " <-> ".join(parts)


if __name__ == "__main__":
    print("=== Reverse a Doubly Linked List ===\n")

    # Test 1: Brute Force
    h1 = build_dll([1, 2, 3, 4])
    print(f"Input:       {dll_to_string(h1)}")
    h1 = brute_force(h1)
    print(f"Brute Force: {dll_to_string(h1)}")

    # Test 2: Optimal
    h2 = build_dll([1, 2, 3, 4])
    h2 = optimal(h2)
    print(f"Optimal:     {dll_to_string(h2)}")

    # Test 3: Best
    h3 = build_dll([1, 2, 3, 4])
    h3 = best(h3)
    print(f"Best:        {dll_to_string(h3)}")

    # Test 4: Single node
    single = build_dll([5])
    single = best(single)
    print(f"\nSingle node: {dll_to_string(single)}")

    # Test 5: Two nodes
    two = build_dll([1, 2])
    two = best(two)
    print(f"Two nodes:   {dll_to_string(two)}")

    # Test 6: Empty list
    empty = best(None)
    print(f"Empty list:  {dll_to_string(empty)}")
