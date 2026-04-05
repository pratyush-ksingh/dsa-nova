"""
Greedy Algorithm to Find Minimum Coins

Indian denomination system: {1, 2, 5, 10, 20, 50, 100, 500, 2000}
Find minimum coins/notes for a given amount.
"""
from typing import List

DENOMINATIONS = [2000, 500, 100, 50, 20, 10, 5, 2, 1]


# ============================================================
# APPROACH 1: BRUTE FORCE -- Recursive enumeration
# Time: O(V^D) worst  |  Space: O(D)
# ============================================================
def brute_force(V: int) -> List[int]:
    if V == 0:
        return []

    def helper(remaining, idx):
        if remaining == 0:
            return []
        if idx >= len(DENOMINATIONS):
            return None

        denom = DENOMINATIONS[idx]
        max_count = remaining // denom
        best = None

        for count in range(max_count, -1, -1):
            sub = helper(remaining - count * denom, idx + 1)
            if sub is not None:
                candidate = [denom] * count + sub
                if best is None or len(candidate) < len(best):
                    best = candidate

        return best

    result = helper(V, 0)
    return result if result else []


# ============================================================
# APPROACH 2: OPTIMAL -- DP (general coin change)
# Time: O(V * D)  |  Space: O(V)
# ============================================================
def optimal(V: int) -> List[int]:
    if V == 0:
        return []

    dp = [float('inf')] * (V + 1)
    parent = [-1] * (V + 1)
    dp[0] = 0

    for i in range(1, V + 1):
        for d in DENOMINATIONS:
            if d <= i and dp[i - d] + 1 < dp[i]:
                dp[i] = dp[i - d] + 1
                parent[i] = d

    # Reconstruct
    result = []
    amount = V
    while amount > 0 and parent[amount] != -1:
        result.append(parent[amount])
        amount -= parent[amount]
    return result


# ============================================================
# APPROACH 3: BEST -- Greedy largest-first
# Time: O(D)  |  Space: O(D) for result
# ============================================================
def best(V: int) -> List[int]:
    result = []

    for denom in DENOMINATIONS:
        while V >= denom:
            result.append(denom)
            V -= denom

    return result


# ============================================================
# MAIN
# ============================================================
if __name__ == "__main__":
    test_amounts = [49, 93, 2000, 1, 0]

    print("=== Minimum Coins (Indian Denominations) ===")
    for V in test_amounts:
        res = best(V)
        print(f"V={V:4d} -> {len(res)} coins: {res}")

    print("\n--- Verification (all approaches for V=49) ---")
    print(f"Brute:   {brute_force(49)}")
    print(f"Optimal: {optimal(49)}")
    print(f"Best:    {best(49)}")
    # V=  49 -> 5 coins: [20, 20, 5, 2, 2]
    # V=  93 -> 5 coins: [50, 20, 20, 2, 1]
    # V=2000 -> 1 coins: [2000]
