"""Problem: Minimum Appends for Palindrome
Difficulty: MEDIUM | XP: 25"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(n)
# Try all prefix lengths from longest to shortest; check palindrome.
# ============================================================
def brute_force(s: str) -> int:
    n = len(s)
    for length in range(n, 0, -1):
        prefix = s[:length]
        if prefix == prefix[::-1]:
            return n - length
    return n - 1


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n)  |  Space: O(n)
# KMP failure function on s + "#" + reverse(s).
# lps[-1] = length of longest palindromic prefix. Answer = n - lps[-1].
# ============================================================
def optimal(s: str) -> int:
    combined = s + "#" + s[::-1]
    n = len(combined)
    lps = [0] * n

    length, i = 0, 1
    while i < n:
        if combined[i] == combined[length]:
            length += 1
            lps[i] = length
            i += 1
        else:
            if length != 0:
                length = lps[length - 1]
            else:
                lps[i] = 0
                i += 1

    return len(s) - lps[-1]


# ============================================================
# APPROACH 3: BEST
# Time: O(n)  |  Space: O(n)
# Same KMP approach with a clean helper. This is optimal —
# you must scan at least O(n) chars to determine prefix palindrome.
# ============================================================
def _build_lps(pattern: str) -> list:
    n = len(pattern)
    lps = [0] * n
    length, i = 0, 1
    while i < n:
        if pattern[i] == pattern[length]:
            length += 1
            lps[i] = length
            i += 1
        elif length != 0:
            length = lps[length - 1]
        else:
            lps[i] = 0
            i += 1
    return lps


def best(s: str) -> int:
    pattern = s + "$" + s[::-1]
    lps = _build_lps(pattern)
    return len(s) - lps[-1]


if __name__ == "__main__":
    tests = [
        ("abcd",     3),
        ("aba",      0),
        ("aacecaaa", 1),
        ("racecar",  0),
        ("a",        0),
        ("ab",       1),
    ]
    print("=== Minimum Appends for Palindrome ===")
    for s, expected in tests:
        b  = brute_force(s)
        o  = optimal(s)
        be = best(s)
        status = "PASS" if b == o == be == expected else "FAIL"
        print(f"Input: {s!r:12} | Brute: {b} | Optimal: {o} | Best: {be} | Expected: {expected} | {status}")
