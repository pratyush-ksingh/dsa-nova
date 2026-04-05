"""
Problem: Trapping Rain Water
Difficulty: HARD | XP: 50

Given n non-negative integers representing an elevation map (each bar width=1),
compute how much water can be trapped after raining.

Water at index i = min(max_left[i], max_right[i]) - height[i]
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Scan left and right for every index
# Time: O(n^2)  |  Space: O(1)
# For each position, find max to its left and right by linear scan
# ============================================================
def brute_force(height: List[int]) -> int:
    n = len(height)
    water = 0
    for i in range(1, n - 1):
        max_left  = max(height[:i + 1])
        max_right = max(height[i:])
        water += min(max_left, max_right) - height[i]
    return water


# ============================================================
# APPROACH 2: OPTIMAL - Prefix/suffix max arrays
# Time: O(n)  |  Space: O(n)
# Precompute leftMax and rightMax in two passes; single pass for water
# ============================================================
def optimal(height: List[int]) -> int:
    n = len(height)
    if n == 0:
        return 0
    left_max  = [0] * n
    right_max = [0] * n
    left_max[0]    = height[0]
    right_max[n-1] = height[n-1]
    for i in range(1, n):
        left_max[i]  = max(left_max[i-1],  height[i])
    for i in range(n-2, -1, -1):
        right_max[i] = max(right_max[i+1], height[i])
    return sum(min(left_max[i], right_max[i]) - height[i] for i in range(n))


# ============================================================
# APPROACH 3: BEST - Two pointers, O(1) space
# Time: O(n)  |  Space: O(1)
# Move the pointer with the smaller running max; safe to compute water for it
# ============================================================
def best(height: List[int]) -> int:
    left, right = 0, len(height) - 1
    max_left = max_right = water = 0
    while left < right:
        if height[left] <= height[right]:
            if height[left] >= max_left:
                max_left = height[left]
            else:
                water += max_left - height[left]
            left += 1
        else:
            if height[right] >= max_right:
                max_right = height[right]
            else:
                water += max_right - height[right]
            right -= 1
    return water


if __name__ == "__main__":
    print("=== Trapping Rain Water ===")

    h1 = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
    print(f"height = {h1}")
    print(f"Brute:   {brute_force(h1)}")   # 6
    print(f"Optimal: {optimal(h1)}")        # 6
    print(f"Best:    {best(h1)}")           # 6

    h2 = [4, 2, 0, 3, 2, 5]
    print(f"\nheight = {h2}")
    print(f"Brute:   {brute_force(h2)}")   # 9
    print(f"Optimal: {optimal(h2)}")
    print(f"Best:    {best(h2)}")

    h3 = [3, 0, 2, 0, 4]
    print(f"\nheight = {h3}")
    print(f"Brute:   {brute_force(h3)}")   # 7
    print(f"Optimal: {optimal(h3)}")
    print(f"Best:    {best(h3)}")
