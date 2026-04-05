"""
Problem: Reverse a Stack using Recursion
Difficulty: MEDIUM | XP: 25

Reverse a stack using only recursion — no loops, no extra data structures
(except the implicit call stack).
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE — Auxiliary Stack
# Time: O(n)  |  Space: O(n) extra
# ============================================================
def brute_force(stack: List[int]) -> List[int]:
    """
    Transfer all elements to an auxiliary stack (naturally reverses order),
    then transfer back.  Uses O(n) extra space explicitly.
    """
    # Pop everything into aux — aux now has elements with old-top at index [-1]
    # and old-bottom at index [0].
    # Example: stack=[1,2,3,4,5] (top=5) -> aux=[1,2,3,4,5] (index 0=bottom,4=top)
    # We want reversed stack: top=1, bottom=5
    # = push items in order 5,4,3,2,1 = push aux[4] first, aux[0] last
    # = just push aux items from index 0 to len-1 using normal append.
    aux = list(stack)   # copy preserving order (index 0 = bottom, -1 = top)
    stack.clear()
    for item in reversed(aux):  # reversed: start from old top, push it first = new bottom
        stack.append(item)
    return stack


# ============================================================
# APPROACH 2: OPTIMAL — Recursive Reverse + insertAtBottom helper
# Time: O(n^2)  |  Space: O(n) call stack only
# ============================================================

def _insert_at_bottom(stack: List[int], item: int) -> None:
    """Recursively insert `item` at the bottom of the stack."""
    if not stack:
        stack.append(item)
        return
    top = stack.pop()
    _insert_at_bottom(stack, item)
    stack.append(top)


def optimal(stack: List[int]) -> List[int]:
    """
    Pop the top element, recursively reverse the rest,
    then insert the popped element at the bottom.

    Intuition:
      reverse([a, b, c, d])          (top = d)
        -> reverse([a, b, c]) + insertAtBottom(d)
        -> reverse([a, b])   + insertAtBottom(c) + insertAtBottom(d)
        -> ...
    """
    if len(stack) <= 1:
        return stack
    top = stack.pop()
    optimal(stack)
    _insert_at_bottom(stack, top)
    return stack


# ============================================================
# APPROACH 3: BEST — Same recursive approach (explicit version)
# Time: O(n^2)  |  Space: O(n) call stack only
# ============================================================
# For this problem "best" == "optimal" because only recursion is allowed.
# We present it as a self-contained pair of methods for clarity.

def _insert_at_bottom_v2(stack: List[int], item: int) -> None:
    if not stack:
        stack.append(item)
        return
    held = stack.pop()
    _insert_at_bottom_v2(stack, item)
    stack.append(held)


def best(stack: List[int]) -> List[int]:
    """
    Identical logic to optimal; presented as a standalone clean solution.
    This is the canonical interview answer when no auxiliary structure is allowed.
    """
    if not stack:
        return stack
    top = stack.pop()
    best(stack)
    _insert_at_bottom_v2(stack, top)
    return stack


if __name__ == "__main__":
    print("=== Reverse a Stack using Recursion ===")

    s1 = [1, 2, 3, 4, 5]   # top = 5
    print(f"Brute:   {brute_force(s1)}")   # [5, 4, 3, 2, 1]

    s2 = [1, 2, 3, 4, 5]
    print(f"Optimal: {optimal(s2)}")       # [5, 4, 3, 2, 1]

    s3 = [1, 2, 3, 4, 5]
    print(f"Best:    {best(s3)}")          # [5, 4, 3, 2, 1]
