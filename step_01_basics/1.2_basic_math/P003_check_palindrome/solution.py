"""
Problem: Check Palindrome (LeetCode #9)
Difficulty: EASY | XP: 10

Given an integer x, return True if x is a palindrome.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- String Conversion
# Time: O(D)  |  Space: O(D) where D = number of digits
# Convert to string, compare with its reverse.
# ============================================================
def brute_force(x: int) -> bool:
    if x < 0:
        return False
    s = str(x)
    return s == s[::-1]


# ============================================================
# APPROACH 2: OPTIMAL -- Full Number Reversal (Math)
# Time: O(D)  |  Space: O(1)
# Reverse entire number using modulo, compare with original.
# ============================================================
def optimal(x: int) -> bool:
    if x < 0:
        return False

    original = x
    reversed_num = 0

    while x > 0:
        reversed_num = reversed_num * 10 + x % 10
        x //= 10

    return reversed_num == original


# ============================================================
# APPROACH 3: BEST -- Half Reversal (Math, Overflow-Safe)
# Time: O(D/2) = O(D)  |  Space: O(1)
# Reverse only the second half. Compare halves.
# ============================================================
def best(x: int) -> bool:
    # Negative or trailing zero (except 0 itself)
    if x < 0 or (x % 10 == 0 and x != 0):
        return False

    reversed_half = 0
    while x > reversed_half:
        reversed_half = reversed_half * 10 + x % 10
        x //= 10

    # Even length: x == reversed_half
    # Odd length: x == reversed_half // 10 (discard middle digit)
    return x == reversed_half or x == reversed_half // 10


if __name__ == "__main__":
    test_cases = [121, -121, 10, 0, 1234321, 1221, 12345, 1, 11]

    print("=== Check Palindrome ===")
    for x in test_cases:
        print(f"\nx = {x}")
        print(f"  Brute Force: {brute_force(x)}")
        print(f"  Optimal:     {optimal(x)}")
        print(f"  Best:        {best(x)}")
