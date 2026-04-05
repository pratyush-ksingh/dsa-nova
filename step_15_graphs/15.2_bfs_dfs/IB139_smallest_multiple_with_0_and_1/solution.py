"""
Problem: Smallest Multiple With 0 and 1
Difficulty: HARD | XP: 50
Source: InterviewBit

Given a positive integer N, find the smallest multiple of N that consists
only of digits 0 and 1. Return as a string.

Key insight: BFS on remainders mod N.
Numbers with only 0/1 digits: 1, 10, 11, 100, 101, ...
BFS builds them in sorted order; remainder state avoids recomputation.

Real-life use: Number theory, digital logic, remainder-based state machines.
"""
from collections import deque
from typing import Optional


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(2^digits)  |  Space: O(2^digits)
# Generate 01-numbers in sorted order via BFS, check divisibility.
# ============================================================
def brute_force(n: int) -> str:
    """BFS on actual numbers (works for small N where no overflow)."""
    if n == 1:
        return "1"
    queue = deque([1])
    while queue:
        num = queue.popleft()
        if num % n == 0:
            return str(num)
        n0 = num * 10
        n1 = num * 10 + 1
        # Guard: Python handles big ints but slow for large N
        queue.append(n0)
        queue.append(n1)
    return "-1"


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(N)  |  Space: O(N)
# BFS on remainder states (0 to N-1). Each state = remainder mod N.
# Transitions: rem -> (rem*10) % N  or  (rem*10 + 1) % N
# Track parent remainder and digit to reconstruct the number.
# O(N) since each remainder visited at most once.
# ============================================================
def optimal(n: int) -> str:
    """BFS on remainders with parent tracking for reconstruction."""
    if n == 1:
        return "1"

    parent = [-1] * n      # parent[rem] = previous remainder
    digit_used = [-1] * n  # digit_used[rem] = digit that led to this rem
    visited = [False] * n

    start = 1 % n
    visited[start] = True
    parent[start] = n      # sentinel: root has no parent (use n as sentinel)
    digit_used[start] = 1

    if start == 0:
        return "1"

    queue = deque([start])
    found = -1

    while queue:
        rem = queue.popleft()
        for d in [0, 1]:
            next_rem = (rem * 10 + d) % n
            if not visited[next_rem]:
                visited[next_rem] = True
                parent[next_rem] = rem
                digit_used[next_rem] = d
                if next_rem == 0:
                    found = next_rem
                    break
                queue.append(next_rem)
        if found != -1:
            break

    # Reconstruct by backtracking
    result = []
    cur = found
    while cur != n:  # n is sentinel for root
        result.append(digit_used[cur])
        cur = parent[cur]
    result.reverse()
    return ''.join(map(str, result))


# ============================================================
# APPROACH 3: BEST
# Time: O(N)  |  Space: O(N)
# BFS on remainders storing the actual number string at each state.
# Cleaner code; for large N the optimal approach is more memory efficient.
# ============================================================
def best(n: int) -> str:
    """BFS on remainders, directly storing the number string."""
    if n == 1:
        return "1"

    num_at_rem = [None] * n
    queue = deque()

    r = 1 % n
    num_at_rem[r] = "1"
    if r == 0:
        return "1"
    queue.append(r)

    while queue:
        rem = queue.popleft()
        cur = num_at_rem[rem]
        for d in [0, 1]:  # append '0' first for smallest result
            nr = (rem * 10 + d) % n
            if num_at_rem[nr] is None:
                num_at_rem[nr] = cur + str(d)
                if nr == 0:
                    return num_at_rem[nr]
                queue.append(nr)
    return "-1"


if __name__ == "__main__":
    print("=== Smallest Multiple With 0 and 1 ===\n")

    tests = [2, 3, 5, 7, 12, 13]
    for n in tests:
        op = optimal(n)
        be = best(n)
        print(f"N={n:3d} | Optimal: {op:<20} | Best: {be}")

    print("\n--- Verification ---")
    for n in [3, 7, 13]:
        res = best(n)
        only_binary = all(c in '01' for c in res)
        rem = 0
        for c in res:
            rem = (rem * 10 + int(c)) % n
        print(f"N={n:2d} -> {res}, onlyBinary={only_binary}, divisible={rem == 0}")
