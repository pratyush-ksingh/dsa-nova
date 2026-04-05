"""Problem: Compare Version Numbers - Compare two version strings like "1.2.3" vs "1.3" """

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(max(n,m))  |  Space: O(n+m)
# Split both strings by ".", pad shorter list with "0"s,
# convert to ints and compare element by element.
# ============================================================
def brute_force(version1: str, version2: str) -> int:
    v1 = list(map(int, version1.split(".")))
    v2 = list(map(int, version2.split(".")))
    # Pad with zeros
    length = max(len(v1), len(v2))
    v1 += [0] * (length - len(v1))
    v2 += [0] * (length - len(v2))
    for a, b in zip(v1, v2):
        if a < b: return -1
        if a > b: return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL - Two-pointer, parse segments on the fly
# Time: O(max(n,m))  |  Space: O(1) extra
# Use two string indices; parse each integer segment without splitting
# ============================================================
def optimal(version1: str, version2: str) -> int:
    i, j = 0, 0
    n1, n2 = len(version1), len(version2)
    while i < n1 or j < n2:
        num1 = num2 = 0
        while i < n1 and version1[i] != '.':
            num1 = num1 * 10 + int(version1[i]); i += 1
        while j < n2 and version2[j] != '.':
            num2 = num2 * 10 + int(version2[j]); j += 1
        if num1 < num2: return -1
        if num1 > num2: return 1
        i += 1; j += 1
    return 0


# ============================================================
# APPROACH 3: BEST - zip_longest for clean Pythonic solution
# Time: O(max(n,m))  |  Space: O(n+m)
# Use itertools.zip_longest to handle different lengths cleanly
# ============================================================
def best(version1: str, version2: str) -> int:
    from itertools import zip_longest
    parts1 = version1.split(".")
    parts2 = version2.split(".")
    for a, b in zip_longest(parts1, parts2, fillvalue="0"):
        va, vb = int(a), int(b)
        if va < vb: return -1
        if va > vb: return 1
    return 0


if __name__ == "__main__":
    print("=== Compare Version Numbers ===")
    tests = [
        ("1.0",   "1.0.0", 0),
        ("0.1",   "1.1",  -1),
        ("1.0.1", "1",     1),
        ("1.01",  "1.001", 0),
    ]
    for v1, v2, expected in tests:
        b  = brute_force(v1, v2)
        o  = optimal(v1, v2)
        be = best(v1, v2)
        print(f'"{v1}" vs "{v2}" => Brute={b}, Optimal={o}, Best={be} (exp={expected})')
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
