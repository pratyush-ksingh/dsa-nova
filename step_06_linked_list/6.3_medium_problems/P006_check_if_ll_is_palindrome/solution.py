"""
Problem: Check if Linked List is Palindrome (LeetCode #234)
Difficulty: MEDIUM | XP: 25
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'Optional[ListNode]' = None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE -- Copy values to array, check palindrome
# Time: O(n)  |  Space: O(n)
#
# Traverse the list, collect all values in a Python list,
# then check if the list equals its reverse.
# ============================================================
def brute_force(head: Optional[ListNode]) -> bool:
    values = []
    curr = head
    while curr:
        values.append(curr.val)
        curr = curr.next
    return values == values[::-1]


# ============================================================
# APPROACH 2: OPTIMAL -- Reverse second half, compare, restore
# Time: O(n)  |  Space: O(1)
#
# 1. Find middle using slow/fast pointers.
# 2. Reverse the second half of the list.
# 3. Compare first half with reversed second half.
# 4. Restore the list (optional but good practice).
# ============================================================
def optimal(head: Optional[ListNode]) -> bool:
    if not head or not head.next:
        return True

    # Step 1: Find mid
    slow, fast = head, head
    while fast.next and fast.next.next:
        slow = slow.next
        fast = fast.next.next
    # slow is now at the end of the first half

    # Step 2: Reverse second half
    def reverse(node: Optional[ListNode]) -> Optional[ListNode]:
        prev = None
        curr = node
        while curr:
            nxt = curr.next
            curr.next = prev
            prev = curr
            curr = nxt
        return prev

    second_half_start = slow.next
    slow.next = None  # cut
    reversed_second = reverse(second_half_start)

    # Step 3: Compare
    p1, p2 = head, reversed_second
    is_pal = True
    while p2:
        if p1.val != p2.val:
            is_pal = False
            break
        p1 = p1.next
        p2 = p2.next

    # Step 4: Restore
    slow.next = reverse(reversed_second)
    return is_pal


# ============================================================
# APPROACH 3: BEST -- Same as Optimal with explicit restoration
# Time: O(n)  |  Space: O(1)
#
# Identical logic but written for maximum clarity and with
# explicit list restoration, which is expected in interviews.
# ============================================================
def best(head: Optional[ListNode]) -> bool:
    if not head or not head.next:
        return True

    # 1. Find end of first half
    slow, fast = head, head
    while fast.next and fast.next.next:
        slow = slow.next
        fast = fast.next.next

    # 2. Reverse second half
    prev, curr = None, slow.next
    slow.next = None
    while curr:
        nxt = curr.next
        curr.next = prev
        prev = curr
        curr = nxt
    second_head = prev

    # 3. Compare
    a, b = head, second_head
    result = True
    while b:
        if a.val != b.val:
            result = False
            break
        a = a.next
        b = b.next

    # 4. Restore: reverse second half back
    prev, curr = None, second_head
    while curr:
        nxt = curr.next
        curr.next = prev
        prev = curr
        curr = nxt
    slow.next = prev

    return result


# --------------- helpers for testing ---------------
def build_list(values: list) -> Optional[ListNode]:
    if not values:
        return None
    head = ListNode(values[0])
    curr = head
    for v in values[1:]:
        curr.next = ListNode(v)
        curr = curr.next
    return head


def list_to_array(head: Optional[ListNode]) -> list:
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


if __name__ == "__main__":
    print("=== Check if Linked List is Palindrome ===\n")

    tests = [
        ([1, 2, 2, 1], True),
        ([1, 2], False),
        ([1], True),
        ([1, 2, 3, 2, 1], True),
        ([1, 2, 3, 4, 5], False),
    ]

    for values, expected in tests:
        h1 = build_list(values)
        h2 = build_list(values)
        h3 = build_list(values)
        b = brute_force(h1)
        o = optimal(h2)
        bt = best(h3)
        status = "OK" if b == o == bt == expected else "MISMATCH"
        print(f"List={values}  Expected={expected}  "
              f"Brute={b}  Optimal={o}  Best={bt}  [{status}]")
        # verify restoration
        print(f"  After optimal restore: {list_to_array(h2)}")
        print(f"  After best restore:    {list_to_array(h3)}")
