"""
Problem: Implement Stack using Queue (LeetCode #225)
Difficulty: EASY | XP: 10

Implement a LIFO stack using only standard queue operations.
"""
from collections import deque


# ============================================================
# APPROACH 1: BRUTE FORCE (Two Queues, Costly Pop)
# push O(1), pop O(n), top O(n)
# ============================================================
class BruteForceStack:
    """Push is O(1). Pop moves n-1 elements to q2, pops last, swaps."""

    def __init__(self):
        self.q1 = deque()
        self.q2 = deque()

    def push(self, x: int) -> None:
        self.q1.append(x)

    def pop(self) -> int:
        # Move all but last to q2
        while len(self.q1) > 1:
            self.q2.append(self.q1.popleft())
        top = self.q1.popleft()  # last element is stack top
        self.q1, self.q2 = self.q2, self.q1  # swap
        return top

    def top(self) -> int:
        while len(self.q1) > 1:
            self.q2.append(self.q1.popleft())
        top = self.q1.popleft()
        self.q2.append(top)  # put it back
        self.q1, self.q2 = self.q2, self.q1
        return top

    def empty(self) -> bool:
        return len(self.q1) == 0


# ============================================================
# APPROACH 2: OPTIMAL (Two Queues, Costly Push)
# push O(n), pop O(1), top O(1)
# ============================================================
class OptimalStack:
    """Push enqueues into q2, moves all of q1 into q2, swaps. Newest is always at front."""

    def __init__(self):
        self.q1 = deque()
        self.q2 = deque()

    def push(self, x: int) -> None:
        self.q2.append(x)
        while self.q1:
            self.q2.append(self.q1.popleft())
        self.q1, self.q2 = self.q2, self.q1

    def pop(self) -> int:
        return self.q1.popleft()

    def top(self) -> int:
        return self.q1[0]

    def empty(self) -> bool:
        return len(self.q1) == 0


# ============================================================
# APPROACH 3: BEST (Single Queue, Costly Push with Rotation)
# push O(n), pop O(1), top O(1)
# ============================================================
class BestStack:
    """Single queue. After push, rotate previous elements behind the new one."""

    def __init__(self):
        self.q = deque()

    def push(self, x: int) -> None:
        self.q.append(x)
        # Rotate: move (size - 1) elements from front to back
        for _ in range(len(self.q) - 1):
            self.q.append(self.q.popleft())

    def pop(self) -> int:
        return self.q.popleft()

    def top(self) -> int:
        return self.q[0]

    def empty(self) -> bool:
        return len(self.q) == 0


# ============================================================
# DRIVER
# ============================================================
def test_stack(name: str, stack) -> None:
    print(f"--- {name} ---")
    stack.push(1)
    stack.push(2)
    stack.push(3)
    print(f"After push(1,2,3):")

    t = stack.top()
    print(f"  top()   = {t} (expected 3) {'PASS' if t == 3 else 'FAIL'}")

    p = stack.pop()
    print(f"  pop()   = {p} (expected 3) {'PASS' if p == 3 else 'FAIL'}")

    p = stack.pop()
    print(f"  pop()   = {p} (expected 2) {'PASS' if p == 2 else 'FAIL'}")

    t = stack.top()
    print(f"  top()   = {t} (expected 1) {'PASS' if t == 1 else 'FAIL'}")

    e = stack.empty()
    print(f"  empty() = {e} (expected False) {'PASS' if not e else 'FAIL'}")

    p = stack.pop()
    print(f"  pop()   = {p} (expected 1) {'PASS' if p == 1 else 'FAIL'}")

    e = stack.empty()
    print(f"  empty() = {e} (expected True) {'PASS' if e else 'FAIL'}")
    print()


if __name__ == "__main__":
    print("=== Implement Stack using Queue ===\n")
    test_stack("Approach 1: Two Queues, Costly Pop", BruteForceStack())
    test_stack("Approach 2: Two Queues, Costly Push", OptimalStack())
    test_stack("Approach 3: Single Queue, Costly Push", BestStack())
