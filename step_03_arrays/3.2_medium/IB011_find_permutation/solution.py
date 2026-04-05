"""
Problem: Find Permutation
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a string S of length n-1 consisting of 'D' (decrease) and 'I' (increase),
construct the lexicographically smallest permutation of integers [1..n] such that:
  - S[i] == 'I' => perm[i] < perm[i+1]
  - S[i] == 'D' => perm[i] > perm[i+1]

Example: S="I", n=2 -> [1,2]
         S="D", n=2 -> [2,1]
         S="DI", n=3 -> [2,1,3]
         S="ID", n=3 -> [1,3,2]
"""
from typing import List
from itertools import permutations


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n! * n)  |  Space: O(n)
# Generate all permutations of [1..n] in order; return the first valid one.
# Python's itertools.permutations already generates in lexicographic order.
# ============================================================
def brute_force(S: str) -> List[int]:
    """
    Generate all permutations of [1..n] in lexicographic order.
    Return the first one that satisfies all D/I constraints.
    """
    n = len(S) + 1
    for perm in permutations(range(1, n + 1)):
        valid = True
        for i, c in enumerate(S):
            if c == 'I' and perm[i] >= perm[i + 1]:
                valid = False
                break
            if c == 'D' and perm[i] <= perm[i + 1]:
                valid = False
                break
        if valid:
            return list(perm)
    return []


# ============================================================
# APPROACH 2: OPTIMAL (Stack-based)
# Time: O(n)  |  Space: O(n)
# Push numbers onto a stack; pop when 'I' is seen or at the end.
# ============================================================
def optimal(S: str) -> List[int]:
    """
    Stack-based approach:
    - Push numbers 1..n onto a stack one by one.
    - Whenever we see 'I' (or reach the end), pop the entire stack into the result.
    - This naturally produces decreasing runs for consecutive D's, followed by
      the turning point on 'I'.

    Example S="DI":
      Push 1, push 2 (D encountered, keep stacking), see 'I' -> pop 2,1 -> result=[2,1]
      Push 3, end -> pop 3 -> result=[2,1,3]
    """
    n = len(S) + 1
    result = []
    stack = []

    for i in range(1, n + 1):
        stack.append(i)
        # Pop stack when we see 'I' or reach the last element
        if i == n or S[i - 1] == 'I':
            while stack:
                result.append(stack.pop())

    return result


# ============================================================
# APPROACH 3: BEST (Reverse D-segments in [1..n])
# Time: O(n)  |  Space: O(n)
# Start with [1..n] then reverse each maximal D-segment.
# ============================================================
def best(S: str) -> List[int]:
    """
    Observation: start with the identity permutation [1, 2, ..., n].
    For every maximal run of consecutive 'D's at positions [i..j],
    reverse the sub-array [i..j+1] in the result.

    Why this works: reversing a segment of [1..n] makes it descending,
    satisfying all 'D' constraints, while the rest remains ascending for 'I'.
    The identity [1..n] is already the smallest arrangement; we only perturb
    it where necessary.

    Example S="DI":
      result = [1,2,3]
      D-run at index 0 (length 1): reverse result[0..1] -> [2,1,3]
    """
    n = len(S) + 1
    result = list(range(1, n + 1))

    i = 0
    while i < len(S):
        if S[i] == 'D':
            j = i
            while j < len(S) and S[j] == 'D':
                j += 1
            # Reverse result[i..j] (j is inclusive end of the segment)
            result[i:j + 1] = result[i:j + 1][::-1]
            i = j
        else:
            i += 1

    return result


if __name__ == "__main__":
    print("=== Find Permutation ===")
    test_cases = [
        ("I",    [1, 2]),
        ("D",    [2, 1]),
        ("DI",   [2, 1, 3]),
        ("ID",   [1, 3, 2]),
        ("DDIID", [3, 2, 1, 4, 6, 5]),
        ("IDID", [1, 3, 2, 5, 4]),
        ("",     [1]),
    ]
    for S, expected in test_cases:
        b  = brute_force(S)
        o  = optimal(S)
        be = best(S)
        status = "OK" if b == o == be == expected else "FAIL"
        print(f'S="{S}" | Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}')
