"""
Problem: Implement Queue using Linked List
Difficulty: EASY | XP: 10

Implement queue using singly linked list with enqueue, dequeue, front, isEmpty.
"""
from typing import Optional


# ============================================================
# NODE DEFINITION
# ============================================================
class ListNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.next: Optional['ListNode'] = None


# ============================================================
# QUEUE IMPLEMENTATION USING LINKED LIST
# All operations: O(1) time, O(1) space per operation
# ============================================================
class QueueLL:
    def __init__(self):
        self.front: Optional[ListNode] = None  # dequeue end (head)
        self.rear: Optional[ListNode] = None   # enqueue end (tail)
        self._size: int = 0

    def enqueue(self, val: int) -> None:
        """Add element to the back of the queue. O(1)."""
        new_node = ListNode(val)

        if self.is_empty():
            # First element: both front and rear point to it
            self.front = self.rear = new_node
        else:
            # Attach after rear, move rear forward
            self.rear.next = new_node
            self.rear = new_node

        self._size += 1

    def dequeue(self) -> int:
        """Remove and return the front element. O(1). Returns -1 if empty."""
        if self.is_empty():
            print("Queue Underflow! Cannot dequeue.")
            return -1

        val = self.front.val
        self.front = self.front.next

        # CRITICAL: reset rear when queue becomes empty
        if self.front is None:
            self.rear = None

        self._size -= 1
        return val

    def peek(self) -> int:
        """Return the front element without removing. O(1)."""
        if self.is_empty():
            print("Queue is empty! Cannot peek.")
            return -1
        return self.front.val

    def is_empty(self) -> bool:
        """Check if queue is empty. O(1)."""
        return self.front is None

    def size(self) -> int:
        """Return the number of elements. O(1)."""
        return self._size

    def display(self) -> str:
        """Return string representation of queue."""
        if self.is_empty():
            return "Queue: (empty)"
        parts = []
        current = self.front
        while current:
            parts.append(str(current.val))
            current = current.next
        return f"Queue front -> [{', '.join(parts)}] <- rear"


if __name__ == "__main__":
    print("=== Implement Queue using Linked List ===\n")

    queue = QueueLL()

    # Enqueue operations
    queue.enqueue(10)
    queue.enqueue(20)
    queue.enqueue(30)
    print(queue.display())  # front -> [10, 20, 30] <- rear

    # Peek
    print(f"Peek: {queue.peek()}")  # 10
    print(f"Size: {queue.size()}")  # 3

    # Dequeue operations
    print(f"\nDequeue: {queue.dequeue()}")  # 10
    print(queue.display())

    print(f"Dequeue: {queue.dequeue()}")  # 20
    print(queue.display())

    print(f"Dequeue: {queue.dequeue()}")  # 30
    print(queue.display())  # (empty)

    # Edge: isEmpty
    print(f"\nis_empty: {queue.is_empty()}")  # True

    # Edge: dequeue from empty
    print(f"Dequeue empty: {queue.dequeue()}")  # -1

    # Edge: enqueue after emptying
    print("\nEnqueue after emptying:")
    queue.enqueue(99)
    print(queue.display())  # front -> [99] <- rear
    print(f"Peek: {queue.peek()}")  # 99
    print(f"Size: {queue.size()}")  # 1
