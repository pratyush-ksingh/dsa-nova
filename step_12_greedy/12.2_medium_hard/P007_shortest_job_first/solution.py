"""
Problem: Shortest Job First (SJF) Scheduling
Difficulty: EASY | XP: 10

Given burst times of n processes (all arrive at time 0),
compute the average waiting time using SJF (non-preemptive).
Sort by burst time, compute prefix sum of waits.
"""
from typing import List


# ============================================================
# SORT + PREFIX SUM (Optimal)
# Time: O(n log n) | Space: O(1) with in-place sort
# ============================================================
def average_waiting_time(burst: List[int]) -> float:
    burst.sort()
    n = len(burst)

    total_wait = 0
    current_time = 0

    for i in range(n):
        total_wait += current_time    # process i waits for all previous jobs
        current_time += burst[i]      # process i now runs

    return total_wait / n


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Shortest Job First ===\n")

    test_cases = [
        ([4, 3, 7, 1, 2], 4.0),
        ([1, 2, 3], 1.0),
        ([5, 5, 5], 5.0),
        ([10], 0.0),
        ([2, 1], 1.0),
        ([6, 2, 8, 3, 1], 3.6),
    ]

    for burst, expected in test_cases:
        result = average_waiting_time(burst[:])
        passes = abs(result - expected) < 0.01
        print(f"burst = {burst}")
        print(f"  Result: {result:.2f} | Expected: {expected:.2f} | Pass: {passes}\n")
