"""
Problem: Intro to Linked List
Difficulty: EASY | XP: 10

Create a linked list from an array, traverse and print.
"""
from typing import List, Optional


# ============================================================
# NODE DEFINITION
# ============================================================
class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: ITERATIVE BUILD FROM ARRAY
# Time: O(n)  |  Space: O(n)
# Walk through array, create nodes, link them in a chain.
# ============================================================
def build_from_array(arr: List[int]) -> Optional[ListNode]:
    """Build a singly linked list from an array."""
    if not arr:
        return None

    head = ListNode(arr[0])
    current = head

    for i in range(1, len(arr)):
        current.next = ListNode(arr[i])
        current = current.next

    return head


# ============================================================
# APPROACH 2: RECURSIVE BUILD FROM ARRAY
# Time: O(n)  |  Space: O(n) + O(n) call stack
# Create current node, set next to recursively built rest.
# ============================================================
def build_recursive(arr: List[int], index: int = 0) -> Optional[ListNode]:
    """Build a linked list recursively."""
    if index >= len(arr):
        return None

    node = ListNode(arr[index])
    node.next = build_recursive(arr, index + 1)
    return node


# ============================================================
# APPROACH 3: HEAD INSERTION (builds reversed list)
# Time: O(n)  |  Space: O(n)
# Insert each element at head. Traverse right-to-left for order.
# ============================================================
def build_by_head_insertion(arr: List[int]) -> Optional[ListNode]:
    """Build list by inserting at head (right-to-left for original order)."""
    if not arr:
        return None

    head = None
    for i in range(len(arr) - 1, -1, -1):
        node = ListNode(arr[i])
        node.next = head
        head = node

    return head


# ============================================================
# TRAVERSAL: Print linked list
# Time: O(n)  |  Space: O(1)
# ============================================================
def print_list(head: Optional[ListNode]) -> str:
    """Traverse and return string representation of linked list."""
    parts = []
    current = head
    while current:
        parts.append(str(current.val))
        current = current.next
    return " -> ".join(parts) if parts else "(empty)"


# ============================================================
# UTILITY: Get length
# Time: O(n)  |  Space: O(1)
# ============================================================
def get_length(head: Optional[ListNode]) -> int:
    """Return the number of nodes in the list."""
    count = 0
    current = head
    while current:
        count += 1
        current = current.next
    return count


# ============================================================
# UTILITY: Search for a value
# Time: O(n)  |  Space: O(1)
# ============================================================
def search(head: Optional[ListNode], target: int) -> bool:
    """Return True if target exists in the list."""
    current = head
    while current:
        if current.val == target:
            return True
        current = current.next
    return False


if __name__ == "__main__":
    print("=== Intro to Linked List ===\n")

    arr = [1, 2, 3, 4, 5]

    # Approach 1: Iterative build
    print("Approach 1 - Iterative Build:")
    head1 = build_from_array(arr)
    print(print_list(head1))
    print(f"Length: {get_length(head1)}")

    # Approach 2: Recursive build
    print("\nApproach 2 - Recursive Build:")
    head2 = build_recursive(arr)
    print(print_list(head2))

    # Approach 3: Head insertion
    print("\nApproach 3 - Head Insertion:")
    head3 = build_by_head_insertion(arr)
    print(print_list(head3))

    # Search
    print(f"\nSearch for 3: {search(head1, 3)}")
    print(f"Search for 9: {search(head1, 9)}")

    # Edge case: empty array
    print("\nEdge case - Empty array:")
    empty = build_from_array([])
    print(print_list(empty))

    # Edge case: single element
    print("Edge case - Single element:")
    single = build_from_array([42])
    print(print_list(single))
    print(f"Length: {get_length(single)}")
