"""
Problem: Count Subsets with Sum K
Difficulty: MEDIUM | XP: 25

Count number of subsets that sum to K. Handle zeros carefully.
All 4 DP approaches.
"""


# ============================================================
# APPROACH 1: PLAIN RECURSION
# Time: O(2^n) | Space: O(n) stack
# ============================================================
def count_recursive(arr: list[int], k: int) -> int:
    """Try take/not-take for each element. Count paths reaching sum=0."""
    n = len(arr)

    def solve(idx: int, target: int) -> int:
        if idx == n:
            return 1 if target == 0 else 0

        not_take = solve(idx + 1, target)
        take = 0
        if arr[idx] <= target:
            take = solve(idx + 1, target - arr[idx])

        return take + not_take

    return solve(0, k)


# ============================================================
# APPROACH 2: MEMOIZATION (Top-Down DP)
# Time: O(n * k) | Space: O(n * k)
# ============================================================
def count_memo(arr: list[int], k: int) -> int:
    """Cache (idx, target) states."""
    n = len(arr)
    cache = {}

    def solve(idx: int, target: int) -> int:
        if idx == n:
            return 1 if target == 0 else 0
        if (idx, target) in cache:
            return cache[(idx, target)]

        not_take = solve(idx + 1, target)
        take = 0
        if arr[idx] <= target:
            take = solve(idx + 1, target - arr[idx])

        cache[(idx, target)] = take + not_take
        return cache[(idx, target)]

    return solve(0, k)


# ============================================================
# APPROACH 3: TABULATION (Bottom-Up DP)
# Time: O(n * k) | Space: O(n * k)
# ============================================================
def count_tab(arr: list[int], k: int) -> int:
    """dp[i][j] = number of subsets using first i elements summing to j."""
    n = len(arr)
    dp = [[0] * (k + 1) for _ in range(n + 1)]

    # Base case: empty subset sums to 0
    dp[0][0] = 1

    for i in range(1, n + 1):
        for j in range(0, k + 1):
            # Not take
            dp[i][j] = dp[i - 1][j]
            # Take (if element fits)
            if arr[i - 1] <= j:
                dp[i][j] += dp[i - 1][j - arr[i - 1]]

    return dp[n][k]


# ============================================================
# APPROACH 4: SPACE OPTIMIZED (1D array, right-to-left)
# Time: O(n * k) | Space: O(k)
# ============================================================
def count_space(arr: list[int], k: int) -> int:
    """
    Single 1D array. Right-to-left for 0/1 knapsack.
    Special handling for zeros: each zero doubles all counts.
    """
    dp = [0] * (k + 1)
    dp[0] = 1  # empty subset sums to 0

    for num in arr:
        if num == 0:
            # Zero element: every existing subset count doubles
            # (can include or exclude the zero without changing sum)
            for j in range(k, -1, -1):
                dp[j] *= 2
        else:
            # Standard 0/1 knapsack: right-to-left
            for j in range(k, num - 1, -1):
                dp[j] += dp[j - num]

    return dp[k]


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Count Subsets with Sum K ===\n")

    test_cases = [
        ([1, 2, 3], 3, 2),
        ([1, 1, 1], 2, 3),
        ([0, 1, 2, 3], 3, 4),
        ([0, 0, 1], 1, 4),
        ([5, 2, 3, 10, 6, 8], 10, 3),
        ([0, 0, 0], 0, 8),
        ([3], 5, 0),
    ]

    header = f"{'Array':<25} {'k':<4} {'Rec':<8} {'Memo':<8} {'Tab':<8} {'Space':<8} {'Exp':<8} {'Pass':<6}"
    print(header)
    print("-" * len(header))

    for arr, k, expected in test_cases:
        r = count_recursive(arr, k)
        m = count_memo(arr, k)
        t = count_tab(arr, k)
        s = count_space(arr, k)

        passes = r == expected and m == expected and t == expected and s == expected
        print(f"{str(arr):<25} {k:<4} {r:<8} {m:<8} {t:<8} {s:<8} {expected:<8} {passes}")

    # Zero handling demo
    print("\n--- Zero Handling Demo ---")
    print("arr = [0, 0, 1], k = 1")
    print("Subsets summing to 1:")
    print("  {1}         (no zeros)")
    print("  {0, 1}      (first zero)")
    print("  {0, 1}      (second zero)")
    print("  {0, 0, 1}   (both zeros)")
    print(f"Count = {count_tab([0, 0, 1], 1)} (2^2 = 4 multiplier from 2 zeros)")

    # DP table visualization
    print("\n--- DP Table: arr=[1,2,3], k=3 ---")
    arr = [1, 2, 3]
    k = 3
    dp = [[0] * (k + 1) for _ in range(len(arr) + 1)]
    dp[0][0] = 1
    for i in range(1, len(arr) + 1):
        for j in range(k + 1):
            dp[i][j] = dp[i - 1][j]
            if arr[i - 1] <= j:
                dp[i][j] += dp[i - 1][j - arr[i - 1]]

    print("       j=0  j=1  j=2  j=3")
    labels = ["i=0(-)", "i=1(1)", "i=2(2)", "i=3(3)"]
    for i in range(len(arr) + 1):
        print(f"{labels[i]:<8} {dp[i][0]:>2}   {dp[i][1]:>2}   {dp[i][2]:>2}   {dp[i][3]:>2}")

    # Edge: target = 0
    print(f"\narr=[0,0,0], k=0 -> {count_space([0, 0, 0], 0)} (all 2^3=8 subsets sum to 0)")
    print(f"arr=[1,2,3], k=0 -> {count_space([1, 2, 3], 0)} (only empty subset)")
