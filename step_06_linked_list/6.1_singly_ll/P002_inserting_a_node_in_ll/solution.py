"""
Problem: Inserting a Node in Linked List
Difficulty: EASY | XP: 10

Insert a node at beginning, end, and at a given position in a singly linked list.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


# ============================================================
# INSERT AT BEGINNING
# Time: O(1)  |  Space: O(1)
# Create node, point it to old head, return as new head.
# ============================================================
def insert_at_beginning(head: Optional[ListNode], val: int) -> ListNode:
    """Insert a new node at the beginning of the list."""
    new_node = ListNode(val)
    new_node.next = head
    return new_node  # new head


# ============================================================
# INSERT AT END
# Time: O(n)  |  Space: O(1)
# Traverse to last node, attach new node.
# ============================================================
def insert_at_end(head: Optional[ListNode], val: int) -> ListNode:
    """Insert a new node at the end of the list."""
    new_node = ListNode(val)

    if head is None:
        return new_node

    current = head
    while current.next:
        current = current.next
    current.next = new_node

    return head


# ============================================================
# INSERT AT POSITION (1-indexed)
# Time: O(k)  |  Space: O(1)
# Walk to position k-1, insert after it.
# ============================================================
def insert_at_position(head: Optional[ListNode], val: int, position: int) -> ListNode:
    """Insert at the given 1-indexed position."""
    if position <= 1:
        return insert_at_beginning(head, val)

    current = head
    for _ in range(position - 2):
        if current is None:
            return head
        current = current.next

    if current is None:
        return head

    new_node = ListNode(val)
    new_node.next = current.next  # IMPORTANT: set new_node.next FIRST
    current.next = new_node

    return head


# ============================================================
# UNIFIED INSERT WITH SENTINEL (Approach 2)
# Time: O(k)  |  Space: O(1)
# Dummy head eliminates special case for position 1.
# ============================================================
def insert_with_sentinel(head: Optional[ListNode], val: int, position: int) -> ListNode:
    """Insert using a sentinel node to unify all cases."""
    dummy = ListNode(0)
    dummy.next = head

    current = dummy
    for _ in range(position - 1):
        if current is None:
            break
        current = current.next

    if current is None:
        return dummy.next

    new_node = ListNode(val)
    new_node.next = current.next
    current.next = new_node

    return dummy.next


# ============================================================
# HELPERS
# ============================================================
def build_list(arr: list) -> Optional[ListNode]:
    """Build linked list from array."""
    if not arr:
        return None
    head = ListNode(arr[0])
    current = head
    for val in arr[1:]:
        current.next = ListNode(val)
        current = current.next
    return head


def print_list(head: Optional[ListNode]) -> str:
    """Return string representation of linked list."""
    parts = []
    current = head
    while current:
        parts.append(str(current.val))
        current = current.next
    result = " -> ".join(parts) if parts else "(empty)"
    print(result)
    return result


if __name__ == "__main__":
    print("=== Inserting a Node in Linked List ===\n")

    # Build initial list: 1 -> 2 -> 3 -> 4 -> 5
    head = build_list([1, 2, 3, 4, 5])
    print("Original:         ", end="")
    print_list(head)

    # Insert at beginning
    head = insert_at_beginning(head, 0)
    print("Insert 0 at head: ", end="")
    print_list(head)  # 0 -> 1 -> 2 -> 3 -> 4 -> 5

    # Insert at end
    head = insert_at_end(head, 6)
    print("Insert 6 at end:  ", end="")
    print_list(head)  # 0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6

    # Insert at position 4 (1-indexed)
    head = insert_at_position(head, 99, 4)
    print("Insert 99 at pos 4:", end="")
    print_list(head)  # 0 -> 1 -> 2 -> 99 -> 3 -> 4 -> 5 -> 6

    # Sentinel approach
    print("\n--- Sentinel Approach ---")
    head2 = build_list([10, 20, 30])
    print("Original:           ", end="")
    print_list(head2)
    head2 = insert_with_sentinel(head2, 5, 1)
    print("Insert 5 at pos 1:  ", end="")
    print_list(head2)  # 5 -> 10 -> 20 -> 30
    head2 = insert_with_sentinel(head2, 25, 4)
    print("Insert 25 at pos 4: ", end="")
    print_list(head2)  # 5 -> 10 -> 20 -> 25 -> 30

    # Edge cases
    print("\n--- Edge Cases ---")
    empty = insert_at_beginning(None, 42)
    print("Insert into empty (head): ", end="")
    print_list(empty)  # 42

    empty2 = insert_at_end(None, 99)
    print("Insert into empty (end):  ", end="")
    print_list(empty2)  # 99
