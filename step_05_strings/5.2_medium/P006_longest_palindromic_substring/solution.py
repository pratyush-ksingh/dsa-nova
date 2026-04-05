"""
Problem: Longest Palindromic Substring (LeetCode #5)
Difficulty: MEDIUM | XP: 25
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Check All Substrings
# Time: O(n³)  |  Space: O(1)
#
# Generate every substring, check if it is a palindrome,
# track the longest one found.
# ============================================================
def brute_force(s: str) -> str:
    n = len(s)
    best_start, best_len = 0, 1

    def is_palindrome(l: int, r: int) -> bool:
        while l < r:
            if s[l] != s[r]:
                return False
            l += 1
            r -= 1
        return True

    for i in range(n):
        for j in range(i, n):
            if is_palindrome(i, j) and (j - i + 1) > best_len:
                best_start = i
                best_len = j - i + 1

    return s[best_start: best_start + best_len]


# ============================================================
# APPROACH 2: OPTIMAL -- Expand Around Center
# Time: O(n²)  |  Space: O(1)
#
# For each position, expand outward for both odd-length
# (center at i) and even-length (center between i and i+1)
# palindromes. Track the longest found.
# ============================================================
def optimal(s: str) -> str:
    n = len(s)
    start, end = 0, 0

    def expand(l: int, r: int) -> tuple:
        while l >= 0 and r < n and s[l] == s[r]:
            l -= 1
            r += 1
        # After loop: s[l+1..r-1] is the palindrome
        return l + 1, r - 1

    for i in range(n):
        # Odd length
        l1, r1 = expand(i, i)
        # Even length
        l2, r2 = expand(i, i + 1)

        if r1 - l1 > end - start:
            start, end = l1, r1
        if r2 - l2 > end - start:
            start, end = l2, r2

    return s[start: end + 1]


# ============================================================
# APPROACH 3: BEST -- Manacher's Algorithm
# Time: O(n)  |  Space: O(n)
#
# Transform string to handle even/odd uniformly by inserting
# '#' between characters: "abc" -> "#a#b#c#".
# Use the palindrome radius array P[] to find the longest
# palindrome in linear time.
# ============================================================
def best(s: str) -> str:
    # Transform: "abc" -> "^#a#b#c#$"
    t = '#'.join('^{}$'.format(s))
    n = len(t)
    p = [0] * n
    center = right = 0

    for i in range(1, n - 1):
        mirror = 2 * center - i
        if i < right:
            p[i] = min(right - i, p[mirror])
        # Expand around i
        while t[i + p[i] + 1] == t[i - p[i] - 1]:
            p[i] += 1
        # Update center and right boundary
        if i + p[i] > right:
            center = i
            right = i + p[i]

    # Find the maximum element in p
    max_len, center_idx = max((v, i) for i, v in enumerate(p))
    start = (center_idx - max_len) // 2
    return s[start: start + max_len]


if __name__ == "__main__":
    print("=== Longest Palindromic Substring ===\n")

    tests = [
        ("babad", {"bab", "aba"}),
        ("cbbd", {"bb"}),
        ("a", {"a"}),
        ("racecar", {"racecar"}),
        ("abacaba", {"abacaba"}),
    ]

    for s, valid in tests:
        b = brute_force(s)
        o = optimal(s)
        bt = best(s)
        ok_b = b in valid
        ok_o = o in valid
        ok_bt = bt in valid
        print(f"Input: \"{s}\"")
        print(f"  Brute:   \"{b}\" [{'OK' if ok_b else 'FAIL'}]")
        print(f"  Optimal: \"{o}\" [{'OK' if ok_o else 'FAIL'}]")
        print(f"  Best:    \"{bt}\" [{'OK' if ok_bt else 'FAIL'}]")
        print()
