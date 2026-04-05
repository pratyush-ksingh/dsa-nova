"""
Problem: Clone LL with Random Pointer
Difficulty: HARD | XP: 50
"""
from typing import Optional


class Node:
    def __init__(self, val: int):
        self.val = val
        self.next: Optional["Node"] = None
        self.random: Optional["Node"] = None


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# ============================================================
# Use a dictionary mapping each original node -> clone node.
# Pass 1: create all clones.
# Pass 2: wire next and random using the map.
def brute_force(head: Optional[Node]) -> Optional[Node]:
    if not head:
        return None

    node_map: dict[Node, Node] = {}

    # Pass 1: create clones
    curr = head
    while curr:
        node_map[curr] = Node(curr.val)
        curr = curr.next

    # Pass 2: wire pointers
    curr = head
    while curr:
        if curr.next:
            node_map[curr].next = node_map[curr.next]
        if curr.random:
            node_map[curr].random = node_map[curr.random]
        curr = curr.next

    return node_map[head]


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# ============================================================
# Interweave technique (no extra dictionary):
#   Pass 1 — insert clone right after each original node.
#   Pass 2 — set clone.random = original.random.next
#   Pass 3 — unweave: restore original list, extract clone list.
def optimal(head: Optional[Node]) -> Optional[Node]:
    if not head:
        return None

    # Pass 1: interleave
    curr = head
    while curr:
        clone = Node(curr.val)
        clone.next = curr.next
        curr.next = clone
        curr = clone.next

    # Pass 2: set random pointers
    curr = head
    while curr:
        if curr.random:
            curr.next.random = curr.random.next
        curr = curr.next.next

    # Pass 3: separate lists
    dummy = Node(0)
    clone_curr = dummy
    curr = head
    while curr:
        clone_curr.next = curr.next
        curr.next = curr.next.next
        clone_curr = clone_curr.next
        curr = curr.next

    return dummy.next


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# ============================================================
# Same interweave approach as Optimal — this is the asymptotically
# optimal solution (O(n) time, O(1) extra space).
# Written as a clean, Pythonic version with generator-style loops.
def best(head: Optional[Node]) -> Optional[Node]:
    if not head:
        return None

    # Step 1: Interleave clone nodes after each original
    node = head
    while node:
        copy = Node(node.val)
        copy.next = node.next
        node.next = copy
        node = copy.next

    # Step 2: Wire random pointers of clones
    node = head
    while node:
        if node.random:
            node.next.random = node.random.next
        node = node.next.next

    # Step 3: Unweave — restore original and extract clone list
    clone_head = head.next
    node = head
    while node:
        copy = node.next
        node.next = copy.next
        copy.next = copy.next.next if copy.next else None
        node = node.next

    return clone_head


# ---- helpers ----

def build_list(vals: list[int]) -> list[Node]:
    """Build nodes list from values; wire next pointers. Returns node array."""
    nodes = [Node(v) for v in vals]
    for i in range(len(nodes) - 1):
        nodes[i].next = nodes[i + 1]
    return nodes


def print_list(head: Optional[Node]) -> str:
    parts = []
    curr = head
    while curr:
        rval = curr.random.val if curr.random else "null"
        parts.append(f"[{curr.val},{rval}]")
        curr = curr.next
    return " -> ".join(parts)


def verify_deep_copy(orig: Optional[Node], clone: Optional[Node]) -> bool:
    o, c = orig, clone
    while o and c:
        if o is c:
            return False  # same object — not a deep copy
        if o.val != c.val:
            return False
        orig_rand = o.random.val if o.random else None
        clone_rand = c.random.val if c.random else None
        if orig_rand != clone_rand:
            return False
        o, c = o.next, c.next
    return o is None and c is None


if __name__ == "__main__":
    print("=== Clone LL with Random Pointer ===\n")

    # Build: 7 -> 13 -> 11 -> 10 -> 1
    # random: 7->null, 13->7, 11->1, 10->11, 1->7
    nodes = build_list([7, 13, 11, 10, 1])
    nodes[1].random = nodes[0]
    nodes[2].random = nodes[4]
    nodes[3].random = nodes[2]
    nodes[4].random = nodes[0]

    print("Original:", print_list(nodes[0]))

    print("\n[Approach 1 - BruteForce: HashMap]")
    c1 = brute_force(nodes[0])
    print("Clone:   ", print_list(c1))
    print("Deep copy verified:", verify_deep_copy(nodes[0], c1))

    print("\n[Approach 2 - Optimal: Interweave]")
    c2 = optimal(nodes[0])
    print("Clone:   ", print_list(c2))
    print("Deep copy verified:", verify_deep_copy(nodes[0], c2))

    print("\n[Approach 3 - Best: Interweave (clean)]")
    c3 = best(nodes[0])
    print("Clone:   ", print_list(c3))
    print("Deep copy verified:", verify_deep_copy(nodes[0], c3))

    # Edge case: single node with random -> self
    print("\n[Edge case: single node, random -> self]")
    single = Node(42)
    single.random = single
    sc = best(single)
    print(f"Clone val: {sc.val}, random val: {sc.random.val}, "
          f"different object: {sc is not single}")
