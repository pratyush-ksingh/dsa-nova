"""Problem: Valid IP Addresses - Generate all valid IPv4 addresses from a digit string"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(1) bounded (at most ~27 results)  |  Space: O(1)
# Try all positions for 3 dots; validate each of 4 segments
# ============================================================
def brute_force(s: str) -> list:
    def valid(seg):
        if not seg or len(seg) > 3:
            return False
        if len(seg) > 1 and seg[0] == '0':
            return False
        return int(seg) <= 255

    result = []
    n = len(s)
    for i in range(1, 4):
        for j in range(i + 1, i + 4):
            for k in range(j + 1, j + 4):
                if k >= n:
                    continue
                segs = [s[:i], s[i:j], s[j:k], s[k:]]
                if all(valid(seg) for seg in segs):
                    result.append(".".join(segs))
    return result


# ============================================================
# APPROACH 2: OPTIMAL - Backtracking
# Time: O(1) bounded  |  Space: O(1)
# Build each of 4 octets character by character via recursion.
# Valid octets: no leading zeros, integer value 0-255.
# ============================================================
def optimal(s: str) -> list:
    def is_valid(seg):
        if not seg or len(seg) > 3:
            return False
        if len(seg) > 1 and seg[0] == '0':
            return False
        return int(seg) <= 255

    result = []

    def backtrack(start, parts):
        if len(parts) == 4:
            if start == len(s):
                result.append(".".join(parts))
            return
        for length in range(1, 4):
            if start + length > len(s):
                break
            seg = s[start:start + length]
            if is_valid(seg):
                parts.append(seg)
                backtrack(start + length, parts)
                parts.pop()

    backtrack(0, [])
    return result


# ============================================================
# APPROACH 3: BEST - Backtracking with remaining-length pruning
# Time: O(1) bounded  |  Space: O(1)
# Prune early: if remaining chars < parts_left or > 3*parts_left, stop.
# This avoids exploring obviously invalid branches.
# ============================================================
def best(s: str) -> list:
    result = []

    def bt(pos, part, current):
        remaining = len(s) - pos
        parts_left = 4 - part
        # Prune: each remaining part needs 1-3 chars
        if remaining < parts_left or remaining > 3 * parts_left:
            return
        if part == 4:
            if pos == len(s):
                result.append(".".join(current))
            return
        for length in range(1, 4):
            if pos + length > len(s):
                break
            seg = s[pos:pos + length]
            if length > 1 and seg[0] == '0':
                break  # no leading zeros
            if int(seg) > 255:
                break
            current.append(seg)
            bt(pos + length, part + 1, current)
            current.pop()

    bt(0, 0, [])
    return result


if __name__ == "__main__":
    print("=== Valid IP Addresses ===")
    tests = [
        "25525511135",
        "0000",
        "1111",
        "010010",
        "255255255255",
    ]
    for s in tests:
        b  = sorted(brute_force(s))
        o  = sorted(optimal(s))
        be = sorted(best(s))
        print(f'"{s}" => {be}')
        assert b == o == be, f"Mismatch for {s}: {b} vs {o} vs {be}"
    print("All tests passed.")
