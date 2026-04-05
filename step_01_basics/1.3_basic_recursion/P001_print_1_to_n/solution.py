"""
Problem: Print 1 to N (Recursion)
Difficulty: EASY | XP: 10

Print numbers from 1 to N using recursion (no loops).
"""


# ============================================================
# APPROACH 1: Counting Up (pass current counter)
# Time: O(n)  |  Space: O(n) call stack
# Print current number, then recurse for next.
# ============================================================
def print_ascending(i: int, n: int) -> None:
    """Print i, then recurse for i+1 until past n."""
    if i > n:
        return  # Base case: stop when past N

    print(i)
    print_ascending(i + 1, n)


# ============================================================
# APPROACH 2: Backtracking (count down, print after recursion)
# Time: O(n)  |  Space: O(n) call stack
# Recurse first (going deeper), print on the way back up.
# ============================================================
def print_via_backtracking(n: int) -> None:
    """Recurse to base case first, then print on unwind."""
    if n < 1:
        return  # Base case: stop at 0

    print_via_backtracking(n - 1)  # Go deeper first
    print(n)  # Print AFTER returning = ascending order


# ============================================================
# APPROACH 3: Single parameter (clean API)
# Time: O(n)  |  Space: O(n) call stack
# Same as Approach 2 with the cleanest interface.
# ============================================================
def print_1_to_n(n: int) -> None:
    """Print 1 to n using a single parameter."""
    if n == 0:
        return
    print_1_to_n(n - 1)  # Handle 1..n-1 first
    print(n)  # Then print n


if __name__ == "__main__":
    print("=== Print 1 to N (Recursion) ===\n")

    n = 5

    print(f"Approach 1: Counting Up (i=1 to n={n})")
    print_ascending(1, n)

    print(f"\nApproach 2: Backtracking (n={n})")
    print_via_backtracking(n)

    print(f"\nApproach 3: Clean API (n={n})")
    print_1_to_n(n)

    # Edge cases
    print("\n--- Edge Case: N = 1 ---")
    print_1_to_n(1)

    print("\n--- Edge Case: N = 0 ---")
    print_1_to_n(0)
    print("(nothing printed)")
