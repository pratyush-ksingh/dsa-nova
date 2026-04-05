"""
Problem: Merge K Sorted Lists
Difficulty: HARD | XP: 50
"""
from typing import List, Optional
import heapq


class ListNode:
    def __init__(self, val: int = 0, next: "Optional[ListNode]" = None):
        self.val = val
        self.next = next

    # Needed so heapq can compare ListNode objects (break ties by object id)
    def __lt__(self, other: "ListNode") -> bool:
        return self.val < other.val


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n log n)  |  Space: O(n)
# ============================================================
# Walk all k lists, collect every value, sort, rebuild a new list.
def brute_force(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
    vals = []
    for head in lists:
        curr = head
        while curr:
            vals.append(curr.val)
            curr = curr.next
    vals.sort()

    dummy = ListNode()
    curr = dummy
    for v in vals:
        curr.next = ListNode(v)
        curr = curr.next
    return dummy.next


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n log k)  |  Space: O(k)
# ============================================================
# Min-heap containing at most k nodes (one per list).
# Repeatedly pop the minimum, append to result, push its successor.
def optimal(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
    heap: list = []
    for head in lists:
        if head:
            heapq.heappush(heap, head)

    dummy = ListNode()
    tail = dummy
    while heap:
        node = heapq.heappop(heap)
        tail.next = node
        tail = tail.next
        if node.next:
            heapq.heappush(heap, node.next)

    return dummy.next


# ============================================================
# APPROACH 3: BEST
# Time: O(n log k)  |  Space: O(log k)  (recursion stack)
# ============================================================
# Divide and conquer: recursively split the list of lists in half,
# merge each half, then merge the two results with a standard
# two-pointer merge.  No heap overhead, cache-friendly.
def best(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
    if not lists:
        return None
    return _divide(lists, 0, len(lists) - 1)


def _divide(lists: List[Optional[ListNode]], lo: int, hi: int) -> Optional[ListNode]:
    if lo == hi:
        return lists[lo]
    mid = (lo + hi) // 2
    left  = _divide(lists, lo, mid)
    right = _divide(lists, mid + 1, hi)
    return _merge_two(left, right)


def _merge_two(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    dummy = ListNode()
    tail = dummy
    while l1 and l2:
        if l1.val <= l2.val:
            tail.next, l1 = l1, l1.next
        else:
            tail.next, l2 = l2, l2.next
        tail = tail.next
    tail.next = l1 if l1 else l2
    return dummy.next


# ---- helpers ----

def build_list(vals: list[int]) -> Optional[ListNode]:
    dummy = ListNode()
    curr = dummy
    for v in vals:
        curr.next = ListNode(v)
        curr = curr.next
    return dummy.next


def list_to_str(head: Optional[ListNode]) -> str:
    parts = []
    while head:
        parts.append(str(head.val))
        head = head.next
    return "[" + ", ".join(parts) + "]"


if __name__ == "__main__":
    print("=== Merge K Sorted Lists ===\n")

    # Standard test
    t1 = [build_list([1, 4, 5]), build_list([1, 3, 4]), build_list([2, 6])]
    t2 = [build_list([1, 4, 5]), build_list([1, 3, 4]), build_list([2, 6])]
    t3 = [build_list([1, 4, 5]), build_list([1, 3, 4]), build_list([2, 6])]

    print("[Approach 1 - BruteForce: Collect & Sort]")
    print("Result:", list_to_str(brute_force(t1)))

    print("\n[Approach 2 - Optimal: Min-Heap]")
    print("Result:", list_to_str(optimal(t2)))

    print("\n[Approach 3 - Best: Divide & Conquer]")
    print("Result:", list_to_str(best(t3)))

    # Edge cases
    print("\n[Edge cases]")
    print("Empty array:   ", list_to_str(best([])))
    print("Single empty:  ", list_to_str(best([None])))
    print("Single list:   ", list_to_str(best([build_list([3, 7, 9])])))
    print("With negatives:", list_to_str(best([build_list([-3, -1, 2]),
                                                build_list([-5, 0, 4])])))
