"""Problem: Swap Nodes in Pairs - Swap every two adjacent nodes in a linked list"""

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
# Collect values, swap adjacent pairs in the list, rebuild
# ============================================================
def brute_force(head: ListNode) -> ListNode:
    vals = ListNode.to_list(head)
    for i in range(0, len(vals) - 1, 2):
        vals[i], vals[i + 1] = vals[i + 1], vals[i]
    return ListNode.from_list(vals)


# ============================================================
# APPROACH 2: OPTIMAL - Iterative pointer manipulation
# Time: O(n)  |  Space: O(1)
# Use dummy head; for each pair, rewire so second node comes first.
# ============================================================
def optimal(head: ListNode) -> ListNode:
    dummy = ListNode(0, head)
    prev = dummy
    while prev.next and prev.next.next:
        first  = prev.next
        second = prev.next.next
        # Rewire
        first.next  = second.next
        second.next = first
        prev.next   = second
        prev = first  # advance past the swapped pair
    return dummy.next


# ============================================================
# APPROACH 3: BEST - Recursive
# Time: O(n)  |  Space: O(n/2) call stack
# Base case: 0 or 1 nodes. Swap head pair, recurse on rest.
# ============================================================
def best(head: ListNode) -> ListNode:
    if not head or not head.next:
        return head
    first, second = head, head.next
    first.next  = best(second.next)
    second.next = first
    return second


if __name__ == "__main__":
    print("=== Swap Nodes in Pairs ===")
    tests = [
        ([1, 2, 3, 4], [2, 1, 4, 3]),
        ([1],           [1]),
        ([],            []),
        ([1, 2, 3],    [2, 1, 3]),
    ]
    for vals, expected in tests:
        b  = ListNode.to_list(brute_force(ListNode.from_list(vals)))
        o  = ListNode.to_list(optimal(ListNode.from_list(vals)))
        be = ListNode.to_list(best(ListNode.from_list(vals)))
        print(f"Input={vals} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
