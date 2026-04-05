"""
Problem: Red Zone
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a 1D array where 1 = infected cell and 0 = healthy cell.
Each second, infection spreads to adjacent cells.
Find the time when ALL cells become infected.

Real-world analogy: fire spreading, virus propagation on a 1D line.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE — Simulation step by step
# Time: O(n * t)  |  Space: O(n)
# Simulate second by second until all cells are infected.
# ============================================================
def brute_force(A: List[int]) -> int:
    state = A[:]
    time = 0
    while 0 in state:
        next_state = state[:]
        for i in range(len(state)):
            if state[i] == 1:
                if i > 0:
                    next_state[i - 1] = 1
                if i < len(state) - 1:
                    next_state[i + 1] = 1
        state = next_state
        time += 1
    return time


# ============================================================
# APPROACH 2: OPTIMAL — Multi-Source BFS
# Time: O(n)  |  Space: O(n)
# Start BFS from all initially infected cells simultaneously.
# Each cell is visited once; track max distance.
# ============================================================
def optimal(A: List[int]) -> int:
    n = len(A)
    dist = [-1] * n
    q = deque()

    for i in range(n):
        if A[i] == 1:
            dist[i] = 0
            q.append(i)

    max_time = 0
    while q:
        cur = q.popleft()
        for nb in (cur - 1, cur + 1):
            if 0 <= nb < n and dist[nb] == -1:
                dist[nb] = dist[cur] + 1
                max_time = max(max_time, dist[nb])
                q.append(nb)

    return max_time


# ============================================================
# APPROACH 3: BEST — Two-Pass Gap Analysis
# Time: O(n)  |  Space: O(n)
# Forward pass: dist from nearest infected to the left.
# Backward pass: dist from nearest infected to the right.
# Infection time for cell i = min(fwd[i], bwd[i]).
# Answer = max over all uninfected cells.
# ============================================================
def best(A: List[int]) -> int:
    n = len(A)
    INF = float('inf')
    fwd = [INF] * n
    bwd = [INF] * n

    # Forward pass
    d = INF
    for i in range(n):
        if A[i] == 1:
            d = 0
        elif d < INF:
            d += 1
        fwd[i] = d

    # Backward pass
    d = INF
    for i in range(n - 1, -1, -1):
        if A[i] == 1:
            d = 0
        elif d < INF:
            d += 1
        bwd[i] = d

    max_time = 0
    for i in range(n):
        if A[i] == 0:
            max_time = max(max_time, min(fwd[i], bwd[i]))

    return max_time


if __name__ == "__main__":
    print("=== Red Zone ===")
    tests = [
        ([0, 1, 0, 0, 0, 1, 0], 2),
        ([1, 0, 0, 0, 0, 0, 1], 3),
        ([1, 1, 1],              0),
        ([0, 0, 0, 1],           3),
        ([1, 0, 0, 0],           3),
    ]
    for A, expected in tests:
        bf = brute_force(A[:])
        op = optimal(A[:])
        be = best(A[:])
        ok = all(x == expected for x in [bf, op, be])
        print(f"A={A} -> Brute={bf}, Optimal={op}, Best={be} | Expected={expected} {'OK' if ok else 'FAIL'}")
