"""
Problem: Implement Queue using Stack (LeetCode #232)
Difficulty: EASY | XP: 10

Implement a FIFO queue using only standard stack operations.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (Single Stack, Costly Pop)
# push O(1), pop O(n), peek O(n)
# ============================================================
class BruteForceQueue:
    """Transfer all to temp on every pop, pop oldest, transfer back."""

    def __init__(self):
        self.main = []
        self.temp = []

    def push(self, x: int) -> None:
        self.main.append(x)

    def pop(self) -> int:
        # Transfer all to temp (reverses order)
        while self.main:
            self.temp.append(self.main.pop())
        front = self.temp.pop()  # oldest element
        # Transfer back
        while self.temp:
            self.main.append(self.temp.pop())
        return front

    def peek(self) -> int:
        while self.main:
            self.temp.append(self.main.pop())
        front = self.temp[-1]
        while self.temp:
            self.main.append(self.temp.pop())
        return front

    def empty(self) -> bool:
        return len(self.main) == 0


# ============================================================
# APPROACH 2: OPTIMAL (Two Stacks, Lazy Transfer)
# push O(1), pop amortized O(1), peek amortized O(1)
# ============================================================
class OptimalQueue:
    """Inbox for pushes, outbox for pops. Transfer only when outbox is empty."""

    def __init__(self):
        self.inbox = []
        self.outbox = []

    def push(self, x: int) -> None:
        self.inbox.append(x)

    def pop(self) -> int:
        if not self.outbox:
            self._transfer()
        return self.outbox.pop()

    def peek(self) -> int:
        if not self.outbox:
            self._transfer()
        return self.outbox[-1]

    def empty(self) -> bool:
        return not self.inbox and not self.outbox

    def _transfer(self) -> None:
        """Move all from inbox to outbox (reverses order)."""
        while self.inbox:
            self.outbox.append(self.inbox.pop())


# ============================================================
# APPROACH 3: BEST (Two Stacks + Front Cache for O(1) Peek)
# push O(1), pop amortized O(1), peek O(1) worst-case
# ============================================================
class BestQueue:
    """Same as optimal but caches front element for O(1) peek."""

    def __init__(self):
        self.inbox = []
        self.outbox = []
        self.front = None

    def push(self, x: int) -> None:
        if not self.inbox:
            self.front = x  # track oldest element in inbox
        self.inbox.append(x)

    def pop(self) -> int:
        if not self.outbox:
            self._transfer()
        return self.outbox.pop()

    def peek(self) -> int:
        if self.outbox:
            return self.outbox[-1]
        return self.front  # O(1) without transfer

    def empty(self) -> bool:
        return not self.inbox and not self.outbox

    def _transfer(self) -> None:
        while self.inbox:
            self.outbox.append(self.inbox.pop())


# ============================================================
# DRIVER
# ============================================================
def test_queue(name: str, q) -> None:
    print(f"--- {name} ---")
    q.push(1)
    q.push(2)
    q.push(3)
    print("After push(1,2,3):")

    pk = q.peek()
    print(f"  peek()  = {pk} (expected 1) {'PASS' if pk == 1 else 'FAIL'}")

    p = q.pop()
    print(f"  pop()   = {p} (expected 1) {'PASS' if p == 1 else 'FAIL'}")

    p = q.pop()
    print(f"  pop()   = {p} (expected 2) {'PASS' if p == 2 else 'FAIL'}")

    q.push(4)
    pk = q.peek()
    print(f"  push(4), peek() = {pk} (expected 3) {'PASS' if pk == 3 else 'FAIL'}")

    p = q.pop()
    print(f"  pop()   = {p} (expected 3) {'PASS' if p == 3 else 'FAIL'}")

    p = q.pop()
    print(f"  pop()   = {p} (expected 4) {'PASS' if p == 4 else 'FAIL'}")

    e = q.empty()
    print(f"  empty() = {e} (expected True) {'PASS' if e else 'FAIL'}")
    print()


if __name__ == "__main__":
    print("=== Implement Queue using Stack ===\n")
    test_queue("Approach 1: Single Stack, Costly Pop", BruteForceQueue())
    test_queue("Approach 2: Two Stacks, Lazy Transfer", OptimalQueue())
    test_queue("Approach 3: Two Stacks + Front Cache", BestQueue())
