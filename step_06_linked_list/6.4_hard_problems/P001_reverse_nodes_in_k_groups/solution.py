"""
Problem: Reverse Nodes in K Groups
Difficulty: HARD | XP: 50
"""
from typing import List, Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


def build_list(vals):
    dummy = ListNode(0)
    cur = dummy
    for v in vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


def to_list(head):
    res = []
    while head:
        res.append(head.val)
        head = head.next
    return res


# ============================================================
# APPROACH 1: BRUTE FORCE — Collect into array, reverse in chunks
# Time: O(n)  |  Space: O(n)
# ============================================================
def brute_force(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    Convert linked list to array, reverse every k-group in array,
    then rebuild the linked list.
    """
    if not head or k == 1:
        return head

    # Collect all values
    vals = []
    cur = head
    while cur:
        vals.append(cur.val)
        cur = cur.next

    n = len(vals)
    # Reverse every complete k-group
    for i in range(0, n - n % k, k):
        vals[i:i + k] = vals[i:i + k][::-1]

    # Rebuild linked list
    return build_list(vals)


# ============================================================
# APPROACH 2: OPTIMAL — Iterative in-place reversal
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    Iteratively reverse each k-group in place. Use a dummy node
    and pointer manipulation. Skip the last group if < k nodes remain.
    """
    if not head or k == 1:
        return head

    dummy = ListNode(0, head)
    group_prev = dummy

    while True:
        # Check if there are k nodes remaining
        kth = group_prev
        for _ in range(k):
            kth = kth.next
            if not kth:
                return dummy.next

        group_next = kth.next
        # Reverse the k nodes between group_prev and group_next
        prev, cur = group_next, group_prev.next
        for _ in range(k):
            nxt = cur.next
            cur.next = prev
            prev = cur
            cur = nxt

        # Connect reversed group back
        tmp = group_prev.next  # this was the first node, now last in group
        group_prev.next = prev  # prev is now the first node of reversed group
        group_prev = tmp  # advance to end of reversed group

    return dummy.next


# ============================================================
# APPROACH 3: BEST — Recursive reversal
# Time: O(n)  |  Space: O(n/k) recursion stack
# ============================================================
def best(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    Recursively reverse k nodes at a time. First check if there are
    k nodes available; if not, return head as-is. Otherwise reverse
    k nodes then recurse on the remainder.
    """
    if not head or k == 1:
        return head

    # Check if k nodes exist
    count = 0
    cur = head
    while cur and count < k:
        cur = cur.next
        count += 1
    if count < k:
        return head

    # Reverse first k nodes
    prev, cur = None, head
    for _ in range(k):
        nxt = cur.next
        cur.next = prev
        prev = cur
        cur = nxt

    # head is now the tail of the reversed group; connect to next group
    head.next = best(cur, k)
    return prev


if __name__ == "__main__":
    print("=== Reverse Nodes in K Groups ===")

    vals = [1, 2, 3, 4, 5]
    k = 2
    print(f"Input: {vals}, k={k}")
    print(f"Brute:   {to_list(brute_force(build_list(vals), k))}")
    print(f"Optimal: {to_list(optimal(build_list(vals), k))}")
    print(f"Best:    {to_list(best(build_list(vals), k))}")

    vals = [1, 2, 3, 4, 5]
    k = 3
    print(f"\nInput: {vals}, k={k}")
    print(f"Brute:   {to_list(brute_force(build_list(vals), k))}")
    print(f"Optimal: {to_list(optimal(build_list(vals), k))}")
    print(f"Best:    {to_list(best(build_list(vals), k))}")
