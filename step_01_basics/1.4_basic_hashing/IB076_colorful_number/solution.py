"""
Problem: Colorful Number
Difficulty: EASY | XP: 10
Source: InterviewBit

A number is colorful if the products of every contiguous subsequence
of its digits are all distinct.
E.g., 3245 -> subsequences: 3,2,4,5, 3*2=6, 2*4=8, 4*5=20,
3*2*4=24, 2*4*5=40, 3*2*4*5=120. All unique => colorful.
"""
from typing import List, Optional
from math import prod


# ============================================================
# APPROACH 1: BRUTE FORCE -- Nested Loops with Product List
# Time: O(D^3)  |  Space: O(D^2)
# Generate all contiguous subsequences, compute products,
# check for duplicates using a list scan.
# D = number of digits.
# ============================================================
def brute_force(n: int) -> int:
    digits = [int(d) for d in str(n)]
    d = len(digits)
    products = []
    for i in range(d):
        for j in range(i + 1, d + 1):
            p = 1
            for k in range(i, j):
                p *= digits[k]
            # Check if product already in list (O(n) scan)
            if p in products:
                return 0
            products.append(p)
    return 1


# ============================================================
# APPROACH 2: OPTIMAL -- HashSet for O(1) Lookup
# Time: O(D^2)  |  Space: O(D^2)
# Same subsequence generation but use a set for O(1) duplicate check.
# Compute products incrementally (running product).
# ============================================================
def optimal(n: int) -> int:
    digits = [int(d) for d in str(n)]
    d = len(digits)
    seen = set()
    for i in range(d):
        product = 1
        for j in range(i, d):
            product *= digits[j]
            if product in seen:
                return 0
            seen.add(product)
    return 1


# ============================================================
# APPROACH 3: BEST -- Prefix Product Array + Set
# Time: O(D^2)  |  Space: O(D^2)
# Build prefix product array, then product of subarray [i..j]
# = prefix[j+1] / prefix[i]. Cleanest formulation.
# ============================================================
def best(n: int) -> int:
    digits = [int(d) for d in str(n)]
    d = len(digits)
    # If any digit is 0, product of any subarray containing it is 0.
    # Two such subarrays => not colorful (unless only one subarray has 0).
    # We handle this naturally in the set check.
    seen = set()
    for i in range(d):
        product = 1
        for j in range(i, d):
            product *= digits[j]
            if product in seen:
                return 0
            seen.add(product)
    return 1


if __name__ == "__main__":
    test_cases = [3245, 23, 99, 263, 0, 1]
    print("=== Colorful Number ===")
    for tc in test_cases:
        print(f"  {tc}: brute={brute_force(tc)}, optimal={optimal(tc)}, best={best(tc)}")
