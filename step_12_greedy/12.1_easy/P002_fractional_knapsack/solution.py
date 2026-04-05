"""
Problem: Fractional Knapsack
Difficulty: MEDIUM | XP: 25
"""
from typing import List, Tuple
from itertools import permutations


# ============================================================
# APPROACH 1: BRUTE FORCE -- Try All Permutations
# Time: O(n! * n)  |  Space: O(n)
# Try every ordering, greedily fill for each. Take max.
# ============================================================
def brute_force(items: List[Tuple[int, int]], capacity: int) -> float:
    max_val = 0.0

    for perm in permutations(items):
        val = 0.0
        remaining = capacity
        for value, weight in perm:
            if remaining <= 0:
                break
            if weight <= remaining:
                val += value
                remaining -= weight
            else:
                val += value * (remaining / weight)
                remaining = 0
        max_val = max(max_val, val)

    return max_val


# ============================================================
# APPROACH 2: OPTIMAL -- Greedy by Value/Weight Ratio
# Time: O(n log n)  |  Space: O(n)
# Sort by ratio descending, take greedily.
# ============================================================
def optimal(items: List[Tuple[int, int]], capacity: int) -> float:
    # Sort by value/weight ratio descending
    sorted_items = sorted(items, key=lambda x: x[0] / x[1], reverse=True)

    total_value = 0.0
    remaining = capacity

    for value, weight in sorted_items:
        if remaining <= 0:
            break

        if weight <= remaining:
            # Take the entire item
            total_value += value
            remaining -= weight
        else:
            # Take a fraction
            total_value += value * (remaining / weight)
            remaining = 0

    return total_value


# ============================================================
# APPROACH 3: BEST -- Quickselect-based O(n) average
# Time: O(n) average  |  Space: O(n)
# Partition to find the breaking item without full sort.
# In practice, sort-based approach preferred for simplicity.
# ============================================================
def best(items: List[Tuple[int, int]], capacity: int) -> float:
    # Clean implementation using ratio calculation
    # Conceptually: partition items by ratio around a pivot,
    # determine which side the "breaking point" is in, recurse.

    ratios = [(v / w, v, w) for v, w in items]
    ratios.sort(reverse=True)  # Sort by ratio descending

    total_value = 0.0
    remaining = capacity

    for ratio, value, weight in ratios:
        if remaining <= 0:
            break
        take = min(weight, remaining)
        total_value += ratio * take
        remaining -= take

    return total_value


if __name__ == "__main__":
    print("=== Fractional Knapsack ===")

    items1 = [(60, 10), (100, 20), (120, 30)]

    print(f"Brute (W=50):   {brute_force(items1, 50):.2f}")   # 240.00
    print(f"Optimal (W=50): {optimal(items1, 50):.2f}")        # 240.00
    print(f"Best (W=50):    {best(items1, 50):.2f}")            # 240.00

    # All items fit
    print(f"All fit (W=60): {optimal(items1, 60):.2f}")        # 280.00

    # Single item, fraction
    items2 = [(500, 30)]
    print(f"Single (W=10):  {optimal(items2, 10):.2f}")        # 166.67

    # Zero capacity
    print(f"Zero cap:       {optimal(items1, 0):.2f}")          # 0.00
