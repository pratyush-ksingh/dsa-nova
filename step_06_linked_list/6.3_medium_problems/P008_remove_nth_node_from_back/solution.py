"""
Problem: Remove Nth Node From End of List (LeetCode 19)
Difficulty: MEDIUM | XP: 25

Given the head of a linked list, remove the n-th node from the end
of the list and return its head.
Constraints: 1 <= n <= length of list.
"""
from typing import Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


def build_list(values):
    if not values:
        return None
    head = ListNode(values[0])
    cur = head
    for v in values[1:]:
        cur.next = ListNode(v)
        cur = cur.next
    return head


def list_to_arr(head):
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE  (two-pass)
# Time: O(2n) = O(n)  |  Space: O(1)
# First pass: count total length L.
# Second pass: walk to node (L - n - 1) and skip the next node.
# ============================================================
def brute_force(head: Optional[ListNode], n: int) -> Optional[ListNode]:
    """
    Pass 1: count the list length L.
    Pass 2: the node to remove is at 1-based index (L - n + 1).
    Walk to its predecessor (index L - n) and rewire .next.
    A dummy head simplifies removing the very first node.
    """
    dummy = ListNode(0, head)

    # Pass 1: count length
    length = 0
    cur = head
    while cur:
        length += 1
        cur = cur.next

    # Pass 2: walk to (length - n)-th node (0-indexed from dummy)
    cur = dummy
    for _ in range(length - n):
        cur = cur.next

    # cur.next is the node to delete
    cur.next = cur.next.next
    return dummy.next


# ============================================================
# APPROACH 2: OPTIMAL  (two pointers, single pass)
# Time: O(n)  |  Space: O(1)
# Advance 'fast' n steps ahead, then move both until fast.next
# is None; 'slow' is now the predecessor of the node to remove.
# ============================================================
def optimal(head: Optional[ListNode], n: int) -> Optional[ListNode]:
    """
    Two-pointer technique:
    - Move 'fast' n steps ahead of 'slow'.
    - Then advance both together until fast.next is None.
    - slow is now directly before the target node.
    - Rewire slow.next to skip the target.
    A dummy node before head handles the edge case where the
    first node needs to be deleted (n == length).
    """
    dummy = ListNode(0, head)
    fast = dummy
    slow = dummy

    # Move fast n+1 steps from dummy so the gap is exactly n
    for _ in range(n + 1):
        fast = fast.next

    while fast:
        fast = fast.next
        slow = slow.next

    slow.next = slow.next.next
    return dummy.next


# ============================================================
# APPROACH 3: BEST  (same two-pointer but gap set explicitly)
# Time: O(n)  |  Space: O(1)
# Identical algorithm to Approach 2, written out with explicit
# gap logic for clarity; also handles n == length cleanly.
# ============================================================
def best(head: Optional[ListNode], n: int) -> Optional[ListNode]:
    """
    Most interview-preferred variant: dummy + two pointers with
    an explicit n-step gap.

    Why dummy? When n equals the list length, we want to delete
    'head'. Without a dummy, slow would need special casing.
    With dummy as the starting point of both pointers, slow
    naturally ends up at dummy when n == length, so
    slow.next = slow.next.next removes head correctly.
    """
    dummy = ListNode(0, head)
    left = dummy
    right = dummy

    # Create a gap of n between right and left
    for _ in range(n):
        right = right.next

    # Advance together until right reaches last node
    while right.next:
        left = left.next
        right = right.next

    # left.next is the node to delete
    left.next = left.next.next
    return dummy.next


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3, 4, 5], 2, [1, 2, 3, 5]),
        ([1],             1, []),
        ([1, 2],          1, [1]),
        ([1, 2],          2, [2]),
        ([1, 2, 3],       3, [2, 3]),
    ]

    print("=== Remove Nth Node From End of List ===\n")
    for values, n, expected in test_cases:
        b   = list_to_arr(brute_force(build_list(values), n))
        o   = list_to_arr(optimal(build_list(values), n))
        bst = list_to_arr(best(build_list(values), n))
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] Input: {values}, n={n}")
        print(f"       Brute:   {b}")
        print(f"       Optimal: {o}")
        print(f"       Best:    {bst}")
        print(f"       Expect:  {expected}\n")
