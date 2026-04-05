"""
Problem: Implement Min Stack (LeetCode 155)
Difficulty: MEDIUM | XP: 25

Design a stack that supports push, pop, top, and retrieving the minimum
element in constant time.

Implement the MinStack class:
  - MinStack()    : initializes the stack object.
  - push(val)     : pushes element val onto the stack.
  - pop()         : removes the element on the top of the stack.
  - top()         : gets the top element of the stack.
  - getMin()      : retrieves the minimum element in the stack.

All operations must run in O(1) time.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE — Stack of (value, current_min) pairs
# Time: O(1) all ops  |  Space: O(n) — stores min at every level
# ============================================================
class MinStackBrute:
    """
    Each entry on the stack is a tuple (value, min_so_far).
    When pushing, compute the new running minimum and store it alongside
    the value. getMin() simply reads the top tuple's second element.
    """
    def __init__(self):
        self._stack = []  # list of (value, min_upto_here)

    def push(self, val: int) -> None:
        current_min = val if not self._stack else min(val, self._stack[-1][1])
        self._stack.append((val, current_min))

    def pop(self) -> None:
        self._stack.pop()

    def top(self) -> int:
        return self._stack[-1][0]

    def getMin(self) -> int:
        return self._stack[-1][1]


# ============================================================
# APPROACH 2: OPTIMAL — Encoded value trick (single stack, O(1) extra)
# Time: O(1) all ops  |  Space: O(1) extra beyond the stack itself
# ============================================================
class MinStackOptimal:
    """
    Use a single stack that stores encoded values.
    Maintain a separate `min_val` variable.

    Encoding:
    - If val >= min_val: push val normally.
    - If val < min_val: push (2 * val - min_val) — a value below the new min.
      This encodes the OLD min inside the stack entry.

    Decoding during pop:
    - If top >= min_val: straightforward pop.
    - If top < min_val: the old min was (2 * min_val - top); restore it.

    top() similarly: if stored < min_val, real top = min_val; else stored value.

    NOTE: This can overflow with 32-bit integers; use Python's arbitrary ints
    or long in Java. LeetCode uses int but doesn't test overflow cases for
    this particular trick in practice. For a safe alternative, see Approach 3.
    """
    def __init__(self):
        self._stack = []
        self._min = float('inf')

    def push(self, val: int) -> None:
        if not self._stack:
            self._stack.append(val)
            self._min = val
        elif val < self._min:
            self._stack.append(2 * val - self._min)
            self._min = val
        else:
            self._stack.append(val)

    def pop(self) -> None:
        top = self._stack.pop()
        if top < self._min:
            # Restore the previous min
            self._min = 2 * self._min - top

    def top(self) -> int:
        top = self._stack[-1]
        return self._min if top < self._min else top

    def getMin(self) -> int:
        return self._min


# ============================================================
# APPROACH 3: BEST — Two-stack approach (auxiliary min-stack)
# Time: O(1) all ops  |  Space: O(n) worst case but clear and safe
# ============================================================
class MinStackBest:
    """
    Maintain two stacks:
    - `stack`     : normal value stack.
    - `min_stack` : auxiliary stack that tracks minimums.
      Push to min_stack when the new value <= current min.
      Pop from min_stack when the popped value == current min.

    This is cleaner than the encoding trick and avoids overflow.
    It uses slightly more space only when elements repeat at minimum.
    """
    def __init__(self):
        self._stack = []
        self._min_stack = []

    def push(self, val: int) -> None:
        self._stack.append(val)
        if not self._min_stack or val <= self._min_stack[-1]:
            self._min_stack.append(val)

    def pop(self) -> None:
        val = self._stack.pop()
        if val == self._min_stack[-1]:
            self._min_stack.pop()

    def top(self) -> int:
        return self._stack[-1]

    def getMin(self) -> int:
        return self._min_stack[-1]


if __name__ == "__main__":
    print("=== Implement Min Stack ===")
    for name, cls in [("Brute", MinStackBrute), ("Optimal", MinStackOptimal), ("Best", MinStackBest)]:
        ms = cls()
        ms.push(-2)
        ms.push(0)
        ms.push(-3)
        print(f"{name}  getMin={ms.getMin()}  top={ms.top()}", end="")  # getMin=-3
        ms.pop()
        print(f"  after pop: top={ms.top()}  getMin={ms.getMin()}")     # top=0, getMin=-2
