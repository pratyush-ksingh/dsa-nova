"""
Problem: An Increment Problem
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given an integer array A, process elements left to right:
- If A[i] has NOT been seen before, place it in the output.
- If A[i] HAS been seen, increment the FIRST occurrence's value by 1
  and remap (chain if the new value also collides).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(n)
# For each element, linearly scan the output so far for a match
# and increment the first found occurrence.
# ============================================================
def brute_force(A: List[int]) -> List[int]:
    n = len(A)
    result = [0] * n
    placed = [False] * n

    for i in range(n):
        found = False
        for j in range(i):
            if placed[j] and result[j] == A[i]:
                result[j] += 1
                found = True
                break
        if not found:
            result[i] = A[i]
            placed[i] = True

    return result


# ============================================================
# APPROACH 2: OPTIMAL — HashMap single-level tracking
# Time: O(n)  |  Space: O(n)
# Map value -> index of first occurrence in result[].
# On collision: increment result[idx], update mapping.
# ============================================================
def optimal(A: List[int]) -> List[int]:
    n = len(A)
    result = [0] * n
    first_idx = {}  # value -> first occurrence index

    for i in range(n):
        val = A[i]
        if val not in first_idx:
            result[i] = val
            first_idx[val] = i
        else:
            idx = first_idx.pop(val)
            result[idx] += 1
            new_val = result[idx]
            first_idx[new_val] = idx
            # position i stays 0

    return result


# ============================================================
# APPROACH 3: BEST — HashMap with cascading chain resolution
# Time: O(n) amortized  |  Space: O(n)
# Same as Optimal but handles cascading collisions: when we
# increment result[idx] to new_val, if new_val is also in the
# map, we chain-increment that slot too.
# ============================================================
def best(A: List[int]) -> List[int]:
    n = len(A)
    result = [0] * n
    first_idx = {}  # value -> first occurrence index

    for i in range(n):
        val = A[i]
        if val not in first_idx:
            result[i] = val
            first_idx[val] = i
        else:
            idx = first_idx.pop(val)
            result[idx] += 1
            new_val = result[idx]

            # Chain-resolve cascading collisions
            while new_val in first_idx:
                next_idx = first_idx.pop(new_val)
                result[next_idx] += 1
                new_val = result[next_idx]
                first_idx[result[next_idx]] = next_idx

            first_idx[new_val] = idx

    return result


if __name__ == "__main__":
    print("=== An Increment Problem ===")
    tests = [
        [1, 2, 1, 2, 1],   # re-appears
        [3, 3, 3],          # cascading: 3->4->5
        [1],                # single
        [1, 1, 1, 1],       # all same
        [5, 5, 5, 5, 5],    # all same
    ]
    for t in tests:
        print(f"Input:   {t}")
        print(f"Brute:   {brute_force(t[:])}")
        print(f"Optimal: {optimal(t[:])}")
        print(f"Best:    {best(t[:])}")
        print()
