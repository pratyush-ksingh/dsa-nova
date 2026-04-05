"""
Problem: MAXSPPROD
Difficulty: HARD | XP: 50
Source: InterviewBit

For each element A[i], find:
  L(i) = 1-indexed position of nearest element strictly GREATER than A[i] to the LEFT
  R(i) = 1-indexed position of nearest element strictly GREATER than A[i] to the RIGHT
Maximize L(i) * R(i) over all i. Return result modulo 1e9+7.
If no larger element on a side, that index is 0.
"""
from typing import List

MOD = 10**9 + 7


# ============================================================
# APPROACH 1: BRUTE FORCE - Linear scan each direction per element
# Time: O(N^2)  |  Space: O(N)
# ============================================================
def brute_force(A: List[int]) -> int:
    """For each i, scan left and right for nearest strictly greater element."""
    n = len(A)
    ans = 0
    for i in range(n):
        left = 0
        for j in range(i - 1, -1, -1):
            if A[j] > A[i]:
                left = j + 1  # 1-indexed
                break
        right = 0
        for j in range(i + 1, n):
            if A[j] > A[i]:
                right = j + 1  # 1-indexed
                break
        prod = (left % MOD) * (right % MOD) % MOD
        ans = max(ans, prod)
    return ans


# ============================================================
# APPROACH 2: OPTIMAL - Monotonic Stack (two passes)
# Time: O(N)  |  Space: O(N)
# Decreasing monotonic stack for nearest greater left/right.
# ============================================================
def optimal(A: List[int]) -> int:
    """Two-pass monotonic stack to find NGL and NGR in O(N)."""
    n = len(A)
    L = [0] * n   # nearest greater to left (1-indexed, 0 if none)
    R = [0] * n   # nearest greater to right (1-indexed, 0 if none)
    stack = []

    for i in range(n):
        while stack and A[stack[-1]] <= A[i]:
            stack.pop()
        L[i] = stack[-1] + 1 if stack else 0
        stack.append(i)

    stack.clear()
    for i in range(n - 1, -1, -1):
        while stack and A[stack[-1]] <= A[i]:
            stack.pop()
        R[i] = stack[-1] + 1 if stack else 0
        stack.append(i)

    ans = 0
    for i in range(n):
        prod = (L[i] % MOD) * (R[i] % MOD) % MOD
        ans = max(ans, prod)
    return ans


# ============================================================
# APPROACH 3: BEST - Same O(N) but skip elements where L or R is 0
# Time: O(N)  |  Space: O(N)
# Identical complexity but avoids multiplying zeros for cleaner logic.
# ============================================================
def best(A: List[int]) -> int:
    """Monotonic stack: skip i where L[i]==0 or R[i]==0 (product is 0)."""
    n = len(A)
    L = [0] * n
    R = [0] * n
    stack = []

    for i in range(n):
        while stack and A[stack[-1]] <= A[i]:
            stack.pop()
        L[i] = stack[-1] + 1 if stack else 0
        stack.append(i)

    stack.clear()
    for i in range(n - 1, -1, -1):
        while stack and A[stack[-1]] <= A[i]:
            stack.pop()
        R[i] = stack[-1] + 1 if stack else 0
        stack.append(i)

    ans = 0
    for i in range(n):
        if L[i] and R[i]:
            ans = max(ans, (L[i] % MOD) * (R[i] % MOD) % MOD)
    return ans


if __name__ == "__main__":
    print("=== MAXSPPROD ===")
    tests = [
        ([5, 4, 3, 4, 5], 8),   # i=2: L=2, R=4 -> 8
        ([1, 2, 3, 4], 0),      # no element has both sides greater
        ([4, 2, 1, 3], 2),      # i=2 (A=1): L=2(A[1]=2), R=4(A[3]=3) -> 8? check
    ]
    for A, exp in tests:
        b = brute_force(A)
        o = optimal(A)
        be = best(A)
        print(f"A={A}: brute={b} opt={o} best={be} (exp={exp})")
