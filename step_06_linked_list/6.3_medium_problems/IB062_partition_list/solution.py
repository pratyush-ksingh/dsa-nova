"""Problem: Partition List
Difficulty: MEDIUM | XP: 25"""

from typing import Optional


class ListNode:
    def __init__(self, val=0, nxt=None):
        self.val = val
        self.next = nxt

    def __repr__(self):
        vals, cur = [], self
        while cur:
            vals.append(cur.val)
            cur = cur.next
        return str(vals)


def build(vals):
    dummy = ListNode()
    cur = dummy
    for v in vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Collect values into two lists; rebuild linked list.
# ============================================================
def brute_force(head: Optional[ListNode], x: int) -> Optional[ListNode]:
    less, greater = [], []
    cur = head
    while cur:
        if cur.val < x:
            less.append(cur.val)
        else:
            greater.append(cur.val)
        cur = cur.next
    return build(less + greater)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# Two dummy-headed sublists; merge in one pass. No extra nodes.
# ============================================================
def optimal(head: Optional[ListNode], x: int) -> Optional[ListNode]:
    less_head = ListNode(0)
    greater_head = ListNode(0)
    less = less_head
    greater = greater_head

    cur = head
    while cur:
        if cur.val < x:
            less.next = cur
            less = less.next
        else:
            greater.next = cur
            greater = greater.next
        cur = cur.next

    greater.next = None
    less.next = greater_head.next
    return less_head.next


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# Same two-pointer approach; explicitly detach nodes before linking.
# ============================================================
def best(head: Optional[ListNode], x: int) -> Optional[ListNode]:
    d_less = ListNode(0)
    d_greater = ListNode(0)
    p_less = d_less
    p_greater = d_greater

    while head:
        nxt = head.next
        head.next = None
        if head.val < x:
            p_less.next = head
            p_less = p_less.next
        else:
            p_greater.next = head
            p_greater = p_greater.next
        head = nxt

    p_less.next = d_greater.next
    return d_less.next


if __name__ == "__main__":
    tests = [
        ([1, 4, 3, 2, 5, 2], 3, [1, 2, 2, 4, 3, 5]),
        ([2, 1],             2, [1, 2]),
        ([1, 2, 3],         10, [1, 2, 3]),
        ([3, 2, 1],          2, [1, 3, 2]),
    ]
    print("=== Partition List ===")
    for vals, x, expected in tests:
        b  = brute_force(build(vals), x)
        o  = optimal(build(vals), x)
        be = best(build(vals), x)

        def to_list(node):
            res, cur = [], node
            while cur:
                res.append(cur.val)
                cur = cur.next
            return res

        b_list  = to_list(b)
        o_list  = to_list(o)
        be_list = to_list(be)
        status = "PASS" if b_list == o_list == be_list == expected else "FAIL"
        print(f"Input: {vals}, x={x} | Best: {be_list} | Expected: {expected} | {status}")
