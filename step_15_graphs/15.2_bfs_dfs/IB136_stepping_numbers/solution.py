"""
Problem: Stepping Numbers
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Given two integers A and B, find all stepping numbers in [A,B].
A stepping number has adjacent digits differing by exactly 1.
"""
from typing import List
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE - Check every number in range
# Time: O((B-A) * digits)  |  Space: O(result size)
# ============================================================
def brute_force(A: int, B: int) -> List[int]:
    def is_stepping(n: int) -> bool:
        s = str(n)
        return all(abs(int(s[i]) - int(s[i+1])) == 1 for i in range(len(s)-1))

    return [n for n in range(A, B + 1) if is_stepping(n)]


# ============================================================
# APPROACH 2: OPTIMAL - BFS from single digits 1-9
# Time: O(result_size * 10)  |  Space: O(result size)
# ============================================================
# Build stepping numbers digit by digit. From number n with
# last digit d, extend to n*10+(d-1) and n*10+(d+1).
# BFS ensures we explore by number of digits (level-order).
# ============================================================
def optimal(A: int, B: int) -> List[int]:
    res = []
    if A == 0:
        res.append(0)

    q = deque(range(1, 10))
    while q:
        n = q.popleft()
        if n > B:
            continue
        if A <= n <= B:
            res.append(n)
        last = n % 10
        if last > 0:
            q.append(n * 10 + (last - 1))
        if last < 9:
            q.append(n * 10 + (last + 1))

    res.sort()
    return res


# ============================================================
# APPROACH 3: BEST - DFS (iterative stack, lower memory)
# Time: O(result_size * 10)  |  Space: O(log10(B) * 10)
# ============================================================
# DFS stack depth is proportional to number of digits, while
# BFS queue can hold many numbers at a given digit level.
# Real-life use: generating stepped PIN codes or keyboard-swipe
# patterns for security or UX research on numeric keypads.
# ============================================================
def best(A: int, B: int) -> List[int]:
    res = []
    if A == 0:
        res.append(0)

    stack = list(range(1, 10))
    while stack:
        n = stack.pop()
        if n > B:
            continue
        if A <= n <= B:
            res.append(n)
        last = n % 10
        if last < 9:
            stack.append(n * 10 + (last + 1))
        if last > 0:
            stack.append(n * 10 + (last - 1))

    res.sort()
    return res


if __name__ == "__main__":
    print("=== Stepping Numbers ===")

    # Test 1: [0,21] => 0,1,2,3,4,5,6,7,8,9,10,12,21
    print(f"Test1 Brute   (expect [0..9,10,12,21]): {brute_force(0, 21)}")
    print(f"Test1 Optimal (expect [0..9,10,12,21]): {optimal(0, 21)}")
    print(f"Test1 Best    (expect [0..9,10,12,21]): {best(0, 21)}")

    # Test 2: [10,15] => 10,12
    print(f"Test2 Best    (expect [10, 12]): {best(10, 15)}")

    # Test 3: [100,130] => 101,121,123
    print(f"Test3 Best    (expect [101, 121, 123]): {best(100, 130)}")

    # Test 4: single number
    print(f"Test4 Best    (expect [0]): {best(0, 0)}")
