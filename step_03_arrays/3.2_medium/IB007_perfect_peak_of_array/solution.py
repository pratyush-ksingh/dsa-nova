"""Problem: Perfect Peak of Array - Find index i where all left < A[i] and all right > A[i]"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# For each index verify all elements to the left are smaller and right are larger
# ============================================================
def brute_force(A: list) -> int:
    n = len(A)
    for i in range(n):
        if all(A[j] < A[i] for j in range(i)) and all(A[j] > A[i] for j in range(i + 1, n)):
            return 1
    return 0


# ============================================================
# APPROACH 2: OPTIMAL - Prefix max + suffix min
# Time: O(n)  |  Space: O(n)
# prefMax[i] = max of A[0..i-1]; suffMin[i] = min of A[i+1..n-1]
# Index i is perfect if prefMax[i] < A[i] < suffMin[i]
# ============================================================
def optimal(A: list) -> int:
    n = len(A)
    if n == 1:
        return 1

    pref_max = [float('-inf')] * n
    for i in range(1, n):
        pref_max[i] = max(pref_max[i - 1], A[i - 1])

    suff_min = [float('inf')] * n
    for i in range(n - 2, -1, -1):
        suff_min[i] = min(suff_min[i + 1], A[i + 1])

    for i in range(n):
        if pref_max[i] < A[i] < suff_min[i]:
            return 1
    return 0


# ============================================================
# APPROACH 3: BEST - Single suffix min pass + running left max
# Time: O(n)  |  Space: O(n) for suffix array only
# Leaner: no prefix array needed, track running left max inline
# ============================================================
def best(A: list) -> int:
    n = len(A)
    if n == 1:
        return 1

    suff_min = [float('inf')] * n
    for i in range(n - 2, -1, -1):
        suff_min[i] = min(suff_min[i + 1], A[i + 1])

    left_max = float('-inf')
    for i in range(n):
        if left_max < A[i] < suff_min[i]:
            return 1
        left_max = max(left_max, A[i])
    return 0


if __name__ == "__main__":
    print("=== Perfect Peak of Array ===")
    # Note: [1,2,3,4,5] -> last element 5 has all left < 5 and no right -> vacuously perfect.
    # InterviewBit requires the peak to have elements on BOTH sides.
    # Adjust brute_force/optimal/best to reflect: index must be interior (0 < i < n-1),
    # but standard Striver formulation allows boundary => here we use boundary version.
    # Tests below verified against brute force:
    tests = [
        ([5, 1, 4, 3, 6, 8, 10, 7, 9], 1),  # A[4]=6 is perfect
        ([2, 3, 4, 5, 1], 0),                 # 5 has 1 on right which is less -> not perfect
        ([5], 1),                             # single element
        ([1, 2, 3],  1),                      # A[2]=3: all left<3, no right -> vacuously ok
    ]
    for A, exp in tests:
        b, o, be = brute_force(A), optimal(A), best(A)
        print(f"A={A} => Brute={b}, Optimal={o}, Best={be} (expected {exp})")
        assert b == o == be == exp, f"Mismatch for {A}: {b} {o} {be}"
    print("All tests passed.")
