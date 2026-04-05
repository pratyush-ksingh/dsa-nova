"""
Problem: Find Repeating and Missing Number
Difficulty: HARD | XP: 50

Given an array of n integers in range [1, n] where one number appears
twice (repeating) and one is missing, find both.
Return (repeating, missing).
"""
from typing import List, Tuple


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# For each value 1..n, count its occurrences.
# ============================================================
def brute_force(A: List[int]) -> Tuple[int, int]:
    n = len(A)
    repeating, missing = -1, -1
    for val in range(1, n + 1):
        count = A.count(val)
        if count == 2:
            repeating = val
        elif count == 0:
            missing = val
    return repeating, missing


# ============================================================
# APPROACH 2: OPTIMAL — Math (Sum + Sum of Squares)
# Time: O(n)  |  Space: O(1)
# Let r = repeating, m = missing.
#   actual_sum - expected_sum = r - m           (1)
#   actual_sum2 - expected_sum2 = r^2 - m^2     (2)
#   divide (2) by (1): r + m = diff2 / diff     (3)
#   solve: r = (diff + (r+m)) / 2, m = r - diff
# ============================================================
def optimal(A: List[int]) -> Tuple[int, int]:
    n = len(A)
    actual_sum = sum(A)
    actual_sum2 = sum(x * x for x in A)

    expected_sum = n * (n + 1) // 2
    expected_sum2 = n * (n + 1) * (2 * n + 1) // 6

    diff = actual_sum - expected_sum        # r - m
    diff2 = actual_sum2 - expected_sum2    # r^2 - m^2

    r_plus_m = diff2 // diff               # r + m
    r = (diff + r_plus_m) // 2
    m = r_plus_m - r
    return r, m


# ============================================================
# APPROACH 3: BEST — XOR Approach
# Time: O(n)  |  Space: O(1)
# XOR all elements with 1..n to get r XOR m.
# Use a set bit to partition into two groups; XOR each group
# separately to isolate r and m, then verify which is repeating.
# ============================================================
def best(A: List[int]) -> Tuple[int, int]:
    n = len(A)
    xor_all = 0
    for x in A:
        xor_all ^= x
    for i in range(1, n + 1):
        xor_all ^= i
    # xor_all = r XOR m

    set_bit = xor_all & (-xor_all)  # rightmost set bit

    x, y = 0, 0
    for val in A:
        if val & set_bit:
            x ^= val
        else:
            y ^= val
    for i in range(1, n + 1):
        if i & set_bit:
            x ^= i
        else:
            y ^= i

    # Verify which is repeating
    if A.count(x) == 2:
        return x, y
    return y, x


if __name__ == "__main__":
    print("=== Find Repeating and Missing Number ===")
    tests = [
        ([3, 1, 2, 5, 3],     (3, 4)),
        ([1, 1],               (1, 2)),
        ([2, 2],               (2, 1)),
        ([4, 3, 6, 2, 1, 1],  (1, 5)),
    ]
    for A, expected in tests:
        bf = brute_force(A[:])
        op = optimal(A[:])
        be = best(A[:])
        ok = all(x == expected for x in [bf, op, be])
        print(f"A={A} -> Brute={bf}, Optimal={op}, Best={be} | Expected={expected} {'OK' if ok else 'FAIL'}")
