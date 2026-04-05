"""
Problem: Delete Node in Doubly Linked List
Difficulty: EASY | XP: 10

Delete node at beginning, end, and given position in doubly linked list.
"""
from typing import Optional, List


# ============================================================
# NODE DEFINITION
# ============================================================
class ListNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.prev: Optional['ListNode'] = None
        self.next: Optional['ListNode'] = None


# ============================================================
# DELETE FROM BEGINNING
# Time: O(1)  |  Space: O(1)
# ============================================================
def delete_from_beginning(head: Optional[ListNode]) -> Optional[ListNode]:
    """Remove the head node, return new head."""
    if not head:
        return None

    new_head = head.next
    if new_head:
        new_head.prev = None
    head.next = None
    return new_head


# ============================================================
# DELETE FROM END
# Time: O(n)  |  Space: O(1)
# Traverse to tail, unlink via tail.prev.
# ============================================================
def delete_from_end(head: Optional[ListNode]) -> Optional[ListNode]:
    """Remove the last node."""
    if not head:
        return None

    # Single node
    if not head.next:
        return None

    # Find tail
    tail = head
    while tail.next:
        tail = tail.next

    tail.prev.next = None
    tail.prev = None
    return head


# ============================================================
# DELETE AT GIVEN POSITION (1-indexed)
# Time: O(k)  |  Space: O(1)
# ============================================================
def delete_at_position(head: Optional[ListNode], position: int) -> Optional[ListNode]:
    """Delete the node at position k (1-indexed)."""
    if not head or position < 1:
        return head

    if position == 1:
        return delete_from_beginning(head)

    # Traverse to k-th node
    target = head
    for _ in range(position - 1):
        if not target:
            print(f"Position {position} out of bounds.")
            return head
        target = target.next

    if not target:
        print(f"Position {position} out of bounds.")
        return head

    # Rewire predecessor and successor
    if target.prev:
        target.prev.next = target.next
    if target.next:
        target.next.prev = target.prev

    target.prev = None
    target.next = None
    return head


# ============================================================
# BONUS: DELETE A GIVEN NODE REFERENCE -- O(1)
# Time: O(1)  |  Space: O(1)
# ============================================================
def delete_node(head: Optional[ListNode], target: ListNode) -> Optional[ListNode]:
    """Delete a node given its direct reference. O(1)."""
    if not target:
        return head

    if target.prev:
        target.prev.next = target.next
    else:
        head = target.next  # target was head

    if target.next:
        target.next.prev = target.prev

    target.prev = None
    target.next = None
    return head


# ============================================================
# UTILITY: Build DLL from list
# ============================================================
def build_from_list(arr: list) -> Optional[ListNode]:
    if not arr:
        return None
    head = ListNode(arr[0])
    current = head
    for val in arr[1:]:
        node = ListNode(val)
        current.next = node
        node.prev = current
        current = node
    return head


# ============================================================
# UTILITY: Print DLL forward
# ============================================================
def print_forward(head: Optional[ListNode]) -> str:
    parts = []
    current = head
    while current:
        parts.append(str(current.val))
        current = current.next
    return " <-> ".join(parts) if parts else "(empty)"


# ============================================================
# UTILITY: Find node by value
# ============================================================
def find_node(head: Optional[ListNode], val: int) -> Optional[ListNode]:
    current = head
    while current:
        if current.val == val:
            return current
        current = current.next
    return None


if __name__ == "__main__":
    print("=== Delete Node in Doubly LL ===\n")

    # Delete from beginning
    head1 = build_from_list([10, 20, 30, 40])
    head1 = delete_from_beginning(head1)
    print(f"Delete beginning: {print_forward(head1)}")  # 20 <-> 30 <-> 40

    # Delete from end
    head2 = build_from_list([10, 20, 30, 40])
    head2 = delete_from_end(head2)
    print(f"Delete end:       {print_forward(head2)}")  # 10 <-> 20 <-> 30

    # Delete at position 3
    head3 = build_from_list([10, 20, 30, 40, 50])
    head3 = delete_at_position(head3, 3)
    print(f"Delete pos 3:     {print_forward(head3)}")  # 10 <-> 20 <-> 40 <-> 50

    # Delete node by reference
    head4 = build_from_list([10, 20, 30, 40, 50])
    node30 = find_node(head4, 30)
    head4 = delete_node(head4, node30)
    print(f"Delete node(30):  {print_forward(head4)}")  # 10 <-> 20 <-> 40 <-> 50

    # Edge: single node
    head5 = build_from_list([99])
    head5 = delete_from_beginning(head5)
    print(f"Single node:      {print_forward(head5)}")  # (empty)

    # Edge: empty list
    print(f"Empty list:       {print_forward(delete_from_beginning(None))}")

    # Edge: delete head by reference
    head6 = build_from_list([10, 20, 30])
    head6 = delete_node(head6, head6)
    print(f"Delete head ref:  {print_forward(head6)}")  # 20 <-> 30
