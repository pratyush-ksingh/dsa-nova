"""
Problem: Check Palindrome String (Recursion)
Difficulty: EASY | XP: 10

Determine whether a string is a palindrome using recursion.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Reverse and Compare
# Time: O(N)  |  Space: O(N)
# Reverse the string and check equality.
# ============================================================
def brute_force(s: str) -> bool:
    return s == s[::-1]


# ============================================================
# APPROACH 2: OPTIMAL -- Recursive Two-Pointer
# Time: O(N)  |  Space: O(N) recursion stack
# Compare endpoints, recurse on inner substring.
# ============================================================
def optimal(s: str) -> bool:
    def check(left: int, right: int) -> bool:
        # Base case: pointers met or crossed
        if left >= right:
            return True
        # Mismatch found
        if s[left] != s[right]:
            return False
        # Recurse on inner substring
        return check(left + 1, right - 1)

    return check(0, len(s) - 1)


# ============================================================
# APPROACH 3: BEST -- Iterative Two-Pointer
# Time: O(N)  |  Space: O(1)
# Same logic as recursive, but with a while loop.
# ============================================================
def best(s: str) -> bool:
    left, right = 0, len(s) - 1
    while left < right:
        if s[left] != s[right]:
            return False
        left += 1
        right -= 1
    return True


# ============================================================
# TESTS
# ============================================================
if __name__ == "__main__":
    test_cases = [
        ("", True), ("a", True), ("ab", False), ("aa", True),
        ("madam", True), ("racecar", True), ("hello", False),
        ("abba", True), ("abcba", True),
    ]

    print("=== Check Palindrome String ===\n")
    for s, expected in test_cases:
        b = brute_force(s)
        o = optimal(s)
        bt = best(s)
        status = "PASS" if b == o == bt == expected else "FAIL"
        print(f"[{status}] s={('\"' + s + '\"'):<12} | Brute={b!s:<5} | Recursive={o!s:<5} | Iterative={bt!s:<5} | Expected={expected!s:<5}")
