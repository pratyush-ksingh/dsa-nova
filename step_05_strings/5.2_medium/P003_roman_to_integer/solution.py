"""
Problem: Roman to Integer (LeetCode #13)
Difficulty: EASY | XP: 10

Convert a Roman numeral string to an integer.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Handle All 13 Cases Explicitly)
# Time: O(n) | Space: O(1)
# ============================================================
def brute_force(s: str) -> int:
    """Check two-char subtractive patterns first, then single chars."""
    two_char = {"CM": 900, "CD": 400, "XC": 90, "XL": 40, "IX": 9, "IV": 4}
    one_char = {"M": 1000, "D": 500, "C": 100, "L": 50, "X": 10, "V": 5, "I": 1}

    result = 0
    i = 0
    while i < len(s):
        if i + 1 < len(s) and s[i:i+2] in two_char:
            result += two_char[s[i:i+2]]
            i += 2
        else:
            result += one_char[s[i]]
            i += 1
    return result


# ============================================================
# APPROACH 2: OPTIMAL (Subtraction Rule with Lookahead)
# Time: O(n) | Space: O(1)
# ============================================================
def optimal(s: str) -> int:
    """If current value < next value, subtract; otherwise add."""
    val = {"I": 1, "V": 5, "X": 10, "L": 50, "C": 100, "D": 500, "M": 1000}
    result = 0
    for i in range(len(s)):
        curr = val[s[i]]
        nxt = val[s[i + 1]] if i + 1 < len(s) else 0
        if curr < nxt:
            result -= curr
        else:
            result += curr
    return result


# ============================================================
# APPROACH 3: RIGHT-TO-LEFT SCAN
# Time: O(n) | Space: O(1)
# ============================================================
def right_to_left(s: str) -> int:
    """Scan from right to left; subtract if current < previous (right neighbor)."""
    val = {"I": 1, "V": 5, "X": 10, "L": 50, "C": 100, "D": 500, "M": 1000}
    result = val[s[-1]]
    for i in range(len(s) - 2, -1, -1):
        curr = val[s[i]]
        right = val[s[i + 1]]
        if curr < right:
            result -= curr
        else:
            result += curr
    return result


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Roman to Integer ===\n")

    test_cases = [
        ("III", 3),
        ("LVIII", 58),
        ("MCMXCIV", 1994),
        ("IV", 4),
        ("IX", 9),
        ("V", 5),
        ("MMMCMXCIX", 3999),
        ("I", 1),
        ("XIV", 14),
    ]

    for s, expected in test_cases:
        b = brute_force(s)
        o = optimal(s)
        r = right_to_left(s)
        status = "PASS" if b == expected and o == expected and r == expected else "FAIL"
        print(f"Input: \"{s}\"")
        print(f"  Brute:      {b}")
        print(f"  Optimal:    {o}")
        print(f"  RightToLeft:{r}")
        print(f"  Expected:   {expected}  [{status}]\n")
