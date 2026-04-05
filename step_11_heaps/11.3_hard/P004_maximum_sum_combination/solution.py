"""
Problem: Maximum Sum Combination
Difficulty: HARD | XP: 50

Given two integer arrays A and B of size N, find the C largest sums
formed by picking one element from A and one from B (A[i] + B[j]).
Return the result in descending order.
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE (Generate all N² sums, sort)
# Time: O(N² log N) | Space: O(N²)
# ============================================================
def brute_force(A: List[int], B: List[int], C: int) -> List[int]:
    """
    Enumerate every possible pair (i, j) and compute A[i]+B[j].
    Sort all N² sums in descending order and return the top C.
    """
    sums = []
    for a in A:
        for b in B:
            sums.append(a + b)
    sums.sort(reverse=True)
    return sums[:C]


# ============================================================
# APPROACH 2: OPTIMAL (Max-Heap + visited set)
# Time: O(N log N + C log C) | Space: O(N + C)
# ============================================================
def optimal(A: List[int], B: List[int], C: int) -> List[int]:
    """
    Sort both arrays descending. The maximum sum is A[0]+B[0].
    Use a max-heap (negate values) seeded with index pair (0,0).
    Each extraction of (i,j) expands to neighbors (i+1,j) and (i,j+1).
    A visited set prevents duplicate entries in the heap.
    Extract C times to collect the top C sums.
    """
    n = len(A)
    A_s = sorted(A, reverse=True)
    B_s = sorted(B, reverse=True)

    result = []
    # Heap stores (-sum, i, j) so the largest sum is always at the top
    heap = [(-A_s[0] - B_s[0], 0, 0)]
    visited = {(0, 0)}

    for _ in range(C):
        neg_sum, i, j = heapq.heappop(heap)
        result.append(-neg_sum)

        if i + 1 < n and (i + 1, j) not in visited:
            heapq.heappush(heap, (-A_s[i + 1] - B_s[j], i + 1, j))
            visited.add((i + 1, j))

        if j + 1 < n and (i, j + 1) not in visited:
            heapq.heappush(heap, (-A_s[i] - B_s[j + 1], i, j + 1))
            visited.add((i, j + 1))

    return result


# ============================================================
# APPROACH 3: BEST (Same Max-Heap — clean production form)
# Time: O(N log N + C log C) | Space: O(N + C)
# ============================================================
def best(A: List[int], B: List[int], C: int) -> List[int]:
    """
    Identical to Approach 2. The algorithm is already optimal.
    This version uses a while-loop instead of a for-loop, which
    reads more naturally as "keep extracting until we have C results."
    Key insight: the sum grid A_s[i]+B_s[j] decreases as i or j
    increases (since both arrays are sorted descending), so BFS over
    this grid via a max-heap always yields sums in descending order.
    """
    n = len(A)
    A_s = sorted(A, reverse=True)
    B_s = sorted(B, reverse=True)

    result = []
    heap = [(-A_s[0] - B_s[0], 0, 0)]
    seen = {(0, 0)}

    while len(result) < C:
        neg_sum, i, j = heapq.heappop(heap)
        result.append(-neg_sum)

        if i + 1 < n and (i + 1, j) not in seen:
            heapq.heappush(heap, (-A_s[i + 1] - B_s[j], i + 1, j))
            seen.add((i + 1, j))

        if j + 1 < n and (i, j + 1) not in seen:
            heapq.heappush(heap, (-A_s[i] - B_s[j + 1], i, j + 1))
            seen.add((i, j + 1))

    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Maximum Sum Combination ===\n")

    test_cases = [
        ([3, 2], [1, 4], 2, [7, 6]),
        ([1, 4, 2, 3], [2, 5, 1, 6], 4, [10, 9, 9, 8]),
        ([1, 2], [3, 4], 3, [6, 5, 5]),
        ([5], [5], 1, [10]),
    ]

    for A, B, C, expected in test_cases:
        b = brute_force(A[:], B[:], C)
        o = optimal(A[:], B[:], C)
        h = best(A[:], B[:], C)
        status = "PASS" if b == expected and o == expected and h == expected else "FAIL"
        print(f"A={A}, B={B}, C={C}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
