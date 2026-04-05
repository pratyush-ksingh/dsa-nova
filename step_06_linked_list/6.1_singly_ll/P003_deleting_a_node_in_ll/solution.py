"""
Problem: Deleting a Node in Singly Linked List
Difficulty: EASY | XP: 10

Delete node at beginning, end, and given position in singly linked list.
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
# DELETE FROM BEGINNING
# Time: O(1)  |  Space: O(1)
# Just move head to head.next.
# ============================================================
def delete_from_beginning(head: Optional[ListNode]) -> Optional[ListNode]:
    """Remove the head node, return new head."""
    if not head:
        return None
    new_head = head.next
    head.next = None  # disconnect old head
    return new_head


# ============================================================
# DELETE FROM END
# Time: O(n)  |  Space: O(1)
# Traverse to second-to-last, set its next to None.
# ============================================================
def delete_from_end(head: Optional[ListNode]) -> Optional[ListNode]:
    """Remove the last node."""
    if not head:
        return None

    # Single node
    if not head.next:
        return None

    # Walk to second-to-last
    current = head
    while current.next.next:
        current = current.next

    current.next = None  # detach last node
    return head


# ============================================================
# DELETE AT GIVEN POSITION (1-indexed)
# Time: O(k)  |  Space: O(1)
# Traverse to position k-1, skip the k-th node.
# ============================================================
def delete_at_position(head: Optional[ListNode], position: int) -> Optional[ListNode]:
    """Delete the node at position k (1-indexed)."""
    if not head or position < 1:
        return head

    # Position 1 = delete head
    if position == 1:
        new_head = head.next
        head.next = None
        return new_head

    # Traverse to node before position k
    prev = head
    for _ in range(position - 2):
        if not prev.next:
            print(f"Position {position} out of bounds.")
            return head
        prev = prev.next

    # Check if target exists
    if not prev.next:
        print(f"Position {position} out of bounds.")
        return head

    # Skip target node
    target = prev.next
    prev.next = target.next
    target.next = None
    return head


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
    print("=== Deleting a Node in LL ===\n")

    # Delete from beginning
    head1 = build_from_list([10, 20, 30, 40])
    head1 = delete_from_beginning(head1)
    print(f"Delete beginning: {print_list(head1)}")  # 20 -> 30 -> 40

    # Delete from end
    head2 = build_from_list([10, 20, 30, 40])
    head2 = delete_from_end(head2)
    print(f"Delete end:       {print_list(head2)}")  # 10 -> 20 -> 30

    # Delete at position 3
    head3 = build_from_list([10, 20, 30, 40, 50])
    head3 = delete_at_position(head3, 3)
    print(f"Delete pos 3:     {print_list(head3)}")  # 10 -> 20 -> 40 -> 50

    # Edge: delete head via position
    head4 = build_from_list([10, 20, 30])
    head4 = delete_at_position(head4, 1)
    print(f"Delete pos 1:     {print_list(head4)}")  # 20 -> 30

    # Edge: single node
    head5 = build_from_list([99])
    head5 = delete_from_beginning(head5)
    print(f"Single node:      {print_list(head5)}")  # (empty)

    # Edge: empty list
    print(f"Empty list:       {print_list(delete_from_beginning(None))}")

    # Edge: out of bounds
    head6 = build_from_list([10, 20, 30])
    head6 = delete_at_position(head6, 10)
    print(f"Out of bounds:    {print_list(head6)}")  # 10 -> 20 -> 30
