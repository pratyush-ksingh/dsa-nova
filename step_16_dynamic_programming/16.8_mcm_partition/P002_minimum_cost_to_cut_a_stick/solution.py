"""
Problem: Minimum Cost to Cut a Stick  (LeetCode 1547)
Difficulty: HARD | XP: 50

You have a stick of length n. You are given an array cuts[] of positions to
cut. The cost of a cut is the current length of the stick being cut. You can
make the cuts in any order. Return the minimum total cost.

Key insight: Sort cuts, add 0 and n as endpoints. Define dp[i][j] = min cost
to make all cuts strictly between cuts[i] and cuts[j]. The first cut in this
segment costs (cuts[j] - cuts[i]) regardless of which position is chosen,
then we add the cost of the left and right sub-problems.
"""
from typing import List
import sys
from itertools import permutations


# ============================================================
# APPROACH 1: BRUTE FORCE (Try all orderings of cuts)
# Time: O(c! * c)  |  Space: O(c) for recursion
# ============================================================
def brute_force(n: int, cuts: List[int]) -> int:
    """
    Try every permutation of cuts; simulate the cost for each ordering.
    For each permutation, maintain the current sub-segments and compute
    how much each cut costs (the length of the segment it falls in).
    Only feasible for very small inputs.
    """
    cuts = sorted(cuts)
    min_cost = sys.maxsize

    def simulate(order, segments):
        """
        segments: list of (left, right) intervals currently on the table.
        For each cut in order, find which segment it belongs to, add its
        length as cost, then split that segment.
        """
        cost = 0
        segs = list(segments)
        for pos in order:
            for idx, (l, r) in enumerate(segs):
                if l < pos < r:
                    cost += r - l
                    segs.pop(idx)
                    segs.append((l, pos))
                    segs.append((pos, r))
                    break
        return cost

    initial_segments = [(0, n)]
    for perm in permutations(cuts):
        cost = simulate(perm, initial_segments)
        min_cost = min(min_cost, cost)

    return min_cost


# ============================================================
# APPROACH 2: OPTIMAL (Top-Down Interval DP)
# Time: O(c^3)  |  Space: O(c^2) memo + O(c) stack
# ============================================================
def optimal(n: int, cuts: List[int]) -> int:
    """
    Add 0 and n as virtual endpoints, sort all positions.
    dp(i, j) = min cost to perform all cuts strictly between new_cuts[i] and new_cuts[j].
    For the first cut at position k (i < k < j):
      cost = (new_cuts[j] - new_cuts[i])  <- this cut costs the full segment length
             + dp(i, k) + dp(k, j)         <- recurse on sub-segments
    Base case: j == i+1 (no cuts between them) -> 0.
    """
    new_cuts = sorted([0] + cuts + [n])
    m = len(new_cuts)
    memo = {}

    def dp(i: int, j: int) -> int:
        if j - i <= 1:          # no room for any cut
            return 0
        if (i, j) in memo:
            return memo[(i, j)]
        best = sys.maxsize
        for k in range(i + 1, j):
            cost = (new_cuts[j] - new_cuts[i]) + dp(i, k) + dp(k, j)
            best = min(best, cost)
        memo[(i, j)] = best
        return best

    return dp(0, m - 1)


# ============================================================
# APPROACH 3: BEST (Bottom-Up Interval DP)
# Time: O(c^3)  |  Space: O(c^2) — no recursion overhead
# ============================================================
def best(n: int, cuts: List[int]) -> int:
    """
    Same recurrence as Approach 2, built iteratively by segment length.
    dp[i][j] = min cost to make all cuts strictly between new_cuts[i] and new_cuts[j].
    Fill by increasing gap (j - i) from 2 upward.
    """
    new_cuts = sorted([0] + cuts + [n])
    m = len(new_cuts)
    dp = [[0] * m for _ in range(m)]

    # gap = j - i; we need gap >= 2 to have at least one cut between i and j
    for gap in range(2, m):
        for i in range(m - gap):
            j = i + gap
            dp[i][j] = sys.maxsize
            for k in range(i + 1, j):
                cost = (new_cuts[j] - new_cuts[i]) + dp[i][k] + dp[k][j]
                dp[i][j] = min(dp[i][j], cost)

    return dp[0][m - 1]


if __name__ == "__main__":
    test_cases = [
        # (n, cuts, expected)
        (7,  [1, 3, 4, 5], 16),
        (9,  [5, 6, 1, 4, 2], 22),
        (10, [2, 4, 7], 20),
        (5,  [3], 5),
    ]

    print("=== Minimum Cost to Cut a Stick ===\n")
    for n, cuts, expected in test_cases:
        # brute force only for small inputs
        b  = brute_force(n, cuts) if len(cuts) <= 8 else -1
        o  = optimal(n, cuts)
        be = best(n, cuts)
        status = "PASS" if o == be == expected else "FAIL"
        print(f"n={n}, cuts={cuts}")
        print(f"  Brute:   {b}")
        print(f"  Optimal: {o}")
        print(f"  Best:    {be}")
        print(f"  Expected:{expected}  [{status}]\n")
