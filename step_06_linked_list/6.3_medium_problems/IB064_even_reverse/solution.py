"""Problem: Even Reverse - Reverse even-positioned nodes in a linked list (1-indexed)"""

# ============================================================
# ListNode definition
# ============================================================
class ListNode:
    def __init__(self, val=0, nxt=None):
        self.val = val
        self.next = nxt

    @staticmethod
    def from_list(vals):
        dummy = ListNode()
        cur = dummy
        for v in vals:
            cur.next = ListNode(v)
            cur = cur.next
        return dummy.next

    @staticmethod
    def to_list(head):
        result = []
        while head:
            result.append(head.val)
            head = head.next
        return result


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Collect values at even positions (1-indexed), reverse them, write back
# ============================================================
def brute_force(head: ListNode) -> ListNode:
    even_vals = []
    cur, pos = head, 1
    while cur:
        if pos % 2 == 0:
            even_vals.append(cur.val)
        cur = cur.next
        pos += 1
    even_vals.reverse()
    cur, pos, idx = head, 1, 0
    while cur:
        if pos % 2 == 0:
            cur.val = even_vals[idx]
            idx += 1
        cur = cur.next
        pos += 1
    return head


# ============================================================
# APPROACH 2: OPTIMAL - Extract even nodes, reverse sub-list, re-weave
# Time: O(n)  |  Space: O(n)
# Separate odd and even position nodes into two lists, reverse the even list,
# then merge them back alternately.
# ============================================================
def optimal(head: ListNode) -> ListNode:
    def reverse_list(node):
        prev, cur = None, node
        while cur:
            nxt = cur.next
            cur.next = prev
            prev = cur
            cur = nxt
        return prev

    odd_dummy, even_dummy = ListNode(), ListNode()
    odd_cur, even_cur = odd_dummy, even_dummy
    cur, pos = head, 1
    while cur:
        nxt = cur.next
        cur.next = None
        if pos % 2 == 1:
            odd_cur.next = cur
            odd_cur = odd_cur.next
        else:
            even_cur.next = cur
            even_cur = even_cur.next
        cur = nxt
        pos += 1

    even_head = reverse_list(even_dummy.next)
    # Interleave
    dummy = ListNode()
    res = dummy
    o, e = odd_dummy.next, even_head
    while o or e:
        if o:
            res.next = o
            res = res.next
            o = o.next
        if e:
            res.next = e
            res = res.next
            e = e.next
    res.next = None
    return dummy.next


# ============================================================
# APPROACH 3: BEST - Clean value-only approach (most Pythonic)
# Time: O(n)  |  Space: O(n/2)
# Same as brute force but cleaner with list slice reverse
# ============================================================
def best(head: ListNode) -> ListNode:
    # Collect nodes at even positions
    even_nodes = []
    cur, pos = head, 1
    while cur:
        if pos % 2 == 0:
            even_nodes.append(cur)
        cur = cur.next
        pos += 1
    # Reverse values in even-position nodes
    vals = [n.val for n in even_nodes]
    vals.reverse()
    for node, val in zip(even_nodes, vals):
        node.val = val
    return head


if __name__ == "__main__":
    print("=== Even Reverse ===")
    # 1->2->3->4->5: even positions are 2,4 -> reversed: 4,2 -> result: 1->4->3->2->5
    tests = [
        ([1, 2, 3, 4, 5], [1, 4, 3, 2, 5]),
        ([1, 2, 3, 4],    [1, 4, 3, 2]),
        ([1],              [1]),
        ([1, 2],           [1, 2]),
    ]
    for vals, expected in tests:
        b  = ListNode.to_list(brute_force(ListNode.from_list(vals)))
        o  = ListNode.to_list(optimal(ListNode.from_list(vals)))
        be = ListNode.to_list(best(ListNode.from_list(vals)))
        print(f"Input={vals} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
