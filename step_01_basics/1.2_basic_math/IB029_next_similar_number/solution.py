"""Problem: Next Similar Number
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given a number as a string, find the next higher number using
the same digits (essentially the next permutation of its digits).
Return -1 if no higher permutation exists.
"""
from typing import List
from itertools import permutations

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n! * n log n)  |  Space: O(n!)
# Generate all permutations, sort, return next one
# ============================================================
def brute_force(num: str) -> str:
    perms = sorted(set(permutations(num)))
    # filter leading zeros
    valid = [''.join(p) for p in perms if p[0] != '0']
    try:
        idx = valid.index(num)
        return valid[idx + 1] if idx + 1 < len(valid) else "-1"
    except ValueError:
        return "-1"


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n log n)  |  Space: O(n)
# Find pivot, swap with smallest larger digit, sort suffix
# ============================================================
def optimal(num: str) -> str:
    d = list(num)
    n = len(d)
    pivot = -1
    for i in range(n - 2, -1, -1):
        if d[i] < d[i + 1]:
            pivot = i
            break
    if pivot == -1:
        return "-1"
    # find smallest d[j] > d[pivot] in suffix
    best_j = pivot + 1
    for j in range(pivot + 2, n):
        if d[pivot] < d[j] <= d[best_j]:
            best_j = j
    d[pivot], d[best_j] = d[best_j], d[pivot]
    d[pivot + 1:] = sorted(d[pivot + 1:])
    return ''.join(d)


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(1) extra (in-place on list)
# Suffix after pivot is descending -> reverse it in O(n)
# ============================================================
def best(num: str) -> str:
    d = list(num)
    n = len(d)
    # 1. Find rightmost i where d[i] < d[i+1]
    i = n - 2
    while i >= 0 and d[i] >= d[i + 1]:
        i -= 1
    if i < 0:
        return "-1"
    # 2. Find rightmost j > i where d[j] > d[i]
    j = n - 1
    while d[j] <= d[i]:
        j -= 1
    # 3. Swap
    d[i], d[j] = d[j], d[i]
    # 4. Reverse suffix
    d[i + 1:] = d[i + 1:][::-1]
    return ''.join(d)


if __name__ == "__main__":
    tests = ["218765", "1234", "4321", "534976", "999", "11"]
    expected = ["251678", "1243", "-1", "536479", "-1", "-1"]
    print("=== Next Similar Number ===")
    print(f"{'Input':<12} {'Brute':<12} {'Optimal':<12} {'Best':<12} {'Expected':<12}")
    for num, exp in zip(tests, expected):
        b = brute_force(num)
        o = optimal(num)
        bst = best(num)
        ok = "OK" if b == o == bst == exp else "MISMATCH"
        print(f"{num:<12} {b:<12} {o:<12} {bst:<12} {exp:<12} {ok}")
