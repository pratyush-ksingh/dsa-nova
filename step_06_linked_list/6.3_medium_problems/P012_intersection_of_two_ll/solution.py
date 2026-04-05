"""
Problem: Intersection of Two Linked Lists (LeetCode 160)
Difficulty: MEDIUM | XP: 25

Find the node at which two singly linked lists intersect.
Return None if they do not intersect.
"""
from typing import Optional


class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next


# ============================================================
# APPROACH 1: BRUTE FORCE — HashSet
# Time: O(m + n)  |  Space: O(m)
# ============================================================
def brute_force(headA: Optional[ListNode], headB: Optional[ListNode]) -> Optional[ListNode]:
    """
    Store all nodes of list A in a set, then traverse list B
    checking if any node is already in the set.
    """
    visited = set()
    curr = headA
    while curr:
        visited.add(curr)
        curr = curr.next

    curr = headB
    while curr:
        if curr in visited:
            return curr
        curr = curr.next

    return None


# ============================================================
# APPROACH 2: OPTIMAL — Length Difference + Align
# Time: O(m + n)  |  Space: O(1)
# ============================================================
def optimal(headA: Optional[ListNode], headB: Optional[ListNode]) -> Optional[ListNode]:
    """
    Compute lengths of both lists. Advance the pointer of the
    longer list by the difference, then walk both together
    until they meet.
    """
    def get_length(node):
        length = 0
        while node:
            length += 1
            node = node.next
        return length

    lenA = get_length(headA)
    lenB = get_length(headB)

    currA, currB = headA, headB

    # Advance the longer list
    while lenA > lenB:
        currA = currA.next
        lenA -= 1
    while lenB > lenA:
        currB = currB.next
        lenB -= 1

    # Walk together until intersection or end
    while currA != currB:
        currA = currA.next
        currB = currB.next

    return currA  # None if no intersection


# ============================================================
# APPROACH 3: BEST — Two Pointer Switch Heads
# Time: O(m + n)  |  Space: O(1)
# ============================================================
def best(headA: Optional[ListNode], headB: Optional[ListNode]) -> Optional[ListNode]:
    """
    Two pointers start at headA and headB.
    When one reaches the end it switches to the OTHER list's head.
    After at most (m + n) steps they either meet at the intersection
    or both reach None simultaneously (no intersection).

    Key insight: both pointers travel the same total distance
    (lenA + lenB), so they align at the intersection node.
    """
    if not headA or not headB:
        return None

    pA, pB = headA, headB
    while pA != pB:
        pA = pA.next if pA else headB
        pB = pB.next if pB else headA

    return pA


# ============================================================
# DEMO
# ============================================================
def build_intersecting_lists():
    # Shared tail: 8 -> 4 -> 5
    shared = ListNode(8, ListNode(4, ListNode(5)))
    # List A: 4 -> 1 -> [8 -> 4 -> 5]
    headA = ListNode(4, ListNode(1, shared))
    # List B: 5 -> 6 -> 1 -> [8 -> 4 -> 5]
    headB = ListNode(5, ListNode(6, ListNode(1, shared)))
    return headA, headB


if __name__ == "__main__":
    print("=== Intersection of Two Linked Lists ===")
    headA, headB = build_intersecting_lists()
    node = brute_force(headA, headB)
    print(f"Brute:   intersects at node with val = {node.val if node else None}")

    headA, headB = build_intersecting_lists()
    node = optimal(headA, headB)
    print(f"Optimal: intersects at node with val = {node.val if node else None}")

    headA, headB = build_intersecting_lists()
    node = best(headA, headB)
    print(f"Best:    intersects at node with val = {node.val if node else None}")
