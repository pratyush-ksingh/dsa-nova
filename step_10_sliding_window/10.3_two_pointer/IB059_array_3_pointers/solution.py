"""
Problem: Array 3 Pointers (InterviewBit)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

You are given 3 sorted arrays A, B, C. Find indices i, j, k such that
max(|A[i]-B[j]|, |B[j]-C[k]|, |C[k]-A[i]|) is minimized.

Return the minimum value of max(|A[i]-B[j]|, |B[j]-C[k]|, |C[k]-A[i]|).

Example:
  A = [1, 4, 10], B = [2, 15, 20], C = [10, 12]
  i=0, j=0, k=0: max(|1-2|, |2-10|, |10-1|) = max(1,8,9) = 9
  i=2, j=0, k=0: max(|10-2|, |2-10|, |10-10|) = max(8,8,0) = 8
  i=0, j=0, k=1: max(|1-2|, |2-12|, |12-1|) = max(1,10,11) = 11
  i=2, j=0, k=1: max(|10-2|, |2-12|, |12-10|) = max(8,10,2) = 10
  Optimal: i=0, j=0, k=0 is not optimal. Try i=2,j=0,k=0: 8 or i=0,j=0,k=0: 9
  Answer: 5 (i=0,j=0,k=0 gives max=9, but let me recheck)
  Actually for A=[1,4,10], B=[2,15,20], C=[10,12]:
  Best is i=2,j=0,k=1: max(|10-2|, |2-12|, |12-10|) = max(8,10,2) = 10? No...
  Let me try i=0,j=0,k=0: max(1,8,9)=9
  Try i=2,j=0,k=0: max(8,8,0)=8
  Try i=2,j=0,k=1: max(8,10,2)=10
  So minimum = 8 for this example? Let me verify: A[2]=10,B[0]=2,C[0]=10 -> max(8,8,0)=8. Yes.

Note: The answer is max(A[i],B[j],C[k]) - min(A[i],B[j],C[k]).
This is because if we have three numbers x, y, z, the maximum pairwise
absolute difference equals max - min.
"""
from typing import List
import heapq


def compute_diff(a: int, b: int, c: int) -> int:
    """max pairwise absolute difference = max(a,b,c) - min(a,b,c)."""
    return max(a, b, c) - min(a, b, c)


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n*m*k)  |  Space: O(1)
#
# Try all combinations of indices (i, j, k) and compute the
# maximum pairwise absolute difference for each triplet.
# Return the minimum over all triplets.
# ============================================================
def brute_force(A: List[int], B: List[int], C: List[int]) -> int:
    """Try all triplets O(n*m*k)."""
    result = float('inf')
    for a in A:
        for b in B:
            for c in C:
                result = min(result, compute_diff(a, b, c))
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Three Pointers
# Time: O(n + m + k)  |  Space: O(1)
#
# Key insight: max(|A[i]-B[j]|, |B[j]-C[k]|, |C[k]-A[i]|)
# = max(A[i],B[j],C[k]) - min(A[i],B[j],C[k])
#
# At each step, we have the current triplet (A[i], B[j], C[k]).
# The difference = max - min. To minimize this, we should advance
# the pointer pointing to the minimum element (since increasing
# the minimum can only decrease or maintain max-min, while
# decreasing the maximum would require going backwards which we can't).
#
# We advance until one array is exhausted.
# ============================================================
def optimal(A: List[int], B: List[int], C: List[int]) -> int:
    """Three pointers: always advance the pointer to the minimum element."""
    i = j = k = 0
    result = float('inf')

    while i < len(A) and j < len(B) and k < len(C):
        curr_diff = compute_diff(A[i], B[j], C[k])
        result = min(result, curr_diff)

        if result == 0:
            return 0  # Can't do better

        # Advance the pointer to the minimum element
        min_val = min(A[i], B[j], C[k])
        if A[i] == min_val:
            i += 1
        elif B[j] == min_val:
            j += 1
        else:
            k += 1

    return result


# ============================================================
# APPROACH 3: BEST -- Three Pointers with Min-Heap
# Time: O((n+m+k) log 3) = O(n+m+k)  |  Space: O(1)
#
# Same three-pointer approach but use a min-heap of size 3 to
# elegantly identify and advance the minimum pointer.
# The heap always holds one element from each array.
# Pop the minimum, record the range (max - min), push the next
# element from the same array. Stop when any array is exhausted.
#
# This formulation generalizes naturally to K sorted arrays.
# ============================================================
def best(A: List[int], B: List[int], C: List[int]) -> int:
    """Three pointers via min-heap: generalizes to K arrays."""
    # heap entries: (value, array_id, index_in_array)
    arrays = [A, B, C]
    heap = [(arrays[aid][0], aid, 0) for aid in range(3)]
    heapq.heapify(heap)
    current_max = max(A[0], B[0], C[0])
    result = current_max - heap[0][0]

    while True:
        min_val, aid, idx = heapq.heappop(heap)
        idx += 1
        if idx >= len(arrays[aid]):
            break  # This array is exhausted
        next_val = arrays[aid][idx]
        heapq.heappush(heap, (next_val, aid, idx))
        current_max = max(current_max, next_val)
        # current min is heap[0][0]
        result = min(result, current_max - heap[0][0])
        if result == 0:
            return 0

    return result


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Array 3 Pointers ===\n")

    tests = [
        ([1, 4, 10],   [2, 15, 20],  [10, 12],    5),
        ([1, 2, 3],    [4, 5, 6],    [7, 8, 9],   4),
        ([1, 10, 20],  [2, 10, 20],  [1, 10, 20], 0),
        ([1],          [1],          [1],          0),
        ([1, 4, 10],   [2, 15, 20],  [10, 12],    5),
    ]

    # Verify first test: A=[1,4,10], B=[2,15,20], C=[10,12]
    # Three pointers:
    # i=0,j=0,k=0: A[0]=1,B[0]=2,C[0]=10 -> max=10,min=1 -> diff=9. min=A[0]=1 -> i++
    # i=1,j=0,k=0: A[1]=4,B[0]=2,C[0]=10 -> max=10,min=2 -> diff=8. min=B[0]=2 -> j++
    # i=1,j=1,k=0: A[1]=4,B[1]=15,C[0]=10 -> max=15,min=4 -> diff=11. min=A[1]=4 -> i++
    # i=2,j=1,k=0: A[2]=10,B[1]=15,C[0]=10 -> max=15,min=10 -> diff=5. min=A[2]=C[0]=10, adv A -> i++
    # i=3: out of bounds. Result = min(9,8,11,5) = 5. Correct!

    for A, B, C, expected in tests:
        b = brute_force(A, B, C)
        o = optimal(A, B, C)
        r = best(A, B, C)
        status = "PASS" if b == o == expected and r == expected else "FAIL"
        print(f"A={A}  B={B}  C={C}")
        print(f"  Expected={expected}  Brute={b}  Optimal={o}  Best={r}  [{status}]")
        print()
