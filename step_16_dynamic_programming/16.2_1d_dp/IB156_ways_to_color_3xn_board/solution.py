"""
Problem: Ways to Color 3xN Board
Difficulty: HARD | XP: 50
Source: InterviewBit

Color a 3xN board with 4 colors such that no two adjacent
cells (horizontally or vertically) have the same color.
Return the count modulo 1e9+7.
"""
from typing import List

MOD = 1_000_000_007


def _generate_patterns():
    """Generate all valid 3-row column colorings (c0!=c1, c1!=c2)."""
    return [(a, b, c) for a in range(4) for b in range(4) for c in range(4)
            if a != b and b != c]


def _compatible(p, q):
    """Two column patterns are compatible if no row has the same color."""
    return p[0] != q[0] and p[1] != q[1] and p[2] != q[2]


# ============================================================
# APPROACH 1: BRUTE FORCE - Column-by-column DP
# Time: O(n * P^2)  |  Space: O(P)  where P=36
# ============================================================
def brute_force(n: int) -> int:
    patterns = _generate_patterns()
    P = len(patterns)  # 36

    dp = [1] * P  # 1 way to color each single-column pattern

    for _ in range(n - 1):
        ndp = [0] * P
        for j in range(P):
            for i in range(P):
                if _compatible(patterns[i], patterns[j]):
                    ndp[j] = (ndp[j] + dp[i]) % MOD
        dp = ndp

    return sum(dp) % MOD


# ============================================================
# APPROACH 2: OPTIMAL - Precomputed transition lists
# Time: O(n * P^2)  |  Space: O(P^2) precomputed
# ============================================================
def optimal(n: int) -> int:
    patterns = _generate_patterns()
    P = len(patterns)

    # Precompute which patterns can precede each pattern j
    trans = [[i for i in range(P) if _compatible(patterns[i], patterns[j])]
             for j in range(P)]

    dp = [1] * P
    for _ in range(n - 1):
        ndp = [0] * P
        for j in range(P):
            for i in trans[j]:
                ndp[j] = (ndp[j] + dp[i]) % MOD
        dp = ndp

    return sum(dp) % MOD


# ============================================================
# APPROACH 3: BEST - Matrix Exponentiation for huge N
# Time: O(P^3 * log n)  |  Space: O(P^2)
# ============================================================
# For n up to 10^18, iterative DP is too slow. The DP is a
# linear recurrence: state vector times transition matrix.
# Use fast matrix exponentiation to compute T^(n-1).
# Real-life use: computing walk counts in graphs after many
# steps, powering Fibonacci-like sequences over large inputs.
# ============================================================
def _mat_mul(A, B, P):
    C = [[0]*P for _ in range(P)]
    for i in range(P):
        for k in range(P):
            if A[i][k] == 0:
                continue
            for j in range(P):
                C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % MOD
    return C


def _mat_pow(M, p, P):
    result = [[1 if i == j else 0 for j in range(P)] for i in range(P)]
    while p:
        if p & 1:
            result = _mat_mul(result, M, P)
        M = _mat_mul(M, M, P)
        p >>= 1
    return result


def best(n: int) -> int:
    patterns = _generate_patterns()
    P = len(patterns)  # 36

    # Transition matrix T[j][i] = 1 if compatible(patterns[i], patterns[j])
    T = [[0]*P for _ in range(P)]
    for i in range(P):
        for j in range(P):
            if _compatible(patterns[i], patterns[j]):
                T[j][i] = 1

    if n == 1:
        return P  # all 36 column patterns valid

    Tn = _mat_pow(T, n - 1, P)

    # Initial vector: all 1s (each pattern has 1 way for a single column)
    ans = 0
    for j in range(P):
        ans = (ans + sum(Tn[j])) % MOD
    return ans


if __name__ == "__main__":
    print("=== Ways to Color 3xN Board ===")

    print(f"n=1 Brute   (expect 36):  {brute_force(1)}")
    print(f"n=1 Optimal (expect 36):  {optimal(1)}")
    print(f"n=1 Best    (expect 36):  {best(1)}")

    print(f"n=2 Brute   (expect 588): {brute_force(2)}")
    print(f"n=2 Optimal (expect 588): {optimal(2)}")
    print(f"n=2 Best    (expect 588): {best(2)}")

    b3, o3, be3 = brute_force(3), optimal(3), best(3)
    print(f"n=3 all match? {b3==o3==be3} val={b3}")

    b5, o5, be5 = brute_force(5), optimal(5), best(5)
    print(f"n=5 all match? {b5==o5==be5} val={b5}")
