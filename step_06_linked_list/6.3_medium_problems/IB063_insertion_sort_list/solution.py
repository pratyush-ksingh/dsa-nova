"""Problem: Insertion Sort List - Sort a linked list using insertion sort"""

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
# Time: O(n^2) via sort O(n log n)  |  Space: O(n)
# Collect values, sort them, rebuild the list
# ============================================================
def brute_force(head: ListNode) -> ListNode:
    vals = []
    cur = head
    while cur:
        vals.append(cur.val)
        cur = cur.next
    vals.sort()
    dummy = ListNode()
    cur = dummy
    for v in vals:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


# ============================================================
# APPROACH 2: OPTIMAL - Classic Insertion Sort on linked list
# Time: O(n^2)  |  Space: O(1)
# Maintain sorted prefix via dummy head; insert each node in correct place.
# ============================================================
def optimal(head: ListNode) -> ListNode:
    dummy = ListNode(float('-inf'))
    cur = head
    while cur:
        nxt = cur.next
        # Find insertion point
        prev = dummy
        while prev.next and prev.next.val < cur.val:
            prev = prev.next
        cur.next = prev.next
        prev.next = cur
        cur = nxt
    return dummy.next


# ============================================================
# APPROACH 3: BEST - Insertion Sort with tail optimization
# Time: O(n^2) worst, O(n) on sorted  |  Space: O(1)
# If current node >= last sorted value, skip the inner scan and extend tail.
# ============================================================
def best(head: ListNode) -> ListNode:
    dummy = ListNode(float('-inf'))
    last_sorted = dummy
    cur = head
    while cur:
        nxt = cur.next
        if cur.val >= last_sorted.val:
            last_sorted.next = cur
            last_sorted = last_sorted.next
        else:
            prev = dummy
            while prev.next and prev.next.val < cur.val:
                prev = prev.next
            cur.next = prev.next
            prev.next = cur
        last_sorted.next = nxt
        cur = nxt
    return dummy.next


if __name__ == "__main__":
    print("=== Insertion Sort List ===")
    tests = [
        [4, 2, 1, 3],
        [-1, 5, 3, 4, 0],
        [1],
        [3, 1, 2],
    ]
    for vals in tests:
        expected = sorted(vals)
        b  = ListNode.to_list(brute_force(ListNode.from_list(vals)))
        o  = ListNode.to_list(optimal(ListNode.from_list(vals)))
        be = ListNode.to_list(best(ListNode.from_list(vals)))
        print(f"Input={vals} => Brute={b}, Optimal={o}, Best={be}")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
