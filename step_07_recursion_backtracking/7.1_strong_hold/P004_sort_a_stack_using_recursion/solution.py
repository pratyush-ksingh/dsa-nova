"""
Problem: Sort a Stack using Recursion
Difficulty: MEDIUM | XP: 25
"""
from typing import List


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n log n)  |  Space: O(n)
# Pop everything into a list, sort it, push back.
# Uses built-in sort — violates "recursion only" spirit.
# ============================================================
def brute_force(stack: List[int]) -> List[int]:
    """
    Pop all elements to an array, sort ascending, push back so
    the smallest element is at the top (stack top = minimum).
    """
    arr = []
    while stack:
        arr.append(stack.pop())
    arr.sort(reverse=True)          # largest at index 0 -> pushed first -> at bottom
    for val in arr:
        stack.append(val)
    return stack


# ============================================================
# APPROACH 2: OPTIMAL — Recursive Sort + Recursive Insert
# Time: O(n²)  |  Space: O(n) recursive call stack
# ============================================================
def _insert_sorted(stack: List[int], element: int) -> None:
    """
    Insert `element` into an already-sorted stack (top = smallest)
    at its correct sorted position, using recursion only.
    """
    # Base cases: empty stack or element should sit on top
    if not stack or element <= stack[-1]:
        stack.append(element)
        return
    # Current top is smaller → hold it aside, recurse deeper
    top = stack.pop()
    _insert_sorted(stack, element)
    stack.append(top)   # restore top after correct position found


def _sort_stack(stack: List[int]) -> None:
    """
    Recursively sort the stack.  Remove top, sort remaining,
    then insert top back in sorted position.
    """
    if len(stack) <= 1:
        return
    top = stack.pop()
    _sort_stack(stack)
    _insert_sorted(stack, top)


def optimal(stack: List[int]) -> List[int]:
    """Sort stack using recursion only (no auxiliary data structures)."""
    _sort_stack(stack)
    return stack


# ============================================================
# APPROACH 3: BEST — Same recursive approach (only recursion)
# Time: O(n²)  |  Space: O(n)
# Identical algorithm; 'best' in this context means the
# cleanest, interview-ready version written as a class.
# ============================================================
class SortedStack:
    def __init__(self):
        self._stack: List[int] = []

    def _insert(self, val: int) -> None:
        if not self._stack or val <= self._stack[-1]:
            self._stack.append(val)
            return
        top = self._stack.pop()
        self._insert(val)
        self._stack.append(top)

    def _sort(self) -> None:
        if len(self._stack) <= 1:
            return
        top = self._stack.pop()
        self._sort()
        self._insert(top)

    def sort(self, items: List[int]) -> List[int]:
        self._stack = items[:]
        self._sort()
        return self._stack


def best(stack: List[int]) -> List[int]:
    return SortedStack().sort(stack)


if __name__ == "__main__":
    print("=== Sort a Stack using Recursion ===")
    s1 = [3, 1, 4, 1, 5, 9, 2, 6]
    s2 = s1[:]
    s3 = s1[:]
    print(f"Input:   {s1}")
    print(f"Brute:   {brute_force(s1)}")
    print(f"Optimal: {optimal(s2)}")
    print(f"Best:    {best(s3)}")
