"""
Problem: Celebrity Problem
Difficulty: MEDIUM | XP: 25

In a party of n people, find the celebrity -- a person known by everyone but who
knows nobody. Given a knows(a, b) function that returns True if a knows b.
Return the celebrity's index, or -1 if no celebrity exists.
"""
from typing import List


# Helper: simulates the knows(a, b) function using an adjacency matrix
def create_knows(matrix: List[List[int]]):
    def knows(a: int, b: int) -> bool:
        return matrix[a][b] == 1
    return knows


# ============================================================
# APPROACH 1: BRUTE FORCE (Check All Pairs)
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def brute_force(n: int, knows) -> int:
    """For each person, check if they know nobody and everyone knows them."""
    for candidate in range(n):
        # Check if candidate knows anyone
        knows_someone = False
        for j in range(n):
            if candidate != j and knows(candidate, j):
                knows_someone = True
                break

        if knows_someone:
            continue

        # Check if everyone knows the candidate
        known_by_all = True
        for j in range(n):
            if candidate != j and not knows(j, candidate):
                known_by_all = False
                break

        if known_by_all:
            return candidate

    return -1


# ============================================================
# APPROACH 2: OPTIMAL (Stack Elimination)
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(n: int, knows) -> int:
    """Push all to stack. Pop two at a time, eliminate one. Verify final candidate."""
    stack = list(range(n))

    # Elimination phase: reduce to one candidate
    while len(stack) > 1:
        a = stack.pop()
        b = stack.pop()
        if knows(a, b):
            # a knows b -> a is NOT the celebrity, keep b
            stack.append(b)
        else:
            # a does NOT know b -> b is NOT the celebrity, keep a
            stack.append(a)

    candidate = stack[0]

    # Verification phase: confirm the candidate
    for i in range(n):
        if i != candidate:
            if knows(candidate, i) or not knows(i, candidate):
                return -1

    return candidate


# ============================================================
# APPROACH 3: BEST (Two-Pointer / Single Candidate Elimination)
# Time: O(n)  |  Space: O(1)
# ============================================================
def best(n: int, knows) -> int:
    """Single pass to find candidate, then verify. O(1) extra space."""
    # Find candidate by elimination
    candidate = 0
    for i in range(1, n):
        if knows(candidate, i):
            # candidate knows i -> candidate is NOT celebrity, try i
            candidate = i

    # Verify the candidate
    for i in range(n):
        if i != candidate:
            if knows(candidate, i) or not knows(i, candidate):
                return -1

    return candidate


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Celebrity Problem ===\n")

    # Test 1: Person 1 is the celebrity
    matrix1 = [
        [0, 1, 0],
        [0, 0, 0],
        [0, 1, 0],
    ]
    knows1 = create_knows(matrix1)

    # Test 2: No celebrity
    matrix2 = [
        [0, 1, 0],
        [0, 0, 1],
        [1, 0, 0],
    ]
    knows2 = create_knows(matrix2)

    # Test 3: Person 2 is the celebrity (4 people)
    matrix3 = [
        [0, 0, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 0, 0],
        [0, 0, 1, 0],
    ]
    knows3 = create_knows(matrix3)

    # Test 4: Single person (is the celebrity)
    matrix4 = [[0]]
    knows4 = create_knows(matrix4)

    tests = [
        (3, knows1, 1),
        (3, knows2, -1),
        (4, knows3, 2),
        (1, knows4, 0),
    ]

    for n, knows_fn, expected in tests:
        b = brute_force(n, knows_fn)
        o = optimal(n, knows_fn)
        r = best(n, knows_fn)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"n = {n}, expected = {expected}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {r}")
        print(f"  [{status}]\n")
