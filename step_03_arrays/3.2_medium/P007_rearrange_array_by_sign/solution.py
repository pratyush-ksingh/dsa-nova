"""Problem: Rearrange Array by Sign - Alternate positive and negative elements"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n)  |  Space: O(n)
# Separate positives and negatives into two lists, then interleave.
# Assumes equal number of positives and negatives.
# ============================================================
def brute_force(nums: list) -> list:
    pos = [x for x in nums if x >= 0]
    neg = [x for x in nums if x < 0]
    res = []
    for p, n in zip(pos, neg):
        res.append(p)
        res.append(n)
    return res


# ============================================================
# APPROACH 2: OPTIMAL - Two index pointers
# Time: O(n)  |  Space: O(n)
# Maintain posIdx=0, negIdx=1 and fill result array in one pass
# ============================================================
def optimal(nums: list) -> list:
    n = len(nums)
    res = [0] * n
    pos_idx, neg_idx = 0, 1
    for x in nums:
        if x >= 0:
            res[pos_idx] = x
            pos_idx += 2
        else:
            res[neg_idx] = x
            neg_idx += 2
    return res


# ============================================================
# APPROACH 3: BEST - Generalized (handles unequal counts)
# Time: O(n)  |  Space: O(n)
# Interleave while both lists have elements, then append the rest.
# Works correctly even when #positives != #negatives.
# ============================================================
def best(nums: list) -> list:
    pos = [x for x in nums if x >= 0]
    neg = [x for x in nums if x < 0]
    res = []
    pi, ni = 0, 0
    while pi < len(pos) and ni < len(neg):
        res.append(pos[pi]); pi += 1
        res.append(neg[ni]); ni += 1
    res.extend(pos[pi:])
    res.extend(neg[ni:])
    return res


if __name__ == "__main__":
    print("=== Rearrange Array by Sign ===")
    tests = [
        [3, 1, -2, -5, 2, -4],
        [-1, 1],
        [1, 2, 3, -1, -2, -3],
    ]
    for nums in tests:
        b  = brute_force(nums)
        o  = optimal(nums)
        be = best(nums)
        print(f"Input={nums}")
        print(f"  Brute={b}")
        print(f"  Optimal={o}")
        print(f"  Best={be}")
        # Verify positives at even, negatives at odd indices (for equal-count case)
        for i, v in enumerate(o):
            assert (v >= 0) == (i % 2 == 0), f"Wrong sign at index {i}: {v}"
    print("All tests passed.")
