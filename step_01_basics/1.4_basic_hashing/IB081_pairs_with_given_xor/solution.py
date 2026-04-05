"""Problem: Pairs With Given XOR - Count pairs (i,j) where A[i]^A[j] == B"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Check every pair (i, j) i < j, count pairs whose XOR equals B
# ============================================================
def brute_force(A: list, B: int) -> int:
    count = 0
    n = len(A)
    for i in range(n):
        for j in range(i + 1, n):
            if A[i] ^ A[j] == B:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL - HashSet one-pass
# Time: O(n)  |  Space: O(n)
# For each x, if x^B is already in seen set, that is a valid pair.
# Add x to seen after checking to avoid using same element twice.
# ============================================================
def optimal(A: list, B: int) -> int:
    seen = set()
    count = 0
    for x in A:
        partner = x ^ B
        if partner in seen:
            count += 1
        seen.add(x)
    return count


# ============================================================
# APPROACH 3: BEST - Frequency HashMap (handles duplicates & B==0)
# Time: O(n)  |  Space: O(n)
# Build frequency map; for B==0 count C(freq,2); else pair up distinct keys.
# Avoids double-counting by tracking visited pairs.
# ============================================================
def best(A: list, B: int) -> int:
    from collections import Counter
    freq = Counter(A)

    if B == 0:
        return sum(f * (f - 1) // 2 for f in freq.values())

    count = 0
    visited = set()
    for x in freq:
        partner = x ^ B
        if x not in visited and partner in freq:
            count += freq[x] * freq[partner]
            visited.add(x)
            visited.add(partner)
    return count


if __name__ == "__main__":
    print("=== Pairs With Given XOR ===")
    tests = [
        ([6, 1, 3, 5, 2], 4),   # expected 2: (1^5=4, 6^2=4) -> (1,5),(6,2) but let's verify
        ([4, 3, 7], 7),          # expected 1: 4^3=7
        ([1, 1, 2, 2, 3], 0),   # B==0: 2 pairs: (1,1),(2,2)
    ]
    for A, B in tests:
        b = brute_force(A, B)
        o = optimal(A, B)
        be = best(A, B)
        print(f"A={A}, B={B} => Brute={b}, Optimal={o}, Best={be}")
        assert b == o, f"Brute vs Optimal mismatch: {b} {o}"
    print("All tests passed.")
