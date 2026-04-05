"""
Problem: Reverse a Linked List
Difficulty: EASY | XP: 10
LeetCode #206

Reverse a singly linked list iteratively and recursively.
"""
from typing import Optional, List


# ============================================================
# NODE DEFINITION
# ============================================================
class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- USING ARRAY
# Time: O(n)  |  Space: O(n)
# Store values, overwrite in reverse. Changes values, not pointers.
# ============================================================
def reverse_brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    """Reverse by collecting values and overwriting."""
    if not head:
        return None

    # Collect values
    vals = []
    temp = head
    while temp:
        vals.append(temp.val)
        temp = temp.next

    # Overwrite in reverse
    temp = head
    for val in reversed(vals):
        temp.val = val
        temp = temp.next

    return head


# ============================================================
# APPROACH 2: ITERATIVE (OPTIMAL)
# Time: O(n)  |  Space: O(1)
# Three-pointer technique: prev, curr, next.
# ============================================================
def reverse_iterative(head: Optional[ListNode]) -> Optional[ListNode]:
    """Reverse by flipping pointers one by one."""
    prev = None
    curr = head

    while curr:
        nxt = curr.next   # save next
        curr.next = prev  # reverse arrow
        prev = curr       # advance prev
        curr = nxt        # advance curr

    return prev  # new head


# ============================================================
# APPROACH 3: RECURSIVE
# Time: O(n)  |  Space: O(n) call stack
# Reverse the rest, then fix the backward link.
# ============================================================
def reverse_recursive(head: Optional[ListNode]) -> Optional[ListNode]:
    """Reverse recursively -- base case propagates new head."""
    # Base case: empty or single node
    if not head or not head.next:
        return head

    # Reverse everything after head
    new_head = reverse_recursive(head.next)

    # head.next is now tail of reversed sublist; point it back
    head.next.next = head
    head.next = None  # head becomes the new tail

    return new_head


# ============================================================
# UTILITY: Build list from array
# ============================================================
def build_from_list(arr: List[int]) -> Optional[ListNode]:
    if not arr:
        return None
    head = ListNode(arr[0])
    current = head
    for val in arr[1:]:
        current.next = ListNode(val)
        current = current.next
    return head


# ============================================================
# UTILITY: Print list
# ============================================================
def print_list(head: Optional[ListNode]) -> str:
    parts = []
    current = head
    while current:
        parts.append(str(current.val))
        current = current.next
    return " -> ".join(parts) if parts else "(empty)"


if __name__ == "__main__":
    print("=== Reverse a Linked List ===\n")

    # Approach 1: Brute force
    head1 = build_from_list([1, 2, 3, 4, 5])
    print(f"Brute force: {print_list(reverse_brute_force(head1))}")

    # Approach 2: Iterative
    head2 = build_from_list([1, 2, 3, 4, 5])
    print(f"Iterative:   {print_list(reverse_iterative(head2))}")

    # Approach 3: Recursive
    head3 = build_from_list([1, 2, 3, 4, 5])
    print(f"Recursive:   {print_list(reverse_recursive(head3))}")

    # Edge cases
    print("\n--- Edge Cases ---")
    print(f"Empty:       {print_list(reverse_iterative(None))}")
    print(f"Single node: {print_list(reverse_iterative(build_from_list([42])))}")
    print(f"Two nodes:   {print_list(reverse_iterative(build_from_list([1, 2])))}")
