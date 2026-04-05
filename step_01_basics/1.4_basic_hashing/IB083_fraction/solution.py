"""Problem: Fraction
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given numerator and denominator, return the fraction as a string.
If the fractional part is repeating, enclose the repeating part in parentheses.
E.g., 1/3 -> "0.(3)", 1/6 -> "0.1(6)", 1/2 -> "0.5"
"""
from typing import List

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(denominator)  |  Space: O(denominator)
# Simulate long division — insert '(' into accumulated list
# ============================================================
def brute_force(numerator: int, denominator: int) -> str:
    if numerator == 0:
        return "0"
    sign = "-" if (numerator < 0) ^ (denominator < 0) else ""
    num, den = abs(numerator), abs(denominator)

    int_part = str(num // den)
    remainder = num % den
    if remainder == 0:
        return sign + int_part

    frac = []
    seen = {}
    while remainder != 0:
        if remainder in seen:
            pos = seen[remainder]
            frac.insert(pos, '(')
            frac.append(')')
            break
        seen[remainder] = len(frac)
        remainder *= 10
        frac.append(str(remainder // den))
        remainder %= den

    return sign + int_part + "." + "".join(frac)


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(denominator)  |  Space: O(denominator)
# Long division with HashMap mapping remainder -> position
# ============================================================
def optimal(numerator: int, denominator: int) -> str:
    if numerator == 0:
        return "0"
    sign = "-" if (numerator < 0) ^ (denominator < 0) else ""
    num, den = abs(numerator), abs(denominator)

    int_part = num // den
    remainder = num % den
    if remainder == 0:
        return sign + str(int_part)

    frac_digits = []
    seen = {}
    cycle_start = -1
    while remainder:
        if remainder in seen:
            cycle_start = seen[remainder]
            break
        seen[remainder] = len(frac_digits)
        remainder *= 10
        frac_digits.append(str(remainder // den))
        remainder %= den

    if cycle_start >= 0:
        frac_str = "".join(frac_digits[:cycle_start]) + "(" + "".join(frac_digits[cycle_start:]) + ")"
    else:
        frac_str = "".join(frac_digits)

    return sign + str(int_part) + "." + frac_str


# ============================================================
# APPROACH 3: BEST
# Time: O(denominator)  |  Space: O(denominator)
# Same as optimal — this problem has one clean O(n) solution.
# Variation: use a StringBuilder-style approach with explicit
# cycle detection and no post-hoc list insertion.
# ============================================================
def best(numerator: int, denominator: int) -> str:
    if numerator == 0:
        return "0"
    sign = "-" if (numerator < 0) ^ (denominator < 0) else ""
    num, den = abs(numerator), abs(denominator)

    result = sign + str(num // den)
    remainder = num % den
    if remainder == 0:
        return result

    result += "."
    seen = {}
    frac_part = []

    while remainder:
        if remainder in seen:
            idx = seen[remainder]
            frac_part.insert(idx, '(')
            frac_part.append(')')
            return result + "".join(frac_part)
        seen[remainder] = len(frac_part)
        remainder *= 10
        frac_part.append(str(remainder // den))
        remainder %= den

    return result + "".join(frac_part)


if __name__ == "__main__":
    cases = [
        (1, 2, "0.5"),
        (2, 1, "2"),
        (2, 3, "0.(6)"),
        (1, 6, "0.1(6)"),
        (-1, 6, "-0.1(6)"),
        (1, 7, "0.(142857)"),
        (-50, 8, "-6.25"),
        (7, 12, "0.58(3)"),
        (0, 3, "0"),
    ]
    print("=== Fraction to Recurring Decimal ===")
    print(f"{'Input':<12} {'BF':<16} {'OPT':<16} {'BEST':<16} {'EXP':<16} Status")
    for num, den, exp in cases:
        b = brute_force(num, den)
        o = optimal(num, den)
        bst = best(num, den)
        ok = "OK" if b == o == bst == exp else "MISMATCH"
        print(f"{num}/{den:<8} {b:<16} {o:<16} {bst:<16} {exp:<16} {ok}")
