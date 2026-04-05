"""
Problem: Insert Node in Doubly Linked List
Difficulty: EASY | XP: 10

Insert at beginning, end, before/after a given node in doubly linked list.
"""
from typing import Optional


# ============================================================
# NODE DEFINITION
# ============================================================
class ListNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.prev: Optional['ListNode'] = None
        self.next: Optional['ListNode'] = None


# ============================================================
# INSERT AT BEGINNING
# Time: O(1)  |  Space: O(1)
# New node becomes the head.
# ============================================================
def insert_at_beginning(head: Optional[ListNode], val: int) -> ListNode:
    """Insert a new node at the beginning of the DLL."""
    new_node = ListNode(val)
    new_node.next = head

    if head:
        head.prev = new_node

    return new_node  # new head


# ============================================================
# INSERT AT END
# Time: O(n)  |  Space: O(1)
# Walk to tail, then attach.
# ============================================================
def insert_at_end(head: Optional[ListNode], val: int) -> ListNode:
    """Insert a new node at the end of the DLL."""
    new_node = ListNode(val)

    if not head:
        return new_node

    tail = head
    while tail.next:
        tail = tail.next

    tail.next = new_node
    new_node.prev = tail
    return head


# ============================================================
# INSERT BEFORE A GIVEN NODE
# Time: O(1)  |  Space: O(1)  (given direct reference)
# ============================================================
def insert_before(head: Optional[ListNode], target: ListNode, val: int) -> ListNode:
    """Insert a new node immediately before the target node."""
    if not target:
        return head

    new_node = ListNode(val)

    # Wire new node's pointers first
    new_node.prev = target.prev
    new_node.next = target

    # Wire predecessor
    if target.prev:
        target.prev.next = new_node
    else:
        head = new_node  # target was head

    # Wire target backward
    target.prev = new_node

    return head


# ============================================================
# INSERT AFTER A GIVEN NODE
# Time: O(1)  |  Space: O(1)  (given direct reference)
# ============================================================
def insert_after(head: Optional[ListNode], target: ListNode, val: int) -> ListNode:
    """Insert a new node immediately after the target node."""
    if not target:
        return head

    new_node = ListNode(val)

    # Wire new node's pointers first
    new_node.next = target.next
    new_node.prev = target

    # Wire successor backward
    if target.next:
        target.next.prev = new_node

    # Wire target forward
    target.next = new_node

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
    print("=== Insert Node in Doubly LL ===\n")

    # Build: 10 <-> 20 <-> 30
    head = build_from_list([10, 20, 30])
    print(f"Initial: {print_forward(head)}")

    # Insert at beginning
    head = insert_at_beginning(head, 5)
    print(f"Insert 5 at beginning: {print_forward(head)}")

    # Insert at end
    head = insert_at_end(head, 40)
    print(f"Insert 40 at end: {print_forward(head)}")

    # Insert before node(20)
    node20 = find_node(head, 20)
    head = insert_before(head, node20, 15)
    print(f"Insert 15 before 20: {print_forward(head)}")

    # Insert after node(20)
    node20 = find_node(head, 20)
    head = insert_after(head, node20, 25)
    print(f"Insert 25 after 20: {print_forward(head)}")

    # Edge: insert before head
    head = insert_before(head, head, 1)
    print(f"Insert 1 before head: {print_forward(head)}")

    # Edge: empty list
    empty = insert_at_beginning(None, 99)
    print(f"\nEmpty list insert: {print_forward(empty)}")
