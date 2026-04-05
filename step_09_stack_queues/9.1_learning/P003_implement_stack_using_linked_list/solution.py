"""
Problem: Implement Stack using Linked List
Difficulty: EASY | XP: 10

Implement stack using singly linked list.
Head of the list = top of the stack.
"""
from typing import Optional


# ============================================================
# NODE DEFINITION
# ============================================================
class Node:
    def __init__(self, val: int = 0):
        self.val = val
        self.next: Optional[Node] = None


# ============================================================
# STACK USING SINGLY LINKED LIST
# All operations: O(1) time
# Space: O(n) overall for n elements
# ============================================================
class LinkedListStack:
    def __init__(self):
        self.top: Optional[Node] = None  # head = top of stack
        self._size: int = 0

    def push(self, val: int) -> None:
        """Push element onto stack. Insert at head. Time: O(1)."""
        new_node = Node(val)
        new_node.next = self.top
        self.top = new_node
        self._size += 1

    def pop(self) -> int:
        """Remove and return top element. Delete head. Time: O(1)."""
        if self.top is None:
            print("Stack Underflow! Cannot pop.")
            return -1
        val = self.top.val
        self.top = self.top.next
        self._size -= 1
        return val

    def peek(self) -> int:
        """Return top element without removing. Time: O(1)."""
        if self.top is None:
            print("Stack is empty! Cannot peek.")
            return -1
        return self.top.val

    def is_empty(self) -> bool:
        """Check if stack is empty. Time: O(1)."""
        return self.top is None

    def size(self) -> int:
        """Return number of elements. Time: O(1)."""
        return self._size

    def print_stack(self) -> None:
        """Print stack from top to bottom."""
        if self.is_empty():
            print("Stack: (empty)")
            return
        elements = []
        current = self.top
        while current:
            elements.append(str(current.val))
            current = current.next
        print(f"Stack (top->bottom): [{', '.join(elements)}]")


if __name__ == "__main__":
    print("=== Implement Stack using Linked List ===\n")

    stack = LinkedListStack()

    # Push operations
    stack.push(10)
    stack.push(20)
    stack.push(30)
    stack.push(40)
    stack.print_stack()  # [40, 30, 20, 10]

    # Peek
    print(f"Peek: {stack.peek()}")        # 40
    print(f"Size: {stack.size()}")        # 4

    # Pop operations
    print(f"Pop:  {stack.pop()}")         # 40
    print(f"Pop:  {stack.pop()}")         # 30
    stack.print_stack()  # [20, 10]

    # Continue popping
    print(f"Pop:  {stack.pop()}")         # 20
    print(f"Pop:  {stack.pop()}")         # 10
    print(f"isEmpty: {stack.is_empty()}")  # True

    # Underflow test
    stack.pop()   # Underflow!
    stack.peek()  # Empty!

    # No overflow -- linked list grows dynamically
    print("\n--- No overflow (dynamic sizing) ---")
    big_stack = LinkedListStack()
    for i in range(1, 11):
        big_stack.push(i * 100)
    print(f"Pushed 10 elements. Size: {big_stack.size()}")
    big_stack.print_stack()

    # Pop all
    print("\n--- Pop all ---")
    while not big_stack.is_empty():
        print(f"Pop: {big_stack.pop()}")
    print(f"isEmpty: {big_stack.is_empty()}")
