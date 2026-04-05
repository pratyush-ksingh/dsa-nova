"""
Problem: Odd Even Linked List (LeetCode 328)
Difficulty: MEDIUM | XP: 25

Given the head of a singly linked list, group all nodes with odd indices
together followed by the nodes with even indices, and return the reordered list.
The first node is considered odd (index 1), second even (index 2), etc.
Note: "odd/even" refers to the NODE INDEX, NOT the node value.
Must run in O(n) time and O(1) extra space.
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
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Collect odd-indexed values and even-indexed values into two
# separate Python lists, then rebuild the linked list by
# concatenating them.
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Walk the list once, separating values by 1-based position parity.
    Rebuild into a new linked list: all odds first, then all evens.
    Simple but uses O(n) extra space for the value arrays.
    """
    if not head:
        return None

    odd_vals, even_vals = [], []
    cur = head
    idx = 1
    while cur:
        if idx % 2 == 1:
            odd_vals.append(cur.val)
        else:
            even_vals.append(cur.val)
        cur = cur.next
        idx += 1

    all_vals = odd_vals + even_vals
    dummy = ListNode(0)
    cur = dummy
    for v in all_vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


# ============================================================
# APPROACH 2: OPTIMAL  (two-chain pointer re-linking)
# Time: O(n)  |  Space: O(1)
# Maintain two separate chains (odd / even) by re-wiring .next
# pointers in a single pass. Attach even chain after odd chain.
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Use two chain heads: 'odd' and 'even'.
    'odd' cursor: 1 -> 3 -> 5 -> ...
    'even' cursor: 2 -> 4 -> 6 -> ...
    After the loop, link the tail of the odd chain to even_head.

    Key invariant: odd and even pointers always refer to the
    current tail of their respective chains.
    """
    if not head or not head.next:
        return head

    odd = head            # starts at node 1 (odd)
    even = head.next      # starts at node 2 (even)
    even_head = even      # save even chain start to attach later

    while even and even.next:
        odd.next = even.next   # odd chain skips over even node
        odd = odd.next         # advance odd tail
        even.next = odd.next   # even chain skips over next odd node
        even = even.next       # advance even tail

    odd.next = even_head       # connect end of odd chain to start of even chain
    return head


# ============================================================
# APPROACH 3: BEST  (same O(1) space, slightly cleaner loop)
# Time: O(n)  |  Space: O(1)
# Identical core algorithm but written to make the pointer
# moves maximally explicit for readability in interviews.
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Same two-chain re-linking as Approach 2, but the loop
    condition and pointer updates are written step-by-step
    to be crystal clear when explaining at a whiteboard.

    We check `even` and `even.next` to ensure we don't go
    out of bounds for both odd and even length lists.
    """
    if not head or not head.next:
        return head

    odd_tail = head
    even_head = head.next
    even_tail = even_head

    while even_tail and even_tail.next:
        # Next odd node is even_tail.next
        next_odd = even_tail.next
        odd_tail.next = next_odd
        odd_tail = next_odd

        # Next even node is odd_tail.next (could be None)
        next_even = odd_tail.next
        even_tail.next = next_even
        even_tail = next_even if next_even else even_tail

        if next_even is None:
            break

    # Attach even chain to end of odd chain
    odd_tail.next = even_head
    return head


if __name__ == "__main__":
    test_cases = [
        ([1, 2, 3, 4, 5],       [1, 3, 5, 2, 4]),
        ([2, 1, 3, 5, 6, 4, 7], [2, 3, 6, 7, 1, 5, 4]),
        ([1],                    [1]),
        ([1, 2],                 [1, 2]),
        ([1, 2, 3],              [1, 3, 2]),
    ]

    print("=== Odd Even Linked List ===\n")
    for values, expected in test_cases:
        b   = list_to_arr(brute_force(build_list(values)))
        o   = list_to_arr(optimal(build_list(values)))
        bst = list_to_arr(best(build_list(values)))
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] Input: {values}")
        print(f"       Brute:   {b}")
        print(f"       Optimal: {o}")
        print(f"       Best:    {bst}")
        print(f"       Expect:  {expected}\n")
