"""
Problem: Largest Divisible Subset (LeetCode #368)
Difficulty: MEDIUM | XP: 25

Find largest subset where every pair (a,b) satisfies a%b==0 or b%a==0.
Sort + LIS variant with divisibility condition.
All 4 DP approaches.
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION (Generate all subsets)
# Time: O(2^n * n) | Space: O(n)
# ============================================================
def lds_recursive(nums: list[int]) -> list[int]:
    """Try all subsets, track largest valid one."""
    nums.sort()
    n = len(nums)
    best = []

    def solve(idx: int, prev_idx: int, current: list[int]):
        nonlocal best
        if len(current) > len(best):
            best = current[:]
        if idx == n:
            return

        # Not take
        solve(idx + 1, prev_idx, current)

        # Take if divisible
        if prev_idx == -1 or nums[idx] % nums[prev_idx] == 0:
            current.append(nums[idx])
            solve(idx + 1, idx, current)
            current.pop()

    solve(0, -1, [])
    return best


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n^2) | Space: O(n^2)
# ============================================================
def lds_memo(nums: list[int]) -> list[int]:
    """Cache (idx, prevIdx) for length computation, then reconstruct."""
    nums.sort()
    n = len(nums)
    cache = {}

    def solve(idx: int, prev_idx: int) -> int:
        """Returns length of largest divisible subset from idx onward."""
        if idx == n:
            return 0
        if (idx, prev_idx) in cache:
            return cache[(idx, prev_idx)]

        not_take = solve(idx + 1, prev_idx)
        take = 0
        if prev_idx == -1 or nums[idx] % nums[prev_idx] == 0:
            take = 1 + solve(idx + 1, idx)

        cache[(idx, prev_idx)] = max(take, not_take)
        return cache[(idx, prev_idx)]

    # Get length (mainly to demonstrate memoization)
    length = solve(0, -1)

    # Reconstruct using tabulation (more practical)
    return _tabulation_reconstruct(nums)


def _tabulation_reconstruct(nums: list[int]) -> list[int]:
    """Helper for reconstruction via tabulation."""
    n = len(nums)
    dp = [1] * n
    parent = [-1] * n

    max_len, max_idx = 1, 0
    for i in range(1, n):
        for j in range(i):
            if nums[i] % nums[j] == 0 and dp[j] + 1 > dp[i]:
                dp[i] = dp[j] + 1
                parent[i] = j
        if dp[i] > max_len:
            max_len = dp[i]
            max_idx = i

    result = []
    idx = max_idx
    while idx != -1:
        result.append(nums[idx])
        idx = parent[idx]
    return result[::-1]


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP -- LIS Style)
# Time: O(n^2) | Space: O(n)
# ============================================================
def lds_tab(nums: list[int]) -> list[int]:
    """
    dp[i] = size of largest divisible subset ending at index i.
    parent[i] = previous index in the subset for reconstruction.
    """
    nums.sort()
    n = len(nums)

    dp = [1] * n
    parent = [-1] * n

    max_len, max_idx = 1, 0

    for i in range(1, n):
        for j in range(i):
            if nums[i] % nums[j] == 0 and dp[j] + 1 > dp[i]:
                dp[i] = dp[j] + 1
                parent[i] = j
        if dp[i] > max_len:
            max_len = dp[i]
            max_idx = i

    # Reconstruct
    result = []
    idx = max_idx
    while idx != -1:
        result.append(nums[idx])
        idx = parent[idx]
    return result[::-1]


# ============================================================
# APPROACH 4: OPTIMIZED TABULATION (same complexity, cleaner)
# Time: O(n^2) | Space: O(n)
# ============================================================
def lds_optimized(nums: list[int]) -> list[int]:
    """
    Same O(n^2) with slight optimization:
    - Iterate j from i-1 down to 0 (larger elements more likely to divide)
    - O(n log n) is NOT possible because divisibility doesn't maintain
      the sorted-tails property needed for binary search.
    """
    nums.sort()
    n = len(nums)
    if n == 0:
        return []

    dp = [1] * n
    parent = [-1] * n

    max_len, max_idx = 1, 0

    for i in range(1, n):
        for j in range(i - 1, -1, -1):
            if nums[i] % nums[j] == 0 and dp[j] + 1 > dp[i]:
                dp[i] = dp[j] + 1
                parent[i] = j
        if dp[i] > max_len:
            max_len = dp[i]
            max_idx = i

    result = []
    idx = max_idx
    while idx != -1:
        result.append(nums[idx])
        idx = parent[idx]
    return result[::-1]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Largest Divisible Subset (LeetCode #368) ===\n")

    test_cases = [
        ([1, 2, 3], 2),
        ([1, 2, 4, 8], 4),
        ([3, 4, 16, 8], 3),
        ([1], 1),
        ([2, 3, 5, 7, 11], 1),
        ([4, 8, 10, 240], 3),
        ([1, 2, 4, 8, 16, 32], 6),
    ]

    header = f"{'nums':<25} {'ExpSz':<6} {'Rec':<20} {'Tab':<20} {'Opt':<20} {'Pass':<6}"
    print(header)
    print("-" * len(header))

    for nums, exp_size in test_cases:
        r = lds_recursive(nums[:])
        t = lds_tab(nums[:])
        o = lds_optimized(nums[:])

        passes = len(r) == exp_size and len(t) == exp_size and len(o) == exp_size
        print(f"{str(nums):<25} {exp_size:<6} {str(r):<20} {str(t):<20} {str(o):<20} {passes}")

    # Verify divisibility
    print("\n--- Verification ---")
    result = lds_tab([1, 2, 4, 8, 16, 32])
    print(f"Subset: {result}")
    valid = all(
        max(a, b) % min(a, b) == 0
        for i, a in enumerate(result)
        for b in result[i + 1:]
    )
    print(f"All pairs divisible: {valid}")

    # Show dp array evolution
    print("\n--- DP Table: [3, 4, 16, 8] -> sorted [3, 4, 8, 16] ---")
    nums = [3, 4, 8, 16]  # already sorted
    dp = [1] * 4
    parent = [-1] * 4
    for i in range(1, 4):
        for j in range(i):
            if nums[i] % nums[j] == 0 and dp[j] + 1 > dp[i]:
                dp[i] = dp[j] + 1
                parent[i] = j
        print(f"  i={i} ({nums[i]}): dp={dp[i]}, parent={parent[i]} -> checks: " +
              ", ".join(f"{nums[i]}%{nums[j]}={'=0' if nums[i] % nums[j] == 0 else '!=0'}"
                        for j in range(i)))
    print(f"  dp = {dp}, parent = {parent}")
    print(f"  Backtrack from idx 3: {nums[3]} -> {nums[parent[3]]} -> {nums[parent[parent[3]]]}")
