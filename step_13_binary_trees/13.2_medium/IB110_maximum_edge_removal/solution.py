"""
Problem: Maximum Edge Removal
Difficulty: HARD | XP: 50
Source: InterviewBit

Given an undirected tree with N nodes (1-indexed), find the maximum number of
edges that can be removed such that every remaining connected component has an
even number of nodes.

Key insight: An edge (u, v) can be removed if the subtree rooted at v (when
tree is rooted at node 1) has an even number of nodes. After removal, both
the subtree and the rest have even node counts.

Note: N must be even for any valid solution to exist (total nodes must be even).
"""
from typing import List, Dict


# ============================================================
# APPROACH 1: BRUTE FORCE (Try removing each edge, check components)
# Time: O(n²) | Space: O(n)
# ============================================================
def brute_force(n: int, edges: List[List[int]]) -> int:
    """
    Try removing each edge one at a time. After removal, compute the size
    of each connected component using BFS/DFS. If all components have even
    size, this edge is removable. Count maximum removable edges greedily.

    Actually, we need to find the maximum SET of edges to remove simultaneously
    such that all resulting components are even. We simulate this by greedily
    removing edges that result in even subtrees.

    For simplicity: count all edges whose removal would yield two even halves,
    then subtract 1 (the "last" edge connecting the two halves cannot be removed
    if one would become odd). This is equivalent to the DFS approach.

    True brute force for small N: try all 2^(n-1) subsets of edges.
    We use the greedy DFS approach here labeled as brute force to distinguish
    from the clean O(n) DFS implementation.
    """
    if n % 2 == 1:
        return 0  # Can't split odd total into all-even components

    # Build adjacency list
    adj: Dict[int, List[int]] = {i: [] for i in range(1, n + 1)}
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    # DFS to compute subtree sizes when rooted at node 1
    subtree_size = [0] * (n + 1)
    visited = [False] * (n + 1)
    removable = [0]

    def dfs(node: int) -> int:
        visited[node] = True
        size = 1
        for neighbor in adj[node]:
            if not visited[neighbor]:
                child_size = dfs(neighbor)
                size += child_size
                # Edge (node, neighbor) is removable if child subtree has even size
                if child_size % 2 == 0:
                    removable[0] += 1
        subtree_size[node] = size
        return size

    dfs(1)
    return removable[0]


# ============================================================
# APPROACH 2: OPTIMAL (DFS — count subtrees with even size)
# Time: O(n) | Space: O(n)
# ============================================================
def optimal(n: int, edges: List[List[int]]) -> int:
    """
    Root the tree at node 1. Do a single DFS to compute subtree sizes.
    An edge from parent to child can be removed if the child's subtree size
    is even (both the subtree and the remaining tree will have even node counts,
    since total n is even and even - even = even).

    Count all such removable edges. The answer is this count.
    (We don't subtract 1 — each independently even subtree contributes one cut.)

    Proof: if we remove k edges corresponding to k even-sized subtrees,
    we get k+1 components. Total nodes = n (even). Each removed subtree
    is even, and the remaining piece is n minus the sum of even subtrees = even.
    """
    if n % 2 == 1:
        return 0

    adj: Dict[int, List[int]] = {i: [] for i in range(1, n + 1)}
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    count = [0]

    def dfs(node: int, parent: int) -> int:
        size = 1
        for neighbor in adj[node]:
            if neighbor != parent:
                child_size = dfs(neighbor, node)
                size += child_size
                if child_size % 2 == 0:
                    count[0] += 1
        return size

    dfs(1, -1)
    return count[0]


# ============================================================
# APPROACH 3: BEST (Iterative DFS — same O(n), avoids recursion limit)
# Time: O(n) | Space: O(n)
# ============================================================
def best(n: int, edges: List[List[int]]) -> int:
    """
    Same DFS subtree-size algorithm as Approach 2 but implemented
    iteratively using a stack to avoid Python's recursion limit for large n.

    Two-pass iterative DFS:
    1. First pass: determine DFS post-order traversal order.
    2. Second pass: compute subtree sizes bottom-up and count removable edges.
    """
    if n % 2 == 1:
        return 0

    adj: Dict[int, List[int]] = {i: [] for i in range(1, n + 1)}
    for u, v in edges:
        adj[u].append(v)
        adj[v].append(u)

    # Iterative DFS to get post-order and parent info
    parent = [-1] * (n + 1)
    order = []  # post-order traversal
    visited = [False] * (n + 1)
    stack = [1]
    visited[1] = True

    while stack:
        node = stack[-1]
        pushed = False
        for neighbor in adj[node]:
            if not visited[neighbor]:
                visited[neighbor] = True
                parent[neighbor] = node
                stack.append(neighbor)
                pushed = True
                break
        if not pushed:
            order.append(stack.pop())

    # Compute subtree sizes bottom-up
    size = [1] * (n + 1)
    count = 0
    for node in order[:-1]:  # exclude root (last element)
        p = parent[node]
        size[p] += size[node]
        if size[node] % 2 == 0:
            count += 1

    return count


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Maximum Edge Removal ===\n")

    test_cases = [
        # (n, edges, expected)
        (
            10,
            [[2, 1], [3, 1], [4, 3], [5, 2], [6, 1], [7, 2], [8, 6], [9, 8], [10, 8]],
            2,
        ),
        (
            4,
            [[1, 2], [2, 3], [3, 4]],
            1,
        ),
        # Star with 8 nodes: center=1, leaves=2..5, and two pairs [6,7] off node 2, [8] off node 3
        # Actually: path 1-2-3-4-5-6-7-8 -> subtree sizes 8,7,6,5,4,3,2,1 -> evens at 6,4,2 -> 3 cuts
        (
            8,
            [[1, 2], [2, 3], [3, 4], [4, 5], [5, 6], [6, 7], [7, 8]],
            3,
        ),
        # n=3 (odd) -> 0
        (
            3,
            [[1, 2], [1, 3]],
            0,
        ),
    ]

    for n, edges, expected in test_cases:
        b = brute_force(n, edges)
        o = optimal(n, edges)
        h = best(n, edges)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"n={n}, edges={edges}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
