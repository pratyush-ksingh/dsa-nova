"""Problem: Matrix Median - Find median in a row-wise sorted matrix"""

import bisect

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(r*c log(r*c))  |  Space: O(r*c)
# Flatten matrix, sort, return middle element
# ============================================================
def brute_force(matrix: list) -> int:
    flat = sorted(x for row in matrix for x in row)
    return flat[len(flat) // 2]


# ============================================================
# APPROACH 2: OPTIMAL - Binary Search on Value
# Time: O(32 * r * log c)  |  Space: O(1)
# Binary search on value range [lo, hi]. For each candidate mid,
# count elements <= mid using bisect on each sorted row.
# Median is smallest value with count > (r*c)//2.
# ============================================================
def optimal(matrix: list) -> int:
    r, c = len(matrix), len(matrix[0])
    lo = min(row[0] for row in matrix)
    hi = max(row[-1] for row in matrix)
    desired = (r * c + 1) // 2  # 1-indexed position of median

    while lo < hi:
        mid = (lo + hi) // 2
        # Count elements <= mid
        count = sum(bisect.bisect_right(row, mid) for row in matrix)
        if count < desired:
            lo = mid + 1
        else:
            hi = mid
    return lo


# ============================================================
# APPROACH 3: BEST - Same as Optimal with explicit explanation
# Time: O(32 * r * log c)  |  Space: O(1)
# Binary search on answer value; this IS the optimal approach for this problem.
# The key insight: for a sorted set the median is the smallest x
# such that at least (n//2 + 1) elements are <= x.
# ============================================================
def best(matrix: list) -> int:
    r, c = len(matrix), len(matrix[0])
    lo = min(row[0] for row in matrix)
    hi = max(row[-1] for row in matrix)
    median_pos = (r * c) // 2 + 1  # ceil of midpoint, 1-indexed

    while lo < hi:
        mid = (lo + hi) // 2
        count = sum(bisect.bisect_right(row, mid) for row in matrix)
        if count < median_pos:
            lo = mid + 1
        else:
            hi = mid
    return lo


if __name__ == "__main__":
    print("=== Matrix Median ===")
    tests = [
        ([[1,3,5],[2,6,9],[3,6,9]], 5),
        ([[1,2,3,4,5]], 3),
        ([[1,1,1],[1,1,1],[1,1,2]], 1),
    ]
    for matrix, expected in tests:
        b  = brute_force(matrix)
        o  = optimal(matrix)
        be = best(matrix)
        print(f"matrix={matrix} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
