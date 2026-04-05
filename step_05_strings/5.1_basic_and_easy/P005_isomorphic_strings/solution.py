"""
Problem: Isomorphic Strings (LeetCode #205)
Difficulty: EASY | XP: 10

Check if two strings are isomorphic (one-to-one character mapping).
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Transform to Pattern)
# Time: O(n) | Space: O(n)
# ============================================================
def brute_force(s: str, t: str) -> bool:
    """Convert each string to a canonical pattern and compare."""
    def to_pattern(string: str) -> list:
        mapping = {}
        counter = 0
        pattern = []
        for c in string:
            if c not in mapping:
                mapping[c] = counter
                counter += 1
            pattern.append(mapping[c])
        return pattern

    return to_pattern(s) == to_pattern(t)


# ============================================================
# APPROACH 2: OPTIMAL (Dual HashMap)
# Time: O(n) | Space: O(k) where k = charset size
# ============================================================
def optimal(s: str, t: str) -> bool:
    """Maintain two maps (s->t and t->s) and verify consistency."""
    map_st = {}
    map_ts = {}
    for sc, tc in zip(s, t):
        # Check s -> t
        if sc in map_st:
            if map_st[sc] != tc:
                return False
        else:
            map_st[sc] = tc

        # Check t -> s
        if tc in map_ts:
            if map_ts[tc] != sc:
                return False
        else:
            map_ts[tc] = sc

    return True


# ============================================================
# APPROACH 3: ELEGANT (Last-Seen Index Comparison)
# Time: O(n) | Space: O(k) where k = charset size
# ============================================================
def last_seen(s: str, t: str) -> bool:
    """Compare last-seen indices for each character pair."""
    last_s = {}
    last_t = {}
    for i, (sc, tc) in enumerate(zip(s, t)):
        if last_s.get(sc, -1) != last_t.get(tc, -1):
            return False
        last_s[sc] = i
        last_t[tc] = i
    return True


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Isomorphic Strings ===\n")

    test_cases = [
        ("egg", "add", True),
        ("foo", "bar", False),
        ("paper", "title", True),
        ("badc", "baba", False),
        ("ab", "aa", False),
        ("a", "b", True),
        ("abc", "abc", True),
        ("aaa", "bbb", True),
    ]

    for s, t, expected in test_cases:
        b = brute_force(s, t)
        o = optimal(s, t)
        ls = last_seen(s, t)
        status = "PASS" if b == expected and o == expected and ls == expected else "FAIL"
        print(f"s=\"{s}\", t=\"{t}\"")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  LastSeen: {ls}")
        print(f"  Expected: {expected}  [{status}]\n")
