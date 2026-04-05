"""Problem: Add Two Numbers in LL
Difficulty: MEDIUM | XP: 25"""

from typing import Optional


class ListNode:
    def __init__(self, val=0, nxt=None):
        self.val = val
        self.next = nxt


def build(vals):
    """Build LL from list; index 0 = least significant digit."""
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
# Time: O(n + m)  |  Space: O(n + m)
# Extract digits to strings, add as integers, rebuild LL.
# ============================================================
def brute_force(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    d1, d2 = to_list(l1), to_list(l2)
    # digits are LSB first — reverse for int conversion
    n1 = int(''.join(map(str, d1[::-1])))
    n2 = int(''.join(map(str, d2[::-1])))
    total = str(n1 + n2)
    # result back to LSB first
    return build([int(c) for c in reversed(total)])


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(max(n, m))  |  Space: O(max(n, m))
# Traverse both lists simultaneously with carry.
# ============================================================
def optimal(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    dummy = ListNode()
    cur = dummy
    carry = 0

    while l1 or l2 or carry:
        total = carry
        if l1:
            total += l1.val
            l1 = l1.next
        if l2:
            total += l2.val
            l2 = l2.next
        carry = total // 10
        cur.next = ListNode(total % 10)
        cur = cur.next

    return dummy.next


# ============================================================
# APPROACH 3: BEST
# Time: O(max(n, m))  |  Space: O(max(n, m)) recursion stack
# Recursive helper; elegant and concise.
# ============================================================
def _add_helper(l1, l2, carry):
    if not l1 and not l2 and carry == 0:
        return None
    total = carry
    if l1:
        total += l1.val
        l1 = l1.next
    if l2:
        total += l2.val
        l2 = l2.next
    node = ListNode(total % 10)
    node.next = _add_helper(l1, l2, total // 10)
    return node


def best(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    return _add_helper(l1, l2, 0)


if __name__ == "__main__":
    tests = [
        # (l1_digits_lsb_first, l2_digits_lsb_first, expected_lsb_first)
        ([2, 4, 3],             [5, 6, 4],             [7, 0, 8]),         # 342+465=807
        ([9, 9, 9, 9, 9, 9, 9],[9, 9, 9, 9],           [8, 9, 9, 9, 0, 0, 0, 1]),  # 9999999+9999=10009998
        ([5],                   [5],                   [0, 1]),            # 5+5=10
        ([0],                   [0],                   [0]),               # 0+0=0
    ]
    print("=== Add Two Numbers in LL ===")
    for d1, d2, expected in tests:
        b  = to_list(brute_force(build(d1), build(d2)))
        o  = to_list(optimal(build(d1), build(d2)))
        be = to_list(best(build(d1), build(d2)))
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"{d1} + {d2} = {be} (expected {expected}) | {status}")
