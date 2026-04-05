"""
Problem: Generate All Binary Strings
Difficulty: MEDIUM | XP: 25

Generate all binary strings of length N such that no two consecutive 1s appear.
Real-life use: Constraint satisfaction, combinatorial enumeration, testing bit patterns.
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Generate all 2^N binary strings, then filter those with consecutive 1s.
# Time: O(N * 2^N)  |  Space: O(N * 2^N)
# ============================================================
def brute_force(n: int) -> List[str]:
    def generate_all(current: str) -> List[str]:
        if len(current) == n:
            return [current]
        return generate_all(current + "0") + generate_all(current + "1")

    def is_valid(s: str) -> bool:
        return "11" not in s

    return [s for s in generate_all("") if is_valid(s)]


# ============================================================
# APPROACH 2: OPTIMAL
# Backtracking with pruning: skip placing '1' if previous char was '1'.
# Time: O(N * Fib(N+2))  |  Space: O(N) recursion stack
# ============================================================
def optimal(n: int) -> List[str]:
    result = []

    def backtrack(arr: List[str], pos: int) -> None:
        if pos == n:
            result.append("".join(arr))
            return
        # Always place '0'
        arr[pos] = "0"
        backtrack(arr, pos + 1)
        # Place '1' only if previous was not '1'
        if pos == 0 or arr[pos - 1] != "1":
            arr[pos] = "1"
            backtrack(arr, pos + 1)

    backtrack([""] * n, 0)
    return result


# ============================================================
# APPROACH 3: BEST
# Iterative level-by-level construction (no recursion stack).
# Strings ending in '0' extend with '0' or '1'.
# Strings ending in '1' extend only with '0'.
# Time: O(N * Fib(N+2))  |  Space: O(Fib(N+2))
# ============================================================
def best(n: int) -> List[str]:
    current = ["0", "1"]
    for _ in range(n - 1):
        nxt = []
        for s in current:
            nxt.append(s + "0")
            if s[-1] != "1":
                nxt.append(s + "1")
        current = nxt
    return current


if __name__ == "__main__":
    print("=== Generate All Binary Strings (No Consecutive 1s) ===")

    n = 4
    print(f"\nN = {n}")
    b = brute_force(n)
    o = optimal(n)
    bst = best(n)
    print(f"Brute  ({len(b)}): {b}")
    print(f"Optimal({len(o)}): {o}")
    print(f"Best   ({len(bst)}): {bst}")

    print("\nN = 3")
    print(f"Optimal: {optimal(3)}")  # 000,001,010,100,101

    print("\nFibonacci pattern (count for N=1..6):")
    for i in range(1, 7):
        print(f"  N={i}: {len(optimal(i))} strings")
