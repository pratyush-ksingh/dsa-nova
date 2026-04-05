"""
Problem: Order of People Heights
Difficulty: HARD | XP: 50
Source: InterviewBit

You are given two arrays A (heights) and B (infronts).
A[i] is the height of person i. B[i] is the number of people
in front of person i who are taller than or equal to them.
Reconstruct the original queue (the position each person stands in).
Return an array of heights in the reconstructed order.
"""
from typing import List
from itertools import permutations


# ============================================================
# APPROACH 1: BRUTE FORCE (Try all permutations, verify constraints)
# Time: O(n! * n) | Space: O(n)
# ============================================================
def brute_force(heights: List[int], infronts: List[int]) -> List[int]:
    """
    Try every permutation of people. For each permutation, verify that
    every person satisfies their 'infronts' constraint. Return the valid one.
    """
    n = len(heights)
    people = list(range(n))  # indices of people

    for perm in permutations(people):
        queue = [heights[i] for i in perm]
        valid = True
        for pos, idx in enumerate(perm):
            h = heights[idx]
            inf = infronts[idx]
            # Count people in front (positions 0..pos-1) with height >= h
            count = sum(1 for j in range(pos) if queue[j] >= h)
            if count != inf:
                valid = False
                break
        if valid:
            return queue

    return []  # should not reach here on valid input


# ============================================================
# APPROACH 2: OPTIMAL (Sort by height desc, insert at index=infronts)
# Time: O(n²) | Space: O(n)
# ============================================================
def optimal(heights: List[int], infronts: List[int]) -> List[int]:
    """
    Greedy insight: pair each person as (height, infronts) and sort
    by height descending (ties broken by infronts ascending).

    After sorting, insert each person at index `infronts` in the result list.
    Why this works:
    - Process tallest people first. When we insert person (h, k), all people
      already in the result are >= h, so inserting at index k means exactly
      k people taller are in front — satisfying the constraint.
    - Shorter people inserted later don't affect the taller people's counts.
    """
    people = sorted(zip(heights, infronts), key=lambda x: (-x[0], x[1]))
    result = []
    for h, k in people:
        result.insert(k, h)
    return result


# ============================================================
# APPROACH 3: BEST (Same greedy, with linked-list insight for O(n log n))
# Time: O(n²) in Python (list.insert is O(n)); O(n log n) with BIT/seg tree
# Space: O(n)
# ============================================================
def best(heights: List[int], infronts: List[int]) -> List[int]:
    """
    Same greedy strategy as Approach 2. For truly O(n log n) insertion,
    one would use a Binary Indexed Tree (BIT/Fenwick tree) to find the
    k-th free slot in O(log n). In Python, we implement the clean greedy
    version (identical to Approach 2) as the 'best' production approach,
    since list.insert is O(n) and the BIT variant is complex.

    BIT approach summary (for interview discussion):
    - Maintain a BIT over n positions. All positions start as "free" (value=1).
    - Sort by height desc. For each (h, k): find the (k+1)-th free position
      using binary search on BIT prefix sums → O(log n). Mark it used.
    - This gives O(n log n) total.
    """
    people = sorted(zip(heights, infronts), key=lambda x: (-x[0], x[1]))
    result = []
    for h, k in people:
        result.insert(k, h)
    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Order of People Heights ===\n")

    test_cases = [
        (
            [5, 3, 2, 6, 1, 4],
            [0, 1, 2, 0, 3, 2],
            [5, 3, 2, 1, 6, 4],  # one valid arrangement
        ),
        (
            [6, 5, 4, 3, 2, 1],
            [0, 0, 0, 0, 0, 0],
            [1, 2, 3, 4, 5, 6],  # All k=0: each in strictly ascending order so no one taller is ahead
        ),
        (
            [1, 2],
            [1, 0],
            [2, 1],
        ),
    ]

    for heights, infronts, expected in test_cases:
        # Brute force only for small inputs
        b = brute_force(heights[:], infronts[:]) if len(heights) <= 7 else "skipped"
        o = optimal(heights[:], infronts[:])
        h = best(heights[:], infronts[:])

        def verify(result, hts, inf):
            if not result:
                return False
            n = len(result)
            for i in range(n):
                # Find which original person is at position i:
                # since result contains heights, we match by checking infronts property
                pass
            return result == expected  # simplified check against expected

        status = "PASS" if o == expected and h == expected else "FAIL"
        print(f"Heights:  {heights}")
        print(f"Infronts: {infronts}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
