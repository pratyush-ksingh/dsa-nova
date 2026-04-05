"""
Problem: Rotate String (LeetCode #796)
Difficulty: EASY | XP: 10

Given two strings s and goal, return True if s can become
goal after some number of left rotations.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Try All Rotations)
# Time: O(n^2) | Space: O(n)
# ============================================================
def brute_force(s: str, goal: str) -> bool:
    """Generate every rotation and check for a match."""
    if len(s) != len(goal):
        return False
    n = len(s)
    for i in range(n):
        rotated = s[i:] + s[:i]
        if rotated == goal:
            return True
    return False


# ============================================================
# APPROACH 2: OPTIMAL (Concatenation Trick: s + s)
# Time: O(n) | Space: O(n)
# ============================================================
def optimal(s: str, goal: str) -> bool:
    """Check if goal is a substring of s + s."""
    if len(s) != len(goal):
        return False
    return goal in (s + s)


# ============================================================
# APPROACH 3: BEST (KMP on circular text)
# Time: O(n) | Space: O(n) for failure array
# ============================================================
def best(s: str, goal: str) -> bool:
    """Use KMP pattern matching on s treated as circular."""
    if len(s) != len(goal):
        return False
    n = len(s)
    if n == 0:
        return True

    # Build KMP failure function for goal
    fail = [0] * n
    k = 0
    for i in range(1, n):
        while k > 0 and goal[k] != goal[i]:
            k = fail[k - 1]
        if goal[k] == goal[i]:
            k += 1
        fail[i] = k

    # Run KMP on s treated as circular (2n characters)
    j = 0
    for i in range(2 * n):
        c = s[i % n]
        while j > 0 and goal[j] != c:
            j = fail[j - 1]
        if goal[j] == c:
            j += 1
        if j == n:
            return True

    return False


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Rotate String ===\n")

    test_cases = [
        ("abcde", "cdeab", True),
        ("abcde", "abced", False),
        ("a", "a", True),
        ("aa", "aa", True),
        ("abc", "abc", True),
        ("abc", "bac", False),
        ("ab", "ba", True),
    ]

    for s, goal, expected in test_cases:
        b = brute_force(s, goal)
        o = optimal(s, goal)
        be = best(s, goal)
        status = "PASS" if b == expected and o == expected and be == expected else "FAIL"
        print(f"Input:    s=\"{s}\", goal=\"{goal}\"")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {be}")
        print(f"  Expected: {expected}  [{status}]\n")
