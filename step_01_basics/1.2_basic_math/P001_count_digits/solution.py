"""
Problem: Count Digits
Difficulty: EASY | XP: 10

Count the number of digits in a given integer N.
"""
import math


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative Division
# Time: O(d) where d = number of digits  |  Space: O(1)
# Repeatedly divide by 10 until 0, counting iterations.
# ============================================================
def brute_force(n: int) -> int:
    if n == 0:
        return 1

    num = abs(n)
    count = 0
    while num > 0:
        count += 1
        num //= 10
    return count


# ============================================================
# APPROACH 2: OPTIMAL -- Logarithm Formula
# Time: O(1)  |  Space: O(1)
# digits = floor(log10(|N|)) + 1
# ============================================================
def optimal(n: int) -> int:
    if n == 0:
        return 1

    return int(math.log10(abs(n))) + 1


# ============================================================
# APPROACH 3: BEST -- String Conversion
# Time: O(d)  |  Space: O(d)
# Convert to string, return length. Simple and readable.
# ============================================================
def best(n: int) -> int:
    return len(str(abs(n)))


if __name__ == "__main__":
    test_cases = [12345, 0, -987, 1000000, 2147483647, -2147483648]
    print("=== Count Digits ===")

    for n in test_cases:
        print(f"N = {n}")
        print(f"  Brute Force: {brute_force(n)}")
        print(f"  Optimal:     {optimal(n)}")
        print(f"  Best:        {best(n)}")
