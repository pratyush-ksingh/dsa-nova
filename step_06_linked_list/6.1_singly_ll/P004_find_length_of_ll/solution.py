"""
Problem: Find Length of Linked List
Difficulty: EASY | XP: 10

Given the head of a singly linked list, find the total number of nodes.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next=None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative Traversal
# Time: O(n)  |  Space: O(1)
# Walk through the list, counting each node until None.
# ============================================================
def brute_force(head: Optional[ListNode]) -> int:
    count = 0
    current = head

    while current is not None:
        count += 1
        current = current.next

    return count


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Length
# Time: O(n)  |  Space: O(n) due to recursion stack
# length(node) = 0 if node is None, else 1 + length(node.next)
# ============================================================
def optimal(head: Optional[ListNode]) -> int:
    if head is None:
        return 0
    return 1 + optimal(head.next)


# ============================================================
# APPROACH 3: BEST -- Tail-Recursive / Clean Iterative
# Time: O(n)  |  Space: O(1)
# Production-ready iterative form. Python has no TCO, so iterative is best.
# ============================================================
def best(head: Optional[ListNode]) -> int:
    count = 0
    curr = head
    while curr:
        count += 1
        curr = curr.next
    return count


def build_list(values: list) -> Optional[ListNode]:
    """Helper: build a linked list from a Python list."""
    if not values:
        return None
    head = ListNode(values[0])
    current = head
    for v in values[1:]:
        current.next = ListNode(v)
        current = current.next
    return head


if __name__ == "__main__":
    print("=== Find Length of Linked List ===\n")

    test_cases = [
        [1, 2, 3, 4, 5],  # 5 nodes
        [10, 20],          # 2 nodes
        [7],               # 1 node
        [],                # empty list
    ]

    for tc in test_cases:
        head = build_list(tc)
        label = tc if tc else "None"
        print(f"Input: {label}")
        print(f"  Brute Force: {brute_force(head)}")
        print(f"  Optimal:     {optimal(head)}")
        print(f"  Best:        {best(head)}")
        print()
