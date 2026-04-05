"""
Problem: Largest Odd Number in String (LeetCode #1903)
Difficulty: EASY | XP: 10
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check All Prefix Substrings
# Time: O(n)  |  Space: O(1) extra
#
# The largest substring must start at index 0 (longer = larger).
# Check prefix substrings from longest to shortest. The first
# one ending on an odd digit is the answer.
# ============================================================
def brute_force(num: str) -> str:
    for j in range(len(num) - 1, -1, -1):
        if int(num[j]) % 2 == 1:
            return num[: j + 1]
    return ""


# ============================================================
# APPROACH 2: OPTIMAL -- Right-Scan for Last Odd Digit
# Time: O(n)  |  Space: O(1) extra
#
# A number is odd iff its last digit is odd. Scan from the right,
# find the rightmost odd digit, return the prefix up to it.
# ============================================================
def optimal(num: str) -> str:
    for i in range(len(num) - 1, -1, -1):
        if num[i] in "13579":
            return num[: i + 1]
    return ""


# ============================================================
# APPROACH 3: BEST -- Same as Optimal (provably optimal)
# Time: O(n)  |  Space: O(1) extra
#
# Compact variant using ord() bitwise check for oddness.
# ============================================================
def best(num: str) -> str:
    for i in range(len(num) - 1, -1, -1):
        if ord(num[i]) & 1:  # odd ASCII = odd digit
            return num[: i + 1]
    return ""


if __name__ == "__main__":
    print("=== Largest Odd Number in String ===\n")

    tests = [
        ("52", "5"),
        ("4206", ""),
        ("35427", "35427"),
        ("2468", ""),
        ("10", "1"),
    ]

    for num, expected in tests:
        print(f"Input:    \"{num}\"")
        print(f"Expected: \"{expected}\"")
        print(f"Brute:    \"{brute_force(num)}\"")
        print(f"Optimal:  \"{optimal(num)}\"")
        print(f"Best:     \"{best(num)}\"")
        print()
