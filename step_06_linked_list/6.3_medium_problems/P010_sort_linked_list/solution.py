"""
Problem: Sort Linked List (LeetCode 148)
Difficulty: MEDIUM | XP: 25

Sort a linked list in O(n log n) time.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'Optional[ListNode]' = None):
        self.val = val
        self.next = next


def make_list(values: list) -> Optional[ListNode]:
    dummy = ListNode(0)
    cur = dummy
    for v in values:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


def list_to_arr(head: Optional[ListNode]) -> list:
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE - Copy to array, sort, rebuild
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    1. Traverse the list, collect all values into a Python list.
    2. Sort the list (Timsort, O(n log n)).
    3. Rewrite node values back in sorted order (reuses existing nodes).
    Simple but uses O(n) extra space.
    """
    if not head:
        return head

    values = []
    cur = head
    while cur:
        values.append(cur.val)
        cur = cur.next

    values.sort()

    cur = head
    for v in values:
        cur.val = v
        cur = cur.next

    return head


# ============================================================
# APPROACH 2: OPTIMAL - Bottom-up merge sort (O(1) extra space)
# Time: O(n log n)  |  Space: O(1)
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Bottom-up merge sort avoids recursion stack overhead.
    Start with sublists of size 1, merge pairs into size 2,
    then size 4, 8, ... until the whole list is sorted.
    Uses a dummy head to simplify merging.
    True O(1) space (no recursion stack).
    """
    if not head or not head.next:
        return head

    # Count length
    length = 0
    cur = head
    while cur:
        length += 1
        cur = cur.next

    dummy = ListNode(0)
    dummy.next = head

    size = 1
    while size < length:
        cur = dummy.next
        tail = dummy

        while cur:
            left = cur
            right = split(left, size)   # split off 'size' nodes from left
            cur   = split(right, size)  # remaining list after right part

            merged_tail = merge(left, right)
            tail.next = merged_tail[0]
            tail = merged_tail[1]

        size *= 2

    return dummy.next


def split(head: Optional[ListNode], size: int):
    """
    Advances 'size-1' steps from head, cuts the link, and returns the next part.
    """
    cur = head
    for _ in range(size - 1):
        if cur and cur.next:
            cur = cur.next
        else:
            break
    if not cur:
        return None
    rest = cur.next
    cur.next = None
    return rest


def merge(l1: Optional[ListNode], l2: Optional[ListNode]):
    """
    Merges two sorted lists. Returns (head, tail) of merged list.
    """
    dummy = ListNode(0)
    cur = dummy
    while l1 and l2:
        if l1.val <= l2.val:
            cur.next = l1
            l1 = l1.next
        else:
            cur.next = l2
            l2 = l2.next
        cur = cur.next
    cur.next = l1 if l1 else l2
    while cur.next:
        cur = cur.next
    return dummy.next, cur


# ============================================================
# APPROACH 3: BEST - Top-down merge sort (O(log n) stack space)
# Time: O(n log n)  |  Space: O(log n) stack
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Classic recursive top-down merge sort.
    1. Find midpoint using slow/fast pointers (Floyd's algorithm).
    2. Split list into two halves.
    3. Recursively sort each half.
    4. Merge the two sorted halves.
    Uses O(log n) stack space from recursion — cleaner than bottom-up.
    """
    if not head or not head.next:
        return head

    # Find midpoint (slow/fast pointer)
    slow, fast = head, head.next
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next

    # Split: slow is at midpoint
    mid = slow.next
    slow.next = None

    left  = best(head)
    right = best(mid)

    return _merge(left, right)


def _merge(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    dummy = ListNode(0)
    cur = dummy
    while l1 and l2:
        if l1.val <= l2.val:
            cur.next = l1
            l1 = l1.next
        else:
            cur.next = l2
            l2 = l2.next
        cur = cur.next
    cur.next = l1 if l1 else l2
    return dummy.next


if __name__ == "__main__":
    print("=== Sort Linked List ===")
    test_cases = [
        ([4, 2, 1, 3],          [1, 2, 3, 4]),
        ([-1, 5, 3, 4, 0],      [-1, 0, 3, 4, 5]),
        ([],                    []),
        ([1],                   [1]),
        ([2, 1],                [1, 2]),
    ]
    for values, expected in test_cases:
        b   = list_to_arr(brute_force(make_list(values)))
        o   = list_to_arr(optimal(make_list(values)))
        bst = list_to_arr(best(make_list(values)))
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"[{status}] input={values} | Brute={b}, Optimal={o}, Best={bst}")
