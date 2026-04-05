"""
Problem: Implement Queue using Arrays
Difficulty: EASY | XP: 10

Implement queue with enqueue, dequeue, front, isEmpty, size using circular array.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE - Linear Array with Shift
# enqueue: O(1)  |  dequeue: O(n) due to shifting
# ============================================================
class NaiveQueue:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.arr = [0] * capacity
        self._size = 0

    def enqueue(self, val: int) -> None:
        if self._size == self.capacity:
            print("Queue Overflow!")
            return
        self.arr[self._size] = val
        self._size += 1

    def dequeue(self) -> int:
        if self._size == 0:
            print("Queue Underflow!")
            return -1
        val = self.arr[0]
        # Shift all elements left -- O(n)
        for i in range(self._size - 1):
            self.arr[i] = self.arr[i + 1]
        self._size -= 1
        return val

    def front(self) -> int:
        if self._size == 0:
            return -1
        return self.arr[0]

    def is_empty(self) -> bool:
        return self._size == 0

    def size(self) -> int:
        return self._size


# ============================================================
# APPROACH 2: OPTIMAL - Circular Array Queue
# All operations: O(1) time
# Uses modulo for wrap-around indexing.
# ============================================================
class CircularQueue:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.arr = [0] * capacity
        self._front = 0
        self._rear = 0
        self._size = 0

    def enqueue(self, val: int) -> None:
        """Add element to rear. Time: O(1)."""
        if self._size == self.capacity:
            print(f"Queue Overflow! Cannot enqueue {val}")
            return
        self.arr[self._rear] = val
        self._rear = (self._rear + 1) % self.capacity  # circular wrap
        self._size += 1

    def dequeue(self) -> int:
        """Remove and return front element. Time: O(1)."""
        if self._size == 0:
            print("Queue Underflow!")
            return -1
        val = self.arr[self._front]
        self._front = (self._front + 1) % self.capacity  # circular wrap
        self._size -= 1
        return val

    def front(self) -> int:
        """Return front element without removing. Time: O(1)."""
        if self._size == 0:
            print("Queue is empty!")
            return -1
        return self.arr[self._front]

    def is_empty(self) -> bool:
        """Check if queue is empty. Time: O(1)."""
        return self._size == 0

    def size(self) -> int:
        """Return number of elements. Time: O(1)."""
        return self._size

    def print_queue(self) -> None:
        """Print queue from front to rear."""
        if self.is_empty():
            print("Queue: (empty)")
            return
        elements = []
        for i in range(self._size):
            index = (self._front + i) % self.capacity
            elements.append(str(self.arr[index]))
        print(f"Queue (front->rear): [{', '.join(elements)}]")


if __name__ == "__main__":
    print("=== Implement Queue using Arrays ===\n")

    # --- Naive Queue Demo ---
    print("--- Naive Queue (dequeue is O(n)) ---")
    nq = NaiveQueue(5)
    nq.enqueue(10)
    nq.enqueue(20)
    nq.enqueue(30)
    print(f"Front: {nq.front()}")        # 10
    print(f"Dequeue: {nq.dequeue()}")    # 10
    print(f"Front: {nq.front()}")        # 20
    print(f"Size: {nq.size()}")          # 2

    # --- Circular Queue Demo ---
    print("\n--- Circular Queue (all O(1)) ---")
    cq = CircularQueue(4)

    cq.enqueue(10)
    cq.enqueue(20)
    cq.enqueue(30)
    cq.print_queue()  # [10, 20, 30]

    print(f"Front: {cq.front()}")         # 10
    print(f"Dequeue: {cq.dequeue()}")     # 10
    print(f"Dequeue: {cq.dequeue()}")     # 20
    cq.print_queue()  # [30]

    # Demonstrate wrap-around
    cq.enqueue(40)
    cq.enqueue(50)
    cq.enqueue(60)  # wraps around to index 0
    cq.print_queue()  # [30, 40, 50, 60]
    print(f"Size: {cq.size()}")           # 4

    # Overflow test
    cq.enqueue(70)  # Overflow!

    # Empty it all
    print("\n--- Empty the queue ---")
    while not cq.is_empty():
        print(f"Dequeue: {cq.dequeue()}")
    print(f"isEmpty: {cq.is_empty()}")    # True

    # Underflow test
    cq.dequeue()  # Underflow!
