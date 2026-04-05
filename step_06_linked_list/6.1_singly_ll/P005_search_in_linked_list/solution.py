"""
Problem: Search in Linked List
Difficulty: EASY | XP: 10

Given a singly linked list and a target value, determine if the
target exists in the list. Return True/False or the 0-based index.
"""
from typing import Optional


# ----- Linked List Node Definition -----
class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


def build_list(arr: list[int]) -> Optional[ListNode]:
    """Helper: build linked list from a Python list."""
    if not arr:
        return None
    head = ListNode(arr[0])
    curr = head
    for v in arr[1:]:
        curr.next = ListNode(v)
        curr = curr.next
    return head


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative Boolean Search
# Time: O(n)  |  Space: O(1)
#
# Walk from head to tail comparing each node's value to target.
# Return True the instant a match is found; False after exhausting
# the entire list.
# ============================================================
def brute_force(head: Optional[ListNode], target: int) -> bool:
    current = head
    while current:
        if current.val == target:
            return True
        current = current.next
    return False


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Search
# Time: O(n)  |  Space: O(n) due to call stack
#
# Recursion mirrors the linked-list structure beautifully.
# Base case 1: None node -> not found.
# Base case 2: node.val == target -> found.
# Recursive case: search the rest of the list.
# ============================================================
def optimal(node: Optional[ListNode], target: int) -> bool:
    if node is None:
        return False
    if node.val == target:
        return True
    return optimal(node.next, target)


# ============================================================
# APPROACH 3: BEST -- Iterative Search with Index Return
# Time: O(n)  |  Space: O(1)
#
# Same traversal as brute force, but maintains an index counter.
# Returns the 0-based index of the first occurrence, or -1 if
# the target is absent.
# ============================================================
def best(head: Optional[ListNode], target: int) -> int:
    current = head
    idx = 0
    while current:
        if current.val == target:
            return idx
        current = current.next
        idx += 1
    return -1


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Search in Linked List ===\n")

    list1 = build_list([1, 3, 5, 7])
    empty = build_list([])
    single = build_list([9])

    print("--- Brute Force (boolean) ---")
    print(f"[1,3,5,7] target=5 -> {brute_force(list1, 5)}")   # True
    print(f"[1,3,5,7] target=4 -> {brute_force(list1, 4)}")   # False
    print(f"[]        target=1 -> {brute_force(empty, 1)}")    # False
    print(f"[9]       target=9 -> {brute_force(single, 9)}")   # True

    print("\n--- Optimal (recursive boolean) ---")
    print(f"[1,3,5,7] target=5 -> {optimal(list1, 5)}")   # True
    print(f"[1,3,5,7] target=4 -> {optimal(list1, 4)}")   # False
    print(f"[]        target=1 -> {optimal(empty, 1)}")    # False
    print(f"[9]       target=9 -> {optimal(single, 9)}")   # True

    print("\n--- Best (index return) ---")
    print(f"[1,3,5,7] target=5 -> index {best(list1, 5)}")   # 2
    print(f"[1,3,5,7] target=4 -> index {best(list1, 4)}")   # -1
    print(f"[]        target=1 -> index {best(empty, 1)}")    # -1
    print(f"[9]       target=9 -> index {best(single, 9)}")   # 0
