"""Problem: Book Allocation - Minimize the maximum pages allocated to any one student"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * sum(pages))  |  Space: O(1)
# Try every value from max(pages) to sum(pages) as the limit.
# Return the first limit where m students suffice.
# ============================================================
def brute_force(pages: list, m: int) -> int:
    if m > len(pages):
        return -1

    def students_needed(limit):
        students, cur = 1, 0
        for p in pages:
            if cur + p > limit:
                students += 1
                cur = p
            else:
                cur += p
        return students

    for limit in range(max(pages), sum(pages) + 1):
        if students_needed(limit) <= m:
            return limit
    return -1


# ============================================================
# APPROACH 2: OPTIMAL - Binary Search on Answer
# Time: O(n log(sum))  |  Space: O(1)
# Binary search between max(pages) and sum(pages). For each mid,
# greedily check if allocation is possible with m students.
# ============================================================
def optimal(pages: list, m: int) -> int:
    if m > len(pages):
        return -1

    def is_possible(limit):
        students, cur = 1, 0
        for p in pages:
            if p > limit:
                return False
            if cur + p > limit:
                students += 1
                cur = p
            else:
                cur += p
        return students <= m

    lo, hi, ans = max(pages), sum(pages), sum(pages)
    while lo <= hi:
        mid = (lo + hi) // 2
        if is_possible(mid):
            ans = mid
            hi = mid - 1
        else:
            lo = mid + 1
    return ans


# ============================================================
# APPROACH 3: BEST - Binary search lo<hi form (minimization template)
# Time: O(n log(sum))  |  Space: O(1)
# Clean lo<hi with lower-mid — standard pattern for "minimize the max"
# ============================================================
def best(pages: list, m: int) -> int:
    if m > len(pages):
        return -1

    def feasible(limit):
        students, cur = 1, 0
        for p in pages:
            if p > limit:
                return False
            if cur + p > limit:
                students += 1
                cur = p
                if students > m:
                    return False
            else:
                cur += p
        return True

    lo, hi = max(pages), sum(pages)
    while lo < hi:
        mid = (lo + hi) // 2  # lower mid for minimization
        if feasible(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo


if __name__ == "__main__":
    print("=== Book Allocation Problem ===")
    tests = [
        ([12, 34, 67, 90], 2, 113),
        ([10, 20, 30, 40], 2, 60),
        ([15, 17, 20], 2, 32),  # optimal: [15,17] and [20] => max=32
    ]
    for pages, m, expected in tests:
        b  = brute_force(pages, m)
        o  = optimal(pages, m)
        be = best(pages, m)
        print(f"Pages={pages}, m={m} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
