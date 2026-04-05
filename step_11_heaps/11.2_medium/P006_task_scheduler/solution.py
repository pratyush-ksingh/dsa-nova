"""
Problem: Task Scheduler (LeetCode 621)
Difficulty: MEDIUM | XP: 25

Given a list of CPU tasks (A-Z) and a cooldown n, find the minimum number
of intervals (units of time) needed to finish all tasks. The CPU must wait
at least n intervals between two same tasks. It can be idle.
"""
from typing import List
from collections import Counter, deque
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE — Simulation with priority queue + cooldown queue
# Time: O(T * 26) where T = total intervals  |  Space: O(26)
# ============================================================
def brute_force(tasks: List[str], n: int) -> int:
    """
    Simulate the CPU scheduler step by step:
    - Use a max-heap of (-count, task_char) for always picking the most frequent.
    - Use a cooldown queue of (available_time, count, task_char).
    - At each time unit: pick the most frequent available task (if any),
      put it on cooldown, advance time.
    """
    freq = Counter(tasks)
    # max-heap: negate counts for Python's min-heap
    heap = [(-cnt, ch) for ch, cnt in freq.items()]
    heapq.heapify(heap)

    cooldown_q = deque()  # (available_at_time, count, char)
    time = 0

    while heap or cooldown_q:
        time += 1

        # Release tasks whose cooldown has expired
        if cooldown_q and cooldown_q[0][0] <= time:
            _, cnt, ch = cooldown_q.popleft()
            heapq.heappush(heap, (cnt, ch))

        if heap:
            cnt, ch = heapq.heappop(heap)
            cnt += 1  # cnt is negative; increment toward 0
            if cnt < 0:
                # Still has remaining executions; put on cooldown
                cooldown_q.append((time + n + 1, cnt, ch))
        # else: CPU is idle this interval

    return time


# ============================================================
# APPROACH 2: OPTIMAL — Math formula O(26)
# Time: O(26) ≈ O(1)  |  Space: O(26) ≈ O(1)
# ============================================================
def optimal(tasks: List[str], n: int) -> int:
    """
    Key insight:
    The most frequent task(s) dominate scheduling.

    Imagine slots arranged in "frames" of size (n+1):
      [A _ _ _] [A _ _ _] [A]   (maxFreq-1 full frames + last partial)

    Minimum time = max(
        len(tasks),                                  -- if tasks fill all gaps
        (maxFreq - 1) * (n + 1) + countOfMaxFreq    -- frame-based lower bound
    )

    If tasks are dense enough, they fill all idle slots and the answer
    is just len(tasks). Otherwise, we need idle time to respect cooldown.
    """
    freq = Counter(tasks)
    max_freq = max(freq.values())
    count_of_max = sum(1 for v in freq.values() if v == max_freq)

    # Frame formula
    frame_time = (max_freq - 1) * (n + 1) + count_of_max
    return max(len(tasks), frame_time)


# ============================================================
# APPROACH 3: BEST — Same formula, slightly cleaner with sort
# Time: O(26 log 26) ≈ O(1)  |  Space: O(26) ≈ O(1)
# ============================================================
def best(tasks: List[str], n: int) -> int:
    """
    Sort the frequencies descending. The first element is maxFreq.
    Count how many tasks share that max frequency.
    Apply the same formula as Approach 2.

    Using sorted frequencies makes it easy to count ties at the top.
    """
    counts = sorted(Counter(tasks).values(), reverse=True)
    max_freq = counts[0]
    count_of_max = counts.count(max_freq)

    frame_time = (max_freq - 1) * (n + 1) + count_of_max
    return max(len(tasks), frame_time)


if __name__ == "__main__":
    print("=== Task Scheduler ===")

    tests = [
        (["A", "A", "A", "B", "B", "B"], 2),   # Expected: 8
        (["A", "A", "A", "B", "B", "B"], 0),   # Expected: 6
        (["A", "A", "A", "A", "A", "A", "B", "C", "D", "E", "F", "G"], 2),  # Expected: 16
        (["A", "B", "C", "D", "E", "F"], 2),   # Expected: 6 (no idle needed)
    ]

    for tasks, n in tests:
        print(f"tasks={tasks}, n={n}")
        print(f"  Brute:   {brute_force(tasks, n)}")
        print(f"  Optimal: {optimal(tasks, n)}")
        print(f"  Best:    {best(tasks, n)}")
