"""
Problem: Print N to 1 (Recursion)
Difficulty: EASY | XP: 10

Print numbers from N to 1 using recursion (no loops).
"""


# ============================================================
# APPROACH 1: Count Down (print before recursion)
# Time: O(n)  |  Space: O(n) call stack
# Print N first, then recurse for N-1.
# ============================================================
def print_descending(n: int) -> None:
    """Print n, then recurse for n-1."""
    if n < 1:
        return  # Base case: stop at 0

    print(n)  # Print BEFORE recursion = descending
    print_descending(n - 1)


# ============================================================
# APPROACH 2: Count Up with Backtracking (print after recursion)
# Time: O(n)  |  Space: O(n) call stack
# Count up from 1 to N, but print on the way back (LIFO).
# ============================================================
def print_via_backtracking(i: int, n: int) -> None:
    """Count up, but print after returning = descending order."""
    if i > n:
        return  # Base case

    print_via_backtracking(i + 1, n)  # Go deeper first
    print(i)  # Print AFTER return = descending


# ============================================================
# APPROACH 3: Clean single-parameter version
# Time: O(n)  |  Space: O(n) call stack
# ============================================================
def print_n_to_1(n: int) -> None:
    """Print n down to 1."""
    if n == 0:
        return
    print(n)  # Print current
    print_n_to_1(n - 1)  # Handle rest


if __name__ == "__main__":
    print("=== Print N to 1 (Recursion) ===\n")

    n = 5

    print(f"Approach 1: Count Down (n={n})")
    print_descending(n)

    print(f"\nApproach 2: Backtracking (i=1, n={n})")
    print_via_backtracking(1, n)

    print(f"\nApproach 3: Clean API (n={n})")
    print_n_to_1(n)

    # Edge cases
    print("\n--- Edge Case: N = 1 ---")
    print_n_to_1(1)

    print("\n--- Edge Case: N = 0 ---")
    print_n_to_1(0)
    print("(nothing printed)")
