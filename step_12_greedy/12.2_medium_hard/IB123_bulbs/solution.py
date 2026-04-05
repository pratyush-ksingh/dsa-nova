"""Problem: Bulbs
Difficulty: EASY | XP: 10
Source: InterviewBit

N bulbs in a row, each on (1) or off (0). Flipping switch i toggles all bulbs from i to N.
Find minimum number of switch flips to turn all bulbs on.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(N^2)  |  Space: O(N)
# Simulate left to right: if bulb off, flip all from i..N
# ============================================================
def brute_force(bulbs: List[int]) -> int:
    state = bulbs[:]
    flips = 0
    for i in range(len(state)):
        if state[i] == 0:
            for j in range(i, len(state)):
                state[j] ^= 1
            flips += 1
    return flips


# ============================================================
# APPROACH 2: OPTIMAL - Greedy with parity tracking
# Time: O(N)  |  Space: O(1)
# Track parity of flips done so far. An odd parity inverts the current bulb.
# If effective state is 0 (off), must flip.
# ============================================================
def optimal(bulbs: List[int]) -> int:
    flips = 0
    for b in bulbs:
        effective = b ^ (flips & 1)
        if effective == 0:
            flips += 1
    return flips


# ============================================================
# APPROACH 3: BEST - Same logic, explicit comment
# Time: O(N)  |  Space: O(1)
# Real-life: light switch panels where flipping one switch
# affects all downstream switches (cascade toggle).
# ============================================================
def best(bulbs: List[int]) -> int:
    flip_count = 0
    for b in bulbs:
        # XOR with parity gives current effective state
        if (b ^ (flip_count & 1)) == 0:
            flip_count += 1
    return flip_count


if __name__ == "__main__":
    tests = [
        ([0, 1, 0, 1], 4),
        ([1, 1, 0, 1], 2),
        ([1, 1, 1], 0),
        ([0], 1),
        ([1], 0),
        ([0, 0], 1),   # flip at 0: [1,1] -> done in 1 flip
    ]
    for bulbs, expected in tests:
        bf = brute_force(bulbs)
        opt = optimal(bulbs)
        be = best(bulbs)
        status = "OK" if bf == opt == be == expected else "FAIL"
        print(f"[{status}] bulbs={bulbs} -> Brute={bf}, Optimal={opt}, Best={be} (expected={expected})")
