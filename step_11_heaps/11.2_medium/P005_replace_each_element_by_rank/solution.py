"""
Problem: Replace Each Element by Rank
Difficulty: MEDIUM | XP: 25

Replace each element in the array with its rank (1-indexed).
Rank is determined by value: smallest element gets rank 1.
Equal elements get the same rank.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²)  |  Space: O(1) extra (output array O(n))
# ============================================================
def brute_force(arr: List[int]) -> List[int]:
    """
    For each element, count how many distinct values are strictly smaller.
    rank(x) = (number of distinct values < x) + 1
    """
    n = len(arr)
    result = []
    for i in range(n):
        smaller = set()
        for j in range(n):
            if arr[j] < arr[i]:
                smaller.add(arr[j])
        result.append(len(smaller) + 1)
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def optimal(arr: List[int]) -> List[int]:
    """
    Sort a copy with original indices to determine ranks.
    Assign rank 1 to the smallest; increment rank only when value changes.
    Map value -> rank in a dictionary, then build result.
    """
    n = len(arr)
    # pair (value, original_index), sort by value
    sorted_pairs = sorted(enumerate(arr), key=lambda x: x[1])

    rank_map = {}
    rank = 1
    for i, (orig_idx, val) in enumerate(sorted_pairs):
        if i > 0 and val != sorted_pairs[i - 1][1]:
            rank += 1
        rank_map[val] = rank

    return [rank_map[x] for x in arr]


# ============================================================
# APPROACH 3: BEST
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def best(arr: List[int]) -> List[int]:
    """
    Same O(n log n) approach but slightly cleaner:
    use sorted(set(arr)) to get unique sorted values,
    then build rank_map with enumerate starting at 1.
    Single pass to build result.
    """
    rank_map = {val: rank for rank, val in enumerate(sorted(set(arr)), start=1)}
    return [rank_map[x] for x in arr]


if __name__ == "__main__":
    print("=== Replace Each Element by Rank ===")
    test1 = [100, 37, 15, 1, 900, 2, 2]
    test2 = [40, 10, 20, 30]
    test3 = [5, 5, 5]

    print(f"Input:   {test1}")
    print(f"Brute:   {brute_force(test1)}")
    print(f"Optimal: {optimal(test1)}")
    print(f"Best:    {best(test1)}")
    print()
    print(f"Input:   {test2}")
    print(f"Best:    {best(test2)}")
    print()
    print(f"Input:   {test3}")
    print(f"Best:    {best(test3)}")
