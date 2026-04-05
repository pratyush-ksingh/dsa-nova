"""
Problem: Asteroid Collision (LeetCode 735)
Difficulty: MEDIUM | XP: 25

We are given an array of integers representing asteroids in a row.
For each asteroid, the absolute value represents its size, and the sign
represents its direction (positive = right, negative = left).

Each asteroid moves at the same speed. Find out the state of the asteroids
after all collisions. If two asteroids meet, the smaller one explodes. If
both are the same size, both explode. Two asteroids moving in the same
direction never meet.
"""
from typing import List
import copy


# ============================================================
# APPROACH 1: BRUTE FORCE — Simulate with list, repeat until stable
# Time: O(n^2)  |  Space: O(n)
# ============================================================
def brute_force(asteroids: List[int]) -> List[int]:
    """
    Repeatedly scan the list for a collision (positive followed by negative).
    Resolve it, then re-scan from the beginning. Repeat until no collision found.
    Inefficient but easy to verify correctness.
    """
    asteroids = list(asteroids)
    changed = True
    while changed:
        changed = False
        i = 0
        while i < len(asteroids) - 1:
            left, right = asteroids[i], asteroids[i + 1]
            # Collision: left moves right (>0), right moves left (<0)
            if left > 0 and right < 0:
                changed = True
                if abs(left) > abs(right):
                    asteroids.pop(i + 1)   # right destroyed
                elif abs(left) < abs(right):
                    asteroids.pop(i)       # left destroyed
                else:
                    asteroids.pop(i + 1)   # both destroyed
                    asteroids.pop(i)
                # Don't advance i; re-check from current position
            else:
                i += 1
    return asteroids


# ============================================================
# APPROACH 2: OPTIMAL — Single-pass stack simulation
# Time: O(n)  |  Space: O(n)
# ============================================================
def optimal(asteroids: List[int]) -> List[int]:
    """
    Use a stack. For each asteroid:
    - If it moves right (>0) or the stack is empty or the stack top moves left (<0):
      push it (no collision possible).
    - If it moves left (<0): resolve collisions with the stack top:
        - If top is also negative: push (same direction, no collision).
        - If top is smaller: destroy top (pop), continue checking.
        - If top is equal size: destroy both (pop, skip current).
        - If top is larger: current asteroid is destroyed (skip).
    """
    stack = []
    for asteroid in asteroids:
        alive = True
        while alive and asteroid < 0 and stack and stack[-1] > 0:
            if stack[-1] < -asteroid:
                stack.pop()          # stack top is smaller, destroyed
            elif stack[-1] == -asteroid:
                stack.pop()          # both same size, both destroyed
                alive = False
            else:
                alive = False        # stack top is larger, current destroyed
        if alive:
            stack.append(asteroid)
    return stack


# ============================================================
# APPROACH 3: BEST — Same stack, slightly reorganized for clarity
# Time: O(n)  |  Space: O(n)
# ============================================================
def best(asteroids: List[int]) -> List[int]:
    """
    Functionally identical to Approach 2. Reorganized with a helper boolean
    and explicit collision condition check for readability.

    Collision condition: stack top > 0 AND current < 0 (they move toward each other).
    """
    stack = []
    for asteroid in asteroids:
        # Process current asteroid against the top of the stack
        while stack and stack[-1] > 0 and asteroid < 0:
            top = stack[-1]
            if top < -asteroid:
                stack.pop()      # top explodes; current continues
                continue
            elif top == -asteroid:
                stack.pop()      # both explode
            # else: current explodes (top >= -asteroid)
            break                # current is dead in both remaining cases
        else:
            # No collision (while condition was never true, or loop exhausted stack)
            stack.append(asteroid)

    return stack


if __name__ == "__main__":
    print("=== Asteroid Collision ===")
    tests = [
        [5, 10, -5],          # [5, 10]
        [8, -8],              # []
        [10, 2, -5],          # [10]
        [-2, -1, 1, 2],       # [-2, -1, 1, 2]
        [1, -1, -2],          # [-2]
        [-1, 9, -9],          # [-1]
    ]
    for t in tests:
        b = brute_force(t[:])
        o = optimal(t[:])
        be = best(t[:])
        print(f"Input: {str(t):25}  Brute: {str(b):20}  Optimal: {str(o):20}  Best: {str(be):20}  Match: {b==o==be}")
