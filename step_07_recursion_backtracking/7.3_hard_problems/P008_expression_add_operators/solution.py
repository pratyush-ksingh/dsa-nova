"""
Problem: Expression Add Operators
Difficulty: HARD | XP: 50

Given a string of digits and a target integer, insert +, -, * operators
(no operator = concatenation) between digits to make the expression equal target.
Return all valid expressions.
Real-life use: Expression parsing, calculator, test case generation.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Generate all expressions as strings, evaluate each one.
# Uses Python's eval() — safe here since input is digits/operators only.
# Time: O(4^N * N)  |  Space: O(4^N * N)
# ============================================================
def brute_force(num: str, target: int) -> List[str]:
    result = []

    def generate(idx: int, expr: str) -> None:
        if idx == len(num):
            try:
                if eval(expr) == target:
                    result.append(expr)
            except Exception:
                pass
            return
        for end in range(idx + 1, len(num) + 1):
            token = num[idx:end]
            if len(token) > 1 and token[0] == '0':
                break  # no leading zeros
            if not expr:
                generate(end, token)
            else:
                generate(end, expr + '+' + token)
                generate(end, expr + '-' + token)
                generate(end, expr + '*' + token)

    generate(0, "")
    return result


# ============================================================
# APPROACH 2: OPTIMAL
# Backtracking with running eval and lastMul tracking.
# When '*' is applied, undo the last term and redo with multiplication.
# eval - lastMul + lastMul * cur  handles precedence correctly.
# Time: O(4^N * N)  |  Space: O(N) recursion stack
# ============================================================
def optimal(num: str, target: int) -> List[str]:
    result = []

    def backtrack(idx: int, path: List[str], eval_val: int, last_mul: int) -> None:
        if idx == len(num):
            if eval_val == target:
                result.append("".join(path))
            return
        for end in range(idx + 1, len(num) + 1):
            if end > idx + 1 and num[idx] == '0':
                break  # no leading zeros
            token = num[idx:end]
            cur = int(token)
            if idx == 0:
                path.append(token)
                backtrack(end, path, cur, cur)
                path.pop()
            else:
                path.append('+'); path.append(token)
                backtrack(end, path, eval_val + cur, cur)
                path.pop(); path.pop()

                path.append('-'); path.append(token)
                backtrack(end, path, eval_val - cur, -cur)
                path.pop(); path.pop()

                path.append('*'); path.append(token)
                backtrack(end, path, eval_val - last_mul + last_mul * cur, last_mul * cur)
                path.pop(); path.pop()

    backtrack(0, [], 0, 0)
    return result


# ============================================================
# APPROACH 3: BEST
# Same logic as optimal but flattened with string building for clarity.
# Uses a single list join at the end — cleanest Python implementation.
# Time: O(4^N * N)  |  Space: O(N)
# ============================================================
def best(num: str, target: int) -> List[str]:
    result = []
    n = len(num)

    def dfs(idx: int, path: str, eval_val: int, last_mul: int) -> None:
        if idx == n:
            if eval_val == target:
                result.append(path)
            return
        for end in range(idx + 1, n + 1):
            if end > idx + 1 and num[idx] == '0':
                break
            token = num[idx:end]
            cur = int(token)
            if idx == 0:
                dfs(end, token, cur, cur)
            else:
                dfs(end, path + '+' + token, eval_val + cur, cur)
                dfs(end, path + '-' + token, eval_val - cur, -cur)
                new_mul = last_mul * cur
                dfs(end, path + '*' + token, eval_val - last_mul + new_mul, new_mul)

    dfs(0, "", 0, 0)
    return result


if __name__ == "__main__":
    print("=== Expression Add Operators ===")

    tests = [("123", 6), ("232", 8), ("105", 5), ("00", 0)]
    for num, t in tests:
        print(f'\nnum="{num}"  target={t}')
        print(f"  Brute  : {brute_force(num, t)}")
        print(f"  Optimal: {optimal(num, t)}")
        print(f"  Best   : {best(num, t)}")
