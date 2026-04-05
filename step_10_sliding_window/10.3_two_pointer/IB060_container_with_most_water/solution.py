"""
Problem: Container With Most Water (LeetCode 11)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given n non-negative integers height[0..n-1] where each represents a
vertical line at position i with height height[i], find two lines that
together with the x-axis form a container that holds the most water.
Return the maximum amount of water.

Water held = min(height[left], height[right]) * (right - left)
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n²)  |  Space: O(1)
# Try every pair (i, j) and compute the water, track the maximum.
# ============================================================
def brute_force(height: List[int]) -> int:
    """
    Enumerate all O(n^2) pairs. For each pair (i, j) with i < j,
    area = min(height[i], height[j]) * (j - i).
    Return the global maximum.
    """
    n = len(height)
    max_water = 0
    for i in range(n):
        for j in range(i + 1, n):
            water = min(height[i], height[j]) * (j - i)
            if water > max_water:
                max_water = water
    return max_water


# ============================================================
# APPROACH 2: OPTIMAL  (two pointers from both ends)
# Time: O(n)  |  Space: O(1)
# Start with the widest possible container (left=0, right=n-1).
# The limiting factor is the shorter line; moving the taller line
# inward can only decrease width without guaranteeing a taller
# replacement — so always move the pointer at the SHORTER line.
# ============================================================
def optimal(height: List[int]) -> int:
    """
    Two-pointer greedy:
    - left starts at index 0, right starts at index n-1.
    - Compute area = min(h[left], h[right]) * (right - left).
    - Move the pointer with the smaller height inward.
      Rationale: keeping the taller line and finding a potentially
      taller replacement can only increase (or maintain) the area.
    - Continue until pointers meet.

    Why this works: The optimal pair is never missed because when
    we discard height[i], we've already computed all containers
    where i is the shorter wall — none of them can beat moving on.
    """
    left, right = 0, len(height) - 1
    max_water = 0

    while left < right:
        h = min(height[left], height[right])
        area = h * (right - left)
        if area > max_water:
            max_water = area

        if height[left] <= height[right]:
            left += 1
        else:
            right -= 1

    return max_water


# ============================================================
# APPROACH 3: BEST  (same two pointers, with skip optimisation)
# Time: O(n)  |  Space: O(1)
# Extend Approach 2 by skipping over consecutive shorter lines:
# after moving a pointer, skip all lines that are not taller than
# the line we just left behind — they cannot possibly improve area
# since they are shorter AND closer together.
# ============================================================
def best(height: List[int]) -> int:
    """
    Same two-pointer approach as Approach 2, but with an inner
    skip loop: after choosing which side to move, keep advancing
    that pointer while it points to a line no taller than the
    current boundary height. This prunes redundant computations
    and runs faster in practice (same worst-case O(n)).
    """
    left, right = 0, len(height) - 1
    max_water = 0

    while left < right:
        hl, hr = height[left], height[right]
        area = min(hl, hr) * (right - left)
        if area > max_water:
            max_water = area

        if hl <= hr:
            # Skip all left lines that are not taller than hl
            while left < right and height[left] <= hl:
                left += 1
        else:
            # Skip all right lines that are not taller than hr
            while left < right and height[right] <= hr:
                right -= 1

    return max_water


if __name__ == "__main__":
    test_cases = [
        ([1, 8, 6, 2, 5, 4, 8, 3, 7], 49),
        ([1, 1],                        1),
        ([4, 3, 2, 1, 4],              16),
        ([1, 2, 1],                     2),
        ([2, 3, 4, 5, 18, 17, 6],      17),
    ]

    print("=== Container With Most Water ===\n")
    for height, expected in test_cases:
        b   = brute_force(height)
        o   = optimal(height)
        bst = best(height)
        status = "PASS" if b == o == bst == expected else "FAIL"
        print(f"[{status}] height={height}")
        print(f"       Brute:   {b}")
        print(f"       Optimal: {o}")
        print(f"       Best:    {bst}")
        print(f"       Expect:  {expected}\n")
