"""
Problem: Generate All Subsequences
Difficulty: MEDIUM | XP: 25

Given an array of n elements, generate all 2^n subsequences.
A subsequence is any subset of elements in their original order.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative Bit Masking
# Time: O(n * 2^n)  |  Space: O(n * 2^n)
#
# Each integer from 0 to 2^n - 1 encodes a subsequence.
# Bit j set => include arr[j].
# ============================================================
def brute_force(arr: List[int]) -> List[List[int]]:
    n = len(arr)
    total = 1 << n  # 2^n
    result = []

    for mask in range(total):
        subseq = []
        for j in range(n):
            if mask & (1 << j):
                subseq.append(arr[j])
        result.append(subseq)

    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Include/Exclude (Backtracking)
# Time: O(n * 2^n)  |  Space: O(n) recursion stack
#
# At each index, include the element or exclude it.
# When we reach the end, add a copy of current to results.
# Backtrack by popping the last element after the include call.
# ============================================================
def optimal(arr: List[int]) -> List[List[int]]:
    result = []

    def generate(i: int, current: List[int]) -> None:
        # Base case: processed all elements
        if i == len(arr):
            result.append(current[:])  # COPY current
            return

        # Include arr[i]
        current.append(arr[i])
        generate(i + 1, current)
        current.pop()  # backtrack

        # Exclude arr[i]
        generate(i + 1, current)

    generate(0, [])
    return result


# ============================================================
# APPROACH 3: BEST -- Cascading (Iterative Build-Up)
# Time: O(n * 2^n)  |  Space: O(n * 2^n)
#
# Start with [[]]. For each element, duplicate all existing
# subsequences with the new element appended.
# No recursion, very clean and Pythonic.
# ============================================================
def best(arr: List[int]) -> List[List[int]]:
    result = [[]]  # start with the empty subsequence

    for x in arr:
        # For each existing subsequence, create a new one with x appended
        result += [subseq + [x] for subseq in result]

    return result


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Generate All Subsequences ===\n")

    tests = [
        [1, 2, 3],
        [1, 2],
        [5],
        [],
    ]

    for arr in tests:
        expected_count = 1 << len(arr)
        print(f"Input: {arr}")
        print(f"Expected count: {expected_count}")

        b = brute_force(arr)
        o = optimal(arr)
        be = best(arr)

        print(f"Brute count:   {len(b)}  {'PASS' if len(b) == expected_count else 'FAIL'}")
        print(f"Optimal count: {len(o)}  {'PASS' if len(o) == expected_count else 'FAIL'}")
        print(f"Best count:    {len(be)}  {'PASS' if len(be) == expected_count else 'FAIL'}")

        # Print all subsequences for small inputs
        if len(arr) <= 3:
            print(f"Brute:   {b}")
            print(f"Optimal: {o}")
            print(f"Best:    {be}")
        print()

    # String example
    print("--- String Example ---")
    s = "abc"
    chars = list(s)
    subseqs = best(chars)
    string_subseqs = [''.join(sub) for sub in subseqs]
    print(f'Input: "{s}"')
    print(f"Subsequences: {string_subseqs}")
    print(f"Count: {len(string_subseqs)} (expected {1 << len(s)})")
