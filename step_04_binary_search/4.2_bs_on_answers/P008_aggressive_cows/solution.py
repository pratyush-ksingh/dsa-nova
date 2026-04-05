"""Problem: Aggressive Cows - Maximize minimum distance between k cows in stalls"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n log n + n * max_dist)  |  Space: O(1)
# Try every possible minimum distance; for each check if k cows fit greedily
# ============================================================
def brute_force(stalls: list, k: int) -> int:
    stalls = sorted(stalls)

    def can_place(min_dist):
        count, last = 1, stalls[0]
        for s in stalls[1:]:
            if s - last >= min_dist:
                last = s
                count += 1
        return count >= k

    ans = 0
    for d in range(1, stalls[-1] - stalls[0] + 1):
        if can_place(d):
            ans = d
        else:
            break
    return ans


# ============================================================
# APPROACH 2: OPTIMAL - Binary Search on Answer
# Time: O(n log n + n log(max_dist))  |  Space: O(1)
# Binary search on min distance. Greedy check: place next cow at
# first stall that is >= min_dist away from the last placed cow.
# ============================================================
def optimal(stalls: list, k: int) -> int:
    stalls = sorted(stalls)

    def can_place(min_dist):
        count, last = 1, stalls[0]
        for s in stalls:
            if s - last >= min_dist:
                last = s
                count += 1
        return count >= k

    lo, hi, ans = 1, stalls[-1] - stalls[0], 0
    while lo <= hi:
        mid = (lo + hi) // 2
        if can_place(mid):
            ans = mid
            lo = mid + 1
        else:
            hi = mid - 1
    return ans


# ============================================================
# APPROACH 3: BEST - Binary search with upper-mid to avoid off-by-one
# Time: O(n log n + n log(max_dist))  |  Space: O(1)
# Uses lo < hi loop form with upper mid — standard competitive programming template
# ============================================================
def best(stalls: list, k: int) -> int:
    stalls = sorted(stalls)

    def feasible(dist):
        cows, prev = 1, stalls[0]
        for s in stalls:
            if s - prev >= dist:
                prev = s
                cows += 1
                if cows == k:
                    return True
        return cows >= k

    lo, hi = 1, stalls[-1] - stalls[0]
    while lo < hi:
        mid = (lo + hi + 1) // 2  # upper mid
        if feasible(mid):
            lo = mid
        else:
            hi = mid - 1
    return lo


if __name__ == "__main__":
    print("=== Aggressive Cows ===")
    tests = [
        ([2, 4, 12, 5, 3], 3, 3),
        ([1, 2, 4, 8, 9],  3, 3),
    ]
    for stalls, k, expected in tests:
        b  = brute_force(stalls, k)
        o  = optimal(stalls, k)
        be = best(stalls, k)
        print(f"Stalls={stalls}, k={k} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
