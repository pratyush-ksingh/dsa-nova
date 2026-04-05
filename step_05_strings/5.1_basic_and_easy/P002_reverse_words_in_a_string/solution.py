"""
Problem: Reverse Words in a String (LeetCode #151)
Difficulty: MEDIUM | XP: 25
"""


# ============================================================
# APPROACH 1: BRUTE FORCE -- Split, Reverse, Join
# Time: O(n)  |  Space: O(n)
#
# Split by whitespace, reverse the list, join with single space.
# ============================================================
def brute_force(s: str) -> str:
    words = s.split()
    return " ".join(reversed(words))


# ============================================================
# APPROACH 2: OPTIMAL -- Backward Scan with Two Pointers
# Time: O(n)  |  Space: O(n)
#
# Scan from the end, extract words in reverse order directly.
# ============================================================
def optimal(s: str) -> str:
    result = []
    n = len(s)
    i = n - 1

    while i >= 0:
        while i >= 0 and s[i] == ' ':
            i -= 1
        if i < 0:
            break

        end = i
        while i >= 0 and s[i] != ' ':
            i -= 1

        result.append(s[i + 1 : end + 1])

    return " ".join(result)


# ============================================================
# APPROACH 3: BEST -- Double Reversal (In-Place Concept)
# Time: O(n)  |  Space: O(n) due to Python string immutability
#
# Reverse entire char list, then reverse each word, compact spaces.
# ============================================================
def best(s: str) -> str:
    arr = list(s)
    n = len(arr)

    arr.reverse()

    write = 0
    i = 0
    while i < n:
        if arr[i] == ' ':
            i += 1
            continue

        if write > 0:
            arr[write] = ' '
            write += 1

        start = write
        while i < n and arr[i] != ' ':
            arr[write] = arr[i]
            write += 1
            i += 1

        left, right = start, write - 1
        while left < right:
            arr[left], arr[right] = arr[right], arr[left]
            left += 1
            right -= 1

    return "".join(arr[:write])


if __name__ == "__main__":
    print("=== Reverse Words in a String ===\n")

    tests = [
        ("the sky is blue", "blue is sky the"),
        ("  hello world  ", "world hello"),
        ("a good   example", "example good a"),
    ]

    for s, expected in tests:
        print(f"Input:    \"{s}\"")
        print(f"Expected: \"{expected}\"")
        print(f"Brute:    \"{brute_force(s)}\"")
        print(f"Optimal:  \"{optimal(s)}\"")
        print(f"Best:     \"{best(s)}\"")
        print()
