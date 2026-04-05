"""
Problem: Kth Node From Middle
Difficulty: EASY | XP: 10
Source: InterviewBit

Given a singly linked list and integer k, find the kth node from
the middle towards the beginning. Middle index = (n-1)//2.
Return the node value, or -1 if invalid.
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'ListNode' = None):
        self.val = val
        self.next = next


def build_list(arr: list[int]) -> Optional[ListNode]:
    if not arr:
        return None
    head = ListNode(arr[0])
    curr = head
    for v in arr[1:]:
        curr.next = ListNode(v)
        curr = curr.next
    return head


# ============================================================
# APPROACH 1: BRUTE FORCE -- Two-Pass Count and Walk
# Time: O(n)  |  Space: O(1)
#
# Pass 1: count n. Compute target = (n-1)//2 - k.
# Pass 2: walk to target index.
# ============================================================
def brute_force(head: Optional[ListNode], k: int) -> int:
    if not head:
        return -1

    # Count length
    n = 0
    curr = head
    while curr:
        n += 1
        curr = curr.next

    mid = (n - 1) // 2
    target = mid - k
    if target < 0:
        return -1

    # Walk to target
    curr = head
    for _ in range(target):
        curr = curr.next
    return curr.val


# ============================================================
# APPROACH 2: OPTIMAL -- Array Storage
# Time: O(n)  |  Space: O(n)
#
# Store all values in a list for O(1) random access.
# Then directly index values[(n-1)//2 - k].
# ============================================================
def optimal(head: Optional[ListNode], k: int) -> int:
    if not head:
        return -1

    values = []
    curr = head
    while curr:
        values.append(curr.val)
        curr = curr.next

    n = len(values)
    mid = (n - 1) // 2
    target = mid - k

    if target < 0:
        return -1
    return values[target]


# ============================================================
# APPROACH 3: BEST -- Clean Two-Pass, O(1) Space
# Time: O(n)  |  Space: O(1)
#
# Count n, compute target = (n-1)//2 - k, walk to it.
# Theoretically optimal for a singly linked list.
# ============================================================
def best(head: Optional[ListNode], k: int) -> int:
    # Count length
    n, c = 0, head
    while c:
        n += 1
        c = c.next

    target = (n - 1) // 2 - k
    if target < 0 or n == 0:
        return -1

    # Walk to target index
    c = head
    for _ in range(target):
        c = c.next
    return c.val


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Kth Node From Middle ===\n")

    test_cases = [
        # (list_values, k, expected)
        ([1, 2, 3, 4, 5], 0, 3),
        ([1, 2, 3, 4, 5], 1, 2),
        ([1, 2, 3, 4, 5], 2, 1),
        ([1, 2, 3, 4, 5], 3, -1),
        ([1, 2, 3, 4], 0, 2),
        ([1, 2, 3, 4], 1, 1),
        ([5], 0, 5),
        ([5], 1, -1),
        ([], 0, -1),
    ]

    for arr, k, expected in test_cases:
        result = best(build_list(arr), k)
        status = "PASS" if result == expected else "FAIL"
        print(f"[{status}] list={arr}, k={k} -> {result} (expected {expected})")

    print("\n--- Cross-check all approaches ---")
    print(f"Brute(k=1):   {brute_force(build_list([1,2,3,4,5]), 1)}")
    print(f"Optimal(k=1): {optimal(build_list([1,2,3,4,5]), 1)}")
    print(f"Best(k=1):    {best(build_list([1,2,3,4,5]), 1)}")
