"""
Problem: Equal
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Find all unique quadruplets (A, B, C, D) — indices — such that:
  arr[A] + arr[B] = arr[C] + arr[D]
  All four indices are distinct, A < B, C < D, (A,B) lexicographically < (C,D).
Return sorted list of such quadruplets.
"""
from typing import List
from collections import defaultdict


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^4)  |  Space: O(1) ignoring output
# ============================================================
def brute_force(A: List[int]) -> List[List[int]]:
    """
    Try all combinations of 4 distinct indices, check the sum condition.
    Real-life: Exhaustive search for balance in financial transaction pairing.
    """
    n = len(A)
    result_set = set()
    for a in range(n):
        for b in range(a + 1, n):
            for c in range(n):
                for d in range(c + 1, n):
                    if len({a, b, c, d}) < 4:
                        continue
                    if A[a] + A[b] == A[c] + A[d]:
                        quad = tuple(sorted([[a, b], [c, d]])[0] + sorted([[a, b], [c, d]])[1])
                        # canonical: smaller pair first
                        p1 = (min(a, b), max(a, b))
                        p2 = (min(c, d), max(c, d))
                        if p1 > p2:
                            p1, p2 = p2, p1
                        result_set.add((p1[0], p1[1], p2[0], p2[1]))
    return sorted([list(q) for q in result_set])


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def optimal(A: List[int]) -> List[List[int]]:
    """
    Hash pairs: sum -> list of (i,j) pairs. For each bucket with >=2 pairs,
    emit valid quadruplets (all 4 indices distinct).
    Real-life: Matching financial trades with equal net value using hash maps.
    """
    n = len(A)
    sum_map: dict = defaultdict(list)
    for i in range(n):
        for j in range(i + 1, n):
            sum_map[A[i] + A[j]].append((i, j))

    seen = set()
    result = []
    for pairs in sum_map.values():
        for p in range(len(pairs)):
            for q in range(p + 1, len(pairs)):
                a, b = pairs[p]
                c, d = pairs[q]
                if len({a, b, c, d}) < 4:
                    continue
                p1, p2 = (a, b), (c, d)
                if p1 > p2:
                    p1, p2 = p2, p1
                key = (p1[0], p1[1], p2[0], p2[1])
                if key not in seen:
                    seen.add(key)
                    result.append(list(key))
    return sorted(result)


# ============================================================
# APPROACH 3: BEST
# Time: O(n^2)  |  Space: O(n^2)
# ============================================================
def best(A: List[int]) -> List[List[int]]:
    """
    Single-pass: keep only the FIRST pair seen for each sum value.
    Emit result immediately when a second valid pair is found.
    Avoids storing all pairs per bucket.
    Real-life: Stream-based duplicate-sum detection in real-time trading systems.
    """
    n = len(A)
    first_pair: dict = {}
    seen = set()
    result = []
    for i in range(n):
        for j in range(i + 1, n):
            s = A[i] + A[j]
            if s in first_pair:
                a, b = first_pair[s]
                c, d = i, j
                if len({a, b, c, d}) == 4:
                    p1, p2 = (a, b), (c, d)
                    if p1 > p2:
                        p1, p2 = p2, p1
                    key = (p1[0], p1[1], p2[0], p2[1])
                    if key not in seen:
                        seen.add(key)
                        result.append(list(key))
            else:
                first_pair[s] = (i, j)
    return sorted(result)


if __name__ == "__main__":
    print("=== Equal ===")
    A1 = [3, 4, 7, 1, 2, 9, 8]
    print(f"\nInput: {A1}")
    print(f"Brute:   {brute_force(A1)}")
    print(f"Optimal: {optimal(A1)}")
    print(f"Best:    {best(A1)}")

    A2 = [1, 1, 1, 1]
    print(f"\nInput: {A2}")
    print(f"Brute:   {brute_force(A2)}")
    print(f"Optimal: {optimal(A2)}")
    print(f"Best:    {best(A2)}")
