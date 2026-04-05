"""Problem: Rearrange Array
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Rearrange an array so that A[i] = A[A[i]] for all i,
without using extra space. Values are in range [0, n-1].
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Use a copy of the original array
# ============================================================
def brute_force(A: List[int]) -> List[int]:
    n = len(A)
    original = A[:]
    for i in range(n):
        A[i] = original[original[i]]
    return A


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(1)
# Encode A[i] = A[i] + n * (A[A[i]] % n)
# A[A[i]] % n gives original value at index A[i]
# Decode with integer division by n
# ============================================================
def optimal(A: List[int]) -> List[int]:
    n = len(A)
    for i in range(n):
        A[i] += n * (A[A[i]] % n)
    for i in range(n):
        A[i] //= n
    return A


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1)
# Same encoding with explicit modulo for clarity
# ============================================================
def best(A: List[int]) -> List[int]:
    n = len(A)
    # Phase 1: encode new value into upper part
    for i in range(n):
        old_ai = A[i] % n          # original A[i]
        new_val = A[old_ai] % n    # original A[A[i]]
        A[i] += n * new_val
    # Phase 2: decode
    for i in range(n):
        A[i] //= n
    return A


if __name__ == "__main__":
    tests = [
        ([3, 2, 0, 1], [1, 0, 3, 2]),
        ([4, 0, 2, 1, 3], [3, 4, 2, 0, 1]),
        ([0], [0]),
    ]
    print("=== Rearrange Array A[i] = A[A[i]] ===")
    for inp, exp in tests:
        b = brute_force(inp[:])
        o = optimal(inp[:])
        bst = best(inp[:])
        ok = all(x == exp for x in [b, o, bst])
        print(f"Input={inp}  Brute={b}  Optimal={o}  Best={bst}  Expected={exp}  {'OK' if ok else 'MISMATCH'}")
