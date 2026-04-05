"""
Problem: Smallest Sequence with Given Primes
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given three prime numbers A, B, C and an integer N, find the first N numbers
whose only prime factors are A, B, and C (in sorted order).
These are called "ugly numbers" generalized to arbitrary primes.

Example: A=2, B=3, C=5, N=5 => [2, 3, 4, 5, 6]
Note: The sequence starts from the primes themselves (1 is excluded).
"""
from typing import List
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(N * log(max_val))  |  Space: O(N)
# For each candidate number, check if its only prime factors are A, B, C.
# ============================================================
def brute_force(A: int, B: int, C: int, N: int) -> List[int]:
    """
    Iterate numbers starting from 2. For each, divide out A, B, C
    repeatedly. If the remainder is 1, the number qualifies.
    Collect the first N such numbers.
    This is very slow when A, B, C are large primes.
    """
    def is_valid(num: int) -> bool:
        for p in (A, B, C):
            while num % p == 0:
                num //= p
        return num == 1

    result = []
    candidate = 1
    while len(result) < N:
        candidate += 1
        if is_valid(candidate):
            result.append(candidate)
    return result


# ============================================================
# APPROACH 2: OPTIMAL — Three-Pointer Merge (Ugly Numbers variant)
# Time: O(N)  |  Space: O(N)
# Maintain three pointers into the result array, each tracking
# which element to multiply next by A, B, or C respectively.
# ============================================================
def optimal(A: int, B: int, C: int, N: int) -> List[int]:
    """
    Classic O(N) three-pointer approach (like Ugly Number II):
    - result[0] = min(A, B, C)
    - Three pointers pa, pb, pc each start at 0.
    - next_a = result[pa] * A, next_b = result[pb] * B, next_c = result[pc] * C
    - Pick minimum, append, advance pointer(s) that produced minimum.
    - Handles duplicates (if two pointers produce same value, advance both).

    Key insight: We seed with an implicit "1" outside the array:
    next_a starts as A (= 1 * A), next_b as B, next_c as C.
    After choosing a value, the pointer advances and generates its next candidate.
    """
    result = [0] * N
    pa = pb = pc = 0

    next_a = A
    next_b = B
    next_c = C

    for i in range(N):
        val = min(next_a, next_b, next_c)
        result[i] = val
        # Advance all pointers that produced this minimum (to skip duplicates)
        if val == next_a:
            next_a = result[pa] * A
            pa += 1
        if val == next_b:
            next_b = result[pb] * B
            pb += 1
        if val == next_c:
            next_c = result[pc] * C
            pc += 1

    return result


# ============================================================
# APPROACH 3: BEST — Min-Heap with Visited Set
# Time: O(N log N)  |  Space: O(N)
# Use a min-heap to always extract the smallest unseen multiple.
# Handles all edge cases cleanly (duplicate primes, same values).
# ============================================================
def best(A: int, B: int, C: int, N: int) -> List[int]:
    """
    Push the three primes into a min-heap. Each time we pop the
    minimum value x, push x*A, x*B, x*C into the heap if not seen.
    The visited set prevents duplicate entries.
    """
    heap = []
    seen = set()

    for p in (A, B, C):
        if p not in seen:
            heapq.heappush(heap, p)
            seen.add(p)

    result = []
    while len(result) < N:
        val = heapq.heappop(heap)
        result.append(val)
        for p in (A, B, C):
            nxt = val * p
            if nxt not in seen:
                seen.add(nxt)
                heapq.heappush(heap, nxt)

    return result


if __name__ == "__main__":
    print("=== Smallest Sequence with Given Primes ===")

    test_cases = [
        (2, 3, 5, 5,  [2, 3, 4, 5, 6]),
        (2, 3, 5, 10, [2, 3, 4, 5, 6, 8, 9, 10, 12, 15]),
        (2, 3, 7, 5,  [2, 3, 4, 6, 7]),
        (3, 5, 7, 4,  [3, 5, 7, 9]),
        (2, 2, 2, 5,  [2, 4, 8, 16, 32]),  # all same prime
    ]

    for A, B, C, N, expected in test_cases:
        b = brute_force(A, B, C, N)
        o = optimal(A, B, C, N)
        bt = best(A, B, C, N)
        ok = "OK" if o == bt == expected else f"FAIL"
        print(f"  A={A},B={B},C={C},N={N}")
        print(f"    Expected={expected}")
        print(f"    Brute   ={b}")
        print(f"    Optimal ={o}")
        print(f"    Best    ={bt}  [{ok}]")
