"""
Problem: Print Name N Times (Recursion)
Difficulty: EASY | XP: 10

Print your name N times using recursion (no loops).
"""


# ============================================================
# APPROACH 1: Count Down from N
# Time: O(n)  |  Space: O(n) call stack
# Print name, then recurse with N-1.
# ============================================================
def print_name_countdown(name: str, n: int) -> None:
    """Print name n times by counting down."""
    if n <= 0:
        return  # Base case: no more prints

    print(name)
    print_name_countdown(name, n - 1)


# ============================================================
# APPROACH 2: Count Up from 1 to N
# Time: O(n)  |  Space: O(n) call stack
# Use a counter that increments until it exceeds N.
# ============================================================
def print_name_countup(name: str, i: int, n: int) -> None:
    """Print name n times by counting up."""
    if i > n:
        return  # Base case: counter exceeded N

    print(name)
    print_name_countup(name, i + 1, n)


# ============================================================
# APPROACH 3: With iteration counter in output
# Time: O(n)  |  Space: O(n) call stack
# Shows which iteration -- helpful for debugging.
# ============================================================
def print_name_with_count(name: str, i: int, n: int) -> None:
    """Print name with iteration number."""
    if i > n:
        return

    print(f"{i}. {name}")
    print_name_with_count(name, i + 1, n)


if __name__ == "__main__":
    print("=== Print Name N Times (Recursion) ===\n")

    name = "Nova"
    n = 5

    print(f"Approach 1: Count Down (n={n})")
    print_name_countdown(name, n)

    print(f"\nApproach 2: Count Up (1 to n={n})")
    print_name_countup(name, 1, n)

    print(f"\nApproach 3: With Iteration Number")
    print_name_with_count(name, 1, n)

    # Edge cases
    print("\n--- Edge Case: N = 1 ---")
    print_name_countdown(name, 1)

    print("\n--- Edge Case: N = 0 ---")
    print_name_countdown(name, 0)
    print("(nothing printed)")

    print("\n--- Custom Name ---")
    print_name_with_count("DSA_Nova", 1, 3)
