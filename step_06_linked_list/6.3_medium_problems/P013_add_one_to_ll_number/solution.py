"""Problem: Add One to LL Number
Difficulty: MEDIUM | XP: 25"""

from typing import Optional


class ListNode:
    def __init__(self, val=0, nxt=None):
        self.val = val
        self.next = nxt


def build(vals):
    dummy = ListNode()
    cur = dummy
    for v in vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


def to_list(node):
    res, cur = [], node
    while cur:
        res.append(cur.val)
        cur = cur.next
    return res


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Convert to integer, add 1, rebuild LL.
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    digits = to_list(head)
    num = int(''.join(map(str, digits))) + 1
    return build([int(d) for d in str(num)])


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# Reverse list, add carry from head, reverse back.
# ============================================================
def _reverse(head):
    prev, cur = None, head
    while cur:
        nxt = cur.next
        cur.next = prev
        prev = cur
        cur = nxt
    return prev


def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    head = _reverse(head)
    cur = head
    carry = 1
    prev = None
    while cur and carry:
        s = cur.val + carry
        cur.val = s % 10
        carry = s // 10
        prev = cur
        cur = cur.next
    if carry:
        prev.next = ListNode(carry)
    return _reverse(head)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n) recursion stack
# Recursive: propagate carry from tail back to head.
# Avoids explicit reversal; cleaner code.
# ============================================================
def _add_carry(node: Optional[ListNode]) -> int:
    if node is None:
        return 1  # initial carry
    carry = _add_carry(node.next)
    total = node.val + carry
    node.val = total % 10
    return total // 10


def best(head: Optional[ListNode]) -> Optional[ListNode]:
    carry = _add_carry(head)
    if carry:
        new_head = ListNode(carry)
        new_head.next = head
        return new_head
    return head


if __name__ == "__main__":
    tests = [
        ([1, 2, 3],    [1, 2, 4]),
        ([9, 9, 9],    [1, 0, 0, 0]),
        ([0],          [1]),
        ([1, 0, 0, 0], [1, 0, 0, 1]),
    ]
    print("=== Add One to LL Number ===")
    for vals, expected in tests:
        b  = to_list(brute_force(build(vals)))
        o  = to_list(optimal(build(vals)))
        be = to_list(best(build(vals)))
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"Input: {vals} | Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}")
