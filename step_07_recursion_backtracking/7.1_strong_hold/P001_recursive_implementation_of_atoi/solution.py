"""
Problem: Recursive Implementation of atoi (LeetCode #8)
Difficulty: MEDIUM | XP: 25

Convert a string to a 32-bit signed integer. Handle whitespace,
optional sign, digits, overflow clamping. Implement recursively.
"""

INT_MAX = 2**31 - 1   # 2147483647
INT_MIN = -(2**31)     # -2147483648


# ============================================================
# APPROACH 1: BRUTE FORCE -- Iterative
# Time: O(n)  |  Space: O(1)
#
# Standard iterative atoi: skip whitespace, read sign, read
# digits with overflow checking.
# ============================================================
def brute_force(s: str) -> int:
    if not s:
        return 0

    i, n = 0, len(s)

    # Skip whitespace
    while i < n and s[i] == ' ':
        i += 1
    if i == n:
        return 0

    # Read sign
    sign = 1
    if s[i] in ('+', '-'):
        sign = -1 if s[i] == '-' else 1
        i += 1

    # Read digits with overflow check
    result = 0
    while i < n and s[i].isdigit():
        digit = int(s[i])

        # Overflow check BEFORE multiplication
        if result > INT_MAX // 10 or (result == INT_MAX // 10 and digit > 7):
            return INT_MAX if sign == 1 else INT_MIN

        result = result * 10 + digit
        i += 1

    return sign * result


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Digit Processing
# Time: O(n)  |  Space: O(d) where d = number of digits
#
# Preprocess whitespace and sign iteratively, then recurse
# over the digit portion. Each call processes one digit.
# ============================================================
def optimal(s: str) -> int:
    if not s:
        return 0

    i, n = 0, len(s)

    # Skip whitespace
    while i < n and s[i] == ' ':
        i += 1
    if i == n:
        return 0

    # Read sign
    sign = 1
    if i < n and s[i] in ('+', '-'):
        sign = -1 if s[i] == '-' else 1
        i += 1

    def recurse_digits(idx: int, result: int) -> int:
        # Base case: end of string or non-digit
        if idx >= n or not s[idx].isdigit():
            return result

        digit = int(s[idx])

        # Overflow check BEFORE accumulation
        if result > INT_MAX // 10 or (result == INT_MAX // 10 and digit > 7):
            return INT_MAX  # caller applies sign for INT_MIN

        result = result * 10 + digit
        return recurse_digits(idx + 1, result)

    unsigned_result = recurse_digits(i, 0)
    value = sign * unsigned_result
    # Clamp in case of overflow edge
    return max(INT_MIN, min(INT_MAX, value))


# ============================================================
# APPROACH 3: BEST -- Fully Recursive (state machine)
# Time: O(n)  |  Space: O(n) call stack
#
# Everything is recursive: whitespace skipping, sign reading,
# and digit processing. Uses a phase parameter:
# 0 = WHITESPACE, 1 = SIGN, 2 = DIGIT
# ============================================================
def best(s: str) -> int:
    if not s:
        return 0

    def fully_recursive(i: int, phase: int, sign: int, result: int) -> int:
        if i >= len(s):
            return sign * result

        c = s[i]

        if phase == 0:  # WHITESPACE
            if c == ' ':
                return fully_recursive(i + 1, 0, sign, result)
            return fully_recursive(i, 1, sign, result)  # transition

        if phase == 1:  # SIGN
            if c == '-':
                return fully_recursive(i + 1, 2, -1, result)
            if c == '+':
                return fully_recursive(i + 1, 2, 1, result)
            return fully_recursive(i, 2, sign, result)  # no sign

        # phase == 2: DIGIT
        if not c.isdigit():
            return sign * result

        digit = int(c)

        # Overflow check
        if result > INT_MAX // 10 or (result == INT_MAX // 10 and digit > 7):
            return INT_MAX if sign == 1 else INT_MIN

        result = result * 10 + digit
        return fully_recursive(i + 1, 2, sign, result)

    return fully_recursive(0, 0, 1, 0)


# ============================================================
# TESTING
# ============================================================
if __name__ == "__main__":
    print("=== Recursive Implementation of atoi ===\n")

    tests = [
        ("42",              42),
        ("   -42",          -42),
        ("+123",            123),
        ("4193 with words", 4193),
        ("words and 987",   0),
        ("",                0),
        ("-91283472332",    -2147483648),
        ("91283472332",     2147483647),
        ("  +0 123",        0),
        ("   ",             0),
        ("-000123",         -123),
    ]

    for s, expected in tests:
        b = brute_force(s)
        o = optimal(s)
        be = best(s)

        print(f'Input: "{s}"  Expected: {expected}')
        print(f"  Brute:   {b}  {'PASS' if b == expected else 'FAIL'}")
        print(f"  Optimal: {o}  {'PASS' if o == expected else 'FAIL'}")
        print(f"  Best:    {be}  {'PASS' if be == expected else 'FAIL'}")
        print()
