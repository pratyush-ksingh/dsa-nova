"""
Problem: Job Sequencing Problem
Difficulty: MEDIUM | XP: 25

Given N jobs, each with a deadline and profit, schedule jobs to maximize
total profit. Each job takes exactly 1 unit of time. A job can only be
scheduled if it is completed before or at its deadline.
Return (number_of_jobs_done, max_profit).
"""
from typing import List, Tuple
from itertools import permutations


# ============================================================
# APPROACH 1: BRUTE FORCE (Try all permutations)
# Time: O(n! * n) | Space: O(n)
# ============================================================
def brute_force(jobs: List[Tuple[int, int, int]]) -> Tuple[int, int]:
    """
    Try every ordering of jobs and greedily schedule each one in its
    earliest available slot. Track the permutation that gives max profit.
    jobs: list of (job_id, deadline, profit)
    Returns: (count_of_jobs_done, max_profit)
    """
    n = len(jobs)
    max_deadline = max(d for _, d, _ in jobs)
    best_profit = 0
    best_count = 0

    for perm in permutations(range(n)):
        slots = [False] * (max_deadline + 1)
        profit = 0
        count = 0
        for idx in perm:
            _, deadline, p = jobs[idx]
            # Try to schedule in earliest available slot up to deadline
            for t in range(1, deadline + 1):
                if not slots[t]:
                    slots[t] = True
                    profit += p
                    count += 1
                    break
        if profit > best_profit:
            best_profit = profit
            best_count = count

    return (best_count, best_profit)


# ============================================================
# APPROACH 2: OPTIMAL (Greedy — sort by profit desc, linear slot scan)
# Time: O(n²) | Space: O(n)
# ============================================================
def optimal(jobs: List[Tuple[int, int, int]]) -> Tuple[int, int]:
    """
    Sort jobs by profit descending. For each job, find the latest
    available time slot <= deadline and assign it there. This greedy
    choice is optimal: always scheduling the highest-profit job first
    guarantees maximum total profit.
    jobs: list of (job_id, deadline, profit)
    Returns: (count_of_jobs_done, max_profit)
    """
    # Sort by profit descending
    sorted_jobs = sorted(jobs, key=lambda x: -x[2])
    max_deadline = max(d for _, d, _ in jobs)

    # slots[i] = True means time slot i is occupied (1-indexed)
    slots = [False] * (max_deadline + 1)
    total_profit = 0
    count = 0

    for _, deadline, profit in sorted_jobs:
        # Find the latest free slot at or before deadline
        for t in range(min(deadline, max_deadline), 0, -1):
            if not slots[t]:
                slots[t] = True
                total_profit += profit
                count += 1
                break

    return (count, total_profit)


# ============================================================
# APPROACH 3: BEST (Greedy + Union-Find for O(α(n)) slot lookup)
# Time: O(n log n + n * α(n)) ≈ O(n log n) | Space: O(n)
# ============================================================
class UnionFind:
    def __init__(self, size: int):
        self.parent = list(range(size + 1))

    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union_to_prev(self, x: int) -> None:
        """Point x to x-1 (the next free slot is now x-1)."""
        self.parent[x] = x - 1


def best(jobs: List[Tuple[int, int, int]]) -> Tuple[int, int]:
    """
    Same greedy strategy (sort by profit desc) but use Union-Find to find
    the latest available slot in near-O(1) amortized time instead of
    scanning linearly.

    Union-Find trick: parent[slot] points to the next free slot at or
    before `slot`. When we use slot t, we union t -> t-1, so future
    find(t) jumps directly to t-1 (the next free slot).
    """
    sorted_jobs = sorted(jobs, key=lambda x: -x[2])
    max_deadline = max(d for _, d, _ in jobs)

    uf = UnionFind(max_deadline)
    total_profit = 0
    count = 0

    for _, deadline, profit in sorted_jobs:
        # Find the latest free slot at or before deadline
        free_slot = uf.find(min(deadline, max_deadline))
        if free_slot > 0:
            total_profit += profit
            count += 1
            uf.union_to_prev(free_slot)  # mark slot as used

    return (count, total_profit)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Job Sequencing Problem ===\n")
    # jobs: (id, deadline, profit)
    test_cases = [
        (
            [(1, 4, 20), (2, 1, 10), (3, 1, 40), (4, 1, 30)],
            (2, 60),   # Job3 at t=1 (40), Job1 at t=4 (20) → 60
        ),
        (
            [(1, 2, 100), (2, 1, 19), (3, 2, 27), (4, 1, 25), (5, 1, 15)],
            (2, 127),  # Job1 at t=2 (100), Job3 at t=1 (27) → 127
        ),
        (
            [(1, 1, 5), (2, 1, 10)],
            (1, 10),   # Only 1 slot, take job with profit 10
        ),
        (
            [(1, 3, 50), (2, 2, 30), (3, 1, 20)],
            (3, 100),  # All 3 fit: slot 3, slot 2, slot 1
        ),
    ]

    for jobs, expected in test_cases:
        # Brute force only feasible for small inputs
        b = brute_force(jobs) if len(jobs) <= 8 else "skipped"
        o = optimal(jobs)
        h = best(jobs)
        status = "PASS" if o == expected and h == expected else "FAIL"
        print(f"Jobs: {[(jid, d, p) for jid, d, p in jobs]}")
        print(f"  Brute:    {b}")
        print(f"  Optimal:  {o}")
        print(f"  Best:     {h}")
        print(f"  Expected: {expected}  [{status}]\n")
