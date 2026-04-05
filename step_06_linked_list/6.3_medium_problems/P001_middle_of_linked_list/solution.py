"""
Problem: Middle of Linked List (LeetCode #876)
Difficulty: EASY | XP: 10

Given the head of a singly linked list, return the middle node.
If two middle nodes, return the second one.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE - Count then traverse
# Time: O(n)  |  Space: O(1)
# Two passes: first count, then walk to middle.
# ============================================================
def middle_brute(head: Optional[ListNode]) -> Optional[ListNode]:
    """Find middle by counting nodes first, then traversing to n/2."""
    # Pass 1: count nodes
    count = 0
    current = head
    while current:
        count += 1
        current = current.next

    # Pass 2: walk to middle
    current = head
    for _ in range(count // 2):
        current = current.next

    return current


# ============================================================
# APPROACH 2: OPTIMAL - Slow and Fast Pointers (Tortoise & Hare)
# Time: O(n)  |  Space: O(1)
# One pass: slow moves 1 step, fast moves 2 steps.
# When fast reaches end, slow is at middle.
# ============================================================
def middle_node(head: Optional[ListNode]) -> Optional[ListNode]:
    """Find middle using slow/fast pointers in a single pass."""
    slow = head
    fast = head

    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next

    return slow


# ============================================================
# APPROACH 3: BEST - Same as Optimal
# The two-pointer approach IS optimal: O(n) time, O(1) space,
# single pass. Cannot do better since every node must be visited.
# ============================================================


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


def print_from_node(node: Optional[ListNode]) -> str:
    """Print list from a given node."""
    parts = []
    while node:
        parts.append(str(node.val))
        node = node.next
    return " -> ".join(parts)


if __name__ == "__main__":
    print("=== Middle of Linked List ===\n")

    # Test 1: Odd length [1,2,3,4,5] -> middle is 3
    head1 = build_list([1, 2, 3, 4, 5])
    print("List: 1 -> 2 -> 3 -> 4 -> 5")
    print(f"Brute force middle: {middle_brute(head1).val}")   # 3
    print(f"Two-pointer middle: {middle_node(head1).val}")    # 3
    print(f"From middle: {print_from_node(middle_node(head1))}")

    # Test 2: Even length [1,2,3,4,5,6] -> second middle is 4
    print()
    head2 = build_list([1, 2, 3, 4, 5, 6])
    print("List: 1 -> 2 -> 3 -> 4 -> 5 -> 6")
    print(f"Brute force middle: {middle_brute(head2).val}")   # 4
    print(f"Two-pointer middle: {middle_node(head2).val}")    # 4

    # Test 3: Single element [1] -> middle is 1
    print()
    head3 = build_list([1])
    print("List: 1")
    print(f"Middle: {middle_node(head3).val}")   # 1

    # Test 4: Two elements [1,2] -> second middle is 2
    print()
    head4 = build_list([1, 2])
    print("List: 1 -> 2")
    print(f"Middle: {middle_node(head4).val}")   # 2
