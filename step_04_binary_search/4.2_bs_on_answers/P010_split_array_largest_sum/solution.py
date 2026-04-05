"""Problem: Split Array Largest Sum - Minimize the largest sum among k subarrays"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n * sum)  |  Space: O(1)
# Try every limit from max(nums) to sum(nums). For each, greedily split
# and check if k pieces suffice. Return the first valid limit.
# ============================================================
def brute_force(nums: list, k: int) -> int:
    def pieces_needed(limit):
        pieces, cur = 1, 0
        for x in nums:
            if cur + x > limit:
                pieces += 1
                cur = x
            else:
                cur += x
        return pieces

    for limit in range(max(nums), sum(nums) + 1):
        if pieces_needed(limit) <= k:
            return limit
    return -1


# ============================================================
# APPROACH 2: OPTIMAL - Binary Search on Answer
# Time: O(n log(sum))  |  Space: O(1)
# Binary search in [max(nums), sum(nums)]. For each mid, greedily
# count minimum splits needed; find smallest mid satisfying k splits.
# ============================================================
def optimal(nums: list, k: int) -> int:
    def can_split(max_sum):
        pieces, cur = 1, 0
        for x in nums:
            if cur + x > max_sum:
                pieces += 1
                cur = x
                if pieces > k:
                    return False
            else:
                cur += x
        return True

    lo, hi, ans = max(nums), sum(nums), sum(nums)
    while lo <= hi:
        mid = (lo + hi) // 2
        if can_split(mid):
            ans = mid
            hi = mid - 1
        else:
            lo = mid + 1
    return ans


# ============================================================
# APPROACH 3: BEST - lo<hi minimization template
# Time: O(n log(sum))  |  Space: O(1)
# Identical to Optimal with cleaner lo<hi loop (lower-mid for minimization)
# ============================================================
def best(nums: list, k: int) -> int:
    def feasible(limit):
        pieces, cur = 1, 0
        for x in nums:
            if cur + x > limit:
                pieces += 1
                cur = x
            else:
                cur += x
        return pieces <= k

    lo, hi = max(nums), sum(nums)
    while lo < hi:
        mid = (lo + hi) // 2  # lower mid
        if feasible(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo


if __name__ == "__main__":
    print("=== Split Array Largest Sum ===")
    tests = [
        ([7, 2, 5, 10, 8], 2, 18),
        ([1, 2, 3, 4, 5],  2, 9),
        ([1, 4, 4],        3, 4),
    ]
    for nums, k, expected in tests:
        b  = brute_force(nums, k)
        o  = optimal(nums, k)
        be = best(nums, k)
        print(f"nums={nums}, k={k} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
