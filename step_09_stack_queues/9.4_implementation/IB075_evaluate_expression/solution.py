"""
Problem: Evaluate Expression (Reverse Polish Notation)
Difficulty: MEDIUM | XP: 25
Source: InterviewBit

Evaluate the value of a postfix (RPN) expression.
Valid operators: +, -, *, /  (integer division truncates toward zero).
Real-life use: Compilers, calculators, stack-based VM evaluation.
"""
from typing import List
import math


# ============================================================
# APPROACH 1: BRUTE FORCE
# Recursive evaluation from right to left (post-order tree traversal view).
# Time: O(N)  |  Space: O(N) recursion
# ============================================================
def brute_force(tokens: List[str]) -> int:
    ops = {'+', '-', '*', '/'}
    idx = [len(tokens) - 1]

    def solve() -> int:
        t = tokens[idx[0]]
        idx[0] -= 1
        if t not in ops:
            return int(t)
        right = solve()
        left = solve()
        return apply(left, right, t)

    def apply(a: int, b: int, op: str) -> int:
        if op == '+': return a + b
        if op == '-': return a - b
        if op == '*': return a * b
        # Truncate toward zero (not floor division)
        return int(a / b)

    return solve()


# ============================================================
# APPROACH 2: OPTIMAL
# Classic iterative stack.
# Time: O(N)  |  Space: O(N)
# ============================================================
def optimal(tokens: List[str]) -> int:
    stack = []
    ops = {'+', '-', '*', '/'}
    for t in tokens:
        if t in ops:
            b, a = stack.pop(), stack.pop()
            if t == '+':   stack.append(a + b)
            elif t == '-': stack.append(a - b)
            elif t == '*': stack.append(a * b)
            else:          stack.append(int(a / b))  # truncate toward zero
        else:
            stack.append(int(t))
    return stack[-1]


# ============================================================
# APPROACH 3: BEST
# Same stack approach but uses operator module for clarity + handles
# large integer division edge cases properly.
# Time: O(N)  |  Space: O(N)
# ============================================================
def best(tokens: List[str]) -> int:
    import operator
    ops = {
        '+': operator.add,
        '-': operator.sub,
        '*': operator.mul,
        '/': lambda a, b: int(a / b),  # truncate toward zero
    }
    stack = []
    for t in tokens:
        if t in ops:
            b, a = stack.pop(), stack.pop()
            stack.append(ops[t](a, b))
        else:
            stack.append(int(t))
    return stack[-1]


if __name__ == "__main__":
    print("=== Evaluate Expression (RPN) ===")

    tests = [
        (["2", "1", "+", "3", "*"], 9),
        (["4", "13", "5", "/", "+"], 6),
        (["10","6","9","3","+","-11","*","/","*","17","+","5","+"], 22),
        (["3", "11", "+", "5", "-"], 9),
    ]
    for tokens, expected in tests:
        print(f"\ntokens={tokens}  expected={expected}")
        print(f"  Brute  : {brute_force(tokens)}")
        print(f"  Optimal: {optimal(tokens)}")
        print(f"  Best   : {best(tokens)}")
