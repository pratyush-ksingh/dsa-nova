"""Problem: Divide Two Integers without Operators
Difficulty: MEDIUM | XP: 25"""

import math

INT_MAX =  2**31 - 1
INT_MIN = -2**31

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(dividend/divisor)  |  Space: O(1)
# Repeated subtraction. Very slow for large inputs.
# ============================================================
def brute_force(dividend: int, divisor: int) -> int:
    if divisor == 0:
        raise ZeroDivisionError
    if dividend == INT_MIN and divisor == -1:
        return INT_MAX

    negative = (dividend < 0) ^ (divisor < 0)
    a, b = abs(dividend), abs(divisor)
    count = 0
    while a >= b:
        a -= b
        count += 1

    result = -count if negative else count
    return max(INT_MIN, min(INT_MAX, result))


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(log^2 n)  |  Space: O(1)
# Bit doubling: keep doubling divisor until it exceeds remainder,
# then subtract and repeat.
# ============================================================
def optimal(dividend: int, divisor: int) -> int:
    if divisor == 0:
        raise ZeroDivisionError
    if dividend == INT_MIN and divisor == -1:
        return INT_MAX

    negative = (dividend < 0) ^ (divisor < 0)
    a, b = abs(dividend), abs(divisor)
    result = 0

    while a >= b:
        temp, multiple = b, 1
        while a >= (temp << 1):
            temp <<= 1
            multiple <<= 1
        a -= temp
        result += multiple

    result = -result if negative else result
    return max(INT_MIN, min(INT_MAX, result))


# ============================================================
# APPROACH 3: BEST
# Time: O(log n)  |  Space: O(1)
# Iterate bits from high to low; build quotient bit by bit.
# Bounded to 32 iterations — cleanest bit-manipulation solution.
# ============================================================
def best(dividend: int, divisor: int) -> int:
    if divisor == 0:
        raise ZeroDivisionError
    if dividend == INT_MIN and divisor == -1:
        return INT_MAX

    negative = (dividend < 0) ^ (divisor < 0)
    a, b = abs(dividend), abs(divisor)
    result = 0

    for i in range(31, -1, -1):
        if (a >> i) >= b:
            result += (1 << i)
            a -= b << i

    result = -result if negative else result
    return max(INT_MIN, min(INT_MAX, result))


if __name__ == "__main__":
    # (dividend, divisor, expected, run_brute)
    # Brute force skipped for large inputs (O(dividend) subtraction loop)
    tests = [
        (10,       3,   3,        True),
        (7,       -3,  -2,        True),
        (0,        1,   0,        True),
        (1,        1,   1,        True),
        (-1,       1,  -1,        True),
        (INT_MIN, -1,  INT_MAX,   False),  # brute skipped: overflow edge case only
        (INT_MIN,  1,  INT_MIN,   False),  # brute skipped: ~2 billion iterations
        (100,     10,  10,        True),
    ]
    print("=== Divide Two Integers (No *, /, %) ===")
    for dividend, divisor, expected, run_brute in tests:
        b  = brute_force(dividend, divisor) if run_brute else "skipped"
        o  = optimal(dividend, divisor)
        be = best(dividend, divisor)
        if run_brute:
            status = "PASS" if b == o == be == expected else "FAIL"
        else:
            status = "PASS" if o == be == expected else "FAIL"
        print(f"{dividend:14} / {divisor:5} | Brute: {str(b):12} | Optimal: {o:12} | Best: {be:12} | Expected: {expected:12} | {status}")
