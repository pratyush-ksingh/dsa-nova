"""
Problem: Sort a Linked List of 0s, 1s, and 2s
Difficulty: MEDIUM | XP: 25

Given a linked list where every node has value 0, 1, or 2,
sort it without using any sorting algorithm (exploit the limited value range).
"""
from typing import Optional


class ListNode:
    def __init__(self, val: int = 0, next: 'Optional[ListNode]' = None):
        self.val = val
        self.next = next


def make_list(values: list) -> Optional[ListNode]:
    dummy = ListNode(0)
    cur = dummy
    for v in values:
        cur.next = ListNode(v)
        cur = cur.next
    return dummy.next


def list_to_arr(head: Optional[ListNode]) -> list:
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result


# ============================================================
# APPROACH 1: BRUTE FORCE - Count 0s, 1s, 2s and overwrite
# Time: O(2n) = O(n)  |  Space: O(1)
# ============================================================
def brute_force(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Two-pass approach:
    Pass 1: Count occurrences of 0, 1, and 2.
    Pass 2: Overwrite node values in sorted order (count[0] zeros, then ones, then twos).
    Simple but requires two traversals and modifies values in-place.
    """
    if not head:
        return head

    count = [0, 0, 0]
    cur = head
    while cur:
        count[cur.val] += 1
        cur = cur.next

    cur = head
    val = 0
    while cur:
        while count[val] == 0:
            val += 1
        cur.val = count[val]  # BUG-FREE: set value, not count
        # Actually: write 'val' (not count[val])
        cur.val = val
        count[val] -= 1
        cur = cur.next

    return head


# ============================================================
# APPROACH 2: OPTIMAL - Three dummy lists (Dutch National Flag via linking)
# Time: O(n)  |  Space: O(1)
# ============================================================
def optimal(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Create three dummy heads for lists of 0s, 1s, and 2s.
    Traverse original list and append each node to the appropriate sub-list.
    Finally, link: zeros -> ones -> twos -> None.
    Single pass, O(1) extra space (only 6 pointers), preserves node identity.
    """
    if not head:
        return head

    d0 = ListNode(0)  # dummy head for 0-list
    d1 = ListNode(0)  # dummy head for 1-list
    d2 = ListNode(0)  # dummy head for 2-list
    t0, t1, t2 = d0, d1, d2  # tails

    cur = head
    while cur:
        nxt = cur.next
        cur.next = None  # detach
        if cur.val == 0:
            t0.next = cur
            t0 = t0.next
        elif cur.val == 1:
            t1.next = cur
            t1 = t1.next
        else:
            t2.next = cur
            t2 = t2.next
        cur = nxt

    # Link the three lists
    t0.next = d1.next if d1.next else d2.next
    t1.next = d2.next
    t2.next = None

    return d0.next


# ============================================================
# APPROACH 3: BEST - Same three-list partition (with explicit null guards)
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    Identical to Approach 2 but written more explicitly with
    separate 'connect non-empty' logic. This pattern is cleaner
    to write in interviews because it makes the connection step obvious.
    """
    if not head:
        return head

    # Create dummy sentinels and tails for each bucket
    heads = [ListNode(-1) for _ in range(3)]
    tails = heads[:]

    cur = head
    while cur:
        v = cur.val
        tails[v].next = cur
        tails[v] = cur
        cur = cur.next

    # Terminate tail of 2s list
    tails[2].next = None

    # Chain non-empty buckets together
    # Connect 0s -> 1s (or 2s if 1s empty)
    if tails[0] != heads[0]:  # 0s list is non-empty
        tails[0].next = heads[1].next if heads[1].next else heads[2].next
    # Connect 1s -> 2s
    if tails[1] != heads[1]:
        tails[1].next = heads[2].next

    # Return first non-empty bucket
    for i in range(3):
        if heads[i].next:
            return heads[i].next
    return None


if __name__ == "__main__":
    print("=== Sort LL of 0s 1s 2s ===")
    test_cases = [
        ([1, 2, 0, 2, 1, 0],    [0, 0, 1, 1, 2, 2]),
        ([0, 1, 2],             [0, 1, 2]),
        ([2, 1, 0],             [0, 1, 2]),
        ([0],                   [0]),
        ([2, 2, 2],             [2, 2, 2]),
        ([0, 0, 1],             [0, 0, 1]),
        ([1, 1, 2, 0, 0],       [0, 0, 1, 1, 2]),
    ]
    for values, expected in test_cases:
        b   = list_to_arr(brute_force(make_list(values)))
        o   = list_to_arr(optimal(make_list(values)))
        bst = list_to_arr(best(make_list(values)))
        status = "OK" if b == o == bst == expected else "FAIL"
        print(f"[{status}] input={values} | Brute={b}, Optimal={o}, Best={bst}")
