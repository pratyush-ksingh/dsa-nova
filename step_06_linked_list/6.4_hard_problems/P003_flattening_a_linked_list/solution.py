"""
Problem: Flattening a Linked List (GeeksForGeeks)
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Optional


class Node:
    def __init__(self, data=0, next=None, bottom=None):
        self.data = data
        self.next = next
        self.bottom = bottom


# ============================================================
# APPROACH 1: BRUTE FORCE -- Collect All, Sort, Rebuild
# Time: O(N log N)  |  Space: O(N)
#
# Traverse entire structure collecting all values, sort them,
# rebuild a single linked list using bottom pointers.
# ============================================================
def brute_force(head: Optional[Node]) -> Optional[Node]:
    if not head:
        return None

    # Collect all values
    values = []
    curr = head
    while curr:
        down = curr
        while down:
            values.append(down.data)
            down = down.bottom
        curr = curr.next

    # Sort
    values.sort()

    # Rebuild using bottom pointers
    dummy = Node(0)
    curr = dummy
    for val in values:
        curr.bottom = Node(val)
        curr = curr.bottom

    return dummy.bottom


# ============================================================
# APPROACH 2: OPTIMAL -- Iterative Merge from Right
# Time: O(N * k)  |  Space: O(1)
#
# Collect top-level nodes, merge bottom-lists pairwise
# from right to left. Each merge is like merge sort's merge.
# ============================================================
def merge_two(a: Optional[Node], b: Optional[Node]) -> Optional[Node]:
    """Merge two sorted bottom-lists into one sorted bottom-list."""
    dummy = Node(0)
    curr = dummy
    while a and b:
        if a.data <= b.data:
            curr.bottom = a
            a = a.bottom
        else:
            curr.bottom = b
            b = b.bottom
        curr = curr.bottom
    curr.bottom = a if a else b
    return dummy.bottom


def optimal(head: Optional[Node]) -> Optional[Node]:
    if not head:
        return None

    # Collect all top-level heads into a list
    heads = []
    curr = head
    while curr:
        heads.append(curr)
        nxt = curr.next
        curr.next = None  # disconnect next pointer
        curr = nxt

    # Merge from right to left
    result = heads[-1]
    for i in range(len(heads) - 2, -1, -1):
        result = merge_two(heads[i], result)

    return result


# ============================================================
# APPROACH 3: BEST -- Recursive Merge from Right
# Time: O(N * k)  |  Space: O(k) recursion stack
#
# Recursively flatten head.next first, then merge head's
# bottom list with the flattened result. Elegant recursion.
# ============================================================
def best(head: Optional[Node]) -> Optional[Node]:
    if not head or not head.next:
        return head

    # Recursively flatten the rest
    head.next = best(head.next)

    # Merge head's bottom list with flattened next
    head = merge_two(head, head.next)

    return head


# ---- Helpers for testing ----
def build_flat_list(top_level):
    """
    Build the multi-level linked list.
    top_level: list of lists, e.g. [[5,7,8,30],[10,20],[19,22,50],[28,35,40,45]]
    """
    if not top_level:
        return None

    heads = []
    for col in top_level:
        head = Node(col[0])
        curr = head
        for val in col[1:]:
            curr.bottom = Node(val)
            curr = curr.bottom
        heads.append(head)

    for i in range(len(heads) - 1):
        heads[i].next = heads[i + 1]

    return heads[0]


def to_list(head):
    """Convert bottom-linked list to Python list."""
    result = []
    while head:
        result.append(head.data)
        head = head.bottom
    return result


if __name__ == "__main__":
    print("=== Flattening a Linked List ===\n")

    top_level = [[5, 7, 8, 30], [10, 20], [19, 22, 50], [28, 35, 40, 45]]
    expected = [5, 7, 8, 10, 19, 20, 22, 28, 30, 35, 40, 45, 50]

    print(f"Input columns: {top_level}")
    print(f"Expected: {expected}")

    head1 = build_flat_list(top_level)
    print(f"Brute:    {to_list(brute_force(head1))}")

    head2 = build_flat_list(top_level)
    print(f"Optimal:  {to_list(optimal(head2))}")

    head3 = build_flat_list(top_level)
    print(f"Best:     {to_list(best(head3))}")
    print()

    # Test 2: Single column
    print("Test 2: Single column [3,6,9]")
    head4 = build_flat_list([[3, 6, 9]])
    print(f"  Brute:   {to_list(brute_force(head4))}")
    head5 = build_flat_list([[3, 6, 9]])
    print(f"  Optimal: {to_list(optimal(head5))}")
    head6 = build_flat_list([[3, 6, 9]])
    print(f"  Best:    {to_list(best(head6))}")
    print()

    # Test 3: Empty
    print("Test 3: Empty")
    print(f"  Brute:   {to_list(brute_force(None))}")
    print(f"  Optimal: {to_list(optimal(None))}")
    print(f"  Best:    {to_list(best(None))}")
