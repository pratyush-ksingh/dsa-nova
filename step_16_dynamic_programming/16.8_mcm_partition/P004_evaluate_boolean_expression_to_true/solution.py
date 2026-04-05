"""
Problem: Evaluate Boolean Expression to True
Difficulty: HARD | XP: 50

Given a boolean expression string consisting of:
  - Operands: 'T' (True) and 'F' (False)
  - Operators: '&' (AND), '|' (OR), '^' (XOR)
  - The expression is always in the form: operand op operand op ... (length 2n-1)

Count the number of ways to parenthesize the expression so that it evaluates to True.

Example: "T|F&T^F"
  One valid parenthesization: (T|(F&(T^F))) -> True
  Count all such parenthesizations.

This is a classic Interval DP / Matrix Chain Multiplication variant.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE - Exhaustive Recursion
# Time: O(4^n * n)  |  Space: O(n^2) recursion + call stack
# ============================================================
def brute_force(expression: str) -> int:
    """
    Try every possible split point k (an operator position) for the expression.
    For each split:
      - Recursively count (trueCount, falseCount) for left and right sub-expressions.
      - Combine based on the operator at position k.

    Operator combination rules:
      AND (&): true  = leftTrue  * rightTrue
               false = leftFalse * rightTrue + leftTrue * rightFalse + leftFalse * rightFalse
      OR  (|): true  = leftTrue * rightTrue + leftTrue * rightFalse + leftFalse * rightTrue
               false = leftFalse * rightFalse
      XOR (^): true  = leftTrue * rightFalse + leftFalse * rightTrue
               false = leftTrue * rightTrue   + leftFalse * rightFalse

    Operands are at even indices (0, 2, 4, ...), operators at odd indices (1, 3, 5, ...).
    """
    def count_ways(lo: int, hi: int) -> tuple:
        """Returns (trueCount, falseCount) for expression[lo..hi]"""
        if lo == hi:
            # Single operand
            if expression[lo] == 'T':
                return (1, 0)
            else:
                return (0, 1)

        total_true  = 0
        total_false = 0

        # Split at each operator (odd indices within [lo, hi])
        for k in range(lo + 1, hi, 2):  # k is the operator index
            op = expression[k]
            lt, lf = count_ways(lo, k - 1)
            rt, rf = count_ways(k + 1, hi)

            if op == '&':
                total_true  += lt * rt
                total_false += lt * rf + lf * rt + lf * rf
            elif op == '|':
                total_true  += lt * rt + lt * rf + lf * rt
                total_false += lf * rf
            elif op == '^':
                total_true  += lt * rf + lf * rt
                total_false += lt * rt + lf * rf

        return (total_true, total_false)

    n = len(expression)
    if n == 0:
        return 0
    true_count, _ = count_ways(0, n - 1)
    return true_count


# ============================================================
# APPROACH 2: OPTIMAL - Interval DP with Memoization
# Time: O(n^3)  |  Space: O(n^2)
# ============================================================
def optimal(expression: str) -> int:
    """
    Same recursion as brute force, but memoize (lo, hi) -> (trueCount, falseCount).

    Number of sub-problems: O(n^2) intervals.
    Each sub-problem iterates over O(n) split points.
    Combine step is O(1) per split.
    Total: O(n^3) time, O(n^2) space.

    Note: n here refers to the number of OPERANDS. String length = 2n-1.
    So if string has length L, n = (L+1)/2 operands, and complexity is O(L^3).
    """
    memo = {}

    def count_ways(lo: int, hi: int) -> tuple:
        if (lo, hi) in memo:
            return memo[(lo, hi)]

        if lo == hi:
            result = (1, 0) if expression[lo] == 'T' else (0, 1)
            memo[(lo, hi)] = result
            return result

        total_true  = 0
        total_false = 0

        for k in range(lo + 1, hi, 2):
            op = expression[k]
            lt, lf = count_ways(lo, k - 1)
            rt, rf = count_ways(k + 1, hi)

            if op == '&':
                total_true  += lt * rt
                total_false += lt * rf + lf * rt + lf * rf
            elif op == '|':
                total_true  += lt * rt + lt * rf + lf * rt
                total_false += lf * rf
            elif op == '^':
                total_true  += lt * rf + lf * rt
                total_false += lt * rt + lf * rf

        memo[(lo, hi)] = (total_true, total_false)
        return (total_true, total_false)

    n = len(expression)
    if n == 0:
        return 0
    true_count, _ = count_ways(0, n - 1)
    return true_count


# ============================================================
# APPROACH 3: BEST - Bottom-Up Tabulation DP
# Time: O(n^3)  |  Space: O(n^2)
# ============================================================
def best(expression: str) -> int:
    """
    Bottom-up tabulation. Fill DP table for increasing interval lengths.

    dp_true[i][j]  = number of ways expression[i..j] evaluates to True
    dp_false[i][j] = number of ways expression[i..j] evaluates to False

    i and j are indices into the expression string (0-based), always pointing
    to operand positions (even indices).

    Fill order: gap = 0 (single operands) -> gap = 2 -> gap = 4 -> ...

    For each interval [i, j] with gap >= 2:
      For each operator k in range(i+1, j, 2):
        Combine left [i, k-1] and right [k+1, j] based on operator expression[k].

    Answer: dp_true[0][n-1] where n-1 is the last character index.

    Advantages over memoization: no recursion overhead, cache-friendly access.
    """
    n = len(expression)
    if n == 0:
        return 0

    # Tables indexed by character positions in the expression string
    dp_true  = [[0] * n for _ in range(n)]
    dp_false = [[0] * n for _ in range(n)]

    # Base case: single operands (even indices)
    for i in range(0, n, 2):
        if expression[i] == 'T':
            dp_true[i][i] = 1
            dp_false[i][i] = 0
        else:
            dp_true[i][i] = 0
            dp_false[i][i] = 1

    # Fill for increasing gap (gap = 2, 4, 6, ...)
    # 'gap' is the step: [i, i+gap] covers operands with operators in between
    for gap in range(2, n, 2):
        for i in range(0, n - gap, 2):
            j = i + gap
            for k in range(i + 1, j, 2):  # k is operator index
                op = expression[k]
                lt = dp_true[i][k - 1]
                lf = dp_false[i][k - 1]
                rt = dp_true[k + 1][j]
                rf = dp_false[k + 1][j]

                if op == '&':
                    dp_true[i][j]  += lt * rt
                    dp_false[i][j] += lt * rf + lf * rt + lf * rf
                elif op == '|':
                    dp_true[i][j]  += lt * rt + lt * rf + lf * rt
                    dp_false[i][j] += lf * rf
                elif op == '^':
                    dp_true[i][j]  += lt * rf + lf * rt
                    dp_false[i][j] += lt * rt + lf * rf

    return dp_true[0][n - 1]


if __name__ == "__main__":
    print("=== Evaluate Boolean Expression to True ===")

    test_cases = [
        ("T|F",     1),   # T|F -> only one way to evaluate, result is True -> 1
        ("T&F",     0),   # T&F -> False only
        ("T^T",     0),   # T^T -> False (XOR same -> False)
        ("T|F&T^F", 5),   # All 5 parenthesizations evaluate to True
        ("T",       1),   # Single True
        ("F",       0),   # Single False
    ]

    for expr, expected in test_cases:
        b  = brute_force(expr)
        o  = optimal(expr)
        be = best(expr)
        status = "OK" if b == o == be == expected else "MISMATCH"
        print(f"expr={expr!r:15s} => Brute={b}, Optimal={o}, Best={be} | Expected={expected} [{status}]")
