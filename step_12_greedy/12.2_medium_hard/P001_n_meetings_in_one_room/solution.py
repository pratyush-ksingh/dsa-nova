"""
Problem: N Meetings in One Room (Activity Selection)
Difficulty: EASY | XP: 10
"""
from typing import List, Tuple
from itertools import combinations


# ============================================================
# APPROACH 1: BRUTE FORCE -- All subsets
# Time: O(2^n * n)  |  Space: O(n)
# ============================================================
def brute_force(start: List[int], end: List[int]) -> int:
    n = len(start)
    max_count = 0
    for size in range(n, 0, -1):
        for combo in combinations(range(n), size):
            meetings = sorted([(start[i], end[i]) for i in combo])
            valid = True
            for i in range(1, len(meetings)):
                if meetings[i][0] < meetings[i - 1][1]:
                    valid = False
                    break
            if valid:
                return size  # since we try largest size first
    return 0


# ============================================================
# APPROACH 2 & 3: OPTIMAL -- Sort by end time + Greedy
# Time: O(n log n)  |  Space: O(n)
# ============================================================
def max_meetings(start: List[int], end: List[int]) -> int:
    n = len(start)
    # Sort meetings by end time, then start time
    meetings = sorted(range(n), key=lambda i: (end[i], start[i]))

    count = 0
    last_end = -1
    selected = []

    for idx in meetings:
        if start[idx] >= last_end:
            count += 1
            last_end = end[idx]
            selected.append(idx + 1)  # 1-indexed

    print(f"  Selected meetings (1-indexed): {selected}")
    return count


if __name__ == "__main__":
    print("=== N Meetings in One Room ===\n")

    tests = [
        ([1, 3, 0, 5, 8, 5], [2, 4, 6, 7, 9, 9], 4),
        ([1, 1, 1], [2, 3, 4], 1),
        ([1, 3, 5], [2, 4, 6], 3),
        ([5], [10], 1),
    ]

    for s, e, expected in tests:
        print(f"start={s}, end={e}")
        result = max_meetings(s, e)
        status = "PASS" if result == expected else "FAIL"
        print(f"  Result: {result} (expected {expected}) {status}\n")

    # Brute force verification
    print("--- Brute Force Verification ---")
    print(f"Brute (test1): {brute_force([1,3,0,5,8,5], [2,4,6,7,9,9])}")  # 4
    print(f"Brute (test2): {brute_force([1,1,1], [2,3,4])}")  # 1
