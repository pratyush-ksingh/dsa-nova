"""
Problem: Reverse a Number (LeetCode #7)
Difficulty: EASY | XP: 10

Reverse digits of a 32-bit signed integer. Return 0 on overflow.
"""

INT_MAX = 2**31 - 1  # 2147483647
INT_MIN = -(2**31)   # -2147483648


# ============================================================
# APPROACH 1: BRUTE FORCE -- String Reversal
# Time: O(d)  |  Space: O(d)
# Convert to string, reverse, convert back. Check overflow.
# ============================================================
def brute_force(x: int) -> int:
    if x == 0:
        return 0

    negative = x < 0
    s = str(abs(x))[::-1]
    result = int(s)

    if negative:
        result = -result

    # Overflow check for 32-bit range
    if result > INT_MAX or result < INT_MIN:
        return 0
    return result


# ============================================================
# APPROACH 2: OPTIMAL -- Mathematical Digit-by-Digit
# Time: O(d)  |  Space: O(1)
# Pop last digit with %10, push onto rev with *10.
# Check overflow BEFORE the multiply.
# ============================================================
def optimal(x: int) -> int:
    rev = 0
    sign = 1 if x >= 0 else -1
    x = abs(x)

    while x > 0:
        digit = x % 10
        x //= 10

        # Overflow check before rev = rev * 10 + digit
        if rev > (INT_MAX - digit) // 10:
            return 0

        rev = rev * 10 + digit

    result = sign * rev
    if result > INT_MAX or result < INT_MIN:
        return 0
    return result


# ============================================================
# APPROACH 3: BEST -- Same as Optimal (Pythonic)
# Time: O(d)  |  Space: O(1)
# Python has arbitrary precision ints, so overflow is only a
# constraint check at the end (simulating 32-bit behavior).
# ============================================================
def best(x: int) -> int:
    sign = 1 if x >= 0 else -1
    rev = int(str(abs(x))[::-1])
    result = sign * rev

    if result < INT_MIN or result > INT_MAX:
        return 0
    return result


if __name__ == "__main__":
    test_cases = [123, -456, 120, 0, 1534236469, -2147483648]
    print("=== Reverse a Number ===")

    for x in test_cases:
        print(f"x = {x}")
        print(f"  Brute Force: {brute_force(x)}")
        print(f"  Optimal:     {optimal(x)}")
        print(f"  Best:        {best(x)}")
